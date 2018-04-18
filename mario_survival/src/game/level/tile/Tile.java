package game.level.tile;

import game.graphics.Screen;
import game.graphics.Sprite;
import game.level.spawntile.SpawnFloorTile;
import game.level.spawntile.SpawnMysteryBoxTile;
import game.level.spawntile.SpawnPlatformTile;
import game.level.spawntile.SpawnWallTile;

public class Tile {
	
	public Sprite sprite;
	
	public static Tile floor = new SpawnFloorTile(Sprite.floor);
	public static Tile wall = new SpawnWallTile(Sprite.wall);
	public static Tile platform = new SpawnPlatformTile(Sprite.platform);
	public static Tile mysteryBox = new SpawnMysteryBoxTile(Sprite.mysteryBox);
	
	public static Tile voidTile = new VoidTile(Sprite.voidSprite);
	
	//Define colors used on the pixel map for proper tile usage.
	public static final int col_wall = 0xff000000; //black
	public static final int col_foor = 0xff595652; //grey
	public static final int col_platform = 0xffac3232; //red
	public static final int col_mysteryBox = 0xfffbf236; //yellow

	
	public Tile(Sprite sprite) {
		this.sprite = sprite;
	}
	
	
	public void render(int x, int y, Screen screen) {
	}
	
	
	public boolean solid() {
		return false;
	}
	
	
	public boolean exit() {
		return false;
	}
}
