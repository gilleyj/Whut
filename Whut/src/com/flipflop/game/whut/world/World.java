package com.flipflop.game.whut.world;

import java.awt.Graphics;

import com.flipflop.game.GameComponent;

public interface World {
	
	public void update(long tm);
	public void render(Graphics g);
	
	public GameComponent getGame();
	public void init();
	public void start();
	public void stop();
}
