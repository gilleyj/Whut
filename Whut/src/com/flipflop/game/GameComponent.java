package com.flipflop.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.util.logging.Logger;

import javax.vecmath.Vector2d;

import com.flipflop.game.whut.input.InputManager;
import com.flipflop.util.LogUtil;
public abstract class GameComponent extends Canvas implements Runnable {
	/** CONSTANTS **/
	// Java Bullshit
	private static final long serialVersionUID = 7896446093560185240L;
	// Utility for managing loggable information
	private static final Logger logger = Logger.getLogger("com");
	// Target frames per second
	private static final int DEFAULT_FPS = 100;
	// Number of frames to analyze for averaging fps
	private static final int FPS_AVG_HISTORY = 20;
	// How often to check the average
	private static final int FPS_INTERVAL_MS = 1000;
	// How many milliseconds to sleep between frames
	private static final int SLEEP_MS = 5;
	// How many nanoseconds to sleep between frames (in addition to the
	// milliseconds)
	private static final int SLEEP_NS = 0;
	
	/** VARIABLES **/
	// The "container" of the drawable window (this, hopefully)
	protected Container parent = null;
	// The manager of inputs
	protected InputManager im = null;
	// The buffer strategy is the "drawable" part of the drawable window
	private BufferStrategy bufferStrategy = null;
	// Ugh, just...I don't know. Don't use this.
	// private VolatileImage volatileBuffer = null;
	// GraphicsConfiguration is like window settings. Keep around for debugging
	// for whenever.
	@SuppressWarnings("unused")
	private GraphicsConfiguration gc = null;
	// The drawable window's size
	private Dimension gameSize = new Dimension(500, 500);
	// Control for the game loop
	private boolean gameRunning = false;
	// Used to provide this information to subclasses.
	private int fps = 0;
	// Clear color used, exposed through getter
	private Color bgColor = Color.gray;
	// Game's default font
	private Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);

	public GameComponent(Container parent) {
		// Set up this class's logger as the root and attach a simple console
		// format
		LogUtil.initRootLogger(logger);
		this.parent = parent;
		this.gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration();

		// Attach the input manager to the canvas because that's what the user
		// will interact with.
		this.im = new InputManager(this);
	}

	/**
	 * A game should implement this function to draw into the Graphics object,
	 * including all entities in the world. This will be called periodically,
	 * with no promise of time elapsed. Any logic should be implemented in
	 * {@link GameComponent#update(long)}.
	 * 
	 * @param g
	 *            The arbitrary drawing interface that needs to be drawn on.
	 */
	public abstract void render(Graphics g);

	/**
	 * Update the game with a duration <code>tm</code> in milliseconds. This
	 * will be the method called within the game loop. Update any business logic
	 * in this method.
	 * 
	 * @param tm
	 *            The time in milliseconds to update the game with
	 */
	public abstract void update(long tm);

	/**
	 * Initialize any components the game has. {@link #init()} must be called
	 * <strong>after</strong> the owning window (indicated by the constructor
	 * {@link #GameComponent(Container)}) has been made visible.
	 */
	public void init() {
		this.parent.setIgnoreRepaint(true);
		this.setIgnoreRepaint(true);
		this.createBufferStrategy(2);
		this.bufferStrategy = this.getBufferStrategy();
		this.setFont(this.font);
		// this.volatileBuffer = this.gc.createCompatibleVolatileImage(
		// this.getWidth(), this.getHeight());
		// this.volatileBuffer.setAccelerationPriority(1.F);

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
		// Used for calculating the time between frames
		long lastTime = System.currentTimeMillis();
		
		// Used for calculating the time between FPS reporting
		long lastUpdateFPS = System.currentTimeMillis();
		
		// Frame delta history for averaging FPS
		long[] avgDelta = new long[FPS_AVG_HISTORY];
		
		// Indexing into the fps history
		int frameCount = 0;
		
		// The target delta so we know when we can sleep or when we're late.
		long deltaTarget = 1000 / DEFAULT_FPS;

		// We are now "running" the game loop.
		this.gameRunning = true;

		// Get the drawable area from the drawable part of the window's buffer.
		Graphics2D g = (Graphics2D) this.bufferStrategy.getDrawGraphics();
		g.setFont(this.font);
		// g = this.volatileBuffer.createGraphics();
		
		while (this.gameRunning) {
			// Time since last frame
			long delta = System.currentTimeMillis() - lastTime;
			
			if (delta >= deltaTarget) {
				// Record when we create a frame
				lastTime = System.currentTimeMillis();
				
				// Set up default colors for drawing.
				g.setColor(Color.black);
				g.setBackground(bgColor);
				// Clear drawing area so no "leftovers".
				g.clearRect(0, 0, this.getWidth(), this.getHeight());
				
				// Poll the inputs (some are synchronous, some are asynchronous...)
				this.im.poll();
				// Update the game with time elapsed
				this.update(delta);
				// Then render the updated game.
				this.render(g);
				// Then render some debug shit on top.
				renderDebug(g);
				
				// Record this frames delta in seconds
				avgDelta[(frameCount++) % FPS_AVG_HISTORY] = 1000 / delta;
				if (System.currentTimeMillis() - lastUpdateFPS >= FPS_INTERVAL_MS) {
					int avg = 0;
					for (long val : avgDelta)
						avg += val;
					this.fps = avg / FPS_AVG_HISTORY;
					// System.out.println("FPS:" + this.fps);
					lastUpdateFPS = System.currentTimeMillis();
				}
			} else {
				// Don't be a jerk, give some time back.
				try {
					Thread.sleep(SLEEP_MS, SLEEP_NS);
				} catch (InterruptedException e) {
				}
			}
			// You have to do this for reasons.
			this.bufferStrategy.show();
		}
	}

	private void renderDebug(Graphics2D g) {
		int x = this.getWidth() / 2;
		int y = this.getHeight() / 2;
		Vector2d vector = this.im.mouseInput.getAverageMouseVector();
//		vector.scale(10.D);
		g.setColor(Color.GREEN);
		g.drawLine(x, y, (int) vector.x + x, (int) vector.y + y);
		g.drawString("Mouse vector: (" + vector.x + ", " + vector.y + ")", 0,
				40);
		String mouseState = this.im.mouseInput.mouseStatus();
		
		g.setColor(Color.RED);
		g.drawString(mouseState, 0, 60);
	}

	public void setGameSize(int width, int height) {
		this.setGameSize(new Dimension(width, height));
	}

	public void setGameSize(Dimension d) {
		this.gameSize = d;
	}

	@Override
	public Dimension getPreferredSize() {
		return this.gameSize;
	}

	public int getFPS() {
		return this.fps;
	}

	public void setBackgroundColor(Color color) {
		if (color != null) {
			this.bgColor = color;
		} else {
			logger.warning("Attempted to set background color to null. Why would you do that?");
		}
	}

	public Color getBackgroundColor() {
		return this.bgColor;
	}
}
