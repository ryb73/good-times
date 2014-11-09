import edu.uwm.cs351.Bag;

public class Main {

	public static void main(String[] args) {
		Bag<Integer> bagOnums = new Bag<Integer>();
		Bag<String> bagOwords = new Bag<String>();
		
		for(int i=1; i<=10; ++i) {
			bagOnums.add(Integer.valueOf(i));
			bagOwords.add(String.valueOf((char)(i+64)));
		}
		
		for(int i=2; i<=10; i+=2) {
			bagOnums.add(Integer.valueOf(i));
			bagOwords.add(String.valueOf((char)(i+64)));
		}
		
		for(int i=1; i<=10; i+=2) {
			bagOnums.remove(Integer.valueOf(i));
			bagOwords.remove(String.valueOf((char)(i+64)));
		}
		
		System.out.println("bagOnums size: " + bagOnums.size());
		System.out.println("bagOwords size: " + bagOwords.size());
		
		for(int i=1; i<=10; ++i) {
			System.out.println("\nItem " + i + " is present in bagOnums " + bagOnums.countOccurrences(i) + " time(s).");
			System.out.println("Item " + (char)(i+64) + " is present in bagOwords " + bagOwords.countOccurrences(String.valueOf((char)(i+64))) + " time(s).");
		}
	}

}
