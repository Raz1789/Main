package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel implements Runnable {

	private static final long serialVersionUID = -7600755214008009002L;

	// FEILDS

	public static final int WIDTH = 300;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 4;
	public static final int FPS = 30;
	public static final int TIMERSCALE = 1000;

	private Vector<Target> vT;
	private Vector<TargetDisappear> vTD;

	private Random rand;

	private int score = 0, gScore = 0;
	private int level = 1, levelScoreLimit = 100;
	private int gX;
	private int gY;

	private long createTime = 0;
	private long levelInit = 0;
	private int levelInitDelay = 5000;

	private boolean running = false;
	private static boolean wait = true;

	private JFrame frame;
	private BufferedImage image;
	private Graphics2D g;

	public mouseListen mListen;

	private Thread thread;
	private Insets insets;

	// CONSTRUCTOR

	public Game() {
		frame = new JFrame("Game");
		thread = new Thread(this, "Game Runner");
		rand = new Random();
		mListen = new mouseListen();
		vT = new Vector<Target>(5, 5);
		vTD = new Vector<TargetDisappear>(5, 5);
		levelInit = System.currentTimeMillis();
		wait = true;
	}

	// METHODS

	public void run() {

		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / FPS;
		double delta = 0;
		int fps = 0;
		int ups = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				update(); // updates the game logics
				render(); // Draws on Jpanel
				delta--;

				ups++; // updates counter

			}
			gameDraw(); // pulls graphics to an background bufferImage

			fps++; // FRAMES counter

			// display the updates and frames on the title bar
			if ((System.currentTimeMillis() - timer) > 1000) {
				timer += 1000;
				frame.setTitle("Game | UPS: " + ups + " FPS: " + fps);
				ups = 0;
				fps = 0;
			}
		}

		// When all is done and gone
		stop();

		System.out.println("Game Stopped");

	}

	// updates the game logics

	public void update() {

		// Level Initialization delay
		if ((System.currentTimeMillis() - levelInit < levelInitDelay) && wait) {
			int transp = (int) (Math.abs(
					Math.sin(Math.toRadians((System.currentTimeMillis() - levelInit) * 540 / levelInitDelay))) * 255);
			if (transp > 255) {
				transp = 255;
			}
			// HUD
			g.setColor(new Color(240, 240, 240));
			g.fillRect(insets.left, insets.top, getWidth() - insets.right, (getHeight() / 6));
			g.setStroke(new BasicStroke(3));
			g.setColor(new Color(240, 240, 240).darker());
			g.drawRect(insets.left, insets.top, getWidth() - insets.right, (getHeight() / 6) - insets.bottom);
			g.setStroke(new BasicStroke(1));

			// Game Area
			g.setColor(new Color(87, 115, 229));
			g.fillRect(insets.left, insets.top + (getHeight() / 6), getWidth(), (5 * getHeight() / 6));
			g.setStroke(new BasicStroke(3));
			g.setColor(new Color(87, 115, 229).darker());
			g.drawRect(insets.left, insets.top + (getHeight() / 6), getWidth() - insets.right,
					(5 * getHeight() / 6) - insets.bottom);
			g.setStroke(new BasicStroke(1));
			
			// Level Text rendering
			g.setColor(new Color(255, 255, 255, transp));
			g.setFont(new Font("Centrury Gothic", Font.BOLD, 50));
			String str = "- L E V E L " + level + " -";
			int length = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
			g.drawString(str, (getWidth() - length) / 2, (getHeight() / 2));
			if (!vTD.isEmpty())
				for (TargetDisappear tD : vTD)
					tD.render(g, gX, gY);
		}
		if ((System.currentTimeMillis() - levelInit > levelInitDelay) && wait) {
			wait = false;
		}

		if (!wait) {

			// Update Level
			if (score >=levelScoreLimit && level < 4) {
				level++;
				score = 0;
				levelInit = System.currentTimeMillis();
				wait = true;
			}

			if (level == 4)
				running = false;

			if (running) {
				// Update new Target

				if (createTime == 0)
					createTime = System.currentTimeMillis();
				if ((System.currentTimeMillis() - createTime) > TIMERSCALE * (4 - level)) {
					createTime = 0;
					vT.add(new Target(rand.nextInt(getWidth() - insets.right - Target.getDia()) + insets.left + Target.getRad(),
							rand.nextInt((5 * getHeight() / 6) - insets.bottom - Target.getDia()) + insets.top + (getHeight() / 6) + Target.getRad(),
							level, System.currentTimeMillis()));

				}

				// Update shots

				if (mListen.isClicked()) {
					gX = mListen.getMX();
					gY = mListen.getMY();
					if (!vT.isEmpty())
						for (Target target : vT) {
							score += target.getScore(gX, gY);
							gScore += target.getScore(gX, gY);
						}
				}

				// Update remove target

				if (!vT.isEmpty())
					for (int i = 0; i < vT.size(); i++) {
						boolean expire = false;
						expire = vT.get(i).isExpire(System.currentTimeMillis());
						if (expire) {
							if (vT.get(i).isHit()) {
								vTD.add(new TargetDisappear(vT.get(i), level));
							}
							vT.remove(i);
							i--;
							break;
						}

					}
			}
		}

	}

	// GAME WAIT VARIABLE GETTER

	public static boolean isWait() {
		return wait;
	}

	// OVERRIDDING THE DEFAULT PAINTCOMPONENT

	public void paintComponent(Graphics2D g) {
	}

	// pulls graphics to an background bufferImage

	public void gameDraw() {

		// HUD
		g.setColor(new Color(240, 240, 240));
		g.fillRect(insets.left, insets.top, getWidth() - insets.right, (getHeight() / 6));
		g.setStroke(new BasicStroke(3));
		g.setColor(new Color(240, 240, 240).darker());
		g.drawRect(insets.left, insets.top, getWidth() - insets.right, (getHeight() / 6) - insets.bottom);
		g.setStroke(new BasicStroke(1));

		// HUD Displays
		g.setColor(Color.BLACK);
		g.setFont(new Font("Century Gothic", Font.PLAIN, 25));
		g.drawString("SCORE: " + gScore, insets.left + 10, insets.top + 50);
		String str = "- L E V E L " + level + " -";
		int length = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
		g.drawString(str, (getWidth() - length) / 2, insets.top + 25);

		// Game Area
		g.setColor(new Color(87, 115, 229));
		g.fillRect(insets.left, insets.top + (getHeight() / 6), getWidth(), (5 * getHeight() / 6));
		g.setStroke(new BasicStroke(3));
		g.setColor(new Color(87, 115, 229).darker());
		g.drawRect(insets.left, insets.top + (getHeight() / 6), getWidth() - insets.right,
				(5 * getHeight() / 6) - insets.bottom);
		g.setStroke(new BasicStroke(1));

		// Target Boundary condition test visual
	/*	g.drawRect(insets.left + Target.getRad(), insets.top + (getHeight() / 6) + Target.getRad(),
				getWidth() - insets.right - Target.getDia(), (5 * getHeight() / 6) - insets.bottom - Target.getDia());*/

		// Target Rendering
		if (!vT.isEmpty())
			for (Target target : vT)
				target.render(g);

		// Target Disappearing Rendering
		if (!vTD.isEmpty())
			for (TargetDisappear tD : vTD)
				tD.render(g, gX, gY);

	}

	// Draws on Jpanel

	public void render() {
		Graphics2D gFrame = (Graphics2D) frame.getGraphics();
		gFrame.drawImage(image, 0, 0, null);
		gFrame.dispose();
	}

	// Game Initialization method

	public synchronized void start() {
		running = true;
		if (insets == null) {
			insets = frame.getInsets(); // setting frame insets

			// creating Bufferedimage w.r.t frame created.

			image = new BufferedImage((WIDTH * SCALE) + insets.left * 2 + insets.right * 2,
					(HEIGHT * SCALE) + insets.top * 2 + insets.bottom * 2, BufferedImage.TYPE_INT_RGB);

			// getting bufferedImage's Graphics2D
			g = (Graphics2D) image.getGraphics();

			// setting Anti-aliasing properties
			// TEXT
			g.setRenderingHints(
					new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
			// GRAPHICS
			g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		}
		thread.start();
	}

	// Game destructor method

	public synchronized void stop() {
		running = false;

		// Stopping Screen Draw
		// HUD
		g.setColor(new Color(240, 240, 240));
		g.fillRect(insets.left, insets.top, getWidth() - insets.right, (getHeight() / 6));
		g.setStroke(new BasicStroke(3));
		g.setColor(new Color(240, 240, 240).darker());
		g.drawRect(insets.left, insets.top, getWidth() - insets.right, (getHeight() / 6) - insets.bottom);
		g.setStroke(new BasicStroke(1));

		// Game Area
		g.setColor(new Color(87, 115, 229));
		g.fillRect(insets.left, insets.top + (getHeight() / 6), getWidth(), (5 * getHeight() / 6));
		g.setStroke(new BasicStroke(3));
		g.setColor(new Color(87, 115, 229).darker());
		g.drawRect(insets.left, insets.top + (getHeight() / 6), getWidth() - insets.right,
				(5 * getHeight() / 6) - insets.bottom);
		g.setStroke(new BasicStroke(1));
		
		//GAME OVER TEXT DISPLAY
		g.setColor(Color.WHITE);
		g.setFont(new Font("Century Gothic", Font.PLAIN, 50));
		String str1 = "- GAME OVER -";
		String str2 = "FINAL SCORE: " + gScore;
		int length1 = (int) g.getFontMetrics().getStringBounds(str1, g).getWidth();
		int length2 = (int) g.getFontMetrics().getStringBounds(str2, g).getWidth();
		g.drawString(str1, (getWidth() - length1) / 2, getHeight() / 2);
		g.drawString(str2, (getWidth() - length2) / 2, getHeight() / 2 + 100);
		
		//rendering Final Screen
		render();
		
		//Thread closure
		try {
			System.out.println("Game starting to close");
			Thread.sleep(5000);
			thread.join(2000);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	// MAIN FUNCTION

	public static void main(String[] args) {

		Game game = new Game();
		game.frame.add(game);
		game.frame.setVisible(true);
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.frame.createBufferStrategy(3);
		game.frame.pack();
		game.frame.setResizable(false);
		game.frame.setFocusable(true);
		game.frame.requestFocus();
		// game.frame.setLocationRelativeTo(null);
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.addMouseListener(game.mListen);
		game.start();

	}

}
