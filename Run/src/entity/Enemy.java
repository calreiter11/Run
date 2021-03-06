package entity;

import graphics.Sprite;

public class Enemy extends Entity {

//======================================================================
//
//--------------------------------Fields--------------------------------
//
//======================================================================
	
	private double gameHeight;

//======================================================================
//
//-----------------------------Constructors-----------------------------
//
//======================================================================
	
	public Enemy(double maxvx, double maxvy, double acc, double gameHeight, Sprite[][] sprites) {
		super(maxvx, maxvy, acc, acc, Integer.MAX_VALUE, sprites);
		this.gameHeight = gameHeight;
		vx = -MAX_VX;
	}
	
//======================================================================
//
//-------------------------------Methods--------------------------------
//
//======================================================================
	
	public void update(double rate) {
		move(rate);
	}
	
	public void move(double rate) {
		if (y < gameHeight / 2) {
			vy += ACC;
		} else if (y > gameHeight / 2) {
			vy -= ACC;
		}
		
		x += vx * rate;
		y += vy;		
	}

//======================================================================
//
//---------------------------Getters/Setters----------------------------
//
//======================================================================
	
}
