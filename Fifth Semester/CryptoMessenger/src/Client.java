import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Base64;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Client extends Application
{
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    String username = "";
    RadioButton AES;
    RadioButton DES;
    RadioButton CBC;
    RadioButton OFB;
    private listenClient lisClient = null;
    ToggleGroup methods;
    ToggleGroup modes;
    String base64message;

    private TextArea chat;

    public Client() {}
    
    public boolean connect(int port)
    {
        String address = "127.0.0.1";
        try {
            socket = new Socket(address,port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            lisClient = new listenClient(in,chat,methods,modes);
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host\n"+e);
            return false;
        } catch (IOException e) {
            System.out.println("IO Exe\n"+e);
            return false;
        } catch (Exception e) {
            System.out.println("Connection failed\n"+e);
            return false;
        }
        lisClient.start();
        return true;
    }

    public static void main(String[] args)
    {
        Application.launch();
    }

    public void start(Stage stage)
    {
        Stage connectionStage = new Stage();
        Button connectButton = new Button("Connect");
        Button disconnectButton = new Button("Disconnect");
        Label connection = new Label("Not Connected");
        Button encryptButton = new Button("Encrypt");
        encryptButton.setDisable(true);
        Button sendButton = new Button("Send");
        sendButton.setDisable(true);

        VBox usernameBox = new VBox();
        Scene input = new Scene(usernameBox);
        connectionStage.setHeight(200);
        connectionStage.setWidth(400);
        connectionStage.setScene(input);
        connectionStage.setTitle("Input");
        connectionStage.initModality(Modality.WINDOW_MODAL);
        connectionStage.initOwner(stage);


        HBox buttonBox = new HBox();
        Label inputLabel = new Label("Enter user name:");
        TextField inputField = new TextField();
        // ****************************************************************
        Button okButton = new Button("OK");
        okButton.setDefaultButton(true);
        okButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override public void handle(ActionEvent e)
            {
                if (connect(5000))
                {
                	chat.clear();
                    username = inputField.getText();
                    connectionStage.close();
                    disconnectButton.setDisable(false);
                    connectButton.setDisable(true);
                    connection.setText("Connected: " + username);
                    encryptButton.setDisable(false);
                }
                else
                    connectionStage.close();
            }
        });
        // ****************************************************************
        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);
        cancelButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override public void handle(ActionEvent e)
            {
                connectionStage.close();
            }
        });
        buttonBox.getChildren().addAll(okButton,cancelButton);
        buttonBox.setSpacing(15);
        usernameBox.getChildren().addAll(inputLabel,inputField,buttonBox);
        usernameBox.setSpacing(15);
        usernameBox.setStyle("-fx-padding: 10;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;");


        HBox connectionBox = new HBox();
        GridPane methodBox = new GridPane();
        GridPane modeBox = new GridPane();

        disconnectButton.setDisable(true);
        Image connectImage = new Image("connect.png");
        ImageView connect = new ImageView(connectImage);
        connect.setFitHeight(15);
        connect.setPreserveRatio(true);
        connectButton.setGraphic(connect);
        // ****************************************************************
        connectButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override public void handle(ActionEvent e)
            {
                connectionStage.show();
            }
        });
        Image disconnectImage = new Image("stop.png");
        ImageView disconnect = new ImageView(disconnectImage);
        disconnect.setFitHeight(15);
        disconnect.setPreserveRatio(true);
        disconnectButton.setGraphic(disconnect);
        // ****************************************************************
        disconnectButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override public void handle(ActionEvent e)
            {
                connectButton.setDisable(false);
                disconnectButton.setDisable(true);
                encryptButton.setDisable(true);
                connection.setText("Not Connected");
                try {
                    out.writeUTF("Over");
                    lisClient.stop();
                    in.close();
                    out.close();
                    socket.close();
                    chat.clear();
                } catch (Exception e2) {
                    System.out.println("Close\n"+e2);
                }
            }
        });

        AES = new RadioButton("AES");
        DES = new RadioButton("DES");
        CBC = new RadioButton("CBC");
        OFB = new RadioButton("OFB");
        AES.setSelected(true);
        CBC.setSelected(true);
        methods = new ToggleGroup();
        methods.getToggles().addAll(AES,DES);
        modes = new ToggleGroup();
        modes.getToggles().addAll(CBC,OFB);

        connectionBox.getChildren().addAll(connectButton,disconnectButton);
        connectionBox.setSpacing(10);

        Label methodLabel = new Label();
        methodLabel.setGraphic(new Label(" Method "));
        methodLabel.getGraphic().setStyle("-fx-background-color: #f4f4f4;");
        methodLabel.setPadding(new Insets(-25,-20,0,0));
        methodLabel.setPrefWidth(75);
        methodBox.add(methodLabel,0,0);
        methodBox.add(AES, 0, 1);
        methodBox.add(DES, 1, 1);
        methodBox.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: gray;");

        Label modeLabel = new Label();
        modeLabel.setGraphic(new Label(" Mode "));
        modeLabel.getGraphic().setStyle("-fx-background-color: #f4f4f4;");
        modeLabel.setPadding(new Insets(-25,-20,0,0));
        modeLabel.setPrefWidth(75);
        modeBox.add(modeLabel,0,0);
        modeBox.add(CBC, 0, 1);
        modeBox.add(OFB, 1, 1);
        modeBox.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: gray;");

        HBox topBox = new HBox();
        topBox.getChildren().addAll(connectionBox,methodBox,modeBox);
        topBox.setSpacing(20);

        chat = new TextArea();
        chat.setText("Firstly you must create server with Server.java"
        		 + "\nSecondly connect to server with connect button."
        		 + "\nThen select encryption modes, text your message, encrypt and send it."
        		 + "\nFinally disconnect from server with Disconnect button and close this program."
        		 + "\nServer will not be closed, will wait for new connections."
        		 + "\nYou must stop Server by interrupt key (CTRL + C).");
        chat.setEditable(false);
        chat.setPrefColumnCount(30);
        chat.setPrefRowCount(30);

        GridPane textGridPane = new GridPane();
        Label textLabel = new Label();
        textLabel.setGraphic(new Label(" Text "));
        textLabel.getGraphic().setStyle("-fx-background-color: #f4f4f4;");
        textLabel.setPadding(new Insets(-18,-20,0,0));
        textLabel.setPrefWidth(50);
        TextArea text = new TextArea();
        text.setPrefColumnCount(13);
        text.setPrefRowCount(4);
        textGridPane.add(textLabel, 0, 0);
        textGridPane.add(text, 0, 1);
        textGridPane.setStyle("-fx-padding: 5;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: gray;");

        GridPane cryptedTextGridPane = new GridPane();
        Label cryptedTextLabel = new Label();
        cryptedTextLabel.setGraphic(new Label(" Crypted Text "));
        cryptedTextLabel.getGraphic().setStyle("-fx-background-color: #f4f4f4;");
        cryptedTextLabel.setPadding(new Insets(-18,-20,0,0));
        cryptedTextLabel.setPrefWidth(100);
        TextArea cryptedText = new TextArea();

        cryptedText.setEditable(false);
        cryptedText.setPrefColumnCount(13);
        cryptedText.setPrefRowCount(4);
        cryptedTextGridPane.add(cryptedTextLabel, 0, 0);
        cryptedTextGridPane.add(cryptedText, 0, 1);
        cryptedTextGridPane.setStyle("-fx-padding: 5;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: gray;");

        // ****************************************************************
        encryptButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override public void handle(ActionEvent e)
            {
                try {
                    cryptedText.setText(encrypt(text.getText()).split("> ")[1].split("\n")[0]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                sendButton.setDisable(false);
            }
        });

        Image sendImage = new Image("send.png");
        ImageView send = new ImageView(sendImage);
        send.setFitHeight(20);
        send.setPreserveRatio(true);
        sendButton.setGraphic(send);
        // ****************************************************************
        sendButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override public void handle(ActionEvent e)
            {
                text.setText("");
                sendMessage(base64message);
                sendButton.setDisable(true);
            }
        });

        HBox bottomBox = new HBox();
        bottomBox.getChildren().addAll(textGridPane,cryptedTextGridPane,encryptButton,sendButton);
        bottomBox.setSpacing(2);

        VBox root = new VBox();
        root.getChildren().addAll(topBox,chat,bottomBox,connection);
        root.setSpacing(10);
        root.setStyle("-fx-padding: 10;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;");

        Scene messenger = new Scene(root);
        stage.setHeight(900);
        stage.setWidth(700);
        stage.centerOnScreen();
        stage.setScene(messenger);
        stage.setTitle("Crypto Messenger");
        stage.show();
    }

    public String encrypt(String input) throws Exception {

        String input2;
        input2 = CryptoClass.funDecryptorEncrypt(input,lisClient.selectIVParameterSpec(((RadioButton)methods.getSelectedToggle()).getText(),((RadioButton)modes.getSelectedToggle()).getText()),lisClient.selectKey(((RadioButton)methods.getSelectedToggle()).getText()),((RadioButton)methods.getSelectedToggle()).getText(),((RadioButton)modes.getSelectedToggle()).getText(),"ENC",username); // encyrption islemleri
        String temp3 = input2+"\n"+username+"> "+input2+"\n";
        base64message = temp3;
        return temp3;
    }

    public void sendMessage(String message)
    {
        try {
            out.writeUTF(message);
        } catch (Exception e) {
            System.out.println("send\n"+e);
        }
        return;
    }
}

