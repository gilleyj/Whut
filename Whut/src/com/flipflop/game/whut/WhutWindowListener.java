package com.flipflop.game.whut;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class WhutWindowListener implements ComponentListener {
	private Whut whutGame;
	
	public WhutWindowListener(Whut whut) {
		this.whutGame = whut;
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent e) {
		whutGame.setGameSize(whutGame.getParent().getSize());
		whutGame.setSize(whutGame.getParent().getSize());
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

}
