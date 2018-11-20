package aufgabe4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class GenProg2 {
		// The amount of operations in a program
		private static final int MAX_OPERATIONS = 1000;
		// Max prime numbers
		private static final int THRESHHOLD = 100;
		// Amount of VMs
		private static final int P = 500;
		private static final double M = 0.05;
		private ArrayList<VM2> vMs = new ArrayList<>();
		private int maxFitness;


		public GenProg2() {
			initVMs(vMs);
			simulate(vMs);
			Collections.sort(vMs);
			this.maxFitness = vMs.get(P - 1).getFitness();
			reset(vMs);
		}

		private void evolutionAlgorithm() {
			int lastFitness = this.maxFitness;
			while (this.maxFitness < THRESHHOLD) {
				// Select the best vMs mem
				selection(vMs);
				// Mutate one operation in the program
				Random rnd = ThreadLocalRandom.current();
				for (int i = 0; i < P * M; i++) {
					int vmIndex = rnd.nextInt(P);
					int memIndex = rnd.nextInt(MAX_OPERATIONS);
					vMs.get(vmIndex).mutateMem(memIndex, rnd.nextInt(7000));
				}
				simulate(vMs);
				Collections.sort(vMs);
				maxFitness = vMs.get(P - 1).getFitness();
				reset(vMs);
				// Print fitness only if it is better
				if (lastFitness < maxFitness) {
					lastFitness = maxFitness;
					System.out.println("Fitness: " + maxFitness + " prime numbers");
				}
			}
			// Get the best program and print its memory
			VM2 vm = vMs.get(P - 1);
			System.out.println("Memory: ");
			for (int i = 0; i < MAX_OPERATIONS; i++) {
				System.out.print(vm.mem[i] + " ,");
			}
		}

		private void selection(ArrayList<VM2> vMs) {
			Collections.sort(vMs);
			VM2 vm;
			VM2 vmToChange;
			int index = P - 1;
			int index2 = 0;
			while (index >= (P / 2)) {
				vm = vMs.get(index);
				vmToChange = vMs.get(index2);
				vmToChange.changeMem(vm.mem);
				index--;
				index2++;
			}
		}

		// Initialize VMs and add them to the vMs (population) list
		private void initVMs(ArrayList<VM2> vMs) {
			int[] mem = new int[MAX_OPERATIONS];
			for (int i = 0; i < P; i++) {
				vMs.add(i, new VM2());
				for (int j = 0; j < mem.length; j++) {
					mem[j] = ThreadLocalRandom.current().nextInt(7000);
				}
				vMs.get(i).setMemAndResizeMAX(mem);
				mem = new int[MAX_OPERATIONS];
			}
		}

		private void simulate(ArrayList<VM2> vMs) {
			for(int k = 0; k < P; k++) {
				vMs.get(k).simulate();
			}
		}

		private void reset(ArrayList<VM2> vMs) {
			for(int k = 0; k < P; k++) {
				vMs.get(k).reset();
			}
		}

		public static void main(String[] args) {
			new GenProg2().evolutionAlgorithm();
		}
}
