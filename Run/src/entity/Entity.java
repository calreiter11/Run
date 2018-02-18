package entity;

import graphics.Sprite;

public abstract class Entity {

//======================================================================
//
//--------------------------------Fields--------------------------------
//
//======================================================================
		
	//location
//	protected int x, y;
	protected double x, y;
	
	//speed and acceleration/deceleration
	protected double vx, vy, ax, ay;
	protected final double MAX_VX;
	protected final double MAX_VY;
	protected final double ACC;
	protected final double DEC;
	
	//animation
	protected final Sprite[][] sprites;
	protected int state, frame;
	protected int animationTimer;
	protected int animationTimerLimit;
		
//======================================================================
//
//-----------------------------Constructors-----------------------------
//
//======================================================================
		
	public Entity(double maxvx, double maxvy, double acc, double dec, int animationSpeed, Sprite[][] sprites) {
		MAX_VX = maxvx;
		MAX_VY = maxvy;
		ACC = acc;
		DEC = dec;
		this.sprites = sprites;
		x = y = 0;
		vx = vy = ax = ay = 0;
		state = frame = 0;
		animationTimer = 0;
		animationTimerLimit = animationSpeed;
	}
	
	public Entity(double maxvx, double maxvy, Sprite[][] sprites) {
		MAX_VX = maxvx;
		MAX_VY = maxvy;
		ACC = 0;
		DEC = 0;
		this.sprites = sprites;
		x = y = 0;
		vx = vy = ax = ay = 0;
		state = frame = 0;
		animationTimer = 0;
		animationTimerLimit = Integer.MAX_VALUE;
	}
		
//======================================================================
//
//-------------------------------Methods--------------------------------
//
//======================================================================
		
	public abstract void update(double rate);
	public abstract void move(double rate);

	
	public void animate() {
		animationTimer++;
		if (animationTimer > animationTimerLimit) {
			frame = (frame + 1) % sprites[state].length;
			animationTimer = 0;
		}
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public int width() {
		return (int) getCurrentSprite().width();
	}
	
	public int height() {
		return (int) getCurrentSprite().height();
	}
	
//======================================================================
//
//---------------------------Getters/Setters----------------------------
//
//======================================================================
	
	public Sprite getCurrentSprite() {
		return sprites[state][frame];
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
}
