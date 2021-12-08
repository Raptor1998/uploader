package com.raptor.uploader.mapper;

import com.raptor.uploader.entity.BlockFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlockFileMapper {
    int deleteByPrimaryKey(Integer blockFileId);

    int insert(BlockFile record);


    BlockFile findByChunkAndMd5(@Param("chunk") Integer chunk, @Param("md5") String md5);

    Integer selectBlockNum(@Param("md5") String md5);

    List<Integer> selectBlockByMd5(@Param("md5") String md5);
}