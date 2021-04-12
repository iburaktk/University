import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Server
{
    private SecretKey secretKeyAES;
    private SecretKey secretKeyDES;
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    private IvParameterSpec ivParameterSpecForAESOFB = null;
    private IvParameterSpec ivParameterSpecForAESCBC = null;
    private IvParameterSpec ivParameterSpecForDESCBC = null;
    private IvParameterSpec ivParameterSpecForDESOFB = null;
    private Set<Listen> threads = null;
    private PrintStream fileOut = null;

    public Server(int port) throws NoSuchAlgorithmException, NoSuchPaddingException {
        CryptoClass cryptoClass = new CryptoClass();
        HashMap<String,SecretKey> keys = cryptoClass.generateKey(); // generating secret keys
        secretKeyAES = keys.get("AES");
        secretKeyDES = keys.get("DES");

        String encodedKeyDES = Base64.getEncoder().encodeToString(secretKeyDES.getEncoded()); // encode keys in order to pass the keys to the client
        String encodedKeyAES =  Base64.getEncoder().encodeToString(secretKeyAES.getEncoded());
        ivParameterSpecForAESOFB = cryptoClass.generateIVector("AES","OFB"); // initialization vectors
        ivParameterSpecForAESCBC = cryptoClass.generateIVector("AES","CBC");
        ivParameterSpecForDESOFB = cryptoClass.generateIVector("DES","OFB");
        ivParameterSpecForDESCBC = cryptoClass.generateIVector("DES","CBC");
                
        threads = new HashSet<>();
        try {
        	fileOut = new PrintStream(new FileOutputStream("log.txt",true)); // log file
            ConsoleAndFileWrite.write(fileOut,System.out,"AES key: "+encodedKeyAES); // writing file and console at the same time
            ConsoleAndFileWrite.write(fileOut,System.out,"DES key: "+encodedKeyDES); // writing file and console at the same time
            ConsoleAndFileWrite.write(fileOut,System.out,"IV for AES CBC: "+Base64.getEncoder().encodeToString(ivParameterSpecForAESCBC.getIV())); // writing file and console at the same time
            ConsoleAndFileWrite.write(fileOut,System.out,"IV for AES OFB: "+Base64.getEncoder().encodeToString(ivParameterSpecForAESOFB.getIV())); // writing file and console at the same time
            ConsoleAndFileWrite.write(fileOut,System.out,"IV for DES CBC: "+Base64.getEncoder().encodeToString(ivParameterSpecForDESCBC.getIV())); // writing file and console at the same time
            ConsoleAndFileWrite.write(fileOut,System.out,"IV for DES OFB: "+Base64.getEncoder().encodeToString(ivParameterSpecForDESOFB.getIV())); // writing file and console at the same time
            server = new ServerSocket(port);
            while(true) 					// keeping server alive
            {
                socket = server.accept();
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());

                Listen client = new Listen(input, output, socket, this);
                // sending keys and init vectors
                output.writeUTF(encodedKeyDES);
                output.writeUTF(encodedKeyAES);
                output.writeInt(ivParameterSpecForAESCBC.getIV().length);
                output.write(ivParameterSpecForAESCBC.getIV());
                output.writeInt(ivParameterSpecForAESOFB.getIV().length);
                output.write(ivParameterSpecForAESOFB.getIV());
                output.writeInt(ivParameterSpecForDESCBC.getIV().length);
                output.write(ivParameterSpecForDESCBC.getIV());
                output.writeInt(ivParameterSpecForDESOFB.getIV().length);
                output.write(ivParameterSpecForDESOFB.getIV());
                threads.add(client);		// adding client to the threads
                client.start();				// starting client
            }
        } catch (Exception e) {
            System.out.println("server cons\n"+e);
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException {

        Server server = new Server(5000);
    }

    public void broadcast(String message, Listen senderClient)
    {
        for (Listen client : threads) 		// sending messages to all clients
            try {
                client.out.writeUTF(message);
            } catch (Exception e) {
                System.out.println("broadcast\n"+e);
            }
    }
    
    public void destroy(Listen thread)
    {
    	threads.remove(thread);				// removing client and its thread.
    	thread.stop();
    }
}

class Listen extends Thread
{
    DataInputStream in = null;
    public DataOutputStream out = null;
    Socket socket = null;
    Server server = null;
    PrintStream defOut;
    PrintStream fileOut;

    public Listen(DataInputStream in, DataOutputStream out, Socket socket, Server server) throws FileNotFoundException {
        this.in = in;
        this.out = out;
        this.socket = socket;
        this.server = server;
        this.defOut = System.out;
        this.fileOut = new PrintStream(new FileOutputStream("log.txt",true)); // log file
    }

    @Override
    public void run()
    {
        String line = "";
        while (true)
        {
            try {
                line = in.readUTF();			// reads input
                if (line.equals("Over"))		// if client disconnects, then break (and close)
                {
                    out.writeUTF("Over");
                    out.close();
                    in.close();
                    socket.close();
                    server.destroy(this);
                    break;
                }
                ConsoleAndFileWrite.write(fileOut,defOut,line.split("\n")[1].split("\n")[0]); // writing file and console at the same time
                server.broadcast(line, this);	// sending the line to the other clients
            } catch (Exception e) {
                System.out.println("server listen\n"+e);
            }
        }
    }
}