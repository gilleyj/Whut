package com.flipflop.game.entities;

import java.awt.Graphics;
import java.util.logging.Logger;

import com.flipflop.game.world.World;

public class Ball extends PhysicalEntity {
	private static final Logger logger = Logger.getLogger(Ball.class.getName());
	protected float radius = 50.F;
	public Circle circle = new Circle(this.radius);

	public Ball(World world) {
		super(world);
		super.name = "Ball";
		circle.setX(super.xPos);
		circle.setY(super.yPos);
	}
	
	@Override
	public void update(long tm) {
		if (!super.isVisible()) this.kill();
		super.update(tm);
		this.circle.setRadius(this.radius);
		this.circle.setX(super.xPos);
		this.circle.setY(super.yPos);
	}

	@Override
	public void render(Graphics g) {
		this.circle.render(g);
	}

	@Override
	protected void prepareToDie() {
		logger.warning("I'm dying! -"+super.name);
	}

	@Override
	protected boolean isReadyForDeath() {
		return true;
	}
}
