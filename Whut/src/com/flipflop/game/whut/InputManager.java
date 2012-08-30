package com.flipflop.game.whut;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputManager implements KeyListener{
	
	public InputManager() {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("Pressed " + KeyEvent.getKeyText(e.getID()));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println("Released " + KeyEvent.getKeyText(e.getID()));
	}

	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println("Typed " + KeyEvent.getKeyText(e.getID()));
	}

}
