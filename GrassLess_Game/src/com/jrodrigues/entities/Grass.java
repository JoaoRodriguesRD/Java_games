package com.jrodrigues.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jrodrigues.main.Game;
import com.jrodrigues.world.Camera;
import com.jrodrigues.world.World;

public class Grass extends Entity {

	public Grass(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
		depth = 1;
	}
	
	public void tick() {
		if(Game.player.x/16 == x/16 && Game.player.y/16 == y/16) {
			Game.player.grassCutted++;
			Game.grass.remove(this);
			
			World.generateParticles(10, (int)x+8, (int)y+8, new Color(0,255,0));
		}
	}
	public void render() {
		
	}

}
