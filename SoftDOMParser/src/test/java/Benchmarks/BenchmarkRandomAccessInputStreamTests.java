package Benchmarks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;
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

import SoftParser.ISMappedByteBufferByRAF;
import SoftParser.RandomAccessInputStream;

public class BenchmarkRandomAccessInputStreamTests  {
	@Test
	public void BenchmarkRandomAccessInputStreamTests() throws RunnerException {
		BenchmarkRandomAccessInputStreamTests.main(null);
	}

	public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(BenchmarkRandomAccessInputStreamTests.class.getSimpleName())
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
		System.out.println("[!!!] start access iteration " + param.fileName + " " + param.accessCount + " " + param.isType);
		XMLInputFactory factory = XMLInputFactory.newInstance();

		Random rn = new Random();
		var max = param.possitions.size();
		for(var i = 0;i< param._accessCount; i++) {
			var x = Math.abs(rn.nextInt());
			access(factory, param.is, param.possitions.get(x % max));
		}
		param.is.close();
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
	    public RandomAccessInputStream is;
	    public ArrayList<Integer> possitions;
	    public int _accessCount;
	    
	    @Param({ BenchmarkParam.File.MB200 })
	    public String fileName;
	    
	    @Param({ "100 000"})
	    public String accessCount;
	    
	    //BufferedInputStream - если считывать со случайной позиции в массив, то он записывает по индексу такомуже
	    //как и в внутри него индекс, те масси должен иметь размерность всего файла, инче будет IndexOfBoundExc

	    @Param({ ISType.ISFileInputStream, ISType.ISMappedByteBuffer, ISType.ISRandomAccessFile }) // ISType.ISMappedByteBufferByFIS
	    public String isType;	

	    @Setup(Level.Iteration)
	    public void setup() throws ParserConfigurationException, IOException{
	    	System.out.println("setup1");
	    	var path = BenchmarkParam.combinePath(fileName);
	    	possitions = getPossitions(path);
	    	
	    	switch(isType) {
	    		case(ISType.ISMappedByteBufferByFIS) : is = new ISMappedByteBufferByFIS(path); break;
	    		case(ISType.ISFileInputStream) : is = new ISFileInputStream(path); break;
	    		case(ISType.ISMappedByteBuffer) : is = new ISMappedByteBufferByRAF(path); break;
	    		case(ISType.ISRandomAccessFile) : is = new ISRandomAccessFile(path); break;
	    	}
	    	
	    	_accessCount = 100*1000;
	    }
	}
	
	private static ArrayList<Integer> getPossitions(String path) throws IOException {
		var buf = new BufferedReader( new FileReader(path, Charset.forName("windows-1251")));
		var res = new ArrayList<Integer>();
		var simb = 0;
		int pos = 0;
		while((simb = buf.read())!= -1) {
			if(simb == 60) {
				simb = buf.read();
				if(simb != 33 && simb != 47) { res.add(pos); }
				pos++;
			}
			pos++;
		}
		buf.close();
		return res;
	}

	public static class ISType {
		public static final String ISMappedByteBufferByFIS = "ISMappedByteBufferByFIS ";
		
		public static final String ISRandomAccessFile = "ISRandomAccessFile";

		public static final String ISMappedByteBuffer = "ISMappedByteBuffer";
		
		public static final String ISFileInputStream = "ISFileInputStream";
	}
}
