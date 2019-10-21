package com.example.ftpmanage.entity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ftpmanage.FilesAdapter;
import com.example.ftpmanage.utils.AppRegExp;
import com.example.ftpmanage.utils.AppSecure;
import com.example.ftpmanage.utils.AppUtil;
import com.example.ftpmanage.utils.ConstantUtil;
import com.example.ftpmanage.utils.FileProvider;
import com.example.ftpmanage.utils.JsonUtil;
import com.example.ftpmanage.utils.NetUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

public class FTPClientEntity {

    public static AppFTPClient client = null;

    public static String getFtpConfigSign(FtpConfig fc) {
        StringBuilder sb = new StringBuilder();
        sb.append(fc.getFid());
        sb.append(fc.getFtpName());
        sb.append(fc.getFtpHost());
        sb.append(fc.getFtpPort());
        sb.append(fc.getFtpHost1());
        sb.append(fc.getFtpPort1());
        sb.append(fc.getAnonymousLogin());
        sb.append(fc.getFtpEncoded());
        sb.append(fc.getFtpMode());
        sb.append(fc.getDomainToIp());
        sb.append(fc.getFtpUserName());
        sb.append(fc.getFtpUserPwd());
        return AppSecure.SHA1(sb.toString());
    }

    private static List<HostPort> getHostPortList(FtpConfig fc) {
        List<HostPort> hlist = new ArrayList<>();
        String netIp = NetUtil.getLocalIpV4Address();
        String ips1 = getHost(fc.getFtpHost(), fc.getDomainToIp().intValue());
        String ips2 = getHost(fc.getFtpHost1(), fc.getDomainToIp().intValue());
        if (NetUtil.isIPRange(netIp, ips1)) {
            hlist.add(new HostPort(ips1, fc.getFtpPort()));
            hlist.add(new HostPort(ips2, fc.getFtpPort1()));
        } else if (NetUtil.isIPRange(netIp, ips2)) {
            hlist.add(new HostPort(ips2, fc.getFtpPort1()));
            hlist.add(new HostPort(ips1, fc.getFtpPort()));
        } else {
            hlist.add(new HostPort(ips2, fc.getFtpPort1()));
            hlist.add(new HostPort(ips1, fc.getFtpPort()));
        }
        return hlist;
    }

    private static String getHost(String s, int domainToIp) {
        if (AppRegExp.isNotReg(AppRegExp.RegExpStr_Ip, s) && domainToIp == 1) {
            String ips = NetUtil.DomainToIp(s);
            if (AppUtil.isNotEmpty(ips)) {
                return ips;
            }
        }
        return s;
    }

    public static synchronized void testLock() {
        for (int i = 0; i < 5; i++) {
        }
    }

