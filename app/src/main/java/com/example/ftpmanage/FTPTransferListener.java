package com.example.ftpmanage;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class FTPTransferListener implements FTPDataTransferListener {

    private Context context;

    private long maxSize = 0;

    private long completedSize = 0;

    private int progressType = 0;

    private Handler handler;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public long getCompletedSize() {
        return completedSize;
    }

    public void setCompletedSize(long completedSize) {
        this.completedSize = completedSize;
    }

    public int getProgressType() {
        return progressType;
    }

    public void setProgressType(int progressType) {
        this.progressType = progressType;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public FTPTransferListener(Context c,Handler handler, int pType, long mSize, long cSize) {
        this.context = c;
        this.handler = handler;
        this.progressType = pType;
        this.maxSize = mSize;
        this.completedSize = cSize;
    }

    //文件开始上传或下载时触发
    public void started() {
        Message msg = new Message();
        Bundle data = new Bundle();
        msg.what = 0;
        data.putInt("type", this.progressType);
        data.putInt("maxSize", (int) this.maxSize / 1024 / 1024);
        msg.setData(data);
        this.handler.sendMessage(msg);
    }

    //显示已经传输的字节数
    public void transferred(int length) {
        this.completedSize += length;
        Message msg = new Message();
        Bundle data = new Bundle();
        msg.what = 1;
        data.putInt("type", this.progressType);
        data.putInt("completedSize", (int) this.completedSize / 1024 / 1024);
        msg.setData(data);
        this.handler.sendMessage(msg);
    }

    //文件传输完成时，触发
    public void completed() {
        Message msg = new Message();
        Bundle data = new Bundle();
        msg.what = 2;
        data.putInt("type", this.progressType);
        data.putInt("maxSize", (int) this.maxSize / 1024 / 1024);
        msg.setData(data);
        this.handler.sendMessage(msg);
    }

    //传输放弃时触发
    public void aborted() {
        Message msg = new Message();
        Bundle data = new Bundle();
        msg.what = 3;
        data.putInt("type", this.progressType);
        msg.setData(data);
        this.handler.sendMessage(msg);
    }

    //传输失败时触发
    public void failed() {
        Message msg = new Message();
        Bundle data = new Bundle();
        msg.what = 4;
        data.putInt("type", this.progressType);
        msg.setData(data);
        this.handler.sendMessage(msg);
    }

}
