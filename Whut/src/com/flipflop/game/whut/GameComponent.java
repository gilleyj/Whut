package com.flipflop.game.whut;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

public abstract class GameComponent extends Canvas implements Runnable{
	private static final long serialVersionUID = 7896446093560185240L;
	private Dimension gameSize = new Dimension(500,500);
	protected Container parent = null;
	protected BufferStrategy bufferStrategy = null;
	protected World world = null;
	protected boolean gameRunning = false;
	
	public GameComponent(Container parent) {
		this.parent = parent;
	}

	public abstract void render(Graphics g);

	/**
	 * Update the game with a duration <code>tm</code> in milliseconds. This
	 * should be the method called within the game loop.
	 * 
	 * @param tm
	 *            The time in milliseconds to update the game with
	 */
	public abstract void update(long tm);

	/**
	 * Initialize any components the game has.  <code>super.init()</code> must be called <strong>after</strong> the owning window has been made visible.
	 */
	public void init() {
		this.parent.setIgnoreRepaint(true);
		this.createBufferStrategy(2);
		this.bufferStrategy = this.getBufferStrategy();
	}
	/**
	 * Let the game know that execution has begun.  Let the game take any necessary pre-cautions.
	 */
	public abstract void prepare();

	/**
	 * Let the game know that execution has stopped.  Let the game take any necessary pre-cautions.
	 */
	public abstract void stop();
	
	/**
	 * The entry point of the game.  This is the game loop.  Override this method to fashion your own game loop.  
	 */
	@Override
	public void run() {
		Graphics2D g = null;
		long lastTime = System.currentTimeMillis();
		long updateFPS = System.currentTimeMillis();
		this.gameRunning = true;
		
		while (this.gameRunning) {
			g = (Graphics2D) this.bufferStrategy.getDrawGraphics();
			
			long delta = System.currentTimeMillis() - lastTime;
			if (delta > (1000/65)) {
				lastTime = System.currentTimeMillis();
				g.setColor(Color.black);
				g.setBackground(Color.gray);
				g.clearRect(0, 0, this.getWidth(), this.getHeight());
				this.update(delta);
				this.render(g);
				if (System.currentTimeMillis() - updateFPS >= 1000) {
					System.out.println("FPS:"+(1000/delta));
					updateFPS = System.currentTimeMillis();
				}
				
			} else {
				try {
					Thread.sleep(0, 1000);
				} catch (InterruptedException e) {}
			}
			
			g.dispose();
			this.bufferStrategy.show();
		}
	}
	
	public void setGameSize(int width, int height) {
		this.gameSize = new Dimension(width, height);
	}
	public void setGameSize(Dimension d) {
		this.gameSize = d;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return this.gameSize;
	}
	
	public void changeWorld(World world) {
		this.world.stop();
		this.world = world;
		this.world.init();
		this.world.start();
	}
}
