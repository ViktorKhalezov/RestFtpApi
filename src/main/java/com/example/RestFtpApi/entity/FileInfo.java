package com.example.RestFtpApi.entity;



public class FileInfo {

    private String fullPath;
    private String creationDate;
    private long size;

    public FileInfo(String fullPath, String creationDate, long size) {
        this.fullPath = fullPath;
        this.creationDate = creationDate;
        this.size = size;
    }

    public String getFullPath() {
        return fullPath;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public long getSize() {
        return size;
    }
}
