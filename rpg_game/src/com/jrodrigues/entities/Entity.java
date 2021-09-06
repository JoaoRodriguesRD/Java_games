package com.jrodrigues.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

import com.jrodrigues.main.Game;
import com.jrodrigues.world.Camera;
import com.jrodrigues.world.Node;
import com.jrodrigues.world.Vector2i;

public class Entity {

	public static BufferedImage LIFEPOINT_EN = Game.spritesheet.getSprite(5 * 16, 0, 16, 16);
	
	public static BufferedImage WEAPON_RIGHT_EN = Game.spritesheet.getSprite(6 * 16, 0, 16, 16);
	public static BufferedImage WEAPON_LEFT_EN = Game.spritesheet.getSprite(6 * 16, 16, 16, 16);
	public static BufferedImage WEAPON_DOWN_EN = Game.spritesheet.getSprite(6 * 16, 32, 16, 16);
	
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(7 * 16, 0, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(8 * 16, 0, 16, 16);
	public static BufferedImage ENEMY_EN_FEEDBACK = Game.spritesheet.getSprite(8 * 16, 32, 16, 16);
	
	
	public static BufferedImage ENEMY_2_EN = Game.spritesheet.getSprite(9 * 16, 0, 16, 16);
	public static BufferedImage ENEMY_2_EN_FEEDBACK = Game.spritesheet.getSprite(9 * 16, 64, 16, 16);
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	protected List<Node> path;
	
	
	private BufferedImage sprite;

	public int maskx, masky, mwidth, mheight;

	public int depth;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;

		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}

	public void setMask(int maskx, int masky, int mwidth, int mheight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}

	public int getX() {
		return (int) x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return (int) y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void tick() {
	}
	
	public double calculateDistance(int x1,int y1,int x2,int y2) {	
		return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
	}
	
	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, mwidth, mheight);
		
		int mask_player_x = 5;
		int mask_player_y = 3;
		Rectangle player = new Rectangle(Game.player.getX() + mask_player_x, Game.player.getY()+mask_player_y,6,14);
	
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColliding(int xNext, int yNext) {
		Rectangle enemyCurrent = new Rectangle(xNext + maskx, yNext + masky, mwidth, mheight);
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this) {
				continue;
			}
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, mwidth, mheight);

			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			} else {
				continue;
			}

		}

		return false;

	}
	
	public void followPath(List<Node> path) {
		if(path != null) {
			if(path.size()>0) {
				Vector2i target = path.get(path.size()-1).tile;
				//xprev = x;
				//yprev = y;
				if(x < target.x *16) {
					x++;
					
				}else if(x > target.x*16 ) {
					x--;
				}
				
				if(y<target.y*16) {
					y++;
				}else if(y>target.y*16) {
					y--;
				}
				
				if(x == target.x *16 && y == target.y*16) {
					path.remove(path.size()-1);
					
				}
				
				
			}
		}
	}
	
	public static boolean isColidding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mwidth, e2.mheight);
		return e1Mask.intersects(e2Mask);
	}

	public void render(Graphics g) {

		g.drawImage(sprite, (int) getX() - Camera.x, (int) getY() - Camera.y, null);

//		if (Game.debug) {
//			g.setColor(Color.red);
//			g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, mwidth, mheight);
//		}

	}
	
	public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {

		@Override
		public int compare(Entity n0, Entity n1) {
			if (n1.depth < n0.depth)
				return +1;

			if (n1.depth > n0.depth)
				return -1;

			return 0;
		}

	};

}
