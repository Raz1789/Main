package main;

import java.awt.Color;
import java.awt.Graphics2D;

import audio.AudioPlayer;

public class Target {
	
	protected int scale = 3;
	protected int x,y;
	protected static int dia1,dia2,dia3;
	protected long startTime;
	protected boolean expire = false,hit = false;
	
	public Target(int x, int y, int level, long startTime) {
		this.x = x;
		this.y = y;
		scale = 4-level;
		this.startTime = startTime;
		dia1 = 15*Game.SCALE*scale;
		dia2 = 10*Game.SCALE*scale;
		dia3 = 5*Game.SCALE*scale;
	}
	
	public Target(Target t) {
		this.scale = t.scale;
		this.x = t.x;
		this.y = t.y;
	}
	
	public void render(Graphics2D g) {
		
		g.setColor(Color.RED);
		g.fillOval(x-(dia1/2), y-(dia1/2), dia1,dia1);
		g.setColor(Color.WHITE);
		g.fillOval(x-(dia2/2), y-(dia2/2), dia2,dia2);
		g.setColor(Color.RED);
		g.fillOval(x-(dia3/2), y-(dia3/2), dia3,dia3);
	}
	public boolean isExpire(long endTime) {
		if((endTime - startTime) > Game.TIMERSCALE*(scale))
			expire = true;
		return expire;
	}
	
	public static int getDia() {
		//System.out.println(dia1);
		return dia1;
	}
	public static int getRad() {
		//System.out.println(dia1/2);
		return dia1/2;
	}
	public boolean isHit() { return hit;}
	
	public int getScore(double x, double y) {
		double exp = Math.sqrt(Math.pow((this.x - x),2) + Math.pow((this.y - y),2));
		if(exp < dia3/2){
			expire = true;
			hit = true;
			new AudioPlayer("/audio/Silencer.wav").play();
			return 10;
		}
		else if(exp < dia2/2) {
			hit = true;
			expire = true;
			new AudioPlayer("/audio/Silencer.wav").play();
			return 5;
		}
		else if (exp < dia1/2) {
			hit = true;
			expire = true;
			new AudioPlayer("/audio/Silencer.wav").play();
			return 1;
		}
		else
			return 0;
	}

}
