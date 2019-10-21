package com.example.ftpmanage.dfileselector.provide.size;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.ftpmanage.entity.FileExt;

import java.io.File;
import java.io.FileFilter;

/**
 * @author duke
 * @dateTime 2018-09-08 15:04
 * @description 获取文件数量或者大小
 */
public abstract class FileSizeProvide {

    public String getFileSize(Context context, FileExt file, FileFilter fileFilter) {
        if (context == null || file == null) {
            return null;
        }
        if (file.isFile()) {
            return getFileLengthIfFile(context, file);
        } else {
            File[] files = file.listFiles(fileFilter);
            file.setChildCount(files.length);
            return getFileCountIfFolder(context, files);
        }
    }

    public abstract String getFileCountIfFolder(@NonNull Context context, @NonNull File[] files);

    public abstract String getFileLengthIfFile(@NonNull Context context, @NonNull File file);

}
