package com.example.ftpmanage.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5 sha 加密类
 */
public class AppSecure {

    /**
     * MD5加密算法
     *
     * @param s 需要加密的字符串
     * @return
     */
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s.getBytes("utf-8");
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * SHA 加密算法
     *
     * @param s 需要加密的字符串
     * @return
     */
    public static String SHA(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            return byte2Hex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * SHA1加密算法
     *
     * @param s 需要加密的字符串
     * @return
     */
    public static String SHA1(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            return byte2Hex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * SHA256加密
     *
     * @param s 需要加密的字符串
     * @return
     */
    public static String SHA256(String s) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(s.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte数组转换为表示16进制值的字符串. 如：byte[]{8,18}转换为：0812 和 byte[]
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的字符串
     */
    public static String byte2Hex(byte[] bytes) {
        int bytesLen = bytes.length;
        StringBuffer hexString = new StringBuffer(bytesLen * 2);
        for (int i = 0; i < bytesLen; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组, 和 String byte2HexStr(byte[] bytes) 互为可逆的转换过程
     *
     * @param s 字符串
     * @return 转换后的byte数组
     */
    public static byte[] hex2Byte(String s) {
        byte[] arrB = s.getBytes();
        int iLen = arrB.length;
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }
}
