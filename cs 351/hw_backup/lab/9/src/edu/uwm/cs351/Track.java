package edu.uwm.cs351;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Track {
	
	private int num_cars;
	private String name;
	private Stack<Car> cars;
	private final int MINWEIGHT=0;

	public Track(String n) { 
		num_cars = 0;
		name = n;
		cars = new Stack<Car>();
	}
	
	public Car removeCar() {
		if (cars.empty()) {
			throw new IllegalStateException("No cars to remove!");
		}
		
		Car car_atback;
		
		//Your code here: use Stack methods to remove car,
		//print it (given) and return it
		car_atback = cars.pop();
		
		System.out.println("Removing " + car_atback.getName() + " from " + name);       
		return car_atback;
	}
	
	public String getName() {
		return name;
	}

	public void addCar(Car new_car) {
		cars.push(new_car);
		num_cars++;
		System.out.println("Adding " + new_car.getName() + " to " + name);     
	}
	
	public Boolean willTrainPassdepot() {
		Queue<Car> train = new LinkedList<Car>();
	  
		int firstcar_weight = MINWEIGHT;

		if (cars.empty()) {
			throw new IllegalStateException("An empty train can't pass a depot!");
		}
		
		while(!cars.empty()) {
			train.add(cars.peek());
			System.out.println("Putting " + (cars.peek()).getName() + " on depot queue.");     
			cars.pop();
	    }
		
		while(!train.isEmpty()) {
			if ((train.peek()).getWeight() < firstcar_weight) {
				return false;
			}
			else {
				firstcar_weight = (train.peek()).getWeight();
			}
			train.remove();
	    }
		return true;
	}
}
