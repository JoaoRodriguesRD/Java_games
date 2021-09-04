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
	public double speed = 1.0;

	public int right_dir = 0, left_dir = 1,up_dir = 2,down_dir=3;

	public int dir = right_dir;
	public boolean moved = false;
	
	public boolean wannaMove = false;
	public boolean respectTile = true;
	
	public boolean pressedMove = false;
	
	public int framesWannaMove = 0;
	public int maxFramesMoving = 10;
	
	
	public int frames = 0, maxFrames = 5;
	public int index = 0, maxIndex = 2 - 1;

	public BufferedImage sprite;

	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;

	private BufferedImage playerDamage;

	public double life;
	public int maxLife = 100;
	
	public int grassCutted = 0;
	
	public int mx, my;

	public Player(int x, int y, int wight, int height, BufferedImage sprite) {
		super(x, y, wight, height, sprite);
		this.life = maxLife;
		Game.entities.add(this);
		rightPlayer = new BufferedImage[2];
		leftPlayer = new BufferedImage[2];
		upPlayer = new BufferedImage[2];
		downPlayer = new BufferedImage[2];
		
		rightPlayer[0] = Game.spritesheet.getSprite(0, 146, 18, 14);
		rightPlayer[1] = Game.spritesheet.getSprite(18, 146, 18, 14);

		leftPlayer[0] = Game.spritesheet.getSprite(0, 130, 18, 14);
		leftPlayer[1] = Game.spritesheet.getSprite(18, 130, 18, 14);

		upPlayer[0] = Game.spritesheet.getSprite(0, 80, 16, 20);
		upPlayer[1] = Game.spritesheet.getSprite(16, 80, 16, 20);
		
		downPlayer[0] = Game.spritesheet.getSprite(0, 108, 16, 20);
		downPlayer[1] = Game.spritesheet.getSprite(16, 108, 16, 20);

		playerDamage = Game.spritesheet.getSprite(32, 32, 16, 16);
		this.setMask(0, 0, 16, 16);
		grassCutted = 0;
	}

	


	public void restoreLife(int point) {
		if (point + life >= maxLife) {
			life = maxLife;
		} else {
			life += point;
		}
	}

	public void tick() {

		depth = 1;
		
		
		this.setMask(0, 0, 16, 16);
		
		
		if (life <= 0) {
			// System.exit(1);
		}

		moved = false;
		
		
		
		
		
		
		
		if(right || left || up || down) {
			wannaMove = true;
		}
		
		
		if (right  && World.isFree((int) (x + speed), this.getY())&&y % World.TILE_SIZE == 0) {
			moved = true;
			dir = right_dir;
			x += speed;
			
		} else if (left  && World.isFree((int) (x - speed), this.getY())&&y % World.TILE_SIZE == 0) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}else if (up  && World.isFree(this.getX(), (int) (y - speed))&& x % World.TILE_SIZE == 0) {
			moved = true;
			dir = up_dir;
			y -= speed;
		} else if (down  && World.isFree(this.getX(), (int) (y + speed))&& x % World.TILE_SIZE == 0) {
			moved = true;
			dir = down_dir;
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
		
		
		
		readjustment();		
		updateCamera();

	}


	private void readjustment() {	
		if(dir == right_dir) {
			if(x % World.TILE_SIZE != 0) {
				x += speed;
			}
		}else if(dir == left_dir) {
			if(x % World.TILE_SIZE != 0) {
				x -= speed;
			}
		}else if(dir == down_dir) {
			if(y % World.TILE_SIZE != 0) {
				y += speed;
			}
		}else if(dir == up_dir) {
			if(y % World.TILE_SIZE != 0) {
				y -= speed;
			}
		}
		
		
	}




	public void updateCamera() {
		Camera.x = Camera.Clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.Clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}

	public void render(Graphics g) {
		int xPosition = (int) this.getX() - Camera.x;
		int yPosition = (int) this.getY() - Camera.y;
		
		
		if(Game.gameState == "NORMAL") {
			if (dir == right_dir) {
				g.drawImage(rightPlayer[index], xPosition-2, yPosition, null);

			} else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], xPosition, yPosition, null);

			}else if (dir == up_dir) {
				g.drawImage(upPlayer[index], xPosition, yPosition, null);

			}else if (dir == down_dir) {
				g.drawImage(downPlayer[index], xPosition, yPosition-4, null);

			}else {
				g.drawImage(rightPlayer[0], xPosition, yPosition, null); //parado mostra isso
			}
		}
		
		
//		
//		g.setColor(Color.cyan);
//		g.fillRect(xPosition+maskx, yPosition+masky,mwidth,mheight);
			
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
