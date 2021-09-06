package com.jrodrigues.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.jrodrigues.graphics.MenuSprite;
import com.jrodrigues.world.World;

public class Menu {

	public String[] options = { "Novo jogo", "Carregar", "Configuracoes", "Sair" };

	public boolean down = false;
	public boolean up = false;
	public boolean enter = false;

	public static boolean pause = false;

	public static boolean saveExits = false;
	public static boolean saveGame = false;

	public boolean conf = false;
	public boolean carr = false;

	public int curOption = 0;
	public int maxOptions = options.length - 1;

	public int framesStop = 0, maxFramesStop = 10;
	public int indexStop = 0, maxIndexStop = 7;

	private static BufferedImage[] spritesStop = new BufferedImage[8];

	private MenuSprite m = new MenuSprite("/menu.png");
	private BufferedImage menu = m.getSpriteTotal();

	private boolean loaded = false;

	public void tick() {
		File file = new File("save.txt");

		saveExits = file.exists();

		if (!loaded) {
			load();
		}
		if (up || down || enter) {
			//Sound.hurtSound.play();
		}

		framesStop++;
		if (framesStop == maxFramesStop) {
			framesStop = 0;
			indexStop++;
			if (indexStop > maxIndexStop) {
				indexStop = 0;
			}
		}

		if (up) {
			up = false;
			Sound.hurtSound.play();
			curOption--;
			if (curOption < 0) {
				curOption = maxOptions;
			}
		}
		if (down) {
			down = false;
			Sound.hurtSound.play();
			curOption++;
			if (curOption > maxOptions) {
				curOption = 0;
			}
		}
		if (enter) {
			enter = false;
			
			if (options[curOption] == "Novo jogo" || options[curOption] == "Continuar") {
				Game.gameState = "NORMAL";
				pause = false;
				file = new File("save.txt");
				file.delete();
			} else if (options[curOption] == "Carregar") {
				carr = true;
				file = new File("save.txt");
				if (file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
				Game.gameState = "NORMAL";
			} else if (options[curOption] == "Configuracoes") {
				conf = true;
			} else if (options[curOption] == "Sair") {
				System.exit(1);
			}
		}
	}

	public static void applySave(String str) {
		String[] spl = str.split("/");
		for (int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch (spl2[0]) {
			case "level":
				World.restartGame("level" + spl2[1] + ".png");
			}
		}
	}

	public void load() {
		loaded = true;
		spritesStop = new BufferedImage[8];

		spritesStop[0] = Game.spritesheet.getSprite(0, 112, 16, 16);
		spritesStop[1] = Game.spritesheet.getSprite(16, 112, 16, 16);
		spritesStop[2] = Game.spritesheet.getSprite(32, 112, 16, 16);
		spritesStop[3] = Game.spritesheet.getSprite(48, 112, 16, 16);

		spritesStop[4] = Game.spritesheet.getSprite(0, 128, 16, 16);
		spritesStop[5] = Game.spritesheet.getSprite(16, 128, 16, 16);
		spritesStop[6] = Game.spritesheet.getSprite(32, 128, 16, 16);
		spritesStop[7] = Game.spritesheet.getSprite(48, 128, 16, 16);
	}

	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if (file.exists()) {

			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));

				try {
					while ((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for (int i = 0; i < val.length; i++) {
							val[i] -= encode;
							trans[1] += val[i];

						}
						line += trans[0];
						line += ":";
						line += trans[1];
						line += "/";
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}
		return line;
	}

	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;

		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < val2.length; i++) {
			String current = val1[i];
			current += ":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for (int n = 0; n < value.length; n++) {
				value[n] += encode;
				current += value[n];
			}

