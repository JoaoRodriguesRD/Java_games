package com.jrodrigues.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.jrodrigues.main.Game;
import com.jrodrigues.world.Camera;
import com.jrodrigues.world.World;

public class Particle extends Entity{
	public int lifeTime = 15;
	public int curLife =0;
	
	public int spd = 2;
	public double dx = 0;
	public double dy = 0;
	
	public Particle(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		dx = new Random().nextGaussian();
		dy = new Random().nextGaussian();
		
	}
	
	public void tick() {
		if(World.isFreeDynamic((int)(x+dx*spd),(int)(y + dy*spd),1,1)) {
		x+=dx*spd;
		y+=dy*spd;
		}else {
			Game.entities.remove(this);
			return;
		}
		curLife++;
		if(lifeTime == curLife) {
			Game.entities.remove(this);
		}

		
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(78,78,78));
		g.fillRect(this.getX()- Camera.x, this.getY() - Camera.y, 1, 1);
		
	}
	
}
