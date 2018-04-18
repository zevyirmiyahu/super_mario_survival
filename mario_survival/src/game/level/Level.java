package game.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import game.Game;
import game.Game.STATE;
import game.entity.Entity;
import game.entity.mob.Enemy;
import game.entity.mob.Player;
import game.graphics.Screen;
import game.level.tile.Tile;
import game.util.Vector2i;

public class Level {
	
	protected int width, height;
	protected int[] tiles;
	
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Player> players = new ArrayList<Player>();
	
	//public static Level spawn = new Level("/game/res/mario_level.png"); //main level in game
	public static Level spawn = new Level("/game/res/mario_level_V2.png");

	//A* search Algorithm fields
	private Comparator<Node> nodeSorter = new Comparator<Node>() {
		public int compare(Node n0, Node n1) {
			if(n1.fCost < n0.fCost) return +1;
			if(n1.fCost > n0.fCost) return -1;
			return 0;
		}
	};

	
	public Level(String path) {
		loadLevel(path);
		generateLevel();
	}
	
	
	protected void generateLevel() {
	}
	
	
	protected void loadLevel(String path) {
		try {
			System.out.println("Trying to load level: " + path + "...");
			BufferedImage image = ImageIO.read(Level.class.getResource(path));
			System.out.println("succeeded load level");
			int w = width = image.getWidth();
			int h = height = image.getHeight();
			tiles = new int[w * h];
			image.getRGB(0, 0, w, h, tiles, 0, w);
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("Could NOT load level file!");
		}
		
		//Add NPCs here
		add(new Enemy(4, 4));
	}
	
	
	public void update() {
		for(int i = 0; i < players.size(); i++) {
			players.get(i).update();
		}
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
		remove();
		gameOver();
	}
	
	public void gameOver() {
		Player player = getClientPlayer();
		Entity enemy = getClientEnemy();
		//difference between 28 - 12 is 16, must be 16 for tile precision
		if(enemy.getX() - 12 <= player.getX() && player.getX() <= enemy.getX() + 28  
				&& enemy.getY() - 12 < player.getY() && player.getY() < enemy.getY() + 28) {
			Game.state = STATE.GAMEOVER;
			entities.remove(0);
			add(new Enemy(4, 4));
		}
	}
	
	
	public void remove() {
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).isRemoved()) players.remove(i);
		}
	}
	
	
	public void add(Entity e) {
		e.init(this);
		if(e instanceof Player) {
			players.add((Player)e);
		}
		else {
			entities.add(e);
		}
	}
	
	
	// A* Algorithm search Algorithm
	public List<Node> findPath(Vector2i start, Vector2i goal) {
		List<Node> openList = new ArrayList<Node>(); //All possible Nodes(tiles) that could be shortest path
		List<Node> closedList = new ArrayList<Node>(); //All no longer considered Nodes(tiles)
		Node current = new Node(start,null, 0, getDistance(start, goal)); //Current Node that is being considered(first tile)
		openList.add(current);
		while(openList.size() > 0) {
			Collections.sort(openList, nodeSorter); // will sort open list based on what's specified in the comparator
			current = openList.get(0); // sets current Node to first possible element in openList
			if(current.tile.equals(goal)) {
				List<Node> path = new ArrayList<Node>(); //adds the nodes that make the path 
				while(current.parent != null) { //retraces steps from finish back to start
					path.add(current); // add current node to list
					current = current.parent; //sets current node to previous node to strace path back to start
				}
				openList.clear(); //erases from memory since algorithm is finished, ensures performance is not affected since garbage collection may not be called
				closedList.clear();
				return path; //returns the desired result shortest/quickest path
			}
			openList.remove(current); //if current Node is not part of path to goal remove
			closedList.add(current); //and puts it in closedList, because it's not used
			for(int i = 0; i < 9; i++ ) { //8-adjacent tile possibilities
				if(i == 4) continue; //index 4 is the middle tile (tile player currently stands on), no reason to check it
				int x = (int)current.tile.getX();
				int y = (int)current.tile.getY();
				int xi = (i % 3) - 1; //will be either -1, 0 or 1
				int yi = (i / 3) - 1; // sets up a coordinate position for Nodes (tiles)
				Tile at = getTile(x + xi, y + yi); // at tile be all surrounding tiles when iteration is run
				if(at == null) continue; //if empty tile skip it
				if(at.solid()) continue; //if solid cant pass through so skip/ don't consider this tile
				Vector2i a = new Vector2i(x + xi, y + yi); //Same thing as node(tile), but changed to a vector
				double gCost = current.gCost + (getDistance(current.tile, a) == 1 ? 1 : 0.95); //*calculates only adjacent nodes* current tile (initial start is 0) plus distance between current tile to tile being considered (a)
				double hCost = getDistance(a, goal);								// conditional piece above for gCost makes a more realist chasing, because without it mob will NOT use diagonals because higher gCost
				Node node = new Node(a, current, gCost, hCost);
				if(vecInList(closedList, a) && gCost >= node.gCost) continue; //is node has already been checked 
				if(!vecInList(openList, a) || gCost < node.gCost) openList.add(node);
			}
		}
		closedList.clear(); //clear the list, openList will have already been clear if no path was found
		return null; //a default return if no possible path was found
	}
	
	
	private boolean vecInList(List<Node> list, Vector2i vector) {
		for(Node n : list) {
			if(n.tile.equals(vector)) return true;
		}
		return false; // if gone through entire list and NOT there return false
	  }
	  
	  // Distance Method used in A* Algorithm above
	  private double getDistance(Vector2i tile, Vector2i goal) {
		  double dx = tile.getX() - goal.getX();
		  double dy = tile.getY() - goal.getY();
		  return Math.sqrt(dx * dx + dy *dy); //distance 
	  }
	//END OF A* SEARCH ALGORITHM CODE
	
	  
	public List<Player> getPlayers() {
		return players;
	}
	
	
	public Player getClientPlayer() { //Returns instance of first player from List
		return players.get(0);
	}
	
	
	public Entity getClientEnemy() {
		return entities.get(0);
	}
	
	public void render(int xScroll, int yScroll, Screen screen) {
		screen.setOffset(xScroll, yScroll);
		int x0 = xScroll >> 4;
		int x1 = (xScroll + screen.width + 16) >> 4;
		int y0 = yScroll >> 4;
		int y1 = (yScroll + screen.height + 16) >> 4;
		for(int y = y0; y < y1; y++) {
			for(int x = x0; x < x1; x++) {
				getTile(x , y).render(x, y, screen);
			}
		}
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).render(screen);
		}
		for(int i = 0; i < players.size(); i++) {
			players.get(i).render(screen);
		}	
	}
	
	
	//Render fixed level to screen
	public void render(Screen screen) {
		int x1 = screen.width;
		int y1 = screen.height;
		for(int y = 0; y < y1; y++) {
			for(int x = 0; x < x1; x++) {
				getTile(x, y).render(x, y, screen);
			}
		}
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).render(screen);
		}
		for(int i = 0; i < players.size(); i++) {
			players.get(i).render(screen);
		}
	}
	
	
	public Tile getTile(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return Tile.voidTile;
		
		if(tiles[x + y * width] == Tile.col_foor) return Tile.floor;
		if(tiles[x + y * width] == Tile.col_wall) return Tile.wall;
		if(tiles[x + y * width] == Tile.col_platform) return Tile.platform;
		if(tiles[x + y * width] == Tile.col_mysteryBox) return Tile.mysteryBox;
		
		return Tile.voidTile; //Returns Tile if edge of map is reached

	}
	
	
}
