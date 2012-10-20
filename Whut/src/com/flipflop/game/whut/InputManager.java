package com.flipflop.game.whut;

import java.awt.Component;

public class InputManager {
	public KeyboardInput keyboardInput = new KeyboardInput();
	public MouseInput mouseInput = null;
	public Component watchable = null;
	
	public InputManager(Component watchable) {
		this.mouseInput = new MouseInput(this);
		this.watchable = watchable;
		watchable.addKeyListener(this.keyboardInput);
		watchable.addMouseListener(this.mouseInput);
		//watchable.addMouseMotionListener(this.mouseInput);
	}
	
	public void poll() {
		this.keyboardInput.poll();
		this.mouseInput.poll();
	}
}
