import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.spec.*;
import java.security.cert.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Client
{
    private Socket socket;
    private DataInputStream in;
    public DataOutputStream out;
    private listenClient lisClient = null;
    private static boolean isStage2=false;
    private PublicKey kdcPublicKey;
    private PrivateKey alicePrivateKey;
    private int nonceValue;
    public String targetServer;
    public byte[] ticket;
    public String password;

    public static void main(String[] args) throws Exception
    {
		Client client = new Client();
    }

    public Client() throws Exception {
        retrieveKeys();
        Scanner scanner1 = new Scanner(System.in);
        System.out.println("Please enter correct password : "); // entering password
        String passwd = scanner1.next();
        FileWriter fileWriterAlice = new FileWriter(new File("Alice_Log.txt"),true); // aman dikkat, belki burayý içerde tanýmlayabiliriz, closed yaptýðýmýzda ikinci defa server seçiminde patlar

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date timestampDate = new Date(System.currentTimeMillis());
        String timestamp = simpleDateFormat.format(timestampDate);
        System.out.println("Please enter a server or type quit: "); // choosing server
        Scanner scanner = new Scanner(System.in);
        String text="";
        text = scanner.next();
        password = passwd;
        switch (text) {
            case "Mail":
                connect(3000); // connecting to the kdcserver

                out.writeUTF("stage 1");
                out.writeUTF("Alice");
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date timestamp2 = new Date(System.currentTimeMillis());
                String dateString = simpleDateFormat.format(timestamp2);
                fileWriterAlice.write(dateString+" : " +"Alice, " + passwd+ " Mail, "+ timestamp+ "\n"); // 25.12.2020 12:02:00 Alice->KDC : "Alice", [Pass], "Mail", [TS1]
                byte[] asym = encryptAsymmetric("Alice"+","+passwd+","+"Mail"+","+timestamp);
                fileWriterAlice.write(dateString+" : " +"Alice, " + Base64.getEncoder().encodeToString(asym)+ "\n"); // 25.12.2020 12:02:00 Alice->KDC : "Alice", Base64[P_KDC("Alice", Pass, "Mail", TS1)]
                fileWriterAlice.close();
                out.writeInt(asym.length);
                out.write(asym);
                Server mailServer = new Server(3001, "Mail"); // starting the mail server
                break;
            case "Web":
                connect(3000); // connecting to the kdcserver

                out.writeUTF("stage 1");
                out.writeUTF("Alice");
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                timestamp2 = new Date(System.currentTimeMillis());
                dateString = simpleDateFormat.format(timestamp2);
                byte[] asymWeb = encryptAsymmetric("Alice"+","+passwd+","+"Web"+","+timestamp);
                fileWriterAlice.write(dateString+" : " +"Alice, " + passwd+ " Web, "+ timestamp+ "\n"); // 25.12.2020 12:02:00 Alice->KDC : "Alice", [Pass], "Mail", [TS1]
                fileWriterAlice.write(dateString+" : " +"Alice, " + Base64.getEncoder().encodeToString(asymWeb)+ "\n");
                fileWriterAlice.close();
                out.writeInt(asymWeb.length);
                out.write(asymWeb);
                Server webServer = new Server(3002, "Web"); // connecting to the web server
                break;
            case "Database":
                connect(3000); // connecting to the kdcserver

                out.writeUTF("stage 1");
                out.writeUTF("Alice");
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                timestamp2 = new Date(System.currentTimeMillis());
                dateString = simpleDateFormat.format(timestamp2);
                byte[] asymDatabase = encryptAsymmetric("Alice"+","+passwd+","+"Database"+","+timestamp);
                fileWriterAlice.write(dateString+" : " +"Alice, " + passwd+ " Database, "+ timestamp+ "\n"); // 25.12.2020 12:02:00 Alice->KDC : "Alice", [Pass], "Mail", [TS1]
                fileWriterAlice.write(dateString+" : " +"Alice, " + Base64.getEncoder().encodeToString(asymDatabase)+ "\n"); // base64(Pa(id,pass,selectedserver,timestamp))
                fileWriterAlice.close();
                out.writeInt(asymDatabase.length);
                out.write(asymDatabase);
                Server databaseServer = new Server(3003, "Database"); // connecting to the database server
                break;
        }
    }

    public void retrieveKeys() throws Exception
    {
        File kdcCert = new File("cert/KDC.cer");
        FileInputStream certInput = new FileInputStream(kdcCert);
        CertificateFactory cF = CertificateFactory.getInstance("X509");
        X509Certificate cert = (X509Certificate) cF.generateCertificate(certInput);
        kdcPublicKey = cert.getPublicKey();

        File alicePrivateKeyFile = new File("keys/Alice");
        BufferedReader privateKeyRead = new BufferedReader(new FileReader(alicePrivateKeyFile));
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyRead.readLine().getBytes());
        PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory kF = KeyFactory.getInstance("RSA");
        alicePrivateKey = kF.generatePrivate(priKeySpec);
    }

    public boolean connect(int port)
    {
        String address = "127.0.0.1"; // localhost
        try {
            socket = new Socket(address,port);
            out = new DataOutputStream(socket.getOutputStream());
            lisClient = new listenClient(socket,this); //
        } catch (Exception e) { // sorun þey olabilir mi, biz constructoru bir defa çaðýrýyoruz kdc serverda , o da bailangý.ta, belki þey yapabiliriz, bi kdcservera gelsen se
            System.out.println("Connection failed\n"+e);
            return false;
        }
        lisClient.start();
        return true;
    }

    public byte[] encryptAsymmetric(String message) throws Exception // encryption
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, kdcPublicKey);

        byte[] asym =cipher.doFinal(message.getBytes());

        return asym;
    }

    public String decryptAsymmetric(String message) throws Exception // decryption
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, alicePrivateKey);
        return new String(cipher.doFinal(message.getBytes()));
     }
    public String decryptAsymmetricByte(byte[] input) throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, alicePrivateKey);
        return new String(cipher.doFinal(input));
    }
}

