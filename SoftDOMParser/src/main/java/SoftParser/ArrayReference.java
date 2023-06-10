package SoftParser;

public interface ArrayReference<T extends Object> {
	void save(int index, T value);
	T get(int index);
}