package example;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class App implements Runnable {

	private static int i;
	private static JFrame frame1 = new JFrame("Title");
	private static JFrame frame2 = new JFrame("title");

	public void run() {
		
		frame1.setSize(500, 400);
		frame1.setLocation(100,100);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setVisible(true);
		
		frame2.setSize(1000, 400);
		frame2.setLocation(10,10);
		//frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.setVisible(true);
		
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new App());
		for (i = 0; i < 100; i++)
			try {
				if (i == 99)
					i = 0;
				System.out.println(i);
				frame1.setTitle("Title " + i);
				frame2.setTitle("title " + i);
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
	}

}
