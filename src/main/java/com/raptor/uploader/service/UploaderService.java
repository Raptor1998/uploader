package com.raptor.uploader.service;

import com.raptor.uploader.config.properties.FileConstraintsProperties;
import com.raptor.uploader.entity.FileInfo;
import com.raptor.uploader.enume.ResultEnum;
import com.raptor.uploader.exception.DescribeException;
import com.raptor.uploader.mapper.FileInfoMapper;
import com.raptor.uploader.utils.uploader.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @author raptor
 * @description UploaderService
 * @date 2021/12/5 14:44
 */
@EnableConfigurationProperties(FileConstraintsProperties.class)
@Service
@Slf4j
public class UploaderService {

    private FileInfoService FileInfoService;
    private FileConstraintsProperties fileConstraintsProperties;
    private static final int SIZE_UNIT = 1024 * 1024;

    @Autowired
    public UploaderService(FileInfoService FileInfoService, FileConstraintsProperties fileConstraintsProperties) {
        this.FileInfoService = FileInfoService;
        this.fileConstraintsProperties = fileConstraintsProperties;
    }

    public void saveFile(MultipartFile file,String md5) {
        checkFile(file);
        try {
            String extractFilename = extractFilename(file.getOriginalFilename());
            FileUtils.write(fileConstraintsProperties.getPath() + extractFilename, file.getInputStream());
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileOriginalName(file.getOriginalFilename());
            fileInfo.setFileUnionName(extractFilename);
            fileInfo.setFileRealPath(fileConstraintsProperties.getPath() + extractFilename);
            fileInfo.setFileSuffix(getExtension(file.getOriginalFilename()));
            fileInfo.setFileUrl(extractFilename);
            fileInfo.setFileSize(file.getSize());
            fileInfo.setFileUploadTime(new Date());
            fileInfo.setMd5(md5);
            FileInfoService.insert(fileInfo);
        } catch (Exception e) {
            log.error("文件保存时出错，重试");
            throw new DescribeException(ResultEnum.FILE_IO_EXCEPTION);
        }
    }


    private void checkFile(MultipartFile file) {
        if (StringUtils.trimToNull(fileConstraintsProperties.getPath()) == null) {
            log.error("文件保存路径未配置");
            throw new DescribeException(ResultEnum.FILE_PATH_IS_NULL);
        }

        String extensionFilename = getExtension(file.getOriginalFilename());

        if (!ArrayUtils.isEmpty(fileConstraintsProperties.getExtension()) && !ArrayUtils.contains(fileConstraintsProperties.getExtension(), extensionFilename)) {
            log.error("文件类型不予许，{}", file.getOriginalFilename());
            throw new DescribeException(ResultEnum.FILE_EXTENSION_NOT_ACCEPT);
        }

        if (file.getSize() > fileConstraintsProperties.getSize() * SIZE_UNIT) {
            log.error("文件过大,{}", file.getOriginalFilename());
            throw new DescribeException(ResultEnum.FILE_SIZE_LARGER);
        }

    }


    private final String extractFilename(String fileName) {
        String extension = getExtension(fileName);
        String newFilename;
        if (fileConstraintsProperties.getFolderRole() == true) {
            newFilename = DateFormatUtils.format(new Date(), "/yyyy/MM/dd") + "/" + encodingFilename(fileName) + "." + extension;
        } else {
            newFilename = encodingFilename(fileName) + "." + extension;
        }
        return newFilename;
    }

    private final String encodingFilename(String fileName) {
        fileName = fileName.replace("_", " ");
        fileName = DigestUtils.md5Hex(fileName + UUID.randomUUID().toString() + RandomStringUtils.randomNumeric(6));
        return fileName;
    }

    private String getExtension(String originalFileName) {
        String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        return suffix;
    }


}
