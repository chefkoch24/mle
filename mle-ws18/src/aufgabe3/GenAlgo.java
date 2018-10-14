package aufgabe3;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GenAlgo {

	private static final int NUMPOPULATION = 6;
	private static final int LENGTH = 10;
	private static final double R = 0.5;
	private static final double M = 1.0;
	private int[] finalBitString = new int[LENGTH];
	private int[][] population = new int[NUMPOPULATION][LENGTH];
	private int[] fitnesses = new int[NUMPOPULATION];

	public GenAlgo() {
		initBitString(finalBitString);
		// print target
		System.out.println("Target: ");
		for (int i = 0; i < finalBitString.length; i++) {
			System.out.print(finalBitString[i] + ", ");
		}
		System.out.println();
		for (int i = 0; i < NUMPOPULATION; i++) {
			initBitString(population[i]);
			fitnesses[i] = getFitness(population[i]);
		}
		// print population
		for (int i = 0; i < NUMPOPULATION; i++) {
			System.out.println(i + 1 + ". Individium");
			for (int j = 0; j < LENGTH; j++) {
				System.out.print(population[i][j] + ", ");
			}
			System.out.println();
		}
	}

	private void initBitString(int[] array) {
		Random rnd = ThreadLocalRandom.current();
		for (int i = 0; i < array.length; i++) {
			array[i] = rnd.nextInt(2);
		}
	}
	
	private void calcFitnesses(){
		for (int i = 0; i < NUMPOPULATION; i++) {
			fitnesses[i] = getFitness(population[i]);
		}
	}

	private int getFitness(int[] array) {
		int fitness = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] == finalBitString[i]) {
				fitness++;
			}
		}
		return fitness;
	}

	private int getMaxFitness() {
		int maxFitness = 0;
		for (int i = 0; i < fitnesses.length; i++) {
			if (fitnesses[i] > maxFitness) {
				maxFitness = fitnesses[i];
			}
		}
		return maxFitness;
	}

	private int sumOfFitnesses() {
		int sum = 0;
		for (int i = 0; i < fitnesses.length; i++) {
			sum += fitnesses[i];
		}
		return sum;
	}

	private int[] copyBitString(int[] copiedArray) {
		int[] temp = new int[copiedArray.length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = copiedArray[i];
		}
		return temp;
	}
	
	private double calcProbability(int index) {
//		System.out.println((double)fitnesses[index] / sumOfFitnesses());
		return (double)fitnesses[index] / sumOfFitnesses();
	}

	private int selectHypothesis() {
		Random rnd = ThreadLocalRandom.current();
		double summe = 0.0;
		int index = rnd.nextInt(NUMPOPULATION);
//		System.out.println("Index " + index);
		double randNum = Math.random();
		while (summe < randNum) {
//			System.out.println("Index " + index);
			index++;
			index = index % NUMPOPULATION;
			summe = summe + calcProbability(index);
//			System.out.println(summe);
		}
		return index;
	}

	private void evolutionAlgorithm() {
		int maxFitness = getMaxFitness();

		while (maxFitness < LENGTH) {
			int[][] nextGeneration = new int[NUMPOPULATION][LENGTH];
			int lastIndex = 0;
			// Selection
			for (int i = 0; i < (1 - R) * NUMPOPULATION; i++) {
				nextGeneration[i] = copyBitString(population[selectHypothesis()]);
				lastIndex=i;
			}
			lastIndex++;
			System.out.println("lastIndex: " + lastIndex);
			// Crossover
			for (int i = 0; i < (R * NUMPOPULATION) / 2; i++) {
				int[] mum = population[selectHypothesis()];
				int[] dad = population[selectHypothesis()];
				int[] child1 = new int[LENGTH];
				int[] child2 = new int[LENGTH];
				// crosspoint in the middle of the parents
				for (int j = 0; j < LENGTH; j++) {
					if (j < LENGTH / 2) {
						child1[j] = mum[j];
						child2[j] = dad[j];
					} else {
						child1[j] = dad[j];
						child2[j] = mum[j];
					}
				}
				nextGeneration[(i+lastIndex)%NUMPOPULATION] = child1;
				nextGeneration[(i+1+lastIndex)%NUMPOPULATION] = child2;
			}
			for(int i = 0; i < nextGeneration.length; i++){
				fitnesses[i] = getFitness(nextGeneration[i]);
			}
			population = nextGeneration;
			maxFitness = getMaxFitness();
			System.out.println("Fitness: " + maxFitness);
		}
		System.out.println("Fertig");
		
	}

	public static void main(String[] args) {
		GenAlgo g = new GenAlgo();
		g.evolutionAlgorithm();
	}

}