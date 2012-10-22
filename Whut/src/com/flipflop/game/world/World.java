package com.flipflop.game.world;

import java.awt.Graphics;

import com.flipflop.game.GameComponent;
import com.flipflop.game.entities.Entity;

public interface World {
	
	public void update(long tm);
	public void render(Graphics g);
	
	public GameComponent getGame();
	public void addEntity(Entity entity);
	public Entity[] getEntities();
	public Entity[] getPhysicalEntities();
	public void init();
	public void start();
	public void stop();
}
