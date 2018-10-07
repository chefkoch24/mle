package aufgabe2;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SimulatedAnnealing {
	private int distance[][];
	private int travel[];

	public static double E = 2.718281;
	private static final int LENGTH = 100;

	public SimulatedAnnealing() {
		this.distance = fillDistances();
		this.travel = new int[LENGTH];
		for (int i = 0; i < travel.length; i++) {
			this.travel[i] = i;
		}
	}

	private int[][] fillDistances() {
		int[][] distance = new int[LENGTH][LENGTH];
		for (int i = 0; i < distance.length; i++) {
			for (int j = i + 1; j < distance[i].length; j++) {
				distance[i][i] = 0;
				distance[i][j] = (int) (Math.random() * 100) + 1;
				distance[j][i] = distance[i][j];
			}
		}
		for (int i = 0; i < distance.length; i++) {
			for (int j = 0; j < distance[i].length; j++) {
				System.out.print(distance[i][j] + " ");
			}
			System.out.println();
		}
		return distance;
	}

	private int getFitness(int[] travel) {
		int fitness = 0;
		for (int i = 0; i < travel.length; i++) {
			fitness += getDistance(travel[i], travel[(i + 1) % travel.length]);
		}
		return -fitness;
	}

	private int getDistance(int startCity, int endCity) {
		return distance[startCity][endCity];
	}

	private void swapCities(int index1, int index2) {
		int city = travel[index1];
		travel[index1] = travel[index2];
		travel[index2] = city;
	}

	private int randomIndex() {
		Random random = ThreadLocalRandom.current();
		return random.nextInt(travel.length);
	}

	private void simulatedAnnealing() {
		// TODO find better starting temperature
		double temperature = 1000;
		// The cooling constant
		double epsilon = 0.01;
		int[] startPoint = travel;
		int lastFitness = getFitness(startPoint);

		do {
			int index1 = randomIndex();
			int index2 = randomIndex();
			swapCities(index1, index2);
			int fitness = getFitness(travel);

			if (fitness > lastFitness) {
				lastFitness = fitness;
				for (int i = 0; i < travel.length; i++) {
					System.out.print(travel[i] + ", ");
				}
				System.out.println();
				System.out.println("Distance:" + lastFitness);

			} else if ((Math.random()) < Math.pow(E, ((fitness - lastFitness) / temperature))) {
				// Print the probability of moving to a more costly state
				System.out.println("Probability -> " + Math.pow(E, ((fitness - lastFitness) / temperature)));
				lastFitness = fitness;
				System.out.println("Distance: " + lastFitness);
			} else {
				swapCities(index2, index1);
			}
			temperature = temperature - epsilon;
		} while (temperature > epsilon);
	}

	public static void main(String[] args) {
		SimulatedAnnealing sa = new SimulatedAnnealing();
		sa.simulatedAnnealing();
	}

}
