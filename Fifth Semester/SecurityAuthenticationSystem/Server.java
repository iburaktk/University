import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

public class Server {
    private SecretKey sessionKey;
    private PrivateKey privKey;
    private PublicKey pubKey;
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    private int portNum;
    public String nameOfServer;   
    
    public Server(int portNum, String nameOfServer){
        try {
            this.nameOfServer = nameOfServer;
            File privKeyFile = new File("keys/"+nameOfServer);
            BufferedReader privateKeyRead = new BufferedReader(new FileReader(privKeyFile)); // fetching private key
            String line = privateKeyRead.readLine();
            byte[] privateKeyBytes = Base64.getDecoder().decode(line.getBytes());
            PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory kF = KeyFactory.getInstance("RSA");
            privKey = kF.generatePrivate(priKeySpec);
            this.portNum = portNum;
            server = new ServerSocket(portNum);     // creating socket connection
            socket = server.accept();
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            Listen client = new Listen(input, output, socket, this,sessionKey);
            client.start();				// starting client
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int verifyTicketGetNonce(byte[] ticket, byte[] encryptedNonce) throws Exception // çalýþmýyor :/ loglara baktýn mý
    {
        String decryptedTicket = decryptTicket(ticket);
        String[] parsed = decryptedTicket.split(",");
        byte[] decodedSessionKey = Base64.getDecoder().decode(parsed[3].getBytes());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date timestamp = new Date(System.currentTimeMillis());
        String dateString = simpleDateFormat.format(timestamp);
        printToTheLogFile(dateString +" Ticket Decrypted : " + parsed[0] + ","+ parsed[1] + "," + parsed[2]+","+ parsed[3]);
        sessionKey = new SecretKeySpec(decodedSessionKey,0,decodedSessionKey.length,"AES");
        String decryptedNonceWithPadding = decryptByte(encryptedNonce);
        String decryptedNonce = decryptedNonceWithPadding.split(",")[0];
        printToTheLogFile(dateString +" Message Decrypted : N1 = "+ decryptedNonce);
        return Integer.valueOf(decryptedNonce);
    }
    
    public String encrypt(String message) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, sessionKey);
        return new String(cipher.doFinal(message.getBytes()));

    }
    public byte[] encryptByte(String message) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, sessionKey,new IvParameterSpec(new byte[16]));
        return cipher.doFinal(message.getBytes());

    }

    public String decrypt(String message) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, sessionKey);
        return new String(cipher.doFinal(message.getBytes()));
    }
    public String decryptByte(byte[] bytes) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, sessionKey,new IvParameterSpec(new byte[16]));
        return new String(cipher.doFinal(bytes));
    }

    public String decryptTicket(byte[] ticket) throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        return new String(cipher.doFinal(ticket));
    }

    public void printToTheLogFile(String text) throws IOException
    {
        FileWriter fileWriter = new FileWriter(new File(nameOfServer+"_Log.txt"),true);
        fileWriter.write(text+"\n");
        fileWriter.close();
    }
}

class Listen extends Thread {
    DataInputStream in = null;
    public DataOutputStream out = null;
    Socket socket = null;
    Server server = null;
    PrintStream defOut;
    PrintStream fileOut;
    SecretKey sessionKey;
    String ticket;
    FileWriter fileWriter;

    int nonce;

    public Listen(DataInputStream in, DataOutputStream out, Socket socket, Server server,SecretKey sessionKey) throws FileNotFoundException
    {
        this.in = in;
        this.out = out;
        this.socket = socket;
        this.server = server;
        this.defOut = System.out;
        this.sessionKey = sessionKey;
        this.fileOut = new PrintStream(new FileOutputStream(server.nameOfServer+"_Log.txt", true)); // log file
    }

    @Override
    public void run() {
        String line = "";
        while (true) {
            try {
                line = in.readUTF();
                if (line.equals("Over"))        // if client disconnects, then break (and close)
                {
                    out.writeUTF("Over");
                    out.close();
                    in.close();
                    socket.close();
                    break;
                }
                switch (line)
                {
                    case "stage 3":
                        String id = in.readUTF();
                        int ticketLength = in.readInt();
                        byte[] ticket = new byte[ticketLength];
                        in.readFully(ticket,0,ticketLength);
                        int nonceLength = in.readInt();
                        byte[] nonceArray = new byte[nonceLength];
                        in.readFully(nonceArray,0,nonceLength);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date timestamp = new Date(System.currentTimeMillis());
                        String dateString = simpleDateFormat.format(timestamp);
                        nonce = server.verifyTicketGetNonce(ticket,nonceArray);
                        int oldNonce = nonce;
                        nonce = Integer.valueOf(nonceGenerator());
                        out.writeUTF("stage 4");
                        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        timestamp = new Date(System.currentTimeMillis());
                        dateString = simpleDateFormat.format(timestamp);
                        fileOut.print(dateString +" "+server.nameOfServer+" -> "+id  +": " + String.valueOf(oldNonce+1)+","+String.valueOf(nonce) +"\n");
                        byte[] result = server.encryptByte(String.valueOf(oldNonce+1)+","+String.valueOf(nonce));
                        fileOut.print(dateString +" "+server.nameOfServer+" -> "+id  +": " +Base64.getEncoder().encodeToString(result)+"\n");
                        out.writeInt(result.length);
                        out.write(result);
                        break;
                    case "stage 5":
                        int result2 = in.readInt();
                        byte[] result2Array = new byte[result2];
                        in.readFully(result2Array,0,result2);
                        int decryptednonce = Integer.valueOf(server.decryptByte(result2Array));
                        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        timestamp = new Date(System.currentTimeMillis());
                        dateString = simpleDateFormat.format(timestamp);
                        fileOut.print(dateString+ " Message Decrypted "  +": "  +decryptednonce +"\n");
                        if(decryptednonce == nonce + 1){
                            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            timestamp = new Date(System.currentTimeMillis());
                            dateString = simpleDateFormat.format(timestamp);
                            fileOut.print(dateString +" "+server.nameOfServer+" -> "+" Alice "  +": Authencitation is completed!"+"\n" );
                            fileOut.close();
                            FileWriter aliceEnd = new FileWriter(new File("Alice_Log.txt"),true);
                            aliceEnd.write(dateString +" "+server.nameOfServer+" -> "+" Alice "  +": Authencitation is completed!"+"\n");
                            aliceEnd.close();
                            out.writeUTF("stage restart");
                        }
                        break;
                }
                if (line.equals("stage 5"))
                    break;
            } catch (Exception e) {
                System.out.println("server listen\n" + e);
                e.printStackTrace();
            }
        }
    }

    public static String nonceGenerator()
    {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++)
            stringBuilder.append(secureRandom.nextInt(10));
        String randomNumber = stringBuilder.toString();
        return randomNumber;
    }
}
