package com.raptor.uploader.service.impl;

import com.raptor.uploader.entity.FileInfo;
import com.raptor.uploader.enume.ResultEnum;
import com.raptor.uploader.exception.DescribeException;
import com.raptor.uploader.mapper.FileInfoMapper;
import com.raptor.uploader.service.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author raptor
 * @description FileInfoServiceImpl
 * @date 2021/12/4 16:50
 */
@Service
public class FileInfoServiceImpl implements FileInfoService {

    private FileInfoMapper fileInfoMapper;

    @Autowired
    public FileInfoServiceImpl(FileInfoMapper fileInfoMapper) {
        this.fileInfoMapper = fileInfoMapper;
    }

    @Override
    public FileInfo insert(FileInfo fileInfo) {
        int i = fileInfoMapper.insert(fileInfo);
        if (i > 0) {
            return fileInfo;
        }
        throw new DescribeException(ResultEnum.FILE_INFO_INSERT_ERROR);
    }
}
