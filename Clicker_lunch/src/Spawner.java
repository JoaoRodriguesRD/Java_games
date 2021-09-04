import java.util.Random;

public class Spawner {
	
	public int curTime = 0, targetTime = 300;
	public Random random;
	
	
	public Spawner() {
		random = new Random();
	}
	
	public void update() {
		curTime++;
		if(curTime == targetTime && !Game.haveMatheus) {
			curTime = 0;
			if(random.nextInt(100) < 25) {
				Game.matheuses.add(new Matheus(random.nextInt(Game.WIDTH - 40),-40));
			}else if(random.nextInt(100) < 50){
				Game.matheuses.add(new Matheus(random.nextInt(Game.WIDTH - 40),Game.HEIGHT - 40));
			}else if(random.nextInt(100) < 50) {
				Game.matheuses.add(new Matheus(0,random.nextInt(Game.HEIGHT - 40)));
			}else{
				Game.matheuses.add(new Matheus((Game.WIDTH - 40),random.nextInt(Game.HEIGHT - 40)));
			}
			
		}
	}
	
}
