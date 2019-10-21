package com.example.ftpmanage.entity;

import java.io.Serializable;
import java.util.List;

public class FtpDirList implements Serializable {

    private List<FTPFileExt> list;

    public List<FTPFileExt> getList() {
        return list;
    }

    public void setList(List<FTPFileExt> list) {
        this.list = list;
    }

    public FtpDirList(List<FTPFileExt> list) {
        this.list = list;
    }
}
