import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;

import edu.uwm.cs351.util.DoubleHashMap;
import edu.uwm.cs351.util.MyEntry;


class Driver {
	private static int passed;
	private static int failed;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			testHashMap();
			(new Driver()).dotests();
		} catch (Exception e) {
			++failed;
			e.printStackTrace();
		}
		System.out.println("Passed " + passed + " tests.");
		System.out.println("Failed " + failed + " test" + (failed == 1? "." : "s."));
	}

	private static void testHashMap() {
		System.out.println("Testing DoubleHashMap");
		DoubleHashMap.debugString = true; // change way results are represented
		
		System.out.println("Simple tests:");
		testSimple("DoubleHash",new DoubleHashMap<Integer,String>(),10,50,38,"Hello","Bye","Foo","New");
		if (failed != 0) {
			System.out.println("Skipping remaining tests (1)");
			return;
		} 
		
		testNull("DoubleHash",new DoubleHashMap<Integer,String>(),10,50,38,"Hello","Bye","Foo","New");

		testSimple("DoubleHash",new DoubleHashMap<String,Integer>(),"Bye","Foo","Hello",10,50,100,-4);
		if (failed != 0) {
			// System.out.println("Strange: must have type errors");
			System.out.println("Skipping remaining tests (2)");
			return;
		} 
		
		System.out.println("Size tests:");
		testSize("DoubleHash",new DoubleHashMap<Integer,String>(),10,50,38,"Hello","Bye","Foo","New");
		if (failed != 0) {
			System.out.println("Skipping remaining tests (3)");
			return;
		} 
		
		System.out.println("Remove tests:");
		testRemove("DoubleHash",new DoubleHashMap<Integer,String>(),10,50,38,"Hello","Bye","Foo","New");
		if (failed != 0) {
			System.out.println("Skipping remaining tests (4)");
			return;
		} 
		
		System.out.println("Rehash tests:");
		testRehash();

	}

	private static <K,V> void testSimple(String prefix,Map<K,V> d, K k1, K k2, K k3, V v1, V v2, V v3, V v4) {
		String name = prefix + "{}";
		d.clear();
		test(d.isEmpty(), name + " should be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),null, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		test(d.toString(),"[,,,,,,]",name + " should be empty");
		
		test(d.put(k1, v1),null, name + ".put(" + k1 + "," + v1 + ")"); 
		name = prefix + "{" + k1 + "->" + v1 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v1, name + ".get(" + k1 + ")");
		test(d.get(k2),null, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		if (k1.equals(10) && v1.equals("Hello")) {
			test(d.toString(),"[,,,10=Hello,,,]",name + " misorganized");
		}
		
		test(d.put(k2, v2),null, name + ".put(" + k2 + "," + v2 + ")"); 
		name = prefix + "{" + k1 + "->" + v1 + "," + k2 + "->" + v2 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v1, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		if (k1.equals(10)) {
			test(d.toString(),"[,50=Bye,,10=Hello,,,]",name + " misorganized");
		}
		
		test(d.put(k3, v3),null, name + ".put(" + k3 + "," + v3 + ")"); 
		name = prefix + "{" + k1 + "->" + v1 + "," + k2 + "->" + v2 + "," + k3 + "->" + v3 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v1, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),v3, name + ".get(" + k3 + ")");
		if (k1.equals(10)) {
			test(d.toString(),"[38=Foo,50=Bye,,10=Hello,,,]",name + " misorganized");
		}
		
		test(d.put(k1, v4),v1,name + ".put(" + k1 + "," + v4 + ")"); 
		name = prefix + "{" + k1 + "-> NOT(" + v1 + ") " + v4 + "," + k2 + "->" + v2 + "," + k3 + "->" + v3 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),v4, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),v3, name + ".get(" + k3 + ")");
		if (k1.equals(10)) {
			test(d.toString(),"[38=Foo,50=Bye,,10=New,,,]",name + " misorganized");
		}
		
		d.clear(); name = prefix + "{} again";
		test(d.isEmpty(), name + " should be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),null, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		test(d.toString(),"[,,,,,,]",name + " should be empty");
		
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
		if (k1.equals(10)) {
			test(d.toString(),"[38=Foo,50=Bye,,10=Hello,,,]",name + " misorganized");
		}
		
		test(d.remove(d),null,name + "should not remove self");
		test(d.remove(v1),null,name + "should not remove value");
		
		test(d.remove(k1),v1,name+".remove(" + k1 + ")"); 
		name = prefix + "{" + k1 + "-/>," + k2 + "->" + v2 + "," + k3 + "->" + v3 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),v3, name + ".get(" + k3 + ")");
		test(d.size(),2,name + ".size()");
		if (k1.equals(10)) {
			test(d.toString(),"[38=Foo,50=Bye,,X,,,]",name + " misorganized");
		}
		
		test(d.remove(k1),null,name+".remove(" + k1 + ")"); 
		test(d.remove(k3),v3,name+".remove(" + k3 + ")");
		name = prefix + "{" + k1 + "-/>," + k2 + "->" + v2 + "," + k3 + "/->" + v3 + "}";
		test(!d.isEmpty(), name + " should not be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),v2, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		test(d.size(),1,name + ".size()");
		if (k1.equals(10)) {
			test(d.toString(),"[X,50=Bye,,X,,,]",name + " misorganized");
		}
		
		test(d.remove(k1),null,name+".remove(" + k1 + ")"); 
		test(d.remove(k3),null,name+".remove(" + k3 + ")");
		test(d.remove(k2),v2,name+".remove(" + k2 + ")");
		name = prefix + "{" + k1 + "-/>," + k2 + "-/>," + k3 + "-/>}";
		test(d.isEmpty(), name + " should be empty");
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),null, name + ".get(" + k2 + ")");
		test(d.get(k3),null, name + ".get(" + k3 + ")");
		test(d.size(),0,name + ".size()");	
		if (k1.equals(10)) {
			test(d.toString(),"[X,X,,X,,,]",name + " misorganized");
		}
		
		test(d.remove(k1),null,name+".remove(" + k1 + ")"); 
		test(d.remove(k3),null,name+".remove(" + k3 + ")");
		test(d.remove(k2),null,name+".remove(" + k2 + ")");
		
		test(d.put(k3,v4),null,name + ".put(" + k1 + "," + v4 + ")");
		name = prefix + "{" + k1 + "-/>," + k2 + "-/>," + k3 + "->" + v4 + "}";
		test(d.get(k1),null, name + ".get(" + k1 + ")");
		test(d.get(k2),null, name + ".get(" + k2 + ")");
		test(d.get(k3),v4, name + ".get(" + k3 + ")");
		if (k1.equals(10)) {
			test(d.toString(),"[X,X,,38=New,,,]",name + " misorganized");
		}
		
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
	
	private static void testRehash() {
		Map<Integer,String> d = new DoubleHashMap<Integer,String>();
		d.put(0, "0");
		d.put(7, "3");
		d.put(14, "5");
		d.put(21, "2");
		test(d.toString(),"[0=0,,21=2,7=3,,14=5,]","{0,7,14,21} disorganized");
		d.remove(7);
		test(d.toString(),"[0=0,,21=2,X,,14=5,]","{0,X7,14,21} disorganized");
		d.put(28, "4");
		test(d.toString(),"[0=0,,21=2,X,28=4,14=5,]","{0,X7,14,21,28} disorganized");
		d.put(35, "9");
		test(d.toString(),"[0=0,14=5,28=4,,,,,,21=2,35=9,,,]","{0,14,21,28,35} disorganized");
		d.remove(35);
		d.remove(28);
		d.remove(21);
		d.put(14,"1");
		d.put(1,"3");
		test(d.toString(),"[0=0,14=1,X,1=3,,,,,X,X,,,]","{0,1,14,X21,X28,X35} disorganized");
		d.put(13, "6");
		test(d.toString(),"[0=0,14=1,X,1=3,,,13=6,,X,X,,,]","{0,1,13,14,X21,X28,X35} disorganized");
		d.put(3,"7");
		test(d.toString(),"[0=0,14=1,X,1=3,,,13=6,3=7,X,X,,,]","{0,1,3,13,14,X21,X28,X35} disorganized");
		d.put(4,"4");
		test(d.toString(),"[0=0,14=1,X,1=3,4=4,,13=6,3=7,X,X,,,]","{0,1,3,4,13,14,X21,X28,X35} disorganized");
		d.remove(0);
		d.remove(13);
		d.remove(14);
		d.remove(1);
		test(d.toString(),"[X,X,X,X,4=4,,X,3=7,X,X,,,]","{X0,X1,3,4,X13,X14,X21,X28,X35} disorganized");
		d.put(5,"5");
		test(d.toString(),"[,,,3=7,4=4,5=5,]","{3,4,5} disorganized");
		d.remove(3);
		d.remove(4);
		d.remove(5);
		d.put(1,"1");
		d.put(2, "2");
		d.remove(1);
		d.remove(2);
		test(d.toString(),"[,X,X,X,X,X,]","{X1,X2,X3,X4,X5} disorganized");
		d.put(0, "0");
		// when rehashing, never make smaller than INITIAL_CAPACITY
		test(d.toString(),"[0=0,,,,,,]","{0} disorganized");
	}
	
	
	private static <K,V> Entry<K,V> e(final K k, final V v) {
		return new MyEntry<K,V>(k,v);
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
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
	
	public static interface Factory<T> {
		public abstract T create(int n);
	}
	
	public static class IntFactory implements Factory<Integer> {
		public Integer create(int x) { return new Integer(x); }
		public static IntFactory instance = new IntFactory();
	}
	
	public static class NastyInteger {
		final int x;
		public NastyInteger(int i) { x = i; }
		
		public int intValue() { return x; }

		public int hashCode() { 
			switch (x % 4) {
			default:
			case 0: return 0;
			case 1: return -30000 * x;
			case 2: return 1;
			case 3: return 70000 * x;
			}
		}
		
		public boolean equals(Object o) {
			if (o instanceof NastyInteger) {
				return x == ((NastyInteger)o).x; 
			}
			return false;
		}
		
		public String toString() { return "" + x; }
	}
	
	public static class NastyFactory implements Factory<NastyInteger> {
		public NastyInteger create(int x) { return new NastyInteger(x); }
		public static NastyFactory instance = new NastyFactory();
	}
	
	public void dotests() {
		Map<Integer,String> s = new DoubleHashMap<Integer,String>();
		Map<Integer,String> m = new java.util.HashMap<Integer,String>();
		
		int[] intTests = new int[]{ -99, 10, -1, 0, 1, 2, 3, 4, 5, 9, 6, -10, 11, 20, 55, 99, 100, -10000};
		NastyInteger[] nastyTests = new NastyInteger[intTests.length];
		for (int i=0; i < intTests.length; ++i) {
			nastyTests[i] = new NastyInteger(intTests[i]);
		}
		Integer[] tests = new Integer[]{ -99, 10, -1, 0, 1, 2, 3, 4, 5, 9, 6, -10, 11, 20, 55, 99, 100, -10000};
		
		System.out.println("New tests");		
		simpleTests(IntFactory.instance,tests);
		/// extra tests
		
		
		if (failed > 0) {
			System.out.println("Giving up.  Failed normal tests");
			return;
		}
		System.out.println("Nasty tests");
		simpleTests(NastyFactory.instance,nastyTests);
		
		System.out.println("Extra tests");
		
		
		// return values
		
		// empty set
		s = new DoubleHashMap<Integer,String>();
		test(s.remove(10) == null,"remove should return null if empty");
		test(s.put(6,"six") == null,"put should return null if added to empty");
		test("six".equals(s.remove(6)),"remove should return old value");
		
		// large set
		s = makeBigMap(IntFactory.instance,m);
		test("arabic(6)".equals(s.put(6, "liu")),"put should return old value if already in");
		test("liu".equals(s.remove(6)),"remove should return old if removed");
		test(s.remove(6) == null,"remove should return null if NOT removed");
		test(s.put(6,"6") == null,"put should return null if added");
		
		
		// wrong type
		
		// empty
		s = new DoubleHashMap<Integer,String>();
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
		s = makeBigMap(IntFactory.instance,m);
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
		s = new DoubleHashMap<Integer,String>();
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
		s = makeBigMap(IntFactory.instance,m);
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
		s = makeBigMap(IntFactory.instance,m);
		test(!s.entrySet().remove(new MyEntry<Integer,String>(2,"2")),"should not remove with wrong value");
		test(s,m,tests,"big.remove(wrong 2)");
		test(s.entrySet().remove(new MyEntry<Integer,String>(2,"arabic(2)")), "should remove with correct entry");
		

		/// Iterators tests
		
		System.out.println("Nice iterator tests");
		iteratorTests(IntFactory.instance);
		System.out.println("Nasty iterator tests");
		iteratorTests(NastyFactory.instance);
		
		/// remove with iterator tests
		if (failed > 0) {
			System.out.println("Skipping remove iterator tests");
			return;
		}
		System.out.println("Remove using iterators");
		
		for (int i : new int[]{1,3,5,13,2,6,10}) {
			testRemoveWithIterator(IntFactory.instance,tests,i);
			testRemoveWithIterator(NastyFactory.instance,nastyTests,i);
		}

		// now remove everything!
		s = makeBigMap(IntFactory.instance,m);
		Iterator<Integer> it = s.keySet().iterator();
		testRemoveRest(s.keySet().iterator(),s.keySet(),m.keySet(),"remove all iteratively");
		Map<NastyInteger,String> ns = new DoubleHashMap<NastyInteger,String>();
		Map<NastyInteger,String> nm = new java.util.HashMap<NastyInteger,String>();
		ns = makeBigMap(NastyFactory.instance,nm);
		testRemoveRest(ns.keySet().iterator(),ns.keySet(),nm.keySet(),"remove all iteratively (nasty)");
		
		
		// same as before, but this time, we leave one around the whole time.
		
		s = makeBigMap(IntFactory.instance,m);
		it = s.keySet().iterator();
		Integer first = it.next();
		testRemoveRest(it,s.keySet(),m.keySet(),"remove all iteratively except " + first);
		ns = makeBigMap(NastyFactory.instance,nm);
		Iterator<NastyInteger> nit = ns.keySet().iterator();
		NastyInteger nf = nit.next();
		testRemoveRest(nit,ns.keySet(),nm.keySet(),"remove all iteratively (nasty) except " + nf);
		
		// some final tests (only with nice integers)
		
		System.out.println("Testing contains/remove on entry set");
		for (int t : new int[]{1,3,5,13,2,6,10}) {
			s = makeBigMap(IntFactory.instance,m);
			test(!s.entrySet().contains(t),"should NOT locate entry using key");
			test(!s.entrySet().remove(t),"should not be able to remove using key");
			test(s,m,tests,"bogus EntrySet remove");
			s = makeBigMap(IntFactory.instance,m);
			test(!s.entrySet().contains(new MyEntry<Integer,String>(t,""+t)),"located entry with wrong value!");
			test(!s.entrySet().remove(new MyEntry<Integer,String>(t,""+t)),"removed with wrong value!");
			test(s,m,tests,"wrong EntrySet remove");
			s = makeBigMap(IntFactory.instance,m);
			test(s.entrySet().contains(new MyEntry<Integer,String>(t,"arabic("+t+")")),"didn't locate entry with right value");
			test(s.entrySet().remove(new MyEntry<Integer,String>(t,"arabic(" + t + ")")),"didn't remove with right value");
			m.remove(t);
			test(s,m,tests,"correct EntrySet remove");
			s = makeBigMap(IntFactory.instance,m);
			try {
				test(!s.entrySet().contains(new MyEntry<Integer,String>(t,null)),"located entry with null value!");
				test(!s.entrySet().remove(new MyEntry<Integer,String>(t,null)),"removed with bad null!");
				test(s,m,tests,"bogus null EntrySet remove");
				s.put(t, null);
				test(s.entrySet().contains(new MyEntry<Integer,String>(t,null)),"didn't located entry with correct null value!");
				test(s.entrySet().remove(new MyEntry<Integer,String>(t,null)),"didn't remove with correct null value");
				m.remove(t);
				test(s,m,tests,"correct null EntrySet remove");
			} catch (RuntimeException e) {
				test(false,"putting null and/or removing null should not throw exception");
			}
		}
	}

	private <X> void simpleTests(Factory<X> factory, X[] tests) {
		Map<X, String> s = new DoubleHashMap<X,String>();
		Map<X, String> m = new java.util.HashMap<X,String>();
		
		// initial test
		test(s,m,tests,"{}");
		if (failed > 0) return;
		
		// some test of add
		testAdd(factory.create(8),"eight",s,m,tests,"{8}");
		if (failed > 0) return;
		
		testAdd(factory.create(4),"four",s,m,tests,"{4,8}");
		if (failed > 0) return;
		
		testAdd(factory.create(10),"ten",s,m,tests,"{4,8,10}");
		if (failed > 0) return;
		
		testAdd(factory.create(6),"six",s,m,tests,"{4,6,8,10}");
		if (failed > 0) return;
		
		testAdd(factory.create(5),"five",s,m,tests,"{4,5,6,8,10}");
		if (failed > 0) return;
		
		testAdd(factory.create(2),"two",s,m,tests,"{2,4,5,6,8,10}");
		if (failed > 0) return;
		
		System.out.println("testing add redundantly");
		testAdd(factory.create(8),"eight",s,m,tests,"unchanged#1");
		testAdd(factory.create(2),"two",s,m,tests,"unchanged#2");
		testAdd(factory.create(10),"ten",s,m,tests,"unchanged#3");
		testAdd(factory.create(6),"six",s,m,tests,"unchanged#4");
		
		System.out.println("testing put changes");
		testAdd(factory.create(8),"huit",s,m,tests,"French 8");
		testAdd(factory.create(6),"sechs",s,m,tests,"German 6");
		testAdd(factory.create(2),"dos",s,m,tests,"Spanish 2");
		
		System.out.println("test clear");
		s.clear(); m.clear();
		test(s,m,tests,"cleared");
		s.clear();
		test(s,m,tests,"cleared again");
		
		System.out.println("Building a large map");
		s = makeBigMap(factory,m);
		
		System.out.println("Testing remove");
		s.remove(1); m.remove(1);
		test(s,m,tests,"remove(1)");
		
		s = makeBigMap(factory,m); s.remove(factory.create(3)); m.remove(factory.create(3));
		test(s,m,tests,"remove(3)");
		
		s = makeBigMap(factory,m); s.remove(factory.create(5)); m.remove(factory.create(5));
		test(s,m,tests,"remove(5)");
		
		s = makeBigMap(factory,m); s.remove(factory.create(13)); m.remove(factory.create(13));
		test(s,m,tests,"remove(13)");
		
		s = makeBigMap(factory,m); s.remove(factory.create(2)); m.remove(factory.create(2));
		test(s,m,tests,"remove(2)");
		
		s = makeBigMap(factory,m); s.remove(factory.create(6)); m.remove(factory.create(6));
		test(s,m,tests,"remove(6)");
		
		s = makeBigMap(factory,m); s.remove(factory.create(10)); m.remove(factory.create(10));
		test(s,m,tests,"remove(10)");
		
		
		// testing removal of things that are NOT in the set:
		
		s = makeBigMap(factory,m); s.remove(factory.create(0));
		test(s,m,tests,"remove(0)");
		
		s = makeBigMap(factory,m); s.remove(factory.create(20));
		test(s,m,tests,"remove(20)");
		
		// in order to get a space in the middle, I have to first remove something:
		s = makeBigMap(factory,m); s.remove(factory.create(8)); m.remove(factory.create(8));
		test(s,m,tests,"remove(8)");
		
		s.remove(factory.create(8)); // again!
		test(s,m,tests,"remove(8) again");
		
		
		// now testing removal in a small tree:
		
		s = new DoubleHashMap<X,String>(); m.clear();
		s.remove(0);
		test(s,m,tests,"{} - 0");
		
		s = new DoubleHashMap<X,String>();
		s.put(factory.create(0),"0");
		s.remove(factory.create(0));
		test(s,m,tests,"{0}-0");
		
		s = new DoubleHashMap<X,String>(); m.clear();
		s.put(factory.create(10),"10"); s.put(factory.create(0),"0");
		s.remove(factory.create(10)); m.put(factory.create(0),"0");
		test(s,m,tests,"{0,10}-10");
		
		s = new DoubleHashMap<X,String>(); m.clear();
		s.put(factory.create(10),"ten"); s.put(factory.create(0),"zero");
		s.remove(factory.create(0)); m.put(factory.create(10),"ten");
		test(s,m,tests,"{0,10}-0");
		
		s = new DoubleHashMap<X,String>(); m.clear();
		s.put(factory.create(10),"shi"); s.put(factory.create(20),"ershi");
		s.remove(factory.create(10)); m.put(factory.create(20),"ershi");
		test(s,m,tests,"{10,20}-10");
		
		s = new DoubleHashMap<X,String>(); m.clear();
		s.put(factory.create(10),"zehn"); s.put(factory.create(20),"zwanzig");
		s.remove(factory.create(20)); m.put(factory.create(10),"zehn");
		test(s,m,tests,"{10,20}-20");
	}

	private <X> void iteratorTests(Factory<X> factory) {
		Map<X, String> s = new DoubleHashMap<X,String>();
		Map<X,String> m = new java.util.HashMap<X,String>();
		Iterator<X> it;

		it= s.keySet().iterator();
		test(!it.hasNext(),"empty set iterator should not have next element");
		try {
			X x = it.next();
			test(false,"empty set next() returns " + x);
		} catch (NoSuchElementException ex){
			test(true,"internal error");
		} catch (RuntimeException ex) {
			test(false,"empty set next() threw wrong exception: " + ex);
		}
		
        s = makeBigMap(factory,m);
		it = s.keySet().iterator();
		int i;
		for (i=1; i <14; ++i) {
			if (it.hasNext()) {
				try {
					X x = it.next();
					if (x == null) {
						test(false,"next() return a null Integer instead of " + i);
					} else {
						test(m.containsKey(x),"it.next()");
						m.remove(x);
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
				X x = it.next();
				test(false,"it.next() should fail, but returns " + x);
			} catch (NoSuchElementException ex){
				test(true,"internal error");
			} catch (RuntimeException ex) {
				test(false,"After 13 next() calls, next() threw wrong exception: " + ex);
			}
		}
	}

	private <X> void testRemoveWithIterator(Factory<X> factory, X[] tests, int n) {
		Map<X, String> s = new DoubleHashMap<X,String>();
		Map<X,String> m = new java.util.HashMap<X,String>();
		s = makeBigMap(factory,m); removeWithIterator(s.keySet(),factory.create(n)); 
		m.remove(factory.create(n));
		test(s,m,tests,"iterator remove(" + n + ")");
	}
	
	private <X> void removeWithIterator(Set<X> s, X n) {
		Set<X> m = new java.util.HashSet<X>(s);
		Iterator<X> it = s.iterator();
		int i = 0;
		String note = "(before removal)";
		for (i=1; i < 14; ++i) {
			X x = null;
			if (it.hasNext()) {
				try {
					x = it.next();
					if (x == null) {
						test(false,note + " it.next() return a null Integer instead of " + i);
					} else {
						test(m.remove(x),"it.next() " + note);
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
			if (x.equals(n)) {
				it.remove();
				note = "(after removal)";
			}
		}
	}
	
	private <X> void testRemoveRest(Iterator<X> it, Set<X> s, Set<X> m, String id) {
		while (it.hasNext()) {
			X x = it.next();
			test(m.remove(x),id + " next() returned " + x);
			it.remove();
		}
		test(s.size(),m.size(),id + " size()");
		// test(!s.iterator().hasNext(),id + " afterwards hasNext() returned true?!?");
	}
	
	private <X> Map<X,String> makeBigMap(Factory<X> factory, Map<X,String> mirror) {
		Map<X,String> result = new DoubleHashMap<X,String>();
		mirror.clear();
		for (int i : new int[]{ 10, 2, 6, 9, 1, 5, 7, 12, 3, 4, 7, 8, 11, 13}) {
			String s = "arabic(" + i + ")";
			result.put(factory.create(i),s); mirror.put(factory.create(i),s);
		}
		return result;
	}
	
	private <X,Y> void testAdd(X x, Y y, Map<X,Y> s, Map<X,Y> mirror, X[] tests, String name) {
		s.put(x,y);
		mirror.put(x,y);
		test(s,mirror,tests,name);
	}
	
	private <X,Y> void test(Map<X,Y> s, Map<X,Y> mirror, X[] tests, String name) {
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
