package com.fastwork.library.mutils;

import android.text.TextUtils;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by lenovo on 2019/1/9.
 * Aes 加密解密
 */

public class AesUtil {

    private static String TAG = AesUtil.class.getName();
    public static String KEY = "B236C808C9FBB0F3";
    public static String IV = "B166AEC87B44C94F";

    //加密
    public static String encrypt(String encData, String key, String ivs) throws Exception {
        if (TextUtils.isEmpty(encData) || TextUtils.isEmpty(key) || TextUtils.isEmpty(ivs)) {
            MLogUtil.e(TAG, "encrypt data is null");
            return "";
        }
        if (key.length() != 16) {
            MLogUtil.e(TAG, "secretKey length no is 16");
            return "";
        }
        if (ivs.length() != 16) {
            MLogUtil.e(TAG, "vector is no is 16");
            return "";
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(encData.getBytes());
        return Base64.encodeToString(encrypted, Base64.NO_WRAP).replaceAll("[\\s*\t\n\r]", ""); // 此处使用BASE64做转码。
    }

    // 解密
    public static String decrypt(String sSrc, String key, String ivs) throws Exception {
        if (TextUtils.isEmpty(sSrc) || TextUtils.isEmpty(key) || TextUtils.isEmpty(ivs)) {
            MLogUtil.e(TAG, "decrypt data is null");
            return "";
        }
        if (key.length() != 16) {
            MLogUtil.e(TAG, "secretKey length no is 16");
            return "";
        }
        if (ivs.length() != 16) {
            MLogUtil.e(TAG, "vector is no is 16");
            return "";
        }
        if (sSrc.contains("\r\n")) {
            sSrc = sSrc.replaceAll("\r\n", "");
        }
        try {
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.decode(sSrc, Base64.NO_WRAP);
            //  byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "utf-8");
        } catch (Exception ex) {
            return null;
        }
    }

}
