package Benchmarks;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.function.Supplier;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import SoftParser.XmlStAXProcessor;

public class BenchmarkXPathTests {

	@Test
	public void BenchmarkXPathTests() throws RunnerException {
		BenchmarkXPathTests.main(null);
	}

	public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(BenchmarkXPathTests.class.getSimpleName())
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
	public void XPath(ProcessDOMParameters param) {
		System.out.println("[!!!] start process iteration " + param.fileName + " "+ param.parserType);
		System.out.println(param.process.get().getLength());
	}
	
	@State(Scope.Thread)
	public static class ProcessDOMParameters {
		public String filePath;

		@Param({ BenchmarkParam.File.MB100, BenchmarkParam.File.MB200 })
	    public String fileName;

	    @Param({ ParserType.LAZY, ParserType.STANDART})
	    public String parserType;
	    
	    public Supplier<NodeList> process;

	    @Setup(Level.Iteration)
	    public void setup() throws ParserConfigurationException, XPathExpressionException, XMLStreamException, IOException, SAXException{
	    	System.out.println("setup1");
	    	filePath = BenchmarkParam.combinePath(fileName);
	    	
	    	var parse = createParseFunction(parserType, filePath);
	    	
			var xf = XPathFactory.newInstance();
			var x = xf.newXPath();
			//var z = x.compile("/Template/GridLayout/Block");
			var z = x.compile("/Template/GridLayout/Block/Cells/Cell");

			process = () -> {
				try {
					return (NodeList)z.evaluate(parse, XPathConstants.NODESET);
				} catch (XPathExpressionException e) { e.printStackTrace(); }
				throw new IllegalStateException("");
			};
	    }
	}
	
    private static Document createParseFunction(String parserType, String filePath) throws ParserConfigurationException, XMLStreamException, IOException, SAXException{
    	switch(parserType) {
			case (ParserType.STANDART) : {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        factory.setIgnoringComments(true);
		        var builder = factory.newDocumentBuilder();

		        return builder.parse(new BufferedInputStream(new FileInputStream(filePath)));
			}
			case(ParserType.LAZY) : {
				var parser = XmlStAXProcessor.create(filePath);
				var res = parser.parse(); 
				parser.close(); 
				return res;
			} 
	    }
	    throw new IllegalStateException("");
	}
	
	private static ArrayList doOOM() {
		var softList = new SoftReference<Object>(new Object());
		ArrayList list = new ArrayList();
		while(softList.get() != null) {
			list.add(new Object[512]);
		}
		return list;
	}

	public static class ParserType {
		public static final String STANDART = "standart";
		
		public static final String LAZY = "lazy";
	}
}