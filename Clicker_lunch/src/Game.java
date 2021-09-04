import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable,MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int WIDTH = 480;
	public static int HEIGHT = 480;
	
	
	public static BufferedImage spriteMatheus;
	public static BufferedImage spriteBack;
	public static List<Matheus> matheuses;
	
	public Spawner spawner;
	
	public static Rectangle maskHole;
	
	public static int mx,my;
	public static boolean isPressed = false;
	
	public static int score = 0;
	public static int maxScore = 0;
	
	public static boolean haveMatheus = false;
	
	public static boolean getted = false;
	
	//public static String mode = "Santos";
	
	public static String mode = "Matheus";
	
	public static void main(String[] args) {
		Game game = new Game();
		JFrame frame = new JFrame();
		if(mode == "Santos") {
			frame.setTitle("doce");
		}else if(mode == "Matheus") {
			frame.setTitle("coxinha de frango");
		}
		frame.add(game);
		frame.setResizable(false);
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		new Thread(game).start();
	}

	public Game() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.addMouseListener(this);
		//Sprites sp = new Sprites("/matheus.png");
		Sprites sp = null;
		if(mode == "Santos") {
			sp = new Sprites("/Santos.png");
		}else if(mode == "Matheus") {
			sp = new Sprites("/matheus.png");
		}
		
		spriteMatheus = sp.getSpriteTotal();
		matheuses = new ArrayList<Matheus>();
		Matheus a = new Matheus(0,0);
		matheuses.add(a);
		
		sp = new Sprites("/back.png");
		if(mode == "Santos") {
			sp = new Sprites("/back2.png");
		}else if(mode == "Matheus") {
			sp = new Sprites("/back.png");
		}
		spriteBack = sp.getSpriteTotal();
		
		spawner = new Spawner();
		
		maskHole = new Rectangle(Game.centerX(),Game.centerY(),40,40);
		Sound.musicBackground.play();
	}

	public void update() {
		for(int i = 0;i<matheuses.size();i++) {
			Matheus m = matheuses.get(i);
			m.update();
		}
		
		if(matheuses.size()==0) {
			haveMatheus = false;
		}
		spawner.update();
		
		checkScore();
		if(getted) {
			Sound.musicBackground.play();
			getted = false;
		}
	}

	private void checkScore() {
		if(score >=maxScore) {
			maxScore = score; 
		}
		
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.gray);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.black);
		
		
		
		
		
		
		g.drawImage(spriteBack, 0, 0, WIDTH, HEIGHT,null);
		if(getted) {
			g.fillOval(WIDTH/2 - 35, HEIGHT/2 - 35, 70,70);
			getted = false;
		}
		
		for(int i = 0;i<matheuses.size();i++) {
			matheuses.get(i).render(g);
		}
		g.setFont(new Font("arial", Font.BOLD, 20));
		
		if(mode == "Santos") {
			g.drawString("NAO DEIXE O SANTOS CHEGAR NO DOCE!", 15, 35);
		}else if(mode == "Matheus") {
			g.drawString("NAO DEIXE O MATHEUS COMER A COXINHA!", 15, 35);
		}
		
		
		g.setFont(new Font("arial", Font.BOLD, 15));
		g.drawString("Score:"+score, 30, 55);
		g.drawString("best score:"+maxScore, 30, 68);
		g.drawString("Created by: jRodrigues", 0,Game.HEIGHT-30);
		g.dispose();
		bs.show();
	}

	@Override
	public void run() {
		while(true) {
			update();
			render();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {}
			
		}

	}
	
	public static int centerX() {
		return WIDTH/2 - 35;
	}
    public static int centerY() {
    	return HEIGHT/2 - 35;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		isPressed = true;
		mx = arg0.getX();
		my = arg0.getY();
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
