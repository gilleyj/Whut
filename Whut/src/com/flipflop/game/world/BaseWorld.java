package com.flipflop.game.world;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.vecmath.Vector2d;

import com.flipflop.game.GameComponent;
import com.flipflop.game.entities.Ball;
import com.flipflop.game.entities.DeathHook;
import com.flipflop.game.entities.Entity;

public class BaseWorld implements World, DeathHook {
	private static final Logger logger = Logger.getLogger(BaseWorld.class.getName());
	protected GameComponent gc = null;
	protected List<Entity> entities = new LinkedList<Entity>();
	protected List<Entity> dyingEntities = new LinkedList<Entity>();
	protected List<Entity> physicalEntities = new LinkedList<Entity>();
	
	public BaseWorld(GameComponent gc) {
		this.gc = gc;
	}
	
	@Override
	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}
	
	@Override
	public Entity[] getEntities() {
		return this.entities.toArray(new Entity[this.entities.size()]);
	}
	
	@Override
	public Entity[] getPhysicalEntities() {
		return this.physicalEntities.toArray(new Entity[this.entities.size()]);
	}

	@Override
	public void update(long tm) {
		if (this.gc.getInputManager().keyboardInput.isKeyPulsed(KeyEvent.VK_B)) {
			spawnBalls(100);
		}
		for (Entity entity : this.dyingEntities) {
			this.entities.remove(entity);
			logger.info("Buried " + entity.getName());
		}
		this.dyingEntities.clear();
		for(Entity entity : this.entities) {
			entity.update(tm);
			if (entity.isDead()) {
				this.dyingEntities.add(entity);
			}
		}
	}

	@Override
	public void render(Graphics g) {
		for(Entity entity : this.entities) {
			entity.render(g);
		}
		Graphics2D debug = (Graphics2D) this.gc.getDebugLayer();
		debug.drawString(String.valueOf(this.entities.size()), this.gc.getWidth() - 50, 20);
	}

	@Override
	public void init() {
		spawnBalls(1000);
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Override
	public GameComponent getGame() {
		return this.gc;
	}

	@Override
	public void notifyReaper(Entity entity) {
		this.dyingEntities.add(entity);
	}
	
	private void spawnBall(int x, int y) {
		Ball ball = new Ball(this);
		ball.addForce(new Vector2d(0.0D, 98.1D));
		ball.moveTo(x, y);
		this.addEntity(ball);
	}
	
	private void spawnBalls(int count) {
		Random rand = new Random(System.currentTimeMillis());
		for (int i=0; i<count; i++) {
			spawnBall((int) (rand.nextFloat() * this.gc.getWidth()), (int) (rand.nextFloat() * this.gc.getHeight()));
		}
	}

}
