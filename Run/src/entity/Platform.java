package entity;

import graphics.Sprite;

public class Platform extends Entity {

	private static final double MAX_VX = 1;
	private static final double MAX_VY = 1;
	
	private static final double ACC = 0.5;
	private static final double DEC = 0.5;
	
	private static final int ANIMATION_SPEED = 1;
	
	public Platform(double x, double y, Sprite[][] sprites) {
		super(MAX_VX, MAX_VY, ACC, DEC, ANIMATION_SPEED, sprites);
		this.x = x;
		this.y = y;
	}

	@Override
	public void update(double rate) {
		move(rate);
	}

	@Override
	public void move(double rate) {
		x -= MAX_VX * rate;
	}

}
