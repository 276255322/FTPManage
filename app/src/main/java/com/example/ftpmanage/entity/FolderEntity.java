package com.example.ftpmanage.entity;

public class FolderEntity {

    private String filePath = "";
    private long filesSize = 0;
    private long foldersSize = 0;
    private long countSzie = 0;
    private int fileType = 0;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFilesSize() {
        return filesSize;
    }

    public void setFilesSize(long filesSize) {
        this.filesSize = filesSize;
    }

    public long getFoldersSize() {
        return foldersSize;
    }

    public void setFoldersSize(long foldersSize) {
        this.foldersSize = foldersSize;
    }

    public long getCountSzie() {
        return countSzie;
    }

    public void setCountSzie(long countSzie) {
        this.countSzie = countSzie;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }
}
