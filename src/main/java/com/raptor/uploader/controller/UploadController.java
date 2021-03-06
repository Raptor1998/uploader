package com.raptor.uploader.controller;

import com.raptor.uploader.entity.FileInfo;
import com.raptor.uploader.enume.ResultEnum;
import com.raptor.uploader.service.FileInfoService;
import com.raptor.uploader.service.UploaderService;
import com.raptor.uploader.utils.response.Result;
import com.raptor.uploader.utils.response.ResultUtil;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    /**
     * 直接上传单个文件
     *
     * @param file
     * @param md5
     * @return
     */
    @PostMapping("/single")
    public Result uploadSingleFile(@RequestParam("file") MultipartFile file, @RequestParam(value = "md5", required = false) String md5) {
        if (md5 == null || "".equals(md5)) {
            md5 = UUID.randomUUID().toString().substring(0, 32);
        }
        FileInfo fileInfo = uploaderService.saveFile(file, md5);
        return ResultUtil.success(fileInfo);
    }

    @PostMapping("/multiple")
    public Result uploadmultipleFile(@RequestParam("files") MultipartFile[] files, @RequestParam("md5s") String[] md5s) {
        if (md5s.length == 0 || md5s.length < files.length) {
            List<FileInfo> res = new ArrayList<>();
            for (MultipartFile multipartFile : files) {
                //随机生成一个md5
                FileInfo fileInfo = uploaderService.saveFile(multipartFile, UUID.randomUUID().toString().substring(0, 32));
                res.add(fileInfo);
            }
            return ResultUtil.success(res);
        }else {
            List<FileInfo> res = new ArrayList<>();
            int index = 0;
            for (MultipartFile multipartFile : files) {
                FileInfo fileInfo = uploaderService.saveFile(multipartFile, md5s[index++]);
                res.add(fileInfo);
            }
            return ResultUtil.success(res);
        }

    }

    /**
     * 秒传
     *
     * @param fileName 上传者机器的文件名
     * @param md5      md5值
     * @return
     */
    @PostMapping("/quick")
    public Result uploadQuick(@RequestParam("fileName") String fileName, @RequestParam("md5") String md5) {
        FileInfo fileInfo = fileInfoService.insertByFileName(fileName, md5);
        return ResultUtil.success(fileInfo);
    }

    /**
     * 根据md5查询文件是否存在
     *
     * @param md5
     * @return
     */
    @GetMapping("/exist/md5")
    public Result existByMd5(@RequestParam(value = "md5") String md5) {
        FileInfo fileInfo = fileInfoService.selectByMd5(md5);
        if (!ObjectUtils.isEmpty(fileInfo)) {
            return ResultUtil.success(fileInfo);
        } else {
            return ResultUtil.defineFail(ResultEnum.FILE_NOT_EXIST);
        }
    }


    /**
     * 分块上传
     *
     * @param originalName 文件原名
     * @param file         文件
     * @param chunks       共分几块
     * @param chunk        第几块
     * @param size         文件总大小
     * @param md5          文件的md5
     * @return
     */
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
