import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class KDCServer
{
    private int portNum;
    private PrivateKey kdcPrivateKey;
    public PublicKey mailPubKey;
    public PublicKey webPubKey;
    public PublicKey databasePubKey;
    public PublicKey alicePubKey;
    private boolean passed;
    private Socket socket = null;
    private ServerSocket server = null;
    FileWriter kdcLog;

    public KDCServer(int portNum) throws Exception
    {
        this.portNum = portNum;
        createCertAndKey();             // random keys and certificate
        getPublicKeys();                // get keys from certificates
        try {
            this.portNum = portNum;
            server = new ServerSocket(portNum);     // create socket connection with alice
            socket = server.accept();
            Listen2 client = new Listen2(socket, this);
            client.start();				                    // starting client
        } catch (Exception e) {
            System.out.println("server cons\n"+e);
        }
    }

    public static void main(String[] args) throws Exception
    {
        File directory = new File("cert");
        if (! directory.exists()){
                directory.mkdir();}
        directory = new File("keys");
        if (! directory.exists()){
                directory.mkdir();}
    	KDCServer kdcServer = new KDCServer(3000);
    }

    public static void execute(String command){ // function for executing keytool commands
        try{
            sun.security.tools.keytool.Main.main(command.trim().split("\\s+"));
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static String getRandomPassword(int length)  // random password generation
	{
		char[] password = new char[length];
		String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	String lower = upper.toLowerCase();
    	String digits = "0123456789";
		char[] symbols = (upper + lower + digits).toCharArray();
        Random random = new Random();
		for (int i = 0; i < length; i++)
            password[i] = symbols[random.nextInt(symbols.length)];
        return new String(password);
	}

    public void createCertAndKey() throws Exception
    {
        ArrayList<String > list = new ArrayList<>();       // a list for automatic key generation
        list.add("KDC");
        list.add("Mail");
        list.add("Web");
        list.add("Database");
        list.add("Alice");
        FileWriter fileWriter = new FileWriter(new File("passwd"));
        kdcLog = new FileWriter(new File("KDC_Log.txt"),true);
        String password = getRandomPassword(20);
        byte[] bytes = generateHashSHA1(password.getBytes()).getBytes();
        String pass = new String(Base64.getEncoder().encode(bytes));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date timestamp = new Date(System.currentTimeMillis());
        String dateString = simpleDateFormat.format(timestamp);
        kdcLog.write(dateString+" : "+password+"\n");
        kdcLog.close();
        fileWriter.write(pass);
        fileWriter.close();

        for(String keyPair : list)
        {
            String command1 = "-genkey -alias "+keyPair+" -keyalg RSA -keysize 2048 -validity 46 -storepass password -keypass password -dname cn=a";
            // we modified keytool command for fast processing, you can change commands
            String command2 = "-exportcert -rfc -alias "+keyPair+" -file cert/"+keyPair+".cer -storepass password -keypass password";
            execute(command1);
            execute(command2);

            PrivateKey key = null;
            try {
                File file = new File(System.getProperty("user.home") + File.separatorChar + ".keystore"); // opening keystore file which is created by keytool
                FileInputStream inputStream = new FileInputStream(file);
                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                keystore.load(inputStream, "password".toCharArray());
                key = (PrivateKey)keystore.getKey(keyPair, "password".toCharArray()); // fetching our private key from keystore
                byte[] encodedPrivateKey = key.getEncoded();    // PKCS8 encoded

                if(kdcPrivateKey == null)
                    kdcPrivateKey = key;
                File output = new File("keys/"+keyPair);
                PrintStream out = new PrintStream(output);
                byte[] encoded = Base64.getEncoder().encode(encodedPrivateKey);
                out.print(new String(encoded)); // saving Base64 encoded private key into a file
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void getPublicKeys() throws Exception // getting public keys from the certificates
    {
        File certFile = new File("cert/Mail.cer");
        FileInputStream certInput = new FileInputStream(certFile);
        CertificateFactory cF = CertificateFactory.getInstance("X509");
        X509Certificate cert = (X509Certificate) cF.generateCertificate(certInput);
        mailPubKey = cert.getPublicKey();

        certFile = new File("cert/Web.cer");
        certInput = new FileInputStream(certFile);
        cert = (X509Certificate) cF.generateCertificate(certInput);
        webPubKey = cert.getPublicKey();

        certFile = new File("cert/Database.cer");
        certInput = new FileInputStream(certFile);
        cert = (X509Certificate) cF.generateCertificate(certInput);
        databasePubKey = cert.getPublicKey();

        certFile = new File("cert/Alice.cer");
        certInput = new FileInputStream(certFile);
        cert = (X509Certificate) cF.generateCertificate(certInput);
        alicePubKey = cert.getPublicKey();
    }
    
    public static String generateHashSHA1( byte[] input) throws NoSuchAlgorithmException, IOException // to find hash of a file
    {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(input,0,input.length);
        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        return sb.toString();
    }

    public String encrypt(String message,String selectedTarget) throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        PublicKey selectedKey = null;
        switch (selectedTarget){
            case "Alice":
                selectedKey = alicePubKey;
                break;
            case "Web":
                selectedKey = webPubKey;
                break;
            case "Mail":
                selectedKey = mailPubKey;
                break;
            case "Database":
                selectedKey = databasePubKey;
                break;
        }
        cipher.init(Cipher.ENCRYPT_MODE, selectedKey );
        return new String(cipher.doFinal(message.getBytes()));
    }
    public byte[] encryptByte(String message,String selectedTarget) throws Exception // encryption
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        PublicKey selectedKey = null;
        switch (selectedTarget){
            case "Alice":
                selectedKey = alicePubKey;
                break;
            case "Web":
                selectedKey = webPubKey;
                break;
            case "Mail":
                selectedKey = mailPubKey;
                break;
            case "Database":
                selectedKey = databasePubKey;
                break;
        }
        cipher.init(Cipher.ENCRYPT_MODE, selectedKey );
        byte[] doFinal = cipher.doFinal(message.getBytes());
        return doFinal;
    }

    public String decrypt(String message) throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE,kdcPrivateKey);
        byte[] asm = message.getBytes();
        return new String(cipher.doFinal(asm));
    }
    public String decrypt(byte[] input) throws Exception // decryption
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE,kdcPrivateKey);
        byte[] result = cipher.doFinal(input);
        return new String(result);
    }
}

