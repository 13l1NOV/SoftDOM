package SoftParser;

import java.io.DataInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.Random;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.lang.ref.SoftReference;

public class Main {

	private static XMLInputFactory fac = XMLInputFactory.newInstance();
	private static RandomAccessInputStream rafis; 
	private static DataInputStream dis; 
	private static XMLStreamReader reader;

	public static void msain(String[] args) {
		// TODO Auto-generated method stub
		Document e = null;
	}

	public static void run(String path) throws IOException, XMLStreamException {
		//raf = new RandomAccessFile(path, "r");
		//baf = new BufferedInputStream(new FileInputStream(raf.getFD()));
		rafis = new ISMappedByteBufferByRAF(path);
		dis = new DataInputStream(rafis);
		//bafs = new BufferedInputStream(dis);
		//var xx = checkSymb(path);

		//start(path);
		
		var xx = measure(safeRun(()->checkSymb(path)), "symbol process");
		measure(safeRun(() -> randMEASURE(path, xx)), "rand");
		
		
		rafis.close();
		dis.close();
	}
	
	private static Object randMEASURE(String path,ArrayList<Integer> xx) throws XMLStreamException, IOException {
		Random rn = new Random();
		var start = 1000*1000;
		String res = null;
		
		for(var i=1;i<=start;i++) {
			if(i%50000 == 0) {
				System.out.println("count:"+i+" "+res);
			}
			var ind = xx.get(Math.abs(rn.nextInt()) % 3400000);
			res = accessX(path, ind == 0 ? ind + 1 : ind);
		}		
		return null;
	}
	
	
	public static String accessX(String path, int pos) throws XMLStreamException, IOException {
		rafis.seek(pos);
		String st = null;

		reader = fac.createXMLStreamReader(rafis);

		var type = reader.next();			
		if(type == XMLStreamConstants.START_ELEMENT ) {
			st = reader.getName().toString();
			if(reader.getAttributeCount() != 0) {
				st = reader.getAttributeName(0).toString();
			}
		}
		reader.close();
		return st;
	}
	
	public static void runSTAXPARSER(String path) throws XMLStreamException, IOException{
		try {
			XmlStAXProcessor parser = XmlStAXProcessor.create(path);
			
			var doc = measure(safeRun(()->parser.parse()), "stax parser");

			System.out.println(doc.getXmlEncoding());
			System.out.println(doc.getFirstChild().getLocalName());
			
//			doOOM();
//			System.out.println("WAS OOM!!!!!!!");
//
//			System.out.println(doc.getXmlEncoding());
//			System.out.println(doc.getFirstChild().getLocalName());
//
//			var res = measure(safeRun(()-> xpath(doc)), "xpath stax process");
//
//			System.out.println(res.getLength());
		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList doOOM() {
		var softList = new SoftReference<Object>(new Object());
		ArrayList list = new ArrayList();

		while(softList.get() != null) {
			list.add(new Object[512]);
		}

		return list;
	}
	
	public static NodeList xpath(Document doc) {
		try {
			var xf = XPathFactory.newInstance();
			var x = xf.newXPath();

			//var z = x.compile("/Template/GridLayout/Block");
			var z = x.compile("/Template/GridLayout/Block/Cells/Cell");///Файл/СвУчДокОбор
			var k = z.evaluate(doc, XPathConstants.NODESET);

			return (NodeList)k;
		} catch(XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws XMLStreamException, IOException {
		var path = "C:/myhome/KURSACH/template400.xml";
		//var path = "C:/myhome/KURSACH/template.xml";
		//var path = "C:/myhome/KURSACH/testUpdMin.xml";
		//var path = "C:/myhome/KURSACH/testUpdMinBom.xml";
		//var path = "C:/myhome/KURSACH/testUpd.xml";
		
		runSTAXPARSER(path);
	}
	
	public static ArrayList<Integer> checkSymb(String path) throws IOException {
		//var e = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
		//var e = new BufferedInputStream(new RafIs(path));
		//var e = new BufferedReader(new FileReader(path));
		var e = new BufferedReader( new FileReader(path, Charset.forName("windows-1251")));
		var res = new ArrayList<Integer>();
		var s = 0;
		int pos = 0;
		while((s = e.read())!= -1) {
			if(s == 60) {
				s = e.read();
				if(s != 33 && s != 47) { res.add(pos); }
				pos++;
			}
			pos++;
		}
		return res;
	}
	
	//===================================================================
		public static <R> R measure(Supplier<R> func, String name) {
			System.out.println("Process: " + name+" start");
			var start = System.currentTimeMillis();
			var e = func.get();
			var end = System.currentTimeMillis();
			System.out.println("Process: " + name+", time: "+ (end - start));
			return e;
		}

		@FunctionalInterface
		public interface ThrowingSupplier<T> {
		    T get() throws Exception;
		}

		private static <T> Supplier<T> safeRun(ThrowingSupplier<T> supplier) {
	        return () -> {
				try {
					return supplier.get();
				} catch (Exception e) { e.printStackTrace(); }
				return null;
	        };
		}
}
