package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Hashing password sederhana (SHA-256 + salt acak), tanpa dependency eksternal.
 * Format tersimpan: "saltHex:hashHex"
 */
public class PasswordUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String hash(String plain) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        String saltHex = toHex(salt);
        String hashHex = sha256(saltHex, plain);
        return saltHex + ":" + hashHex;
    }

    public static boolean verify(String plain, String stored) {
        if (stored == null || !stored.contains(":")) return false;
        String[] parts = stored.split(":", 2);
        String saltHex = parts[0];
        String expectedHashHex = parts[1];
        String actualHashHex = sha256(saltHex, plain);
        return actualHashHex.equals(expectedHashHex);
    }

    private static String sha256(String saltHex, String plain) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(saltHex.getBytes());
            byte[] hashed = digest.digest(plain.getBytes());
            return toHex(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
