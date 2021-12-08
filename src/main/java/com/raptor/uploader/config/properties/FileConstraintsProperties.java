package com.raptor.uploader.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author raptor
 * @description FileConstraints
 * @date 2021/12/4 20:09
 */
@Component
@ConfigurationProperties(prefix = "uploader.file")
@Data
public class FileConstraintsProperties {
    private String path;
    private Integer size;
    private String[] extension;
    private String prefixUrl;
    private Boolean folderRole;

}
