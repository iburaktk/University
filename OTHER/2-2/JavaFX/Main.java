import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application
{
	String zorlukSeviyesi;
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		
		
		try 
		{
			GridPane root = new GridPane();
			root.setHgap(50);
			GridPane menu = new GridPane();
			
			Label messageLabel = new Label(" Welcome to System. Proceed.");
			TextField firstTextField = new TextField();
			Label firstLabel = new Label(" Username: ");
			TextField secondTextField = new TextField();
			Label secondLabel = new Label(" Password: ");
			firstTextField.setPrefColumnCount(15);
			secondTextField.setPrefColumnCount(15);
			firstLabel.setLabelFor(firstTextField);
			secondLabel.setLabelFor(secondTextField);
			
			
			
			Label soru = new Label("Zorluk seviyesini secin: ");
			MenuButton zorluk = new MenuButton("Seciniz.");
			MenuItem easy = new MenuItem("Easy");
			easy.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				zorlukSeviyesi = "kolay";
				System.out.print(zorlukSeviyesi);
			};
			});
			MenuItem medium = new MenuItem("Medium");
			medium.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				zorlukSeviyesi = "orta";
				System.out.print(zorlukSeviyesi);
			};
			});
			MenuItem hard = new MenuItem("Hard");
			hard.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				zorlukSeviyesi = "zor";
				System.out.print(zorlukSeviyesi);
			};
			});
			zorluk.getItems().addAll(easy,medium,hard);
			
			Button baslaButton = new Button("Start Game");
			baslaButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					Game newGame = new Game(zorlukSeviyesi);
					primaryStage.setScene(new Scene(newGame.start(),1920,1030));
					primaryStage.setX(-10);
					primaryStage.setY(0);
				};
			});
			
			menu.addRow(0, soru);
			menu.addRow(1, zorluk);
			menu.addRow(2, baslaButton);
			
			
			Scene scene2 = new Scene(menu,900,500);
			
			Button signIn = new Button("Sign In");
			signIn.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent e) {
					if (firstTextField.getText().equals("admin"))
					{
						if (secondTextField.getText().equals("admin"))
						{
							System.out.print("Basarili");
							primaryStage.setScene(scene2);
						}
						else 
						{
							System.out.print("Wrong Password");
						}
					}
					else
					{
						System.out.print("Wrong Username");
					}
					
				}
			});
			//MenuButton - ChoiceBox - ComboBox - ListView
			//CheckBox
			//ToggleButton - RadioButton
			
			root.addRow(0, messageLabel);
			root.addRow(1, firstLabel,firstTextField);
			root.addRow(2, secondLabel,secondTextField);
			root.addRow(3, signIn);
			
			
			
			
			Scene scene1 = new Scene(root,600,400);
			
			
			primaryStage.setScene(scene2);
			primaryStage.setTitle("Python");
			primaryStage.show();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
}

class Game
{
	int zorlukSeviyesi;
	public Game(String zorlukSeviyesi)
	{
		if (zorlukSeviyesi.equals("kolay"))
			this.zorlukSeviyesi = 1;
		if (zorlukSeviyesi.equals("orta"))
			this.zorlukSeviyesi = 2;
		if (zorlukSeviyesi.equals("zor"))
			this.zorlukSeviyesi = 3;
	}
	
	public GridPane start()
	{
		GridPane root = new GridPane();
		
		
		
		return root;
	}
}





