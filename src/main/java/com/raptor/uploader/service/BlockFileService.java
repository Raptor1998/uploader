package com.raptor.uploader.service;

import com.raptor.uploader.entity.BlockFile;

/**
 * @author raptor
 * @description BlockFileService
 * @date 2021/12/6 16:06
 */
public interface BlockFileService {
    BlockFile insert(BlockFile blockFile);

    BlockFile findByChunkAndMd5(Integer chunk, String md5);
}