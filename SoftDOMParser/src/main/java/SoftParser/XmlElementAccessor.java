package SoftParser;

import java.io.Closeable;
import java.io.IOException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

public class XmlElementAccessor implements Closeable {

	private int[] startPossitionInFile;
	private RandomAccessInputStream rafis;
	private XMLInputFactory factory;
	
	public XmlElementAccessor(int[] startPossitionInFile, RandomAccessInputStream fileIs){
		this.startPossitionInFile = startPossitionInFile;
		factory = XMLInputFactory.newInstance();
		this.rafis = fileIs;
	}

	public AssociatedElementData get(int id) throws XMLStreamException, IOException {
		rafis.seek(startPossitionInFile[id]);
		var reader = factory.createXMLStreamReader(rafis);

		if(reader.next() == XMLStreamConstants.START_ELEMENT ) {
			Attribute[] attributes = null;
			if(reader.getAttributeCount() != 0) {
				attributes = new Attribute[reader.getAttributeCount()];
				for(var i=0;i<reader.getAttributeCount();i++) {
					attributes[i] = new Attribute(reader.getAttributeName(i).getLocalPart(), reader.getAttributeValue(i));
				}
			}
			reader.close();
			return new AssociatedElementData(attributes);
		}
		throw new IllegalStateException(this.getClass().getSimpleName()+" - must get a Node!");
	}
	
	public void close() throws IOException {
		rafis.close();
	}
}