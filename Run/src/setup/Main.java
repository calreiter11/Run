package setup;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Main extends Application {
	
	//main method
	public static void main(String[] args) {
		launch(args);
	}
	
	//start the game
	@Override
	public void start(Stage stage) throws Exception {
		//initialize components
		Group root = new Group();
		Scene scene = new Scene(root);
		ImageView background = 
//				new ImageView(new Image("file:C:/Users/Caleb/Documents/AHA Backup/AHA/AHA 15-16/AP Computer Science/Run/res/background.png"));
				new ImageView(new Image("file:D:/Users/Caleb/OneDrive - UW-Madison/eclipse-workspace/Run/res/background.png"));
		Canvas canvas = new Canvas(background.getImage().getWidth(), background.getImage().getHeight());
		Canvas canvas2 = new Canvas(canvas.getWidth(), canvas.getHeight());
		GraphicsContext gc = canvas.getGraphicsContext2D();
		GraphicsContext gc2 = canvas2.getGraphicsContext2D();
		
		//initialize music
		final boolean MUSIC = false; 
		if (MUSIC) {
			Media backgroundMusic = 
					new Media(
							new File("C:/Users/calreiter16/Desktop/AHA/AHA 15-16/AP Computer Science/Run/res/audio/got.mp4").toURI().toString());
			MediaPlayer player = new MediaPlayer(backgroundMusic);
			player.setCycleCount(MediaPlayer.INDEFINITE);
			player.play();
		}
		
		//add components
		root.getChildren().add(background);
		root.getChildren().add(canvas);
		root.getChildren().add(canvas2);
		
		//start game
		Game game = new Game(scene, gc, gc2);
		game.start();
		
		//display game
		stage.setScene(scene);
		stage.show();
	}

}
