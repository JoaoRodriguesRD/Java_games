package com.jrodrigues.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.jrodrigues.main.Game;
import com.jrodrigues.main.Sound;
import com.jrodrigues.world.Camera;
import com.jrodrigues.world.World;

public class Enemy_2 extends Enemy {
	private double speed = 2.5;
	private Random r;

	public int damage = 1;
	public int timeDamage = 0;

	public int life = 3;

	public int frames = 0, maxFrames = 2;
	public int index = 0, maxIndex = 1;
	private BufferedImage[] sprites;
	private BufferedImage[] spritesStop;
	private BufferedImage[] spritesDead;
	public int framesStop = 0, maxFramesStop = 10;
	public int indexStop = 0, maxIndexStop = 7;
	private boolean moved = false;
	private boolean dead = false;
	private int deadFrame = 0, maxDeadFrames = 10;
	int indexDead = 0, maxIndexDead = 3;

	int maskX = 5, maskY = 6, maskWi = 6, maskHe = 4;
	protected int maskX_view = 0, maskY_view = 0, maskWH_view = 64, calc = -8;
	protected boolean debug = false;
	
	public boolean angry = false;
	
	
	public Enemy_2(int x, int y, int wight, int height, BufferedImage sprite) {
		super(x, y, wight, height, null);

		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(0, 48, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(16, 48, 16, 16);
		// 0,48

		spritesStop = new BufferedImage[8];
		spritesStop[0] = Game.spritesheet.getSprite(0, 48, 16, 16);
		spritesStop[1] = Game.spritesheet.getSprite(16, 48, 16, 16);
		spritesStop[2] = Game.spritesheet.getSprite(32, 48, 16, 16);
		spritesStop[3] = Game.spritesheet.getSprite(48, 48, 16, 16);

		spritesStop[4] = Game.spritesheet.getSprite(0, 64, 16, 16);
		spritesStop[5] = Game.spritesheet.getSprite(16, 64, 16, 16);
		spritesStop[6] = Game.spritesheet.getSprite(32, 64, 16, 16);
		spritesStop[7] = Game.spritesheet.getSprite(48, 64, 16, 16);

		spritesDead = new BufferedImage[4];
		spritesDead[0] = Game.spritesheet.getSprite(0, 80, 16, 16);
		spritesDead[1] = Game.spritesheet.getSprite(16, 80, 16, 16);
		spritesDead[2] = Game.spritesheet.getSprite(32, 80, 16, 16);
		spritesDead[3] = Game.spritesheet.getSprite(48, 80, 16, 16);

	}

	public void tick() {
		r = new Random();
		moved = false;
		if (!dead) {
			if (isCollidingWithPlayer() == false && isCatchPlayer()) {
				if (r.nextInt(100) < 30) {
					if ((int) x < Game.player.getX() && World.isFree((int) (x + speed), this.getY())
							&& !isColliding((int) (x + speed), this.getY())) {
						x += speed;
						moved = true;

					} else if ((int) x > Game.player.getX() && World.isFree((int) (x - speed), this.getY())
							&& !isColliding((int) (x - speed), this.getY())) {
						x -= speed;
						moved = true;

					}

					if ((int) y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed))
							&& !isColliding(this.getX(), (int) (y + speed))) {
						y += speed;
						moved = true;

					} else if ((int) y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed))
							&& !isColliding(this.getX(), (int) (y - speed))) {
						y -= speed;
						moved = true;

					}

				}
			} else {

			}
			if (isCollidingWithPlayer()) {
				timeDamage++;
				if (timeDamage == 12) {
					Game.player.hit(damage);
					timeDamage = 0;
				}
			}

			if (moved) {
				frames++;
				if (frames == maxFrames) {
					frames = 0;
					index++;
					if (index > maxIndex) {
						index = 0;
					}
				}
			} else {
				framesStop++;
				if (framesStop == maxFramesStop) {
					framesStop = 0;
					indexStop++;
					if (indexStop > maxIndexStop) {
						indexStop = 0;
					}
				}
			}

			if (isDamaged) {
				damageFrames++;
				if (damageFrames == 10) {
					damageFrames = 0;
					isDamaged = false;
				}
			}

			CollidingBullet();

		} else {// when is dead
			deadFrame++;
			
			if (deadFrame == maxDeadFrames) {
				deadFrame= 0;
				indexDead++;
				if(indexDead > maxIndexDead) {
				destroySelf();
				return;
				}
				
			}
		}

		if (life <= 0) {
			dead = true;
			// destroySelf();
			// return;

		}

	}

	public boolean isCatchPlayer() {
		if(angry) {
			return true;
		}
		Rectangle enemyCur = new Rectangle(this.getXPosition() - maskWH_view / 4 + calc,
				this.getYPosition() - maskWH_view / 4 + calc, maskWH_view, maskWH_view);
		
		Rectangle player = new Rectangle(Game.player.getX() - Camera.x + 5, Game.player.getY() - Camera.y + 3, 6, 14);
		return enemyCur.intersects(player);

	}

	public boolean isColliding(int xNext, int yNext) {
//		Rectangle enemyCurrent = new Rectangle(xNext + maskX, yNext + maskY, maskWi, maskHe);
//		for (int i = 0; i < Game.enemies.size(); i++) {
//			Enemy e = Game.enemies.get(i);
//			if (e == this) {
//				continue;
//			}
//			Rectangle targetEnemy = new Rectangle(e.getX() + maskX, e.getY() + maskY, maskWi, maskHe);
//
//			if (enemyCurrent.intersects(targetEnemy)) {
//				return true;
//			} else {
//				continue;
//			}
//
//		}

		return false;

	}

	protected void destroySelf() {
		Game.entities.remove(this);
		Game.enemies.remove(this);

	}

	public void CollidingBullet() {
		for (int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if (e instanceof BulletShoot) {
				if (Entity.isColidding(this, e)) {
					life--;
					Game.bullets.remove(i);
					isDamaged = true;
					Sound.hurtSound.play();
					angry = true;
					return;
				}

			}
		}

	}

	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskX, this.getY() + maskY, maskWi, maskHe);

		int mask_player_x = 5;
		int mask_player_y = 3;
		Rectangle player = new Rectangle(Game.player.getX() + mask_player_x, Game.player.getY() + mask_player_y, 6, 14);
		return enemyCurrent.intersects(player);
	}

	public void render(Graphics g) {
		if (Game.debug) {
			// g.fillRect(this.getX() - Camera.x + maskX, this.getY() - Camera.y + maskY,
			// maskWi, maskHe);
			g.setColor(Color.green);
			g.fillRect(this.getXPosition() - maskWH_view / 4 + calc, this.getYPosition() - maskWH_view / 4 + calc,
					maskWH_view, maskWH_view);
			g.setColor(Color.blue);
			g.fillRect(this.getX() - Camera.x + maskX, this.getY() - Camera.y + maskY, maskWi, maskHe);
		}
		if (!dead) {
			if (moved) {
				g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);

			} else {
				g.drawImage(spritesStop[indexStop], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}

			if (isDamaged) {
				g.drawImage(Entity.ENEMY_2_EN_FEEDBACK, getXPosition(), getYPosition(), null);
			}
		} else {
			g.drawImage(spritesDead[indexDead], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
}
