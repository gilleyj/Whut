package com.flipflop.game.whut;

import java.awt.Component;

public class InputManager {
	public KeyboardInput keyboardInput= new KeyboardInput();
	public MouseInput mouseInput= new MouseInput();
	
	public InputManager(Component watchable) {
		watchable.addKeyListener(this.keyboardInput);
		watchable.addMouseListener(this.mouseInput);
		watchable.addMouseMotionListener(this.mouseInput);
	}
}
