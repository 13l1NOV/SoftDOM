package SoftParser;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public abstract class UnsupportedOperationsNode implements Node, Element {

	// Stub Node Operations

	@Override
	public String getNamespaceURI() { //[todo] сделать stub или реализовать, смотреть на реальных данных есть ли namespace
		return null;
	}

	@Override
	public String getNodeValue() throws DOMException { // убрать?
		return null;
	}

	//Stub Element Operations
	
	@Override
	public TypeInfo getSchemaTypeInfo() {
		return null;
	}

	// Unsupported Namespace Operations

	@Override
	public String getPrefix() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getBaseURI() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String lookupPrefix(String namespaceURI) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDefaultNamespace(String namespaceURI) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String lookupNamespaceURI(String prefix) {
		throw new UnsupportedOperationException();
	}

	// Unsupported Node Operations

	@Override
	public void setNodeValue(String nodeValue) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSupported(String feature, String version) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getFeature(String feature, String version) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object setUserData(String key, Object data, UserDataHandler handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getUserData(String key) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node removeChild(Node oldChild) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node appendChild(Node newChild) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void normalize() {
		throw new UnsupportedOperationException();		
	}

	@Override
	public void setPrefix(String prefix) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTextContent(String textContent) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public short compareDocumentPosition(Node other) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getTextContent() throws DOMException { // может и нужно
		throw new UnsupportedOperationException();
	}

	// Unsupported Element Operations

	@Override
	public void setAttribute(String name, String value) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeAttribute(String name) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attr setAttributeNode(Attr newAttr) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setIdAttribute(String name, boolean isId) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		//return null;
	}

	@Override
	public String getAttribute(String name) { // ADD
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		//return null;
	}

	@Override
	public Attr getAttributeNode(String name) { //ADD
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		//return null;
	}

	@Override
	public NodeList getElementsByTagName(String name) { // ADD
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		//return null;
	}

	@Override
	public String getAttributeNS(String namespaceURI, String localName) throws DOMException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		//return null;
	}

	@Override
	public Attr getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
		throw new UnsupportedOperationException();
		// TODO Auto-generated method stub
		//return null;
	}

	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) throws DOMException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		//return null;
	}

	@Override
	public boolean hasAttributeNS(String namespaceURI, String localName) throws DOMException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		//return false;
	}

	@Override
	public boolean hasAttribute(String name) { // ADD
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		//return false;
	}
}