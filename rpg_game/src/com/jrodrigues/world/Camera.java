package com.jrodrigues.world;

public class Camera {
	public static int x;
	public static int y;
	
	public static int Clamp(int atual, int min, int max) {
		if(atual < min) {
			atual = min;
		} 
		if(atual > max) {
			atual = max;
		}
		return atual;
	}
	
	
	
}
