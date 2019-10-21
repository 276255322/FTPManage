package com.example.ftpmanage.entity;

import it.sauronsoftware.ftp4j.FTPFile;

public class FTPFileExt extends FTPFile {

    private boolean isDown = false;

    private int childCount = -1;

    private String localPath ="";

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public boolean isDown() {
        return isDown;
    }

    public void setDown(boolean down) {
        isDown = down;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }
}
