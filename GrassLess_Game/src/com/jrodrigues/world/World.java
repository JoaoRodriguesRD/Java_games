package com.jrodrigues.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import com.jrodrigues.entities.Entity;
import com.jrodrigues.entities.Grass;
import com.jrodrigues.entities.Particle;
import com.jrodrigues.entities.Player;
import com.jrodrigues.entities.Weapon;
import com.jrodrigues.graphics.Spritesheet;
import com.jrodrigues.main.Game;

public class World {

	public static Tile[] tiles;

	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;

	private BufferedImage floor;
	
	
	
	public World(String path) {
		
		
		floor = Tile.TILE_FLOOR;
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
						//tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, floor[r.nextInt(3)]);
						 tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					} else if (pixelAtual == 0xFFFFFFFF) {// parede
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
					} else if (pixelAtual == 0xFF0000FF) {// player
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
						// tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);

					} else if (pixelAtual == 0xFF00FF00) {// grama
						Grass gr = new Grass(xx * 16, yy * 16, 16, 16, Entity.GRASS);
						//Game.entities.add(gr);
						Game.grass.add(gr);
					} else if (pixelAtual == 0xFF808080) {// arma
						// tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Entity.WEAPON_EN);
						//Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_RIGHT_EN));
					} else if (pixelAtual == 0xFFFFFF00) {// balas
						// tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Entity.BULLET_EN);
						//Game.entities.add(new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET_EN));
					} else if (pixelAtual == 0xFF00FFFF) {// vida
						// tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16,
						// Entity.LIFEPOINT_EN);
						//Game.entities.add(new LifePoint(xx * 16, yy * 16, 16, 16, Entity.LIFEPOINT_EN));
					} else if (pixelAtual == 0xFF00FF21) {// 00FF21 - verde, inimigo_2
						//Enemy_2 en = new Enemy_2(xx * 16, yy * 16, 16, 16, Entity.ENEMY_2_EN);
						//Game.entities.add(en);
						//Game.enemies.add(en);
					}

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void generateParticles(int amount, int x, int y, Color c) {
		for(int i = 0; i < amount; i++) {
			Game.entities.add(new Particle(x,y,1,1,c,null));
		}
	}
	
	
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
		//Game.enemies.clear();
		Game.entities = new ArrayList<Entity>();
		//Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
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
