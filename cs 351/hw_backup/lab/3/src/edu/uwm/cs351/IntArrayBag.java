package edu.uwm.cs351;

public class IntArrayBag implements Cloneable{
	private int[] data;
	private int manyItems;
	
	public IntArrayBag() {
		final int INITIAL_CAPACITY = 10;
		manyItems = 0;
		data = new int[INITIAL_CAPACITY];
	}
	
	public IntArrayBag(int initialCapacity) {
		if (initialCapacity < 0) {
			throw new IllegalArgumentException
			("initialCapacity is negative: " + initialCapacity);
		}
		manyItems = 0;
		data = new int[initialCapacity];
	}
	
	public void add(int element) {
		if (manyItems == data.length) {
			ensureCapacity(manyItems * 2 + 1);
		}
		data[manyItems] = element;
		manyItems++;
	}
	
	public boolean remove(int target) {
		int index = 0;
		while((index < manyItems) && (target != data[index])) {
			index++;
		}
		if(index == manyItems) {
			return false;
		}
		else {
			manyItems--;
			data[index] = data[manyItems];
			return true;
		}
	}
	
	public int size() {
		return manyItems;
	}
	
	public int getCapacity() {
		return data.length;
	}
	
	public int countOccurrences(int target) {
		int answer = 0;
		int index = 0;
		
		for(index = 0; index < manyItems; index++) {
			if(target == data[index]) {
				answer++;
			}
		}
		return answer;
	}
	
	public void ensureCapacity(int minimumCapacity) {
		int[] biggerArray;

		if(data.length < minimumCapacity) {
			biggerArray = new int[minimumCapacity];
			System.arraycopy(data, 0, biggerArray, 0, manyItems);
			data = biggerArray;
		}
	}
}
