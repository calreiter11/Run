package entity;

import java.util.ArrayList;
import java.util.Hashtable;

import graphics.Sprite;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

public class Player extends Entity {

//======================================================================
//
//--------------------------------Fields--------------------------------
//
//======================================================================
	
	//speed constants
	private static final double MAX_VX = 5;
	private static final double MAX_VY = 15;
	public static final double JUMP_HEIGHT = 10;
	
	//acceleration/deceleration constants
	private static final double ACC = 0.5;
	private static final double DEC = 0.5;
	
	//gravity
	private static final double GRAVITY = 0.5;
	
	//sprite constants
	private static final Sprite[][] SPRITES = Sprite.PLAYER;
	
	//animation
	private static final int ANIMATION_SPEED = 5;
	private static final int MOVE_STATE = 0;
	private static final int JUMP_STATE = 1;
	private static final int DEAD_STATE = 2;
	
	//reference to keyboard input
	private final Hashtable<KeyCode, Boolean> keys;
	
	//reference to platforms in world
	private final ArrayList<Platform> platforms;
	
	//reference to enemies in world
	private final ArrayList<Enemy> enemies;
	
	//double jump
	private boolean canDoubleJump = true;
	private int doubleJumpDelayTimer;
	private static final int DOUBLE_JUMP_DELAY = 10;
	
	//alive or dead
	private boolean dead;
	
//======================================================================
//
//-----------------------------Constructors-----------------------------
//
//======================================================================
	
	public Player(Hashtable<KeyCode, Boolean> keys, ArrayList<Platform> platforms, ArrayList<Enemy> enemies) {
		super(MAX_VX, MAX_VY, ACC, DEC, ANIMATION_SPEED, SPRITES);
		this.keys = keys;
		this.platforms = platforms;
		this.enemies = enemies;
		dead = false;
	}
	
//======================================================================
//
//-------------------------------Methods--------------------------------
//
//======================================================================
	
	@Override
	public void update(double rate) {
		if (!dead && enemyCollision())
			dead = true;
		
		move(rate);
		
		if (!dead) {
			if (inAir()) {
				if (vy < 0) {
					state = JUMP_STATE;
					frame = 0;
					animate();
				} else {
					state = MOVE_STATE;
					frame = 3;
				}
			} else {
				state = MOVE_STATE;
				animate();
			}
		} else {
			state = DEAD_STATE;
			frame = 0;
		}
	}
	
	@Override
	public void move(double rate) {
		boolean up = keys.get(KeyCode.UP);
		boolean down = keys.get(KeyCode.DOWN);
		boolean left = keys.get(KeyCode.LEFT);
		boolean right = keys.get(KeyCode.RIGHT);
		
		if (!dead) {
			//check for collisions
			if (onPlatform()) {
				if (up && !down) vy = -JUMP_HEIGHT;
				if (!up && down) vy = 1;
				if (!up && !down) vy = 0;			
				
				canDoubleJump = true;
				doubleJumpDelayTimer = 0;
			} else {
				if (up && canDoubleJump && doubleJumpDelayTimer > DOUBLE_JUMP_DELAY) {
					vy = -MAX_VY / 2;
					canDoubleJump = false;
					doubleJumpDelayTimer = 0;
				} else {
					doubleJumpDelayTimer++;
					vy += GRAVITY;
				}
			}
			
			//move left or right
			if (left || right) {
				if (left) vx -= ACC;
				if (right) vx += ACC;
			} else {
				if (vx > 0) vx -= DEC;
				if (vx < 0) vx += DEC;
			}
		} else {
			vy += GRAVITY;
		}
		
		//check speed bounds
		if (vx > MAX_VX) vx = MAX_VX;
		if (vx < -MAX_VX) vx = -MAX_VX;
		if (vy > MAX_VY) vy = MAX_VY;
		if (vy < -MAX_VY) vy = -MAX_VY;
				
		//update position
		x += vx;
		y += vy;
	}
	
	public boolean onPlatform() {
		double leftX = x;
		double rightX = x + width();
		double botY = y + height();
		double pLeftX, pRightX, pTopY;
		for (Platform platform : platforms) {
			pLeftX = platform.getX();
			pRightX = platform.getX() + platform.width();
			pTopY = platform.getY();
			//if centered in the x direction above platform
			if ((leftX >= pLeftX && leftX <= pRightX) || (rightX >= pLeftX && rightX <= pRightX)) {
				//if falling or at rest
				if (vy >= 0) {
					//if above platform in the y direction
					if (botY < pTopY) {
						//if next position is lower than top of platform
						if (botY + vy >= pTopY - 1) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean enemyCollision() {
		Rectangle playerRect = new Rectangle(x, y, width(), height());
		for (Enemy enemy : enemies)
			if (playerRect.intersects(enemy.getX(), enemy.getY(), enemy.width(), enemy.height()))
				return true;
		
		return false;
	}
	
	public boolean isRunning() {
		return ((keys.get(KeyCode.LEFT) || keys.get(KeyCode.RIGHT)) && vy == 0);
	}
	
	public boolean inAir() {
		return !onPlatform();
	}
	
	public void resurrect() {
		dead = false;
	}
	
//======================================================================
//
//---------------------------Getters/Setters----------------------------
//
//======================================================================
	
	
}
