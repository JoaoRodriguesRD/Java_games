package com.jrodrigues.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MenuSprite {
	
	private BufferedImage spritesheet;
	
	
	public MenuSprite(String path) {
		try {
			spritesheet = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public BufferedImage getSprite(int x, int y, int wigth, int height) {
		return spritesheet.getSubimage(x, y, wigth, height);
	}
	public BufferedImage getSpriteTotal() {
		return spritesheet;
	}
	
}
