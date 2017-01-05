package main;

import java.awt.Color;
import java.awt.Graphics2D;

import audio.AudioPlayer;

public class Target {

	protected int level = 1, scale = 1;
	protected int x, y;
	protected static int dia1, dia2, dia3;
	protected long startTime;
	protected boolean expire = false, hit = false;

	public Target(int x, int y, int level, long startTime) {
		this.x = x;
		this.y = y;
		this.startTime = startTime;
		this.level = level;
		scale = level / 4;

		dia1 = (15 - (scale * 3)) * Game.SCALE * 3;
		dia2 = (10 - (scale * 2)) * Game.SCALE * 3;
		dia3 = (5 - scale) * Game.SCALE * 3;
	}

	public Target(Target t) {
		this.level = t.level;
		this.x = t.x;
		this.y = t.y;
	}

	public void render(Graphics2D g) {

		g.setColor(Color.RED);
		g.fillOval(x - (dia1 / 2), y - (dia1 / 2), dia1, dia1);
		g.setColor(Color.WHITE);
		g.fillOval(x - (dia2 / 2), y - (dia2 / 2), dia2, dia2);
		g.setColor(Color.RED);
		g.fillOval(x - (dia3 / 2), y - (dia3 / 2), dia3, dia3);
	}

	public boolean isExpire(long endTime) {

		if (3 * Game.TIMERSCALE > (100 - level)) {
			if ((endTime - startTime) > (3 * (Game.TIMERSCALE - ((Game.TIMERSCALE / (Game.LEVELMAX+1)) * level))))
				expire = true;
			return expire;
		} else {
			System.err.println("System timer error");
			return true;
		}

	}

	public static int getDia() {
		// System.out.println(dia1);
		return dia1;
	}

	public static int getRad() {
		// System.out.println(dia1/2);
		return dia1 / 2;
	}

	public boolean isHit() {
		return hit;
	}

	public int getScore(double x, double y) {
		double exp = Math.sqrt(Math.pow((this.x - x), 2) + Math.pow((this.y - y), 2));
		if (exp < dia3 / 2) {
			expire = true;
			hit = true;
			new AudioPlayer("/audio/Silencer.wav").play();
			return 10;
		} else if (exp < dia2 / 2) {
			hit = true;
			expire = true;
			new AudioPlayer("/audio/Silencer.wav").play();
			return 5;
		} else if (exp < dia1 / 2) {
			hit = true;
			expire = true;
			new AudioPlayer("/audio/Silencer.wav").play();
			return 1;
		} else
			return 0;
	}

}
