package com.flipflop.game.animator;

import com.flipflop.game.entities.Entity;

public class Lifeline extends BaseAnimator {
	private long lifeInMilli = 0;
	private long lifeBeganAt = 0;

	public Lifeline(Entity target, long lifeInMilliseconds) {
		super(target);
		this.lifeInMilli = lifeInMilliseconds;
	}

	@Override
	public void update(long tm) {
		if (this.isRunning()) {
			this.lifeInMilli -= System.currentTimeMillis() - this.lifeBeganAt;
			if (this.lifeInMilli <= 0) {
				target.kill();
			}
		}
	}

	@Override
	public void start() {
		this.lifeBeganAt = System.currentTimeMillis();
		this.startRunning();
	}

	@Override
	public void stop() {
		this.stopRunning();
		super.done();
	}

	@Override
	public void pause() {
		this.stopRunning();
	}
	
}
