package edu.uwm.cs351;

public class Car {
	
	private int weight;
	private String name;

	
	public Car() {
		weight = 0;
		name= "";
	}
	
	public Car(String n, int w) {
		weight = w;
		name = n;
	}
	
	public void setWeight(int w) {
		weight = w;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public String getName() {
		return name;
	}
}
