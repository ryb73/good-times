import java.util.Comparator;

import edu.uwm.cs351.TreeSet;


public class Beers {

	static Comparator<String> lexicographic = new Comparator<String>() {
		public int compare(String o1, String o2) {
			return o1.toString().compareTo(o2.toString());
		}
	};
	
	public static void main(String[] args) {
		final String[] beers = {"Leinie", "Riverwest", "Strohs", "Bud", "Pabst", "Best", 
				"Anchor", "Sprecher", "Lakefront", "Coors", "Michelob", "Blatz", "Redhook", "Miller"};
		final int totalBeers = 14;

		// If you don't want debugging, change the value below:
		final boolean debug = true;
		
		TreeSet<String> s = new TreeSet<String>(lexicographic, debug);
		int i;

		for(i = 0; i < totalBeers; ++i) {
			s.add(beers[i]);
		}

		s.print();
		return;
	}
}
