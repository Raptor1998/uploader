package com.raptor.uploader.utils.uploader;

/**
 * @author raptor
 * @description FilenameUtils
 * @date 2021/12/4 20:00
 */
public class FilenameUtils {

    public static String getExtension(String originalFilename) {
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        return suffix;
    }
}
