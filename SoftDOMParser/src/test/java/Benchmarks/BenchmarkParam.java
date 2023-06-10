package Benchmarks;

import java.nio.file.Paths;
import java.util.function.Supplier;

public class BenchmarkParam {
	public final static String pathToFiles = "C:/myhome/KURSACH/";
	
    public static String combinePath(String fileName) {
    	return Paths.get(pathToFiles, fileName + ".xml").toString();
    }

	@FunctionalInterface
	public interface ThrowingSupplier<T> {
	    T get() throws Exception;
	}

	public static <T> Supplier<T> safeRun(ThrowingSupplier<T> supplier) {
        return () -> {
			try {
				return supplier.get();
			} catch (Exception e) { e.printStackTrace(); }
			return null;
        };
	}

	public static class File {
		public static final String MB100 = "template100";

		public static final String MB200 = "template200";
		
		public static final String MB400 = "template400";
	}
}
