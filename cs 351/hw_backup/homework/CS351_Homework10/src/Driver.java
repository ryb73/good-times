import java.util.Comparator;

import edu.uwm.cs351.util.ArrayDictionary;
import edu.uwm.cs351.util.Dictionary;
import edu.uwm.cs351.util.Entry;
import edu.uwm.cs351.util.EntryPredicate;
import edu.uwm.cs351.util.TreeDictionary;

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
			System.out.println("Testing ArrayDictionary");
			testSimple("Array",new ArrayDictionary<Integer,String>(),10,50,100,"Hello","Bye","Foo","New");
			testNull("Array",new ArrayDictionary<Integer,String>(),10,50,100,"Hello","Bye","Foo","New");
			testSize("Array",new ArrayDictionary<Integer,String>(),10,50,100,"Hello","Bye","Foo","New");
			testRemove("Array",new ArrayDictionary<Integer,String>(),10,50,100,"Hello","Bye","Foo","New");
			testTreeDictionary();
		} catch (Exception e) {
			++failed;
			e.printStackTrace();
		}
		System.out.println("Passed " + passed + " tests.");
		System.out.println("Failed " + failed + " test" + (failed == 1? "." : "s."));
	}

	private static void testTreeDictionary() {
		System.out.println("Testing TreeDictionary");
		
		System.out.println("Simple tests:");
		testSimple("Tree",new TreeDictionary<Integer,String>(new DefaultComparator<Integer>()),10,50,100,"Hello","Bye","Foo","New");
		if (failed != 0) {
			System.out.println("Skipping remaining tests (1)");
			return;
		} 
		
		testNull("Tree",new TreeDictionary<Integer,String>(new DefaultComparator<Integer>()),10,50,100,"Hello","Bye","Foo","New");

		testSimple("Tree",new TreeDictionary<String,Integer>(new DefaultComparator<String>()),"Bye","Foo","Hello",10,50,100,-4);
		if (failed != 0) {
			// System.out.println("Strange: must have type errors");
			System.out.println("Skipping remaining tests (2)");
			return;
		} 
		
		System.out.println("Size tests:");
		testSize("Tree",new TreeDictionary<Integer,String>(new DefaultComparator<Integer>()),10,50,100,"Hello","Bye","Foo","New");
		if (failed != 0) {
			System.out.println("Skipping remaining tests (3)");
			return;
		} 
		
		System.out.println("Remove tests:");
		testRemove("Tree",new TreeDictionary<Integer,String>(new DefaultComparator<Integer>()),10,50,100,"Hello","Bye","Foo","New");
		if (failed != 0) {
			System.out.println("Skipping remaining tests (4)");
			return;
		} 
		

		System.out.println("BIG tests:");
		Dictionary<Integer, String> dict = makeBigTree();
		
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

	private static <K,V> void testSimple(String prefix,Dictionary<K,V> d, K k1, K k2, K k3, V v1, V v2, V v3, V v4) {
		String name = prefix + "{}";
		d.clear();
		test(d.isEmpty(), name + " should be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),null, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		
		d.put(k1, v1); name = prefix + "{" + k1 + "->" + v1 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v1, name + ".get(" + k1 + ")");
		test(d.get(k2),null, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		
		d.put(k2, v2); name = prefix + "{" + k1 + "->" + v1 + "," + k2 + "->" + v2 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v1, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		
		d.put(k3, v3); name = prefix + "{" + k1 + "->" + v1 + "," + k2 + "->" + v2 + "," + k3 + "->" + v3 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v1, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),v3, name + ".get(" + k3 + ")");
		
		d.put(k1, v4); name = prefix + "{" + k1 + "-> NOT(" + v1 + ") " + v4 + "," + k2 + "->" + v2 + "," + k3 + "->" + v3 + "}";
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
		
		d.put(k2, v4); name = prefix + "{" + k1 + "->" + v1 + "," + k2 + "-> NOT(" + v2 + ")" + v4 +  "," + k3 + "->" + v3 + "}";
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
	
	private static <K,V> void testSize(String prefix,Dictionary<K,V> d, K k1, K k2, K k3, V v1, V v2, V v3, V v4) {
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
	
	private static <K,V> void testRemove(String prefix,Dictionary<K,V> d, K k1, K k2, K k3, V v1, V v2, V v3, V v4) {
		String name = prefix + "{}";
		d.clear();
		d.put(k1, v1);
		d.put(k2, v2); 
		d.put(k3, v3); name = prefix + "{" + k1 + "->" + v1 + "," + k2 + "->" + v2 + "," + k3 + "->" + v3 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v1, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),v3, name + ".get(" + k3 + ")");
		
		keepByNumbers(d,"011"); name = prefix + "{" + k1 + "-/>," + k2 + "->" + v2 + "," + k3 + "->" + v3 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),v3, name + ".get(" + k3 + ")");
		test(d.size(),2,name + ".size()");
		
		keepByNumbers(d,"00"); name = prefix + "{" + k1 + "-/>," + k2 + "-/>," + k3 + "-/>}";
		test(d.isEmpty(), name + " should be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),null, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		test(d.size(),0,name + ".size()");
		
		d.clear();
		d.put(k1, v1);
		d.put(k2, v2); 
		d.put(k3, v3); 
		keepByNumbers(d,"010"); name = prefix + "{" + k1 + "-/>," + k2 + "->" + v2 + "," + k3 + "-/>}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		test(d.size(),1,name + ".size()");		
	}
	
	private static <K,V> void testNull(String prefix,Dictionary<K,V> d, K k1, K k2, K k3, V v1, V v2, V v3, V v4) {
		String name = prefix + "{}";
		d.clear();
		d.put(k1, v1);
		d.put(k2, v2); 
		d.put(k3, v3); name = prefix + "{" + k1 + "->" + v1 + "," + k2 + "->" + v2 + "," + k3 + "->" + v3 + "}";		
		
		test(d.get(null),null,name + ".get(null)");
		
		try {
			d.put(null,v4);
			test(false,name + ".put(null,v) should not succeed");
		} catch (IllegalArgumentException ex) {
			// OK!
		}
		
		try {
			d.keepIf(null);
			test(false,name + ".keepIf(null) should not succeed");
		} catch (IllegalArgumentException ex) {
			// OK!
		}
	}
	
	private static Dictionary<Integer, String> makeBigTree() {
		Dictionary<Integer, String> dict = new TreeDictionary<Integer,String>(new DefaultComparator<Integer>());
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
		return new Entry<K,V>(k,v);
	}
	
	
	private static class TestEntries<K,V> implements EntryPredicate<K,V> {

		private final String name;
		private int index = 0;
		private final Entry<K,V>[] entries;
		
		public TestEntries(String n, Entry<K,V>... es) {
			name = n;
			entries = es;
		}
		
		public boolean keep(Entry<K,V> e) {
			if (index < entries.length) {
				test(e.getKey(),entries[index].getKey(),name + ".key #" + index );
				test(e.getValue(),entries[index].getValue(),name + ".value #" + index);
			}
			++index;
			return true;
		}
		
		public void checkDone() {
			test(index == entries.length, name + " wong size (" + index + "): expected " + entries.length);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void test(Dictionary<Integer, String> dict, String name, Integer... keys) {
		Entry<Integer,String>[] entries = new Entry[keys.length];
		for (int i=0; i < keys.length; ++i) {
			entries[i] = e(keys[i],""+keys[i]);
		}
		test(dict,name,entries);
	}
	
	private static <K,V> void test(Dictionary<K, V> dict, String name, Entry<K,V>... entries) {
		TestEntries<K,V> pred = new TestEntries<K,V>(name,entries);
		dict.keepIf(pred);
		pred.checkDone();
	}
	
	private static class KeepByNumbers<K,V> implements EntryPredicate<K,V> {

		private int index = -1;
		private final String numbers;
		public KeepByNumbers(String s) {
			numbers = s;
		}
		
		public boolean keep(Entry<K,V> e) {
			return (++index < numbers.length() && numbers.charAt(index) != '0');
		}
		
	}
	private static <K,V> void keepByNumbers(Dictionary<K,V> dict, String s) {
		dict.keepIf(new KeepByNumbers<K,V>(s));
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
