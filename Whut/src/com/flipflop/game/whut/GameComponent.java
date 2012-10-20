package com.flipflop.game.whut;

import java.awt.BufferCapabilities;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.ImageCapabilities;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.util.logging.Logger;

import javax.vecmath.Vector2d;

public abstract class GameComponent extends Canvas implements Runnable {
	// Java Bullshit
	private static final long serialVersionUID = 7896446093560185240L;
	// Utility for managing loggable information
	private static final Logger logger = Logger.getLogger("com");
	// Target frames per second
	private static final int DEFAULT_FPS = 50;
	// Number of frames to analyze for averaging fps
	private static final int FPS_AVG_HISTORY = 20;
	// How often to check the average
	private static final int FPS_INTERVAL_MS = 1000;
	// How many milliseconds to sleep between frames
	private static final int SLEEP_MS = 5;
	// How many nanoseconds to sleep between frams (in addition to the
	// milliseconds)
	private static final int SLEEP_NS = 0;
	// The "container" of the drawable window (this, hopefully)
	protected Container parent = null;
	// The world object to update and render
	protected World world = null;
	// The manager of inputs
	protected InputManager im = null;
	// The buffer strategy is the "drawable" part of the drawable window
	private BufferStrategy bufferStrategy = null;
	// Ugh, just...I don't know. Don't use this.
	private VolatileImage volatileBuffer = null;
	// GraphicsConfiguration is like window settings
	private GraphicsConfiguration gc = null;
	// The drawable window's size
	private Dimension gameSize = new Dimension(500, 500);
	// Control for the game loop
	private boolean gameRunning = false;
	// Used to provide this information to subclasses.
	private int fps = 0;

	public GameComponent(Container parent) {
		System.setProperty("sun.java2d.opengl", "true");
		System.setProperty("apple.awt.graphics.UseQuartz", "true");
		System.setProperty("apple.awt.graphics.EnableQ2DX", "true");
		LogUtil.initRootLogger(logger);
		this.parent = parent;
		this.gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration();
		GraphicsDevice[] devices = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getScreenDevices();
		logger.info(devices.length + " graphics devices.");
		for (GraphicsDevice device : devices) {
			GraphicsConfiguration[] configurations = device.getConfigurations();
			logger.info("Device: " + device.getIDstring());
			logger.info("\tAccelerate memory: "
					+ device.getAvailableAcceleratedMemory());
			logger.info("\tSupport full-screen:"
					+ device.isFullScreenSupported());
			logger.info("\tConfigurations: " + configurations.length);
			logger.info("\tDefault config: " + device.getDefaultConfiguration());
			for (GraphicsConfiguration config : configurations) {
				BufferCapabilities bc = config.getBufferCapabilities();
				logger.info("\t\tIs fullscreen RQ'd: "
						+ bc.isFullScreenRequired());
				logger.info("\t\tIs multi-buffer AVLBL: "
						+ bc.isMultiBufferAvailable());
				logger.info("\t\tIs page flipping: " + bc.isPageFlipping());
				logger.info("\t\tConfiguration (BACK): " + config.toString());
				ImageCapabilities bb = config.getBufferCapabilities()
						.getBackBufferCapabilities();
				logger.info("\t\t\tIs accelerated: " + bb.isAccelerated());
				logger.info("\t\t\tIs volatile: " + bb.isTrueVolatile());
				logger.info("\t\tConfiguration (FRONT): " + config.toString());
				ImageCapabilities fc = config.getBufferCapabilities()
						.getFrontBufferCapabilities();
				logger.info("\t\t\tIs accelerated: " + fc.isAccelerated());
				logger.info("\t\t\tIs volatile: " + fc.isTrueVolatile());
			}
		}
		this.im = new InputManager(this);
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
		this.createBufferStrategy(2);
		this.bufferStrategy = this.getBufferStrategy();
		this.volatileBuffer = this.gc.createCompatibleVolatileImage(
				this.getWidth(), this.getHeight());
		this.volatileBuffer.setAccelerationPriority(1.F);

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
		long lastUpdateFPS = System.currentTimeMillis();
		long[] avgDelta = new long[FPS_AVG_HISTORY];
		int frameCount = 0;
		long deltaTarget = 1000 / DEFAULT_FPS;

		this.gameRunning = true;

		g = (Graphics2D) this.bufferStrategy.getDrawGraphics();
		// g = this.volatileBuffer.createGraphics();
		while (this.gameRunning) {
			long delta = System.currentTimeMillis() - lastTime;
			if (delta >= deltaTarget) {
				lastTime = System.currentTimeMillis();
				g.setColor(Color.black);
				g.setBackground(Color.gray);
				g.clearRect(0, 0, this.getWidth(), this.getHeight());
				this.update(delta);
				this.render(g);
				renderDebug(g);
				avgDelta[(frameCount++) % FPS_AVG_HISTORY] = 1000 / delta;
				if (System.currentTimeMillis() - lastUpdateFPS >= FPS_INTERVAL_MS) {
					int avg = 0;
					for (long val : avgDelta)
						avg += val;
					this.fps = avg = avg / FPS_AVG_HISTORY;
					// System.out.println("FPS:" + this.fps);
					lastUpdateFPS = System.currentTimeMillis();
				}
			} else {
				try {
					Thread.sleep(SLEEP_MS, SLEEP_NS);
				} catch (InterruptedException e) {
				}
			}
			this.bufferStrategy.show();
		}
	}

	private void renderDebug(Graphics2D g) {
		int x = this.getWidth() / 2;
		int y = this.getHeight() / 2;
		Vector2d vector = (Vector2d) this.im.mouseInput.mouseInfo.velocity.clone();
		vector.scale(0.5D);
		g.setColor(Color.GREEN);
		g.drawLine(x, y, (int) this.im.mouseInput.mouseInfo.velocity.x + x,
				(int) this.im.mouseInput.mouseInfo.velocity.y + y);
		g.drawString("Mouse vector: (" + vector.x + ", " + vector.y + ")", 0,
				40);
		String mouseState = "";
		switch (this.im.mouseInput.mouseInfo.state) {
		case LEFT_CLICKING:
			mouseState = "LEFT DOWN";
			break;
		case LEFT_RIGHT_CLICKING:
			mouseState = "LEFT AND RIGHT DOWN";
			break;
		case MIDDLE_CLICKING:
			mouseState = "MIDDLE DOWN";
			break;
		case RELEASED:
			mouseState = "NOTHING DOWN";
			break;
		case RIGHT_CLICKING:
			mouseState = "RIGHT DOWN";
			break;
		}
		g.setColor(Color.RED);
		g.drawString(mouseState, 0, 60);
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

	public int getFPS() {
		return this.fps;
	}
}
