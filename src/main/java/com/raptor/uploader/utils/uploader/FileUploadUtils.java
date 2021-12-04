package com.raptor.uploader.utils.uploader;

import com.raptor.uploader.enume.ResultEnum;
import com.raptor.uploader.exception.DescribeException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class FileUploadUtils {
    /**
     * 设置保存文件路径名
     *
     * @param pathName
     * @return
     */

    public static Builder setPathName(String pathName) {
        return builder().setPathName(pathName);

    }

    /**
     * 设置允许上传文件大小
     *
     * @param maxSize
     * @return
     */

    public static Builder setMaxSize(int maxSize) {
        return builder().setMaxSize(maxSize);

    }

    /**
     * 设置最大文件名长度
     *
     * @param maxFileNameLength
     * @return
     */

    public static Builder setMaxFileNameLength(int maxFileNameLength) {
        return builder().setMaxFileNameLength(maxFileNameLength);

    }

    /**
     * 设置允许上传文件路径
     *
     * @param allowedExtension
     * @return
     */

    public static Builder setAllowedExtension(String[] allowedExtension) {
        return builder().setAllowedExtension(allowedExtension);

    }

    public static Builder builder() {
        return new Builder();

    }

    public static class Builder {
        /**
         * 默认大小 50M
         */

        private long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

        /**
         * 默认的文件名最大长度 100
         */

        private int DEFAULT_FILE_NAME_LENGTH = 100;

        /**
         * 允许上传文件的后缀名
         */

        private String[] DEFAULT_ALLOWED_EXTENSION = {};

        /**
         * 保存文件路径名
         */

        private String PATH_NAME;

        public Builder setMaxSize(int maxSize) {
            this.DEFAULT_MAX_SIZE = maxSize * 1024 * 1024;

            return this;

        }

        public Builder setMaxFileNameLength(int maxFileNameLength) {
            this.DEFAULT_FILE_NAME_LENGTH = maxFileNameLength;

            return this;

        }

        public Builder setAllowedExtension(String[] allowedExtension) {
            this.DEFAULT_ALLOWED_EXTENSION = allowedExtension;

            return this;

        }

        public Builder setPathName(String pathName) {
            this.PATH_NAME = pathName;

            return this;

        }

        /**
         * 上传
         *
         * @param file
         * @return
         * @throws IOException
         */

        public final String upload(MultipartFile file) throws IOException {
            //校验

            if (StringUtils.trimToNull(PATH_NAME) == null) {
                throw new DescribeException(ResultEnum.FILE_PATH_IS_NULL);
            }

            String extensionFilename = FilenameUtils.getExtension(file.getOriginalFilename());

            if (!ArrayUtils.isEmpty(DEFAULT_ALLOWED_EXTENSION) && !ArrayUtils.contains(DEFAULT_ALLOWED_EXTENSION, extensionFilename)) {
                throw new DescribeException(ResultEnum.FILE_EXTENSION_NOT_ACCEPT);
            }

            if (file.getSize() > DEFAULT_MAX_SIZE) {
                throw new DescribeException(ResultEnum.FILE_SIZE_LARGER);
            }

            if (file.getOriginalFilename().length() > DEFAULT_FILE_NAME_LENGTH) {
                throw new DescribeException(ResultEnum.FILE_NAME_LONGER);
            }

            //上传

            String fileName = extractFilename(file);

            File desc = getAbsoluteFile(PATH_NAME, fileName);

            file.transferTo(desc);

            return fileName;

        }

        /**
         * 编码文件名
         */

        private final String extractFilename(MultipartFile file) {
            String filename = file.getOriginalFilename();

            String extension = getExtension(file);

            filename = DateFormatUtils.format(new Date(), "/yyyy/MM/dd") + "/" + encodingFilename(filename) + "." + extension;

            return filename;

        }

        /**
         * 编码文件名
         */

        public final String encodingFilename(String filename) {
            filename = filename.replace("_", " ");

            filename = DigestUtils.md5Hex(filename + System.nanoTime() + RandomStringUtils.randomNumeric(6));

            return filename;

        }

        /**
         * 获取文件名的后缀
         *
         * @param file 表单文件
         * @return 后缀名
         */

        public final String getExtension(MultipartFile file) {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());

            return extension;

        }

        /**
         * 创建文件
         *
         * @param uploadDir
         * @param filename
         * @return
         * @throws IOException
         */

        private final File getAbsoluteFile(String uploadDir, String filename) throws IOException {
            File desc = new File(uploadDir + File.separator + filename);

            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();

            }

            if (!desc.exists()) {
                desc.createNewFile();

            }
            return desc;

        }

    }

}