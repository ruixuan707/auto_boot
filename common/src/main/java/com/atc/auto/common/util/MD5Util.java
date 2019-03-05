package com.atc.auto.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: kaKaXi
 * @Date: 2019/1/18 10:37
 * @Version 1.0
 * @Description:
 */
public class MD5Util {

    /**
     * md5
     * @param password
     * @return
     */
    public static String md5Password(String password) {

        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            for (byte b : result) {
                int number = b & 0xff;
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 可逆的加密算法
     * @param inStr
     * @return
     */
    public static String encryptKL(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;
    }

    /**
     * 解密
     * @param inStr
     * @return
     */
     public static String decryptJM(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String k = new String(a);
        return k;
    }

    /**
     * 加密
     * @param password
     * @return
     */
    public static String encrypt(String password) {

        return encryptKL(md5Password(password));
    }

}
