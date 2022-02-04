package com.github.scribejava.apis.examples;

import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class App {
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException, URISyntaxException {
//        String plainText = "Hello, world!";
//        String crText = "";
//        String decrText = "";
//        Save save = null;
//        byte[] decr = null;
//        String pin = "1234";
//        System.out.println(plainText);
//        try {
//            save = Save.encrypt(pin, plainText);
//        } catch (Exception e){
//            System.out.println("1 exception: " + e.getMessage());
//        }
//        System.out.println(new String(Hex.encodeHex(save.refreshTokenHash, false)));
//        try {
//            decr = Save.decrypt(pin, save.refreshTokenHash, save.iv, save.keySalt);
//        } catch (Exception e){
//            System.out.println("2 exception: " + e.getMessage());
//        }
//        System.out.println(new String(decr));

//        String plainText = "Hello, world!";
//        String pin = "1234";
//        System.out.println(plainText);
//        String enc_temp = "";
//        Save save = null;
//        byte[] save2 = null;
//        try {
//            save = Save.encryptToken(plainText, pin);
//            enc_temp = String.valueOf(Hex.encodeHex(save.refreshTokenHash));
//        }
//        catch (Exception e){
//            System.out.println("1 exception: " + e.getMessage());
//        }
//        System.out.println(enc_temp);
//        String dec_temp = "";
//        try {
//            save2 = Save.decryptToken(save.refreshTokenHash, pin, save.iv, save.keySalt);
//            dec_temp = new String(save2);
//        }
//        catch (Exception e){
//            System.out.println("2 exception: " + e.getMessage());
//        }
//        System.out.println(dec_temp);

        Dropbox dropbox = new Dropbox();
        Random rnd = new Random();
        int random_int = rnd.nextInt(1000);
        dropbox.createFolder("new dir" + random_int);
        var folders = dropbox.getFolders();
        for (DBFolder folder: folders
             ) {
            System.out.print(folder.getName() + " ");
        }
        dropbox.updateFolder("new dir" + random_int, "new dir" + random_int + "UPDATED");
        folders = dropbox.getFolders();
        for (DBFolder folder: folders
        ) {
            System.out.print(folder.getName() + " ");
        }
        dropbox.deleteFolder("new dir" + random_int + "UPDATED");
        folders = dropbox.getFolders();
        for (DBFolder folder: folders
        ) {
            System.out.print(folder.getName() + " ");
        }

        System.out.println(dropbox.getrefToken());
    }
}
