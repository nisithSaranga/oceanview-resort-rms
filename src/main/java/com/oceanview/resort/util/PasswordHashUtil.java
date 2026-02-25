package com.oceanview.resort.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordHashUtil {

    private PasswordHashUtil() { }

    /**
     * Hash a password using SHA-256 (coursework-friendly).
     * Output: lowercase hex string.
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null) {
            throw new IllegalArgumentException("plainPassword cannot be null");
        }
        return sha256Hex(plainPassword);
    }

    public static boolean verifyPassword(String plainPassword, String expectedHashHex) {
        if (plainPassword == null || expectedHashHex == null) {
            return false;
        }
        String actual = hashPassword(plainPassword);
        return constantTimeEquals(actual, expectedHashHex);
    }

    private static String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return toHex(digest);
        } catch (NoSuchAlgorithmException e) {
            // Should never happen on a normal JDK
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(Character.forDigit((b >>> 4) & 0xF, 16));
            sb.append(Character.forDigit(b & 0xF, 16));
        }
        return sb.toString();
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) return false;
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}