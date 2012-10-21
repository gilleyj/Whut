package com.flipflop.game.whut.entities;

import java.awt.Graphics;

import com.flipflop.game.whut.world.World;

public abstract class BaseEntity implements Entity {
	
	protected World world;
	protected int xPos = 0, yPos = 0;
	
	public BaseEntity(World world) {
		this.world = world;
	}

	@Override
	public abstract void render(Graphics g);

	@Override
	public abstract void update(long tm);

}
