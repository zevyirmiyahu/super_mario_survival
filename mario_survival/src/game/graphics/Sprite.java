package game.graphics;

public class Sprite {
	
	public final int SIZE;
	private int x, y;
	private int width, height;
	public int[] pixels;
	protected SpriteSheet sheet;
	
	public static Sprite player = new Sprite(16, 0, 0, SpriteSheet.player_right); //Not a square sprite
	
	public static Sprite enemy = new Sprite(32, 0, 0, SpriteSheet.enemyPhase1);

	public static Sprite floor = new Sprite(16, 1, 0, SpriteSheet.spawn_level); //square sprite
	public static Sprite wall = new Sprite(16, 0, 1, SpriteSheet.spawn_level);
	public static Sprite platform = new Sprite(16, 0, 0, SpriteSheet.spawn_level);
	public static Sprite mysteryBox = new Sprite(16, 1, 1, SpriteSheet.spawn_level);
	
	public static Sprite voidSprite = new Sprite(16, 0x1B87E0);
	
	public static Sprite menuSprite = new Sprite(2, 0, 0, SpriteSheet.menu);
	
	//Sprite for voidSprite
	public Sprite(int size, int color) {
		SIZE = size;
		this.width = size;
		this.height = size;
		pixels = new int[SIZE * SIZE];
		setColor(color);
	}
	
	//For square sprites
	public Sprite(int size, int x, int y, SpriteSheet sheet) {
		SIZE = size;
		this.width = size;
		this.height = size;
		pixels = new int[SIZE * SIZE];
		this.x = x * size;
		this.y = y * size;
		this.sheet = sheet;
		load();	
	}
	
	
	//For sprites that are NOT square
	public Sprite(int width, int height, SpriteSheet sheet) {
		SIZE = width * height;
		this.width = width;
		this.height = height;
		pixels = new int[SIZE];
		this.x = x * width;
		this.y = y * height;
		this.sheet = sheet;
		load();
	}
	
	public Sprite(int[] pixels, int width, int height) {
		SIZE = (width == height) ? width : -1;
		this.width = width;
		this.height = height;
		this.pixels = new int[pixels.length];
		for(int i = 0; i < pixels.length; i++) {
			this.pixels[i] = pixels[i];
		}
	}
	
	
	//Used by Animated Sprite class
	public Sprite(SpriteSheet sheet, int width, int height) {
		if(width == height) SIZE = width;
		else SIZE = -1;
		this.width = width;
		this.height = height;
		this.sheet = sheet;
	}
	
	
	
	public void setColor(int color) {
		for(int i = 0; i < width * height; i++) {
			pixels[i] = color;
		}
	}
	
	
	public int getWidth() {
		return width;
	}
	
	
	public int getHeight() {
		return height;
	}
	
	
	// Splits an spritesheet into seperate sprites. Used to obtain individual characters
	public static Sprite[] split(SpriteSheet sheet) { 
		  int amount = (sheet.getWidth() * sheet.getHeight()) / (sheet.SPRITE_WIDTH * sheet.SPRITE_HEIGHT); //area of whole sprite sheet divided by area of a single sprite
		  Sprite[] sprites = new Sprite[amount];
		  int[] pixels = new int[sheet.SPRITE_WIDTH * sheet.SPRITE_HEIGHT]; //the amount of pixels of a particular sprite
		  int current = 0; //Current sprite thats being worked with
		  
		  for(int yp = 0; yp < sheet.getHeight() / sheet.SPRITE_HEIGHT; yp++) { //yp and xp, y position and x position
			  for(int xp = 0; xp < sheet.getWidth() / sheet.SPRITE_WIDTH; xp++) { //amount of sprite horizontally 
				  for(int  y = 0; y < sheet.SPRITE_HEIGHT; y++) { //every pixel of the sprite
					  for(int x = 0; x < sheet.SPRITE_WIDTH; x++) {
						  int xo = x + xp * sheet.SPRITE_WIDTH; //x offset
						  int yo = y + yp * sheet.SPRITE_HEIGHT; //y offset
						  pixels[x + y * sheet.SPRITE_WIDTH] = sheet.getPixels() [xo + yo * sheet.getWidth()];
					  }
				  }
				  //current++ start with 0 and then increments, if ++current will increment first and start at 1
				  sprites[current++] = new Sprite(pixels, sheet.SPRITE_WIDTH, sheet.SPRITE_HEIGHT);
			  }
		  }
		  return sprites;
	  }
	
	
	private void load() {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				pixels[x + y * width] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SPRITE_WIDTH];
			}
		}
	}
}
