package com.raptor.uploader.mapper;

import com.raptor.uploader.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface FileInfoMapper {
    int deleteByPrimaryKey(Long fileId);

    int insert(FileInfo record);

    FileInfo selectByPrimaryKey(Long fileId);

    List<FileInfo> selectAll();

    int updateByPrimaryKey(FileInfo record);

    FileInfo selectByMd5(@Param("md5") String md5);
}