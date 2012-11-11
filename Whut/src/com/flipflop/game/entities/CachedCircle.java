package com.flipflop.game.entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class CachedCircle extends Circle {
	
	protected BufferedImage bi;

	public CachedCircle(BufferedImage bi, double radius, double x, double y) {
		super(radius, radius, radius);
		this.initialize(bi);
		super.x = x;
		super.y = y;
	}
	
	public CachedCircle(BufferedImage bi, float radius) {
		super(radius);
		this.initialize(bi);
	}
	
	private void initialize(BufferedImage bi) {
		this.bi = bi;
		super.render(this.bi.createGraphics());
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(this.bi, null, (int) this.x, (int) this.y);
	}

}
