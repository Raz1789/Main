package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import audio.AudioPlayer;

public class Game extends JPanel implements Runnable {

	private static final long serialVersionUID = -7600755214008009002L;

	// FEILDS

	public static final int WIDTH = 300;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 3;
	public static final int FPS = 30;
	public static final int TIMERSCALE = 700;
	public static final int LEVELHITLIMIT = 2;
	public static final int LEVELMAX  = 15;

	private Vector<Target> vT;
	private Vector<TargetDisappear> vTD;

	private Random rand;

	private int hitCounter = 0, gScore = 0;
	private int level = 1;
	private int gX;
	private int gY;

	private long createTime = 0;
	private long levelInit = 0;
	private int levelInitDelay = 5000;
	private long levelTimer = 0;
	private long levelTimeLimit = 45 * 1000;
	private int timeLeft = (int) (levelTimeLimit / 1000);
	private int timerClosingCounter = 0;
	private String logo = "eNDgamers Production";

	private boolean running = false;
	private boolean bIntro = false;
	private boolean timeOver = false;
	private boolean timeLimitClosing = false;
	private static boolean wait = true;

	private JFrame frame;
	// private JButton button1, button2;
	private BufferedImage image, cImage;
	private Graphics2D g;

	public mouseListen mListen;

	public AudioPlayer apIntro;

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
		// button1 = new JButton("Start");
		// button2 = new JButton("Retry");
		wait = true;
	}

	// METHODS

	// Game Initialization method

	public synchronized void start() {
		bIntro = true;
		running = true;
		if (insets == null) {
			insets = frame.getInsets(); // setting frame insets

			// creating Bufferedimage w.r.t frame created.

			image = new BufferedImage((WIDTH * SCALE) + insets.left * 2 + insets.right * 2,
					(HEIGHT * SCALE) + insets.top * 2 + insets.bottom * 2, BufferedImage.TYPE_INT_RGB);

			// getting bufferedImage's Graphics2D
			g = (Graphics2D) image.getGraphics();

			// setting Anti-aliasing properties - GRAPHICS
			g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
			g.fillRect(0, 0, (WIDTH * SCALE) + insets.left * 2 + insets.right * 2,
					(HEIGHT * SCALE) + insets.top * 2 + insets.bottom * 2);

			// Setting Cursor Look n Feel
			try {
				cImage = ImageIO.read(getClass().getResourceAsStream("/image/crosshair.png"));
				// cImageBlank =
				// ImageIO.read(getClass().getResourceAsStream("/image/Blank.png"));
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			g.drawImage(cImage, mListen.getMMX() - 25, mListen.getMMY() - 25, null);
		}
		mListen.setInsets(insets);
		apIntro = new AudioPlayer("/audio/Intro.wav");
		apIntro.playContinuous();
		thread.start();
	}

	public void intro() {

		g.setColor(new Color(240, 240, 240));
		g.fillRect(insets.left, insets.top, getWidth() - insets.right, (getHeight() / 6));
		g.setStroke(new BasicStroke(3));
		g.setColor(new Color(240, 240, 240).darker());
		g.drawRect(insets.left, insets.top, getWidth() - insets.right, (getHeight() / 6) - insets.bottom);
		g.setStroke(new BasicStroke(1));

		// Game Area
		g.setColor(new Color(87, 115, 229));
		g.fillRect(insets.left, insets.top + (getHeight() / 6), getWidth(), (5 * getHeight() / 6)+insets.bottom);
		g.setStroke(new BasicStroke(3));
		g.setColor(new Color(87, 115, 229).darker());
		g.drawRect(insets.left, insets.top + (getHeight() / 6), getWidth() - insets.right,
				(5 * getHeight() / 6) - insets.bottom);
		g.setStroke(new BasicStroke(1));

		// INSTRUCTIONS
		String[] str1 = new String[3];
		str1[0] = "TA";
		str1[1] = "RG";
		str1[2] = "ET";
		String[] str2 = new String[6];
		str2[0] = "CENTER RED : 10 Points";
		str2[1] = "WHITE      :  5 Points";
		str2[2] = "OUTER RED  :  1 Points";
		str2[3] = "Hit 10 targets in 45 Seconds";
		str2[4] = "To advance to next level";
		str2[5] = "Click to continue....";

		// str1 Draw
		g.setFont(new Font("Century Gothic", Font.BOLD, 12 * SCALE * 2));
		g.setColor(Color.RED);
		int length = (int) g.getFontMetrics().getStringBounds(str1[0], g).getWidth();
		g.drawString(str1[0], (getWidth() - length * 3) / 2, insets.top + 12 * SCALE * 2);
		g.setColor(Color.GRAY);
		g.drawString(str1[1], (getWidth() - length) / 2, insets.top + 12 * SCALE * 2);
		g.setColor(Color.RED);
		g.drawString(str1[2], ((getWidth() + length) / 2) + 5 * SCALE, insets.top + 12 * SCALE * 2);

		g.setFont(new Font("Century Gothic", Font.PLAIN, 12 * SCALE));
		g.setColor(Color.RED);
		g.drawString(str2[0], insets.left + 10 * SCALE, (getHeight() / 4) + 20 * SCALE);
		g.setColor(Color.WHITE);
		g.drawString(str2[1], insets.left + 10 * SCALE, (getHeight() / 4) + 20 * SCALE * 2);
		g.setColor(Color.RED);
		g.drawString(str2[2], insets.left + 10 * SCALE, (getHeight() / 4) + 20 * SCALE * 3);
		g.setColor(Color.BLACK);
		length = (int) g.getFontMetrics().getStringBounds(str2[3], g).getWidth();
		g.drawString(str2[3], ((getWidth() - length) / 2), (getHeight() / 4) + 20 * SCALE * 5);
		length = (int) g.getFontMetrics().getStringBounds(str2[4], g).getWidth();
		g.drawString(str2[4], ((getWidth() - length) / 2), (getHeight() / 4) + 20 * SCALE * 6);
		length = (int) g.getFontMetrics().getStringBounds(str2[5], g).getWidth();
		g.drawString(str2[5], (getWidth() - length) / 2, getHeight() - 6 * SCALE * 2);
		g.setFont(new Font("Century Gothic", Font.BOLD, 5 * SCALE));
		length = (int) g.getFontMetrics().getStringBounds(logo, g).getWidth();
		g.drawString(logo, getWidth() - length - insets.right - 2 * SCALE, (getHeight() / 6) + insets.top - 5 * SCALE);

		new Target((getWidth() - (60 * SCALE)), ((getHeight() / 4) + (30 * SCALE)), 1, System.currentTimeMillis())
				.render(g);

		g.drawImage(cImage, mListen.getMMX() - 25, mListen.getMMY() - 25, null);

		if (mListen.isClicked()) {
			bIntro = false;
			levelInit = System.currentTimeMillis();
		}

	}

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

		apIntro.close();

		// When all is done and gone
		stop();

		System.out.println("Game Stopped");

	}

	// updates the game logics

	// @SuppressWarnings("deprecation")
	public void update() {

		// Cursor Invisibility
		if (mListen.isEntered()) {
			frame.setCursor(frame.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
					new Point(0, 0), "null"));
		}

		if (!bIntro) {

			// LEVEL INITIALIZATION UPDATE
			if ((System.currentTimeMillis() - levelInit < levelInitDelay) && wait) {
				int transp = (int) (Math
						.abs(Math.sin(Math.toRadians((System.currentTimeMillis() - levelInit) * 450 / levelInitDelay)))
						* 255);
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
				g.fillRect(insets.left, insets.top + (getHeight() / 6), getWidth(), (5 * getHeight() / 6)+insets.bottom);
				g.setStroke(new BasicStroke(3));
				g.setColor(new Color(87, 115, 229).darker());
				g.drawRect(insets.left, insets.top + (getHeight() / 6), getWidth() - insets.right,
						(5 * getHeight() / 6) - insets.bottom);
				g.setStroke(new BasicStroke(1));

				// Level Text rendering
				g.setColor(new Color(255, 255, 255, transp));
				g.setFont(new Font("Centrury Gothic", Font.BOLD, 12 * SCALE));
				String str = "- L E V E L " + level + " -";
				int length = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
				g.drawString(str, (getWidth() - length) / 2, (getHeight() / 2));
				if (!vTD.isEmpty())
					for (TargetDisappear tD : vTD)
						tD.render(g, gX, gY);
				g.drawImage(cImage, mListen.getMMX() - 25, mListen.getMMY() - 25, null);
			}
			if ((System.currentTimeMillis() - levelInit > levelInitDelay) && wait) {
				wait = false;
			}

			if (!wait) {

				if (levelTimer == 0) {
					levelTimer = System.currentTimeMillis();
				}

				// Update Level
				if (hitCounter >= LEVELHITLIMIT && level < LEVELMAX) {
					level++;
					hitCounter = 0;
					levelInit = System.currentTimeMillis();
					levelTimer = System.currentTimeMillis();
					timerClosingCounter = 0;
					timeLimitClosing = false;
					wait = true;
				}

				// Timer update

				if ((System.currentTimeMillis() - levelTimer > levelTimeLimit)) {
					running = false;
					timeOver = true;
				}

				timeLeft = (int) (levelTimeLimit - (System.currentTimeMillis() - levelTimer)) / 1000;

				if (timeLeft <= 11 && timeLeft > -1) {
					timerClosingCounter++;
					timeLimitClosing = true;
				}

				if ((timerClosingCounter % FPS == 0) && (timeLimitClosing == true) && (timeLeft > 0))
					new AudioPlayer("/audio/Tick.wav").play();
				if ((timerClosingCounter % FPS == 0) && (timeLimitClosing == true) && (timeLeft == 0)) {
					new AudioPlayer("/audio/Buzzer.wav").play();
					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
					timeLimitClosing = false;
				}

				if (level == LEVELMAX)
					running = false;

				if (running) {

					// Update new Target

					if (createTime == 0)
						createTime = System.currentTimeMillis();
					
					System.out.println((3 * (Game.TIMERSCALE - ((Game.TIMERSCALE / (LEVELMAX+1)) * level))));
					
					if ((System.currentTimeMillis() - createTime) > (3 * (Game.TIMERSCALE - ((Game.TIMERSCALE / (LEVELMAX+1)) * level)))) {
						createTime = 0;
						vT.add(new Target(
								rand.nextInt(getWidth() - insets.right - Target.getDia()) + insets.left
										+ Target.getRad(),
								rand.nextInt((5 * getHeight() / 6) - insets.bottom - Target.getDia()) + insets.top
										+ (getHeight() / 6) + Target.getRad(),
								level, System.currentTimeMillis()));

					}

					// Update shots

					if (mListen.isClicked()) {
						gX = mListen.getMX();
						gY = mListen.getMY();
						if (!vT.isEmpty())
							for (Target target : vT) {
								gScore += target.getScore(gX, gY);
								if (target.isHit())
									hitCounter++;
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
		} else {
			intro();
		}

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
		g.setFont(new Font("Century Gothic", Font.PLAIN, 7 * SCALE));
		g.drawString("SCORE: " + gScore, insets.left + 10, insets.top + 12 * SCALE);
		String str = "- L E V E L " + level + " -";
		int length = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
		g.drawString(str, (getWidth() - length) / 2, insets.top + 12 * SCALE);
		if (!timeLimitClosing || timeLeft > 10) {
			g.drawString("TIMER: 0:" + timeLeft, insets.left + 10, insets.top + 12 * SCALE * 2);
		} else {
			int alpha = Math.abs((int) ((Math.sin(180 * timerClosingCounter / FPS) * 255)));
			if (alpha > 255)
				alpha = 255;
			g.setColor(new Color(255, 0, 0, alpha));
			if (timeLeft < 10)
				g.drawString("TIMER: 0:0" + timeLeft, insets.left + 10, insets.top + 12 * SCALE * 2);
			else
				g.drawString("TIMER: 0:" + timeLeft, insets.left + 10, insets.top + 12 * SCALE * 2);
		}
		g.setFont(new Font("Century Gothic", Font.BOLD, 5 * SCALE));
		length = (int) g.getFontMetrics().getStringBounds(logo, g).getWidth();
		g.drawString(logo, getWidth() - length - insets.right - 2 * SCALE, (getHeight() / 6) + insets.top - 5 * SCALE);

		// Game Area
		g.setColor(new Color(87, 115, 229));
		g.fillRect(insets.left, insets.top + (getHeight() / 6), getWidth(), (5 * getHeight() / 6)+insets.bottom);
		g.setStroke(new BasicStroke(3));
		g.setColor(new Color(87, 115, 229).darker());
		g.drawRect(insets.left, insets.top + (getHeight() / 6), getWidth() - insets.right,
				(5 * getHeight() / 6) - insets.bottom);
		g.setStroke(new BasicStroke(1));

		// Target Boundary condition test visual
		/*
		 * g.drawRect(insets.left + Target.getRad(), insets.top + (getHeight() /
		 * 6) + Target.getRad(), getWidth() - insets.right - Target.getDia(), (5
		 * * getHeight() / 6) - insets.bottom - Target.getDia());
		 */

		// Target Rendering
		if (!vT.isEmpty())
			for (Target target : vT)
				target.render(g);

		// Target Disappearing Rendering
		if (!vTD.isEmpty())
			for (TargetDisappear tD : vTD)
				tD.render(g, gX, gY);
		// Cursor Rendering
		g.drawImage(cImage, mListen.getMMX() - 25, mListen.getMMY() - 25, null);

	}

	// Draws on Jpanel

	public void render() {
		Graphics2D gFrame = (Graphics2D) frame.getGraphics();
		gFrame.drawImage(image, 0, 0, null);
		gFrame.dispose();
	}

	// GAME WAIT VARIABLE GETTER

	public static boolean isWait() {
		return wait;
	}

	// OVERRIDDING THE DEFAULT PAINTCOMPONENT

	public void paintComponent(Graphics2D g) {
	}

	// Game destructor method

	public synchronized void stop() {

		// setting game flags to close mode
		running = false;
		timeLimitClosing = false;

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

		// GAME OVER TEXT DISPLAY
		g.setColor(Color.WHITE);
		g.setFont(new Font("Century Gothic", Font.PLAIN, 12 * SCALE));
		String str1 = "- GAME OVER -";
		String str2 = "FINAL SCORE: " + gScore;
		int length1 = (int) g.getFontMetrics().getStringBounds(str1, g).getWidth();
		int length2 = (int) g.getFontMetrics().getStringBounds(str2, g).getWidth();
		g.drawString(str1, (getWidth() - length1) / 2, getHeight() / 2);
		g.drawString(str2, (getWidth() - length2) / 2, getHeight() / 2 + 12 * SCALE * 2);
		if (timeOver) {
			new AudioPlayer("/audio/Fail.wav").play();
			g.setColor(Color.RED);
			String str3 = "TIMER RAN OUT!!!!";
			int length3 = (int) g.getFontMetrics().getStringBounds(str3, g).getWidth();
			g.drawString(str3, (getWidth() - length3) / 2, (getHeight() / 2) - 12 * SCALE * 2);
		}

		// rendering Final Screen
		render();

		// Thread closure
		try {
			System.out.println("Game starting to close");
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
		//game.frame.setLocationRelativeTo(null);
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.addMouseMotionListener(game.mListen);
		game.frame.addMouseListener(game.mListen);
		// game.frame.add(game.button1,BorderLayout.CENTER);
		game.start();

	}

}
