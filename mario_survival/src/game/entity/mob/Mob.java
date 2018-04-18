package game.entity.mob;

import game.entity.Entity;
import game.graphics.Screen;

public abstract class Mob extends Entity {
	
	protected boolean moving = false;
	protected boolean walking = false;
	protected int health;
	
	protected enum Direction {
		LEFT, RIGHT, UP, DOWN
	}
	
	protected Direction dir;
	
	
	public void jump() {
	}
	
	public void hitGameBorder(double xa, double ya) {
		if(x == 0 || x == 256) {
			move(0, ya);
		}
	}

		
	public void move(double xa, double ya) {
		if(xa != 0 && ya != 0) {
			move(xa, 0);
			move(0, ya);
			return;
		}
		if(xa > 0) dir = Direction.RIGHT;
		if(xa < 0) dir = Direction.LEFT;
		if(ya > 0) dir = Direction.UP;
		if(ya < 0) dir = Direction.DOWN;

		while(xa != 0) {
			if(Math.abs(xa) > 1) {
				if(!collision(abs(xa), ya)) {
					this.x += abs(xa);
			    }
			    xa -= abs(xa);
			} 
			else {
				if(!collision(abs(xa), ya)) {
					this.x += xa;
			    }
				xa = 0;
			}
		}
		while(ya != 0) {
			if(Math.abs(ya) > 1) {
				if(!collision(xa, abs(ya))) {
					this.y += abs(ya);
				}
				ya -=abs(ya);
			}
			else {
				if(!collision(xa, abs(ya))) {
					this.y += ya;
				}
				ya = 0;
			}
		}
	}
	
	
	private int abs(double value) {
		if(value < 0) return -1;
		return 1;
	}
	
	
	public abstract void update();
	public abstract void render(Screen screen);
	
	/*
	private boolean collision(double xa, double ya) {
		boolean solid = false;
		
		for(int c = 0; c < 4; c++) {
			double xt = ((x + xa - 4.5) - c % 2 * 5 - 7) / 16;
			double yt = ((y + ya - 3) - c / 2 * 5 -5) / 16;
			
			int ix = (int) Math.ceil(xt);
			int iy = (int) Math.ceil(yt);
			if(c % 2 == 0) ix = (int) Math.floor(xt);
			if(c / 2 == 0) iy = (int) Math.floor(yt);
			if(level.getTile(ix,iy).solid()) solid = true;
		}
		return solid;
	}
	*/
	
	private boolean collision(double xa, double ya) {
		boolean solid = false;
		for(int c = 0; c < 4; c++) {			
			double xt = ((x + xa) - c % 2 * 30) / 16;
			double yt = ((y + ya) - c / 2 * 30 + 11) / 16;
			int ix = (int) Math.ceil(xt);
			int iy = (int) Math.ceil(yt);
			if(c % 2 == 0) ix = (int) Math.floor(xt);
			if(c / 2 == 0) iy = (int) Math.floor(yt);
			if(level.getTile(ix, iy).solid()) solid = true;
		}
		return solid;
	}
}
