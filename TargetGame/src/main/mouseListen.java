package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class mouseListen implements MouseListener {

	private int mX, mY;
	private boolean click;

	@Override
	public void mouseClicked(MouseEvent e) {
		// System.out.println("Clicked");

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// System.out.println("Entered");

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// System.out.println("Exited");

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// System.out.println("Pressed");
		if (!Game.isWait()) {
			mX = e.getX();
			mY = e.getY();
			click = true;
			new AudioPlayer("/Silencer.wav").play();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// System.out.println("Released");
		click = false;

	}

	public boolean isClicked() {
		return click;
	}

	public int getMX() {
		return mX;
	}

	public int getMY() {
		return mY;
	}

	public void update() {
		System.out.println(mX + " | " + mY);
	}
}
