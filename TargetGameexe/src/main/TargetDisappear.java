package main;

import java.awt.Color;
import java.awt.Graphics2D;

public class TargetDisappear extends Target{
	
	private int disTime = 0;
	private double start = 0;
	private int tdX =0, tdY = 0;
	private int tdDia1,tdDia2,tdDia3;
	//private boolean killingShot = false;
	

	public TargetDisappear(Target t,int level) {
		super(t);
		disTime = Game.FPS*(scale)*20;
	}
	
	public void render(Graphics2D g,int gX,int gY) {
		if(start<disTime)
		{
			if(start == 0) {
				tdX = gX;
				tdY = gY;
				tdDia1 = dia1;
				tdDia2 = dia2;
				tdDia3 = dia3;
			}
			start++;
			int transp = (int)(Math.abs(Math.sin(Math.toRadians(start*360/disTime)))*255);
			if(transp > 255){
				transp = 255;
			}
		g.setColor(new Color(255,0,0,transp));
		g.fillOval(x-(tdDia1/2), y-(tdDia1/2), tdDia1,tdDia1);
		g.setColor(new Color(255,255,255,transp));
		g.fillOval(x-(tdDia2/2), y-(tdDia2/2), tdDia2,tdDia2);
		g.setColor(new Color(255,0,0,transp));
		g.fillOval(x-(tdDia3/2), y-(tdDia3/2), tdDia3,tdDia3);
		g.setColor(Color.BLACK);
		g.fillOval(tdX, tdY, 4, 4);
		}
	}
	

}
