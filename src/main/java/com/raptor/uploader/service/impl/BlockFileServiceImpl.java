package com.raptor.uploader.service.impl;

import com.raptor.uploader.entity.BlockFile;
import com.raptor.uploader.enume.ResultEnum;
import com.raptor.uploader.exception.DescribeException;
import com.raptor.uploader.mapper.BlockFileMapper;
import com.raptor.uploader.service.BlockFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author raptor
 * @description BlockFileServiceImpl
 * @date 2021/12/6 16:08
 */
@Service
@Slf4j
public class BlockFileServiceImpl implements BlockFileService {

    private BlockFileMapper blockFileMapper;

    @Autowired
    public BlockFileServiceImpl(BlockFileMapper blockFileMapper) {
        this.blockFileMapper = blockFileMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BlockFile insert(BlockFile blockFile) {
        try {
            int i = blockFileMapper.insert(blockFile);
            return blockFile;
        } catch (Exception e) {
            log.error("分片已经存在，数据库插入时出错");
            e.printStackTrace();
            throw new DescribeException(ResultEnum.FILE_INFO_INSERT_ERROR);
        }
    }

    @Override
    public BlockFile findByChunkAndMd5(Integer chunk, String md5) {
        return blockFileMapper.findByChunkAndMd5(chunk, md5);
    }

    @Override
    public Integer selectBlockNum(String md5) {
        return blockFileMapper.selectBlockNum(md5);
    }

    @Override
    public List<Integer> selectBlockByMd5(String md5) {
        return blockFileMapper.selectBlockByMd5(md5);
    }
}
