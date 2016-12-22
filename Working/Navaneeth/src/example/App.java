package example;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;

public class App extends ExitableJFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8984059550307826098L;

	Insets insets;

	public void paint(Graphics g) {

		super.paint(g);
		if (insets == null) {
			insets = getInsets();
		}

		g.translate(insets.left, insets.top+10);
		System.out.println("Left: "+insets.left+"\nRight: "+ insets.right+"\n Top: "+ insets.top+"\n Bottom: "+insets.bottom);
		g.drawString("Hello World", 1, 1);
		g.drawChars(new char[] { 'H', 'e', 'l', 'l', 'o' }, 0, 5, 1, 15);
		g.dispose();

	}

	public static void main(String[] args) {
		Frame f = new App();
		f.setTitle("App");
		f.setSize(1000, 600);
		f.setVisible(true);
	}

}