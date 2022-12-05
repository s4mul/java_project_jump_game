
package application;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.Random;


public class Main extends Application {
	private obst Ob1;
	private playChar player;
	private obst Ob2;
	private Text message;
	private Text score;
	private Text scoreboard;
	int flag = 0;
	boolean play = false;
	int scorevalue = 0;
	int speed;
	public void start(Stage primaryStage) {
		try {
			//playChar play = new playChar();
			Ob1 = new obst();
			Ob2 = new obst();
			player = new playChar();
			
			
			player.setScaleX(0.25);
			player.setScaleY(0.25);
			//player.setTranslateY(30);

			player.setTranslateY(30);
			player.setTranslateX(-50);
			
			Ob1.setScaleY(0.25);
			Ob1.setScaleX(0.25);
		
			
			Ob2.setScaleY(0.25);
			Ob2.setScaleX(0.25);
			
			Ob1.setTranslateY(300);
			Ob2.setTranslateY(300);
			Ob2.setTranslateX(300);
			Rectangle Ground = new Rectangle(0,450,500,50);
			Ob1.reset(0);
			Ob2.reset(0);
			
			message = new Text(30,200,"Press SPACE to Start");
			message.setFont(new Font(50));
			
			score = new Text(25,25,"");
			
			scoreboard = new Text(200,300,"");
			
			
			Group root = new Group(message,score,scoreboard,Ob1,Ob2,player,Ground);
			Scene scene = new Scene(root ,500,500);
			
			scene.setOnKeyPressed(this::processGame);
			
			
			primaryStage.setTitle("Avoid ");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);

		
	}
	public void processGame(KeyEvent event) {
		
		
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
			for(int i = 0;i <= 100; i++) {	
				int j = i;
				
				
				Platform.runLater(new Runnable() {
					@Override
					public void run(){
						player.jump(player.calcCor(j));
					}
				});
				try{ Thread.sleep(20);}
				catch(InterruptedException e) {};
			}
			flag = 0;
			}
		});
		thread1.setDaemon(true);

		switch(event.getCode()) {
		case UP:
			if(flag == 0 && play == true) {
				
				flag = 1;
				
				thread1.start();
			}
			break;
		case SPACE:
			
			if(play == false && flag == 0) {
				startGame();
				
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						Random rand = new Random();
						
					while(play == true) {	
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								
									Ob1.moveObs();
									//Ob2.moveSub(Ob1.currentX(), 50);
									
									if(Ob1.currentX() >= -160 && Ob1.currentX() < -150) {
										//Ob1.reset(0);
										scorevalue += 1;
										score.setText("Score: " + scorevalue);
									}

									Ob2.moveObs();
									//Ob2.moveSub(Ob1.currentX(), 50);
									
									if(Ob2.currentX() < -450) {
										
										Ob1.reset(0);
						
										Ob2.reset(Ob1.currentX() - 350 - rand.nextInt(120));
										scorevalue += 1;
										score.setText("Score: " + scorevalue);
									}
									//System.out.println(Ob2.currentX());
									if(-70 < Ob1.currentX() && Ob1.currentX() < 0 && player.currentY() > -62.5)
										endGame();
									if(-370 < Ob2.currentX() && Ob2.currentX() < -300 && player.currentY() > -62.5)
										endGame();
									
									//try{ Thread.sleep(10);}
									//catch(InterruptedException e) {};
									if(scorevalue < 50) speed = scorevalue;
							};
							
						});
						try{ Thread.sleep(100 - speed);}
						//try{ Thread.sleep(100);}
						catch(InterruptedException e) {};
					}
					}
				});
				thread.setDaemon(true);
				thread.start();
			}
			break;
		case ESCAPE:
			endGame();
			
			break;
		default:
			
		
		}
	}	
	
	 public void startGame() {
		 play = true;
		 scorevalue = 0;
		 Ob1.reset(0);
		 Ob2.reset(0);
		 //player.setTranslateY(player.currentY());
		 //System.out.println(player.currentY());
		 player.setTranslateY(30 - player.currentY());
		 
		 message.setText("");
		 scoreboard.setText("");
		 score.setText("Score: 0");
		 score.setFont(new Font(20));
	 }
	 public void endGame() {
		 play = false;
	 	 score.setText("");
		 message.setText("Press Space to Start");
		 scoreboard.setText("Score: " + scorevalue);
		 scoreboard.setFont(new Font(20));
		 //System.out.println(player.currentY());
		 
	 }
	
		
}
class playChar extends Group{
	Circle reg1;
	Circle reg2;
	Rectangle recBot;
	Rectangle recTop;
	Circle wel1;
	Circle wel2;
	Group body;
	public playChar() {
		recBot = new Rectangle(0,350,300,100);
		recTop = new Rectangle(50,300,200,100);
		wel1 = new Circle(70,450,45);
		wel2 = new Circle(230,450,45);
		wel1.setFill(Color.WHITE);
		wel2.setFill(Color.WHITE);
		
		body = new Group(recBot, recTop, wel1,wel2);
		reg1 = new Circle(70,450,43);
		reg2 = new Circle(230,450,43);
		getChildren().addAll(body, reg1, reg2);
	}
	
	public void jump(double change) {
		//body.setTranslateY(body.getTranslateY() - change);
		body.setTranslateY(body.getTranslateY() - change);
		reg1.setTranslateY(reg1.getTranslateY() - change);
		reg2.setTranslateY(reg2.getTranslateY() - change);
	}
	public double calcCor(int i) {
		return -0.1*i + 5;
	}
	public double currentY() {
		return body.getTranslateY();
	}
}

class obst extends Group{
	Rectangle body;
	Rectangle line1;
	Rectangle line2;
	Rectangle line3;
	Rectangle remover1;
	Rectangle remover2;
	Group obstr;
	public obst() {
		body = new Rectangle(100,0,50,250);
		line1 = new Rectangle(50,30,150,30);
		line2 = new Rectangle(50,105,150,30);
		line3 = new Rectangle(50,180,150,30);
		
		remover1 = new Rectangle(0,-20,100,250);
		remover2 = new Rectangle(150,0,150,270);
		
		line1.setRotate(45);
		line2.setRotate(45);
		line3.setRotate(45);
		line1.setFill(Color.YELLOW);
		line2.setFill(Color.YELLOW);
		line3.setFill(Color.YELLOW);
		
		remover1.setFill(Color.WHITE);
		remover2.setFill(Color.WHITE);
		
		obstr = new Group(body, line1,line2,line3,remover1, remover2);
		getChildren().addAll(obstr);
	}
	public void reset(double state) {
		//System.out.println(obstr.getTranslateX());      
		obstr.setTranslateX(350 + state);
	}
	public void moveObs() {
		//System.out.println(obstr.getTranslateX());
		obstr.setTranslateX(obstr.getTranslateX() - 10);
	}
	public double currentX() {
		return obstr.getTranslateX();
	}
	public void moveSub(double corMain, double turm) {
		obstr.setTranslateX(corMain + turm);
	}
}

