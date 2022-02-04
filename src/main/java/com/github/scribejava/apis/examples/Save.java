package com.github.scribejava.apis.examples;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Save {
    public byte[] refreshTokenHash;
    public byte[] keySalt;
    public byte[] iv;

    public static Save encryptToken(String token, String pin) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] byteToken = token.getBytes(StandardCharsets.UTF_8);

        byte[] iv = generate(16);
        byte[] salt = generate(12);

        String keyS = pin+String.valueOf(Hex.encodeHex(salt)).substring(0, 12);

        byte[] key = keyS.getBytes(StandardCharsets.UTF_8);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        Save result = new Save();
        result.keySalt = salt;
        result.iv = iv;
        result.refreshTokenHash = cipher.doFinal(byteToken);
        return result;
    }


    public static byte[] generate(int size) {
        byte[] random = new byte[size];
        (new SecureRandom()).nextBytes(random);
        return random;
    }

    public static byte[] decryptToken(byte[] byteToken, String pin, byte[] iv, byte[] salt) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String keyS = pin+String.valueOf(Hex.encodeHex(salt)).substring(0, 12);

        byte[] key = keyS.getBytes(StandardCharsets.UTF_8);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // инициаллизируем нашим шифронабором
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        return cipher.doFinal(byteToken);
    }

    public static void WriteSave(Save save) throws IOException {
        File f = new File("/save.json");
        if (!f.exists()) {
            f.createNewFile();
        }
        FileWriter file = new FileWriter("/save.json");
        String temp = new Gson().toJson(save);
        file.write(temp);
        file.close();
    }

    public static Save ReadSave() throws IOException {
        String saveJSON =  new String(Files.readAllBytes(Paths.get("/save.json")));
        return new Gson().fromJson(saveJSON, Save.class);
    }

    public static boolean IsAuthentificated(){
        File f = new File("/save.json");
        return f.exists();
    }

    public static boolean DeleteSave()
    {
        File file = new File("/save.json");
        return file.delete();
    }
}
