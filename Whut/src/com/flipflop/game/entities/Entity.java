package com.flipflop.game.entities;

import java.awt.Graphics;

import com.flipflop.game.animator.Animator;


public interface Entity {
	public void render(Graphics g);
	public void update(long tm);
	public void moveTo(int x, int y);
	public boolean isVisible();
	public void addAnimator(Animator anim);
	public void addDeathHook(DeathHook dh);
	public boolean kill();
	public boolean isDead();
	public String getName();
}
