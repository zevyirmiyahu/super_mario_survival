package game.level.spawntile;

import game.graphics.Screen;
import game.graphics.Sprite;
import game.level.tile.Tile;

public class SpawnMysteryBoxTile extends Tile {
	
	
	public SpawnMysteryBoxTile(Sprite sprite) {
		super(sprite);
	}
	
	
	public void render(int x, int y, Screen screen) {
		screen.renderTile(x << 4, y << 4, this);
	}
	
	
	public boolean solid() { //Can't pass through the mystery box
		return true;
	}
}
