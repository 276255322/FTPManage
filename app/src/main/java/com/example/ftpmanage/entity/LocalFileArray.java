package com.example.ftpmanage.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LocalFileArray  implements Serializable {

    private List<FileExt> fextList = new ArrayList<>();

    private String ftpPath = "";

    public List<FileExt> getFextList() {
        return fextList;
    }

    public void setFextList(List<FileExt> fextList) {
        this.fextList = fextList;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }
}
