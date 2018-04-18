package game.menu;

import game.Game;
import game.graphics.Screen;
import game.graphics.SpriteSheet;
import game.input.Keyboard;
import game.level.Level;

public class GameOverMenu {
	
	private Keyboard input;
	private Level level;
	
	public GameOverMenu(Keyboard input) {
		this.input = input;
	}
	
	public void update() {
		if(input.enter) {
			Game.state = Game.STATE.GAME;
			Game.startClock = true; //prompt clock to restart counting
		}
		if(input.help) {
			Game.state = Game.STATE.HELP;
		}
	}
	
	
	public void render(Screen screen) {
		screen.renderSheet(0, 0, SpriteSheet.gameOverMenu, true);
	}
}