			try {
				write.write(current);
				if (i < val1.length - 1)
					write.newLine();
			} catch (IOException e) {
			}
		}
		try {
			write.flush();
			write.close();
		} catch (IOException e) {
		}

	}

	public void render(Graphics g) {
		g.setColor(Color.black);
		//g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		//Toolkit.getDefaultToolkit().getScreenSize()
		//g.fillRect(0, 0,Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		if(Game.isFullscreen) {
			g.fillRect(0, 0,Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		}else {
			g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		}
		
		// g.drawImage(menu, 0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE,
		// null);

		g.setColor(Color.white);
		//g.setFont(new Font("arial", Font.BOLD, 48));
		g.setFont(Game.myFont.deriveFont(48f));
		g.drawString("NAO TA PRONTO", Game.WIDTH * Game.SCALE / 64, Game.HEIGHT * Game.SCALE / 16);

		//g.setFont(new Font("arial", Font.BOLD, 32));
		
		g.setFont(Game.myFont.deriveFont(20f));
		String stg = "";
		if (pause) {
			stg = "Retornar";
		} else {
			stg = "Novo jogo";
		}
		g.drawString(stg, Game.WIDTH * Game.SCALE / 32, Game.HEIGHT * Game.SCALE / 8);
		g.drawString("Carregar", Game.WIDTH * Game.SCALE / 32, Game.HEIGHT * Game.SCALE / 8 + 30);
		g.drawString("Configuracoes", Game.WIDTH * Game.SCALE / 32, Game.HEIGHT * Game.SCALE / 8 + 60);
		g.drawString("Sair", Game.WIDTH * Game.SCALE / 32, Game.HEIGHT * Game.SCALE / 8 + 90);
		if (Game.debug) {
			g.drawString("VERSAO DEBUG", Game.WIDTH * Game.SCALE / 32, Game.HEIGHT * Game.SCALE / 8 + 180);
		}

		if (options[curOption] == "Novo jogo") {
			// g.drawString(">", Game.WIDTH * Game.SCALE / 128, Game.HEIGHT * Game.SCALE /
			// 8);
			g.drawImage(spritesStop[indexStop], Game.WIDTH * Game.SCALE / 64 - 23, Game.HEIGHT * Game.SCALE / 8 - 30,
					48, 48, null);
		} else if (options[curOption] == "Carregar") {
			// g.drawString(">", Game.WIDTH * Game.SCALE / 128, Game.HEIGHT * Game.SCALE /
			// 8+30);
			g.drawImage(spritesStop[indexStop], Game.WIDTH * Game.SCALE / 64 - 23, Game.HEIGHT * Game.SCALE / 8, 48, 48,
					null);
		} else if (options[curOption] == "Configuracoes") {
			// g.drawString(">", Game.WIDTH * Game.SCALE / 128, Game.HEIGHT * Game.SCALE /
			// 8+60);
			g.drawImage(spritesStop[indexStop], Game.WIDTH * Game.SCALE / 64 - 23, Game.HEIGHT * Game.SCALE / 8 + 30,
					48, 48, null);
		} else if (options[curOption] == "Sair") {
			// g.drawString(">", Game.WIDTH * Game.SCALE / 128, Game.HEIGHT * Game.SCALE /
			// 8+90);
			g.drawImage(spritesStop[indexStop], Game.WIDTH * Game.SCALE / 64 - 23, Game.HEIGHT * Game.SCALE / 8 + 60,
					48, 48, null);
		}

		if (conf) {
			g.setFont(new Font("arial", Font.BOLD, 16));
			g.drawString("Por que voce apertou aqui? Ta tudo configurado :)", Game.WIDTH * Game.SCALE / 4 + 30,
					Game.HEIGHT * Game.SCALE / 8 + 60);
		}
		if (carr) {
			g.setFont(new Font("arial", Font.BOLD, 16));
			g.drawString("essa opcao vai chegar na versao 0.0.1.0",
					Game.WIDTH * Game.SCALE / 4 + 30, Game.HEIGHT * Game.SCALE / 8 + 30);
		}
		g.setFont(new Font("arial", Font.BOLD, 32));
		g.drawString("Versao: 0.0.0.1", 4, Game.HEIGHT * Game.SCALE);
		g.drawString("created by: jRodrigues", 4, Game.HEIGHT * Game.SCALE-40);
	}
	
}
