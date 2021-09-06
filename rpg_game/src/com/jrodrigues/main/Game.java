package com.jrodrigues.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.jrodrigues.entities.BulletShoot;
import com.jrodrigues.entities.Enemy;
import com.jrodrigues.entities.Entity;
import com.jrodrigues.entities.Player;
import com.jrodrigues.graphics.Spritesheet;
import com.jrodrigues.graphics.UI;
import com.jrodrigues.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Random rand;
	
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	
	public final static int SCALE = 3;
	
	public static final int WIDTH = 320;
	public static final int HEIGHT = 230;
	
	//public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width/SCALE+1;
	//public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height/SCALE+1;
	
//Toolkit.getDefaultToolkit().getScreenSize()
	//public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	//public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	
	
	public static int frames = 0;
	private BufferedImage image;
	public static ArrayList<Entity> entities;
	public static ArrayList<Enemy> enemies;
	public static ArrayList<BulletShoot> bullets;

	public static Spritesheet spritesheet;

	public BufferedImage lightmap;
	public int[] lightMapPixels;

	public static World world;

	public static Player player;

	private static int curLevel = 1;

	private int maxLevel = 2;

	public static String gameState = "MENU";

	private int frameLoser = 0;

	public static boolean debug = false;
	public static boolean canDebug = false;
	
	public static boolean isFullscreen = false;
	
	public UI ui;

	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("8bit.TTF");
	public static Font myFont;

	public Menu menu;

	public int[] pixels;

	private boolean restartGame = false;
	private boolean isPressedF2 = false;
	private boolean isPressedF11 = false;
	public boolean saveGame = false;

	private int mx;

	private int my;

	public Game() {
		Sound.musicBackground.loop();

		addKeyListener(this); // adiciona evento de KeyListener nesta classe

		addMouseListener(this);

		addMouseMotionListener(this);

		//setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		if(isFullscreen) {
			setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
		}else {
			setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		}
		
		initFrame();
		spritesheet = new Spritesheet("/spritesheet3.png");
		rand = new Random();
		ui = new UI();
		menu = new Menu();

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

		try {
			lightmap = ImageIO.read(getClass().getResource("/lightmap.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		lightMapPixels = new int[lightmap.getWidth() * lightmap.getHeight()];
		lightmap.getRGB(0, 0, lightmap.getWidth(), lightmap.getHeight(), lightMapPixels, 0, lightmap.getWidth());

		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();

		player = new Player(0, 0, 16, 16, spritesheet.getSprite(0, 0, 16, 16));
		world = new World("/level1.png");
		// entities.add(player);

		try {
			myFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(30f);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void reset(String level) {
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.bullets = new ArrayList<BulletShoot>();
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(0, 0, 16, 16));
		Game.world = new World(level);
		curLevel = 1;
	}

	public static void restartGame(String level) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);
		return;
	}

	public void selectLevel(String path, boolean isNewPlayer) {
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		// if new player: without: double life,int maxLife,int ammo,boolean hasGun
		if (isNewPlayer) {
			Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(0, 0, 16, 16));
		} else {
			Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(0, 0, 16, 16), player.life,
					player.maxLife, player.getAmmo(), player.hasGun());
		}

		Game.world = new World("/" + path);
		System.out.println("inimigos:" + enemies.size());
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

	// metodo para iniciar o frame
	public void initFrame() {
		
		frame = new JFrame("Titulo");
		
		frame.add(this);
		
		
		frame.setUndecorated(isFullscreen); // para modo tela cheia
		
		// nao ser redimensionavel
		frame.setResizable(false);
		
		Image imagem = null;
		try {
			imagem = ImageIO.read(getClass().getResource("/icon.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
		frame.setIconImage(imagem);
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		
		//cursor personalizado
		//Image image = toolkit.getImage(getClass().getResource("/cursor.png"));
		//Cursor c = toolkit.createCustomCursor(image, new Point(0,0), "img");
		//frame.setCursor(c);
		
		// calcula certo as dimensoes
		frame.pack();

		// iniciar no centro
		frame.setLocationRelativeTo(null);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setVisible(true);
		
		
		
		
	}
	
   

	public static void main(String args[]) {
		Game game = new Game();
		game.start();

	}

	private void tick() {
			
		requestFocus();
		
		if (gameState == "NORMAL") {
			// Sound.musicBackground.loop();

			if (this.saveGame) {
				this.saveGame = false;
				String[] opt1 = { "level" };
				int[] opt2 = { curLevel };
				Menu.saveGame(opt1, opt2, 10);
			}

			restartGame = false;
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();

			}
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).tick();
			}

			if (enemies.size() == 0) {
				
				// avancar nivel
				curLevel++;
				if (curLevel > maxLevel) {// voltar tudo se chegou no final
					curLevel = 1;
					gameState = "WIN";
					player.maxLife *= 2;
					player.life *= 2;
					gameState = "NORMAL";

				}
				// System.out.println("" + curLevel);
				String newWorld = "level" + curLevel + ".png";
				selectLevel(newWorld, false);
			}

			if (isPressedF2 && debug) {
				isPressedF2 = false;
				curLevel++;
				if (curLevel > maxLevel) {
					curLevel = 1;
				}
				String newWorld = "level" + curLevel + ".png";
				selectLevel(newWorld, false);
			} else {
				isPressedF2 = false;
			}

		} else if (gameState == "GAME_OVER") {

			if (restartGame) {
				Game.reset("/level1.png");
				restartGame = false;
				gameState = "NORMAL";

			}

			frameLoser++;
			if (frameLoser > WIDTH / 2) {
				frameLoser = 0;
			}

		} else if (gameState == "MENU") {
			menu.tick();
		}

		// && gameState == "GAME_OVER"

	}

	private void render() {

		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		// Graphics g = bs.getDrawGraphics();
		Graphics g = image.getGraphics();

		g.setColor(new Color(120, 120, 120));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// renderizacao do jogo:
		world.render(g);

		Collections.sort(entities, Entity.nodeSorter);// profundidade das entities

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}

		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		// Message m = new Message();
		// m.drawMessage("Teste String1234567891012345678912345678912345634234");

		// m.drawMessage("");
		// m.render(g);
		g.setColor(Color.black);

		if (debug) {
			g.drawString("F1: DEBUG = " + debug, 10, 10);
			g.drawString("F3: LIFE+1000", 10, 20);
			String testx = "x:" + player.getX();
			String testy = "y:" + player.getY();
			g.setColor(Color.black);
			g.drawString(testx, 20, 100);
			g.drawString(testy, 20, 110);
			g.drawString("F5,F6:speed:" + player.speed, 20, 220);
			// g.drawString("F1: DEBUG" , 10,10);
		}

