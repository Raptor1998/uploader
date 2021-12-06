package com.raptor.uploader.controller;

import com.raptor.uploader.entity.FileInfo;
import com.raptor.uploader.enume.ResultEnum;
import com.raptor.uploader.service.FileInfoService;
import com.raptor.uploader.service.UploaderService;
import com.raptor.uploader.utils.response.Result;
import com.raptor.uploader.utils.response.ResultUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author raptor
 * @description UploadController
 * @date 2021/12/4 16:35
 */
@RestController
public class UploadController {

    FileInfoService fileInfoService;
    UploaderService uploaderService;

    public UploadController(FileInfoService fileInfoService, UploaderService uploaderService) {
        this.fileInfoService = fileInfoService;
        this.uploaderService = uploaderService;
    }

    @PostMapping("/single")
    public Result uploadSingleFile(@RequestParam("file") MultipartFile file, @RequestParam(value = "md5", required = false) String md5) {
        uploaderService.saveFile(file, md5);
        return ResultUtil.success(null);
    }

    /**
     * @param fileName 上传者机器的文件名
     * @param md5      md5值
     * @return
     */
    @PostMapping("/quick")
    public Result uploadQuick(@RequestParam("fileName") String fileName, @RequestParam("md5") String md5) {
        fileInfoService.insertByFileName(fileName, md5);
        return ResultUtil.success(null);
    }

    @GetMapping("/exist/{md5}")
    public Result existByMd5(@RequestParam(value = "md5") String md5) {
        FileInfo fileInfo = fileInfoService.selectByMd5(md5);
        if (!ObjectUtils.isEmpty(fileInfo)) {
            return ResultUtil.success(null);
        } else {
            return ResultUtil.defineFail(ResultEnum.FILE_NOT_EXIST);
        }
    }


    @PostMapping("/block/single")
    public Result uploadBigSingleFile(@RequestParam("originalName") String originalName,
                                      @RequestParam("file") MultipartFile file,
                                      @RequestParam("chunks") Integer chunks,
                                      @RequestParam("chunk") Integer chunk,
                                      @RequestParam("size") Long size,
                                      @RequestParam("md5") String md5) {
        uploaderService.uploadWithBlock(originalName, file, chunks, size, chunk, md5);
        return ResultUtil.success(null);
    }


}
