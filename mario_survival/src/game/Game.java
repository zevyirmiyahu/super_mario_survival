package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import game.entity.mob.Player;
import game.font.Font;
import game.graphics.Screen;
import game.input.Keyboard;
import game.level.Level;
import game.level.TileCoordinate;
import game.menu.GameOverMenu;
import game.menu.HelpMenu;
import game.menu.StartMenu;


/*
 * This is a survival game in which the player plays as Mario, running around
 * in order to avoid being eliminated by a fire monster. The score is the amount
 * in seconds one can survive. Everything from the java code to the sprites were made 
 * from scratch. Java was written using Eclipse and the sprites were created using
 * ASEPRITE. Enjoy!
 * 
 * @author Zev Yirmiyahu
 * 
 * E-Mail: zy@zeyirmiyahu.com
 * 
 * GitHub: https://github.com/zevyirmiyahu 
 * 
 * Personal Website: www.zevyirmiyahu.com
 */


public class Game extends Canvas implements Runnable {
	
	private static int width = 256;
	private static int height = width; //  / 16 * 9;
	private static int scale = 2;

	private long score; //Players scrore
	
	private boolean running = false;
	
	// For clock usage in scoring
	public static  boolean startClock = true;
	private long startTime;
	
	public static String title = "Super Mario Survival";
	
	private Thread thread;
	private JFrame frame;
	
	public static Game game;
	private static StartMenu menu;
	private HelpMenu helpMenu;
	private static GameOverMenu gameOverMenu;
	private static Keyboard key;
	private Screen screen;
	private Player player;
	private static TileCoordinate playerSpawn = new TileCoordinate(9, 14);
	private Level level;
	private Font font;
	
	public static enum STATE {
		MENU, //main title menu
		HELP, //help menu
		GAME, //game
 		GAMEOVER; //gameover screen 
	};
	
	//public static STATE state = STATE.GAME;
	public static STATE state = STATE.MENU;
	
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	public Game() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		setFocusable(true);
		
		screen = new Screen(width, height);
		frame = new JFrame();
		key = new Keyboard();
		menu = new StartMenu(key);
		helpMenu = new HelpMenu(key);
		gameOverMenu = new GameOverMenu(key);
		level = Level.spawn;
		font = new Font();

		//Add player to level
		player = new Player(playerSpawn.x(), playerSpawn.y(), key);
		level.add(player);
		addKeyListener(key); //listen for input from key
	}
	
	
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}
	
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1_000_000_000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >=1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(title + "  |  " + updates +"UPS, " + frames + "FPS");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
		
	public void update() {
		
		if(state.equals(STATE.MENU)) {
			key.update();
			menu.update();
		}
		if(state.equals(STATE.HELP)) {
			key.update();
			helpMenu.update();
		}
		if(state.equals(STATE.GAME)) {
			key.update();
			level.update();			
		}
		if(state.equals(STATE.GAMEOVER)) {
			key.update();
			gameOverMenu.update();
		}
	}
	
	
	public void render() {
		
		System.out.println("startClock = " + startClock);
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		screen.clear();
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, width * scale, height * scale, null);
		
		if(state.equals(STATE.MENU)) {
			menu.render(screen);
			font.render(4, 210, -8, "PRESS ENTER \n\n TO PLAY", screen);
			font.render(170, 210, -8, "PRESS H \n\n FOR HELP", screen);
			for(int i = 0; i < pixels.length; i++) {
				pixels[i] = screen.pixels[i];
			}
		}
		if(state.equals(STATE.GAME)) {
			
			if(startClock) {
				startTime = System.currentTimeMillis(); //Captures current time for score timer calculation
				startClock = false;
			}

			level.render(screen);			
			font.render(158, 0, -8, "Score:", screen);
			for(int i = 0; i < pixels.length; i++) {
				pixels[i] = screen.pixels[i];
			}
			
			//SCORE TIMER
			g.setFont(new java.awt.Font("Verdana", 1, 30));
			g.setColor(Color.RED);
			long now = System.currentTimeMillis();
			score = (now - startTime) / 1000;
			g.drawString("" + score, 440, 30);
		}
		if(state.equals(STATE.HELP)) {
			font.render(80, 30, -8, 0xffABCD32, "HELP MENU", screen);
			font.render(0, 5, -10, "Go back [Press b] ", screen);
			String text = "Player must avoid touching \nfire monster as long as\npossible. "
					+ "Score is determined\nby survival time."
					+ "\n\nMove Left: left arrow OR a"
					+ "\n\nMove Right: right arrow OR d"
					+ "\n\nJump: space bar"
					+ "\n\n\n\nGame Creator: Zev Yirmiyahu"
					+ "\n\n\nE-mail: zy@zevyirmiyahu.com";
			font.render(0, 60, -8, text, screen);
			for(int i = 0; i < pixels.length; i++) {
				pixels[i] = screen.pixels[i];
			}
		}
		if(state.equals(STATE.GAMEOVER)) {
			startClock = true; //reset clock flag
			gameOverMenu.render(screen);
			font.render(40, 30, "GAME OVER", screen);
			font.render(4, 180, -8, "PRESS ENTER \n\n TO PLAY", screen);
			font.render(170, 180, -8, "PRESS H \n\nFOR HELP", screen);
			font.render(25, 60, "Score:", screen);
			player.setPosition(playerSpawn.x(), playerSpawn.y());
			for(int i = 0; i < pixels.length; i++) {
				pixels[i] = screen.pixels[i];
			}
			g.setFont(new java.awt.Font("Verdana", 1, 30));
			g.setColor(Color.RED);
			g.drawString("" + score, 300, 150);
		}
		
		g.dispose();
		bs.show();
	}

	
	public static void main(String[] args) {
		
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle(title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
	}
}
