package com.flipflop.game.whut.entities;

import java.awt.Graphics;


public interface Entity {
	public void render(Graphics g);
	public void update(long tm);
}
