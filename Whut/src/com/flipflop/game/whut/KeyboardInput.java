package com.flipflop.game.whut;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {
	
	public enum KeyState {
		PRESSED_ONCE,
		PRESSED,
		RELEASED;
	}
	
	private static final int KEY_COUNT = 256;
	private KeyState[] keyStates = new KeyState[KEY_COUNT];
	
	public KeyboardInput() {
		for (int i=0; i<KEY_COUNT; i++) {
			keyStates[i] = KeyState.RELEASED;
		}
	}
	
	public boolean keyPressed(int keyCode) {
		return false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code >= 0 && code < KEY_COUNT) {
			if (keyStates[code] == KeyState.PRESSED_ONCE)
				keyStates[code] = KeyState.PRESSED;
			else if (keyStates[code] == KeyState.RELEASED)
				keyStates[code] = KeyState.PRESSED_ONCE;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code >= 0 && code < KEY_COUNT) {
			keyStates[code] = KeyState.RELEASED;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// No one cares, Java.
	}

}
