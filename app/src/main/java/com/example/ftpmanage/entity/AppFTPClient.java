package com.example.ftpmanage.entity;

import android.content.Context;

import com.example.ftpmanage.utils.AppUtil;

import it.sauronsoftware.ftp4j.FTPClient;

public class AppFTPClient extends FTPClient {

    private FtpConfig ftpConfig = null;

    private Context context;

    private int connStatus = 0;

    private boolean isErr = false;

    private String errMessage = "";

    private String errCode = "";

    private int errType = 0;

    public FtpConfig getFtpConfig() {
        return ftpConfig;
    }

    public void setFtpConfig(FtpConfig ftpConfig) {
        this.ftpConfig = ftpConfig;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getConnStatus() {
        return connStatus;
    }

    public void setConnStatus(int connStatus) {
        this.connStatus = connStatus;
    }

    public boolean isErr() {
        return isErr;
    }

    public void setErr(boolean err) {
        isErr = err;
    }

    public String getErrMessage() {
        StringBuilder sb = new StringBuilder();
        if (this.errType == 1) {
            sb.append("FTP连接失败，失败信息：");
        } else if (this.errType == 2){
            sb.append("关闭FTP连接失败，失败信息：");
        } else if (this.errType == 3){
            sb.append("创建FTP目录失败，失败信息：");
        } else if (this.errType == 4){
            sb.append("上传文件到FTP失败，失败信息：");
        } else if (this.errType == 5){
            sb.append("下载FTP文件失败，失败信息：");
        } else if (this.errType == 6){
            sb.append("删除FTP文件失败，失败信息：");
        } else if (this.errType == 7){
            sb.append("读取FTP文件列表失败，失败信息：");
        } else {
            sb.append("FTP操作失败，失败信息：");
        }
        if (AppUtil.isIndexOf(this.errMessage, "Forbidden filename")) {
            sb.append("文件被禁止写入！");
        }else if (AppUtil.isIndexOf(this.errMessage, "Broken pipe")) {
            sb.append("FTP已断开！");
        }else if (AppUtil.isIndexOf(this.errMessage, "Network is unreachable")) {
            sb.append("无法连接到网络！");
        }else if (AppUtil.isIndexOf(this.errMessage, "Client not connected")) {
            sb.append("FTP连接失败！");
        }else if (AppUtil.isIndexOf(this.errMessage, "Software caused connection abort")) {
            sb.append("FTP连接失败！");
        }else if (AppUtil.isIndexOf(this.errMessage, "connect timed out")) {
            sb.append("FTP连接超时！");
        }else {
            sb.append(this.errMessage);
        }
        return sb.toString();
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getErrOriginal() {
        return errMessage;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public int getErrType() {
        return errType;
    }

    public void setErrType(int errType) {
        this.errType = errType;
    }

    public AppFTPClient(FtpConfig ftpConfig) {
        this.ftpConfig = ftpConfig;
    }

    public void setError() {
        this.isErr = false;
        this.errMessage = "";
    }

    public void setError(String code, int type, String err) {
        this.isErr = true;
        this.errType = type;
        this.errCode = code;
        this.errMessage = err;
    }

}
