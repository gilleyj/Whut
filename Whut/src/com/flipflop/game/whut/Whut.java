package com.flipflop.game.whut;

import java.awt.Container;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.flipflop.game.GameComponent;
import com.flipflop.game.whut.world.WhutWorld;
import com.flipflop.game.whut.world.World;

public class Whut extends GameComponent {

	private static final long serialVersionUID = 8303829757030771706L;
	private static final String TITLE = "Whut";
	// The world object to update and render
	protected World world = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JFrame frame = new JFrame(Whut.TITLE);
		frame.setLocation(20, 20);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		Whut game = new Whut(frame.getContentPane());

		frame.getContentPane().add("Center", game);
		frame.pack();
		frame.setVisible(true);

		game.init();
		game.prepare();

		Thread gameThread = new Thread(game, "GameLoop");
		gameThread.start();

	}

	public Whut(Container parent) {
		super(parent);
	}

	@Override
	public void render(Graphics g) {
		this.world.render(g);
		g.drawString("FPS: " + String.valueOf(super.getFPS()), 0, 20);
	}

	@Override
	public void update(long tm) {
		this.world.update(tm);
	}

	@Override
	public void init() {
		super.init();
		super.setGameSize(512, 512);
		super.parent.addComponentListener(new WhutWindowListener(this));
		super.setFocusTraversalKeysEnabled(false);
		this.world = new WhutWorld(this);
		this.world.init();
	}

	@Override
	public void prepare() {
		super.parent.requestFocusInWindow();
		this.world.start();
	}

	@Override
	public void stop() {
		this.world.stop();
	}
}
