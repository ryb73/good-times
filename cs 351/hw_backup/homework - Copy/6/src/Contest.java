import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import edu.uwm.cs351.Nondiscrimination;
import edu.uwm.cs351.RewardSeeker;
import edu.uwm.cs351.Smart;
import edu.uwm.cs351.Task;
import edu.uwm.cs351.TaskList;
import edu.uwm.cs351.TyrannyOfTheUrgent;
import edu.uwm.cs351.ValueHunter;

class Contest {
   public static void main(String[] args) {
	   new Contest().run();
   }
   
   private List<Task> _tasks = new ArrayList<Task>();
   private Random _random = new Random();
   private List<Comparator<Task>> _priorities = new ArrayList<Comparator<Task>>();
   
   private static int LIMIT = Task.MAX_VALUE;
   
   public void run() {
	   int sum = 0;
	   System.out.println("Tasks to try our contestants with: ");
	   while (sum < LIMIT) {
		   Task t = makeRandomTask();
		   _tasks.add(t);
		   sum += t.getSize();
		   System.out.println("  " + t);
	   }
	   _priorities.add(Nondiscrimination.getInstance());
	   _priorities.add(TyrannyOfTheUrgent.getInstance());
	   _priorities.add(RewardSeeker.getInstance());
	   _priorities.add(ValueHunter.getInstance());
	   _priorities.add(Smart.getInstance());
	   
	   Object winner = null;
	   int maximum = -1;
	   for (Comparator<Task> priority : _priorities) {
		   System.out.println("Contestant: " + priority);
		   System.out.print("Loading up the tasks: ");
		   TaskList tl = new TaskList(priority);
		   for (Task t : _tasks) {
			   tl.add(t);
		   }
		   System.out.println("done.");
		   System.out.print("Performing tasks: ");
		   int result = tl.performAll(LIMIT);
		   System.out.println("reward = " + result);
		   if (result > maximum) {
			   maximum = result;
			   winner = priority;
		   }
	   }
	   System.out.println();
	   System.out.print("And the winner with a score of " + maximum + " is ");
	   try {
		   Thread.sleep(2000);
	   } catch (InterruptedException e) {
		// muffle
	   }
	   System.out.println(winner);
   }
   
   private int generateExponentialValue(int median) {
	   double base = -Math.log(_random.nextDouble())*median/Math.log(2);
	   if (base > Task.MAX_VALUE) return Task.MAX_VALUE;
	   return (int)base;
   }
   
   private static int _unique = 0;
   
   private static final int MEDIAN_IMPORTANCE = 100;
   private static final int MEDIAN_SIZE = 25;
   
   public Task makeRandomTask() {
	   return new Task("#"+ ++_unique,
			           generateExponentialValue(MEDIAN_IMPORTANCE),
			           _random.nextInt(LIMIT),
			           generateExponentialValue(MEDIAN_SIZE));
   }
}
