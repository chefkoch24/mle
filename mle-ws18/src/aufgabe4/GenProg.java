package aufgabe4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GenProg {
	// The amount of operations of a program
	// Has to be changed in the VM too (maxOperationPerVMSimulation)
	private static final int MAX_OPERATIONS = 1000;
	// Max prime numbers
	private static final int THRESHHOLD = 50;
	// Amount of programs
	private static final int P = 500;
	// Mutation rate
	// We don't want to mutate our best program, that means we mutate P*M programs at index = 0..<(P*M)
	private static final double M = 0.9;
	private ArrayList<Program> programs = new ArrayList<>();
	private VM vm = new VM();

	public GenProg() {
		initPrograms(programs);
		for (int i = 0; i < P; i++) {
			// Load program into VM
			vm.setMemAndResizeMAX(programs.get(i).getMemory());
			vm.simulate();
			// Fitness = amount of prime numbers
			programs.get(i).setFitness(vm.primeNumbers.size());
			vm.reset();
		}
	}

	private void evolutionAlgorithm() {
		// Sort programs by Fitness
		Collections.sort(programs);
		// Get the fitness of the currently best program
		// The best program is located at the last position of the list
		int maxFitness = programs.get(P - 1).getFitness();
		int lastFitness = maxFitness;

		while (maxFitness < THRESHHOLD) {
			// Select the best programs
			selection(programs);
			// Mutate one operation in the program
			Random rnd = ThreadLocalRandom.current();
			for (int i = 0; i < P * M; i++) {
				int programIndex = rnd.nextInt((int) (P * M));
				int memIndex = rnd.nextInt(MAX_OPERATIONS);
				programs.get(programIndex).mutateProgram(memIndex, rnd.nextInt(256));
			}

			for (int i = 0; i < P; i++) {
				// Load program into VM
				vm.setMemAndResizeMAX(programs.get(i).getMemory());
				vm.simulate();
				// Fitness = amount of prime numbers
				programs.get(i).setFitness(vm.primeNumbers.size());
				vm.reset();
			}
			Collections.sort(programs);
			maxFitness = programs.get(P - 1).getFitness();
			// Print the best fitness only if it is better
			if (lastFitness < maxFitness) {
				lastFitness = maxFitness;
				System.out.println("Fitness: " + maxFitness + " prime numbers");
			}
		}
		// Get the best program and print its memory
		Program program = programs.get(P - 1);
		System.out.println("Memory: ");
		for (int i = 0; i < MAX_OPERATIONS; i++) {
			System.out.print(program.getMemory()[i] + " ,");
		}
	}

	// Select the P/2 best programs and override the bad ones in the same list
	// Example: Array sorted by fitness [0,2,2,5,6,10]
	// After the selection: [10,6,5,5,6,10]
	private void selection(ArrayList<Program> programs) {
		Collections.sort(programs);
		Program program;
		Program copy;
		int index = P - 1;
		int index2 = 0;
		while (index >= (P / 2)) {
			program = programs.get(index);
			copy = new Program(MAX_OPERATIONS);
			copy.setMemory(copyMemory(program.getMemory()));
			programs.set(index2, copy);
			index--;
			index2++;
		}
	}

	// private int selectHypothesis() {
	// Random rnd = ThreadLocalRandom.current();
	// double summe = 0.0;
	// int index = rnd.nextInt(P);
	// double randNum = Math.random();
	// while (summe < randNum) {
	// index++;
	// index = index % P;
	// summe = summe + calcProbability(index);
	// }
	// return index;
	// }

	// private double calcProbability(int index) {
	// int fitness = programs.get(index).getFitness();
	// return (double) fitness / sumOfFitnesses();
	// }

	private int[] copyMemory(int[] arrayToCopy) {
		int[] temp = new int[arrayToCopy.length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = arrayToCopy[i];
		}
		return temp;
	}

	// Calculate the sum of all fitnesses
	private int sumOfFitnesses() {
		int sum = 0;
		for (int i = 0; i < programs.size(); i++) {
			sum += programs.get(i).getFitness();
		}
		return sum;
	}

	// Initialize Programs and add them to the programs list
	private void initPrograms(ArrayList<Program> programs) {
		int[] mem = new int[MAX_OPERATIONS];
		for (int i = 0; i < P; i++) {
			programs.add(i, new Program(MAX_OPERATIONS));
			mem = programs.get(i).getMemory();
			for (int j = 0; j < mem.length; j++) {
				mem[j] = ThreadLocalRandom.current().nextInt(256);
			}
		}
	}

	public static void main(String[] args) {
		GenProg g = new GenProg();
		g.evolutionAlgorithm();
	}
}