import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;

/**
 * Assignment 4
 * @author �brahim Burak Tanr�kulu
 * Road Fighter
 */

public class Assignment4 extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}
	
	Rectangle yourCar,car1,car2,car3,car4,car5,car6,car7;
	Circle tree1,tree2,tree3,tree4,tree5,tree6,tree7,tree8;
	Rectangle line1,line2,line3,line4,line5,line6,line7,line8,line9,line10,line11,line12,line13,line14,line15,line16,line17;
	int[] carLocations = {125,160,175,195,230,330,365,385,400,435}; //X coordinates for new spawned cars
	int point = 0, level = 1, passedCar = 0, passedCarAmount = 0, velocity = 0;
	Boolean goLeft = false,goRight = false,accelarate=false;
	AnimationTimer animationTimer;
	HBox gameover,restart,yourScore;
	
	public void start(Stage primaryStage)
	{
		Rectangle leftLawn = new Rectangle(120,750,Color.GREEN);
		leftLawn.setLayoutX(0);
		Rectangle rightLawn = new Rectangle(120,750,Color.GREEN);
		rightLawn.setLayoutX(505);
		Rectangle road = new Rectangle(385,750,Color.DARKGRAY);
		road.setLayoutX(120);
		
		int car1Location = (int) (Math.random()*10);
		car1 = new Rectangle(65,110,Color.YELLOW);
		car1.setLayoutX(carLocations[car1Location]);
		car1.setLayoutY(440);
		car2 = new Rectangle(65,110,Color.YELLOW);
		car2.setLayoutX(carLocations[(int) (Math.random()*10)]);
		car2.setLayoutY(310);
		car3 = new Rectangle(65,110,Color.YELLOW);
		car3.setLayoutX(carLocations[(int) (Math.random()*10)]);
		car3.setLayoutY(180);
		car4 = new Rectangle(65,110,Color.YELLOW);
		car4.setLayoutX(carLocations[(int) (Math.random()*10)]);
		car4.setLayoutY(50);
		car5 = new Rectangle(65,110,Color.YELLOW);
		car5.setLayoutX(carLocations[(int) (Math.random()*10)]);
		car5.setLayoutY(-80);
		car6 = new Rectangle(65,110,Color.YELLOW);
		car6.setLayoutX(carLocations[(int) (Math.random()*10)]);
		car6.setLayoutY(-210);
		car7 = new Rectangle(65,110,Color.YELLOW);
		car7.setLayoutX(carLocations[(int) (Math.random()*10)]);
		car7.setLayoutY(-340);
		
		tree1 = new Circle(20,Color.RED);
		tree1.setLayoutX(60);
		tree1.setLayoutY(550);
		tree2 = new Circle(20,Color.DARKGREEN);
		tree2.setLayoutX(50);
		tree2.setLayoutY(330);
		tree3 = new Circle(20,Color.DARKGREEN);
		tree3.setLayoutX(70);
		tree3.setLayoutY(110);
		tree4 = new Circle(20,Color.DARKGREEN);
		tree4.setLayoutX(550);
		tree4.setLayoutY(635);
		tree5 = new Circle(20,Color.DARKGREEN);
		tree5.setLayoutX(570);
		tree5.setLayoutY(385);
		tree6 = new Circle(20,Color.DARKGREEN);
		tree6.setLayoutX(560);
		tree6.setLayoutY(165);
		tree7 = new Circle(20,Color.DARKGREEN);
		tree7.setLayoutX(580);
		tree7.setLayoutY(-55);
		tree8 = new Circle(20,Color.DARKGREEN);
		tree8.setLayoutX(80);
		tree8.setLayoutY(-110);
		
		line1 = new Rectangle(11,25,Color.BLACK);
		line1.setLayoutX(310);
		line1.setLayoutY(715);
		line2 = new Rectangle(11,25,Color.BLACK);
		line2.setLayoutX(310);
		line2.setLayoutY(665);
		line3 = new Rectangle(11,25,Color.BLACK);
		line3.setLayoutX(310);
		line3.setLayoutY(615);
		line4 = new Rectangle(11,25,Color.BLACK);
		line4.setLayoutX(310);
		line4.setLayoutY(565);
		line5 = new Rectangle(11,25,Color.BLACK);
		line5.setLayoutX(310);
		line5.setLayoutY(515);
		line6 = new Rectangle(11,25,Color.BLACK);
		line6.setLayoutX(310);
		line6.setLayoutY(465);
		line7 = new Rectangle(11,25,Color.BLACK);
		line7.setLayoutX(310);
		line7.setLayoutY(415);
		line8 = new Rectangle(11,25,Color.BLACK);
		line8.setLayoutX(310);
		line8.setLayoutY(365);
		line9 = new Rectangle(11,25,Color.BLACK);
		line9.setLayoutX(310);
		line9.setLayoutY(315);
		line10 = new Rectangle(11,25,Color.BLACK);
		line10.setLayoutX(310);
		line10.setLayoutY(265);
		line11 = new Rectangle(11,25,Color.BLACK);
		line11.setLayoutX(310);
		line11.setLayoutY(215);
		line12 = new Rectangle(11,25,Color.BLACK);
		line12.setLayoutX(310);
		line12.setLayoutY(165);
		line13 = new Rectangle(11,25,Color.BLACK);
		line13.setLayoutX(310);
		line13.setLayoutY(115);
		line14 = new Rectangle(11,25,Color.BLACK);
		line14.setLayoutX(310);
		line14.setLayoutY(65);
		line15 = new Rectangle(11,25,Color.BLACK);
		line15.setLayoutX(310);
		line15.setLayoutY(15);
		line16 = new Rectangle(11,25,Color.BLACK);
		line16.setLayoutX(310);
		line16.setLayoutY(-35);
		line17 = new Rectangle(11,25,Color.BLACK);
		line17.setLayoutX(310);
		line17.setLayoutY(-85);

		yourCar = new Rectangle(65,130,Color.RED);
		int randomNum = (int) (Math.random()*10);
		int yourCarLocation = (Math.abs(car1Location-randomNum) <= 2) ? (randomNum+5)%10 : randomNum;
		yourCar.setLayoutX(carLocations[yourCarLocation]);
		yourCar.setLayoutY(550);
		
		HBox texts = new HBox();
		Text score = new Text("Score: "+point+"\nLevel: "+level+"\nVelocity: "+velocity);
		score.setFill(Color.WHITE);
		texts.getChildren().addAll(score);
		
		Pane layout = new Pane();
		layout.getChildren().addAll(
				leftLawn,rightLawn,road, //background
				line1,line2,line3,line4,line5,line6,line7,line8,line9,line10,line11,line12,line13,line14,line15,line16,line17,
				car1,car2,car3,car4,car5,car6,car7, //rival cars
				tree1,tree2,tree3,tree4,tree5,tree6,tree7,tree8,
				yourCar,score);
		
		Scene scene = new Scene(layout, 625, 750);
		
		scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT:
                	goLeft = true;
                  	break;
                case RIGHT: 
                   	goRight = true;
                   	break;
                case UP:
                   	accelarate = true;
                   	break;
                case ENTER:
                	
                   	restart();
                   	layout.getChildren().removeAll(gameover,restart,yourScore);
                   	animationTimer.start();
                   	break;
                default:
                   	break;
            }
        });
		
        scene.setOnKeyReleased(e -> {
                switch (e.getCode()) {
                	case LEFT:
                		goLeft = false;
                		break;
                	case RIGHT:
                		goRight = false;
                		break;
                	case UP:
                		accelarate = false;
                    	break;
                	default:
                		break;
                }
            });
        
        
        primaryStage.setTitle("HUBBM-Racer");
		primaryStage.setScene(scene);
		primaryStage.show();
        
		
        animationTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				
				try {Thread.sleep(10);} catch (InterruptedException e1) {e1.printStackTrace();}
				
		        score.setText("\nScore: "+point+"\nLevel: "+level+"\nVelocity: "+velocity);
		        
		        if (4+(level * 2) == passedCarAmount)
		        {
		        	level++;
		        	passedCarAmount=0;
		        }
				int horizontalMove = 0;
				if (goLeft)
				{
					horizontalMove-=4;
				}
				if (goRight)
				{
					horizontalMove+=4;
				}
				if (accelarate)
				{
					velocity = (velocity < 40 + (level * 20)) ? velocity+1 : velocity ;
				}
				else 
				{
					velocity = (velocity > 0) ? velocity-1 : velocity ;
				}
				moveIt(horizontalMove);
				
				if (!checkCollision())
				{
					yourCar.setFill(Color.BLACK);
					if ((passedCar+1)%7 == 1)
					{
						car1.setFill(Color.BLACK);
					}
					if ((passedCar+1)%7 == 2)
					{
						car2.setFill(Color.BLACK);
					}
					if ((passedCar+1)%7 == 3)
					{
						car3.setFill(Color.BLACK);
					}
					if ((passedCar+1)%7 == 4)
					{
						car4.setFill(Color.BLACK);
					}
					if ((passedCar+1)%7 == 5)
					{
						car5.setFill(Color.BLACK);
					}
					if ((passedCar+1)%7 == 6)
					{
						car6.setFill(Color.BLACK);
					}
					if ((passedCar+1)%7 == 0)
					{
						car7.setFill(Color.BLACK);
					}
					
					animationTimer.stop();
					gameover = new HBox();
					Text gameoverText = new Text("GAME OVER");
					gameoverText.setFont(Font.font ("Verdana", 80));
					gameoverText.setFill(Color.RED);
					gameover.getChildren().add(gameoverText);
					gameover.setLayoutX(65);
					gameover.setLayoutY(200);
					yourScore = new HBox();
					Text yourScoreText = new Text("Your Score: "+point);
					yourScoreText.setFont(Font.font ("Verdana", 50));
					yourScoreText.setFill(Color.RED);
					yourScore.getChildren().add(yourScoreText);
					yourScore.setLayoutX(110);
					yourScore.setLayoutY(300);
					restart = new HBox();
					Text restartText = new Text("Press ENTER for restart.");
					restartText.setFont(Font.font ("Verdana", 40));
					restartText.setFill(Color.RED);
					restart.getChildren().add(restartText);
					restart.setLayoutX(70);
					restart.setLayoutY(400);
					layout.getChildren().addAll(gameover,restart,yourScore);
				}
			}
		};
		animationTimer.start();
	}

	
	
	/**
	 * This method moves objects vertically and moves my car horizontally.
	 * @param horizontalMove
	 */
	public void moveIt(int horizontalMove) 
	{
		int newPosition = (int) (yourCar.getLayoutX() + horizontalMove);
		if (newPosition > 120 && newPosition < 440 && velocity != 0 )
		{
			yourCar.relocate(newPosition, yourCar.getLayoutY());
		}
		int gap;
		if (car1.getLayoutY() >= 550 && car1.getLayoutY() < 570)
		{
			if (passedCar != 1)
			{
				car1.setFill(Color.GREEN);
				point += level;
				passedCarAmount++;
			}
			passedCar = 1;
		}
		if (car1.getLayoutY() >= 750)
		{
			gap = (int) (car1.getLayoutY() - 750);
			car1.relocate(carLocations[(int) (Math.random()*10)], -160+gap);
			car1.setFill(Color.YELLOW);
		}
		else car1.relocate(car1.getLayoutX(), car1.getLayoutY()+(velocity/10));
		
		if (car2.getLayoutY() >= 550  && car2.getLayoutY() < 570)
		{
			if (passedCar != 2)
			{
				car2.setFill(Color.GREEN);
				point += level;
				passedCarAmount++;
			}
			passedCar = 2;
		}
		if (car2.getLayoutY() >= 750)
		{
			gap = (int) (car2.getLayoutY() - 750);
			car2.relocate(carLocations[(int) (Math.random()*10)], -160+gap);
			car2.setFill(Color.YELLOW);
		}
		else car2.relocate(car2.getLayoutX(), car2.getLayoutY()+(velocity/10));
		
		if (car3.getLayoutY() >= 550  && car3.getLayoutY() < 570)
		{
			if (passedCar != 3)
			{
				car3.setFill(Color.GREEN);
				point += level;
				passedCarAmount++;
			}
			passedCar = 3;
		}
		if (car3.getLayoutY() >= 750)
		{
			gap = (int) (car3.getLayoutY() - 750);
			car3.relocate(carLocations[(int) (Math.random()*10)], -160+gap);
			car3.setFill(Color.YELLOW);
		}
		else car3.relocate(car3.getLayoutX(), car3.getLayoutY()+(velocity/10));
		
		if (car4.getLayoutY() >= 550  && car4.getLayoutY() < 570)
		{
			if (passedCar != 4)
			{
				car4.setFill(Color.GREEN);
				point += level;
				passedCarAmount++;
			}
			passedCar = 4;
		}
		if (car4.getLayoutY() >= 750)
		{
			gap = (int) (car4.getLayoutY() - 750);
			car4.relocate(carLocations[(int) (Math.random()*10)], -160+gap);
			car4.setFill(Color.YELLOW);
		}
		else car4.relocate(car4.getLayoutX(), car4.getLayoutY()+(velocity/10));
		
		if (car5.getLayoutY() >= 550  && car5.getLayoutY() < 570)
		{
			if (passedCar != 5)
			{
				car5.setFill(Color.GREEN);
				point += level;
				passedCarAmount++;
			}
			passedCar = 5;
		}
		if (car5.getLayoutY() >= 750)
		{
			gap = (int) (car5.getLayoutY() - 750);
			car5.relocate(carLocations[(int) (Math.random()*10)], -160+gap);
			car5.setFill(Color.YELLOW);
		}
		else car5.relocate(car5.getLayoutX(), car5.getLayoutY()+(velocity/10));
		
		if (car6.getLayoutY() >= 550  && car6.getLayoutY() < 570)
		{
			if (passedCar != 6)
			{
				car6.setFill(Color.GREEN);
				point += level;
				passedCarAmount++;
			}
			passedCar = 6;
		}
		if (car6.getLayoutY() >= 750)
		{
			gap = (int) (car6.getLayoutY() - 750);
			car6.relocate(carLocations[(int) (Math.random()*10)], -160+gap);
			car6.setFill(Color.YELLOW);
		}
		else car6.relocate(car6.getLayoutX(), car6.getLayoutY()+(velocity/10));
		
		if (car7.getLayoutY() >= 550  && car7.getLayoutY() < 570)
		{
			if (passedCar != 7)
			{
				car7.setFill(Color.GREEN);
				point += level;
				passedCarAmount++;
			}
			passedCar = 7;
		}
		if (car7.getLayoutY() >= 750)
		{
			gap = (int) (car7.getLayoutY() - 750);
			car7.relocate(carLocations[(int) (Math.random()*10)], -160+gap);
			car7.setFill(Color.YELLOW);
		}
		else car7.relocate(car7.getLayoutX(), car7.getLayoutY()+(velocity/10));
		
		
		
		if (tree1.getCenterY() >= 220)
		{
			gap = (int) (tree1.getCenterY() - 770);
			tree1.setCenterY(-130+gap);
		}
		else tree1.setCenterY(tree1.getCenterY()+(velocity/10));
		
		if (tree2.getCenterY() >= 440)
		{
			gap = (int) (tree2.getCenterY() - 770);
			tree2.setCenterY(-130+gap);
		}
		else tree2.setCenterY(tree2.getCenterY()+(velocity/10));
		
		if (tree3.getCenterY() >= 660)
		{
			gap = (int) (tree3.getCenterY() - 770);
			tree3.setCenterY(-130+gap);
		}
		else tree3.setCenterY(tree3.getCenterY()+(velocity/10));
		
		if (tree4.getCenterY() >= 135)
		{
			gap = (int) (tree4.getCenterY() - 770);
			tree4.setCenterY(-130+gap);
		}
		else tree4.setCenterY(tree4.getCenterY()+(velocity/10));
		
		if (tree5.getCenterY() >= 355)
		{
			gap = (int) (tree5.getCenterY() - 770);
			tree5.setCenterY(-130+gap);
		}
		else tree5.setCenterY(tree5.getCenterY()+(velocity/10));
		
		if (tree6.getCenterY() >= 575)
		{
			gap = (int) (tree6.getCenterY() - 770);
			tree6.setCenterY(-130+gap);
		}
		else tree6.setCenterY(tree6.getCenterY()+(velocity/10));
		
		if (tree7.getCenterY() >= 795)
		{
			gap = (int) (tree7.getCenterY() - 770);
			tree7.setCenterY(-130+gap);
		}
		else tree7.setCenterY(tree7.getCenterY()+(velocity/10));
		
		if (tree8.getCenterY() >= 880)
		{
			gap = (int) (tree8.getCenterY() - 770);
			tree8.setCenterY(-130+gap);
		}
		else tree8.setCenterY(tree8.getCenterY()+(velocity/10));
		
		
		
		if (line1.getLayoutY() >= 750)
		{
			gap = (int) (line1.getLayoutY() - 750);
			line1.relocate(310, -100+gap);
		}
		else line1.relocate(310, line1.getLayoutY()+(velocity/10));
		
		if (line2.getLayoutY() >= 750)
		{
			gap = (int) (line2.getLayoutY() - 750);
			line2.relocate(310, -100+gap);
		}
		else line2.relocate(310, line2.getLayoutY()+(velocity/10));
		
		if (line3.getLayoutY() >= 750)
		{
			gap = (int) (line3.getLayoutY() - 750);
			line3.relocate(310, -100+gap);
		}
		else line3.relocate(310, line3.getLayoutY()+(velocity/10));
		
		if (line4.getLayoutY() >= 750)
		{
			gap = (int) (line4.getLayoutY() - 750);
			line4.relocate(310, -100+gap);
		}
		else line4.relocate(310, line4.getLayoutY()+(velocity/10));
		
		if (line5.getLayoutY() >= 750)
		{
			gap = (int) (line5.getLayoutY() - 750);
			line5.relocate(310, -100+gap);
		}
		else line5.relocate(310, line5.getLayoutY()+(velocity/10));
		
		if (line6.getLayoutY() >= 750)
		{
			gap = (int) (line6.getLayoutY() - 750);
			line6.relocate(310, -100+gap);
		}
		else line6.relocate(310, line6.getLayoutY()+(velocity/10));
		
		if (line7.getLayoutY() >= 750)
		{
			gap = (int) (line7.getLayoutY() - 750);
			line7.relocate(310, -100+gap);
		}
		else line7.relocate(310, line7.getLayoutY()+(velocity/10));
		
		if (line8.getLayoutY() >= 750)
		{
			gap = (int) (line8.getLayoutY() - 750);
			line8.relocate(310, -100+gap);
		}
		else line8.relocate(310, line8.getLayoutY()+(velocity/10));
		
		if (line9.getLayoutY() >= 750)
		{
			gap = (int) (line9.getLayoutY() - 750);
			line9.relocate(310, -100+gap);
		}
		else line9.relocate(310, line9.getLayoutY()+(velocity/10));
		
		if (line10.getLayoutY() >= 750)
		{
			gap = (int) (line10.getLayoutY() - 750);
			line10.relocate(310, -100+gap);
		}
		else line10.relocate(310, line10.getLayoutY()+(velocity/10));
		
		if (line11.getLayoutY() >= 750)
		{
			gap = (int) (line11.getLayoutY() - 750);
			line11.relocate(310, -100+gap);
		}
		else line11.relocate(310, line11.getLayoutY()+(velocity/10));
		
		if (line12.getLayoutY() >= 750)
		{
			gap = (int) (line12.getLayoutY() - 750);
			line12.relocate(310, -100+gap);
		}
		else line12.relocate(310, line12.getLayoutY()+(velocity/10));
		
		if (line13.getLayoutY() >= 750)
		{
			gap = (int) (line13.getLayoutY() - 750);
			line13.relocate(310, -100+gap);
		}
		else line13.relocate(310, line13.getLayoutY()+(velocity/10));
		
		if (line14.getLayoutY() >= 750)
		{
			gap = (int) (line14.getLayoutY() - 750);
			line14.relocate(310, -100+gap);
		}
		else line14.relocate(310, line14.getLayoutY()+(velocity/10));
		
		if (line15.getLayoutY() >= 750)
		{
			gap = (int) (line15.getLayoutY() - 750);
			line15.relocate(310, -100+gap);
		}
		else line15.relocate(310, line15.getLayoutY()+(velocity/10));
		
		if (line16.getLayoutY() >= 750)
		{
			gap = (int) (line16.getLayoutY() - 750);
			line16.relocate(310, -100+gap);
		}
		else line16.relocate(310, line16.getLayoutY()+(velocity/10));
		
		if (line17.getLayoutY() >= 750)
		{
			gap = (int) (line17.getLayoutY() - 750);
			line17.relocate(310, -100+gap);
		}
		else line17.relocate(310, line17.getLayoutY()+(velocity/10));
	}
	
	
	/**
	 * This method checks collision
	 * @return My car chrashed or not?
	 */
	private boolean checkCollision()
	{
		if (car1.getLayoutY() > 439 && car1.getLayoutY() < 550 )
		{
			if (car1.getLayoutX() - 65 < yourCar.getLayoutX() && yourCar.getLayoutX() < car1.getLayoutX() + 65 )
			{
				return false;
			}
		}
		else if (car2.getLayoutY() > 439 && car2.getLayoutY() < 550 )
		{
			if (car2.getLayoutX() - 65 < yourCar.getLayoutX() && yourCar.getLayoutX() < car2.getLayoutX() + 65 )
			{
				return false;
			}
		}
		else if (car3.getLayoutY() > 439 && car3.getLayoutY() < 550)
		{
			if (car3.getLayoutX() - 65 < yourCar.getLayoutX() && yourCar.getLayoutX() < car3.getLayoutX() + 65 )
			{
				return false;
			}
		}
		else if (car4.getLayoutY() > 439 && car4.getLayoutY() < 550 )
		{
			if (car4.getLayoutX() - 65 < yourCar.getLayoutX() && yourCar.getLayoutX() < car4.getLayoutX() + 65 )
			{
				return false;
			}
		}
		else if (car5.getLayoutY() > 439 && car5.getLayoutY() < 550 )
		{
			if (car5.getLayoutX() - 65 < yourCar.getLayoutX() && yourCar.getLayoutX() < car5.getLayoutX() + 65 )
			{
				return false;
			}
		}
		else if (car6.getLayoutY() > 439 && car6.getLayoutY() < 550 )
		{
			if (car6.getLayoutX() - 65 < yourCar.getLayoutX() && yourCar.getLayoutX() < car6.getLayoutX() + 65 )
			{
				return false;
			}
		}
		else if (car7.getLayoutY() > 439 && car7.getLayoutY() < 550 )
		{
			if (car7.getLayoutX() - 65 < yourCar.getLayoutX() && yourCar.getLayoutX() < car7.getLayoutX() + 65 )
			{
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * This method relocates objects and resets values.
	 */
	private void restart()
	{
		int car1Location = (int) (Math.random()*10);
		int randomNum = (int) (Math.random()*10);
		int yourCarLocation = (Math.abs(car1Location-randomNum) <= 2) ? (randomNum+5)%10 : randomNum;
		car1.setLayoutX(carLocations[car1Location]);
		car1.setLayoutY(440);
		car2.setLayoutX(carLocations[(int) (Math.random()*10)]);
		car2.setLayoutY(310);
		car3.setLayoutX(carLocations[(int) (Math.random()*10)]);
		car3.setLayoutY(180);
		car4.setLayoutX(carLocations[(int) (Math.random()*10)]);
		car4.setLayoutY(50);
		car5.setLayoutX(carLocations[(int) (Math.random()*10)]);
		car5.setLayoutY(-80);
		car6.setLayoutX(carLocations[(int) (Math.random()*10)]);
		car6.setLayoutY(-210);
		car7.setLayoutX(carLocations[(int) (Math.random()*10)]);
		car7.setLayoutY(-340);
		yourCar.setLayoutX(carLocations[yourCarLocation]);
		line1.setLayoutY(715);
		line2.setLayoutY(665);
		line3.setLayoutY(615);
		line4.setLayoutY(565);
		line5.setLayoutY(515);
		line6.setLayoutY(465);
		line7.setLayoutY(415);
		line8.setLayoutY(365);
		line9.setLayoutY(315);
		line10.setLayoutY(265);
		line11.setLayoutY(215);
		line12.setLayoutY(165);
		line13.setLayoutY(115);
		line14.setLayoutY(65);
		line15.setLayoutY(15);
		line16.setLayoutY(-35);
		line17.setLayoutY(-85);
		car1.setFill(Color.YELLOW);
		car2.setFill(Color.YELLOW);
		car3.setFill(Color.YELLOW);
		car4.setFill(Color.YELLOW);
		car5.setFill(Color.YELLOW);
		car6.setFill(Color.YELLOW);
		car7.setFill(Color.YELLOW);
		yourCar.setFill(Color.RED);
		point=0;
		level=1;
		velocity=0;
		passedCarAmount=0;
		passedCar=0;
	}
}