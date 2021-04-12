import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

public class CryptoClass {
    public static byte[] funDecryptorEncrypt(byte[] privateKey,String password,String encordec,String meaningfulText) throws Exception
    {
        int modeencordec =0;
        switch (encordec){ // encrypt or decrypt mode
            case "DEC":
                modeencordec = Cipher.DECRYPT_MODE;
                break;
            case "ENC":
                modeencordec = Cipher.ENCRYPT_MODE;
                break;
        }

        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding"); // setting encyrption based on algorithm mode (AES/DES) and mode (CBC/OFB)
        cipher.init(modeencordec, generateKey(password));


        if(modeencordec == Cipher.ENCRYPT_MODE)
        {
        	byte[] meaningfulTextBytes = meaningfulText.getBytes();		// meaningful text bytes
        	byte[] message = new byte[privateKey.length+meaningfulText.length()];	// new ByteArray
        	System.arraycopy(privateKey, 0, message, 0, privateKey.length);		
        	System.arraycopy(meaningfulTextBytes, 0, message, privateKey.length, meaningfulTextBytes.length);
        	// message = privateKey + meaningfulText
        	
            byte[] messageBytes = doPadding(message); // padding
            return cipher.doFinal(messageBytes);		// return encrypted privateKey
        }
        else
            return cipher.doFinal(privateKey);			//return decrypted privateKey
    }

    public static SecretKey generateKey(String password) throws NoSuchAlgorithmException // MD5 hash of taken password
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        SecretKey secretKey2 = new SecretKeySpec(digest,"AES");
        return secretKey2;
    }

    public static String generateHash(File file,String type) throws NoSuchAlgorithmException, IOException // to find hash of a file
    {
        MessageDigest md = MessageDigest.getInstance(type);		// MD5 or SHA-256
        FileInputStream fis = new FileInputStream(file);
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;
        while ((bytesCount = fis.read(byteArray)) != -1)
            md.update(byteArray, 0, bytesCount);
        fis.close();
        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        return sb.toString();
    }

    public static byte[] doPadding(byte[] before)
    {
        int needed = 128 - (before.length % 128);		// AES uses 128 byte blocks.
        byte[] after = new byte[before.length+needed];	// new ByteArray 
        System.arraycopy(before, 0, after, 0, before.length);
        for (int i=before.length;i<after.length;i++)
            after[i] = 85;		// 85 decimal = 01010101 binary
        return after;
    }
}
