package com.raptor.uploader.controller;

import com.raptor.uploader.service.FileInfoService;
import com.raptor.uploader.utils.response.Result;
import com.raptor.uploader.utils.response.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author raptor
 * @description FileInfoController
 * @date 2021/12/5 17:28
 */
@Controller
public class FileInfoController {

    FileInfoService fileInfoService;

    @Autowired
    public FileInfoController(FileInfoService fileInfoService) {
        this.fileInfoService = fileInfoService;
    }


    @GetMapping("/index.html")
    public String index(){
        return "index";
    }

    @GetMapping("/getAll/files")
    @ResponseBody
    public Result getALlFiles(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> map = fileInfoService.selectAll(page, size);
        return ResultUtil.success(map);
    }
}