class Listen2 extends Thread
{
    DataInputStream in = null;
    public DataOutputStream out = null;
    Socket socket = null;
    KDCServer server = null;
    PrintStream defOut;
    PrintStream fileOut;
    String ticket;
    FileWriter kdcLog;
    int nonce;

    public Listen2(Socket socket, KDCServer server) throws FileNotFoundException,IOException
    {
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.socket = socket;
        this.server = server;
        this.defOut = System.out;
    }

    @Override
    public void run() {
        String line = "";
        while (true) {      // listens everytime
            try {
                line = in.readUTF();
                if (line.equals("Over"))        // if client disconnects, then break (and close),
                {
                    out.writeUTF("Over");
                    out.close();
                    in.close();
                    socket.close();
                    break;
                }
                if(line.equals("stage 1"))      // alice to kdc stage 1
                {
                    String id = in.readUTF();
                    int length = in.readInt();
                    byte[] byteArray = new byte[length];
                    in.readFully(byteArray,0,length);
                    kdcLog = new FileWriter(new File("KDC_Log.txt"),true);
                    String decrypted = server.decrypt(byteArray);
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date timestamp2 = new Date(System.currentTimeMillis());
                    String dateString2 = simpleDateFormat2.format(timestamp2);
                    String[] parsed = decrypted.split(",");
                    kdcLog.write(dateString2+ " KDC -> Alice"+" : "  +"Alice, " +Base64.getEncoder().encodeToString(byteArray) +"\n");
                    kdcLog.write(dateString2+ " Message Decrypted "+" : "  + parsed[0] + "," + parsed[1] + "," + parsed[2] + "," +parsed[3] +"\n");
                    String password =parsed[1];
                    String hashOfPassword = server.generateHashSHA1(password.getBytes());
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("passwd")));
                    String passwd = new String(Base64.getDecoder().decode(bufferedReader.readLine()));
                    bufferedReader.close();
                    if(!hashOfPassword.equals(passwd)){ // if password is not correct
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date timestamp = new Date(System.currentTimeMillis());
                        String dateString = simpleDateFormat.format(timestamp);
                        kdcLog.write(dateString + " KDC -> Alice"+" : Password Denied"+"\n");
                        out.writeUTF("stage restart");
                    }
                    else{ // if password is correct
                        String target = parsed[2];
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date timestamp = new Date(System.currentTimeMillis());
                        String dateString = dateFormat.format(timestamp);
                        kdcLog.write(dateString + " "+"KDC -> Alice"+" : Password Verified"+"\n");
                        SecretKey sessionKey = KeyGenerator.getInstance("AES").generateKey();
                        String encodedSessionKey = Base64.getEncoder().encodeToString(sessionKey.getEncoded()); // Input length not multiple of 16 bytes
                        out.writeUTF("stage 2");
                        kdcLog.write(dateString+ " KDC -> Alice"+" : " +encodedSessionKey +"," +target+"," + dateString+"\n");
                        byte[] encryptedWithAliceKey = server.encryptByte((encodedSessionKey+","+target+","+dateString),"Alice");
                        out.writeInt(encryptedWithAliceKey.length);
                        out.write(encryptedWithAliceKey);
                        byte[] ticket = server.encryptByte(("Alice"+","+target+","+dateString +"," + encodedSessionKey),target);
                        out.writeInt(ticket.length);
                        out.write(ticket);
                        kdcLog.write(dateString+ " KDC -> Alice"+" : " +Base64.getEncoder().encodeToString(encryptedWithAliceKey)+"," + Base64.getEncoder().encodeToString(ticket)+"\n"); // 25.12.2020 12:02:58 KDC->Alice : Base64[P_A(K_A, "Mail", TS2)], Base64[Ticket]
                        kdcLog.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

}
