package Benchmarks;

import java.io.IOException;

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

public class BenchmarkArrayGetTests {
	@Test
	public void BenchmarkArrayGetTests() throws RunnerException {
		BenchmarkArrayGetTests.main(null);
	}

	public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(BenchmarkArrayGetTests.class.getSimpleName())
            .forks(1)
            .warmupForks(0)
            .shouldFailOnError(true)
            .build();
		
        new Runner(options).run();
    }

	@Benchmark
	@Warmup(iterations = 1)
	@Measurement(iterations = 2)
	@BenchmarkMode(Mode.SingleShotTime)
	public void runRead(GenerateDOMParameters param) throws XMLStreamException, IOException {
		System.out.println("[!!!] start iteration " + param.fileName + " "+ param.arrayType + " READ " + param.arrayLen);
		for(var i = 0; i < param._arrayLen; i++) {
			param.targetArray.get(i);
		}
	}

	@State(Scope.Thread)
	public static class GenerateDOMParameters {
		public ArrayReference targetArray;
		public int _arrayLen;
		
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

			switch(arrayType) {
				case(ArrayType.SIMPLE) : {
					targetArray = new ArraySimple(_arrayLen);
					break;
				} case(ArrayType.SOFTARRAY) : {
					targetArray =  new ArraySoft(_arrayLen);
					break;
				}
			}
	    	
	    	switch(arrayLen) {
	    		case(ArrayLen.M20) : _arrayLen = 20 * 1000 * 1000; break;
	    		case(ArrayLen.M10) : _arrayLen = 10 * 1000 * 1000 ; break;
	    	}
	    	
	    	for(int i = 0; i < _arrayLen; targetArray.save(i, new Integer(i)), i++);
	    }
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