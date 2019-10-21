package com.example.ftpmanage.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPFile;

public class ImgUtil {

    /**
     * 返回缩略图
     *
     * @param imagePath 图片地址
     * @param maxWidth  缩略图最大宽度
     * @param maxHeight 缩略图最大高度
     * @return
     */
    public static Bitmap getThumbnail(String imagePath, int maxWidth, int maxHeight) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            return getThumbnail(bitmap, maxWidth, maxHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回缩略图
     *
     * @param bitmap    图片
     * @param maxWidth  缩略图最大宽度
     * @param maxHeight 缩略图最大高度
     * @return
     */
    public static Bitmap getThumbnail(Bitmap bitmap, int maxWidth, int maxHeight) {
        if (bitmap != null) {
            try {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                if (width > maxWidth || height > maxHeight) {
                    if (width > maxWidth) {
                        height = (int) (((double) height * (double) maxWidth) / (double) width);
                        width = maxWidth;
                    }
                    if (height > maxHeight) {
                        width = (int) (((double) width * (double) maxHeight) / (double) height);
                        height = maxHeight;
                    }
                } else {
                    return bitmap;
                }
                return ThumbnailUtils.extractThumbnail(bitmap, width, height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Bitmap getBitmap(String imagePath) {
        try {
            return BitmapFactory.decodeFile(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据本地图片路径读取并返回本地图片路径
     * @param imagePath 本地图片路径
     * @return
     */
    public static String getLocalPath(String imagePath) {
        if (AppUtil.isExt(imagePath, ConstantUtil.IMAGE_SUFFIX_GATHER)) {
            File sfile = new File(imagePath);
            if (sfile.exists()) {
                return imagePath;
            }else{
                imagePath = imagePath + ConstantUtil.IMAGE_PRIVATE_SUFFIX;
                sfile = new File(imagePath);
                if (sfile.exists()) {
                    return imagePath;
                }
            }
        }else if (AppUtil.isExt(imagePath, ConstantUtil.IMAGE_PRIVATE_SUFFIX_GATHER)) {
            File sfile = new File(imagePath);
            if (sfile.exists()) {
                return imagePath;
            }
        }
        return "";
    }

    /**
     * 根据本地图片路径读取并返回图片
     * @param imagePath 本地图片路径
     * @return
     */
    public static Bitmap getLocalBitmap(String imagePath) {
        if (AppUtil.isExt(imagePath, ConstantUtil.IMAGE_SUFFIX_GATHER)) {
            File sfile = new File(imagePath);
            if (sfile.exists()) {
                return getBitmap(imagePath);
            }else{
                imagePath = imagePath + ConstantUtil.IMAGE_PRIVATE_SUFFIX;
                sfile = new File(imagePath);
                if (sfile.exists()) {
                    return getBitmap(imagePath);
                }
            }
        }else if (AppUtil.isExt(imagePath, ConstantUtil.IMAGE_PRIVATE_SUFFIX_GATHER)) {
            File sfile = new File(imagePath);
            if (sfile.exists()) {
                return getBitmap(imagePath);
            }
        }
        return null;
    }

    /**
     * 根据本地图片路径返回本地图片真实路径
     * @param imagePath 本地图片路径
     * @return
     */
    public static String getLocalImagePath(String imagePath) {
        if (AppUtil.isExt(imagePath, ConstantUtil.IMAGE_SUFFIX_GATHER)) {
            File sfile = new File(imagePath);
            if (sfile.exists()) {
                return imagePath;
            }else{
                imagePath = imagePath + ConstantUtil.IMAGE_PRIVATE_SUFFIX;
                sfile = new File(imagePath);
                if (sfile.exists()) {
                    return imagePath;
                }
            }
        }else if (AppUtil.isExt(imagePath, ConstantUtil.IMAGE_PRIVATE_SUFFIX_GATHER)) {
            File sfile = new File(imagePath);
            if (sfile.exists()) {
                return imagePath;
            }
        }
        return "";
    }
}
