package com.flipflop.game.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.vecmath.Vector2d;

import com.flipflop.game.GameComponent;
import com.flipflop.game.entities.PrettyCircle;
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
		if (this.gc.getInputManager().keyboardInput.isKeyPulsed(KeyEvent.VK_O)) {
			spawnBall();
		}
		for(Entity entity : this.entities) {
			entity.update(tm);
			if (entity.isDead()) {
				this.dyingEntities.add(entity);
			}
		}
		for (Entity entity : this.dyingEntities) {
			this.entities.remove(entity);
			logger.info("Buried " + entity.getName());
		}
		this.dyingEntities.clear();
		Collections.sort(this.entities, new Comparator<Entity>() {

			@Override
			public int compare(Entity o1, Entity o2) {
				return o1.getDepth() > o2.getDepth() ? 1 : 
						o1.getDepth() < o2.getDepth() ? -1 : 0;
			}
			
		});
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
	
	private PrettyCircle spawnBall(int x, int y) {
		PrettyCircle ball = new PrettyCircle(this);
		ball.addForce(new Vector2d(0.0D, 98.1D));
		ball.moveTo(x, y);
		this.addEntity(ball);
		return ball;
	}
	
	private PrettyCircle spawnBall() {
		Random rand = new Random(System.currentTimeMillis());
		PrettyCircle ball = spawnBall((int) (rand.nextFloat() * this.gc.getWidth()), (int) (rand.nextFloat() * this.gc.getHeight()));
		ball.addVelocity(new Vector2d(rand.nextDouble()*100-50, rand.nextDouble()*100-50));
		ball.setColor(Color.red);
		ball.setDepth(2);
		
		return ball;
	}
	
	private void spawnBalls(int count) {
		Random rand = new Random(System.currentTimeMillis());
		for (int i=0; i<count; i++) {
			PrettyCircle ball = spawnBall((int) (rand.nextFloat() * this.gc.getWidth()), (int) (rand.nextFloat() * this.gc.getHeight()));
			ball.addVelocity(new Vector2d(rand.nextDouble()*100-50, rand.nextDouble()*100-50));
		}
		PrettyCircle ball = spawnBall((int) (rand.nextFloat() * this.gc.getWidth()), (int) (rand.nextFloat() * this.gc.getHeight()));
		ball.addVelocity(new Vector2d(rand.nextDouble()*100-50, rand.nextDouble()*100-50));
		ball.setColor(Color.blue);
		ball.setDepth(1);
	}

}
