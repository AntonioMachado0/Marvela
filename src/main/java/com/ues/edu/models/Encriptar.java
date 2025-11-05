package com.ues.edu.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.mindrot.jbcrypt.BCrypt;

public class Encriptar {

    // Algoritmos disponibles
    public static final String MD2 = "MD2";
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    public static final String SHA384 = "SHA-384";
    public static final String SHA512 = "SHA-512";

    // Convierte un arreglo de bytes a hexadecimal
    private static String toHexadecimal(byte[] digest) {
        StringBuilder hash = new StringBuilder();
        for (byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) {
                hash.append("0");
            }
            hash.append(Integer.toHexString(b));
        }
        return hash.toString();
    }

    // Encripta una cadena usando el algoritmo especificado
    public static String getStringMessageDigest(String cadena, String algoritmo) {
        if (cadena == null || algoritmo == null) {
            throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algoritmo);
            messageDigest.reset();
            messageDigest.update(cadena.getBytes());
            byte[] digest = messageDigest.digest();
            return toHexadecimal(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo no soportado: " + algoritmo, e);
        }
    }

    // Encripta una contraseña usando BCrypt
    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // Verifica si la contraseña coincide con el hash
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || hashedPassword == null) {
            return false;
        }
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}