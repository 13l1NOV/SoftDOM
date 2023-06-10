package Benchmarks;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import SoftParser.RandomAccessInputStream;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Consumer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
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

public class BenchmarkXMLStreamParserTests {
	@Test
	public void BenchmarkXMLStreamParserTests() throws RunnerException {
		BenchmarkXMLStreamParserTests.main(null);
	}

	public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(BenchmarkXMLStreamParserTests.class.getSimpleName())
            .forks(1)
            .warmupForks(0)
            .shouldFailOnError(true)
            .shouldDoGC(true)
            .build();
		
        new Runner(options).run();
    }
	
	@Benchmark
	@Warmup(iterations = 1)
	@Measurement(iterations = 2)
	@BenchmarkMode(Mode.SingleShotTime)
	public void run(GenerateDOMParameters param) throws XMLStreamException, IOException {
		System.out.println("[!!!] start generate iteration " + param.fileName + " "+ param.parserType);

		param.parse.accept(null);
		param.baf.close();
	}
	
	public String access(XMLInputFactory factory, RandomAccessInputStream is, int pos) throws XMLStreamException, IOException {
		is.seek(pos);
		var reader = factory.createXMLStreamReader(is);

		var type = reader.next();
		String st = null;
		if(type == XMLStreamConstants.START_ELEMENT ) {
			st = reader.getName().toString();
		}
		reader.close();
		return st;
	}

	@State(Scope.Thread)
	public static class GenerateDOMParameters {
		public BufferedInputStream baf;
		public Consumer<Object> parse;
	    
	    @Param({ BenchmarkParam.File.MB200 })
	    public String fileName;
	    
	    @Param({ ParserType.SAX, ParserType.StAXEvent, ParserType.StAXStream })
	    public String parserType;
	    

	    @Setup(Level.Iteration)
	    public void setup() throws ParserConfigurationException, IOException, SAXException, XMLStreamException{
	    	System.out.println("setup1");
	    	var path = BenchmarkParam.combinePath(fileName);
	    	baf = new BufferedInputStream(new FileInputStream(path));
	    	switch(parserType) {
	    		case(ParserType.SAX) : {
	    			SAXParserFactory factory = SAXParserFactory.newInstance();
	    			var saxParser = factory.newSAXParser();

	    			parse = safeRun((_null) -> saxParser.parse(path, new DefaultHandler()));
	    			break;
	    		} case(ParserType.StAXEvent) : {
	    			var fac = XMLInputFactory.newInstance();
	    			var reader = fac.createXMLEventReader(baf);

	    			parse = safeRun((_null) -> { while(reader.hasNext()) {reader.next();}});
	    			break;
	    		} case(ParserType.StAXStream) : {
	    			var fac = XMLInputFactory.newInstance();
	    			var reader = fac.createXMLStreamReader(baf);

	    			parse = safeRun((_null) -> { while(reader.hasNext()) {reader.next();} });
	    			break;
	    		}
	    	}
	    }
	}
	
	@FunctionalInterface
	public interface ThrowingConsumer<T> {
	    void accept(T arg) throws Exception;
	}

	private static <T> Consumer<T> safeRun(ThrowingConsumer<T> supplier) {
        return (_null) -> {
			try {
				supplier.accept(null);
			} catch (Exception e) { e.printStackTrace(); }
        };
	}

	public static class ParserType {
		public static final String SAX = "SAX";
		
		public static final String StAXStream = "StAXStream";

		public static final String StAXEvent = "StAXEvent";
	}
}
