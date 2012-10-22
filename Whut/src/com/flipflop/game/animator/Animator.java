package com.flipflop.game.animator;

public interface Animator {
	public void start();
	public boolean isRunning();
	public void stop();
	public void pause();
	public boolean isDone();
	public void update(long tm);
}
