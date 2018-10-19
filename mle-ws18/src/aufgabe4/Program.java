package aufgabe4;

public class Program implements Comparable<Program> {
	private int fitness = 0;
	private int[] memory;

	public Program(int size) {
		memory = new int[size];
	}

	@Override
	public int compareTo(Program otherProgram) {
		if(this.fitness == otherProgram.getFitness()) {
			return 0;
		}
		return this.fitness > otherProgram.getFitness() ? 1 : -1;
	}

	public void mutateProgram(int index, int operation) {
		if(index >=0 && index < memory.length) {
			memory[index] = operation;
		}
	}

	public int getFitness() { return fitness; }

	public void setFitness(int fitness) { this.fitness = fitness; }

	public int[] getMemory() { return memory; }

	public void setMemory(int[] memory) { this.memory = memory; }
}
