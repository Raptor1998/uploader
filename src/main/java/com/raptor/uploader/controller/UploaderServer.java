package com.raptor.uploader.controller;

import com.raptor.uploader.config.properties.FileConstraintsProperties;
import com.raptor.uploader.service.FileInfoService;
import com.raptor.uploader.utils.uploader.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author raptor
 * @description UploaderServer
 * @date 2021/12/4 20:06
 */
@EnableConfigurationProperties(FileConstraintsProperties.class)
@Component
@Slf4j
public class UploaderServer {


    private FileInfoService fileInfoService;

    private FileConstraintsProperties fileConstraintsProperties;

    @Autowired
    public UploaderServer(FileInfoService fileInfoService, FileConstraintsProperties fileConstraintsProperties) {
        this.fileInfoService = fileInfoService;
        this.fileConstraintsProperties = fileConstraintsProperties;
    }

    public void upload(MultipartFile file) throws IOException {
        FileUploadUtils.setPathName(fileConstraintsProperties.getPath()).setAllowedExtension(fileConstraintsProperties.getExtension()).upload(file);
    }
}
