package game.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	
	private String path;
	public final int SIZE;
	public final int SPRITE_WIDTH, SPRITE_HEIGHT;
	private int width, height;
	public int[] pixels;
	private Sprite[] sprites;
	/*
	 * NOTE:
	 * public static SpriteSheet player_left = new SpriteSheet(spritesheet_object, );
	 */
	public static SpriteSheet player = new SpriteSheet("/game/res/mario.png", 64, 64);
	public static SpriteSheet player_left = new SpriteSheet(player, 0, 0, 2, 1, 16, 32);
	public static SpriteSheet player_right = new SpriteSheet(player, 0, 1, 2, 1, 16, 32);
	public static SpriteSheet player_jump_left = new SpriteSheet(player, 2, 0, 1, 1, 16, 32);
	public static SpriteSheet player_jump_right = new SpriteSheet(player, 2, 1, 1, 1, 16, 32);
	
	public static SpriteSheet enemy = new SpriteSheet("/game/res/marioEnemyV2.png", 96, 32);
	public static SpriteSheet enemyPhase1 = new SpriteSheet(enemy, 0, 0, 3, 1, 32, 32);

	public static SpriteSheet spawn_level = new SpriteSheet("/game/res/marioTiles.png", 32, 32); //Tiles for main level

	public static SpriteSheet menu = new SpriteSheet("/game/res/MarioMenuSmall.jpg", 256, 256);
	public static SpriteSheet gameOverMenu = new SpriteSheet("/game/res/MarioHatSmall.jpg", 256, 256);

	
	public SpriteSheet(String path, int width, int height) {
		this.path = path;
		SIZE = -1; 
		SPRITE_WIDTH = width;
		SPRITE_HEIGHT = height;
		pixels = new int[SPRITE_WIDTH * SPRITE_HEIGHT];
		load();
	}
	
	
	public SpriteSheet(BufferedImage image) {
		SIZE = -1;
		SPRITE_WIDTH = image.getWidth();
		SPRITE_HEIGHT = image.getHeight();
		pixels = new int[SPRITE_WIDTH * SPRITE_HEIGHT];
		load(image);
	}
	
	
	public SpriteSheet(SpriteSheet sheet, int x, int y, int width, int height, int spriteWidth, int spriteHeight) {
		int xx = x * spriteWidth;
		int yy = y * spriteHeight;
		int w = width * spriteWidth;
		int h = height * spriteHeight;
		if(width == height) SIZE = width;
		else SIZE = -1; //Since not square
		SPRITE_WIDTH = w;
		SPRITE_HEIGHT = h;
		pixels = new int[w * h];
		for(int y0 = 0; y0 < h; y0++) {
			int yp = yy + y0;
			for(int x0 = 0; x0 < w; x0++) {
				int xp = xx + x0;
				pixels[x0 + y0 * w] = sheet.pixels[xp + yp * sheet.SPRITE_WIDTH];
			}
		}
		int frame = 0;
		sprites = new Sprite[width * height];
		for(int ya = 0; ya < height; ya++) {
			for(int xa = 0; xa < width; xa++) {
				int[] spritePixels = new int[spriteWidth * spriteHeight];
				for(int y0 = 0; y0 < spriteHeight; y0++) {
					for(int x0 = 0; x0 < spriteWidth; x0++) {
						spritePixels[x0 + y0 * spriteWidth] 
								= pixels[(x0 + xa * spriteWidth) + (y0 + ya * spriteHeight) * SPRITE_WIDTH];
					}
				}
				Sprite sprite = new Sprite(spritePixels, spriteWidth, spriteHeight);
				sprites[frame++] = sprite;
			}
		}
	}
	
	
	public Sprite[] getSprites() {
		return sprites;
	}
	
	
	public int[] getPixels() {
		return pixels;
	}
	
			
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	private void load() {
		try {
			System.out.println("Trying to load: " + path + "...");
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
			System.out.println("Succeeded!");
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch(IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			System.err.println("Failed!");
		}
	}
	
	
	// For Game image loading inorder to properly reize and render image to screen
	public void load(BufferedImage img) {
		try {
			System.out.println("Trying to load: " + path + "...");
			BufferedImage image = img;
			System.out.println("Succeeded!");
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch(Exception e) {
			System.err.println("Failed!");
		}
	}
}
