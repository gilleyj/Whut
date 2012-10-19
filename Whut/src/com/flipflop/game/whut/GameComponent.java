package com.flipflop.game.whut;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;

public abstract class GameComponent extends Canvas implements Runnable {
	private static final long serialVersionUID = 7896446093560185240L;
	private static final int DEFAULT_FPS = 60;
	protected Container parent = null;
	protected World world = null;
	protected InputManager im = null;
	protected BufferStrategy bufferStrategy = null;
	protected VolatileImage volatileBuffer = null;
	protected GraphicsConfiguration gc = null;
	private Dimension gameSize = new Dimension(500, 500);
	protected boolean gameRunning = false;

	public GameComponent(Container parent) {
		this.parent = parent;
		this.gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice()
				.getDefaultConfiguration();
		this.im = new InputManager(this.parent);
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
	 * Initialize any components the game has. <code>.init()</code> must be
	 * called <strong>after</strong> the owning window has been made visible.
	 */
	public void init() {
		this.parent.setIgnoreRepaint(true);
		this.setIgnoreRepaint(true);
		this.createBufferStrategy(1);
		this.bufferStrategy = this.getBufferStrategy();
		this.volatileBuffer = this.gc.createCompatibleVolatileImage(
				this.getWidth(), this.getHeight());
		
		System.setProperty("sun.java2d.opengl", "true");
		System.setProperty("apple.awt.graphics.UseQuartz","true");
		System.setProperty("apple.awt.graphics.EnableQ2DX","true");
	}

	/**
	 * Let the game know that execution has begun. Let the game take any
	 * necessary pre-cautions.
	 */
	public abstract void prepare();

	/**
	 * Let the game know that execution has stopped. Let the game take any
	 * necessary pre-cautions.
	 */
	public abstract void stop();

	/**
	 * The entry point of the game. This is the game loop. Override this method
	 * to fashion your own game loop.
	 */
	@Override
	public void run() {
		Graphics2D g = null;
		long lastTime = System.currentTimeMillis();
		long updateFPS = System.currentTimeMillis();

		this.gameRunning = true;

		while (this.gameRunning) {
			g = (Graphics2D) this.bufferStrategy.getDrawGraphics();
			g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
			g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
			long delta = System.currentTimeMillis() - lastTime;
			if (delta > (1000 / DEFAULT_FPS)) {
				lastTime = System.currentTimeMillis();
				g.setColor(Color.black);
				g.setBackground(Color.gray);
				g.clearRect(0, 0, this.getWidth(),
						this.getHeight());
				this.update(delta);
				this.render(g);
				if (System.currentTimeMillis() - updateFPS >= 5000) {
					System.out.println("FPS:" + (1000 / delta));
					updateFPS = System.currentTimeMillis();
				}
			} else {
				try {
					Thread.sleep(5, 0);
				} catch (InterruptedException e) {
				}
			}

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
