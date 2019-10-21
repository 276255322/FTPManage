package com.example.ftpmanage;

import android.Manifest;
import android.app.Activity;
import androidx.annotation.NonNull;

import com.example.ftpmanage.permission.PermissionUtils;
import com.example.ftpmanage.permission.request.IRequestPermissions;
import com.example.ftpmanage.permission.request.RequestPermissions;
import com.example.ftpmanage.permission.requestresult.IRequestPermissionsResult;
import com.example.ftpmanage.permission.requestresult.RequestPermissionsResultSetApp;
import com.example.ftpmanage.utils.TimingUtil;
import com.example.ftpmanage.utils.UiUtil;

/**
 * Activity请求操作权限
 */
public class ActivityPermissions {

    public Activity activity;

    private IRequestPermissions reqps = RequestPermissions.getInstance();
    private IRequestPermissionsResult reqpsr = RequestPermissionsResultSetApp.getInstance();

    /**
     * 构造方法
     * @param activity
     */
    public ActivityPermissions(Activity activity){
        this.activity = activity;
    }

    /**
     * 请求权限
     *
     * @return
     */
    public boolean requestPermissions() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
        TimingUtil.isAppPower = reqps.requestPermissions(activity, permissions, PermissionUtils.ResultCode1);
        return TimingUtil.isAppPower;
    }

    /**
     * 授权结果
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
        if (reqpsr.doRequestPermissionsResult(activity, permissions, grantResults)) {
            UiUtil.showPosition(activity,  "授权成功，请重新点击刚才的操作！");
        } else {
            UiUtil.showPosition(activity,  "请给APP授权，否则功能无法正常使用！");
        }
    }

}
