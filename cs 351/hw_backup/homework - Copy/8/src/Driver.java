
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uwm.cs351.util.LinkedList;


public class Driver {

	private static int passed;
	private static int failed;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		  testList();
		  
		} catch (Exception e) {
			++failed;
			e.printStackTrace();
		}
		System.out.println("Passed " + passed + " tests.");
		System.out.println("Failed " + failed + " test" + (failed == 1? "." : "s."));
	}

	private static void testList()
	{
		Collection<String> l = new LinkedList<String>();
		String p1 = "Apple";
		String p2 = "Bread";
		String p3 = "Cheese";

		test(l,"empty");

		try {
			l.iterator().next();
			test(false,"empty list iterator has _next");
		} catch (NoSuchElementException e) {
			test(true,"");
		} catch (RuntimeException e) {
			test(false,"wrong exception: " + e);
		}

		l.add(p1);
		test(l.iterator().next(),p1,"{p1}.first");
		test(l,"{p1}",p1);

		Iterator<String> it = l.iterator();
		it.next(); // result discarded

		try {
			it.next();
			test(false,"advanced too far");
		} catch (NoSuchElementException e) {
			test(true,"");
		} catch (RuntimeException e) {
			test(false,"wrong exception: " + e);
		}

		it = l.iterator();
		test(it.next(),p1,"again {p1}.first");

		it.remove();
		test(l,"again empty");

	  l.add(p2);
	  test(l.iterator().next(),p2,"{p2}.first");
	  test(l,"{p2}",p2);
	  
	  l.clear();
	  test(l,"cleared");
	  
	  l.add(p1);
	  l.add(p2);
	  it = l.iterator();
	  test(it.next(),p1,"{p1,p2}.first");
	  test(l,"{p1,p2}",p1,p2);

	  test(it.next(),p2,"{p1,p2}.second");

	  test(!it.hasNext(),"{p1,p2} should have nothing at end");
	  l.add(p3);
	  test(l,"{p1,p2,p3}",p1,p2,p3);
	  
	  it = l.iterator();
	  it.next(); // ignored
	  test(it.next(),p2,"{p1,p2,p3}.second");
	  
	  it.remove();
	  test(l,"after removing middle",p1,p3);
	  
	  l.add(p2);
	  test(l,"re-adding p2",p1,p3,p2);
	  
	  it = l.iterator();
	  it.next();
	  it.next();
	  it.next();
	  it.remove();
	  test(l,"after removing p2 again",p1,p3);
	  test(!it.hasNext(),"should not have remaining element after remove()");
	  
	  it = l.iterator();
	  try {
		  it.remove();
		  test(false,"remove() called before _next()");
	  } catch (IllegalStateException e) {
		  test(true,"");
	  } catch (RuntimeException e) {
		  test(false,"wrong exception " + e);
	  }
	  
	  it = l.iterator();
	  it.next();
	  it.remove();
	  
	  test(l,"after removing first",p3);
	  test(it.hasNext(),"should have remaining element after remove()");
	  try {
		  it.remove();
		  test(false,"remove() called after already removed");
	  } catch (IllegalStateException e) {
		  test(true,"");
	  } catch (RuntimeException e) {
		  test(false,"wrong exception " + e);
	  }
  
	  it = l.iterator();
	  it.next();
	  it.remove();
	  test(!it.hasNext(),"should not have remaining element after remove()");
	  test(l,"last element removed");
	}
	
	private static void test(Collection<String> l, String name, String... parts)
	{
		test(l.size(),parts.length,name + ".size()");
		Iterator<String> it = l.iterator();
		int i=0;
		while (it.hasNext() && i < parts.length) {
			test(it.next(),parts[i],name + "[" + i + "]");
			++i;
		}
		test(!it.hasNext(),name + " too long");
		test(!(i < parts.length),name + " too short");
	}

	private static void test(String x, String expected, String name) {
		if (x.equals(expected)) ++passed;
		else {
			++failed;
			System.out.println("\n!!! Failed test: " + name + ".  Expected \"" + expected + "\", but got \"" + x + "\"\n");
		}
	}

	private static void test(int x, int expected, String name) {
		if (x == expected) ++passed;
		else {
			++failed;
			System.out.println("\n!!! Failed test: " + name + ".  Expected " + expected + ", but got " + x + "\n");
		}
	}
	private static void test(boolean b, String problem) {
		if (b) {
			++passed;
		} else {
			++failed;
			System.out.println("\n!!! Failed test: " + problem);
		}
	}
}
