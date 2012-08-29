package com.flipflop.game.whut;

import java.awt.Graphics;

public interface World {
	
	public void update(long tm);
	public void render(Graphics g);
	
	public GameComponent getGame();
	public void init();
	public void start();
	public void stop();
}
