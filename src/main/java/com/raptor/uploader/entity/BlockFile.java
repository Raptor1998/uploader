package com.raptor.uploader.entity;

import java.io.Serializable;
import java.util.Date;

public class BlockFile implements Serializable {
    private Integer blockFileId;

    private Integer blockFileChunk;

    private String blockFileMd5;

    private Date uploadTime;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", blockFileId=").append(blockFileId);
        sb.append(", blockFileChunk=").append(blockFileChunk);
        sb.append(", blockFileMd5=").append(blockFileMd5);
        sb.append(", uploadTime=").append(uploadTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}