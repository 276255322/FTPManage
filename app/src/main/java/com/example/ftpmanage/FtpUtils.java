package com.example.ftpmanage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.ftpmanage.entity.FtpConfig;
import com.example.ftpmanage.utils.AppUtil;
import com.example.ftpmanage.utils.ConstantUtil;
import com.example.ftpmanage.utils.DateUtil;
import com.example.ftpmanage.utils.ImgUtil;

import java.util.ArrayList;
import java.util.List;

public class FtpUtils {

    public static boolean saveFTPConfig(SQLiteDatabase db, FtpConfig fe, boolean isadd) {
        ContentValues values = new ContentValues();
        values.put("ftpName", fe.getFtpName());
        values.put("ftpHost", fe.getFtpHost());
        values.put("ftpPort", fe.getFtpPort().toString());
        values.put("ftpHost1", fe.getFtpHost1());
        values.put("ftpPort1", fe.getFtpPort1().toString());
        values.put("ftpUserName", fe.getFtpUserName());
        values.put("ftpUserPwd", fe.getFtpUserPwd());
        values.put("anonymousLogin", fe.getAnonymousLogin().toString());
        values.put("domainToIp", fe.getDomainToIp().toString());
        values.put("ftpMode", fe.getFtpMode().toString());
        values.put("ftpEncoded", fe.getFtpEncoded());
        values.put("creatorDate", DateUtil.getDates(fe.getCreatorDate()));
        values.put("updateDate", DateUtil.getDates(fe.getUpdateDate()));
        if (isadd) {
            db.insert("FtpList", null, values);
        } else {
            db.update("FtpList", values, "fid=?", new String[]{fe.getFid().toString()});
        }
        return true;
    }

    public static FtpConfig getFTPConfigById(SQLiteDatabase db, String fid) {
        Cursor cursor = db.query("FtpList", null, "fid=?", new String[]{fid}, null, null, null);
        while (cursor.moveToNext()) {
            FtpConfig fe = new FtpConfig();
            fe.setFid(AppUtil.getInt(cursor.getString(cursor.getColumnIndex("fid"))));
            fe.setFtpName(cursor.getString(cursor.getColumnIndex("ftpName")));
            fe.setFtpHost(cursor.getString(cursor.getColumnIndex("ftpHost")));
            fe.setFtpPort(AppUtil.getInt(cursor.getString(cursor.getColumnIndex("ftpPort"))));
            fe.setFtpHost1(cursor.getString(cursor.getColumnIndex("ftpHost1")));
            fe.setFtpPort1(AppUtil.getInt(cursor.getString(cursor.getColumnIndex("ftpPort1"))));
            fe.setFtpUserName(cursor.getString(cursor.getColumnIndex("ftpUserName")));
            fe.setFtpUserPwd(cursor.getString(cursor.getColumnIndex("ftpUserPwd")));
            fe.setAnonymousLogin(AppUtil.getInt(cursor.getString(cursor.getColumnIndex("anonymousLogin"))));
            fe.setDomainToIp(AppUtil.getInt(cursor.getString(cursor.getColumnIndex("domainToIp"))));
            fe.setFtpMode(AppUtil.getInt(cursor.getString(cursor.getColumnIndex("ftpMode"))));
            fe.setFtpEncoded(cursor.getString(cursor.getColumnIndex("ftpEncoded")));
            fe.setCreatorDate(DateUtil.getDate(cursor.getString(cursor.getColumnIndex("creatorDate"))));
            fe.setUpdateDate(DateUtil.getDate(cursor.getString(cursor.getColumnIndex("updateDate"))));
            return fe;
        }
        return null;
    }

