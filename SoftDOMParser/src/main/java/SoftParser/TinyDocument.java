package SoftParser;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public class TinyDocument extends TinyElement implements Document {
	private String version;
	private String encoding;
	private boolean standalone;
	
	public TinyDocument(ElementProvider provider, int identifier, String version, String encoding, boolean standalone) {
		super(provider, identifier);
		this.standalone = standalone;
		this.version = version;
		this.encoding = encoding;
	}

	@Override
	protected AssociatedElementData getAssociatedData() {
		return null;
	}
	
	@Override
	protected String getName() {
		return "#document";
	}

	@Override
	public short getNodeType() {
		return Node.DOCUMENT_NODE;
	}

	@Override
	public String getInputEncoding() {
		return encoding;
	}

	@Override
	public String getXmlEncoding() {
		return encoding;
	}

	@Override
	public boolean getXmlStandalone() {
		return standalone;
	}

	@Override
	public String getXmlVersion() {
		return version;
	}
	
	@Override
	public Document getOwnerDocument() {
		return this;
	}

	@Override
	public DocumentType getDoctype() {
		return null;
	}
	
	@Override
	public Element getDocumentElement() {
		return this;
	}

	//!@#!@#!@#!@#!@#!@#!@#!@#
	@Override
	public NodeList getElementsByTagName(String tagname) { // походу это поиск по имени, проверить на doc и реализовать надо мб
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) { //?
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getElementById(String elementId) { //?
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentURI() { // или делать свою реализацию или забить
		// TODO Auto-generated method stub
		return null;
	}

	//  UnsupportedOperations

	@Override
	public Element createElement(String tagName) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public DocumentFragment createDocumentFragment() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Text createTextNode(String data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Comment createComment(String data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CDATASection createCDATASection(String data) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attr createAttribute(String name) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public EntityReference createEntityReference(String name) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node importNode(Node importedNode, boolean deep) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setXmlVersion(String xmlVersion) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getStrictErrorChecking() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setStrictErrorChecking(boolean strictErrorChecking) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDocumentURI(String documentURI) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node adoptNode(Node source) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public DOMConfiguration getDomConfig() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void normalizeDocument() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public DOMImplementation getImplementation() {
		throw new UnsupportedOperationException();
	}
}
