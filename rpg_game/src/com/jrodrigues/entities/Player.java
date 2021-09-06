package com.jrodrigues.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import com.jrodrigues.main.Game;
import com.jrodrigues.world.Camera;
import com.jrodrigues.world.World;

public class Player extends Entity {

	public boolean right, up, left, down;
	public double speed = 1.1;

	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public boolean moved = false;
	public int frames = 0, maxFrames = 40;
	public int index = 0, maxIndex = 2 - 1;

	public BufferedImage sprite;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage stoppedPlayer;
	private BufferedImage playerDamage;
	
	

	public double life;
	public int maxLife = 100;

	private int ammo = 0;
	private boolean hasGun = false;

	public boolean shoot = false;
	public boolean mouseShoot = false;
	public int frameGun = 0;
	public boolean ableToShoot = true;
	
	public int mx, my;
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	
	public Player(int x, int y, int wight, int height, BufferedImage sprite) {
		super(x, y, wight, height, sprite);
		this.life = maxLife;
		Game.entities.add(this);
		rightPlayer = new BufferedImage[2];
		leftPlayer = new BufferedImage[2];
		rightPlayer[0] = Game.spritesheet.getSprite(48, 0, 16, 16);
		rightPlayer[1] = Game.spritesheet.getSprite(48, 16, 16, 16);
		leftPlayer[0] = Game.spritesheet.getSprite(32, 0, 16, 16);
		leftPlayer[1] = Game.spritesheet.getSprite(32, 16, 16, 16);
		stoppedPlayer = Game.spritesheet.getSprite(64, 0, 16, 16);
		playerDamage = Game.spritesheet.getSprite(32, 32, 16, 16);
		this.setMask(5, 3, 6, 14);
	}
	
	//construtor para salvar o mesmo player
	public Player(int x, int y, int wight, int height, BufferedImage sprite,double life,int maxLife,int ammo,boolean hasGun) {
		super(x, y, wight, height, sprite);
		this.life = maxLife;
		Game.entities.add(this);
		rightPlayer = new BufferedImage[2];
		leftPlayer = new BufferedImage[2];
		rightPlayer[0] = Game.spritesheet.getSprite(48, 0, 16, 16);
		rightPlayer[1] = Game.spritesheet.getSprite(48, 16, 16, 16);
		leftPlayer[0] = Game.spritesheet.getSprite(32, 0, 16, 16);
		leftPlayer[1] = Game.spritesheet.getSprite(32, 16, 16, 16);
		stoppedPlayer = Game.spritesheet.getSprite(64, 0, 16, 16);
		playerDamage = Game.spritesheet.getSprite(32, 32, 16, 16);
		this.life = life;
		this.maxLife = maxLife;
		this.ammo = ammo;
		this.hasGun = hasGun;
		this.setMask(5, 3, 6, 14);
	}
	
//	public Player(Player p) {
//		super((int)p.x, (int)p.y, p.width, p.height, p.sprite);
//		this = p;
//		Game.entities.add(this);
//		this.life = life;
//		this.maxLife = maxLife;
//		this.ammo = ammo;
//		this.hasGun = hasGun;
//		this.setMask(5, 3, 6, 14);
//	}
	

	public void hit(int damage) {
		isDamaged = true;
		if (damage >= life) {
			// System.exit(1);
			//Game.reset();
			Game.gameState = "GAME_OVER";
			life = 0;
		} else {
			life -= damage;
		}
	}

	public void restoreLife(int point) {
		if (point + life >= maxLife) {
			life = maxLife;
		} else {
			life += point;
		}
	}
	public boolean hasGun() {
		return hasGun;
	}

	public int getAmmo() {
		return ammo;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public void tick() {
		
		depth = 1;
		
		
		frameGun++;
		if(frameGun == 30) {
			frameGun = 0;
			ableToShoot = true;
		}
		
		
		if (life <= 0) {
			// System.exit(1);
		}

		moved = false;
		
		
		
		if (right && World.isFree((int) (x + speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x += speed;
		} else if (left && World.isFree((int) (x - speed), this.getY())) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}
		if (up && World.isFree(this.getX(), (int) (y - speed))) {
			moved = true;
			y -= speed;
		} else if (down && World.isFree(this.getX(), (int) (y + speed))) {
			moved = true;
			y += speed;
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
		}

		if (isDamaged) {
			damageFrames++;
			if (damageFrames == 10) {
				damageFrames = 0;
				isDamaged = false;
			}
		}
		
		
		
		
//		if (shoot && hasGun && ammo > 0) {
//
//			ammo--;
//			shoot = false;
//			int dx = 0;
//			int px = 0;
//			int py = 0;
//			if (moved) {
//				if (dir == right_dir) {
//					dx = 1;
//					px = 16;
//					py = 5;
//				} else {
//					dx = -1;
//					px = -5;
//					py = 5;
//				}
//			} else {
//				if (dir == right_dir) {
//					dx = 1;
//					px = 0;
//					py = 0;
//				} else {
//					dx = -1;
//					px = 3;
//					py = 0;
//				}
//			}
//			BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);
//			Game.bullets.add(bullet);
//
//		} else {
//			shoot = false;
//		}

		if (mouseShoot && hasGun && ammo > 0 && ableToShoot) {
			runGun();
			mouseShoot = false;
			ammo--;
			
			int px = 2,py =2;
			double angle = 0;
			if(moved) {
			if(dir == right_dir) {
				px = 17;
				py = 4;
			}else {
				px = -8;
				py = 4;
			}
			}
			angle = Math.atan2(my - (this.getY()+py - Camera.y),mx - (this.getX()+px - Camera.x));
			double dx = Math.cos(angle);
			double dy = Math.sin(angle);
			
			BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py,30,30,null,dx,dy);
			
			BulletShoot bullet2 = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,0.4,-0.4);
			
			BulletShoot bullet3 = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,-0.4,0.4);
			
			BulletShoot bullet4 = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,-0.4,-0.4);
			
