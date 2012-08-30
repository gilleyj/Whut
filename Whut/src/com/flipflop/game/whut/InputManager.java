package com.flipflop.game.whut;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InputManager implements KeyListener, MouseListener {
	private KeyboardInput keyboardInput= null;
	//private MoustInput mouseInput= null;
	
	public InputManager(Component watchable) {
		watchable.addKeyListener(this.keyboardInput);
	//	watchable.addMouseListener(this.mouseInput);
	//	watchable.addMouseMotionListener(this.mouseInput);
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
	//	System.out.println("Typed " + KeyEvent.getKeyText(e.getID()));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
