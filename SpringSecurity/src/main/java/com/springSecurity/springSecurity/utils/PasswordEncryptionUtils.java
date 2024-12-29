package com.springSecurity.springSecurity.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryptionUtils {
    // Validate the password by comparing plain text and encrypted
    public static boolean validatePassword(String plain, String encrypted) {
        if (notNull(plain) && notNull(encrypted)) {
            // Split the encrypted password into hash and salt
            String[] stack = encrypted.split(":");

            if (stack.length != 2) return false;

            // Hash the salt and plain text password and compare with the stored hash
            return md5(stack[1] + plain).equals(stack[0]);
        }

        return false;
    }

    // Check if the value is not null
    private static boolean notNull(Object value) {
        if (value instanceof String strValue) {
            return !strValue.trim().isEmpty() && !strValue.equals("NULL");
        } else if (value instanceof Integer) {
            return true;
        } else if (value instanceof Object[]) {
            return ((Object[]) value).length > 0;
        }

        return value != null;
    }

    // Generate MD5 hash of a string
    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
