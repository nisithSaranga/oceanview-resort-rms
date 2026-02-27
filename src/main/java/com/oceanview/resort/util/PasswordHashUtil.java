package com.oceanview.resort.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordHashUtil {

    private static final String ALGO = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 65_536;
    private static final int SALT_BYTES = 16;
    private static final int KEY_LENGTH_BITS = 256;

    private static final SecureRandom RNG = new SecureRandom();

    private PasswordHashUtil() {}

    /**
     * Output format stored in DB:
     * pbkdf2$<iterations>$<saltBase64>$<hashBase64>
     */
    public static String hashPassword(char[] plainPassword) {
        if (plainPassword == null || plainPassword.length == 0) {
            throw new IllegalArgumentException("Password must not be blank");
        }

        byte[] salt = new byte[SALT_BYTES];
        RNG.nextBytes(salt);

        byte[] hash = pbkdf2(plainPassword, salt, ITERATIONS, KEY_LENGTH_BITS);

        return "pbkdf2$" + ITERATIONS + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(char[] plainPassword, String storedHash) {
        if (plainPassword == null || plainPassword.length == 0) return false;
        if (storedHash == null || storedHash.isBlank()) return false;

        String[] parts = storedHash.split("\\$");
        if (parts.length != 4) return false;
        if (!"pbkdf2".equals(parts[0])) return false;

        int iterations;
        try {
            iterations = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        byte[] salt = Base64.getDecoder().decode(parts[2]);
        byte[] expected = Base64.getDecoder().decode(parts[3]);

        byte[] actual = pbkdf2(plainPassword, salt, iterations, expected.length * 8);

        return MessageDigest.isEqual(expected, actual); // constant-time compare
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLengthBits) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLengthBits);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGO);
            return skf.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new IllegalStateException("Password hashing failed", e);
        }
    }
}