package com.raptor.uploader.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.raptor.uploader.entity.FileInfo;
import com.raptor.uploader.enume.ResultEnum;
import com.raptor.uploader.exception.DescribeException;
import com.raptor.uploader.mapper.FileInfoMapper;
import com.raptor.uploader.service.FileInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author raptor
 * @description FileInfoServiceImpl
 * @date 2021/12/5 15:37
 */
@Service
@Slf4j
public class FileInfoServiceImpl implements FileInfoService {

    private FileInfoMapper fileInfoMapper;

    @Value("${uploader.file.prefixUrl}")
    private String prefixUrl;

    @Autowired
    public FileInfoServiceImpl(FileInfoMapper fileInfoMapper) {
        this.fileInfoMapper = fileInfoMapper;
    }

    @Override
    public FileInfo selectByMd5(String md5) {
        return fileInfoMapper.selectByMd5(md5);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileInfo insert(FileInfo fileInfo) {
        int flag = fileInfoMapper.insert(fileInfo);
        if (flag > 0) {
            log.info("文件信息：{}", fileInfo);
            return fileInfo;
        }
        throw new DescribeException(ResultEnum.FILE_INFO_INSERT_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileInfo insertByFileName(String fileName, String md5) {
        FileInfo fileInfo = fileInfoMapper.selectByMd5(md5);
        if (ObjectUtils.isEmpty(fileInfo)) {
            log.info("文件的md5值不存在,{}", md5);
            throw new DescribeException(ResultEnum.FILE_NOT_EXIST_SECONDS);
        }
        log.info("已存在的文件，md5:{}", md5);
        FileInfo fileInfoUp = new FileInfo();
        fileInfoUp.setFileOriginalName(fileName);
        fileInfoUp.setFileUnionName(fileInfo.getFileUnionName());
        fileInfoUp.setFileSuffix(fileInfo.getFileSuffix());
        fileInfoUp.setFileSize(fileInfo.getFileSize());
        fileInfoUp.setFileUploadTime(new Date());
        fileInfoUp.setFileRealPath(fileInfo.getFileRealPath());
        fileInfoUp.setFileUrl(fileInfo.getFileUrl());
        fileInfoUp.setMd5(md5);
        FileInfo insert = insert(fileInfoUp);
        return insert;
    }

    @Override
    public Map<String, Object> selectAll(int page, int size) {
        Page<Object> objects = PageHelper.startPage(page, size);
        List<FileInfo> fileInfos = fileInfoMapper.selectAll();
        fileInfos.stream().forEach(item -> {
            item.setFileUrl(prefixUrl + item.getFileUrl());
        });
        Map<String, Object> result = new HashMap<>(3);
        result.put("total", objects.getTotal());
        result.put("fileInfos", fileInfos);
        return result;
    }
}
