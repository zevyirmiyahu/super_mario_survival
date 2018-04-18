package game.menu;

import game.Game;
import game.graphics.Screen;
import game.graphics.SpriteSheet;
import game.input.Keyboard;


public class StartMenu {

	private Keyboard input; //hitting 'enter' starts game
	
	
	public StartMenu(Keyboard input) {
		this.input = input;
	}

	
	public void update() {
		if(input.enter) {
			Game.state = Game.STATE.GAME;
		} else if(input.help) {
			Game.state = Game.STATE.HELP;
		}
	}
	

	public void render(Screen screen) {
		screen.renderSheet(0, 0, SpriteSheet.menu, true);
	}
	
	
}
