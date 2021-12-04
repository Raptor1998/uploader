package com.raptor.uploader.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author raptor
 * @description ThreadPoolConfigProperties
 * @date 2021/12/4 16:45
 */
@ConfigurationProperties(prefix = "uploader.thread")
@Data
public class ThreadPoolConfigProperties {

    private Integer coreSize;

    private Integer maxSize;

    private Integer keepAliveTime;

}
