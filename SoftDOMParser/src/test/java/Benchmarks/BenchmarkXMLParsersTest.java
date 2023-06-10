package Benchmarks;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Supplier;

import javax.xml.parsers.DocumentBuilderFactory;
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
import org.w3c.dom.Document;

import SoftParser.XmlStAXProcessor;

public class BenchmarkXMLParsersTest {

	@Test
	public void BenchmarkXMLParsersTest() throws RunnerException {
		BenchmarkXMLParsersTest.main(null);
	}

	public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(BenchmarkXMLParsersTest.class.getSimpleName())
            //.timeUnit(TimeUnit.MICROSECONDS)
            .forks(1)
            .warmupForks(0)
            //.mode(Mode.Throughput) //?
            .shouldFailOnError(true)
            .shouldDoGC(true) //?
            .build();
		
        new Runner(options).run();
    }
	
	@Benchmark
	@Warmup(iterations = 1)
	@Measurement(iterations = 2)
	@BenchmarkMode(Mode.SingleShotTime)
	public void runGenerate(GenerateDOMParameters param) {
		System.out.println("[!!!] start generate iteration " + param.fileName + " "+ param.parserType);
		param.parse.get();
	}

	@State(Scope.Thread)
	public static class GenerateDOMParameters {
		@Param({ BenchmarkParam.File.MB100, BenchmarkParam.File.MB200 })
	    public String fileName;
	    
	    @Param({ ParserType.LAZY, ParserType.STANDART})
	    public String parserType;
	    
	    public String filePath;

	    public Supplier<Document> parse;

	    @Setup(Level.Iteration)
	    public void setup() throws ParserConfigurationException, XMLStreamException, IOException{
	    	System.out.println("setup1");
	    	filePath = BenchmarkParam.combinePath(fileName);
	    	
	    	parse = createParseFunction(parserType, filePath);
	    }
	}
	
    private static Supplier<Document> createParseFunction(String parserType, String filePath) throws ParserConfigurationException, XMLStreamException, IOException{
    	switch(parserType) {
			case (ParserType.STANDART) : {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        factory.setIgnoringComments(true);
		        var builder = factory.newDocumentBuilder();

		        return BenchmarkParam.safeRun(
		        		() -> builder.parse(new BufferedInputStream(new FileInputStream(filePath))));
			}
			case(ParserType.LAZY) : {
				var parser = XmlStAXProcessor.create(filePath);
				return BenchmarkParam.safeRun(() -> { var res = parser.parse(); parser.close(); return res;});
			} 
	    }
	    throw new IllegalStateException("");
	}

	public static class ParserType {
		public static final String STANDART = "standart";
		
		public static final String LAZY = "lazy";
	}
}