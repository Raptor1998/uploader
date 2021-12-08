package com.raptor.uploader.controller;

import com.raptor.uploader.service.BlockFileService;
import com.raptor.uploader.service.FileInfoService;
import com.raptor.uploader.utils.response.Result;
import com.raptor.uploader.utils.response.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author raptor
 * @description FileInfoController
 * @date 2021/12/5 17:28
 */
@RestController
public class FileInfoController {

    FileInfoService fileInfoService;
    BlockFileService blockFileService;

    @Autowired
    public FileInfoController(FileInfoService fileInfoService, BlockFileService blockFileService) {
        this.fileInfoService = fileInfoService;
        this.blockFileService = blockFileService;
    }

    /**
     * 所有的文件信息
     *
     * @param page 页码
     * @param size 大小
     * @return
     */
    @GetMapping("/getAll/files")
    public Result getALlFiles(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> map = fileInfoService.selectAll(page, size);
        return ResultUtil.success(map);
    }

    /**
     * 根据md5的值查询已经上传了哪些分片
     *
     * @param md5
     * @return
     */
    @GetMapping("/getAll/blocks")
    public Result getAllBlocksByMd5(@RequestParam("md5") String md5) {
        return ResultUtil.success(blockFileService.selectBlockByMd5(md5));
    }
}
