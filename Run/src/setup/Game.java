package setup;

import java.util.ArrayList;
import java.util.Hashtable;

import entity.Enemy;
import entity.Platform;
import entity.Player;
import graphics.Sprite;
import javafx.animation.AnimationTimer;
import javafx.animation.RotateTransition;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class Game extends AnimationTimer {

//======================================================================
//
//--------------------------------Fields--------------------------------
//
//======================================================================
	
	private double gameWidth; //game width
	private double gameHeight; //game height
	
	private ArrayList<Platform> platforms; //platforms to update/render
	private ArrayList<Enemy> enemies; //enemies to update/render
	
	private Player player; //the player
	
	private Hashtable<KeyCode, Boolean> keys; //keys that are pressed (true) or not pressed (false)
	
	private static final KeyCode[] KEYS_TO_INITIALIZE = //keys initialized as false to avoid NullPointerException errors
		{KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.SPACE};
	
	private Scene scene; //the scene
	
	private GraphicsContext gc; //the GraphicsContext of the canvas of the game/entities
	private GraphicsContext gc2; //the GraphicsContext of the canvas of the text
	
	private double rate; //the speed multiplier 
	private double rateTimer; //counter to increase the rate
	
	private double score; //the score
	
	private int rotationInterval; //counter to determine how often the game is rotated
	private static final int MIN_ROTATION_INTERVAL = 12;
	private static final int ANGLES_PER_SECOND = 30;
	private long rotationTimer;
	private RotateTransition rotateTransition;

//======================================================================
//
//-----------------------------Constructors-----------------------------
//
//======================================================================
	
	public Game(Scene scene, GraphicsContext gc, GraphicsContext gc2) {
		platforms = new ArrayList<Platform>();
		enemies = new ArrayList<Enemy>();
		keys = new Hashtable<KeyCode, Boolean>();
		initializeKeys();
		player = new Player(keys, platforms, enemies);
		this.scene = scene;
		this.gc = gc;
		this.gc2 = gc2;
		gameWidth = gc.getCanvas().getWidth();
		gameHeight = gc.getCanvas().getHeight();
		
		this.scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				keys.put(event.getCode(), true);
			}
		});
		
		this.scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				keys.put(event.getCode(), false);
			}
		});
		
		resetGame();
		
//		scene.setOnKeyPressed(e -> keys.put(e.getCode(), true));
//		scene.setOnKeyReleased(e -> keys.put(e.getCode(), false));
	}
	
