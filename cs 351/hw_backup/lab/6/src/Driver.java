import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.uwm.cs351.Colony;

public class Driver {
	
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int rr = 0, sr = 0, da = 0, ps = 0, fl = 0, time = 0;
		char act;
		boolean invalidEntry;
		      
		while(true) {
			invalidEntry = false;
		    System.out.println("Please enter the following INTEGER values:");
		    System.out.print("\tInitial Population Size: "); 
		    try {
			    ps = Integer.parseInt(in.readLine());
			    System.out.print("\tRate of reproduction: ");    
			    rr = Integer.parseInt(in.readLine());
			    System.out.print("\tEpidemy rate: ");            
			    sr = Integer.parseInt(in.readLine());
			    System.out.print("\tFatality level: ");          
			    fl = Integer.parseInt(in.readLine());
			    System.out.print("\tDeath Age: ");               
			    da = Integer.parseInt(in.readLine());
			    System.out.print("\tTime period: ");            
			    time = Integer.parseInt(in.readLine());
		    }
		    catch(NumberFormatException e) {
		    	System.out.println("Invalid entry... let's try this again...");
		    	invalidEntry = true;
		    }
		    catch(IOException e) {
		    	e.printStackTrace();
		    }
		    System.out.println('\n');
		    if(!invalidEntry) {
			    System.out.println("\t\t*----------------------*");
			    System.out.println("\t\t  *** STATISTICS ***       ");
			    System.out.println("\t\t*----------------------*");
			    System.out.println("\t\tInitial Population Size " + ps);
			    System.out.println("\t\tRate of reproduction    " + rr);
			    System.out.println("\t\tEpidemy rate            " + sr);
			    System.out.println("\t\tFatality level          " + fl);
			    System.out.println("\t\tDeath Age               " + da);
			    System.out.println();
			    
			    Colony cells = new Colony(ps, rr, sr, fl, da);
			    cells.print();
			    for (int year = 1; year <= time; year++) {
			    	System.out.println("\t\t        YEAR " + year);
			        cells.age();
			        cells.lifeSignal();
	
			        //UNCOMMENT BELOW
			        cells.deathSignal();
	
			        cells.reportStatistics();
			        System.out.print("show list(s) continue(c) start over(o) exit(e)? "); 
			        try {
			        	act = in.readLine().charAt(0);
			        	System.out.println();
			        	if (act == 's' || act == 'S') {
			        		cells.print();
			        	}
			        	else if(act == 'o' || act == 'O') {
			        		year = time+1;
			        	}
			        	else if(act == 'e' || act == 'E') {
			        		return;
			        	}
			        }
			        catch(IOException e) {
			        	e.printStackTrace();
			        }
			    }
			    System.out.println('\n');
		    }
		}
	}

}