class listenClient extends Thread
{
    private DataInputStream input = null;
    ToggleGroup methods;
    ToggleGroup modes;
    TextArea chat = null;
    String aesEncrypted = null;
    String desEncrypted = null;
    SecretKey aesKey = null;
    String ivAESCBC = null;
    String ivDESCBC = null;
    String ivAESOFB = null;
    String ivDESOFB = null;

    public IvParameterSpec getIvParameterSpecAESCBC() {
        return ivParameterSpecAESCBC;
    }
    public IvParameterSpec getIvParameterSpecAESOFB() {
        return ivParameterSpecAESOFB;
    }
    public IvParameterSpec getIvParameterSpecDESCBC() {
        return ivParameterSpecDESCBC;
    }
    public IvParameterSpec getIvParameterSpecDESOFB() {
        return ivParameterSpecDESOFB;
    }

    IvParameterSpec ivParameterSpecAESCBC = null;
    IvParameterSpec ivParameterSpecDESCBC = null;
    IvParameterSpec ivParameterSpecAESOFB = null;
    IvParameterSpec ivParameterSpecDESOFB = null;

    public SecretKey getAesKey() {
        return aesKey;
    }
    public SecretKey getDesKey(){
        return desKey;
    }
    public SecretKey selectKey(String text){
        switch (text){
            case "AES":
                return getAesKey();
            case "DES":
                return getDesKey();
        }
        return null;
    }
    public IvParameterSpec selectIVParameterSpec(String text,String text2)
    {
        switch (text+text2){
            case "AESCBC":
                return getIvParameterSpecAESCBC();
            case "AESOFB":
                return getIvParameterSpecAESOFB();
            case "DESCBC":
                return getIvParameterSpecDESCBC();
            case "DESOFB":
                return getIvParameterSpecDESOFB();
        }
        return null;
    }