    public static List<FtpConfig> getFTPConfigList(SQLiteDatabase db) {
        List<FtpConfig> ftplist = new ArrayList<>();
        Cursor cursor = db.query("FtpList", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            FtpConfig fe = new FtpConfig();
            fe.setFid(AppUtil.getInt(cursor.getString(cursor.getColumnIndex("fid"))));
            fe.setFtpName(cursor.getString(cursor.getColumnIndex("ftpName")));
            fe.setFtpHost(cursor.getString(cursor.getColumnIndex("ftpHost")));
            fe.setFtpPort(AppUtil.getInt(cursor.getString(cursor.getColumnIndex("ftpPort"))));
            fe.setFtpHost1(cursor.getString(cursor.getColumnIndex("ftpHost1")));
            fe.setFtpPort1(AppUtil.getInt(cursor.getString(cursor.getColumnIndex("ftpPort1"))));
            fe.setFtpUserName(cursor.getString(cursor.getColumnIndex("ftpUserName")));
            fe.setFtpUserPwd(cursor.getString(cursor.getColumnIndex("ftpUserPwd")));
            fe.setAnonymousLogin(AppUtil.getInt(cursor.getString(cursor.getColumnIndex("anonymousLogin"))));
            fe.setDomainToIp(AppUtil.getInt(cursor.getString(cursor.getColumnIndex("domainToIp"))));
            fe.setFtpMode(AppUtil.getInt(cursor.getString(cursor.getColumnIndex("ftpMode"))));
            fe.setFtpEncoded(cursor.getString(cursor.getColumnIndex("ftpEncoded")));
            fe.setCreatorDate(DateUtil.getDate(cursor.getString(cursor.getColumnIndex("creatorDate"))));
            fe.setUpdateDate(DateUtil.getDate(cursor.getString(cursor.getColumnIndex("updateDate"))));
            ftplist.add(fe);
        }
        return ftplist;
    }

    public static int getFileIcon(boolean isFile, boolean isLocal, int childCount, String path) {
        if (isFile) {
            return getFileIcon(path);
        } else {
            if (isLocal) {
                if (childCount == 0) {
                    return R.drawable.sys_folder_empty;
                } else if (childCount > 0) {
                    return R.drawable.sys_folder_use;
                } else {
                    return R.drawable.sys_folder_question;
                }
            } else {
                if (childCount == 0) {
                    return R.drawable.sys_ftp_folder;
                } else if (childCount > 0) {
                    return R.drawable.sys_ftp_folder_use;
                } else {
                    return R.drawable.sys_ftp_folder_question;
                }
            }
        }
    }

    public static int getFileIcon(String path) {
        int imgint = R.drawable.file_none;
        if (AppUtil.isExt(path, ConstantUtil.IMAGE_ALL_SUFFIX_GATHER)) {
            imgint = R.drawable.file_jpg;
            if (AppUtil.isExt(path, "png")) {
                imgint = R.drawable.file_png;
            }
        } else if (AppUtil.isExt(path, "tiff,tif")) {
            imgint = R.drawable.file_tif;
        } else if (AppUtil.isExt(path, "wmf,emf")) {
            imgint = R.drawable.file_jpg;
        } else if (AppUtil.isExt(path, "zip")) {
            imgint = R.drawable.file_zip;
        } else if (AppUtil.isExt(path, "cab")) {
            imgint = R.drawable.file_cab;
        } else if (AppUtil.isExt(path, ConstantUtil.ZIP_SUFFIX_GATHER)) {
            imgint = R.drawable.file_rar;
        } else if (AppUtil.isExt(path, "xlsx,xls")) {
            imgint = R.drawable.file_xls;
        } else if (AppUtil.isExt(path, "doc,docx")) {
            imgint = R.drawable.file_doc;
        } else if (AppUtil.isExt(path, "ppt,pptx")) {
            imgint = R.drawable.file_ppt;
        } else if (AppUtil.isExt(path, ConstantUtil.TXT_SUFFIX_GATHER)) {
            imgint = R.drawable.file_txt;
        } else if (AppUtil.isExt(path, "mpg4")) {
            imgint = R.drawable.file_mpeg;
        } else if (AppUtil.isExt(path, "mpg")) {
            imgint = R.drawable.file_mpg;
        } else if (AppUtil.isExt(path, "avi")) {
            imgint = R.drawable.file_avi;
        } else if (AppUtil.isExt(path, ConstantUtil.VIDEO_SUFFIX_GATHER)) {
            imgint = R.drawable.file_mp4;
        } else if (AppUtil.isExt(path, "midi")) {
            imgint = R.drawable.file_mid;
        } else if (AppUtil.isExt(path, "mp3")) {
            imgint = R.drawable.file_mp3;
        } else if (AppUtil.isExt(path, ConstantUtil.MUSIC_SUFFIX_GATHER)) {
            imgint = R.drawable.file_wav;
        }
        return imgint;
    }

}
