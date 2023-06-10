package Benchmarks;

import java.lang.ref.SoftReference;

import SoftParser.ArrayReference;

public class ArraySimple<T extends Object> implements ArrayReference<T> {
	private SoftReference<Object>[] storage;
	
	public ArraySimple(int size) {
		storage = new SoftReference[size];
	}
	
	public void save(int index, T value) {
		storage[index] = new SoftReference(value);
	}
	
	public T get(int index) {
		return (T)storage[index].get();
	}
}