    SecretKey desKey = null;

    public listenClient(DataInputStream in, TextArea chat,ToggleGroup methods,ToggleGroup modes)
    {
        input = in;
        this.chat = chat;
        this.methods = methods;
        this.modes = modes;
    }

    @Override
    public void run()
    {
        String line = "";
        byte[] message = null;
        while (!line.equals("Over"))
        {
            try {
                if(aesEncrypted == null){
                line = input.readUTF();	}
                else if(ivDESOFB == null){
                    int length = input.readInt();
                    if(length>0) {
                       message = new byte[length];
                        input.readFully(message, 0, message.length);
                    }
                }
                else{
                    line = input.readUTF();
                    String first = line.split("> ")[0] +"> ";
                    String second = line.split("> ")[1].split("\n")[0];

                    second = CryptoClass.funDecryptorEncrypt(second,selectIVParameterSpec(((RadioButton)methods.getSelectedToggle()).getText(),((RadioButton)modes.getSelectedToggle()).getText()),selectKey(((RadioButton)methods.getSelectedToggle()).getText()),((RadioButton)methods.getSelectedToggle()).getText(),((RadioButton)modes.getSelectedToggle()).getText(),"DEC",first);
                    line = first +" "+ second + "\n";// decrypt burada olacak
                }
                if (line.equals("Over"))
                {
                    break;
                }
                if(desEncrypted == null){
                    desEncrypted = line;

                    byte[] encodedKey     = Base64.getDecoder().decode(desEncrypted);
                    SecretKey originalKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "DES");
                    desKey = originalKey;

                }
                else if( aesEncrypted == null){
                    aesEncrypted = line;
                    byte[] encodedKey     = Base64.getDecoder().decode(aesEncrypted);
                    SecretKey originalKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
                    aesKey = originalKey;

                }
                else if( ivAESCBC == null){ 
                    ivAESCBC = new String(message);
                    byte[] iv = message;

                    ivParameterSpecAESCBC = new IvParameterSpec(iv);
                }
                else if( ivAESOFB == null){
                    ivAESOFB = new String(message);
                    byte[] iv = message;
                    ivParameterSpecAESOFB = new IvParameterSpec(iv);
                }
                else if( ivDESCBC == null){
                    ivDESCBC = new String(message);
                    byte[] iv = message;
                    ivParameterSpecDESCBC = new IvParameterSpec(iv);
                }
                else if( ivDESOFB== null){
                    ivDESOFB = new String(message);
                    byte[] iv = message;
                    ivParameterSpecDESOFB= new IvParameterSpec(iv);
                }
                else{
                    chat.appendText(line);
                }
            } catch (Exception e) {
                System.out.println("listen\n"+ e);
            }
        }
    }
}