package aufgabe1;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class HillClimber {

	private int[][] distance;
	private int [] travel;
	
	private static final int LENGTH = 100;
	private static final int MAX_RUNS = 100000;

	public HillClimber() {
		this.distance = fillDistances();
		this.travel = new int [LENGTH];
		for(int i = 0; i < travel.length; i++){
			this.travel[i] = i;
		}
	}

	/**
	 * initialize Matrix of distances
	 * @return matrix of distances
	 */
	private int[][] fillDistances() {
		int [][] distance = new int[LENGTH][LENGTH];
		for (int i = 0; i < distance.length; i++) {
		    for (int j = i+1; j < distance[i].length; j++) {
		    	distance[i][i] = 0;
		        distance [i][j] = (int) (Math.random() *1000) + 1;
		        distance[j][i] = distance[i][j];  
		    }
		}
		//printing matrix 
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
	 * @param travel
	 * @return the negative length of the travel distance
	 */
	private int getFitness(int [] travel){
		int fitness = 0;
		for(int i = 0; i < travel.length; i++){
			fitness += getDistance(travel[i], travel[(i+1)% travel.length]);
		}
		// -Fitness for climbing up
		return -(fitness);
	}
	
	/**
	 * distance between two cities
	 * @param startCity
	 * @param endCity
	 * @return
	 */
	private int getDistance(int startCity, int endCity){
		return distance[startCity][endCity];
	}
	
	/**
	 * change the position of two cities in the travel array
	 * @param index1
	 * @param index2
	 */
	private void swapCities (int index1, int index2){
		int city = travel[index1];
		travel[index1] = travel[index2];
		travel[index2] = city;
	}
	
	private int randomIndex(){
		Random random = ThreadLocalRandom.current();    
		return random.nextInt(travel.length);
	}
	
	/**
	 * Hill climbing algorithm for traveling salesman problem
	 */
	private void hillClimbing(){
		int counter = 0;
		int [] startPoint = travel;
		int lastFitness = getFitness(startPoint);
		do{
			counter++;
			int index1 = randomIndex();
			int index2 = randomIndex();
			
			swapCities(index1, index2);
			int fitness = getFitness(travel);
			if( fitness > lastFitness){
				lastFitness = fitness;
				for(int i = 0; i < travel.length; i++){
					System.out.print(travel[i] + ", ");
				}
				System.out.println();
				//System.out.println("Counter:" + counter);
				System.out.println("Distance:" + -lastFitness);
			}else{
				swapCities(index2, index1);
			}
		}while(counter < MAX_RUNS);
	}

	public static void main(String[] args) {
		HillClimber h = new HillClimber();
		h.hillClimbing();

	}

}
