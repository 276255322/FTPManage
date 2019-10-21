package com.example.ftpmanage.entity;

public class FtpLock {
    public static boolean unLock = false;

    public static void setUnLock(){
        unLock = true;
        FTPClientEntity.testLock();
    }
}
