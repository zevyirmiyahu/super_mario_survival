package game.entity;

import java.util.Random;

import game.graphics.Screen;
import game.graphics.Sprite;
import game.level.Level;

public abstract class Entity {
	
	protected double x, y;
	protected boolean removed = false;
	protected Level level;
	protected Random random = new Random();
	protected Sprite sprite;
	
	
	public void update() {
		
	}
	
	public void render(Screen screen) {
		//if(sprite != null) screen.renderPlayer((int)x, (int)y, sprite);
	}
	
	
	public void remove() {
		removed = true;
	}
	
	public double getX() {
		return x;
	}
	
	
	public double getY() {
		return y;
	}
	
	
	public Sprite getSprite() {
		return sprite;
	}
	
	
	public boolean isRemoved() {
		return removed;
	}
	
	
	public void init(Level level) {
		this.level = level;
	}

}
