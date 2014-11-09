package edu.uwm.cs351;

import java.util.Random;

public class Colony {
	
	private static class Cell 
    {
		Cell before, after; 
		int age;
	    boolean healthy;
        int order;
        
		public Cell(Cell b, Cell a) {
        	before = b;
        	after = a;
        	age = 0;
        	healthy = true;
            order = ++count;
        }
    }
	
	private Cell first; // entry to the list
    private int diedOld, diedSick;

    private int reproRate, // rate of reproduction 
    			sickRate,  // rate of contamination
    			epiLevel,  // lethality level
    			deathAge;  // age of dying
    
    private final Random generator = new Random();
    private static int count = 0;
	
	public Colony(int size, int rRate, int sRate, int l, int da) {
		first = null;
		reproRate = rRate > 0 ? rRate : 1;
		sickRate = sRate > 0 ? sRate : 1;
		epiLevel = l < sickRate ? l : sickRate/4;
		deathAge = da > 0? da : 1;
		
		// populate Colony
		addOne(first);
		
		Cell index = first;
		for (int i = 1; i < size; i++) {
			addOne(index);
		    // randomize age of initial cell population
		    index.age = generator.nextInt(Integer.MAX_VALUE) % deathAge; 
		    index = index.after;
		}
		diedOld = diedSick = 0; // begin epidemy statistics
	}

	public int size() {   
		int number = 0;
		for (Cell current = first; current != null; current = current.after) {
			++number;
		}
		return number;
	}

	public void age() {   
		for (Cell current = first; current != null; current = current.after) {
			current.age++;
		}
	}

	public void lifeSignal() {   
		for (Cell current = first; current != null; current = current.after) {
			if(current.healthy && action(reproRate)) {  
				addOne(current);
				if(current.after != null) {
					current = current.after;
				}
			}
	    }
	}

	public void deathSignal() {
		
		for (Cell current = first; current != null; current = current.after) {
			if(current.age >= deathAge) {  
				die(current);
				diedOld++;
			}
			else if (current.age == 0);
	
			else if	(!current.healthy && action(epiLevel)) {  
				die(current);
	        	diedSick++;
			}
	
			else if (current.healthy && action(sickRate)) {  
				mark(current);
	        	Cell right = current.after;
	        	Cell left  = current.before;
	        	int chance = 2;
	        	for(; (left != null && right != null) && chance < Integer.MAX_VALUE/2; chance *= 2) {  
	        		if(action(chance)) {
	        			mark(left); 
	        		}
	        		if(action(chance)) {
	        			mark(right);
	        		}
	        		if(left != null) {
	        			left  = left.before; 
	        		}
	        		if(right != null) {
	        			right = right.after;
	        		}
	        	}
			}
		}
	}

	public void print() {   
		System.out.println('\n');
		int i = 0;
		for (Cell current = first; current != null; current = current.after) {   
			if(i < 8) {
				i++;
			}
	     	else {  
	     		System.out.print('\n'); 
	     		i = 1; 
	     	}
	     	System.out.print((i == 1 ? "[": "\t[") + current.order + ":" + current.age + (current.healthy? "" : "+") + "]");
		}
		System.out.println('\n');
	}

	public void reportStatistics() {   
		System.out.println("\t\tCurrent Population \t" + size());
		System.out.println("\t\tDead From Old Age  \t" + diedOld);
		System.out.println("\t\tDead From Decease  \t" + diedSick);  
	}

	public void printReverse (Cell theone) {
		int i = 0;
		for(Cell current = theone; current != null; current = current.before) {   
			if(i < 8) {
				i++;
			}
	     	else {  
	     		System.out.println(); 
	     		i = 1; 
	     	}
	     	System.out.print((i == 1 ? "[": "\t[") + current.order + ":" + current.age + (current.healthy? "" : "+") + "]");
		}
		System.out.println('\n');
	}
	
	private void mark(Cell theone) {  
		if(theone != null) {
			theone.healthy = false;
		}
	}

	private boolean action(int chance) {  
		//a random number between 0 and chance-1
		if (chance != 0) {
			return (generator.nextInt(Integer.MAX_VALUE) % chance) == 0;  
		}
		else {
			return true;
		}
	} 
	
	private void addOne(Cell theone) {  
		Cell current;
		/* insert into empty list */
		if (first == null) {
			first = new Cell(null, null);
		}
		/* insert at the end */
		else if(theone.after == null) {
			current = new Cell(theone, null);
		    current.before.after = current;
		}    
		
		/* no case for inserting in the beginning since 
		 * a new Cell is always added to the right 
		 */
		/* insert in the middle */
		else {    
			current = new Cell(theone, theone.after);
		    current.before.after = current;
		    current.after.before = current;
		}
	}

	private void die(Cell theone) {
		
		if (first ==null || theone == null) {
			throw new IllegalArgumentException("No cell to die!");
		}
		
		//UNCOMMENT THE FOLLOWING LINE FOR DEBUGGING
		System.out.println("Deleting [" + theone.order + "] ...");
		
		/* delete from the beginning */
		if (theone == first) {   
			if(size() > 1) {  
				first = first.after;
		        first.before = null;
		    }
		    else {
		    	first = null;
		    }
		} 
		/* delete from the middle or the end */
		else {
			if(theone.after != null)
				theone.after.before = theone.before;
			theone.before.after = theone.after;
		} 
		//UNCOMMENT THE FOLLOWING LINE FOR DEBUGGING
		printReverse(theone.after); // check that delete worked
	}

}
