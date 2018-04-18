package game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
	private boolean[] keys = new boolean[120];
	public boolean jump, left, right, enter, help, back;

	
	public void update() {
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		jump = keys[KeyEvent.VK_SPACE];
		enter = keys[KeyEvent.VK_ENTER];
		help = keys[KeyEvent.VK_H];
		back = keys[KeyEvent.VK_B]; 
	}
	
	
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}
	
	
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}
	
	
	public void keyTyped(KeyEvent e) {
	}

}