class listenClient extends Thread {
    private Socket socket ;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    private Socket customSocket ;
    private DataInputStream customInput = null;
    private DataOutputStream customOutput = null;
    private Client client;
    private String password;
    private int nonce;
    private SecretKey sessionKey;

    FileWriter fileWriter;


    public void setSocket(int port, String address) throws Exception // changing socket
    {
        if(customSocket == null){
            customSocket = socket;
            customInput = input;
//            System.out.println("****\n\n\n"+socket+"\n\n\n****");
//            System.out.println("****\n\n\n"+input+"\n\n\n****");
        }
        socket = new Socket(address,port);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
    }

    public listenClient(Socket socket,Client client) throws Exception
    {
        this.socket = socket;
        input = new DataInputStream(socket.getInputStream());
        this.client = client;
        fileWriter = new FileWriter(new File("Alice_Log.txt"),true);
    } 

    @Override
    public void run(){
        String line = "";

        byte[] message = null;

        while (!line.equals("Over")) {
            try {

                line = input.readUTF();

                if (line.equals("stage 2")) // if password is correct, in client side enters here
                {

                    int alicelength = input.readInt();
                    byte[] aliceByte = new byte[alicelength];
                    input.readFully(aliceByte,0,alicelength);
                    int ticketlength = input.readInt();
                    byte[] ticketByte = new byte[ticketlength];
                    input.readFully(ticketByte,0,ticketlength);
                    String decrypted = client.decryptAsymmetricByte(aliceByte);
                    String[] parsed = decrypted.split(",");
                    byte[] decodedSessionKey = Base64.getDecoder().decode(parsed[0].getBytes());
                    client.targetServer = parsed[1];
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date timestampDate = new Date(System.currentTimeMillis());
                    String timestamp = simpleDateFormat.format(timestampDate);
                    fileWriter.write(timestamp +" KDC->Alice : Password Verified"+"\n"); // password verified
                    sessionKey = new SecretKeySpec(decodedSessionKey,0,decodedSessionKey.length,"AES");
                    fileWriter.write(timestamp +" KDC->Alice : " + Base64.getEncoder().encodeToString(aliceByte) + "," + Base64.getEncoder().encodeToString(ticketByte)+"\n"); // 25.12.2020 12:03:00 KDC->Alice : Base64[P_A(K_A, "Mail", TS2)], Base64[Ticket]
                    fileWriter.write(timestamp +" Message Decrypted : " + parsed[0] + "," + parsed[1]+","+ parsed[2]+"\n"); // 25.12.2020 12:03:00 KDC->Alice : Base64[P_A(K_A, "Mail", TS2)], Base64[Ticket]
                    client.ticket = ticketByte;
                    String ipaddress = "127.0.0.1";   // localhost
                    switch (client.targetServer)
                    {
                        case "Mail":
                            setSocket(3001,ipaddress);
                            break;
                        case "Web":
                            setSocket(3002,ipaddress);
                            break;
                        case "Database":
                            setSocket(3003,ipaddress);
                            break;
                        default:
                            System.out.println("not entered any socket");
                    }
                    output.writeUTF("stage 3");
                    output.writeUTF("Alice");
                    output.writeInt(ticketByte.length);
                    output.write(ticketByte);
                    String nonceString = nonceGenerator();
                    nonce = Integer.valueOf(nonceString);
                    fileWriter.write(timestamp +" Alice->" + client.targetServer +" : " + "Alice" + "," + nonce+"\n"); // 25.12.2020 12:03:00 KDC->Alice : Base64[P_A(K_A, "Mail", TS2)], Base64[Ticket]
                    byte[] nonceByte = encryptByte((nonceString+',').getBytes());
                    output.writeInt(nonceByte.length);
                    output.write(nonceByte);
                    fileWriter.write(timestamp +" Alice->" + client.targetServer +" : "  + Base64.getEncoder().encodeToString(ticketByte)+ "," + Base64.getEncoder().encodeToString(nonceByte) + "\n"); // 25.12.2020 12:03:00 KDC->Alice : Base64[P_A(K_A, "Mail", TS2)], Base64[Ticket]
                }
                else if (line.equals("stage password not correct")){
                    System.out.println("Your password is not correct. Enter the correct password");

                    Scanner scanner = new Scanner(System.in);
                    output.writeUTF("stage 1"); // test edeceðim tekrardan, bir yeri deðiþtirdim aþapýda
                    System.out.println("coming next : stage 1");
                    output.writeUTF("Alice");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date timestamp = new Date(System.currentTimeMillis());
                    String dateString = simpleDateFormat.format(timestamp);
                    output.write(client.encryptAsymmetric("Alice"+","+password+","+ client.targetServer+"," + dateString ));
                }
                else if(line.equals("stage 4")){ // verify nonce value, ok
                    int resultLength = input.readInt();
                    byte[] resultArray = new byte[resultLength];
                    input.readFully(resultArray,0,resultLength);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date timestamp = new Date(System.currentTimeMillis());
                    String dateString = simpleDateFormat.format(timestamp);
                    fileWriter.write(dateString  + client.targetServer +"-> Alice" +" : "  + Base64.getEncoder().encodeToString(resultArray) + "\n"); // 25.12.2020 12:03:00 KDC->Alice : Base64[P_A(K_A, "Mail", TS2)], Base64[Ticket]
                    String decrypted = decryptByteAES(resultArray);
                    String[] parsed = decrypted.split(",");
                    if(nonce == Integer.valueOf(parsed[0]) -1){
                        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        timestamp = new Date(System.currentTimeMillis());
                        dateString = simpleDateFormat.format(timestamp);
                        fileWriter.write(dateString  +"Message Decrypted : "  + nonce +"is OK, N2 =" + parsed[1] + "\n");
                        fileWriter.write(dateString  +" Alice->" + client.targetServer + " : " + (Integer.valueOf(parsed[1])+1) + "\n");// 25.12.2020 12:03:00 KDC->Alice : Base64[P_A(K_A, "Mail", TS2)], Base64[Ticket]

                        output.writeUTF("stage 5");
                        byte[] result = encryptByte(String.valueOf(Integer.valueOf(parsed[1])+1).getBytes());
                        fileWriter.write(dateString  +" Alice->" + client.targetServer + " :" + Base64.getEncoder().encodeToString(result) + "\n");
                        output.writeInt(result.length);
                        output.write(result);
                        fileWriter.close();
                    }
                }
                else if(line.equals("stage restart")){
                    socket = customSocket;
                    input = new DataInputStream(customSocket.getInputStream());
                    output = client.out;


                    output.writeUTF("stage 1");
                    output.writeUTF("Alice");

                    String ipaddress = "127.0.0.1";   // localhost

                    //System.out.println(i)
                    Scanner scanner = new Scanner(System.in);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date timestamp2 = new Date(System.currentTimeMillis());
                    String dateString = simpleDateFormat.format(timestamp2);
                    FileWriter fileWriterAlice = new FileWriter(new File("Alice_Log.txt"),true);
                    String targetServerString = scanner.next();
                    client.targetServer = targetServerString;
                    fileWriterAlice.write(dateString+" : " +"Alice, " + client.password+" "+targetServerString+" "+ dateString+ "\n");
                    byte[] asym = client.encryptAsymmetric("Alice"+","+ client.password+","+targetServerString+","+dateString);
                    fileWriterAlice.write(dateString+" : " +"Alice, " + Base64.getEncoder().encodeToString(asym)+ "\n");
                    output.writeInt(asym.length);
                    fileWriterAlice.close();
                    output.write(asym);
                }


            } catch (Exception e) {
                System.out.println("listen\n" + e);
                e.printStackTrace();
                break;
            }
        }

//        while (true){
//            try{
//                line = input.readUTF();
//                if(line.equals("stage 4")){
//                    int resultLength = input.readInt();
//                    byte[] resultArray = new byte[resultLength];
//                    input.readFully(resultArray,0,resultLength);
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//                    Date timestamp = new Date(System.currentTimeMillis());
//                    String dateString = simpleDateFormat.format(timestamp);
//                    fileWriter.write(dateString  + client.targetServer +"-> Alice" +" : "  + Base64.getEncoder().encodeToString(resultArray) + "\n"); // 25.12.2020 12:03:00 KDC->Alice : Base64[P_A(K_A, "Mail", TS2)], Base64[Ticket]
//                    String decrypted = decryptByteAES(resultArray);
//                    String[] parsed = decrypted.split(",");
//                    if(nonce == Integer.valueOf(parsed[0]) -1){
//                        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//                        timestamp = new Date(System.currentTimeMillis());
//                        dateString = simpleDateFormat.format(timestamp);
//                        fileWriter.write(dateString  +"Message Decrypted : "  + nonce +"is OK, N2 =" + parsed[1] + "\n");
//                        fileWriter.write(dateString  +" Alice->" + client.targetServer + " : " + (Integer.valueOf(parsed[1])+1) + "\n");// 25.12.2020 12:03:00 KDC->Alice : Base64[P_A(K_A, "Mail", TS2)], Base64[Ticket]
//
//                        output.writeUTF("stage 5");
//                        byte[] result = encryptByte(String.valueOf(Integer.valueOf(parsed[1])+1).getBytes());
//                        fileWriter.write(dateString  +" Alice->" + client.targetServer + " :" + Base64.getEncoder().encodeToString(result) + "\n");
//                        output.writeInt(result.length);
//                        output.write(result);
//                        fileWriter.close();
//                    }
//                }
//                else if(line.equals("stage restart")){
//                    socket = reservedKDCSocket;
//                    input = reservedKDCInputStream;
//                    output = new DataOutputStream(reservedKDCSocket.getOutputStream());
//
//                    output.writeUTF("stage 1");
//                    output.writeUTF("Alice");
//
//                    String ipaddress = "127.0.0.1";   // localhost
//                    System.out.println("Please enter a server or type quit: ");
//                    //System.out.println(i)
//                    Scanner scanner = new Scanner(System.in);
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//                    Date timestamp2 = new Date(System.currentTimeMillis());
//                    String dateString = simpleDateFormat.format(timestamp2);
//                    FileWriter fileWriterAlice = new FileWriter(new File("Alice_Log.txt"),true);
//                    String targetServerString = scanner.next();
//                    client.targetServer = targetServerString;
//                    fileWriterAlice.write(dateString+" : " +"Alice, " + client.password+" "+targetServerString+" "+ dateString+ "\n");
//                    byte[] asym = client.encryptAsymmetric("Alice"+","+ client.password+","+targetServerString+","+dateString);
//                    fileWriterAlice.write(dateString+" : " +"Alice, " + Base64.getEncoder().encodeToString(asym)+ "\n");
//                    output.writeInt(asym.length);
//                    fileWriterAlice.close();
//                    output.write(asym);
//                    Server targetServer;
//                    switch (targetServerString)
//                    {
//                        case "Mail":
//                            targetServer = new Server(3001, "Mail");
//                            break;
//                        case "Web":
//                            targetServer= new Server(3002, "Web");
//                            break;
//                        case "Database":
//                            targetServer = new Server(3003, "Database");
//                            break;
//                        default:
//                            System.out.println("not entered any socket");
//                    }
//                }
//            }
//            catch (Exception e){
//                System.out.println("error" +e);
//                e.printStackTrace();
//                break;
//            }
//        }
    }

    public String nonceGenerator()
    {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++)
            stringBuilder.append(secureRandom.nextInt(10));
        String randomNumber = stringBuilder.toString();
        return randomNumber;
    }

    public String encrypt(String message) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, sessionKey,new IvParameterSpec(new byte[16]));
        return new String(cipher.doFinal(message.getBytes()));
    }
    public byte[] encryptByte(byte[] input) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, sessionKey,new IvParameterSpec(new byte[16]));
        return cipher.doFinal(input);
    }
    public String decrypt(String message) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, sessionKey,new IvParameterSpec(new byte[16]));
        return new String(cipher.doFinal(message.getBytes()));
    }
    public String decryptByteAES(byte[] bytes) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, sessionKey,new IvParameterSpec(new byte[16]));
        return new String(cipher.doFinal(bytes));
    }
    public String decryptByte(byte[] input) throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, sessionKey);
        return new String(cipher.doFinal(input));
    }
}