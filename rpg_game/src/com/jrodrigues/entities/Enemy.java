package com.jrodrigues.entities;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.jrodrigues.main.Game;
import com.jrodrigues.world.AStar;
import com.jrodrigues.world.Camera;
import com.jrodrigues.world.Vector2i;
import com.jrodrigues.world.World;

public class Enemy extends Entity {

	private double speed = 0.9;

	
	public int frameAttack = 0;
	public int damage = 10;
	
	public int frames = 0, maxFrames = 60;
	public int index = 0, maxIndex = 2 - 1;
	private BufferedImage[] sprites;
	
	public int  life = 5;
	
	// mascaras
	protected int maskX = 3, maskY = 4, maskWi = 10, maskHe = 10;
	//mascara de proximidade
	protected int maskX_view = 0, maskY_view = 0, maskWH_view = 128, calc = -24;


	protected boolean debug = false;


	private int timeDamage = 10;
	protected boolean isDamaged = false;
	protected int damageFrames;
	
	public boolean angry = false;
	
	public Enemy(int x, int y, int wight, int height, BufferedImage sprite) {
		super(x, y, wight, height, null);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(128, 0, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(128, 16, 16, 16);
		
		this.setMask(maskX,maskY,maskWi,maskHe);
	}

	public void tick() {
		depth = 0;
		/*
		if(this.calculateDistance(this.getX(), getY(), Game.player.getX(), Game.player.getY()) < 50||angry) {
		if (isCollidingWithPlayer() == false) {
			if ((int) x < Game.player.getX() && World.isFree((int) (x + speed), this.getY())
					&& !isColliding((int) (x + speed), this.getY())) {
				x += speed;

			} else if ((int) x > Game.player.getX() && World.isFree((int) (x - speed), this.getY())
					&& !isColliding((int) (x - speed), this.getY())) {
				x -= speed;

			}

			if ((int) y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed))
					&& !isColliding(this.getX(), (int) (y + speed))) {
				y += speed;

			} else if ((int) y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed))
					&& !isColliding(this.getX(), (int) (y - speed))) {
				y -= speed;

			}
		}
		
		}
		
		*/
		
		
		if(angry && isCollidingWithPlayer() == false) {
			if(x % 16 == 0 && y % 16 == 0) {
				if(new Random().nextInt(100) < 10) {
					Vector2i start = new Vector2i(((int)(x/16)),((int)(y/16)));
					Vector2i end = new Vector2i(((int)(Game.player.x/16)),((int)(Game.player.y/16)));
					path = AStar.findPath(Game.world, start, end);
				}
			}
			followPath(path);
		}
		
		
		
		
		
		if(isCollidingWithPlayer()) {
			timeDamage++;
			
			if(timeDamage >= 43) {
				Game.player.hit(damage);
				timeDamage = 0;
			}
		}

		
		
		if (true) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
		}
		
		CollidingBullet();
		
		if (isDamaged) {
			damageFrames++;
			if (damageFrames == 10) {
				damageFrames = 0;
				isDamaged = false;
			}
		}
		
		
		if(life <=0) {
			destroySelf();
			return;
		}
		
//		Random r = new Random();
//		x += r.nextInt(2);
//		y += r.nextInt(2);
//		x -= r.nextInt(2);
//		y -= r.nextInt(2);
	}
	
	protected void destroySelf() {
		Game.entities.remove(this);
		Game.enemies.remove(this);
		
	}

	public void CollidingBullet() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if(e instanceof BulletShoot) {
				if(Entity.isColidding(this, e)) {
					life--;
					Game.bullets.remove(i);
					isDamaged = true;
					angry = true;
					return;
				}
				
			}
		}
		
	}

	public boolean isCatchPlayer() {
		//Circle enemyCur = new Cicle();
		//g.drawOval(100, 110, 11, 11);
		//Shape enemyCur = new Ellipse2D.Double(this.getX() - Camera.x - w/2, this.getY() - Camera.y - h/2, w, h);
		if(angry) {
			return true;
		}
		Rectangle enemyCur = new Rectangle(this.getXPosition()-maskWH_view/4 +calc, this.getYPosition()-maskWH_view/4 +calc, maskWH_view, maskWH_view);
		Rectangle player = new Rectangle(Game.player.getX() - Camera.x+5, Game.player.getY() - Camera.y+3,6,14);
		return enemyCur.intersects(player);
		
	}
	
	
	
	
	

	public void render(Graphics g) {
		super.render(g);
		if(Game.debug) {
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskWi, maskHe);
			
			g.setColor(Color.red);
			//this.getXPosition()-maskWH_view/4 +calc, this.getYPosition()-maskWH_view/4 +calc, maskWH_view, maskWH_view
			
			g.fillRect(this.getXPosition()-maskWH_view/4 +calc, this.getYPosition()-maskWH_view/4 +calc, maskWH_view, maskWH_view);
			
			g.setColor(Color.blue);
			 g.fillRect(this.getX() - Camera.x + maskX, this.getY() - Camera.y + maskY,
			 maskWi, maskHe);
			
		}
		if(!isDamaged) {
			g.drawImage(sprites[index], getXPosition(), getYPosition(),null);
		}else {
			g.drawImage(Entity.ENEMY_EN_FEEDBACK, getXPosition(), getYPosition(),null);
		}
		
		 
		
		
		
	}
	
	public int getXPosition() {
		return this.getX() - Camera.x;
	}
	public int getYPosition() {
		return this.getY() - Camera.y;
	}

}
