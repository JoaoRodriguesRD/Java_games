package com.jrodrigues.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jrodrigues.main.Game;
import com.jrodrigues.world.Camera;
import com.jrodrigues.world.World;

public class BulletShoot extends Entity{
	
	private double dx;
	private double dy;
	private double speed = 3;
	
	private int time = 60, curTime = 0;
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite,double dx,double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	
	public void tick() {
		if(World.isFreeDynamic((int)(x+dx*speed),(int)(y + dy*speed),3,3)) {
		
		x += dx*speed;
		y += dy*speed;
		}else {
			Game.bullets.remove(this);
			World.generateParticles(10,(int)x,(int)y);
			return;
		}
		
		curTime++;
		if(curTime == time) {
			Game.bullets.remove(this);
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX()- Camera.x, this.getY() - Camera.y, 3, 3);
	}

}
