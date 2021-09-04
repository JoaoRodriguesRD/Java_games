package com.jrodrigues.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.jrodrigues.main.Game;
import com.jrodrigues.world.Camera;
import com.jrodrigues.world.World;

public class Particle extends Entity{
	public int lifeTime = 10;
	public int curLife =0;
	
	public int spd = 1;
	public double dx = 0;
	public double dy = 0;
	public Color color;
	
	
	public Particle(int x, int y, int width, int height, Color color, BufferedImage sprite) {
		super(x, y, width, height, sprite); 
		dx = new Random().nextGaussian();
		dy = new Random().nextGaussian();
		this.color = color;
		
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
		g.setColor(color);
		g.fillRect(this.getX()- Camera.x, this.getY() - Camera.y, 1, 1);
		
	}
	
}
