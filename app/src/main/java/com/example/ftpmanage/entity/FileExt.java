package com.example.ftpmanage.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ftpmanage.utils.JsonUtil;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class FileExt extends File {

    private int childCount = -1;

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public FileExt(@NonNull String pathname) {
        super(pathname);
    }

    public FileExt(@Nullable String parent, @NonNull String child) {
        super(parent, child);
    }

    public FileExt(@Nullable File parent, @NonNull String child) {
        super(parent, child);
    }

    public FileExt(@NonNull URI uri) {
        super(uri);
    }

    public List<FileExt> getFileExtList() {
        File[] files = super.listFiles();
        List<FileExt> fextList = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                FileExt fext = JsonUtil.omap.readValue(JsonUtil.omap.writeValueAsString(file), FileExt.class);
                fextList.add(fext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fextList;
    }

    public static FileExt getFileExt(File file) {
        try {
            FileExt fext = JsonUtil.omap.readValue(JsonUtil.omap.writeValueAsString(file), FileExt.class);
            return fext;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
