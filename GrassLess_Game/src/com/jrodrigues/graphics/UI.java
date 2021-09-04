package com.jrodrigues.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.jrodrigues.entities.Player;
import com.jrodrigues.main.Game;

public class UI {
	public static Font myFont;
	public static BufferedImage layout;
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("8bit.TTF");
	
	//public static BufferedImage heart = Game.spritesheet.getSprite(0, 35, 5, 5);
	
	
	public UI(){
		try {
			layout = ImageIO.read(getClass().getResource("/layout.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			myFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(30f);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public void render(Graphics g) {
		g.setFont(myFont.deriveFont(16f));
		//g.setColor(Color.gray);
		//g.fillRect(8, 21, 100, 12);
		g.drawImage(layout,230, 0, 720, 192, null);
		g.setColor(Color.green);
		//g.fillRect(13, 21, (int)((Game.player.getLife() /Game.player.getMaxLife())*100)-10, 10);
		//g.fillRect(130, 210, (int)((Game.player.grassCutted/Game.grassCount )*100)-10, 10);
		//g.fillRect(130, 210, 30, 10);
		g.setColor(Color.white);
		
		
		
		g.drawString("jrodrigues", 5, 20);
		
		double number = (double)Game.player.grassCutted/Game.grassCount*100;
		
		
		g.setFont(myFont.deriveFont(48f));
		g.drawString("yard", 780, 130);
		g.drawString("DONE", 300, 130);
		g.setFont(myFont.deriveFont(32f));
		g.drawString(""+Game.getCurLevel(), 800, 180);
		g.drawString((int)number+"%", 320, 180);
		
		g.fillRect(302, 140, (int)number, 10);
	}
}
