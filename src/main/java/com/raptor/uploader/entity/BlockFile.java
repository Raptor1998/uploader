package com.raptor.uploader.entity;

import java.io.Serializable;
import java.util.Date;

public class BlockFile implements Serializable {
    private Integer blockFileId;

    private Integer blockFileChunk;

    private String blockFileMd5;

    private Date uploadTime;

    private String tempPath;

    private static final long serialVersionUID = 1L;

    public Integer getBlockFileId() {
        return blockFileId;
    }

    public void setBlockFileId(Integer blockFileId) {
        this.blockFileId = blockFileId;
    }

    public Integer getBlockFileChunk() {
        return blockFileChunk;
    }

    public void setBlockFileChunk(Integer blockFileChunk) {
        this.blockFileChunk = blockFileChunk;
    }

    public String getBlockFileMd5() {
        return blockFileMd5;
    }

    public void setBlockFileMd5(String blockFileMd5) {
        this.blockFileMd5 = blockFileMd5 == null ? null : blockFileMd5.trim();
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    @Override
    public String toString() {
        return "BlockFile{" +
                "blockFileId=" + blockFileId +
                ", blockFileChunk=" + blockFileChunk +
                ", blockFileMd5='" + blockFileMd5 + '\'' +
                ", uploadTime=" + uploadTime +
                ", tempPath='" + tempPath + '\'' +
                '}';
    }
}