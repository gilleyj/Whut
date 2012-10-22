package com.flipflop.game.entities;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.flipflop.game.animator.Animator;
import com.flipflop.game.world.World;

public abstract class BaseEntity implements Entity {

	protected World world;
	protected String name = "Basic Entity";
	protected double xPos = 0, yPos = 0, width = 0, height = 0;
	protected List<DeathHook> deathHooks = null;
	protected List<Animator> animators = null;
	private boolean isDead;

	public BaseEntity(World world) {
		this.world = world;
		this.deathHooks = new LinkedList<DeathHook>();
		this.animators = new LinkedList<Animator>();
	}

	@Override
	public abstract void render(Graphics g);

	@Override
	public void update(long tm) {
		Iterator<Animator> iter = this.animators.iterator();
		while (iter.hasNext()) {
			Animator anim = iter.next();
			anim.update(tm);
			if (anim.isDone()) {
				iter.remove();
			}
		}
	}

	protected abstract void prepareToDie();

	protected abstract boolean isReadyForDeath();

	protected double getVisibleWidth() {
		return this.width;
	}

	protected double getVisibleHeight() {
		return this.height;
	}

	@Override
	public void moveTo(int x, int y) {
		this.xPos = x;
		this.yPos = y;
	}

	@Override
	public boolean isVisible() {
		if (this.xPos + this.width * 0.5 < 0)
			return false;
		else if (this.xPos - this.width * 0.5 > this.world.getGame().getWidth())
			return false;
		else if (this.yPos + this.height * 0.5 < 0)
			return false;
		else if (this.yPos - this.height * 0.5 > this.world.getGame()
				.getHeight())
			return false;
		else
			return true;
	}

	@Override
	public void addAnimator(Animator anim) {
		this.animators.add(anim);
	}

	@Override
	public void addDeathHook(DeathHook dh) {
		this.deathHooks.add(dh);
	}

	@Override
	public boolean kill() {
		this.prepareToDie();
		if (this.isReadyForDeath()) {
			for (DeathHook dh : this.deathHooks) {
				dh.notifyReaper(this);
			}
			this.isDead = true;
		}
		return this.isDead;
	}
	
	@Override
	public boolean isDead() {
		return this.isDead;
	}

	@Override
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

}
