package com.example.ftpmanage.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FtpDirListArray implements Serializable {

    private List<FTPFileExt> fextList = new ArrayList<>();

    private String ftpPath = "";

    public List<FTPFileExt> getFextList() {
        return fextList;
    }

    public void setFextList(List<FTPFileExt> fextList) {
        this.fextList = fextList;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }
}
