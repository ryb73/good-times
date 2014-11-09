import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;

import edu.uwm.cs351.util.AbstractEntry;
import edu.uwm.cs351.util.TreeMap;


class Driver {
	private static int passed;
	private static int failed;
	
	private static class DefaultComparator<T extends Comparable<T>> implements Comparator<T> {
		public int compare(T o1, T o2) {
			return o1.compareTo(o2);
		}	
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			testTreeMap();
			new Driver().dotests();
		} catch (Exception e) {
			++failed;
			e.printStackTrace();
		}
		System.out.println("Passed " + passed + " tests.");
		System.out.println("Failed " + failed + " test" + (failed == 1? "." : "s."));
	}

	private static void testTreeMap() {
		System.out.println("Testing TreeMap");
		
		System.out.println("Simple tests:");
		testSimple("Tree",new TreeMap<Integer,String>(new DefaultComparator<Integer>()),10,50,100,"Hello","Bye","Foo","New");
		if (failed != 0) {
			System.out.println("Skipping remaining tests (1)");
			return;
		} 
		
		testNull("Tree",new TreeMap<Integer,String>(new DefaultComparator<Integer>()),10,50,100,"Hello","Bye","Foo","New");

		testSimple("Tree",new TreeMap<String,Integer>(new DefaultComparator<String>()),"Bye","Foo","Hello",10,50,100,-4);
		if (failed != 0) {
			// System.out.println("Strange: must have type errors");
			System.out.println("Skipping remaining tests (2)");
			return;
		} 
		
		System.out.println("Size tests:");
		testSize("Tree",new TreeMap<Integer,String>(new DefaultComparator<Integer>()),10,50,100,"Hello","Bye","Foo","New");
		if (failed != 0) {
			System.out.println("Skipping remaining tests (3)");
			return;
		} 
		
		System.out.println("Remove tests:");
		testRemove("Tree",new TreeMap<Integer,String>(new DefaultComparator<Integer>()),10,50,100,"Hello","Bye","Foo","New");
		if (failed != 0) {
			System.out.println("Skipping remaining tests (4)");
			return;
		} 
		

		System.out.println("BIG tests:");
		Map<Integer, String> dict = makeBigTree();
		
		for (int k = 0; k < 140; k += 10) {
			if (k != 0) test(dict.get(k),""+k,"BIG.get("+k+")#1");
			test(dict.get(k+5),null,"BIG.get("+(k+5)+")#1");
		}
		test(dict,"BIG",10,20,30,40,50,60,70,80,90,100,110,120,130);
		for (int k = 0; k < 140; k += 10) {
			if (k != 0) test(dict.get(k),""+k,"BIG.get("+k+")#2");
			test(dict.get(k+5),null,"BIG.get("+(k+5)+")#2");
		}

		/// several individual removals
		
		keepByNumbers(dict,"0111111111111");
		test(dict,"BIG-10",20,30,40,50,60,70,80,90,100,110,120,130);

		if (failed != 0) {
			System.out.println("Skipping remaining tests (5)");
			return;
		} 
		dict = makeBigTree();
		keepByNumbers(dict,"1101111111111");
		test(dict,"BIG-30",10,20,40,50,60,70,80,90,100,110,120,130);

		dict = makeBigTree();
		keepByNumbers(dict,"1111011111111");
		test(dict,"BIG-50",10,20,30,40,60,70,80,90,100,110,120,130);
		
		dict = makeBigTree();
		keepByNumbers(dict,"1111101111111");
		test(dict,"BIG-60",10,20,30,40,50,70,80,90,100,110,120,130);
		
		dict = makeBigTree();
		keepByNumbers(dict,"1111111110111");
		test(dict,"BIG-100",10,20,30,40,50,60,70,80,90,110,120,130);
		
		dict = makeBigTree();
		keepByNumbers(dict,"1111111111110");
		test(dict,"BIG-130",10,20,30,40,50,60,70,80,90,100,110,120);

		if (failed != 0) {
			System.out.println("Skipping remaining tests (6)");
			return;
		} 
		
		// large removals at a time:
		dict = makeBigTree();
		keepByNumbers(dict,"0101010101010");
		test(dict,"BIG-ODD",20,40,60,80,100,120);
		
		dict = makeBigTree();
		keepByNumbers(dict,"1010101010101");
		test(dict,"BIG-EVEN",10,30,50,70,90,110,130);
		
		dict = makeBigTree();
		keepByNumbers(dict,"0000000001111");
		test(dict,"BIG-SMALL",100,110,120,130);
		
		dict = makeBigTree();
		keepByNumbers(dict,"1111111110000");
		test(dict,"BIG-LARGE",10,20,30,40,50,60,70,80,90);
	}

	private static <K,V> void testSimple(String prefix,Map<K,V> d, K k1, K k2, K k3, V v1, V v2, V v3, V v4) {
		String name = prefix + "{}";
		d.clear();
		test(d.isEmpty(), name + " should be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),null, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		
		test(d.put(k1, v1),null, name + ".put(" + k1 + "," + v1 + ")"); 
		name = prefix + "{" + k1 + "->" + v1 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v1, name + ".get(" + k1 + ")");
		test(d.get(k2),null, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		
		test(d.put(k2, v2),null, name + ".put(" + k2 + "," + v2 + ")"); 
		name = prefix + "{" + k1 + "->" + v1 + "," + k2 + "->" + v2 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v1, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		
		test(d.put(k3, v3),null, name + ".put(" + k3 + "," + v3 + ")"); 
		name = prefix + "{" + k1 + "->" + v1 + "," + k2 + "->" + v2 + "," + k3 + "->" + v3 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v1, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),v3, name + ".get(" + k3 + ")");
		
		test(d.put(k1, v4),v1,name + ".put(" + k1 + "," + v4 + ")"); 
		name = prefix + "{" + k1 + "-> NOT(" + v1 + ") " + v4 + "," + k2 + "->" + v2 + "," + k3 + "->" + v3 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v4, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),v3, name + ".get(" + k3 + ")");
		
		d.clear(); name = prefix + "{} again";
		test(d.isEmpty(), name + " should be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),null, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		
		d.put(k3, v3); name = prefix + "{" + k3 + "->" + v3 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),null, name + ".get(" + k2 + ")");
		test(d.get(k3),v3, name + ".get(" + k3 + ")");
		
		d.put(k1, v1); name = prefix + "{" + k3 + "->" + v3 +  "," + k1 + "->" + v1 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v1, name + ".get(" + k1 + ")");
		test(d.get(k2),null, name + ".get(" + k2 + ")");
		test(d.get(k3),v3, name + ".get(" + k3 + ")");
		
		d.put(k2, v2); name = prefix + "{" + k3 + "->" + v3 + "," + k1 + "->" + v1 + "," + k2 + "->" + v2 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v1, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),v3, name + ".get(" + k3 + ")");
		
		test(d.put(k2, v4),v2,name+".put(" + k2 + "," + v4 + ")");
		name = prefix + "{" + k1 + "->" + v1 + "," + k2 + "-> NOT(" + v2 + ")" + v4 +  "," + k3 + "->" + v3 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v1, name + ".get(" + k1 + ")");
		test(d.get(k2),v4, name + ".get(" + k2 + ")");
		test(d.get(k3),v3, name + ".get(" + k3 + ")");

		d.clear(); name = prefix + "{} #3";
		test(d.isEmpty(), name + " should be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),null, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		
		d.put(k2, v2); name = prefix + "{" +k2 + "->" + v2 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		
		d.put(k1, v1); name = prefix + "{" + k2 + "->" + v2 + "," + k1 + "->" + v1 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v1, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		
		d.put(k3, v3); name = prefix + "{" + k2 + "->" + v2 +  "," + k1 + "->" + v1 + "," + k3 + "->" + v3 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v1, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),v3, name + ".get(" + k3 + ")");	
	}
	
	private static <K,V> void testSize(String prefix,Map<K,V> d, K k1, K k2, K k3, V v1, V v2, V v3, V v4) {
		String name = prefix + "{}";
		d.clear();
		test(d.size(),0,name + ".size()");
		d.put(k1, v1); 
		test(d.size(),1,prefix + "{" + k1 + "->" + v1 + "}.size()");
		d.put(k2, v2); 
		test(d.size(),2,prefix + "{" + k1 + "->" + v1 + k2 + "->" + v2 + "}.size()");
		d.put(k3, v3); name = prefix + "{" + k1 + "->" + v1 + "," + k2 + "->" + v2 + "," + k3 + "->" + v3 + "}";
		test(d.size(),3,name + ".size()");
		d.put(k2, v4); name = prefix + "{" + k1 + "->" + v1 + "," + k2 + "->NOT(" + v2 + ")" + v4 + "," + k3 + "->" + v3 + "}";
		test(d.size(),3,name + ".size()");
		d.put(k1, null); name = prefix + "{" + k1 + "->NOT(" + v1 + ")null," + k2 + "->NOT(" + v2 + ")" + v4 + "," + k3 + "->" + v3 + "}";
		test(d.size(),3,name + ".size()");
	}
	
	private static <K,V> void testRemove(String prefix,Map<K,V> d, K k1, K k2, K k3, V v1, V v2, V v3, V v4) {
		String name = prefix + "{}";
		d.clear();
		d.put(k1, v1);
		d.put(k2, v2); 
		d.put(k3, v3); name = prefix + "{" + k1 + "->" + v1 + "," + k2 + "->" + v2 + "," + k3 + "->" + v3 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v1, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),v3, name + ".get(" + k3 + ")");
		
		test(d.remove(d),null,name + "should not remove self");
		test(d.remove(v1),null,name + "should not remove value");
		
		test(d.remove(k1),v1,name+".remove(" + k1 + ")"); 
		name = prefix + "{" + k1 + "-/>," + k2 + "->" + v2 + "," + k3 + "->" + v3 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),v3, name + ".get(" + k3 + ")");
		test(d.size(),2,name + ".size()");
		
		test(d.remove(k1),null,name+".remove(" + k1 + ")"); 
		test(d.remove(k3),v3,name+".remove(" + k3 + ")");
		name = prefix + "{" + k1 + "-/>," + k2 + "->" + v2 + "," + k3 + "/->" + v3 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		test(d.size(),1,name + ".size()");
		
		test(d.remove(k1),null,name+".remove(" + k1 + ")"); 
		test(d.remove(k3),null,name+".remove(" + k3 + ")");
		test(d.remove(k2),v2,name+".remove(" + k2 + ")");
		name = prefix + "{" + k1 + "-/>," + k2 + "-/>," + k3 + "-/>}";
		test(d.isEmpty(), name + " should be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),null, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		test(d.size(),0,name + ".size()");	
		
		test(d.remove(k1),null,name+".remove(" + k1 + ")"); 
		test(d.remove(k3),null,name+".remove(" + k3 + ")");
		test(d.remove(k2),null,name+".remove(" + k2 + ")");
	}
	
	private static <K,V> void testNull(String prefix,Map<K,V> d, K k1, K k2, K k3, V v1, V v2, V v3, V v4) {
		String name = prefix + "{}";
		d.clear();
		d.put(k1, v1);
		d.put(k2, v2); 
		d.put(k3, v3); name = prefix + "{" + k1 + "->" + v1 + "," + k2 + "->" + v2 + "," + k3 + "->" + v3 + "}";		
		
		test(d.get(null),null,name + ".get(null)");
		test(d.remove(null),null,name + ".remove(null)");
		test(!d.containsKey(null),name + " should not contain null");
		
		try {
			d.put(null,v4);
			test(false,name + ".put(null,v) should not succeed");
		} catch (IllegalArgumentException ex) {
			// OK!
		}
		
	}
	
	private static Map<Integer, String> makeBigTree() {
		Map<Integer, String> dict = new TreeMap<Integer,String>(new DefaultComparator<Integer>());
		// (((1)2(((3(4))5)6((7(8))9)))10((11)12(13)))
		dict.put(100,"100");
		dict.put(20,"20");
		dict.put(10,"10");
		dict.put(60,"60");
		dict.put(50,"50");
		dict.put(30,"30");
		dict.put(40,"40");
		dict.put(90, "90");
		dict.put(70, "70");
		dict.put(80,"80");
		dict.put(120,"120");
		dict.put(110,"110");
		dict.put(130,"130");
		return dict;
	}
	
	private static <K,V> Entry<K,V> e(final K k, final V v) {
		return new AbstractEntry<K,V>(k,v);
	}
	
	@SuppressWarnings("unchecked")
	private static void test(Map<Integer, String> dict, String name, Integer... keys) {
		Entry<Integer,String>[] entries = new Entry[keys.length];
		for (int i=0; i < keys.length; ++i) {
			entries[i] = e(keys[i],""+keys[i]);
		}
		test(dict,name,entries);
	}
	
	private static <K,V> void test(Map<K, V> dict, String name, Entry<K,V>... entries) {
		int i=0;
		for (Entry<K,V> e : dict.entrySet()) {
			if (i < entries.length) {
				test(e,entries[i],name + "[" + i + "]");
			}
			++i;
		}
		test (i, entries.length,name + " iteration length");
	}
	
	private static <K,V> void keepByNumbers(Map<K,V> dict, String s) {
		int index = -1;
		for (Iterator<Entry<K,V>> it = dict.entrySet().iterator(); it.hasNext();) {
			// Entry<K,V> e  = 
			it.next(); // ignored!
			if (++index < s.length() && s.charAt(index) == '0') it.remove();
		}
	}

	
	/// This is a set of tests from a previous semester.
	
	Comparator<Integer> ascending = new Comparator<Integer>() {
		public int compare(Integer o1, Integer o2) {
			return o1 - o2;
		}
	};

	Comparator<Integer> descending = new Comparator<Integer>() {
		public int compare(Integer o1, Integer o2) {
			return o2 - o1;
		}
	};

	Comparator<Integer> lexicographic = new Comparator<Integer>() {
		public int compare(Integer o1, Integer o2) {
			return o1.toString().compareTo(o2.toString());
		}
	};
	
	Comparator<String> asciibetical = new Comparator<String>() {
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
	};

	public void dotests() {
		Map<Integer,String> s = new TreeMap<Integer,String>(descending);
		HashMap<Integer,String> m = new HashMap<Integer,String>();
		
		Integer[] tests = new Integer[]{ -99, 10, -1, 0, 1, 2, 3, 4, 5, 9, 6, -10, 11, 20, 55, 99, 100, -10000};
		
		System.out.println("Initial tests");
		// initial test
		test(s,m,tests,"{}");
		if (failed > 0) return;
		
		// some test of add
		testAdd(8,"eight",s,m,tests,"{8}");
		if (failed > 0) return;
		
		testAdd(4,"four",s,m,tests,"{4,8}");
		if (failed > 0) return;
		
		testAdd(10,"ten",s,m,tests,"{4,8,10}");
		if (failed > 0) return;
		
		testAdd(6,"six",s,m,tests,"{4,6,8,10}");
		if (failed > 0) return;
		
		testAdd(5,"five",s,m,tests,"{4,5,6,8,10}");
		if (failed > 0) return;
		
		testAdd(2,"two",s,m,tests,"{2,4,5,6,8,10}");
		if (failed > 0) return;
		
		System.out.println("testing add redundantly");
		testAdd(8,"eight",s,m,tests,"unchanged#1");
		testAdd(2,"two",s,m,tests,"unchanged#2");
		testAdd(10,"ten",s,m,tests,"unchanged#3");
		testAdd(6,"six",s,m,tests,"unchanged#4");
		
		System.out.println("testing put changes");
		testAdd(8,"huit",s,m,tests,"French 8");
		testAdd(6,"sechs",s,m,tests,"German 6");
		testAdd(2,"dos",s,m,tests,"Spanish 2");
		
		System.out.println("test clear");
		s.clear(); m.clear();
		test(s,m,tests,"cleared");
		s.clear();
		test(s,m,tests,"cleared again");
		
		System.out.println("Building a large map");
		s = makeBigMap(m);
		
		System.out.println("Testing remove");
		s.remove(1); m.remove(1);
		test(s,m,tests,"remove(1)");
		
		s = makeBigMap(m); s.remove(3); m.remove(3);
		test(s,m,tests,"remove(3)");
		
		s = makeBigMap(m); s.remove(5); m.remove(5);
		test(s,m,tests,"remove(5)");
		
		s = makeBigMap(m); s.remove(13); m.remove(13);
		test(s,m,tests,"remove(13)");
		
		s = makeBigMap(m); s.remove(2); m.remove(2);
		test(s,m,tests,"remove(2)");
		
		s = makeBigMap(m); s.remove(6); m.remove(6);
		test(s,m,tests,"remove(6)");
		
		s = makeBigMap(m); s.remove(10); m.remove(10);
		test(s,m,tests,"remove(10)");
		
		
		// testing removal of things that are NOT in the set:
		
		s = makeBigMap(m); s.remove(0);
		test(s,m,tests,"remove(0)");
		
		s = makeBigMap(m); s.remove(20);
		test(s,m,tests,"remove(20)");
		
		// in order to get a space in the middle, I have to first remove something:
		s = makeBigMap(m); s.remove(8); m.remove(8);
		test(s,m,tests,"remove(8)");
		
		s.remove(8); // again!
		test(s,m,tests,"remove(8) again");
		
		
		// now testing removal in a small tree:
		
		s = new TreeMap<Integer,String>(ascending); m.clear();
		s.remove(0);
		test(s,m,tests,"{} - 0");
		
		s = new TreeMap<Integer,String>(ascending);
		s.put(0,"0");
		s.remove(0);
		test(s,m,tests,"{0}-0");
		
		s = new TreeMap<Integer,String>(ascending); m.clear();
		s.put(10,"10"); s.put(0,"0");
		s.remove(10); m.put(0,"0");
		test(s,m,tests,"{0,10}-10");
		
		s = new TreeMap<Integer,String>(ascending); m.clear();
		s.put(10,"ten"); s.put(0,"zero");
		s.remove(0); m.put(10,"ten");
		test(s,m,tests,"{0,10}-0");
		
		s = new TreeMap<Integer,String>(ascending); m.clear();
		s.put(10,"shi"); s.put(20,"ershi");
		s.remove(10); m.put(20,"ershi");
		test(s,m,tests,"{10,20}-10");
		
		s = new TreeMap<Integer,String>(ascending); m.clear();
		s.put(10,"zehn"); s.put(20,"zwanzig");
		s.remove(20); m.put(10,"zehn");
		test(s,m,tests,"{10,20}-20");
		
		
		
		
		/// extra tests
		
		
		if (failed > 0) {
			System.out.println("Giving up.  Failed normal tests");
			return;
		}
		System.out.println("Extra tests");
		
		
		// return values
		
		// empty set
		s = new TreeMap<Integer,String>(ascending);
		test(s.remove(10) == null,"remove should return null if empty");
		test(s.put(6,"six") == null,"put should return null if added to empty");
		test("six".equals(s.remove(6)),"remove should return old value");
		
		// large set
		s = makeBigMap(m);
		test("arabic(6)".equals(s.put(6, "liu")),"put should return old value if already in");
		test("liu".equals(s.remove(6)),"remove should return old if removed");
		test(s.remove(6) == null,"remove should return null if NOT removed");
		test(s.put(6,"6") == null,"put should return null if added");
		
		
		// wrong type
		
		// empty
		s = new TreeMap<Integer,String>(ascending);
		try {
			test(!s.containsKey("Hello"), "empty set of integers should not contain a string!");
		} catch (RuntimeException e) {
			test(false,"checking for a string in {} caused exception " + e);
		}
		try {
			test(s.remove("Hello") == null,"empty set of integers cannot have a string removed from it");
		} catch (RuntimeException e) {
			test(false,"removing a string from {} caused exception " + e);
		}
		
		// big
		s = makeBigMap(m);
		try {
			test(!s.containsKey("Hello"), "large set of integers should not contain a string!");
		} catch (RuntimeException e) {
			test(false,"checking for a string in a  large set caused exception " + e);
		}
		try {
			test(s.remove("Hello") == null,"large set of integers cannot have a string removed from it");
		} catch (RuntimeException e) {
			test(false,"removing a string from a large set caused exception " + e);
		}
		
		// null
		
		// empty
		s = new TreeMap<Integer,String>(ascending);
		try {
			test(!s.containsKey(null), "empty set of integers should not contain null!");
		} catch (RuntimeException e) {
			test(false,"checking for null in {} still causes exception " + e);
		}
		try {
			test(s.remove(null) == null,"empty set of integers cannot have null removed from it");
		} catch (RuntimeException e) {
			test(false,"removing null from {} still causes exception " + e);
		}
		
		// big
		s = makeBigMap(m);
		try {
			test(!s.containsKey(null), "large set of integers should not contain null!");
		} catch (RuntimeException e) {
			test(false,"checking for null in a large set still causes exception " + e);
		}
		try {
			test(s.remove(null) == null,"large set of integers cannot have null removed from it");
		} catch (RuntimeException e) {
			test(false,"removing null from a large set still causes exception " + e);
		}
		
		// checking entry set removal
		s = makeBigMap(m);
		test(!s.entrySet().remove(new AbstractEntry<Integer,String>(2,"2")),"should not remove with wrong value");
		test(s,m,tests,"big.remove(wrong 2)");
		test(s.entrySet().remove(new AbstractEntry<Integer,String>(2,"arabic(2)")), "should remove with correct entry");
		

		/// Iterators tests
		
		System.out.println("Iterator tests");
		
		s = new TreeMap<Integer,String>(ascending);
		Iterator<Integer> it= s.keySet().iterator();
		test(!it.hasNext(),"empty set iterator should not have next element");
		try {
			Integer x = it.next();
			test(false,"empty set next() returns " + x);
		} catch (NoSuchElementException ex){
			test(true,"internal error");
		} catch (RuntimeException ex) {
			test(false,"empty set next() threw wrong exception: " + ex);
		}
		
		s = makeBigMap(m);
		it = s.keySet().iterator();
		int i;
		for (i=1; i <14; ++i) {
			if (it.hasNext()) {
				try {
					Integer x = it.next();
					if (x == null) {
						test(false,"next() return a null Integer instead of " + i);
					} else {
						test(x,i,"it.next()");
					}
				} catch (NoSuchElementException ex){
					test(false,"hasNext() was true but next() failed after " + (i-1) + " next calls.");
					break;
				} catch (RuntimeException ex) {
					ex.printStackTrace();
					break;
				}
			} else {
				test(it.hasNext(),"iterator claims to be done after " + (i-1) + " (of 13) next() calls");
				break;
			}
		}
		if (i == 14) {
			test(!it.hasNext(),"After 13 next() calls, iterator should not have next element");
			try {
				Integer x = it.next();
				test(false,"it.next() should fail, but returns " + x);
			} catch (NoSuchElementException ex){
				test(true,"internal error");
			} catch (RuntimeException ex) {
				test(false,"After 13 next() calls, next() threw wrong exception: " + ex);
			}
		}
		
		
		
		/// remove with iterator tests
		if (failed > 0) {
			System.out.println("Skipping remove iterator tests");
			return;
		}
		System.out.println("Remove using iterators");
		
		removeWithIterator(s.keySet(),1); m.remove(1);
		test(s,m,tests,"iterator remove(1)");
		
		s = makeBigMap(m); removeWithIterator(s.keySet(),3); m.remove(3);
		test(s,m,tests,"iterator remove(3)");
		
		s = makeBigMap(m); removeWithIterator(s.keySet(),5); m.remove(5);
		test(s,m,tests,"iterator remove(5)");
		
		s = makeBigMap(m); removeWithIterator(s.keySet(),13); m.remove(13);
		test(s,m,tests,"iterator remove(13)");
		
		s = makeBigMap(m); removeWithIterator(s.keySet(),2); m.remove(2);
		test(s,m,tests,"iterator remove(2)");
		
		s = makeBigMap(m); removeWithIterator(s.keySet(),6); m.remove(6);
		test(s,m,tests,"iterator remove(6)");
		
		s = makeBigMap(m); removeWithIterator(s.keySet(),10); m.remove(10);
		test(s,m,tests,"iterator remove(10)");

		// now remove everything!
		s = makeBigMap(m);
		it = s.keySet().iterator();
		
		test(it.next(),1,"removing iteratively"); it.remove(); m.remove(1);
		test(s,m,tests,"remove everything to 1");
		
		test(it.next(),2,"removing iteratively"); it.remove(); m.remove(2);
		test(s,m,tests,"remove everything to 2");
		
		test(it.next(),3,"removing iteratively"); it.remove(); m.remove(3);
		test(s,m,tests,"remove everything to 3");
		
		test(it.next(),4,"removing iteratively"); it.remove(); m.remove(4);
		test(s,m,tests,"remove everything to 4");
		
		test(it.next(),5,"removing iteratively"); it.remove(); m.remove(5);
		test(s,m,tests,"remove everything to 5");
		
		test(it.next(),6,"removing iteratively"); it.remove(); m.remove(6);
		test(s,m,tests,"remove everything to 6");
		
		test(it.next(),7,"removing iteratively"); it.remove(); m.remove(7);
		test(s,m,tests,"remove everything to 7");
		
		test(it.next(),8,"removing iteratively"); it.remove(); m.remove(8);
		test(s,m,tests,"remove everything to 8");
		
		test(it.next(),9,"removing iteratively"); it.remove(); m.remove(9);
		test(s,m,tests,"remove everything to 9");
		
		test(it.next(),10,"removing iteratively"); it.remove(); m.remove(10);
		test(s,m,tests,"remove everything to 10");
		
		test(it.next(),11,"removing iteratively"); it.remove(); m.remove(11);
		test(s,m,tests,"remove everything to 11");
		
		test(it.next(),12,"removing iteratively"); it.remove(); m.remove(12);
		test(s,m,tests,"remove everything to 12");
		
		test(it.next(),13,"removing iteratively"); it.remove(); m.remove(13);
		test(s,m,tests,"remove everything to 13");
		
		
		// same as before, but this time, we leave 1 around the whole time.
		
		s = makeBigMap(m);
		it = s.keySet().iterator();
		test(it.next(),1,"removing iteratively except 1");
		
		test(it.next(),2,"removing iteratively"); it.remove(); m.remove(2);
		test(s,m,tests,"remove all but 1 to 2");
		
		test(it.next(),3,"removing iteratively except 1"); it.remove(); m.remove(3);
		test(s,m,tests,"remove all but 1 to 3");
		
		test(it.next(),4,"removing iteratively except 1"); it.remove(); m.remove(4);
		test(s,m,tests,"remove all but 1 to 4");
		
		test(it.next(),5,"removing iteratively except 1"); it.remove(); m.remove(5);
		test(s,m,tests,"remove all but 1 to 5");
		
		test(it.next(),6,"removing iteratively except 1"); it.remove(); m.remove(6);
		test(s,m,tests,"remove all but 1 to 6");
		
		test(it.next(),7,"removing iteratively except 1"); it.remove(); m.remove(7);
		test(s,m,tests,"remove all but 1 to 7");
		
		test(it.next(),8,"removing iteratively except 1"); it.remove(); m.remove(8);
		test(s,m,tests,"remove all but 1 to 8");
		
		test(it.next(),9,"removing iteratively except 1"); it.remove(); m.remove(9);
		test(s,m,tests,"remove all but 1 to 9");
		
		test(it.next(),10,"removing iteratively except 1"); it.remove(); m.remove(10);
		test(s,m,tests,"remove all but 1 to 10");
		
		test(it.next(),11,"removing iteratively except 1"); it.remove(); m.remove(11);
		test(s,m,tests,"remove all but 1 to 11");
		
		test(it.next(),12,"removing iteratively except 1"); it.remove(); m.remove(12);
		test(s,m,tests,"remove all but 1 to 12");
		
		test(it.next(),13,"removing iteratively except 1"); it.remove(); m.remove(13);
		test(s,m,tests,"remove all but 1 to 13");
		
		System.out.println("Testing contains/remove on entry set");
		for (int t : new int[]{1,3,5,13,2,6,10}) {
			s = makeBigMap(m);
			test(!s.entrySet().contains(t),"should NOT locate entry using key");
			test(!s.entrySet().remove(t),"should not be able to remove using key");
			s = makeBigMap(m);
			test(!s.entrySet().contains(new AbstractEntry<Integer,String>(t,""+t)),"located entry with wrong value!");
			test(!s.entrySet().remove(new AbstractEntry<Integer,String>(t,""+t)),"removed with wrong value!");
			s = makeBigMap(m);
			test(s.entrySet().contains(new AbstractEntry<Integer,String>(t,"arabic("+t+")")),"didn't locate entry with right value");
			test(s.entrySet().remove(new AbstractEntry<Integer,String>(t,"arabic(" + t + ")")),"didn't remove with right value");
			s = makeBigMap(m);
			try {
				test(!s.entrySet().contains(new AbstractEntry<Integer,String>(t,null)),"located entry with null value!");
				test(!s.entrySet().remove(new AbstractEntry<Integer,String>(t,null)),"removed with bad null!");
				s.put(t, null);
				test(s.entrySet().contains(new AbstractEntry<Integer,String>(t,null)),"didn't located entry with correct null value!");
				test(s.entrySet().remove(new AbstractEntry<Integer,String>(t,null)),"didn't remove with correct null value");
			} catch (RuntimeException e) {
				test(false,"putting null and/or removing null should not throw exception");
			}
		}
	}
	
	private void removeWithIterator(Set<Integer> s, int n) {
		Iterator<Integer> it = s.iterator();
		int i = 0;
		String note = "(before removal)";
		for (i=1; i < 14; ++i) {
			if (it.hasNext()) {
				try {
					Integer x = it.next();
					if (x == null) {
						test(false,note + " it.next() return a null Integer instead of " + i);
					} else {
						test(x,i,"it.next() " + note);
					}
				} catch (NoSuchElementException ex){
					test(false,note + " hasNext() was true but next() failed after " + (i-1) + " next calls.");
					break;
				} catch (RuntimeException ex) {
					ex.printStackTrace();
					break;
				}
			} else {
				test(it.hasNext(),note + " iterator claims to be done after " + (i-1) + " (of 13) next() calls");
				break;
			}
			if (i == n) {
				it.remove();
				note = "(after removal)";
			}
		}
	}
	
	private Map<Integer,String> makeBigMap(HashMap<Integer,String> mirror) {
		Map<Integer,String> result = new TreeMap<Integer,String>(ascending);
		mirror.clear();
		for (Integer i : new Integer[]{ 10, 2, 6, 9, 1, 5, 7, 12, 3, 4, 7, 8, 11, 13}) {
			String s = "arabic(" + i + ")";
			result.put(i,s); mirror.put(i,s);
		}
		return result;
	}
	
	private <X,Y> void testAdd(X x, Y y, Map<X,Y> s, HashMap<X,Y> mirror, X[] tests, String name) {
		s.put(x,y);
		mirror.put(x,y);
		test(s,mirror,tests,name);
	}
	
	private <X,Y> void test(Map<X,Y> s, HashMap<X,Y> mirror, X[] tests, String name) {
		if (mirror.isEmpty()) {
			test(s.isEmpty(),name+".isEmpty()");
		} else {
			test(!s.isEmpty(),"!"+name+".isEmpty()");
		}
		
		test(s.size(),mirror.size(),name +".size()");
		
		for (X x : tests) {
			if (mirror.containsKey(x)) {
				test(s.containsKey(x),name + ".contains(" + x + ")");
				test(s.get(x) + "", mirror.get(x) + "", name + ".get(" + x + ")");
			} else {
				test(!s.containsKey(x),"!" + name + ".contains(" + x + ")");
				test(s.get(x) + "", "null", name + ".get(" + x + ")");
			}
		}
		
	}

	///
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
