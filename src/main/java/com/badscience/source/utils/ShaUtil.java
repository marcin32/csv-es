package com.badscience.source.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaUtil {
    public static String shaHash(final String content) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] digestContent = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digestContent);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String bytesToHex(byte[] hash) {
        final StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
