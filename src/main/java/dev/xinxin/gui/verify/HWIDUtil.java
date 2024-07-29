package dev.xinxin.gui.verify;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HWIDUtil {
    public static String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        StringBuilder s = new StringBuilder();
        String main = System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
        byte[] bytes = main.getBytes("UTF-8");
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256"); // 使用 SHA-256 算法增加安全性
        byte[] hash = messageDigest.digest(bytes);

        //哈希值大神
        String hex = bytesToHex(hash).substring(0, 6);

        //添加前缀 Lavender
        s.append(hex);
        return s.toString();
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
