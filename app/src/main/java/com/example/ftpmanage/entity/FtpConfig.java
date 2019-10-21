package com.example.ftpmanage.entity;

import java.util.Date;

public class FtpConfig {

    private Integer fid;
    private String ftpName;
    private String ftpHost;
    private Integer ftpPort;
    private String ftpHost1;
    private Integer ftpPort1;
    private String ftpUserName;
    private String ftpUserPwd;
    private Integer anonymousLogin;
    private Integer domainToIp;
    private Integer ftpMode;
    private String ftpEncoded;
    private Date creatorDate;
    private Date updateDate;

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getFtpName() {
        return ftpName;
    }

    public void setFtpName(String ftpName) {
        this.ftpName = ftpName;
    }

    public String getFtpHost() {
        return ftpHost + "";
    }

    public void setFtpHost(String ftpHost) {
        this.ftpHost = ftpHost;
    }

    public Integer getFtpPort() {
        return ftpPort;
    }

    public String getFtpHost1() {
        return ftpHost1 + "";
    }

    public void setFtpHost1(String ftpHost1) {
        this.ftpHost1 = ftpHost1;
    }

    public Integer getFtpPort1() {
        return ftpPort1;
    }

    public void setFtpPort1(Integer ftpPort1) {
        this.ftpPort1 = ftpPort1;
    }

    public void setFtpPort(Integer ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getFtpUserName() {
        return ftpUserName;
    }

    public void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }

    public String getFtpUserPwd() {
        return ftpUserPwd;
    }

    public void setFtpUserPwd(String ftpUserPwd) {
        this.ftpUserPwd = ftpUserPwd;
    }

    public Integer getAnonymousLogin() {
        return anonymousLogin;
    }

    public void setAnonymousLogin(Integer anonymousLogin) {
        this.anonymousLogin = anonymousLogin;
    }

    public Integer getDomainToIp() {
        return domainToIp;
    }

    public void setDomainToIp(Integer domainToIp) {
        this.domainToIp = domainToIp;
    }

    public Integer getFtpMode() {
        return ftpMode;
    }

    public void setFtpMode(Integer ftpMode) {
        this.ftpMode = ftpMode;
    }

    public String getFtpEncoded() {
        return ftpEncoded;
    }

    public void setFtpEncoded(String ftpEncoded) {
        this.ftpEncoded = ftpEncoded;
    }

    public Date getCreatorDate() {
        return creatorDate;
    }

    public void setCreatorDate(Date creatorDate) {
        this.creatorDate = creatorDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
