package com.flipflop.game.whut;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

public class WhutWorld implements World {
	private GameComponent gc = null;
	private List<Entity> entities = new LinkedList<Entity>();
	
	public WhutWorld(GameComponent gc) {
		this.gc = gc;
	}

	@Override
	public void update(long tm) {
		for(Entity entity : this.entities) {
			entity.update(tm);
		}
	}

	@Override
	public void render(Graphics g) {
		for(Entity entity : this.entities) {
			entity.render(g);
		}
	}

	@Override
	public void init() {
		this.entities.add(new FractalTerrain(this, 64));
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public GameComponent getGame() {
		// TODO Auto-generated method stub
		return this.gc;
	}

}
