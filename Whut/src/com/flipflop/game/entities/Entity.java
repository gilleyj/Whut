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

	/**
	 * Sets the order in which this entity is drawn in reference to all other
	 * drawn entities, in which a higher <code>depth</code> value means it is
	 * drawn "closer" to the screen.
	 * 
	 * @param depth
	 *            The zIndex to be applied to this object. A higher value will
	 *            be drawn on top of lower values.
	 */
	public void setDepth(int depth);
	
	public int getDepth();

	public void moveBack();

	public void moveForward();
}
