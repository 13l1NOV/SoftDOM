package Benchmarks;

import java.io.IOException;
import java.util.function.Function;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.xml.sax.SAXException;

import SoftParser.ArrayReference;
import SoftParser.ArraySoft;

//заменяем дефолтный GC на тот, который не чистит память в принципе
//JVM: -Xms4096m -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC
public class BenchmarkArraySaveTests {
	@Test
	public void BenchmarkArraySaveTests() throws RunnerException {
		BenchmarkArraySaveTests.main(null);
	}

	public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(BenchmarkArraySaveTests.class.getSimpleName())
            .forks(1)
            .warmupForks(0)
            .shouldFailOnError(true)
            //.shouldDoGC(true)
            .build();
		
        new Runner(options).run();
    }

	@Benchmark
	@Warmup(iterations = 0)
	@Measurement(iterations = 1)
	@BenchmarkMode(Mode.SingleShotTime)
	public void run(GenerateDOMParameters param) throws XMLStreamException, IOException {
		System.out.println("[!!!] start iteration " + param.fileName + " "+ param.arrayType + " WRITE " + param.arrayLen);
		var start = Runtime.getRuntime().freeMemory();
		var array = getArray(param.arrayType, param._arrayLen);
		for(var i = 0; i < param._arrayLen; i++) {
			array.save(i, new Integer(i));
		}
		var end = Runtime.getRuntime().freeMemory();
		System.out.println((start - end) / (1024.0 * 1024) +" mb");
	}

	@State(Scope.Thread)
	public static class GenerateDOMParameters {
		public ArrayReference targetArray;
		public int _arrayLen;
		public Function<Integer, Object> interaction;
		
		@Param({ ArrayLen.M10, ArrayLen.M20})
	    public String arrayLen;
	    
	    @Param({ BenchmarkParam.File.MB200 })
	    public String fileName;
	    
	    @Param({ ArrayType.SIMPLE, ArrayType.SOFTARRAY })
	    public String arrayType;

	    @Setup(Level.Iteration)
	    public void setup() throws ParserConfigurationException, IOException, SAXException, XMLStreamException{
	    	System.out.println("setup1");

	    	switch(arrayLen) {
	    		case(ArrayLen.M20) : _arrayLen = 20 * 1000 * 1000; break;
	    		case(ArrayLen.M10) : _arrayLen = 10 * 1000 * 1000 ; break;
	    	}
	    }
	}
	
	private ArrayReference getArray(String arrayType, int _arrayLen) {
		switch(arrayType) {
			case(ArrayType.SIMPLE) : {
				return new ArraySimple(_arrayLen);
			} case(ArrayType.SOFTARRAY) : {
				return new ArraySoft(_arrayLen);
			}
		}
		throw new IllegalStateException("");
	}

	public static class ArrayType {
		public static final String SIMPLE = "SIMPLE";
		
		public static final String SOFTARRAY = "SOFTARRAY";
	}
	
	public static class ArrayLen {
		public static final String M20 = "20 000 000";
		
		public static final String M10 = "10 000 000";
	}
}