package com.raptor.uploader.service;

import com.raptor.uploader.entity.FileInfo;

import java.util.Map;

/**
 * @author raptor
 * @description FileInfoService
 * @date 2021/12/5 15:36
 */
public interface FileInfoService {
    FileInfo selectByMd5(String md5);

    FileInfo insert(FileInfo fileInfo);

    FileInfo insertByFileName(String fileName, String md5);

    Map<String, Object> selectAll(int page, int size);

}
