package example;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;

public class App extends ExitableJFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8984059550307826098L;

	Image image;
	Insets insets;

	public App(String filename) {
		super(filename);
		image = getToolkit().getImage(filename);
	}

	public void paint(Graphics g) {
		super.paint(g);
		if (insets == null) {
			insets = getInsets();
		}
		try {
		for (int i = 0; i < 100; i++) {
			repaint();
			g.drawImage(image, insets.left + i*3, insets.top, this);
			Thread.sleep(200);
		}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		if (args.length > 0) {
			Frame f = new App(args[0]);
			f.setSize(1280, 980);
			f.setVisible(true);
		} else {
			System.err.println("Please specify image to display");
		}
	}

}