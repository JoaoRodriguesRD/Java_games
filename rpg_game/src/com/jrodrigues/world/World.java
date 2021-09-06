package com.jrodrigues.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import com.jrodrigues.entities.Bullet;
import com.jrodrigues.entities.Enemy;
import com.jrodrigues.entities.Enemy_2;
import com.jrodrigues.entities.Entity;
import com.jrodrigues.entities.LifePoint;
import com.jrodrigues.entities.Particle;
import com.jrodrigues.entities.Player;
import com.jrodrigues.entities.Weapon;
import com.jrodrigues.graphics.Spritesheet;
import com.jrodrigues.main.Game;

public class World {

	public static Tile[] tiles;

	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;

	private BufferedImage[] floor;
	
	//world random generator
	public World(String path,int seed) {
		
		Game.player.setX(0);
		Game.player.setY(0);
		WIDTH = 100;
		HEIGHT = 100;
		tiles = new Tile[WIDTH*HEIGHT];
		
		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				tiles[xx + yy * WIDTH] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
				
			}
		}
		
		int dir = 0;
		int xx = 0, yy = 0;
		
		for(int i = 0;i<200;i++) {
			tiles[xx+yy*WIDTH] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
			if(dir == 0) {// direita
				
				if(xx < WIDTH) {
					xx++;
				}
			}else if(dir == 1) {//esquerda
				
				if(xx > 0) {
					xx--;
				}
			}else if(dir == 2) {//baixo
				if(yy < HEIGHT) {
					yy++;
				}
			}else if(dir == 3) {//cima
				if(yy > 0) {
					yy--;
				}
			}
			
			if(Game.rand.nextInt(100) <30) {
				dir = Game.rand.nextInt(4);
			}
			
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public World(String path) {
		
		floor = new BufferedImage[3];
		floor[0] = Tile.TILE_FLOOR;
		floor[1] = Tile.TILE_FLOOR1;
		floor[2] = Tile.TILE_FLOOR2;
		Random r = new Random();
		r.setSeed(10);

		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			tiles = new Tile[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());

			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];

					// PADRAO
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);

					if (pixelAtual == 0xFF000000) {// chao
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, floor[r.nextInt(3)]);
						// tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					} else if (pixelAtual == 0xFFFFFFFF) {// parede
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
					} else if (pixelAtual == 0xFF0000FF) {// player
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
						// tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);

					} else if (pixelAtual == 0xFFFF0000) {// inimigo
						Enemy en = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					} else if (pixelAtual == 0xFF808080) {// arma
						// tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Entity.WEAPON_EN);
						Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_RIGHT_EN));
					} else if (pixelAtual == 0xFFFFFF00) {// balas
						// tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Entity.BULLET_EN);
						Game.entities.add(new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET_EN));
					} else if (pixelAtual == 0xFF00FFFF) {// vida
						// tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16,
						// Entity.LIFEPOINT_EN);
						Game.entities.add(new LifePoint(xx * 16, yy * 16, 16, 16, Entity.LIFEPOINT_EN));
					} else if (pixelAtual == 0xFF00FF21) {// 00FF21 - verde, inimigo_2
						Enemy_2 en = new Enemy_2(xx * 16, yy * 16, 16, 16, Entity.ENEMY_2_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					}

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void generateParticles(int amount, int x, int y) {
		for(int i = 0; i < amount; i++) {
			Game.entities.add(new Particle(x,y,1,1,null));
		}
	}
	
	/*
	 * public static boolean isFree(double xNext_d, double yNext_d) { int xNext =
	 * (int) xNext_d; int yNext = (int) yNext_d; int x1 = xNext / TILE_SIZE; int y1
	 * = yNext / TILE_SIZE;
	 * 
	 * int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE; int y2 = (yNext + TILE_SIZE) /
	 * TILE_SIZE;
	 * 
	 * int x3 = (xNext + TILE_SIZE) / TILE_SIZE; int y3 = (yNext + TILE_SIZE - 1) /
	 * TILE_SIZE;
	 * 
	 * int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE; int y4 = (yNext + TILE_SIZE -
	 * 1) / TILE_SIZE;
	 * 
	 * 
	 * int x5_c = (int) Math.pow(xNext^2+yNext^2, 0.5); int y5_c = (int)
	 * Math.pow(xNext^2+yNext^2, 0.5);
	 * 
	 * int x5 = (x5_c + TILE_SIZE - 1) / TILE_SIZE; int y5 = (y5_c + TILE_SIZE) /
	 * TILE_SIZE;
	 * 
	 * int x6 = (x5_c + TILE_SIZE ) / TILE_SIZE; int y6 = (y5_c + TILE_SIZE - 1) /
	 * TILE_SIZE;
	 * 
	 * return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) || (tiles[x2 +
	 * (y2*World.WIDTH)] instanceof WallTile) || (tiles[x3 + (y3*World.WIDTH)]
	 * instanceof WallTile) || (tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
	 * }
	 */
	public static boolean isFree(int xnext, int ynext) {

		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;

		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));
	}
	
	public static boolean isFreeDynamic(int xnext, int ynext, int width, int height) {

		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		int x2 = (xnext + width - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;

		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + height - 1) / TILE_SIZE;

		int x4 = (xnext + width - 1) / TILE_SIZE;
		int y4 = (ynext + height - 1) / TILE_SIZE;

		return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));
	}
	
	public static void restartGame(String level){
		Game.entities.clear();
		Game.enemies.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet3.png");
		Game.player = new Player(0,0,16,16,Game.spritesheet.getSprite(32, 0,16,16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		return;
	}

	
	
	
	public void render(Graphics g) {

		// int xStart = Camera.x / 16;
		int xStart = Camera.x >> 4;
		int yStart = Camera.y >> 4;

		int xFinal = xStart + (Game.WIDTH >> 4);
		int yFinal = yStart + (Game.HEIGHT >> 4)+1;

		for (int xx = xStart; xx <= xFinal; xx++) {
			for (int yy = yStart; yy <= yFinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);

			}

		}
	}

}
