package aufgabe5;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.io.IOException;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	public static final int imageWidth = 360;
	public static final int imageHeight = 360;
	public InputOutput inputOutput = new InputOutput(this);
	public boolean stop = false;
	ImagePanel canvas = new ImagePanel();
	ImageObserver imo = null;
	Image renderTarget = null;
	public int mousex, mousey, mousek;
	public int key;
	int trainingsteps = 10; // steps auf 0 setzen wenn trained auf true
	// true -> QTable wird aus einer Datei ausgelesen und nur noch die beste Aktion wird ausgewählt(e-greedy aus)
	// false -> QTable random mit Werten zwischen 0 und 1 (e-greedy an)
	boolean trained = true;

	/**
	 * Konstruktor
	 * 
	 * @param args
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public MainFrame(String[] args) throws NumberFormatException, IOException {
		super("PingPong");

		getContentPane().setSize(imageWidth, imageHeight);
		setSize(imageWidth + 50, imageHeight + 50);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		canvas.img = createImage(imageWidth, imageHeight);

		add(canvas);

		run();
	}

	/**
	 * Startet den Ablauf von PinPong mit dem Q-Learning Algorithmus
	 * 
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void run() throws NumberFormatException, IOException {
		int counter = 0;
		int xBall = 5, yBall = 6, xSchlaeger = 5, xV = 1, yV = 1;
		int score = 0;
		int xBallMax = 10;
		int yBallMax = 11;
		int yMax = 11;
		int schlaegerMax = 9;
		int xSchlaegerMax = 9;
		int xVmax = 2;
		int yVmax = 2;
		QLearningAgent qla = new QLearningAgent(xBallMax, yMax, yBallMax, schlaegerMax, xSchlaegerMax, xVmax, yVmax,
				trained);
		int s = qla.calculateState(xBall, yMax, yBall, schlaegerMax, xSchlaeger, xVmax, xV, yVmax, yV);
		int s_next;
		while (!stop) {
			inputOutput.fillRect(0, 0, imageWidth, imageHeight, Color.black);
			inputOutput.fillRect(xBall * 30, yBall * 30, 30, 30, Color.green);
			inputOutput.fillRect(xSchlaeger * 30, 11 * 30 + 20, 90, 10, Color.orange);

			int action = qla.chooseAction(s);

			xSchlaeger += action;

			if (xSchlaeger < 0) {
				xSchlaeger = 0;
			}
			if (xSchlaeger > 9) {
				xSchlaeger = 9;
			}

			xBall += xV;
			yBall += yV;
			if (xBall > 9 || xBall < 1) {
				xV = -xV;
			}
			if (yBall > 10 || yBall < 1) {
				yV = -yV;
			}
			s_next = qla.calculateState(xBall, yMax, yBall, schlaegerMax, xSchlaeger, 2, xV, 2, yV);
			if (yBall == 11) {
				if (xSchlaeger == xBall || xSchlaeger == xBall - 1 || xSchlaeger == xBall - 2) {
					if (counter > trainingsteps)
						System.err.println("positive reward");
					qla.learn(s, s_next, action, 1);
					s = s_next;
				} else {
					if (counter > trainingsteps)
						System.out.println("negative reward");
					qla.learn(s, s_next, action, -1);
					s = s_next;
				}
			}
			qla.learn(s, s_next, action, 0);
			s = s_next;
			if (counter > trainingsteps) {
				repaint();
				try {
					Thread.sleep(10); // 1000 milliseconds is one second.
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				if (!trained) {
					qla.writeToFile();
					stop = true;
				}

			} else {
				counter++;
			}

			validate();
		}

		setVisible(false);
		dispose();
	}

	public void mouseReleased(MouseEvent e) {
		mousex = e.getX();
		mousey = e.getY();
		mousek = e.getButton();
	}

	public void mousePressed(MouseEvent e) {
		mousex = e.getX();
		mousey = e.getY();
		mousek = e.getButton();
	}

	public void mouseExited(MouseEvent e) {
		mousex = e.getX();
		mousey = e.getY();
		mousek = e.getButton();
	}

	public void mouseEntered(MouseEvent e) {
		mousex = e.getX();
		mousey = e.getY();
		mousek = e.getButton();
	}

	public void mouseClicked(MouseEvent e) {
		mousex = e.getX();
		mousey = e.getY();
		mousek = e.getButton();
	}

	public void mouseMoved(MouseEvent e) {
		mousex = e.getX();
		mousey = e.getY();
		mousek = e.getButton();
	}

	public void mouseDragged(MouseEvent e) {
		mousex = e.getX();
		mousey = e.getY();
		mousek = e.getButton();
	}

	public void keyTyped(KeyEvent e) {
		key = e.getKeyCode();
	}

	public void keyReleased(KeyEvent e) {
		key = e.getKeyCode();
	}

	public void keyPressed(KeyEvent e) {
		System.out.println(e.toString());
	}

	/**
	 * Construct main frame
	 *
	 * @param args
	 *            passed to MainFrame
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		new MainFrame(args);
	}
}
