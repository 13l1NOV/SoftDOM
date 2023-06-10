package SoftParser;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.w3c.dom.Document;

public class XmlStAXProcessor {
	private BufferedInputStream baf;
	private XMLStreamReader reader;
	private RandomAccessInputStream rafis;
	private XmlProcessor eventProcessor;
	private String path;
	
	public XmlStAXProcessor(String path, RandomAccessInputStream rafis) throws XMLStreamException, FileNotFoundException {
		this.path = path;
		this.rafis = rafis;
		baf = new BufferedInputStream(new FileInputStream(path));
		var fac = XMLInputFactory.newInstance();
		reader = fac.createXMLStreamReader(baf);
	}
	
	public static XmlStAXProcessor create(String path, RandomAccessInputStream rafis) throws FileNotFoundException, XMLStreamException {
		return new XmlStAXProcessor(path, rafis);
	}
	
	public static XmlStAXProcessor create(String path) throws XMLStreamException, IOException {
		return new XmlStAXProcessor(path, new ISMappedByteBufferByRAF(path));
	}

	public void close() throws XMLStreamException, IOException {
		reader.close();
		baf.close();
		rafis.close();
	}

	public Attribute[] getAttributes(XMLStreamReader reader) {
		if(reader.getAttributeCount() == 0) {
			return null;
		}

		Attribute[] attributes = new Attribute[reader.getAttributeCount()];
		for(var i=0;i<reader.getAttributeCount(); i++) {
			attributes[i] = new Attribute(reader.getAttributeName(i).getLocalPart(), reader.getAttributeValue(i));
		}
		return attributes;
	}

	public Document parse() throws XMLStreamException, IOException {
		preprocessing();

		var first = true;
		while(reader.hasNext()) {
			switch(reader.next()) {
				case(XMLStreamConstants.START_ELEMENT) : {
					if(first) {
						first = false;
						eventProcessor.startDocument(reader.getVersion(), reader.getCharacterEncodingScheme(), reader.standaloneSet());
					}
					eventProcessor.startElement(reader.getLocalName(), getAttributes(reader));
					break;
				}
				case(XMLStreamConstants.END_ELEMENT) : {
					eventProcessor.endElement();
					break;
				}
			}
		}
		close();
		return eventProcessor.getDocument();
	}
	
	private void preprocessing() throws IOException {
		var startElementIndexes = getStartElementPossitions();
		var ea = new XmlElementAccessor(startElementIndexes , rafis);
		var ep = new ElementProvider(startElementIndexes.length, ea);
		eventProcessor = new XmlProcessor(ep);
	}

	private int[] getStartElementPossitions() throws IOException {
		var fileBR = new BufferedReader(new FileReader(path, Charset.forName("windows-1251"))); // encoding must be one-byte-type
		var res = new ArrayList<Integer>();
		var curByte = 0;
		int pos = 0;
		while((curByte = fileBR.read())!= -1) {
			if(curByte == 60) { // 60 is '<'
				curByte = fileBR.read();
				if(curByte != 33 && curByte != 47) { // 33 is '?',  47 is '!' 
					res.add(pos); 
				} 
				pos++;
			}
			pos++;
		}
		fileBR.close();

		var m_res = new int[res.size()];
		for(var i = 0; i < res.size(); m_res[i] = res.get(i), i++);
		return m_res;
	}
}
