package SoftParser;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ElementProvider {
	private XmlElementAccessor accessor;
	private ArrayReference<Node> storageNode;
	private ArrayReference<AssociatedElementData> storageAssociatedData;

	private int[] parents;
	private int[] firstChilds;
	private int[] previousSiblings;
	private int[] nextSiblings;
	private String[] names;

	private Document owner;
	private int countElements;
	private int index = 1;

	// from 93 to 76 sec to find 700k elements // 800mb // 6 - 8 sec to process dom /// with batch 2048 - 62-65 seconds to find 700k
	// 60 - 65 sec to find 4k elements // 350 mb //

	public ElementProvider(int countElements, XmlElementAccessor accessor) { 
		countElements = countElements + 2; // zero - is special value, one - is for Document
		this.countElements = countElements;
		this.accessor = accessor;

		storageNode = new ArraySoft<Node>(countElements);
		storageAssociatedData = new ArraySoft<AssociatedElementData>( countElements);

		this.parents = new int[countElements];
		this.firstChilds = new int[countElements];
		this.previousSiblings = new int[countElements];
		this.nextSiblings = new int[countElements];
		this.names = new String[countElements];
	}

	public int getNextId() {
		checkBounds(index);
		return index++;
	}

	public void save(int id, Node target, String name, AssociatedElementData data) {
		checkBounds(id);
		storageNode.save(id, target);
		names[id] = name;
		storageAssociatedData.save(id, data);
	}

	public Node get(int id) {
		checkBounds(id);
		if(id == 0) { // zero - defines that associated Node doesn't exist 
			return null;
		}
		if(id == 1) { // one - is for Document
			return getOwnerDocument();
		}

		var target = storageNode.get(id); 

		if(target == null) {
			target = new TinyElement(this, id);
			storageNode.save(id, target);
		}
		return target;
	}
	
	public String getName(int id) {
		checkBounds(id);
		return names[id];
	}

	public AssociatedElementData getAssociatedData(int id) {
		checkBounds(id);

		var target = storageAssociatedData.get(id);
		if(target == null) {
			try {
				target = accessor.get(id - 1); // zero - is special value, one - is for Document which doesn't exist in storage
			} catch (XMLStreamException | IOException e) { 
				e.printStackTrace(); throw new IllegalStateException(this.getClass().getSimpleName()+": cant take data from storage", e);
			}
			storageAssociatedData.save(id, target);
		}
		return target;

	}
	
	public Node getParent(int id) {
		checkBounds(id);
		return get(parents[id]);
	}
	
	public Node getPreviousSibling(int id) {
		checkBounds(id);
		return get(previousSiblings[id]);
	}
	
	public Node getNextSibling(int id) {
		checkBounds(id);
		return get(nextSiblings[id]);
	}
	
	public Node getFirstChild(int id) {
		checkBounds(id);
		return get(firstChilds[id]);
	}
	
	public void setParent(int id, int idParent) {
		checkBounds(id);
		parents[id] = idParent;
	}
	
	public void setPreviousSibling(int id, int idPreviousSibling) {
		checkBounds(id);
		previousSiblings[id] = idPreviousSibling;
	}
	
	public void setNextSibling(int id, int idNextSibling) {
		checkBounds(id);
		nextSiblings[id] = idNextSibling;
	}
	
	public void setFirstChild(int id, int firstChild) {
		checkBounds(id);
		firstChilds[id] = firstChild;
	}
	
	public Document getOwnerDocument() {
		return owner;
	}
	
	public void setOwnerDocument(int id, Document owner) {
		if(this.owner != null) {
			throw new IllegalStateException("owner document already saved!");
		}
		if(id != 1) {
			throw new IllegalStateException("document must have id equals one!");
		}
		this.owner = owner;
	}

	private void checkBounds(int id) {
		if(id < 0 || countElements <= id)
		{
			throw new IndexOutOfBoundsException(id + "- must be in range 0-" + countElements);
		}
	}
}