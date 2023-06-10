package SoftParser;

import java.lang.ref.SoftReference;

public class ArraySoft<T extends Object> implements ArrayReference<T> {
	private SoftReference<T[]>[] storage;
	private int batchSize = 1024;

	public ArraySoft(int capacity) {
		storage = new SoftReference[(int)(Math.ceil(capacity / (double)batchSize))];
		for(int i = 0; i < storage.length; i++) {
			storage[i] = new SoftReference<T[]>( (T[])new Object[batchSize]);
		}
	}
	
	public void save(int index, T target) {
		var setTo = storage[index / batchSize].get();
		if(setTo == null) {
			setTo = (T[])new Object[batchSize];
			storage[index / batchSize] = new SoftReference(setTo);
		}
		setTo[index % batchSize] = (T)target;
	}
	
	public T get(int index) {
		var target = storage[index / batchSize].get();
		if(target != null) {
			return target[index % batchSize];
		}
		return null;
	}
}