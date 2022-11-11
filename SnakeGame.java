import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 10;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
	static final int DELAY = 175;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	final int xb[] = new int[GAME_UNITS];
	final int yb[] = new int[GAME_UNITS];
	final int a[] = { 1, 2, 3, 5, 6, 7, 8, 9, 10, 12, 13, 14 };
	int j, count = 0;
	int entry;
	int bodyParts = 6;
	int score;
	int appleX;
	int appleY;
	int barrierX;
	int barrierY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;

	SnakeGame() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {

		if (running) {
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			for (int i = barrierY; i <= barrierY + (4 * UNIT_SIZE); i = i + UNIT_SIZE) {
				for (j = barrierX; j <= barrierX + (4 * UNIT_SIZE); j = j + UNIT_SIZE) {
					if (i == barrierY || i == (barrierY + (4 * UNIT_SIZE)) || j == barrierX
							|| j == (barrierX + (4 * UNIT_SIZE))) {
						g.setColor(Color.blue);
						g.fillRect(j, i, UNIT_SIZE, UNIT_SIZE);
						xb[count] = j;
						yb[count] = i;
						count++;

					}
				}
				j = barrierX;
			}
			count = 0;
			g.setColor(Color.black);
			g.fillRect(xb[entry], yb[entry], UNIT_SIZE, UNIT_SIZE);
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(new Color(45, 180, 0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					if (i % 2 == 0)
						g.setColor(Color.green);
					else
						g.setColor(new Color(45, 180, 0));

					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Oswald", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("" + score, (SCREEN_WIDTH - metrics.stringWidth("" + score)) / 2,
					g.getFont().getSize());
		} else {
			gameOver(g);
		}

	}

	public void newApple() {
		appleX = random.nextInt(3, ((int) (SCREEN_WIDTH / UNIT_SIZE) - 3)) * UNIT_SIZE;
		appleY = random.nextInt(3, ((int) (SCREEN_HEIGHT / UNIT_SIZE) - 3)) * UNIT_SIZE;
		newBarrier();
	}

	public void newBarrier() {
		barrierX = appleX - (2 * UNIT_SIZE);
		barrierY = appleY - (2 * UNIT_SIZE);
		getEntry();

	}

	public void getEntry() {
		entry = a[random.nextInt(11)];
	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		switch (direction) {
			case 'U':
				y[0] = y[0] - UNIT_SIZE;
				break;
			case 'D':
				y[0] = y[0] + UNIT_SIZE;
				break;
			case 'L':
				x[0] = x[0] - UNIT_SIZE;
				break;
			case 'R':
				x[0] = x[0] + UNIT_SIZE;
				break;
		}

	}

	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			score++;
			newApple();

		}
	}

	public void checkCollisions() {
		// checks if head collides with body
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		// check if head touches left border
		if (x[0] < 0) {
			x[0] = SCREEN_WIDTH;
		}
		// check if head touches right border
		if (x[0] > SCREEN_WIDTH) {
			x[0] = 0;
		}
		// check if head touches top border
		if (y[0] < 0) {
			y[0] = SCREEN_HEIGHT;
		}
		// check if head touches bottom border
		if (y[0] > SCREEN_HEIGHT) {
			y[0] = 0;
		}
		// check if head touches barrier
		for (int i = 0; i < 16; i++) {
			if (x[0] == xb[i] && y[0] == yb[i]) {
				if (x[0] != xb[entry] || y[0] != yb[entry]) {
					bodyParts -= 2;
					score -= 2;
				}
			}
		}
		// checks if bodyparts is less than or equal to 0
		if (bodyParts <= 0)
			running = false;
		if (!running) {
			timer.stop();
		}
	}

	public void gameOver(Graphics g) {

		g.setColor(Color.red);
		g.setFont(new Font("Oswald", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("" + score, (SCREEN_WIDTH - metrics1.stringWidth("" + score)) / 2,
				g.getFont().getSize());

		g.setColor(Color.red);
		g.setFont(new Font("Oswald", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if (direction != 'R') {
						direction = 'L';
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (direction != 'L') {
						direction = 'R';
					}
					break;
				case KeyEvent.VK_UP:
					if (direction != 'D') {
						direction = 'U';
					}
					break;
				case KeyEvent.VK_DOWN:
					if (direction != 'U') {
						direction = 'D';
					}
					break;
			}
		}
	}
}