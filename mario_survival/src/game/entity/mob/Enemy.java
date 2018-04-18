package game.entity.mob;

import java.util.List;

import game.graphics.AnimatedSprite;
import game.graphics.Screen;
import game.graphics.SpriteSheet;
import game.level.Node;
import game.util.Vector2i;

public class Enemy extends Mob {
	
	private AnimatedSprite animEnemy = new AnimatedSprite(SpriteSheet.enemyPhase1, 32, 32, 3);
	
	private int time = 0;
	private double xa = 0;
	private double ya = 0;
	private List<Node> path = null; //default equals null
	private Player player;

	
	public Enemy(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	public void move() {
		xa = 0;
	    ya = 0;
	    
	    int px = (int) level.getClientPlayer().getX(); //Players x position, LOCATIONS ARE IN PIXEL PRECISION
	    int py = (int) level.getClientPlayer().getY(); //Players y position
	    Vector2i start = new Vector2i((int)getX() >> 4, (int)getY() >> 4); //Must divide by 16 to convert from pixel precision to tile precision. A shift to the right by four 
	    Vector2i destination = new Vector2i(px >> 4, py >> 4);
	    path = level.findPath(start, destination); // ADD: if(time % 3 == 1) FOR EFFICIENCY. if statement is for efficiency purpose. It will only update path 20 updates per second
	    if(path != null) {
	    	if(path.size() > 0) {
	    		Vector2i vec = path.get(path.size() - 1).tile; //path.size() - 1 because A* Algorithm store the Nodes with the 1 element being closest to destination, thus we must beginning with last element in the arraylist
	    		if(x < (int)vec.getX() << 4) xa++; //multiplying 16 to vec.getX() converts to tile precision
	    		if(x > (int)vec.getX() << 4) xa--; //these four lines simply move the mob to the above vector: vec
	    		if(y < (int)vec.getY() << 4) ya++;
	    		if(y > (int)vec.getY() << 4) ya--;
	    	}
	    }
	    
	    if(xa != 0 || ya != 0) {
	        move(xa, ya); 
	        walking = true;
	    } else {
	    	walking = false;
	    }
	}
	
	
	public void update() {
		
		time++;
		move();
		if(walking) animEnemy.update();
		else animEnemy.setFrame(0);
		if(ya < 0) {
			dir = Direction.UP;
		} else if(ya > 0) {
			dir = Direction.DOWN;
		} 
		if(xa < 0) {
			dir = Direction.LEFT;
		} else if(xa > 0) {
			dir = Direction.RIGHT;
			time = 0; //MUST RESET TO AVOID GAME CRASH
		}
	}

	
	public void render(Screen screen) {
		sprite = animEnemy.getSprites();
		screen.renderPlayer((int) x - 16, (int) y - 16, sprite);
	}

}
