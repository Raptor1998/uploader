package com.raptor.uploader.service.impl;

import com.raptor.uploader.entity.BlockFile;
import com.raptor.uploader.enume.ResultEnum;
import com.raptor.uploader.exception.DescribeException;
import com.raptor.uploader.mapper.BlockFileMapper;
import com.raptor.uploader.service.BlockFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author raptor
 * @description BlockFileServiceImpl
 * @date 2021/12/6 16:08
 */
@Service
public class BlockFileServiceImpl implements BlockFileService {

    private BlockFileMapper blockFileMapper;

    @Autowired
    public BlockFileServiceImpl(BlockFileMapper blockFileMapper) {
        this.blockFileMapper = blockFileMapper;
    }

    @Override
    public BlockFile insert(BlockFile blockFile) {
        int i = blockFileMapper.insert(blockFile);
        if (i > 0) {
            return blockFile;
        }
        throw new DescribeException(ResultEnum.FILE_INFO_INSERT_ERROR);
    }

    @Override
    public BlockFile findByChunkAndMd5(Integer chunk, String md5) {
        return blockFileMapper.findByChunkAndMd5(chunk, md5);
    }
}