//======================================================================
//
//-------------------------------Methods--------------------------------
//
//======================================================================

	@Override
	public void handle(long now) {
		update();
		render();
	}
	
	public void update() {		
		//if still playing
		if (!gameOver()) {
			//update platforms
			for (Platform platform : platforms)
				platform.update(rate);
			//update enemies
			for (Enemy enemy : enemies)
				enemy.update(rate);
			//update player
			player.update(rate);
			//rotate
			if ((System.currentTimeMillis() - rotationTimer) / 1000 >= rotationInterval) {
				int angle = (int) (Math.random() * 720) - 360;
				rotate(angle, Math.abs(angle / ANGLES_PER_SECOND));
				rotationTimer = System.currentTimeMillis();
				if (rotationInterval > MIN_ROTATION_INTERVAL) rotationInterval--;
			}
			//generate new platform
			Platform lastPlatform = platforms.get(platforms.size() - 1);
			if (lastPlatform.getX() < gameWidth - lastPlatform.width())
				addRandomPlatform();
			//generate new enemy
			Enemy lastEnemy = enemies.get(enemies.size() - 1);
			if (lastEnemy.getX() < 0)
				addRandomEnemy();
			//remove platforms that are off the screen
			cleanUpEntities();
			//speed up the game
			rate = Math.sqrt(rateTimer);
			rateTimer += .005;
			//add to score
			score += .5 * rate;
		} else {
			if (rotateTransition != null)
				rotateTransition.stop();
			if (keys.get(KeyCode.SPACE))
				resetGame();
		}
	}
	
	public void render() {
		//clear screen
		gc.clearRect(0, 0, gameWidth, gameHeight);
		gc2.clearRect(0, 0, gameWidth, gameHeight);
		
		if (!gameOver()) {
			//draw border
			gc.beginPath();
			gc.rect(0, 0, gameWidth, gameHeight);
			gc.setStroke(Color.WHITE);
			gc.setLineWidth(3);
			gc.setLineDashes(25);
			gc.stroke();
			gc.closePath();
			//render player
			gc.drawImage(player.getCurrentSprite().getImage(), player.getX(), player.getY());		
			//render platforms
			for (Platform platform : platforms) {
				gc.drawImage(platform.getCurrentSprite().getImage(), platform.getX(), platform.getY());
				//fill under starting platforms
				if (platform.getCurrentSprite() == Sprite.NULL_PLATFORM[0][0]) {
					gc.setFill(Color.DARKBLUE);
					gc.fillRect(platform.getX() - 1, platform.getY() + platform.height() - 1, 
							platform.width() + 1, gameHeight - platform.getY() - platform.height() + 1);
				}
			}
			//render enemies
			for (Enemy enemy : enemies)
				gc.drawImage(enemy.getCurrentSprite().getImage(), enemy.getX(), enemy.getY());
			//display score
			gc2.setTextAlign(TextAlignment.LEFT);
			gc2.setFill(Color.BLACK);
			gc2.setFont(new Font("Bitwise", 50));
			gc2.fillText(String.format("Score: " + (int) score), 0, 40);
			//display rotate interval
			gc2.setFont(new Font("Bitwise", 30));
			gc2.fillText("Time to rotation: " + (rotationInterval - (System.currentTimeMillis() - rotationTimer) / 1000), 0, 70);
			//display rate
			gc2.setFont(new Font("Bitwise", 25));
			gc2.fillText(String.format("Rate: %.3f", rate), 0, 100);
		} else {
			//display game over
			gc2.setFill(Color.BLACK);
			gc2.setTextAlign(TextAlignment.CENTER);
			gc2.setFont(new Font("Bitwise", 75));
			gc2.fillText("GAME OVER", gameWidth / 2, gameHeight / 2 - 60);
			gc2.setFont(new Font("Bitwise", 50));
			gc2.fillText("Score: " + (int) score, gameWidth / 2, gameHeight / 2 - 15);
			gc2.setFont(new Font("Bitwise", 30));
			gc2.fillText("Press space to play again", gameWidth / 2, gameHeight / 2 + 15);
		}
	}
	
	public void rotate(int angle, int time) {
		rotateTransition = new RotateTransition(Duration.seconds(time), gc.getCanvas());
		rotateTransition.setFromAngle(gc.getCanvas().getRotate());
		rotateTransition.setToAngle(gc.getCanvas().getRotate() + angle);
		rotateTransition.play();
	}
	
	public boolean gameOver() {
		return (player.getY() >= gameHeight);
	}
	
	public void resetGame() {
		//remove all platforms
		platforms.clear();		
		//remove all enemies
		enemies.clear();
		//reset player position
		player.setPosition(50, 50);
		//resurrect player
		player.resurrect();
		//reset all counters/timers
		rate = rateTimer = 1;
		score = 1;
		rotationInterval = 20; /////////////////////////////////////////////////////////////////////////
		rotationTimer = System.currentTimeMillis();
		//generate new platforms
		generateStartPlatforms();
		//add random enemy
		addRandomEnemy();
		//reset rotation
		gc.getCanvas().setRotate(0);
	}
	
	public void generateStartPlatforms() {
		for (int i = 0; i < 5; i++)
			platforms.add(new Platform(Sprite.NULL_PLATFORM[0][0].width() * i, gameHeight / 2, Sprite.NULL_PLATFORM));
	}
	
	public void addRandomPlatform() {
		//generate variables
		Sprite[][] sprite = Sprite.PLATFORMS[(int) (Math.random() * Sprite.PLATFORMS.length)];
		double topBound = platforms.get(platforms.size() - 1).getY() - Player.JUMP_HEIGHT * 15;
		double botBound = gameHeight - sprite[0][0].height();
		double x = platforms.get(platforms.size() - 1).getX() + platforms.get(platforms.size() - 1).width() + 
				(Math.random() * 2 * sprite[0][0].width());
		double y = Math.random() * (botBound - topBound) + topBound;
		//check bounds
		y = Math.max(y, sprite[0][0].height() + 2 * player.height());
		y = Math.min(y, botBound);
		//add platform
		platforms.add(new Platform(x, y, sprite));
	}
	
	public void addRandomEnemy() {
		//generate variables
		Sprite[][] sprite = Sprite.ENEMIES[(int) (Math.random() * Sprite.ENEMIES.length)];
		double y = Math.random() * (gameHeight - sprite[0][0].height());
		//add enemy
		enemies.add(new Enemy(2, 4, 0.25, gameHeight, sprite));
		//position enemy
		enemies.get(enemies.size() - 1).setPosition(gameWidth, y);
	}
	
	public void cleanUpEntities() {
		if (platforms.size() > 0 && platforms.get(0).getX() + platforms.get(0).width() < 0)
			platforms.remove(0);
		if (enemies.size() > 0 && enemies.get(0).getX() + enemies.get(0).width() < 0)
			enemies.remove(0);
	}
	
	public void initializeKeys() {
		for (KeyCode key : KEYS_TO_INITIALIZE)
			keys.put(key, false);
	}
	
//======================================================================
//
//---------------------------Getters/Setters----------------------------
//
//======================================================================
	
}
