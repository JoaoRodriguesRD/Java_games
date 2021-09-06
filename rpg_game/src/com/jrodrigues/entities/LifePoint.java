package com.jrodrigues.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.jrodrigues.main.Game;
import com.jrodrigues.world.Camera;

public class LifePoint extends Entity {

	public int frames = 0, maxFrames = 7;
	public int index = 0, maxIndex = 5 - 1;
	
	public int framesg = 0, maxFramesg = 4;
	
	private BufferedImage[] sprites;
	private BufferedImage getPoint;
	private boolean getted = false;
	
	public int points = 10;
	public LifePoint(int x, int y, int wight, int height, BufferedImage sprite) {
		super(x, y, wight, height, sprite);
		//0,48
		sprites = new BufferedImage[5];
		sprites[0] = Game.spritesheet.getSprite(0, 96, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(16, 96, 16, 16);
		sprites[2]= Game.spritesheet.getSprite(32, 96, 16, 16);
		sprites[3]= Game.spritesheet.getSprite(48, 96, 16, 16);
		sprites[4]= Game.spritesheet.getSprite(64, 96, 16, 16);
		
		getPoint = Game.spritesheet.getSprite(80, 96, 16, 16);
		
		
		this.setMask(2, 4, 12, 11);
	}

	public void tick() {
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index > maxIndex) {
				index = 0;
			}
		}
		
		if(getted) {
			points = 0;
			
			framesg++;
			if(frames == maxFramesg) {
				framesg = 0;
				Game.entities.remove(this);
			}
			
		}
		
		
	}
	
	public void getted() {
		getted = true;
		//Game.entities.remove(this);
	}

	public void render(Graphics g) {
		if(!getted) {
			g.drawImage(sprites[index], getXPosition(), getYPosition(),null);
		}else {
			g.drawImage(getPoint, getXPosition(), getYPosition(),null);
		}
		
		if(Game.debug) {
			g.setColor(Color.gray);
			g.fillRect(this.getXPosition() + maskx, this.getYPosition() + masky,
					 mwidth, mheight);
		}
	}
	
	
	
	public int getXPosition() {
		return this.getX() - Camera.x;
	}
	public int getYPosition() {
		return this.getY() - Camera.y;
	}

}
