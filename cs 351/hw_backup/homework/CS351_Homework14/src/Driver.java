import java.util.Comparator;

import edu.uwm.cs351.WordList;


public class Driver {

	private static int passed, failed;
	
	/**
	 * Test the word list program.
	 * @param args (ignored)
	 */
	public static void main(String[] args) {
		try {
			(new Driver()).dotests();
		} catch (Exception e) {
			++failed;
			e.printStackTrace();
		}
		System.out.println("Passed " + passed + " tests.");
		System.out.println("Failed " + failed + " test" + (failed == 1? "." : "s."));
	}
	
	private static Comparator<String> asciibetical = new Comparator<String>() {
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
	};
	
	private static Comparator<String> alphabetical = new Comparator<String>() {
		public int compare(String o1, String o2) {
			return o1.compareToIgnoreCase(o2);
		}
	};
	
	private void dotests() {
		WordList w = new WordList();

		System.out.println("Testing add");
		testWL("[]",w);
		w.add("One");
		testWL("1",w,"One");
		w.add("Two");
		testWL("12",w,"One", "Two");
		w.add("three");
		testWL("123",w,"One","Two","three");
		w.add("Four");
		testWL("1234",w,"One","Two","three","Four");
		w.add("5ive");
		testWL("12345",w,"One","Two","three","Four", "5ive");
		
		if (failed > 0) {
			System.out.println("Skipping remaining tests");
			return;
		}
		
		System.out.println("Testing sort");
		w.sort(alphabetical);
		testWL("54132",w,"5ive","Four","One","three","Two");
		
		w.sort(asciibetical);
		testWL("54123",w,"5ive","Four","One","Two","three");
		
		w = new WordList();
		w.sort(alphabetical);
		testWL("[]",w);
		w.add("hi");
		w.sort(alphabetical);
		testWL("hi",w,"hi");
		
		w = new WordList();
		w.add("1");
		w.add("one");
		w.add("One");
		w.sort(asciibetical);
		testWL("111",w,"1","One","one");

		w.sort(alphabetical); // should remove the duplicate!
		testWL("11",w,"1","One");
		
		if (failed > 0) {
			System.out.println("Skipping remaining tests");
			return;
		}
		
		System.out.println("Testing merge");
		
		w = new WordList();
		w.add("A");
		w.add("D");
		w.add("E");
		w.add("F");
		w.add("B");
		w.add("C");
		
		WordList w2 = new WordList();
		w2.add("fire");
		w2.add("fumes");
		w2.add("farther");
		w2.add("a");
		w2.add("clock");
		w2.add("d");
		
		w.merge(alphabetical, w2);
		testWL("merged",w,"A","B","C","clock","D","E","F","farther","fire","fumes");
		testWL("errors",w2,"clock","farther","fire","fumes");
	
		w2.clear();
		testWL("cleared",w2);
		
		w2.add("bumpers");
		w2.add("3");
		w2.add("b");
		w2.add("baby");
		w2.add("buggy");
		w2.add("BABY");
		w2.add("C");
		w.merge(alphabetical, w2);
		testWL("merged2",w,"3","A","B","baby","buggy","bumpers","C","clock","D","E","F","farther","fire","fumes");
		testWL("errors2",w2,"3","baby","buggy","bumpers");
		
		w.clear();
		w2.clear();
		w2.add("wiggly");
		w2.add("woo");
		w2.add("iggly");
		w.merge(alphabetical,w2);
		testWL("copied",w,"iggly","wiggly","woo");
		testWL("errors3",w2,"iggly","wiggly","woo");
	}
	
	private static void testWL(String name, WordList wl, String... compare) {
		test(wl.size(),compare.length,name + ".size()");
		int i=0;
		for (String w : wl) {
			if (i < compare.length) {
			test(w,compare[i],name + "[" + i + "]");
			} else {
				test(false, "extra word " + w);
			}
			++i;
		}
		for (i=wl.size(); i < compare.length; ++i) {
			test(false,"missing word " + compare[i]);
		}
	}
	
	private static <T> void test(T x, T expected, String name) {
		if (x == expected || (x != null && x.equals(expected))) ++passed;
		else {
			++failed;
			System.out.println("\n!!! Failed test: " + name + ".  Expected " + expected + ", but got " + x + "\n");
		}
	}
	
	private static boolean test(boolean b, String problem) {
		if (b) {
			++passed;
		} else {
			++failed;
			System.out.println("\n!!! Failed test: " + problem);
		}
		return b;
	}

}
