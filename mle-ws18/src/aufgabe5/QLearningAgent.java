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
	private double alpha = 0.01;
	private double gamma = 0.9;
	public static final int POSSIBLE_ACTIONS = 3;
	private double q[][];
	boolean trained;

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

	public void learn(int s, int s_next, int a, double r) {
		int aIndex = a + 1;
		q[s][aIndex] += alpha * (r + gamma * (q[s_next][actionWithBestRating(s_next)]) - q[s][aIndex]);

	}

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

	public int calculateState(int xBall, int yMax, int yBall, int schlaegerMax, int xSchlaeger, int xVMax, int xV,
			int yVMax, int yV) {
		xV = (xV + 1) / 2;
		yV = (yV + 1) / 2;

		return ((((xBall * yMax + yBall) * schlaegerMax + xSchlaeger) * xVMax + xV) * yVMax + yV);
	}

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
		BufferedWriter writer = new BufferedWriter(new FileWriter("./TrainedQValues.txt"));
		writer.write(builder.toString());
		writer.close();
	}

	public void readFile() throws NumberFormatException, IOException {
		String saveQValues = "c:/Users/Sannes/Desktop/MLE/PingPongJava (1)/JavaInputOutput/TrainedQValues.txt";
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

