package game.menu;

import game.Game;
import game.Game.STATE;
import game.graphics.Screen;
import game.input.Keyboard;

public class HelpMenu {
		
	private Keyboard input;
	
	
	public HelpMenu(Keyboard input) {
		this.input = input;
	}
	
	
	public void update() {
		if(input.back) {
			Game.state = STATE.MENU;
		}
	}
	
	
	public void render(Screen screen) {
	}
}
