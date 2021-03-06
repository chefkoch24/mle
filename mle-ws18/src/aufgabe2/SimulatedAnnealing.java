package aufgabe2;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SimulatedAnnealing {
	private int distance[][];
	private int travel[];

	public static double E = 2.718281;
	private static final int LENGTH = 100;
	private static double TEMPERATURE = 1000;
	private static double EPSILON = 0.007; // The cooling constant

	public SimulatedAnnealing() {
		this.distance = fillDistances();
		this.travel = new int[LENGTH];
		for (int i = 0; i < travel.length; i++) {
			this.travel[i] = i;
		}
		shuffleArray();
	}

	/**
	 * initialize Matrix of distances
	 * 
	 * @return matrix of distances
	 */
	private int[][] fillDistances() {
		int[][] distance = new int[LENGTH][LENGTH];
		for (int i = 0; i < distance.length; i++) {
			for (int j = i + 1; j < distance[i].length; j++) {
				distance[i][i] = 0;
				distance[i][j] = (int) (Math.random() * 100) + 1;
				distance[j][i] = distance[i][j];
			}
		}
		// printing matrix
		for (int i = 0; i < distance.length; i++) {
			for (int j = 0; j < distance[i].length; j++) {
				System.out.print(distance[i][j] + " ");
			}
			System.out.println();
		}
		return distance;
	}

	/**
	 * get the length of the travel distance
	 * 
	 * @param travel
	 * @return the negative length of the travel distance
	 */
	private int getFitness(int[] travel) {
		int fitness = 0;
		for (int i = 0; i < travel.length; i++) {
			fitness += getDistance(travel[i], travel[(i + 1) % travel.length]);
		}
		// -Fitness for climbing up
		return -fitness;
	}

	/**
	 * distance between two cities
	 * 
	 * @param startCity
	 * @param endCity
	 * @return
	 */
	private int getDistance(int startCity, int endCity) {
		return distance[startCity][endCity];
	}

	/**
	 * change the position of two cities in the travel array
	 * 
	 * @param index1
	 * @param index2
	 */
	private void swapCities(int index1, int index2) {
		int city = travel[index1];
		travel[index1] = travel[index2];
		travel[index2] = city;
	}

	private int randomIndex() {
		Random random = ThreadLocalRandom.current();
		return random.nextInt(travel.length);
	}

	/**
	 * simulated annealing algorithm for traveling salesman problem
	 */
	private void simulatedAnnealing() {
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
				System.out.println("Distance:" + -lastFitness);

			} else if ((Math.random()) < Math.pow(E, ((fitness - lastFitness) / TEMPERATURE))) {
				// Print the probability of moving to a more costly state
				System.out.println("Probability -> " + Math.pow(E, ((fitness - lastFitness) / TEMPERATURE)));
				lastFitness = fitness;
				System.out.println("Distance: " + -lastFitness);
			} else {
				swapCities(index2, index1);
			}
			TEMPERATURE = TEMPERATURE - EPSILON;
		} while (TEMPERATURE > EPSILON);
	}

	public void shuffleArray() {
		Random rnd = ThreadLocalRandom.current();
		for (int i = travel.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			int a = travel[index];
			travel[index] = travel[i];
			travel[i] = a;
		}
	}

	public static void main(String[] args) {
		SimulatedAnnealing sa = new SimulatedAnnealing();
		sa.simulatedAnnealing();
	}

}
