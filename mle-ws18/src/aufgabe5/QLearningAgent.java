package aufgabe5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class QLearningAgent {
	private double epsilon = 0.1; 
	private double alpha = 0.01; // Lernrate (0..1)
	private double gamma = 0.9; // Bewertungsfaktor (0..1)
	public static final int POSSIBLE_ACTIONS = 3; // Mögliche Aktionen (links, rechts, bleiben)
	private double q[][]; // Q-Learning-Array
	boolean trained; 

	/**
	 * Führt das Q-Learning durch
	 * 
	 * @param xBallMax: Maximale Position des Balls in x-Richtung
	 * @param yMax: Maximale Ausdehnung des Spielfelds in y-Richtung
	 * @param yBallMax: Maximale Position des Balls in y-Richtung
	 * @param schlaegerMax: Breite des Schlägers
	 * @param xSchlaegerMax: Maximale Position des Schlägers in x-Richtung
	 * @param xVMax: Maximale Geschwindigkeit des Balls in x-Richtung (1)
	 * @param yVMax: Maximale Geschwindigkeit des Balls in y-Richtung (1)
	 * @param trained
	 * @throws NumberFormatException 
	 * @throws IOException
	 */
	public QLearningAgent(int xBallMax, int yMax, int yBallMax, int schlaegerMax, int xSchlaegerMax, int xVMax,
			int yVMax, boolean trained) throws NumberFormatException, IOException {
		int s = ((((xBallMax * yMax + yBallMax) * schlaegerMax + xSchlaegerMax) * xVMax + xVMax - 1) * yVMax + yVMax - 1);
		this.trained = trained;
		q = new double[s][POSSIBLE_ACTIONS];
		System.out.println(s);
		if (!trained) {
			for (int i = 0; i < q.length; i++) {
				for (int k = 0; k < POSSIBLE_ACTIONS; k++) {
					q[i][k] = Math.random();
				}
			}
		} else {
			readFile();
		}
	}

	/**
	 * Lernt durch die übergebenen Zustände, ob die Aktion erfolgreich war und speichert die Werte in das q-array.
	 * 
	 * @param s: Aktueller Zustand
	 * @param s_next: Nächster Zustand
	 * @param a: Aktion
	 * @param r: Belohnung
	 */
	public void learn(int s, int s_next, int a, double r) {
		int aIndex = a + 1;
		q[s][aIndex] += alpha * (r + gamma * (q[s_next][actionWithBestRating(s_next)]) - q[s][aIndex]);

	}

	/**
	 * Wählt eine zufällige Aktion anhand des Zustands aus
	 * 
	 * @param s: Zustand
	 * @return: Gibt die Aktion als int zurück.
	 */
	public int chooseAction(int s) {
		int a = 0;
		if (Math.random() < epsilon && trained == false) {
			// Bound = 2, because we want 1 to be inclusive
			a = ThreadLocalRandom.current().nextInt(-1, 2);
		} else {
			a = actionWithBestRating(s) - 1;
		}
		return a;
	}

	/**
	 * Wählt die Aktion mit der besten Rate aus
	 * 
	 * @param s: Zustand s
	 * @return: Gibt die Aktion als int zurück.
	 */
	public int actionWithBestRating(int s) {
		double max = 0;
		int index = 0;
		for (int i = 0; i < POSSIBLE_ACTIONS; i++) {
			if (q[s][i] > max) {
				max = q[s][i];
				index = i;
			}
		}
		return index;
	}

	/**
	 * Berechnet den aktuellen Zustand s vom mehrdimensionalen Feld in ein eindimensionalen Wert
	 * 
	 * @param xBall: aktuelle Position des Balls in x-Richtung
	 * @param yMax: Maximale Ausdehnung des Feldes in y-Richtung
	 * @param yBall: aktuelle Position des Balls in y-Richtung
	 * @param schlaegerMax: Maximale Position des Schlägers in x-Richtung
	 * @param xSchlaeger: Breite des Schlägers
	 * @param xVMax: Maximale Geschwindigkeit des Balls in x-Richtung (1)
	 * @param xV: Aktuelle Geschwindigkeit des Balls in x-Richtung (0..1)
	 * @param yVMax: Maximale Geschwindigkeit des Balls in y-Richtung (1)
	 * @param yV: Aktuelle Geschwindigkeit des Balls in y-Richtung (0..1)
	 * @return: Gibt den Zustand s als int zurück
	 */
	public int calculateState(int xBall, int yMax, int yBall, int schlaegerMax, int xSchlaeger, int xVMax, int xV,
			int yVMax, int yV) {
		xV = (xV + 1) / 2;
		yV = (yV + 1) / 2;
		return ((((xBall * yMax + yBall) * schlaegerMax + xSchlaeger) * xVMax + xV) * yVMax + yV);
	}

	/**
	 * Speichert die gelernten Zustände und Aktionen aus dem q-array in die Datei "TrainedQValues.txt" ab.
	 * 
	 * @throws IOException
	 */
	public void writeToFile() throws IOException {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < q.length; i++) {
			for (int j = 0; j < POSSIBLE_ACTIONS; j++) {
				builder.append(q[i][j] + "");
				if (j < q.length - 1)
					builder.append(",");
			}
			builder.append("\n");
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/student/git/mle/mle-ws18/src/aufgabe5/TrainedQValues.txt"));
		writer.write(builder.toString());
		writer.close();
	}

	/**
	 * Lädt zuvor gespeicherte Zustände und Aktionen aus der Datei "TrainedQValues.txt" ins q-array.
	 * 
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void readFile() throws NumberFormatException, IOException {
		String saveQValues = "C:/Users/student/git/mle/mle-ws18/src/aufgabe5/TrainedQValues.txt";
		BufferedReader reader = new BufferedReader(new FileReader(saveQValues));
		String line = "";
		int row = 0;
		while ((line = reader.readLine()) != null) {
			String[] cols = line.split(",");
			int col = 0;
			for (String c : cols) {
				this.q[row][col] = Double.parseDouble(c);
				col++;
			}
			row++;
		}
		reader.close();
	}

}