			BulletShoot bullet5 = new BulletShoot(this.getX(),this.getY(),3,3,null,0.4,0.4);
			
			BulletShoot bullet6 = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,0,0.4);
			
			BulletShoot bullet7 = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,0,-0.4);
			
			BulletShoot bullet8 = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,0.4,0);
			
			BulletShoot bullet9 = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,-0.4,0);
			Game.bullets.add(bullet);
			Game.bullets.add(bullet2);
			Game.bullets.add(bullet3);
			Game.bullets.add(bullet4);
			Game.bullets.add(bullet5);
			Game.bullets.add(bullet6);
			Game.bullets.add(bullet7);
			Game.bullets.add(bullet8);
			Game.bullets.add(bullet9);

			
		}else {
			mouseShoot = false;
		}

		// Camera.x = (int) ( (this.getX() - Game.WIDTH/2));
		// Camera.y = (int) (this.getY() - Game.HEIGHT/2);

		// limitar a camera
		updateCamera();

		checkCollisionLifePoint();
		checkCollisionAmmo();
		checkCollisionGun();
		
	}
	
	public void updateCamera() {
		Camera.x = Camera.Clamp(this.getX() - (Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.Clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
	}

	private void runGun() {
		// TODO Auto-generated method stub
		ableToShoot = false;
	}

	public void checkCollisionGun() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity actual = Game.entities.get(i);
			if (actual instanceof Weapon) {
				if (Entity.isColidding(this, actual)) {
					hasGun = true;
					Game.entities.remove(i);
				}
			}
		}
	}

	public void checkCollisionAmmo() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity actual = Game.entities.get(i);
			if (actual instanceof Bullet) {
				if (Entity.isColidding(this, actual)) {
					ammo += 38;
					Game.entities.remove(i);
				}
			}
		}
	}

	public void checkCollisionLifePoint() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity actual = Game.entities.get(i);
			if (actual instanceof LifePoint) {
				if (LifePoint.isColidding(this, actual) && life < maxLife  ) {
					((LifePoint) actual).getted();
					this.restoreLife(((LifePoint) actual).points);
					
					
					//Game.entities.remove(i);
				}
			}
		}
	}

	public void render(Graphics g) {
		int xPosition = (int) this.getX() - Camera.x;
		int yPosition = (int) this.getY() - Camera.y;
		
		if(Game.debug) {
			g.setColor(Color.cyan);
			g.fillRect(xPosition+maskx, yPosition+masky,mwidth,mheight);
		}
		
		if (!isDamaged) {
			if (moved) {

				if (dir == right_dir) {
					g.drawImage(rightPlayer[index], xPosition, yPosition, null);
					if (hasGun) {
						g.drawImage(Entity.WEAPON_RIGHT_EN, xPosition + 10, yPosition, null);
					}
				} else if (dir == left_dir) {
					g.drawImage(leftPlayer[index], xPosition, yPosition, null);
					if (hasGun) {
						g.drawImage(Entity.WEAPON_LEFT_EN, xPosition - 9, yPosition, null);
					}
				}

			} else {
				g.drawImage(stoppedPlayer, xPosition, yPosition, null);
				
				if (hasGun) {
					g.drawImage(Entity.WEAPON_DOWN_EN, xPosition, yPosition, null);
				}
			}
		} else {
			g.drawImage(playerDamage, xPosition, yPosition, null);
		}
	}

	public double getLife() {
		return life;
	}

	public void setLife(double life) {
		this.life = life;
	}

	public int getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}
	
	public int getXPosition() {
		return this.getX() - Camera.x;
	}
	public int getYPosition() {
		return this.getY() - Camera.y;
	}

}
