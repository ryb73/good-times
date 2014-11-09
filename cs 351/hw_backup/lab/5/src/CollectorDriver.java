import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.uwm.cs351.Collector;
import edu.uwm.cs351.Collector.ThingType;


public class CollectorDriver {
	
	private final static int THINGS = 5;
	private final static String things[] = { "coin", "gem", "key", "ring", "stone" };
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	private final static String describe(ThingType t) {  
		if (t == ThingType.NOTHING) {
			return "nothing, check input!!"; 
		}
		else {
		   return things[t.getValue()];
		}
	}

	private static ThingType query_thing() {  
		System.out.print("Pick one of these things: (");
		for (int i = 0; i < THINGS; i++) {    
			System.out.print("a " + things[i]);
			if (i != THINGS - 1) {
				System.out.print(", "); 
			}
	        else {
	        	System.out.println(".)");
	        }
		}
		try {
			String input = in.readLine();
			input = input.toLowerCase();
			if (input == null) {
				return ThingType.NOTHING; // no choice
			}
	
			// find it among the things (substring is legal)
			for (int i = 0; i < THINGS; i++) {
				if (input.contains(things[i])) {  
					if (input.contains(",")) {  
						System.out.println("You can only grab one thing at a time.");
						System.out.println("(processing your request as '" + things[i] + "')");
					}
					return ThingType.values()[i+1];
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ThingType.NOTHING;
	}

	private static void print_choices() 
	{
		System.out.println("Please choose:");
		System.out.println("  (E) Exit.");
		System.out.println("  (G) Grab: have the collector grab something.");
		System.out.println("  (L) Leave: have the collector leave something.");
		System.out.println("  (A) LeaveAll: have the collector leave all of something.");
		System.out.println("  (S) Show: have the collector show everything.");
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		char choice = '\0';
		Collector collector = new Collector();

		while (choice != 'e' && choice != 'E') {  
			print_choices();
			try {
				choice = (char) in.read();
				while ((char) in.read() != '\n');
			} 
			catch(IOException e) {
				e.printStackTrace();
			}

		    switch (choice) { 
		    	case 'e': // Exit
		        case 'E': System.out.println("Bye, bye.");
		            continue;
		            //break;
		        case 'g': // Grab
		        case 'G':
		        {   
		        	System.out.println("You want the collector to grab some thing.");
		            ThingType t = query_thing();
		            if (t != ThingType.NOTHING) {
		            	collector.grab(t);
		            }
		            System.out.println("Collector grabbed a " + describe(t) + ".");
		        }
		        break;

		        case 'l': // Leave
		        case 'L':
		        {   
		        	System.out.println("You want the collector to leave some thing.");
		            try {   
		            	ThingType t = query_thing();
		                collector.leave(t);
		                System.out.println("Collector left a " + describe(t) + ".");
		            }
		            catch (IllegalStateException e) {   
		            	System.out.println("Collectors can't leave things they don't have!"); 
		            }
		        }
		        break;

		        case 'a': // LeaveAll
		        case 'A':
		        {   
		        	System.out.println("You want the collector to leave all of some thing.");
		            try {   
		            	ThingType t = query_thing();
		                collector.leaveAll(t);
		                System.out.println("Collector left all " + describe(t) + "s.");
		            }
		            catch (IllegalStateException e) {   
		            	System.out.println("Collectors can't leave things they don't have!");
		            }
		        }
		        break;

		        case 's': // Show
		        case 'S':
		        {   
		        	if (!collector.hasNoThings()) {   
		        		System.out.print("Collector has ");
		                int how_many = collector.howManyThings();
		                System.out.println(how_many + " kind(s) of things: ");
		                System.out.print("  ");
		                for (int j = 0; j < how_many; j++) {  
		                	ThingType t = collector.whatIs(j);
		                	int how_many_of_this = collector.howManyOf(t);
		                	System.out.print(how_many_of_this + " " + describe(t));
		                	if (how_many_of_this > 1) {
		                		System.out.print("s");
		                	}
		                	if (j < how_many - 1) {
		                		System.out.print(", ");
		                	}
		                	if (j == (how_many -2)) {
		                		System.out.print("and ");
		                	}
		                }
		                System.out.println(" ");
		            } 
		            else {
		            	System.out.println("Collector has nothing.");
		            }
		        }
		        break;

		        default:
		        	System.out.println(choice + " is not a valid choice.  Try again.");
		        break;
		    }
		}
	}
}