package main;

import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class mouseListen implements MouseListener,MouseMotionListener {

	private int mX, mY;
	private int mMX, mMY;
	private boolean click;
	private boolean entered = false;
	private Insets insets = new Insets(0,0,0,0);

	@Override
	public void mouseClicked(MouseEvent e) {
		// System.out.println("Clicked");

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// System.out.println("Entered");
		entered = true;

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// System.out.println("Exited");
		entered = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// System.out.println("Pressed");
//		if (!Game.isWait()) {
			mX = e.getX();
			mY = e.getY();
			click = true;
//		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// System.out.println("Released");
		click = false;

	}
	@Override
	public void mouseDragged(MouseEvent e) {
		mMX = e.getX();
		if(e.getY() > ((Game.HEIGHT*Game.SCALE)/6 + insets.top))
			mMY = e.getY();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mMX = e.getX();
		if(e.getY() > ((Game.HEIGHT*Game.SCALE)/6 + insets.top))
			mMY = e.getY();
		
	}
	
	public boolean isEntered() {
		return entered;
	}

	public boolean isClicked() {
	//	System.out.println(click);
		return click;
	}

	public int getMMX() {
		return mMX;
	}

	public int getMMY() {
		return mMY;
	}
	
	public int getMX() {
		return mX;
	}

	public int getMY() {
		return mY;
	}

	public void setInsets(Insets insets) {
		this.insets = insets;		
	}
}
