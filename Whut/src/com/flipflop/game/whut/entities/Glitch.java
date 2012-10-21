package com.flipflop.game.whut.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.flipflop.game.whut.world.World;

public class Glitch extends BaseEntity {
	private BufferedImage bi = null;
	
	public Glitch(World world) {
		super(world);
		
		this.world = world;
		this.bi = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		
		Color colors[] = new Color[16];
		for (int i=0; i < 16; i++) {
			float sample = (float) Math.random();
			colors[i] = new Color(sample, sample, sample);
		}
		int[] pixels = ((DataBufferInt)this.bi.getRaster().getDataBuffer()).getData();
		for (int i=0; i<64; i++) {
			for (int j=0; j<64; j++) {
//				 TODO: The math for indexing the colors is broken. Fix it.
				pixels[i*64 + j] = colors[(i/8+j%8)].getRGB();
			}
		}
		
		this.xPos = (int) (this.world.getGame().getWidth() * Math.random());
		this.yPos = (int) (this.world.getGame().getHeight() * Math.random());
	}
	@Override
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(this.bi, null, this.xPos, this.yPos);
	}

	@Override
	public void update(long tm) {
		int xDelta = Math.random()>0.5d?1:-1;
		int yDelta = Math.random()>0.5d?1:-1;
		
		this.xPos += xDelta;
		this.yPos += yDelta;
	}

}
