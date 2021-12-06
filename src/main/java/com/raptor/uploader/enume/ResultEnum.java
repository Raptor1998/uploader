package com.raptor.uploader.enume;

/**
 * @author raptor
 * @description FileUploadEnum
 * @date 2021/12/4 16:52
 */
public enum ResultEnum {
    SUCCESS("00000", "请求成功"),
    FAIL("00001", "默认错误"),

    // FILE UPLOAD ERROR
    FILE_INFO_INSERT_ERROR("F0001", "文件信息插入失败"),
    FILE_NOT_EXIST("F0002", "文件不存在"),
    FILE_PATH_IS_NULL("F0003", "文件保存路径为空"),
    FILE_EXTENSION_NOT_ACCEPT("F0004", "此类型文件不可上传"),
    FILE_SIZE_LARGER("F0005", "文件超过限制大小"),
    FILE_NAME_LONGER("F0006", "文件名过长"),
    FILE_IO_EXCEPTION("F0007", "文件上传失败，请重试"),
    FILE_CHUNK_EXIST("F0008", "文件分片已经存在");
    private String code;
    private String msg;

    ResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
