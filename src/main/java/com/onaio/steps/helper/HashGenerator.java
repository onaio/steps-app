package com.onaio.steps.helper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

    @Nullable
    public static String generate(@NonNull String input, @NonNull HashStrategy hashStrategy) {

        try {
            MessageDigest digest = MessageDigest.getInstance(hashStrategy.strategy);
            digest.reset();
            digest.update(input.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public enum HashStrategy {
        MD5("MD5"), SHA_256("SHA-256");

        final String strategy;

        HashStrategy(String strategy) {
            this.strategy = strategy;
        }
    }
}
