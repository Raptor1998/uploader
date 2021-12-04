package com.raptor.uploader.controller;

import com.raptor.uploader.service.FileInfoService;
import com.raptor.uploader.utils.response.Result;
import com.raptor.uploader.utils.response.ResultUtil;
import com.raptor.uploader.utils.uploader.FileUploadUtils;
import com.raptor.uploader.utils.uploader.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author raptor
 * @description UploadController
 * @date 2021/12/4 16:35
 */
@RestController
@RequestMapping(value = "/upload")
public class UploadController {

    UploaderServer uploaderServer;

    @Autowired
    public UploadController(UploaderServer uploaderServer) {
        this.uploaderServer = uploaderServer;
    }

    @PostMapping("/single")
    public Result uploadSingleFile(MultipartFile file) throws IOException {
        uploaderServer.upload(file);
        return ResultUtil.success(null);
    }

    @PostMapping("/multiple")
    public Result uploadMultipleFile(MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            uploaderServer.upload(file);
        }
        return ResultUtil.success(null);
    }

}
