package com.flipflop.game.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Transparency;
import java.util.logging.Logger;

import com.flipflop.game.world.World;

public class PrettyCircle extends PhysicalEntity {
	private static final Logger logger = Logger.getLogger(PrettyCircle.class.getName());
	protected float radius = 50.F;
	public Circle circle;

	public PrettyCircle(World world) {
		super(world);
		super.name = this.getClass().getSimpleName();
		super.width = this.radius;
		super.height = this.radius;
		this.circle = new CachedCircle(
				this.world.getGame()
				.getGraphicsConfiguration()
				.createCompatibleImage((int)(radius*2), (int)(radius*2), Transparency.TRANSLUCENT), 
				this.radius,
				super.xPos,
				super.yPos);
	}
	
	@Override
	public void update(long tm) {
		super.update(tm);
		if (!super.isVisible()) this.kill();
		//this.circle.setRadius(this.radius);
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
	
	public void setColor(Color color) {
		this.circle.setFillColor(color);
	}
}
