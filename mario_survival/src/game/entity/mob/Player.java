package game.entity.mob;

import game.graphics.AnimatedSprite;
import game.graphics.Screen;
import game.graphics.Sprite;
import game.graphics.SpriteSheet;
import game.input.Keyboard;

public class Player extends Mob {
	
	private int anim = 0; //animation counter for sprite walking movement
		
	protected static int direction;
	
	private boolean walking = false;
	private boolean hasJumped = false;
	
	//private int floor = 256 - 16  * 3; // y-position of floor on screen
	
	private Sprite sprite;
	private Keyboard input;
	private Keyboard lastInput = null; //used to reset frame
	
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.player_right, 16, 32, 2);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.player_left, 16, 32, 2);
	private AnimatedSprite jumpRight = new AnimatedSprite(SpriteSheet.player_jump_right, 16, 32, 1);
	private AnimatedSprite jumpLeft = new AnimatedSprite(SpriteSheet.player_jump_left, 16, 32, 1);
	
	private AnimatedSprite animSprite = right; //Initial direction that the player faces

	
	public Player(int x, int y, Keyboard input) {
		this.x = x;
		this.y = y;
		this.input = input;
		//sprite = Sprite.player;
	}
	
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}

	
	public void changeSprite() {
		if(direction == 0) animSprite = right;
		if(direction == 1) animSprite = left;
		if(direction == 2) animSprite = jumpRight;
		if(direction == 3) animSprite = jumpLeft;
	}
	
	
	public double jump(double ya, Keyboard input) {
		double jumpMax = 80; //Maximum height player can jump in a single jump
		
		if(input.jump) {
			direction = 2;
			changeSprite();
			ya -= 5;
			return ya;

		}
		return ya;
	}
	
	double timeStart = System.currentTimeMillis();
	public void update() {
		
		if(walking) animSprite.update();
		if(!walking) animSprite.setFrame(0);
			
		if(anim < 7500) anim++;
		else anim = 0; //resets anim to avoid crash
		
		double xa = 0, ya = 0; //resets values for each update
		double speed = 1.4;
		double gravity = 3.0;
				
		//Limit jumping
		double timeNow = System.currentTimeMillis();
		double timeElapsed = (timeNow - timeStart) / 1000; //time elapsed in seconds 
		
		if(input.right) {
			changeSprite();
			direction = 0;
			xa += speed;
			hasJumped = false;
		}
		if(input.left) {
			direction = 1;
			changeSprite();
			xa -= speed;
			hasJumped = false;
		}
		ya += gravity;
	
		if(/*timeElapsed > 0.2 && */input.jump && ya > 0) {
			if(xa > 0) {
				direction = 2;
			}
			else direction = 3;
			
			changeSprite();
			//ya -= 10;
			ya -= Math.sin(ya) * 50;
			timeStart = System.currentTimeMillis(); //reset timeElapsed after jump
		}
		
		System.out.println("timeElapsed = " + timeElapsed);
		System.out.println("x = " + x + ", y = " + y);
	    System.out.println("xa = " + xa + ", ya = " + ya);
	    //Creates game panel borders for player
		double leftBorder = 16;
		double rightBorder = 260;
		double topBorder = 16;
		if(xa != 0 && x > leftBorder && x < rightBorder && y >topBorder) {
			move(xa, ya);
			walking = true;
		}
		else if(xa != 0 && x < leftBorder && x < rightBorder && y > topBorder) {
			move(0, ya);
			walking = true;
			x = x + 0.1;
		}
		else if(xa != 0 && x > leftBorder && x > rightBorder && y > topBorder) {
			move(0, ya);
			walking = true;
			x = x - 0.1;
		}
		else if(y < topBorder) {
			move(xa, 0);
			walking = true;
			y = y + 1;
		}
		else {
			move(xa, ya);
			walking = false;
		}
	}
	
	
	public void render(Screen screen) {
		sprite = animSprite.getSprites();
		screen.renderPlayerDynamic((int) x - 16, (int) y - 16, sprite, false);
	}
}
