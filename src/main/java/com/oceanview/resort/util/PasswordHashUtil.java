package com.oceanview.resort.util;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordHashUtil {

    private static final SecureRandom RNG = new SecureRandom();

    // Safe defaults (works fine for coursework)
    private static final int SALT_BYTES = 16;
    private static final int ITERATIONS = 120_000;
    private static final int KEY_LENGTH_BITS = 256;

    private PasswordHashUtil() {}

    /**
     * Output format: pbkdf2:<iterations>:<saltB64>:<hashB64>
     */
    public static String hash(String plainPassword) {
        if (plainPassword == null || plainPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null/blank");
        }

        byte[] salt = new byte[SALT_BYTES];
        RNG.nextBytes(salt);

        byte[] derived = pbkdf2(plainPassword.toCharArray(), salt, ITERATIONS, KEY_LENGTH_BITS);

        return "pbkdf2:" + ITERATIONS + ":" +
                Base64.getEncoder().encodeToString(salt) + ":" +
                Base64.getEncoder().encodeToString(derived);
    }

    public static boolean verify(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null || storedHash.isBlank()) {
            return false;
        }

        // expected: pbkdf2:120000:salt:hash
        String[] parts = storedHash.split(":");
        if (parts.length != 4) return false;
        if (!"pbkdf2".equalsIgnoreCase(parts[0])) return false;

        int iterations;
        try {
            iterations = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        byte[] salt;
        byte[] expected;
        try {
            salt = Base64.getDecoder().decode(parts[2]);
            expected = Base64.getDecoder().decode(parts[3]);
        } catch (IllegalArgumentException e) {
            return false;
        }

        byte[] actual = pbkdf2(plainPassword.toCharArray(), salt, iterations, expected.length * 8);
        return constantTimeEquals(expected, actual);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLenBits) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLenBits);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            // Fallback for older JDKs if needed:
            try {
                PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLenBits);
                SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                return skf.generateSecret(spec).getEncoded();
            } catch (GeneralSecurityException ex) {
                throw new IllegalStateException("PBKDF2 not available", ex);
            }
        }
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a == null || b == null) return false;
        if (a.length != b.length) return false;

        int diff = 0;
        for (int i = 0; i < a.length; i++) {
            diff |= (a[i] ^ b[i]);
        }
        return diff == 0;
    }
}
