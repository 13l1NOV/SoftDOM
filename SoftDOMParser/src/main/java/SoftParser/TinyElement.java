package SoftParser;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TinyElement extends UnsupportedOperationsNode {
	protected ElementProvider provider;
	protected int identifier;
	
	public TinyElement (ElementProvider provider, int identifier) {
		this.provider = provider;
		this.identifier = identifier;
	}
	
	protected AssociatedElementData getAssociatedData() {
		return provider.getAssociatedData(identifier);
	}

	protected String getName() {
		return provider.getName(identifier);
	}

	@Override
	public String getNodeName() {
		return getName();
	}

	@Override
	public String getLocalName() {
		return getName();
	}

	@Override
	public short getNodeType() {
		return Node.ELEMENT_NODE;
	}

	@Override
	public Node getParentNode() {
		return provider.getParent(identifier);
	}

	@Override
	public NodeList getChildNodes() {
		//return provider.getFirstChild(identifier);
		throw new IllegalStateException();
	}

	@Override
	public Node getFirstChild() {
		return provider.getFirstChild(identifier);
	}

	@Override
	public Node getLastChild() {
		//return provider.getFirstChild(identifier);
		return null;
	}

	@Override
	public Node getPreviousSibling() {
		return provider.getPreviousSibling(identifier);
	}

	@Override
	public Node getNextSibling() {
		return provider.getNextSibling(identifier);
	}

	@Override
	public NamedNodeMap getAttributes() { //!@#!@#!@#!@#!@#!@#!@#!@#!@#!@#!@#
		return null;
	}

	@Override
	public Document getOwnerDocument() {
		return provider.getOwnerDocument();
	}

	@Override
	public boolean hasChildNodes() {
		return provider.getFirstChild(identifier) != null;
	}

	@Override
	public Node cloneNode(boolean deep) {
		throw new IllegalStateException();
//		return null;
	}

	@Override
	public boolean hasAttributes() {
		var attributes = getAssociatedData().attributes;
		return attributes != null && attributes.length != 0;
	}

	@Override
	public boolean isSameNode(Node other) {
		if(other instanceof TinyElement) {
			var tiny = ((TinyElement)other);
			if(tiny.getLocalName().equals(getLocalName()) && !(tiny.hasAttributes() ^ hasAttributes())) {
				if(tiny.hasAttributes()) {
					var attrs1 = tiny.getAssociatedData().attributes;
					var attrs2 = getAssociatedData().attributes;
					if(attrs1.length == attrs2.length) { // делать сравнение по hash у attributes
						for(var i = 0; i < attrs1.length; i++) {
							if(!attrs1[i].name.equals(attrs2[i].name) || !attrs1[i].value.equals(attrs2[i].value)) {
								return false;
							}
						}
						return true;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isEqualNode(Node arg) {
		if(arg instanceof TinyElement) {
			return ((TinyElement)arg).identifier == identifier;
		}
		return false;
	}
}