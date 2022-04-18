package com.raptor.uploader.service;

import com.raptor.uploader.config.properties.FileConstraintsProperties;
import com.raptor.uploader.entity.BlockFile;
import com.raptor.uploader.entity.FileInfo;
import com.raptor.uploader.enume.ResultEnum;
import com.raptor.uploader.exception.DescribeException;
import com.raptor.uploader.utils.uploader.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author raptor
 * @description UploaderService
 * @date 2021/12/5 14:44
 */
@EnableConfigurationProperties(FileConstraintsProperties.class)
@Service
@Slf4j
public class UploaderService {

    private FileInfoService fileInfoService;
    private BlockFileService blockFileService;
    private FileConstraintsProperties fileConstraintsProperties;
    private ThreadPoolExecutor poolExecutor;
    private static final int SIZE_UNIT = 1024 * 1024;

    @Autowired
    public UploaderService(FileInfoService fileInfoService, BlockFileService blockFileService, FileConstraintsProperties fileConstraintsProperties, ThreadPoolExecutor poolExecutor) {
        this.fileInfoService = fileInfoService;
        this.blockFileService = blockFileService;
        this.fileConstraintsProperties = fileConstraintsProperties;
        this.poolExecutor = poolExecutor;
    }

    /**
     * 完整的单个上传文件测试，并保存数据库
     *
     * @param file
     * @param md5
     */
    public FileInfo saveFile(MultipartFile file, String md5) {
        checkFile(file);
        try {
            String extractFilename = extractFilename(file.getOriginalFilename(), md5);
            FileUtils.write(fileConstraintsProperties.getPath() + extractFilename, file.getInputStream());
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileOriginalName(file.getOriginalFilename());
            fileInfo.setFileUnionName(extractFilename);
            fileInfo.setFileRealPath(fileConstraintsProperties.getPath() + extractFilename);
            fileInfo.setFileSuffix(getExtension(file.getOriginalFilename()));
            fileInfo.setFileUrl(fileConstraintsProperties.getPrefixUrl()+extractFilename);
            fileInfo.setFileSize(file.getSize());
            fileInfo.setFileUploadTime(new Date());
            fileInfo.setMd5(md5);
            FileInfo insert = fileInfoService.insert(fileInfo);
            return insert;
        } catch (Exception e) {
            log.error("文件保存时出错，重试");
            log.error(e.getMessage());
            throw new DescribeException(ResultEnum.FILE_IO_EXCEPTION);
        }
    }

    /**
     * 检查问价是否符合上传标准
     *
     * @param file
     */
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

    /**
     * 生成unionName   根据原文件名+md5
     *
     * @param fileName
     * @param md5
     * @return
     */
    private final String extractFilename(String fileName, String md5) {
        String extension = getExtension(fileName);
        String newFilename;
        if (fileConstraintsProperties.getFolderRole() == true) {
            newFilename = DateFormatUtils.format(new Date(), "yyyy/MM/dd") + "/" + encodingFilename(fileName, md5) + "." + extension;
        } else {
            newFilename = encodingFilename(fileName, md5) + "." + extension;
        }
        return newFilename;
    }

    /**
     * 编码文件名
     *
     * @param fileName
     * @param md5
     * @return
     */
    private final String encodingFilename(String fileName, String md5) {
        fileName = fileName.replace("_", " ");
        fileName = DigestUtils.md5Hex(fileName + md5 + UUID.randomUUID().toString());
        return fileName;
    }

    /**
     * 文件后缀
     *
     * @param originalFileName
     * @return
     */
    private String getExtension(String originalFileName) {
        String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        return suffix;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public BlockFile uploadWithBlock(String originalName, MultipartFile file, Integer chunks, Long size, Integer chunk, String md5) {
        if (chunk >= chunks) {
            throw new DescribeException(ResultEnum.FILE_IO_EXCEPTION);
        }
        String newFilename = extractFilename(originalName, md5);
        BlockFile byChunkAndMd5 = blockFileService.findByChunkAndMd5(chunk, md5);
        if (!ObjectUtils.isEmpty(byChunkAndMd5)) {
            log.info("分片已经存在，chunk：{}，md5：{}", chunk, md5);
            throw new DescribeException(ResultEnum.FILE_CHUNK_EXIST);
        }
        BlockFile blockFile = new BlockFile();
        blockFile.setBlockFileChunk(chunk);
        blockFile.setBlockFileMd5(md5);
        blockFile.setUploadTime(new Date());
        blockFile.setTempPath(fileConstraintsProperties.getPath() + newFilename);
        log.info(blockFile.toString());
        BlockFile block = blockFileService.insert(blockFile);

        try {
            InputStream inputStream = file.getInputStream();
            log.info("正在上传的文件：{}，unionName：{}，第{}块，共{}块", originalName, newFilename, chunk, chunks);
            RandomAccessFile randomAccessFile = null;
            randomAccessFile = new RandomAccessFile(fileConstraintsProperties.getPath() + newFilename, "rw");
            Long chunkSize = file.getSize();
            randomAccessFile.setLength(size);
            if (chunk == chunks - 1) {
                randomAccessFile.seek(size - chunkSize);
            } else {
                randomAccessFile.seek(chunk * chunkSize);
            }
            byte[] buf = new byte[1024];
            int len;
            while (-1 != (len = inputStream.read(buf))) {
                randomAccessFile.write(buf, 0, len);
            }
            randomAccessFile.close();
            return block;
        } catch (Exception e) {
            log.error("块保存出错");
            e.printStackTrace();
            throw new DescribeException(ResultEnum.FILE_CHUNK_EXIST);
        } finally {
            Integer blockNum = blockFileService.selectBlockNum(md5);
            log.info("已上传块数：{}，总块数：{}", blockNum, chunks);
            if (!ObjectUtils.isEmpty(blockNum) && blockNum.equals(chunks)) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileOriginalName(originalName);
                fileInfo.setFileUnionName(newFilename);
                fileInfo.setFileRealPath(fileConstraintsProperties.getPath() + newFilename);
                fileInfo.setFileSuffix(getExtension(originalName));
                fileInfo.setFileUrl(fileConstraintsProperties+newFilename);
                fileInfo.setFileSize(size);
                fileInfo.setFileUploadTime(new Date());
                fileInfo.setMd5(md5);
                fileInfoService.insert(fileInfo);
            }
        }
    }


}
