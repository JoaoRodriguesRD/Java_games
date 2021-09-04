package com.jrodrigues.graphics;

import java.awt.Color;
import java.awt.Graphics;

import com.jrodrigues.main.Game;

public class Animation {

	private int frames = 0;
	private int maxFrames = 10;
	public boolean ended = false;
	public String lastState;
	public boolean run = false;
	public boolean pressedEnter = false;
	
	private int yScreen = 0;
	
	public Animation() {

	}

	public void tick() {
		if(run) {
			if (ended) {
				run = false;
				ended = false;
				if(pressedEnter) {
					Game.gameState = "NORMAL";
					pressedEnter = false;
				}
				
				
			}
			
			
			if(frames >= maxFrames||pressedEnter) {
				frames = 0;
				ended = true;
				pressedEnter = false;
			}else {
				frames++;
			}
			
			
			upScreen();
			
		}
		
		
		
	}

	public void render(Graphics g) {
		if(run) {
			g.setColor(Color.blue);
			g.fillRect(100, yScreen, 20, 20);
		}
	}

	public void runLevelClear() {
		run = true;
		
	}
	
	public void upScreen() {
		yScreen++;
		if(yScreen<200) {
			yScreen++;
		}
	}

}
