package com.raptor.uploader.service;

import com.raptor.uploader.entity.BlockFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author raptor
 * @description BlockFileService
 * @date 2021/12/6 16:06
 */
public interface BlockFileService {
    BlockFile insert(BlockFile blockFile);

    BlockFile findByChunkAndMd5(Integer chunk, String md5);

    Integer selectBlockNum( String md5);

    List<Integer> selectBlockByMd5(@Param("md5") String md5);
}
