package com.example.ftpmanage.entity;

import android.app.Application;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

import com.tao.admin.loglib.IConfig;
import com.tao.admin.loglib.TLogApplication;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TLogApplication.initialize(this);
        IConfig.getInstance().isShowLog(true)//是否在logcat中打印log,默认不打印
                .isWriteLog(true)//是否在文件中记录，默认不记录
                .fileSize(1000000)//日志文件的大小，默认0.1M,以bytes为单位
                .tag("FtpManage");//logcat 日志过滤tag
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }
    }
}
