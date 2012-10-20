package com.flipflop.game.whut;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

public class KeyboardInput implements KeyListener {
	private static final Logger logger = Logger.getLogger(KeyboardInput.class.getName());
	public enum KeyState {
		PRESSED,
		RELEASED;
	}
	
	public class KeyInfo {
		public KeyState keyState;
		public long pressedSince;
		
		public KeyInfo() {
			this.keyState = KeyState.RELEASED;
			this.pressedSince = 0;
		}
	}
	
	private static final int KEY_COUNT = 256;
	private KeyInfo[] keysInfo = new KeyInfo[KEY_COUNT];
	
	public KeyboardInput() {
		for (int i=0; i<KEY_COUNT; i++) {
			keysInfo[i] = new KeyInfo();
		}
	}
	
	public boolean isKeyPressed(int keyCode) {
		if (keyCode >= 0 && keyCode < KEY_COUNT) {
			return (keysInfo[keyCode].keyState==KeyState.PRESSED?true:false);
		} else {
			return false;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code >= 0 && code < KEY_COUNT) {
			if (keysInfo[code].keyState == KeyState.RELEASED) {
				keysInfo[code].keyState = KeyState.PRESSED;
				keysInfo[code].pressedSince = System.currentTimeMillis();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code >= 0 && code < KEY_COUNT) {
			logger.info(e.getKeyChar() + " (" + e.getKeyCode() + ") pressed for " + (System.currentTimeMillis() - keysInfo[code].pressedSince));
			keysInfo[code].keyState = KeyState.RELEASED;
			keysInfo[code].pressedSince = 0;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// No one cares, Java.
	}

}
