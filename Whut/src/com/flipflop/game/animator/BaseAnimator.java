package com.flipflop.game.animator;

import com.flipflop.game.entities.Entity;

public abstract class BaseAnimator implements Animator {
	protected Entity target = null;
	protected boolean isRunning = false;
	protected boolean isDone = false;
	
	public BaseAnimator(Entity target) {
		this.target = target;
	}
	
	@Override
	public boolean isRunning() {
		return this.isRunning;
	}
	
	@Override
	public boolean isDone() {
		return this.isDone;
	}
	
	public void done() {
		this.isDone = true;
	}
	
	protected void stopRunning() {
		this.isRunning = false;
	}
	
	protected void startRunning() {
		this.isRunning = true;
	}
}
