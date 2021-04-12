import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

public class CryptoClass {
    public static String funDecryptorEncrypt(String message,IvParameterSpec ivParameterSpec,SecretKey key,String algorithmMode,String mode,String encordec,String username) throws Exception {

        int modeencordec =0;

        switch (encordec){ // encrypt or decrypt mode
            case "DEC":
            modeencordec = Cipher.DECRYPT_MODE;
            break;
            case "ENC":
                modeencordec = Cipher.ENCRYPT_MODE;
                break;
        }

        Cipher cipher = Cipher.getInstance(algorithmMode+"/"+mode+"/"+"PKCS5Padding"); // setting encyrption based on algorithm mode (AES/DES) and mode (CBC/OFB)
        cipher.init(modeencordec, key, ivParameterSpec);

        // base64 encoding and encryption
        if(modeencordec == Cipher.ENCRYPT_MODE){
            byte[] result = cipher.doFinal(message.getBytes("UTF-8"));
            byte[] temp = Base64.getEncoder().encode(result);
            return new String(temp,"UTF-8");
        }
        else{ // decode and decryption
            byte[] decoded = Base64.getDecoder().decode(message.getBytes("UTF-8"));
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(message.getBytes("UTF-8")));
            return new String(result,"UTF-8");
        }
    }

    public IvParameterSpec generateIVector(String algorithmMode,String mode) throws NoSuchPaddingException, NoSuchAlgorithmException 
    {	// generating iv vector for aes/cbc-ofb and des/cbc-ofb
        Cipher cipher = Cipher.getInstance(algorithmMode+"/"+mode+"/"+"PKCS5Padding");
        SecureRandom randomSecureRandom = new SecureRandom();
        byte[] iv2 = new byte[cipher.getBlockSize()];
        randomSecureRandom.nextBytes(iv2);
        IvParameterSpec ivParams = new IvParameterSpec(iv2);
        return ivParams ;
    }
    
    public HashMap<String,SecretKey> generateKey() throws NoSuchAlgorithmException 
    {	// aes and des keys, will be sent to the clients
        HashMap<String,SecretKey> secretKeyHashMap = new HashMap<>();
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56); 								// for example
        SecretKey secretKey = keyGen.generateKey();
        secretKeyHashMap.put("DES",secretKey);
        KeyGenerator keyGen2 = KeyGenerator.getInstance("AES");
        keyGen2.init(128); 								// for example
        SecretKey secretKey2 = keyGen2.generateKey();
        secretKeyHashMap.put("AES",secretKey2);
        return secretKeyHashMap;
    }
}
