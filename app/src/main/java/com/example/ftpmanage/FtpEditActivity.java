package com.example.ftpmanage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.example.ftpmanage.entity.FtpConfig;
import com.example.ftpmanage.ui.FtpFilesFragment;
import com.example.ftpmanage.ui.FtpFragment;
import com.example.ftpmanage.ui.Ftp_Fragments;
import com.example.ftpmanage.ui.LocalFragment;
import com.example.ftpmanage.utils.AppRegExp;
import com.example.ftpmanage.utils.AppUtil;
import com.example.ftpmanage.utils.DateUtil;
import com.example.ftpmanage.utils.UiUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

public class FtpEditActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText t_Ftp_Name;
    private TextInputEditText t_Ftp_Host;
    private TextInputEditText t_Ftp_Port;
    private TextInputEditText t_Ftp_Host1;
    private TextInputEditText t_Ftp_Port1;
    private TextInputEditText t_Ftp_User;
    private TextInputEditText t_Ftp_Pwd;
    private Switch s_Ftp_AnonymousLogin;
    private Switch s_Ftp_Mode;
    private Switch s_Ftp_DomainToIp;
    private Button bt_FtpSave;
    private SQLiteDatabase db;
    private Intent intent = null;
    private FtpConfig fe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp_edit);
        intent = getIntent();
        initView();
        loadFtpInfo();
    }

    //初始化控件
    private void initView() {
        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        t_Ftp_Name = findViewById(R.id.t_Ftp_Name);
        t_Ftp_Host = findViewById(R.id.t_Ftp_Host);
        t_Ftp_Port = findViewById(R.id.t_Ftp_Port);
        t_Ftp_Host1 = findViewById(R.id.t_Ftp_Host1);
        t_Ftp_Port1 = findViewById(R.id.t_Ftp_Port1);
        t_Ftp_User = findViewById(R.id.t_Ftp_User);
        t_Ftp_Pwd = findViewById(R.id.t_Ftp_Pwd);
        s_Ftp_AnonymousLogin = findViewById(R.id.s_Ftp_AnonymousLogin);
        s_Ftp_AnonymousLogin.setOnClickListener(this);
        s_Ftp_Mode = findViewById(R.id.s_Ftp_Mode);
        s_Ftp_Mode.setOnClickListener(this);
        s_Ftp_DomainToIp = findViewById(R.id.s_Ftp_DomainToIp);
        s_Ftp_DomainToIp.setOnClickListener(this);
        bt_FtpSave = findViewById(R.id.bt_FtpSave);
        bt_FtpSave.setOnClickListener(this);
    }

    private void loadFtpInfo() {
        fe = null;
        String fid = intent.getStringExtra("fid");
        if (fid != null && AppUtil.isInt(fid)) {
            fe = FtpUtils.getFTPConfigById(db, fid);
        }
        if (fe != null) {
            t_Ftp_Name.setText(fe.getFtpName());
            t_Ftp_Host.setText(fe.getFtpHost());
            t_Ftp_Port.setText(fe.getFtpPort().toString());
            t_Ftp_Host1.setText(fe.getFtpHost1());
            if (fe.getFtpPort1().intValue() > -1) {
                t_Ftp_Port1.setText(fe.getFtpPort1().toString());
            }
            t_Ftp_User.setText(fe.getFtpUserName());
            t_Ftp_Pwd.setText(fe.getFtpUserPwd());
            s_Ftp_AnonymousLogin.setChecked(AppUtil.getBool(fe.getAnonymousLogin().intValue()));
            s_Ftp_Mode.setChecked(AppUtil.getBool(fe.getFtpMode().intValue()));
            s_Ftp_DomainToIp.setChecked(AppUtil.getBool(fe.getDomainToIp().intValue()));
        }
        changeAnonymousLogin();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_FtpSave:
                saveFtpInfo();
                break;
            case R.id.s_Ftp_AnonymousLogin:
                changeAnonymousLogin();
                break;
            default:
                break;
        }
    }

    private void changeAnonymousLogin() {
        if (s_Ftp_AnonymousLogin.isChecked()) {
            t_Ftp_User.setEnabled(false);
            t_Ftp_Pwd.setEnabled(false);
            t_Ftp_User.setText("");
            t_Ftp_Pwd.setText("");
        } else {
            t_Ftp_User.setEnabled(true);
            t_Ftp_Pwd.setEnabled(true);
        }
    }

    public void saveFtpInfo() {
        String ftpName = t_Ftp_Name.getText().toString().trim();
        if (AppUtil.isEmpty(ftpName)) {
            t_Ftp_Name.setError("请填写名称！");
            return;
        }
        if (AppRegExp.isNotReg(AppRegExp.RegExpStr_Num26LeAndCna, ftpName)) {
            t_Ftp_Name.setError("名称必须由数字、下划线、26个英文大小写字母或者中文组成！");
            return;
        }
        String ftpHost = t_Ftp_Host.getText().toString().trim();
        if (AppUtil.isEmpty(ftpHost)) {
            t_Ftp_Host.setError("请填写主机！");
            return;
        }
        String ftpPort = t_Ftp_Port.getText().toString().trim();
        if (!AppUtil.isInt(ftpPort)) {
            t_Ftp_Port.setError("端口必须是数字！");
            return;
        }
        int ftpPortint = AppUtil.getInt(ftpPort);
        if (ftpPortint < 0 || ftpPortint > 65535) {
            t_Ftp_Port.setError("端口不能小于0或大于65535！");
            return;
        }
        String ftpHost1 = t_Ftp_Host1.getText().toString().trim();
        String ftpPort1 = t_Ftp_Port1.getText().toString().trim();
        int ftpPortint1 = -1;
        if (AppUtil.isNotEmpty(ftpHost1)) {
            if (AppUtil.isInt(ftpPort1)) {
                ftpPortint1 = AppUtil.getInt(ftpPort1);
                if (ftpPortint1 < 0 || ftpPortint1 > 65535) {
                    t_Ftp_Port1.setError("次主机端口不能小于0或大于65535！");
                    return;
                }
            } else {
                ftpPortint1 = ftpPortint;
            }
        }
        String ftpUserName = t_Ftp_User.getText().toString().trim();
        String ftpUserPwd = t_Ftp_Pwd.getText().toString().trim();
        int ftpAnonymousLogin = AppUtil.getInt(s_Ftp_AnonymousLogin.isChecked());
        if (ftpAnonymousLogin == 1) {
            ftpUserName = "";
            ftpUserPwd = "";
        } else {
            if (AppUtil.isEmpty(ftpUserName)) {
                t_Ftp_User.setError("请填写用户名！");
                return;
            }
            if (AppUtil.isEmpty(ftpUserPwd)) {
                t_Ftp_Pwd.setError("请填写密码！");
                return;
            }
        }
        int ftpMode = AppUtil.getInt(s_Ftp_Mode.isChecked());
        int ftpDomainToIp = AppUtil.getInt(s_Ftp_DomainToIp.isChecked());
        boolean isadd = true;
        if (fe == null) {
            fe = new FtpConfig();
            fe.setFtpEncoded("AUTO");
            fe.setCreatorDate(new Date());
            fe.setUpdateDate(new Date());
        } else {
            isadd = false;
            fe.setUpdateDate(new Date());
        }
        fe.setFtpName(ftpName);
        fe.setFtpHost(ftpHost);
        fe.setFtpPort(ftpPortint);
        fe.setFtpHost1(ftpHost1);
        fe.setFtpPort1(ftpPortint1);
        fe.setFtpUserName(ftpUserName);
        fe.setFtpUserPwd(ftpUserPwd);
        fe.setAnonymousLogin(ftpAnonymousLogin);
        fe.setDomainToIp(ftpDomainToIp);
        fe.setFtpMode(ftpMode);
        FtpUtils.saveFTPConfig(db, fe, isadd);
        UiUtil.showPosition(this,  "保存成功！");
        LocalFragment.isFirst = true;
        FtpFragment.isFirst = true;
        FtpFilesFragment.isFirst = true;
        this.finish();
    }
}
