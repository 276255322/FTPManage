package com.example.ftpmanage.utils;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 3DES加密类
 */
public class App3DES {

    /**
     * 向量,长度必须是8位
     */
    private static String iv = "f18ca0a6";

    /**
     * 向量,长度必须是8位
     */
    public static String getIv() {
        return iv;
    }

    /**
     * 向量,长度必须是8位
     */
    public static void setIv(String iv) {
        App3DES.iv = iv;
    }

    /**
     * 加解密统一使用的编码方式
     */
    private static String encoding = "utf-8";

    /**
     * 加解密统一使用的编码方式
     */
    public static String getEncoding() {
        return encoding;
    }

    /**
     * 加解密统一使用的编码方式
     */
    public static void setEncoding(String encoding) {
        App3DES.encoding = encoding;
    }

    /**
     * 向量,长度必须是8位
     */
    private static String despwd = "90143823ed31424a9bbfee37e842bcd0";

    /**
     * 3DES加密
     *
     * @param s 普通文本
     * @return
     * @throws Exception
     */
    public static String encrypt3DES(String s) {
        return encrypt3DES(s, despwd, App3DES.iv, App3DES.encoding);
    }

    /**
     * 3DES加密
     *
     * @param s   普通文本
     * @param pwd 密码,长度不得小于24
     * @return
     * @throws Exception
     */
    public static String encrypt3DES(String s, String pwd) {
        return encrypt3DES(s, pwd, App3DES.iv, App3DES.encoding);
    }

    /**
     * 3DES加密
     *
     * @param s      普通文本
     * @param pwd    密码,长度不得小于24
     * @param encode 编码
     * @return
     * @throws Exception
     */
    public static String encrypt3DES(String s, String pwd, String encode) {
        return encrypt3DES(s, pwd, App3DES.iv, encode);
    }

    /**
     * 3DES加密
     *
     * @param s      普通文本
     * @param pwd    密码,长度不得小于24
     * @param ivs    向量
     * @param encode 编码
     * @return
     * @throws Exception
     */
    public static String encrypt3DES(String s, String pwd, String ivs, String encode) {
        try {
            Key deskey = null;
            DESedeKeySpec spec = new DESedeKeySpec(pwd.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
            byte[] encryptData = cipher.doFinal(s.getBytes(encode));
            return Base64.encodeToString(encryptData, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 3DES解密
     *
     * @param s 加密文本
     * @return
     * @throws Exception
     */
    public static String decrypt3DES(String s) {
        return decrypt3DES(s, despwd, App3DES.iv, App3DES.encoding);
    }

    /**
     * 3DES解密
     *
     * @param s   加密文本
     * @param pwd 密码,长度不得小于24
     * @return
     * @throws Exception
     */
    public static String decrypt3DES(String s, String pwd) {
        return decrypt3DES(s, pwd, App3DES.iv, App3DES.encoding);
    }

    /**
     * 3DES解密
     *
     * @param s      加密文本
     * @param pwd    密码,长度不得小于24
     * @param encode 编码
     * @return
     * @throws Exception
     */
    public static String decrypt3DES(String s, String pwd, String encode) {
        return decrypt3DES(s, pwd, App3DES.iv, encode);
    }

    /**
     * 3DES解密
     *
     * @param s      加密文本
     * @param pwd    密码,长度不得小于24
     * @param ivs    向量
     * @param encode 编码
     * @return
     * @throws Exception
     */
    public static String decrypt3DES(String s, String pwd, String ivs, String encode) {
        if (null != s && !"".equals(s)) {
            try {
                Key deskey = null;
                DESedeKeySpec spec = new DESedeKeySpec(pwd.getBytes());
                SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
                deskey = keyfactory.generateSecret(spec);
                Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
                IvParameterSpec ips = new IvParameterSpec(ivs.getBytes());
                cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
                byte[] decryptData = cipher.doFinal(Base64.decode(s, Base64.DEFAULT));
                return new String(decryptData, encode);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

}
