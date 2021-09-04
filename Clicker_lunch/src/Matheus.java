import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Matheus {

	public double x, y, dx, dy;
	public double speed = 0.44;

	private BufferedImage sprite;
	
	public boolean getted = false;
	
	public Matheus(int x, int y) {
		this.x = x;
		this.y = y;
		sprite = Game.spriteMatheus;

		double radius = Math.atan2(Game.centerY() - y, Game.centerX() - x);
		this.dx = Math.cos(radius);
		this.dy = Math.sin(radius);
		this.speed+=Game.score*0.27;
	}

	public void update() {
		x += dx * speed;
		y += dy * speed;
		checkMouse();
		Rectangle mask = new Rectangle((int) x, (int) y, 40, 40);
		if (mask.intersects(Game.maskHole)) {
			Game.getted = true;
			Game.matheuses.remove(this);
			Game.score = 0;
			return;
		}

		

	}

	private void checkMouse() {
		if (Game.isPressed) {
			Game.isPressed = false;
			if (Game.mx >= x && Game.mx <= x + 50) {
				if (Game.my >= y && Game.my <= y + 80) {
					Sound.hurtSound.play();
					Game.matheuses.remove(this);
					Game.score++;
					return;
					
				}

			}
		}
	}

	public void render(Graphics g) {
		g.drawImage(sprite, (int) x, (int) y, sprite.getWidth() / 10, sprite.getHeight() / 10, null);
		if(getted) {
			g.drawRect((int) x, (int) y, sprite.getWidth() / 10,sprite.getHeight() / 10);
		}
		
		//g.drawRect((int) x+10, (int) y+50, 40, 30);
	}

}
