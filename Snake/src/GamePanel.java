import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.Random; 
import javax.swing.JPanel;
     
public class GamePanel extends JPanel implements ActionListener {
	static final int Screen_WIDTH = 600;
	static final int Screen_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNIT = (Screen_WIDTH * Screen_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 75;

	final int x[] = new int[GAME_UNIT];
	final int y[] = new int[GAME_UNIT];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;

	GamePanel() {

		random = new Random();
		this.setPreferredSize(new Dimension(Screen_WIDTH, Screen_HEIGHT));
		this.setBackground(Color.BLACK);
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
			/*
			 * for (int i = 0; i < Screen_WIDTH / UNIT_SIZE; i++) { g.drawLine(i *
			 * UNIT_SIZE, 0, i * UNIT_SIZE, Screen_HEIGHT); g.drawLine(0, i * UNIT_SIZE,
			 * Screen_WIDTH, i * UNIT_SIZE); }
			 */			
			g.setColor(Color.RED);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.GREEN);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					//g.setColor(new Color(45, 180, 0));
					g.setColor(new Color(random.nextInt(225),random.nextInt(225),random.nextInt(225)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

				} 
			}
			g.setColor(Color.red);
			g.setFont(new Font("ink free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (Screen_WIDTH - metrics.stringWidth("Score: "+applesEaten)) / 2, g.getFont().getSize());
		
		}
		else {
			gameOver(g);
		}
	}

	public void newApple() {
		appleX = random.nextInt((int) (Screen_WIDTH / UNIT_SIZE) * UNIT_SIZE);
		appleY = random.nextInt((int) (Screen_WIDTH / UNIT_SIZE) * UNIT_SIZE);
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
			applesEaten++;
			newApple();
		}
	}

	public void checkCollisions() {
		// check if head collides with body
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		// check if head touches right border
		if (x[0] < 0) {
			running = false;
		}
		// check if head touches right border
		if (x[0] > Screen_WIDTH) {
			running = false;
		}
		// check if head touches top border
		if (y[0] < 0) {
			running = false;
		}
		// check if head touches bottom border
		if (y[0] > Screen_HEIGHT) {
			running = false;
		}
		if (!running) {
			timer.stop();
		}
	}

	public void gameOver(Graphics g) {

		// Game over text
		g.setColor(Color.red);
		g.setFont(new Font("ink free", Font.BOLD, 75));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (Screen_WIDTH - metrics1.stringWidth("GAME OVER")) / 2, Screen_HEIGHT / 2);
		
		g.setColor(Color.red);
		g.setFont(new Font("ink free", Font.BOLD, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (Screen_WIDTH - metrics2.stringWidth("Score: "+applesEaten)) / 2, g.getFont().getSize());
	
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
