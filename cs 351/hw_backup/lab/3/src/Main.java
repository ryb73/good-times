import edu.uwm.cs351.IntArrayBag;

public class Main {
	
	public static void main(String args[]) {
		IntArrayBag bag = new IntArrayBag(10);
		for(int i = 1; i <= 10; ++i) {
			bag.add(i);
		}

		displaySizeAndCapacity(bag);

		for(int i = 2; i <= 10; i += 2) {
			bag.add(i);
		}

		for(int i = 1; i <= 9; i += 2) {
			bag.remove(i);
		}

		displaySizeAndCapacity(bag);

		for(int i = 1; i <= 10; ++i) {
			System.out.println("Number of occurrences for " + i + ": "
					+ bag.countOccurrences(i));
		}
	}

	/**
	 * @param bag
	 */
	private static void displaySizeAndCapacity(IntArrayBag bag) {
		System.out.println("Bag size: " + bag.size());
		System.out.println("Bag capacity: " + bag.getCapacity());
		System.out.println();
	}
}
