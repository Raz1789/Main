package example;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class App extends JComponent implements MouseMotionListener,MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1198592621116410922L;
	Insets insets = new Insets(15, 3, 3, 3);
	private StringBuffer sb = new StringBuffer("");

	public void paint(Graphics g) {
		/*
		 * super.paint(g); if (insets == null) { insets = getInsets(); }
		 */

		g.translate(insets.left, insets.top);
		g.setFont(new Font("Serif", Font.PLAIN, 16));
		g.drawString(sb.toString(), 0, 0);
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		sb.delete(0, sb.toString().length());
		sb.append(" X: " + e.getX() + " Y: " + e.getY());
		System.out.println(sb.toString());
		repaint();
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Mouse Motion Listener");
		App game = new App();
		frame.setPreferredSize(new Dimension(800, 600));
		frame.add(game);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.addMouseMotionListener(game);
		frame.addMouseListener(game);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		sb.append("  Clicked");
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		sb.append("  Entered");
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		sb.append("  Exited");
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		sb.append("  Pressed");
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		sb.append("  Released");
		
	}

}