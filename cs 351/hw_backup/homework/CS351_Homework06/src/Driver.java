import java.util.Comparator;

import edu.uwm.cs351.Nondiscrimination;
import edu.uwm.cs351.RewardSeeker;
import edu.uwm.cs351.Task;
import edu.uwm.cs351.TaskList;
import edu.uwm.cs351.TyrannyOfTheUrgent;
import edu.uwm.cs351.ValueHunter;

public class Driver {

	private static int passed;
	private static int failed;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			testComparators();
			testAddInPriority();
			testRemove();
			testPerformAll();
		} catch (Exception e) {
			++failed;
			e.printStackTrace();
		}
		System.out.println("Passed " + passed + " tests.");
		System.out.println("Failed " + failed + " test" + (failed == 1? "." : "s."));
	}
	
	private static void testComparators() {
		System.out.println("Testing nondiscrimination comparator");
		Comparator<Task> n = Nondiscrimination.getInstance();
		Task taskn1 = new Task("Task1", 1, 100, 10);
		Task taskn2 = new Task("Task2", 2, 200, 20);
		Task taskn3 = new Task("Task3", 1, 100, 10);
		test(n.compare(taskn1, taskn2), 0, "nondiscrimination test (1)");
		test(n.compare(taskn2, taskn1), 0, "nondiscrimination test (2)");
		test(n.compare(taskn1, taskn3), 0, "nondiscrimination test (3)");
		
		System.out.println("Testing rewardseeker comparator");
		Comparator<Task> r = RewardSeeker.getInstance();
		Task taskr1 = new Task("Task1", 1, 100, 10);
		Task taskr2 = new Task("Task2", 2, 50, 20);
		Task taskr3 = new Task("Task3", 1, 300, 30);
		test(r.compare(taskr2, taskr1), 1, "rewardseeker test (1)");
		test(r.compare(taskr1, taskr2), -1, "rewardseeker test (2)");
		test(r.compare(taskr1, taskr3), 0, "rewardseeker test (3)");
		test(r.compare(taskr3, taskr1), 0, "rewardseeker test (4)");
		
		System.out.println("Testing tyrannyOfTheUrgent comparator");
		Comparator<Task> t = TyrannyOfTheUrgent.getInstance();
		Task taskt1 = new Task("Task1", 1, 100, 10);
		Task taskt2 = new Task("Task2", 2, 200, 20);
		Task taskt3 = new Task("Task3", 3, 100, 30);
		Task taskt4 = new Task("Task4", 1, 100, 30);
		Task taskt5 = new Task("Task5", 4, 200, 50);
		test(t.compare(taskt1, taskt2), 1, "tyrannyOfTheUrgent test (1)");
		test(t.compare(taskt2, taskt1), -1, "tyrannyOfTheUrgent test (2)");
		test(t.compare(taskt3, taskt1), 1, "tyrannyOfTheUrgent test (3)");
		test(t.compare(taskt2, taskt5), -1, "tyrannyOfTheUrgent test (4)");
		test(t.compare(taskt1, taskt4), 0, "tyrannyOfTheUrgent test (5)");
		test(t.compare(taskt4, taskt1), 0, "tyrannyOfTheUrgent test (6)");
		
		System.out.println("Testing valueHunter comparator");
		Comparator<Task> v = ValueHunter.getInstance();
		Task taskv1 = new Task("Task1", 1, 100, 10);
		Task taskv2 = new Task("Task2", 2, 200, 10);
		Task taskv3 = new Task("Task3", 1, 100, 5);
		Task taskv4 = new Task("Task4", 2, 100, 5);
		Task taskv5 = new Task("Task5", 3, 200, 15);
		test(v.compare(taskv2, taskv1), 1, "valueHunter test (1)");
		test(v.compare(taskv1, taskv3), -1, "valueHunter test (2)");
		test(v.compare(taskv4, taskv1), 1, "valueHunter test (3)");
		test(v.compare(taskv2, taskv3), -1, "valueHunter test (4)");
		test(v.compare(taskv5, taskv2), 0, "valueHunter test (5)");
		test(v.compare(taskv2, taskv5), 0, "valueHunter test (6)");
	}
	
	private static void testAddInPriority() {
		System.out.println("Testing addInPriority()");
		
		//test using nondiscrimination
		Comparator<Task> c = Nondiscrimination.getInstance();
		Task tn1 = new Task("Taskn1", 5, 500, 50);
		Task tn2 = new Task("Taskn2", 2, 200, 20);
		Task tn3 = new Task("Taskn3", 8, 800, 80);
		Task tn4 = new Task("Taskn4", 5, 500, 50);
		
		tn1.addInPriority(tn2, c);
		test(tn1, "addInPriority (n1)", tn1,tn2);
		
		tn1.addInPriority(tn3, c);
		test(tn1, "addInPriority (n2)", tn1,tn2,tn3);
		
		tn1.addInPriority(tn4, c);
		test(tn1, "addInPriority (n3)", tn1,tn2,tn3,tn4);
		
		//test using rewardseeker
		c = RewardSeeker.getInstance();
		Task tr1 = new Task("Taskr1", 5, 500, 50);
		Task tr2 = new Task("Taskr2", 2, 200, 20);
		Task tr3 = new Task("Taskr3", 8, 800, 80);
		Task tr4 = new Task("Taskr4", 5, 100, 10);
		Task tr5 = new Task("Taskr5", 6, 900, 90);
		
		tr1.addInPriority(tr2, c);
		test(tr1, "addInPriority (r1)", tr1,tr2);
		
		tr1.addInPriority(tr3, c);
		test(tr1, "addInPriority (r2)", tr3,tr1,tr2);
		
		tr1.addInPriority(tr4, c);
		test(tr1, "addInPriority (r3)", tr3,tr1,tr4,tr2);
		
		tr1.addInPriority(tr5, c);
		test(tr1, "addInPriority (r4)", tr3,tr5,tr1,tr4,tr2);
		
		//test using tyrannyOfTheUrgent
		c = TyrannyOfTheUrgent.getInstance();
		Task tt1 = new Task("Taskt1", 5, 500, 50);
		Task tt2 = new Task("Taskt2", 2, 200, 20);
		Task tt3 = new Task("Taskt3", 3, 300, 30);
		Task tt4 = new Task("Taskt4", 3, 500, 10);
		Task tt5 = new Task("Taskt5", 4, 500, 90);
		
		
		tt1.addInPriority(tt2, c);
		test(tt1, "addInPriority (t1)", tt2,tt1);
		
		tt1.addInPriority(tt3, c);
		test(tt1, "addInPriority (t2)", tt2,tt3,tt1);
		
		tt1.addInPriority(tt4, c);
		test(tt1, "addInPriority (t3)", tt2,tt3,tt1,tt4);
		
		tt1.addInPriority(tt5, c);
		test(tt1, "addInPriority (t4)", tt2,tt3,tt1,tt5,tt4);
		
		//test using valueHunter
		c = ValueHunter.getInstance();
		Task tv1 = new Task("Taskv1", 5, 500, 50);
		Task tv2 = new Task("Taskv2", 4, 500, 10);
		Task tv3 = new Task("Taskv3", 3, 500, 10);
		Task tv4 = new Task("Taskv4", 2, 600, 20);
		Task tv5 = new Task("Taskv5", 2, 500, 20);
		
		tv1.addInPriority(tv2, c);
		test(tv1, "addInPriority (v1)", tv2,tv1);
		
		tv1.addInPriority(tv3, c);
		test(tv1, "addInPriority (v2)", tv2,tv3,tv1);

		tv1.addInPriority(tv4, c);
		test(tv1, "addInPriority (v3)", tv2,tv3,tv1,tv4);
		
		tv1.addInPriority(tv5, c);
		test(tv1, "addInPriority (v4)", tv2,tv3,tv1,tv5,tv4);
	}
	
	private static void testRemove() {
		System.out.println("Testing remove()");
		
		Comparator<Task> c = Nondiscrimination.getInstance();
		Task t1 = new Task("Task1", 1, 100, 10);
		Task t2 = new Task("Task2", 2, 200, 20);
		Task t3 = new Task("Task3", 3, 300, 30);
		Task t4 = new Task("Task4", 4, 400, 40);
		Task t5 = new Task("Task5", 5, 500, 50);
		
		t1.addInPriority(t2, c);
		t1.addInPriority(t3, c);
		t1.addInPriority(t4, c);
		t1.addInPriority(t5, c);
		
		t3.remove();
		test(t3, "removed middle node no longer references other nodes", t3);
		test(t1, "removed middle node no longer in list", t1,t2,t4,t5);
		
		t5.remove();
		test(t5, "removed last node no longer references other nodes", t5);
		test(t1, "removed last node no longer in list", t1,t2,t4);
		
		t1.remove();
		test(t1, "removed first node no longer references other nodes", t1);
		test(t2, "removed first node no longer in list", t2,t4);
		
		t1.remove();
		test(t1, "removed node not present in a list", t1);
	}
	
	private static void testPerformAll() {
		System.out.println("Testing performAll()");
		
		Comparator<Task> c = Nondiscrimination.getInstance();
		TaskList tl = new TaskList(c);
		
		Task t1 = new Task("Task1", 1, 100, 100);
		Task t2 = new Task("Task2", 2, 300, 200);
		Task t3 = new Task("Task3", 3, 600, 300);
		
		Task t4 = new Task("Task4", 4, 1000, 900);
		Task t5 = new Task("Task5", 5, 1500, 800);
		Task t6 = new Task("Task6", 6, 1600, 700);
		
		test(tl, 1500, 0, "performAll, no tasks to complete", (Task)null);
		
		tl.add(t1);
		test(tl, 100, 1, "performAll, single task should complete", t1);
		
		tl.add(t1);
		tl.add(t2);
		tl.add(t3);
		test(tl, 1000, 6, "performAll, all tasks should complete", t1,t2,t3);
		
		tl.add(t1);
		tl.add(t2);
		tl.add(t3);
		test(tl, 600, 6, "performAll, all tasks should complete (2)", t1,t2,t3);
		
		tl.add(t1);
		tl.add(t2);
		tl.add(t3);
		test(tl, 500, 3, "performAll, last task shouldn't complete", t1,t2,t3);
		
		tl.add(t4);
		tl.add(t6);
		tl.add(t5);
		test(tl, 3000, 10, "performAll, last task shouldn't complete (2)", t4,t6,t5);
		
		tl.add(t6);
		tl.add(t5);
		tl.add(t4);
		test(tl, 3000, 11, "performAll, last task shouldn't complete (3)", t6,t5,t4);
		
		tl.add(t4);
		tl.add(t5);
		tl.add(t6);
		test(tl, 800, 5, "performAll, only second task should complete", t4,t5,t6);
		
		tl.add(t4);
		tl.add(t5);
		tl.add(t6);
		test(tl, 3000, 10, "performAll, only second task shouldn't complete", t4,t5,t6);
	}
	
	private static void test(int produced, int expected, String name) {
		if(expected == 0 && produced == 0) {
			++passed;
		} else if(expected == -1 && produced < 0) {
			++passed;
		} else if(expected == 1 && produced > 0) {
			++passed;
		} else {
			++failed;
			String expectedStr = "0";
			if(expected > 0) {
				expectedStr = "positive value";
			} else if(expected < 0) {
				expectedStr = "negative value";
			}
			System.out.println("\n!!! Failed test: " + name + ".  Expected " + expectedStr + ", but got " + produced + "\n");
		}
	}
	
	private static void test(Task t, String name, Task... expected) {
		Task tCopy = t;
		//check all Tasks starting from first, following next links
		if(t != null) {
			while(t.getPrevious() != null) {
				t = t.getPrevious();
			}
		}
		int i = 0;
		while(t != null && i < expected.length) {
			test(expected[i], t, name + "(" + i + ") using next links");
			t = t.getNext();
			++i;
		}
		
		if(i < expected.length) {
			++failed;
			System.out.println("\n!!! Failed test: " + name + "(" + i + ") using next links.  Expected " + expected[i] + ", but got end of tasks\n");
		} else if(t != null) {
			++failed;
			System.out.println("\n!!! Failed test: " + name + "(" + i + ") using next links.  Expected end of tasks, but got " + t + "\n");
		} else {
			++passed;
		}
		
		//check all Tasks starting from last, following previous links
		t = tCopy;
		if(t != null) {
			while(t.getNext() != null) {
				t = t.getNext();
			}
		}
		int j = expected.length-1;
		while(t != null && j >= 0) {
			test(expected[j], t, name + "(" + j + ") using previous links");
			t = t.getPrevious();
			--j;
		}
		
		if(j >= 0) {
			++failed;
			System.out.println("\n!!! Failed test: " + name + "(" + j + ") using previous links.  Expected " + expected[j] + ", but got end of tasks\n");
		} else if(t != null) {
			++failed;
			System.out.println("\n!!! Failed test: " + name + "(" + j + ") using previous links.  Expected end of tasks, but got " + t + "\n");
		} else {
			++passed;
		}
	}
	
	private static void test(Task t, Task expected, String name) {
		if(!expected.toString().equals(t.toString())) {
			++failed;
			System.out.println("\n!!! Failed test: " + name + ".  Expected " + expected + ", but got " + t + "\n");
		} else {
			++passed;
		}
	}
	
	private static void test(TaskList tl, int timeLimit, int expectedPrioritySum, String name, Task... expected) {
		int prioritySum = tl.performAll(timeLimit);
		
		if(prioritySum != expectedPrioritySum) {
			++failed;
			System.out.println("\n!!! Failed test: return value of " + name + ".  Expected " + expectedPrioritySum + ", but got " + prioritySum + "\n");
		} else {
			++passed;
		}
		
		//ensure all nodes have been removed
		if(expected[0] != null) {
			for(int i=0; i<expected.length; ++i) {
				test(expected[i],"node not removed via " + name + " (" + i + ")", expected[i]);
			}
		}
	}
}
