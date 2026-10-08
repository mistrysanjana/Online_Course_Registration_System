package com.courseregistration.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility for hashing and verifying passwords.
 * Supports both SHA-256 hashing and plain-text fallback (for educational demonstration/sample data).
 */
public class PasswordUtil {

    /**
     * Hashes a password using SHA-256.
     *
     * @param password Raw password string
     * @return 64-character hexadecimal SHA-256 hash string
     */
    public static String hashPassword(String password) {
        if (password == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Verifies raw input password against stored password.
     * Matches either plain-text directly OR SHA-256 hash.
     *
     * @param rawInput       Password entered by the user
     * @param storedPassword Password string stored in the database
     * @return true if matches, false otherwise
     */
    public static boolean verifyPassword(String rawInput, String storedPassword) {
        if (rawInput == null || storedPassword == null) {
            return false;
        }

        // Direct match (for initial database scripts / college demo simplicity)
        if (rawInput.equals(storedPassword)) {
            return true;
        }

        // SHA-256 hash match
        String hashed = hashPassword(rawInput);
        return hashed.equalsIgnoreCase(storedPassword);
    }
}
