package com.oceanview.resort.util;

public final class HashGenerator {
    private HashGenerator() {}

    public static void main(String[] args) {
        // Change these values temporarily to generate what need
        String plain = "change-me";
        String hash = PasswordHashUtil.hash(plain);
        System.out.println("PLAIN: " + plain);
        System.out.println("HASH : " + hash);

        // sanity check
        System.out.println("VERIFY (correct): " + PasswordHashUtil.verify(plain, hash));
        System.out.println("VERIFY (wrong)  : " + PasswordHashUtil.verify("wrong", hash));
    }
}