    /**
     * 创建FTP连接
     *
     * @param fc      FTP配置对象
     * @param context 窗体控件
     */
    public static synchronized void createConnection(FtpConfig fc, Context context) {
        if (client == null) {
            client = new AppFTPClient(fc);
        } else {
            client.setError();
            if (client.getConnStatus() == 1) {
                String ySign = getFtpConfigSign(client.getFtpConfig());
                String sign = getFtpConfigSign(fc);
                if (ySign.equals(sign)) {
                    try {
                        client.list();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        client.setError();
        closeConnection();
        if (fc.getFtpMode().intValue() == 0) {
            client.setPassive(true);
        } else {
            client.setPassive(false);
        }
        client.setContext(context);
        List<HostPort> hpList = getHostPortList(fc);
        try {
            client.setFtpConfig(fc);
            client.connect(hpList.get(0).getHost(), hpList.get(0).getPort());
            if (fc.getAnonymousLogin().intValue() != 0) {
                client.login("anonymous", "ftp4j");
            } else {
                client.login(fc.getFtpUserName(), fc.getFtpUserPwd());
            }
            if (client.isCompressionSupported()) {
                client.setCompressionEnabled(true);
            }
            client.setAutoNoopTimeout(30000);
            client.setConnStatus(1);
        } catch (Exception e) {
            closeConnection();
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 1, e.getMessage());
            createMinorConnection(hpList);
        }
    }

    /**
     * 创建次要FTP连接
     */
    public static synchronized boolean createMinorConnection(List<HostPort> hpList) {
        client.setError();
        try {
            FtpConfig fc = client.getFtpConfig();
            client.connect(hpList.get(1).getHost(), hpList.get(1).getPort());
            if (fc.getAnonymousLogin().intValue() != 0) {
                client.login("anonymous", "ftp4j");
            } else {
                client.login(fc.getFtpUserName(), fc.getFtpUserPwd());
            }
            if (client.isCompressionSupported()) {
                client.setCompressionEnabled(true);
            }
            client.setAutoNoopTimeout(30000);
            client.setConnStatus(1);
            return true;
        } catch (Exception e) {
            closeConnection();
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 1, e.getMessage());
        }
        return false;
    }

    /**
     * 重新连接FTP
     */
    public static synchronized void reconnect() {
        if (client == null) {
            return;
        }
        client.setError();
        closeConnection();
        if (client.getFtpConfig().getFtpMode().intValue() == 0) {
            client.setPassive(true);
        } else {
            client.setPassive(false);
        }
        String ipstr = client.getFtpConfig().getFtpHost();
        if (AppRegExp.isNotReg(AppRegExp.RegExpStr_Ip, ipstr) && client.getFtpConfig().getDomainToIp().intValue() == 1) {
            String ips = NetUtil.DomainToIp(ipstr);
            if (AppUtil.isNotEmpty(ips)) {
                ipstr = ips;
            }
        }
        try {
            client.connect(ipstr, client.getFtpConfig().getFtpPort());
            if (client.getFtpConfig().getAnonymousLogin().intValue() != 0) {
                client.login("anonymous", "ftp4j");
            } else {
                client.login(client.getFtpConfig().getFtpUserName(), client.getFtpConfig().getFtpUserPwd());
            }
            if (client.isCompressionSupported()) {
                client.setCompressionEnabled(true);
            }
            client.setAutoNoopTimeout(30000);
            client.setConnStatus(1);
        } catch (IllegalStateException e) {
            closeConnection();
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 1, e.getMessage());
        } catch (IOException e) {
            closeConnection();
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 1, e.getMessage());
        } catch (FTPIllegalReplyException e) {
            closeConnection();
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 1, e.getMessage());
        } catch (FTPException e) {
            closeConnection();
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 1, e.getMessage());
        }
    }

    /**
     * 关闭FTP连接
     *
     * @return
     */
    public static synchronized boolean closeConnection() {
        if (client == null) {
            return true;
        }
        client.setError();
        if (client.isConnected()) {
            try {
                client.disconnect(true);
                client.setConnStatus(0);
                return true;
            } catch (Exception e) {
                try {
                    client.disconnect(false);
                    client.setConnStatus(0);
                    return true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    client.setError(String.valueOf(e.hashCode()), 2, e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 在指定目录路径下创建目录
     *
     * @param path 目录路径
     * @param name 目录名
     */
    public static synchronized void createDirectory(String path, String name) {
        if (client == null) {
            return;
        }
        client.setError();
        try {
            client.changeDirectory(path);
            client.createDirectory(name);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 3, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 3, e.getMessage());
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 3, e.getMessage());
        } catch (FTPException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 3, e.getMessage());
        }
    }

    /**
     * 上传文件
     *
     * @param localPath 本地文件路径
     * @param ftpPath   ftp路径
     * @param ftl       文件传输监听器
     * @return
     */
    public static synchronized boolean upLoadFile(String localPath, String ftpPath, FTPDataTransferListener ftl) {
        if (client == null) {
            return false;
        }
        client.setError();
        try {
            if (!client.currentDirectory().equals(ftpPath)) {
                client.changeDirectory(ftpPath);
            }
            File file = new File(localPath);
            client.upload(file, ftl);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 4, e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 4, e.getMessage());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 4, e.getMessage());
            return false;
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 4, e.getMessage());
            return false;
        } catch (FTPException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 4, e.getMessage());
            return false;
        } catch (FTPDataTransferException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 4, e.getMessage());
            return false;
        } catch (FTPAbortedException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 4, e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 下载文件
     *
     * @param fpath     服务器文件地址
     * @param localfile 本地文件
     * @param ftl       文件传输监听器
     * @return
     */
    public static synchronized boolean downFile(String fpath, File localfile, FTPDataTransferListener ftl) {
        if (client == null) {
            return false;
        }
        client.setError();
        try {
            if (localfile.exists() && localfile.isFile()) {
                FileProvider.deleteFile(localfile);
            }
            client.setType(client.TYPE_BINARY);
            client.download(fpath, localfile, ftl);
            return true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 5, e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 5, e.getMessage());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 5, e.getMessage());
            return false;
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 5, e.getMessage());
            return false;
        } catch (FTPException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 5, e.getMessage());
            return false;
        } catch (FTPDataTransferException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 5, e.getMessage());
            return false;
        } catch (FTPAbortedException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 5, e.getMessage());
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param ftpDir   服务器目录地址
     * @param fileName 文件名称
     * @return
     */
    public static synchronized boolean deleteFile(String ftpDir, String fileName) {
        if (client == null) {
            return false;
        }
        client.setError();
        try {
            client.deleteFile(ftpDir + fileName);
            return true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 6, e.getMessage());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 6, e.getMessage());
            return false;
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 6, e.getMessage());
            return false;
        } catch (FTPException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 6, e.getMessage());
            return false;
        }
    }

    /**
     * 返回指定FTP路径下项目列表
     *
     * @param path 指定FTP路径
     * @return
     */
    public static synchronized List<FTPFileExt> getFTPList(String path) {
        if (client == null) {
            return null;
        }
        client.setError();
        try {
            client.changeDirectory(path);
            FTPFile[] flist = client.list();
            List<FTPFileExt> fexts = new ArrayList<>();
            for (int i = 0; i < flist.length; i++) {
                FTPFile ffile = flist[i];
                FTPFileExt fext = JsonUtil.omap.readValue(JsonUtil.omap.writeValueAsString(ffile), FTPFileExt.class);
                fexts.add(fext);
            }
            return fexts;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 7, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 7, e.getMessage());
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 7, e.getMessage());
        } catch (FTPException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 7, e.getMessage());
        } catch (FTPDataTransferException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 7, e.getMessage());
        } catch (FTPAbortedException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 7, e.getMessage());
        } catch (FTPListParseException e) {
            e.printStackTrace();
            client.setError(String.valueOf(e.hashCode()), 7, e.getMessage());
        }
        return null;
    }

    public static synchronized void downAllImages(AppCompatActivity appCompatActivity, FilesAdapter filesAdapter, Handler handler, String localSaveDir) {
        boolean fail = false;
        for (int i = 0; i < filesAdapter.mList.size(); i++) {
            if (fail || FtpLock.unLock) {
                break;
            }
            FTPFileExt ffile = filesAdapter.mList.get(i);
            String fName = ffile.getName();
            if (ffile.getType() == 0 && AppUtil.isExt(fName, ConstantUtil.IMAGE_SUFFIX_GATHER)) {
                if (!ffile.isDown()) {
                    String savePath = localSaveDir + fName + ConstantUtil.IMAGE_PRIVATE_SUFFIX;
                    File sfile = new File(savePath);
                    if (!sfile.exists()) {
                        Message msg = new Message();
                        Bundle data = new Bundle();
                        boolean isDown = FTPClientEntity.downFile(fName, sfile, null);
                        if (FTPClientEntity.client.isErr()) {
                            FileProvider.deleteFile(sfile);
                            String errs = FTPClientEntity.client.getErrMessage();
                            String oErrs = FTPClientEntity.client.getErrOriginal();
                            if (AppUtil.isIndexOf(oErrs, "Broken pipe") || AppUtil.isIndexOf(oErrs, "Client not connected") || AppUtil.isIndexOf(oErrs, "Software caused connection abort")) {
                                FTPClientEntity.createConnection(FTPClientEntity.client.getFtpConfig(), appCompatActivity);
                                if (FTPClientEntity.client.isErr()) {
                                    fail = true;
                                    msg.what = 9999;
                                    data.putString("errMessage", errs);
                                    msg.setData(data);
                                    handler.sendMessage(msg);
                                }
                            } else {
                                fail = true;
                                msg.what = 9999;
                                data.putString("errMessage", errs);
                                msg.setData(data);
                                handler.sendMessage(msg);
                            }
                        } else if (isDown) {
                            msg.what = 3;
                            data.putInt("position", i);
                            msg.setData(data);
                            handler.sendMessage(msg);
                        }
                    }
                }
            }
        }
    }

    public static synchronized void loadAllFtpDirectory(FilesAdapter filesAdapter, Handler handler, String ftpPath) {
        for (int i = 0; i < filesAdapter.mList.size(); i++) {
            if (FtpLock.unLock) {
                break;
            }
            FTPFileExt ffile = filesAdapter.mList.get(i);
            if (ffile.getType() == 1) {
                String fpath = ftpPath + ffile.getName() + "/";
                List<FTPFileExt> fexts = FTPClientEntity.getFTPList(fpath);
                if (!FTPClientEntity.client.isErr()) {
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    ffile.setChildCount(fexts.size());
                    msg.what = 3;
                    data.putInt("position", i);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
            }
        }
    }

}
