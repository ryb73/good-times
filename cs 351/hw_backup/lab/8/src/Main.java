import java.util.Comparator;

import edu.uwm.cs351.SortedBag;

class Main {
	
	static Comparator<Integer> ascendingValue  = new Comparator<Integer>() {
		public int compare(Integer i1, Integer i2) {
			return i1.compareTo(i2);
		}

	};
	
	static Comparator<Integer> descendingValue  = new Comparator<Integer>() {
		public int compare(Integer i1, Integer i2) {
			return i2.compareTo(i1);
		}

	};
	
	public static void main(String[] args) { 
		
		SortedBag<Integer> bagOnums = new SortedBag<Integer>(ascendingValue);
		
		//add odd numbers to the bag
		for(int i=1; i<=4; i+=2) {
			bagOnums.add(i);
		}
		
		//add even numbers to the bag
		for(int i=2; i<=4; i+=2) {
			bagOnums.add(i);
		}
		
		//print bag contents
		System.out.println("Bag contents: " + bagOnums);
		
		//sort by descending value instead
		bagOnums.setComparator(descendingValue);
		
		//print bag contents
		System.out.println("Bag contents: " + bagOnums);
	}
}
