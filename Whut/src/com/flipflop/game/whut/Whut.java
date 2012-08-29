package com.flipflop.game.whut;

import java.awt.Container;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Whut extends GameComponent {

	private static final long serialVersionUID = 8303829757030771706L;
	private static final String TITLE = "Whut";
	
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
		super.world.render(g);
		g.drawString("FPS: "+String.valueOf(fps), 0, 20);
	}
	
	private static float fps;
	@Override
	public void update(long tm) {
		fps = 1000/tm;
		super.world.update(tm);
	}

	@Override
	public void init() {
		super.init();
		super.setGameSize(512, 512);
		super.parent.addComponentListener(new WhutWindowListener(this));
		super.world = new WhutWorld(this);
		super.world.init();
	}

	@Override
	public void prepare() {
		super.world.start();
	}

	@Override
	public void stop() {
		super.world.stop();
	}
}
