package com.example.hotel.model.service.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {
    private Encryptor() {
    }

    public static final String MD5 = "MD5";
    public static final String SHA_256 = "SHA-256";

    public static String encrypt(String input, String algorithm) throws NoSuchAlgorithmException {
        if (!algorithm.equals(MD5) && !algorithm.equals(SHA_256))
            throw new NoSuchAlgorithmException();

        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.update(input.getBytes());
        byte[] hash = digest.digest();

        StringBuilder stringBuilder = new StringBuilder();

        for (byte b : hash) {
            stringBuilder.append(Character.forDigit((b >> 4) & 0xF, 16));
            stringBuilder.append(Character.forDigit((b & 0xF), 16));
        }

        return stringBuilder.toString().toUpperCase();
    }
}