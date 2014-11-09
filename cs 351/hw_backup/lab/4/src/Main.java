import java.util.Iterator;

import edu.uwm.cs351.util.ArrayList;
import edu.uwm.cs351.util.List;

public class Main {
	public static void main(String args[]) {
		
		List<String> my_courses;
		
		/*
		 * initialize my_classes as an ArrayList below
		 */
		my_courses = new ArrayList<String>();



		/*
		 * add the name of each class you're taking to "my_classes" below
		 */
		my_courses.add("Philos 211");
		my_courses.add("Atm Sci 100");
		my_courses.add("CS 351");
		my_courses.add("CS 361");
		my_courses.add("CS 317");


		System.out.println("The " + my_courses.size() + " classes I'm enrolled in are: ");
	
		for (String s : my_courses) {
			/*
			 * print the name of each class by iterating through the list below
			 */
			System.out.println(s);


		}
		
		System.out.println("All of my classes besides Data Structures and Algorithms are: ");
		
		for (Iterator<String> it = my_courses.iterator(); it.hasNext();) {
			/*
			 * print the name of each class other than Data Structures and Algorithms
			 * by iterating through the list below
			 */
			String className = it.next();
			if(!className.equals("CS 351"))
				System.out.println(className);

			my_courses.add("hi");

		}
	}
}
