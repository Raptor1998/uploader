package com.raptor.uploader.mapper;

import com.raptor.uploader.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileInfoMapper {
    int deleteByPrimaryKey(Long fileId);

    int insert(FileInfo record);

    FileInfo selectByPrimaryKey(Long fileId);

    List<FileInfo> selectAll();

    int updateByPrimaryKey(FileInfo record);
}