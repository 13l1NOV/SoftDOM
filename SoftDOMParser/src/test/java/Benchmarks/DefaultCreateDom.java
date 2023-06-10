package Benchmarks;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import SoftParser.XmlStAXProcessor;

public class DefaultCreateDom {

	public static void main(String[] args) throws SAXException, ParserConfigurationException, IOException, XMLStreamException {
        System.out.println("start");
        try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // 0,55 gb + | 0.06 gb +
        // 1,05 gb + | 0.1 gb +
        // 2,1 gb +  | 0.18 gb +
        var path = "C:/myhome/KURSACH/template200.xml";
        //var path = "C:/myhome/KURSACH/template100.xml";
		//var path = "C:/myhome/KURSACH/template400.xml"; 

		//var doc = getStandardDoc(path);
        var doc = getSoftDoc(path);
		
		//var k = getByXPath(doc);

		
		System.out.println("end");
	}
	
	private static Document getStandardDoc(String path) throws SAXException, ParserConfigurationException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        var is = new FileInputStream(path);
        return builder.parse(is);
	}
	
	private static Document getSoftDoc(String path) throws XMLStreamException, IOException {
		XmlStAXProcessor parser = XmlStAXProcessor.create(path);

		return parser.parse();
	}

	private static NodeList getByXPath(Document doc) throws XPathFactoryConfigurationException {
		try {
			var xf = XPathFactory.newInstance();
			var x = xf.newXPath();
			var z = x.compile("//*");
			//var z = x.compile("/Template/GridLayout/Block");
			var k = z.evaluate(doc, XPathConstants.NODESET);

			return (NodeList)k;
		} catch(XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
	}
}
