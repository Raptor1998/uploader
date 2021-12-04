package com.raptor.uploader.service;

import com.raptor.uploader.entity.FileInfo;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author raptor
 * @description FileInfoService
 * @date 2021/12/4 16:48
 */
public interface FileInfoService {

    /**
     * 新增一个文件信息
     *
     * @param fileInfo 文件详情
     * @return
     */
    FileInfo insert(FileInfo fileInfo);

}
