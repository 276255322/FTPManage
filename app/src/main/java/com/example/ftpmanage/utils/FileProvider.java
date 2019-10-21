package com.example.ftpmanage.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.example.ftpmanage.entity.FolderEntity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * 类：FileProviderUtils
 * 从APP向外共享的文件URI时，必须使用该类进行适配，否则在7.0以上系统，会报错：FileUriExposedException(文件Uri暴露异常)
 */
public class FileProvider {

    private static final String[][] MATCH_ARRAY = {
            //{后缀名，    文件类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".prop", "text/plain"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/zip"},
            {"", "*/*"}
    };

    /**
     * 通过系统应用打开文件
     *
     * @param activity 上下文
     * @param file     文件
     */
    public static void openFileOfApp(Activity activity, File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.uriFromFile(activity, file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        String path = file.getPath();
        String type = "";
        if (AppUtil.isExt(path, ConstantUtil.VIDEO_SUFFIX_GATHER)) {
            type = "video/*";
        } else if (AppUtil.isExt(path, ConstantUtil.MUSIC_SUFFIX_GATHER)) {
            type = "audio/*";
        } else if (AppUtil.isExt(path, ConstantUtil.ZIP_SUFFIX_GATHER)) {
            type = "application/zip";
        } else {
            for (int i = 0; i < MATCH_ARRAY.length; i++) {
                if (file.getPath().contains(MATCH_ARRAY[i][0])) {
                    type = MATCH_ARRAY[i][1];
                    break;
                }
            }
        }
        try {
            intent.setDataAndType(uri, type);
            activity.startActivity(intent);
        } catch (Exception e) {
            UiUtil.showPosition(activity,  "无法打开该格式文件，文件地址：" + path + "!");
            e.printStackTrace();
        }
    }

    /**
     * 从文件获得URI
     *
     * @param activity 上下文
     * @param file     文件
     * @return 文件对应的URI
     */
    public static Uri uriFromFile(Activity activity, File file) {
        Uri fileUri;
        //7.0以上进行适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String p = activity.getPackageName() + ".FileProvider";
            fileUri = androidx.core.content.FileProvider.getUriForFile(activity, p, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    /**
     * 设置Intent的data和类型，并赋予目标程序临时的URI读写权限
     *
     * @param activity  上下文
     * @param intent    意图
     * @param type      类型
     * @param file      文件
     * @param writeAble 是否赋予可写URI的权限
     */
    public static void setIntentDataAndType(Activity activity, Intent intent, String type, File file, boolean writeAble) {
        //7.0以上进行适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(uriFromFile(activity, file), type);
            //临时赋予读写Uri的权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
    }

    /**
     * 设置Intent的data和类型，并赋予目标程序临时的URI读写权限
     *
     * @param context   上下文
     * @param intent    意图
     * @param type      类型
     * @param fileUri   文件uri
     * @param writeAble 是否赋予可写URI的权限
     */
    public static void setIntentDataAndType(Context context, Intent intent, String type, Uri fileUri, boolean writeAble) {
        //7.0以上进行适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(fileUri, type);
            //临时赋予读写Uri的权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(fileUri, type);
        }
    }

    /**
     * 创建文件夹
     *
     * @param filePath
     */

    public static void createrDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    /**
     * 根据Uri返回文件路径
     *
     * @param context
     * @param contentURI
     * @return
     */
    public static String getUriFilePath(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
        if (cursor == null) result = contentURI.getPath();
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

    /**
     * 将文本写入到指定的文件路径
     *
     * @param filePath 文件保存路径
     * @param s
     * @return
     */
    public static String writeTextFile(String filePath, String s) {
        File file = new File(filePath);
        try {
            if (!file.isFile()) {
                file.createNewFile();
            }
            DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
            out.writeBytes(s);
        } catch (IOException ex) {
            return ex.getMessage();
        }
        return "";
    }

    /**
     * 根据指定的文件路径返回文件文本内容
     *
     * @param filePath 文件保存路径
     * @return
     */
    public static String readTextFile(String filePath) {
        File file = new File(filePath);
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tstr = null;
            int line = 1;
            while ((tstr = reader.readLine()) != null) {
                sb.append(tstr);
                ++line;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb.toString();
    }

    /**
     * 递归删除指定文件路径的所有文件或文件夹
     *
     * @param sfile
     */
    public static void deleteFile(File sfile) {
        if (sfile == null || !sfile.exists()) {
            return;
        }
        if (sfile.isDirectory()) {
            for (File file : sfile.listFiles()) {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    deleteFile(file);
                }
            }
        }
        sfile.delete();
    }

    public static void getFilesCountSize(FolderEntity fe, String filePath, int lv) {
        File fi = new File(filePath);
        if (lv == 0) {
            fe.setFilePath(filePath);
            if (fi.isDirectory()) {
                fe.setFileType(1);
            } else {
                fe.setFileType(0);
                fe.setFoldersSize(0);
                fe.setFilesSize(1);
                fe.setCountSzie(fi.length());
                return;
            }
        }
        File[] files = fi.listFiles();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f.isDirectory()) {
                    fe.setFoldersSize(fe.getFoldersSize() + 1);
                    lv++;
                    getFilesCountSize(fe, f.getPath(), lv);
                } else {
                    fe.setFilesSize(fe.getFilesSize() + 1);
                    fe.setCountSzie(fe.getCountSzie() + f.length());
                }
            }
        }
    }

    /**
     * 返回文件大小按K、M、G划分
     *
     * @param size 文件大小
     * @return
     */
    public static String getSize(long size) {
        if (size >= 1024 * 1024 * 1024) {
            return new Long(size / 1073741824L) + "GB";
        } else if (size >= 1024 * 1024) {
            return new Long(size / 1048576L) + "MB";
        } else if (size >= 1024) {
            return new Long(size / 1024) + "KB";
        }
        return size + "B";
    }

}
