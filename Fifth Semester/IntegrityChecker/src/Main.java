import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class Main {


    public static void main(String[] args) throws Exception
    {
        HashMap<String,String> parameters = new HashMap<>();
        parameters.put("mode",args[0]);
        for(int i=1;i<args.length;i=i+2)
            parameters.put(args[i],args[i+1]);
        switch(parameters.get("mode")){
            case "createCert":
                createCert(parameters);
                break;
            case "createReg":
                createReg(parameters.get("-r"),parameters.get("-p"),parameters.get("-l"),parameters.get("-h"),parameters.get("-k"));
                break;
            case "check":
                check(parameters.get("-r"),parameters.get("-p"),parameters.get("-l"),parameters.get("-h"),parameters.get("-c"));
                break;
        }
    }

    public static void createCert(HashMap<String,String> parameters)
    {
        String pathOfCert = parameters.get("-c");
        String pathOfKey = parameters.get("-k");
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a password for keystore: ");	// firstly, keytool ask for a password
    	String password = input.nextLine();
        String command1 = "-genkey -alias keyPair46 -keyalg RSA -keysize 2048 -validity 46 -storepass "+password;	
        String command2 = "-exportcert -rfc -alias keyPair46 -file "+pathOfCert+" -storepass "+password;
        //this commands use the keystore password you entered
        execute(command1);
        execute(command2);
        PrivateKey key = null;
        try {
            File file = new File(System.getProperty("user.home") + File.separatorChar + ".keystore"); // opening keystore file which is created by keytool
            FileInputStream inputStream = new FileInputStream(file);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(inputStream, "password".toCharArray());
            key = (PrivateKey)keystore.getKey("keyPair46", "password".toCharArray());	// fetching our private key from keystore
            byte[] encodedPrivateKey = key.getEncoded();								// PKCS8 encoded
            System.out.println("Please enter a password to encrypt the private key: ");
            byte[] encrypted = CryptoClass.funDecryptorEncrypt(key.getEncoded(),input.nextLine(),"ENC","This is private key."); // encryption
            // this encryption function also adds meaningful text and does padding.
            File output = new File(pathOfKey);
            PrintStream out = new PrintStream(output);
            byte[] encoded = Base64.getEncoder().encode(encrypted);
            out.print(new String(encoded));		// saving encrypted private key into a file
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void createReg(String reg , String pathStr , String log, String hash, String  priKeyStr) throws Exception
    {
        System.out.println("enter your password: ");		// asks for the password to decrypt private key file
        FileWriter regFile = new FileWriter(new File(reg));
        FileWriter logFile = new FileWriter(new File(log));
        File path = new File(pathStr);
        File priKey = new File(priKeyStr);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(priKey));
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        byte[] bytes = bufferedReader.readLine().getBytes();	// reading encrypted private key from the file
        byte[] decoded = Base64.getDecoder().decode(bytes);		
        byte[] resultBytes = CryptoClass.funDecryptorEncrypt(decoded,password,"DEC",""); // decryption
        Date dated =new Date(System.currentTimeMillis());
        byte[] privateKeyBytes = stripMeaningful(resultBytes, "This is private key."); // strip meaningful text and padding
        if(privateKeyBytes == null)							// if decrypted key does not contain meaningful text
        {            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
            String date = dateFormat.format(dated);
            logFile.write("["+date+"] : Wrong password!");
            logFile.close();
        }
        else{												// everything is alright
            for(File f: path.listFiles()){					// lets create registry 
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
                String date = dateFormat.format(dated);
                regFile.write(f.getPath() +" "+ CryptoClass.generateHash(f,hash)+System.getProperty("line.separator"));
                logFile.write("["+date+"] : "+f.getName() +" added to registry"+System.getProperty("line.separator"));
            }
            regFile.close();
            String hashOfFile = CryptoClass.generateHash(new File(reg),hash); // calculate hash of registry file
            Signature sign;
            if(hash.equals("SHA-256"))
                sign = Signature.getInstance("SHA256withRSA");
            else if (hash.equals("MD5"))
                sign = Signature.getInstance("MD5withRSA");
            PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes); 
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(priKeySpec);				// PKCS8 decoded private key
            sign.initSign(privateKey);									
            sign.update(hashOfFile.getBytes());
            byte[] signature = sign.sign();								// sign registry file with private key
            PrintStream out = new PrintStream(new FileOutputStream(reg, true));
            out.write(Base64.getEncoder().encode(signature));			// append signature to end of registry file
            out.close();
            dated =new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
            String date = dateFormat.format(dated);
            logFile.write(path.listFiles().length+" files are added to the registry and "+
            		"registry creation is finished!\n");
            logFile.close();
            regFile.close();
       }
    }
    
    public static void check(String regFile , String path , String logFile1, String hash, String  pubKeyCert) throws Exception
    {

        byte[] writtenSignature = getSignature(regFile);			// get signature from registry file and remove it from file
        FileWriter logFile = new FileWriter(new File(logFile1),true);
        byte[] calculatedSignature = CryptoClass.generateHash(new File(regFile),hash).getBytes(); // calculate hash of registry file
        Signature sign;
        if(hash.equals("SHA-256"))
             sign = Signature.getInstance("SHA256withRSA");
        else if (hash.equals("MD5"))
             sign = Signature.getInstance("MD5withRSA");
        FileInputStream inputStream = new FileInputStream(new File(pubKeyCert));
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate)cf.generateCertificate(inputStream);
        PublicKey publicKey = cert.getPublicKey();				// get public key from certificate
        sign.initVerify(publicKey);
        sign.update(calculatedSignature);
        boolean verification;
        try {
        	verification = sign.verify(writtenSignature);		// verifiying signature
		} catch (Exception e) {
			verification = false;
		}
        if(!verification)
        {
            Date dated =new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
            String date = dateFormat.format(dated);
            logFile.write("["+date+"] : Verification Failed!\n");
        }
        else
        {
            File directory = new File(path);
            ArrayList<File> fileList = new ArrayList<>();
            ArrayList<String> checkedFiles = new ArrayList<>();
            for(File file : directory.listFiles())
                fileList.add(file);		// adding all files in that path to an ArrayList
            int changedcontent=0;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(regFile)));
            String l ="";
            while((l= bufferedReader.readLine())!= null)
            {
                String[] splitted = l.split(" ");
                try{
                    File file = new File(splitted[0]);  // path of file from registry file
                    if(!CryptoClass.generateHash(file,hash).equals(splitted[1].split("\n")[0].split("\r\n")[0])) // calculate hash of file
                    {												// if hash of file not equal to registry file, then file changed
                        Date dated =new Date(System.currentTimeMillis());
                        SimpleDateFormat dateFormat =
                                new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
                        String date = dateFormat.format(dated);
                        logFile.write("["+date+"] :  " +file.getName()+" changed" );
                        changedcontent++;
                    }
                }
                catch(FileNotFoundException e){				// file is deleted
                    Date dated =new Date(System.currentTimeMillis());
                    SimpleDateFormat dateFormat =
                            new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
                    String date = dateFormat.format(dated);
                    logFile.write("["+date+"] :  " +splitted[0].substring(splitted[0].lastIndexOf(System.getProperty("file.separator"))+1)+" deleted\n" );
                    changedcontent++;
                }
                checkedFiles.add(splitted[0]);		// this file is checked
            }
            for(File file :fileList)
            {
                String fileName = file.getPath();
                if(!checkedFiles.contains(fileName))	// if any extra file at this path which is not checked, then that file is added
                {
                    Date dated =new Date(System.currentTimeMillis());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
                    String date = dateFormat.format(dated);
                    logFile.write("["+date+"] :  " +fileName.substring(fileName.lastIndexOf(System.getProperty("file.separator"))+1)+" added\n");
                    changedcontent++;
                }
            }
            if(changedcontent==0){		// anything didnt changed
                Date dated =new Date(System.currentTimeMillis());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
                String date = dateFormat.format(dated);
                logFile.write("["+date+"] :  registry file not changed\n" );
            }
        }
        logFile.close();
        PrintStream out = new PrintStream(new FileOutputStream(regFile, true));
        out.write(Base64.getEncoder().encode(writtenSignature));
        out.close();
    }
    
    public static byte[] getSignature(String regFile) throws Exception
    {
    	File inputFile = new File(regFile);
    	File tempFile = new File("myTempFile.txt");

    	Scanner reader = new Scanner(inputFile);
    	FileWriter writer = new FileWriter(tempFile);
    	
    	String currentLine = reader.nextLine();
    	while(reader.hasNext()) 	// writing to temp file until last line which is signature
    	{
    	    String trimmedLine = currentLine.trim();
    	    writer.write(currentLine + System.getProperty("line.separator"));
    	    currentLine = reader.nextLine();
    	}
    	writer.close(); 
    	reader.close();
    	inputFile.delete(); // deleting actual file and renaming temp file to actual file
    	tempFile.renameTo(inputFile);
    	return Base64.getDecoder().decode(currentLine.getBytes()); // return signature
    }
    
    public static byte[] stripMeaningful(byte[] decrypted, String meaningfulText)
    {
    	byte[] meaningfulBytes = meaningfulText.getBytes();	// byte to byte search
    	int j=0;
    	for (int i = 0;i<decrypted.length;i++)
    	{
    		if (decrypted[i] == meaningfulBytes[j])
    		{
    			j++;
    			if (meaningfulText.length() == j)		// if matched with meaningful text
    				return Arrays.copyOfRange(decrypted, 0, i-j+1);	// copy contents to new ByteArray until meaningful text
    		}
    		else
    			j=0;
    	}
    	return null;
    }
    
    public static void execute(String command){		// function for executing keytool commands
        try{
            sun.security.tools.keytool.Main.main(command.trim().split("\\s+"));
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}