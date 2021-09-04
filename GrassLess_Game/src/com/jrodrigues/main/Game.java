package com.jrodrigues.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.jrodrigues.entities.Entity;
import com.jrodrigues.entities.Grass;
import com.jrodrigues.entities.Player;
import com.jrodrigues.graphics.Animation;
import com.jrodrigues.graphics.Spritesheet;
import com.jrodrigues.graphics.UI;
import com.jrodrigues.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;

	public static final int WIDTH = 384;
	public static final int HEIGHT = 224;
	public final static int SCALE = 3;
	int frames = 0;
	private BufferedImage image;

	public static ArrayList<Entity> entities;

	public static ArrayList<Grass> grass;
	public static int grassCount =0;
	
	public static Spritesheet spritesheet;
	public static Spritesheet layoutsheet;
	
	public static World world;

	public static Player player;
	
	public static Animation animation;
	
	
	private static int curLevel = 1;

	private int maxLevel = 15;

	public static String gameState = "NORMAL";

	private int frameLoser = 0;

	public static boolean debug = false;
	public static boolean canDebug = false;

	public UI ui;

	private boolean restartGame = false;
	private boolean isPressedF2 = false;

	public boolean saveGame = false;

	public Game() {
		// Sound.musicBackground.loop();

		addKeyListener(this); // adiciona evento de KeyListener nesta classe

		addMouseListener(this);

		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		spritesheet = new Spritesheet("/spritesheet.png");
		
		ui = new UI();
		
		layoutsheet = new Spritesheet("/layoutsheet.png");
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		animation = new Animation();
		
		entities = new ArrayList<Entity>();
		grass = new ArrayList<Grass>();
		
		
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(0, 0, 16, 16));
		world = new World("/level1.png");
		this.grassCount = grass.size();
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	public void initFrame() {
		frame = new JFrame("Titulo");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String args[]) {
		Game game = new Game();
		game.start();

	}
	
	public void selectLevel(String path, boolean isNewPlayer) {
		Game.entities = new ArrayList<Entity>();
		Game.grass = new ArrayList<Grass>();
//		if (isNewPlayer) {
//			Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(0, 0, 16, 16));
//		} else {
//			Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(0, 0, 16, 16), player.life,
//					player.maxLife, player.getAmmo(), player.hasGun());
//		}
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(0, 0, 16, 16));
		Game.world = new World("/" + path);
		System.out.println("numero de grass: " + grass.size());
		grassCount = grass.size();
	}
	
	
	private void tick() {
		
		animation.tick();
		
		if (gameState == "NORMAL") {
			// Sound.musicBackground.loop();

			restartGame = false;
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();

			}
			for (int i = 0; i < grass.size(); i++) {
				Grass gr = grass.get(i);
				gr.tick();

			}
			
			
			
			if (grass.size() == 0) {
				
				// avancar nivel
				curLevel++;
				if (curLevel > maxLevel) {// voltar tudo se chegou no final
					curLevel = 1;
					gameState = "WIN";
					gameState = "NORMAL";

				}
				String newWorld = "level" + curLevel + ".png";
				selectLevel(newWorld, false);
				gameState = "NORMAL";
				
				//gameState = "CLEAR";
			}
			
			
			
			

		} else if (gameState == "GAME_OVER") {

			frameLoser++;
			if (frameLoser > WIDTH / 2) {
				frameLoser = 0;
			}

		} else if (gameState == "MENU") {

		}else if (gameState == "CLEAR") {
			//animation.runLevelClear();
			System.out.println("look me haha");
		}else if(gameState == "NEWLEVEL") {
			
		}
		
		

	}

	private void render() {

		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		
		
		
		if(gameState == "NORMAL") {
			world.render(g);
			for (int i = 0; i < grass.size(); i++) {
				Grass gr = grass.get(i);
				gr.render(g);

			}
			
		}
		
		
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		
		
		g = bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
		//g.drawImage(image, -202, 0, WIDTH * SCALE, HEIGHT * SCALE-150, null);
		g.drawImage(image, -202, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		//bs.show();
		
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(230, 0, 720, 192);
		
		
		ui.render(g);
		animation.render(g);
		
		bs.show();
		
	}

	@Override
	public void run() {
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;

		double timer = System.currentTimeMillis();
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				tick();
				render();

				frames++;
				delta--;
			}

			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS:" + frames);

				frames = 0;
				timer += 1000;
			}
		}

		stop();

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
			player.wannaMove = true;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
			player.wannaMove = true;

		}else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			player.wannaMove = true;

		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			player.wannaMove = true;

		}
		
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			animation.pressedEnter = true;
		}
		
		

		

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
			player.wannaMove = false;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
			player.wannaMove = false;

		}else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
			player.wannaMove = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
			player.wannaMove = false;
		}
		
		

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
//
//		player.mx = arg0.getX() / SCALE;
//		player.my = arg0.getY() / SCALE;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	public static int getCurLevel() {
		return curLevel;
	}

}
