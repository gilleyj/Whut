package com.flipflop.game.entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;

import com.flipflop.game.world.World;

public class FractalTerrain extends BaseEntity {
	private int width;
	protected float[] array = null;
	protected BufferedImage bi = null;
	protected boolean dirty = true;
	protected Dimension oldWorldDimension;

	public FractalTerrain(World world, int width) {
		super(world);
		this.oldWorldDimension = this.world.getGame().getSize();
		int closestWidth = 2;
		while (closestWidth * 2 <= width) {
			closestWidth *= 2;
		}
		this.width = closestWidth;
		this.array = new float[(this.width + 1) * (this.width + 1)];

		this.bi = new BufferedImage(ColorModel.getRGBdefault(), WritableRaster.createBandedRaster(BufferedImage.TYPE_INT_ARGB,  
				this.world.getGame().getWidth(), this.world.getGame().getHeight(), 4, new Point(0,0)),
				false, null);
		Fractal.fill2DFractal(this.array, this.width, 1, 1, 0.8f);
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (dirty) {
			int gameWidth = this.oldWorldDimension.width;
			int gameHeight = this.oldWorldDimension.height;
			int tileWidth = (int) gameWidth / this.width;
			int tileHeight = (int) gameHeight / this.width;
			int xTileIndex = 0;
			int yTileIndex = 0;
			Graphics pixels = bi.getGraphics();
			for (int i = 0; i < gameHeight; i++) {
				yTileIndex = (int) (Math.floor(i / tileHeight) % this.width);
				for (int j = 0; j < gameWidth; j++) {
					xTileIndex = (int) (Math.floor(j / tileWidth) % this.width);
					int blackValue = (int) (2 * Byte.MAX_VALUE * (0.5f + this.array[(this.width
							* yTileIndex + xTileIndex)]));
					int rgb = 0xff; // ALPHA
					rgb = (rgb << 8) + blackValue; // RED
					rgb = (rgb << 8) + blackValue; // BLUE
					rgb = (rgb << 8) + blackValue; // GREEN
					pixels.setColor(new Color(rgb, true));
					pixels.drawRect(j, i, 1, 1);
				}
			}
			pixels.dispose();
			dirty = false;
		}
		g2.drawImage(bi, 0, 0, null);

	}

	@Override
	public void update(long tm) {
		if (this.oldWorldDimension.width != this.world.getGame().getSize().width
				|| this.oldWorldDimension.height != this.world.getGame().getSize().height) {
			this.dirty = true;
			this.oldWorldDimension = this.world.getGame().getSize();
		}
		int[] data = ((DataBufferInt) this.bi.getRaster().getDataBuffer()).getData();
		int change = -1;
		if (Math.random() > 0.5f)
			change = 1;
		for (int i = 0; i < data.length; i++) {
			data[i] += change;
		}
	}

	@Override
	protected void prepareToDie() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isReadyForDeath() {
		// TODO Auto-generated method stub
		return false;
	}
}
