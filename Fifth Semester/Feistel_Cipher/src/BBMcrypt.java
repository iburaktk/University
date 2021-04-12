import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

public class BBMcrypt
{
    private String[] args;
    
    public static void main(String[] args) throws Exception 
    {
        HashMap<String,String> parameters = new HashMap<>(); // Stored program arguments in a map.
        parameters.put("encordec",args[0]);					 // enc for encryption, dec for decryption
        for(int i =1;i<args.length;i=i+2)
            parameters.put(args[i],args[i+1]);
        
        int mode = 0;										 // Block cipher modes
        switch (parameters.get("-M"))
        {
            case "ECB":
                mode =1;
                break;
            case "CBC":
                mode=2;
                break;
            case "OFB":
                mode=3;
                break;
        }
        
        PrintStream output = new PrintStream(new FileOutputStream(new File(parameters.get("-O"))));
        
        int encrypt = 0;									 // encryption or decryption
        switch(parameters.get("encordec"))
        {
            case "enc":
                encrypt =1;
                break;
            case "dec":
                encrypt =0;
                break;
        }
        
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(parameters.get("-I")), "utf-8"));
        // reader for input text
        BufferedReader bufferedReader1 = new  BufferedReader(new InputStreamReader(new FileInputStream(parameters.get("-K")), "utf-8"));
        // reader for key
        String s = "";
        String base64Key = "";
        while ((s= bufferedReader1.readLine() )!= null)
        {
            byte[] decode = Base64.getDecoder().decode(s.getBytes());
            base64Key = new String(decode);
        }
        
        File file = new File(parameters.get("-O"));			 // output file
        
        if (mode == 1)
            Operations.ecb(bufferedReader,file, base64Key, encrypt);
        if (mode == 2)
            Operations.cbc(bufferedReader,file, base64Key, encrypt);
        if (mode == 3)
            Operations.ofb(bufferedReader,file, base64Key, encrypt);
    }
}
