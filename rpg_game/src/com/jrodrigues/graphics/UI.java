package com.jrodrigues.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jrodrigues.main.Game;

public class UI {
	
	public static BufferedImage layout = Game.spritesheet.getSprite(0, 32, 20, 3);
	public static BufferedImage heart = Game.spritesheet.getSprite(0, 35, 5, 5);
	public void render(Graphics g) {
		
		//g.setColor(Color.gray);
		//g.fillRect(8, 21, 100, 12);
		
		g.setColor(Color.green);
		g.fillRect(13, 21, (int)((Game.player.getLife() /Game.player.getMaxLife())*100)-10, 10);
		g.drawImage(layout, 8, 21, 100,10, null);
		g.drawImage(heart, 7, 21, 5,5, null);
	}
}
