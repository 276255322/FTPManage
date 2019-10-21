package com.example.ftpmanage.utils;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加解密
 */
public class AppAES {

    /**
     * AES加密
     * @param content  待加密的内容
     * @param password 加密密码
     * @return
     */
    public static byte[] encryptBitAES(byte[] content, String password) {
        try {
            Cipher encryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, getKey(password));
            byte[] result = encryptCipher.doFinal(content);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES解密
     * @param content 待解密内容
     * @param password 解密密钥
     * @return
     */
    public static byte[] decryptBitAES(byte[] content, String password) {
        try {
            Cipher decryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, getKey(password));
            byte[] result = decryptCipher.doFinal(content);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES字符串加密
     * @param content 待加密的内容
     * @param password 加密密码
     * @return
     */
    public static String encryptAES(String content, String password) {
        try{
            return AppSecure.byte2Hex(encryptBitAES(content.getBytes(), password));
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }

    /**
     * AES字符串解密
     * @param content 待解密内容
     * @param password 解密密钥
     * @return
     */
    public static String decryptAES(String content, String password) {
        try{
            return new String(decryptBitAES(AppSecure.hex2Byte(content), password));
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 从指定字符串生成密钥
     * @param password 构成该秘钥的字符串
     * @return 生成的密钥
     * @throws NoSuchAlgorithmException
     */
    private static Key getKey(String password) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom(password.getBytes());
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        return key;
    }

}
