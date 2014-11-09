
import edu.uwm.cs351.Person;
import edu.uwm.cs351.util.Table;

public class Driver {

	public static void main(String[] args) {
		
		final int DEFAULT_CAPACITY = 17;
		Table table = new Table(DEFAULT_CAPACITY);
		
		Person[] brewers = { 
				new Person("Ricky", "Weeks"),
				new Person("Carlos", "Gomez"),
				new Person("Ryan", "Braun"),
				new Person("Prince", "Fielder"),
				new Person("Casey", "McGehee"),
				new Person("Corey", "Hart"),
				new Person("Gregg", "Zaun"),
				new Person("Alcides", "Escobar"),
				new Person("Yovani", "Gallardo")
		};
			
		for(int i=0; i<brewers.length; ++i) {
			table.put(brewers[i].getKey(), brewers[i]);
		}
		System.out.println("Number of collisions: " + table.getNumCollisions());
	}
}
