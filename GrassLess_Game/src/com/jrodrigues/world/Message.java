package com.jrodrigues.world;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jrodrigues.main.Game;

public class Message {

	public static boolean show = false;

	public static BufferedImage TILE_MESSAGE = Game.spritesheet.getSprite(0, 16, 32, 16);
	public static final int MAX_MESSAGE = 47;
	public String message;

	private int line;

	public void drawMessage(String message) {
		line = 1;
		String m = message;
		show = false;
		if (message.length() > MAX_MESSAGE && message.length() <MAX_MESSAGE*2) {
			line+=1;
		}else if(message.length() > MAX_MESSAGE*2) {
			line+=2;
		}
		
		this.message = m;
		show = true;

	}

	public void deleteMessage() {
		show = false;

	}

	public void render(Graphics g) {
		if (show) {
			g.drawImage(TILE_MESSAGE, 0, 0, Game.WIDTH, Game.WIDTH / 5, null);
			g.setFont(new Font("TimesRoman", Font.BOLD, 11));
			g.setColor(Color.WHITE);
			// String m = "Teste String1234567891012345678912345678912345634234";
			if(line == 1) {
				g.drawString(message, 20, 20);
			}else if(line == 2){
				g.drawString(message.substring(0, MAX_MESSAGE), 20, 20);
				g.drawString(message.substring(MAX_MESSAGE), 20, 30);
			}else if(line == 3) {
				g.drawString(message.substring(0, MAX_MESSAGE), 20, 20);
				g.drawString(message.substring(MAX_MESSAGE,MAX_MESSAGE*2), 20, 30);
				g.drawString(message.substring(MAX_MESSAGE*2), 20, 40);
			}
			
		}

	}

}