//		g.setColor(Color.GREEN);
//		g.fillRect(0, 0, 80, 60);
//		
//		g.setFont(new Font("arial", Font.BOLD, 13));
//		g.setColor(Color.WHITE);
//		g.drawString("Teste String12345678910", 0, 55);
//		
//		

		// applyLight();
		ui.render(g);
		
		//TODO
		 //drawRectangleExample(mx/SCALE,my/SCALE);

		g.setFont(new Font("arial", Font.BOLD, 10));
		g.setColor(Color.black);
		g.drawString("vida: " + (int) player.life + "/" + player.maxLife, 20, 29);
		g.dispose();
		g = bs.getDrawGraphics();
		
		//g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
//Toolkit.getDefaultToolkit().getScreenSize()	
		if(isFullscreen) {
			g.drawImage(image, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height, null);
		}else {
			g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		}
		
		
		
		// g.setFont(new Font("arial",Font.BOLD,17));
		// g.drawString("texto nao pixelado", 10, 10); texto depois de drawImage
		g.setFont(myFont.deriveFont(16f));
		g.drawString("municao:" + player.getAmmo(), 40, 120);

		if (gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 200));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			g.setFont(new Font("arial", Font.BOLD, 28));
			g.setColor(Color.white);

			g.drawString("PERDEU", frameLoser * SCALE * 2, HEIGHT * SCALE / 2);

			g.drawString("<ENTER> para reiniciar", WIDTH, HEIGHT);

		} else if (gameState == "MENU") {
			menu.render(g);
		} else if (gameState == "WIN") {
			// TODO
		}
		/**
		 * codigo para rotacionar de acordo com o mouse Graphics2D g2 = (Graphics2D) g;
		 * int sizeRec = 160; int positionRecx = 200; int positionRecy = 200; double
		 * angleMouse = Math.atan2( positionRecx+sizeRec/2-my,
		 * positionRecy+sizeRec/2-mx);
		 * g2.rotate(angleMouse,positionRecx+sizeRec/2,positionRecy+sizeRec/2);
		 * g.setColor(Color.red); g.fillRect(positionRecx, positionRecy, sizeRec,
		 * sizeRec);
		 */

		bs.show();
	}

	private void applyLight() {
		for (int xx = 0; xx < Game.WIDTH; xx++) {
			for (int yy = 0; yy < Game.HEIGHT; yy++) {
				if (lightMapPixels[xx + (yy * WIDTH)] == 0xffffffff) {
					pixels[xx + (yy * Game.WIDTH)] = 0;
				}
			}
		}

	}

	private void drawRectangleExample(int x, int y) {
		for (int xx = 0; xx < 32; xx++) {
			for (int yy = 0; yy < 32; yy++) {
				int xOff = xx + x;
				int yOff = yy + y;
				if (xOff < 0 || yOff < 0 || xOff >= WIDTH || yOff >= HEIGHT)
					continue;
				pixels[xOff + (yOff * WIDTH)] = 0xff0000;
			}
		}

	}

	@Override
	public void run() {
		
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

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;

		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			if (gameState == "MENU") {
				menu.up = true;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			if (gameState == "MENU") {
				menu.down = true;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			saveGame = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			// restartGame = false;
			restartGame = true;
			if (gameState == "MENU") {
				menu.enter = true;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_F1) {
			if (canDebug) {
				if (debug) {
					debug = false;
				} else {
					debug = true;
				}
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_F2 && gameState == "NORMAL") {

			isPressedF2 = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_F3 && gameState == "NORMAL") {
			if (debug) {
				player.life += 1000;
				player.maxLife += 1000;
			}

		}

		if (e.getKeyCode() == KeyEvent.VK_F4 && gameState == "NORMAL") {
			if (debug) {
				player.setAmmo(player.getAmmo() + 1000);
			}

		}
		if (e.getKeyCode() == KeyEvent.VK_F5 && gameState == "NORMAL") {
			if (debug) {
				player.speed -= 0.1;
			}

		}
		if (e.getKeyCode() == KeyEvent.VK_F6 && gameState == "NORMAL") {
			if (debug) {
				player.speed += 0.1;
			}

		}
		
		if (e.getKeyCode() == KeyEvent.VK_F11) {
			this.isPressedF11 = true;
				
			
		}
		
		
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE && gameState == "NORMAL") {

			if (gameState == "MENU") {
				gameState = "NORMAL";
				menu.pause = false;
			} else {
				gameState = "MENU";
				menu.pause = true;
			}

		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {

			player.left = false;

		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {

			player.up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {

			player.down = false;
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
		player.mouseShoot = true;
		
		player.mx = arg0.getX() / SCALE;
		player.my = arg0.getY() / SCALE;
		
	   //player.mx = arg0.getX();
      //player.my = arg0.getY();
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		this.mx = arg0.getX();
		this.my = arg0.getY();

	}

}
