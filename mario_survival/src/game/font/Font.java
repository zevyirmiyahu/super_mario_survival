package game.font;

import game.graphics.Screen;
import game.graphics.Sprite;
import game.graphics.SpriteSheet;

public class Font {
	private static SpriteSheet font = new SpriteSheet("/game/res/fonts/classicFont.png", 16, 16);
	private static Sprite[] characters = Sprite.split(font);
	
	private static String charIndex = "ABCDEFGHIJKLM" + //
										"NOPQRSTUVWXYZ" + //
										"abcdefghijklm" + //
										"nopqrstuvwxyz" + //
										"0123456789:,?" + //
										"!\"'.()\\^[]+-*";
	
	
	public Font() {
	}
	
	
	public void render(int x, int y, String text, Screen screen) {
		render(x, y, 0, text, screen); //If no spacing is given default 0 is used.
	}		
	
	public void render(int x, int y, int spacing, String text, Screen screen) { // x and y are positions to render string
		render(x, y, spacing, 0, text, screen); 
	}
	
	
	public void render(int x, int y, int spacing, int color, String text, Screen screen) {
		int xOffset = 0;
		int line = 0; //used to drop sentence down one line
		for(int i = 0; i < text.length(); i++) {
			xOffset += 16 + spacing;
			int yOffset = 0; //Used to drop down certain character, example g and y as in "gamey"
			char currentChar = text.charAt(i);
			if(currentChar == 'g' || currentChar == 'y' || currentChar == 'q' || currentChar == 'p' || currentChar == 'j' || currentChar == ',') yOffset = 2;
			if(currentChar == '\n') {
				xOffset = 0;
				line++;
			}
			int index = charIndex.indexOf(currentChar);
			if(index == -1) continue;
			screen.renderSprite(x + xOffset, y + line*10+ yOffset, characters[index], false); //USE RenderTextCharacter if you want different color text
		}
	}
}
