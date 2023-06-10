package SoftParser;

import java.util.Stack;

import org.w3c.dom.Document;

public class XmlProcessor {
	private ElementProvider provider;
	private TinyDocument document;
	
	private int countStart;
	private int countEnd;
	private Stack<Integer> currentNodeOnThisLevel;
	private Stack<Integer> previousUsedOnThisLevel;

	public XmlProcessor(ElementProvider provider) {
		this.provider = provider;
		
		currentNodeOnThisLevel = new Stack<Integer>();
		previousUsedOnThisLevel = new Stack<Integer>(); 
	}

	public void startDocument(String version, String encoding, boolean standalone) {
		countStart++;

		var docId = provider.getNextId();
		document = new TinyDocument(provider, docId, version, encoding, standalone);
		provider.setOwnerDocument(docId, document);
		currentNodeOnThisLevel.push(docId);
	}

	public void startElement(String name, Attribute[] attributes) {
		countStart++;

    	var newId = provider.getNextId();
    	int previousSibling = 0;
    	Integer previousNodeFromThisLevel;
    	if(countStart == 1 && (previousNodeFromThisLevel = previousUsedOnThisLevel.peek()) != null) {
    		// if new node located on current level, set previousSibling
    		previousSibling = previousNodeFromThisLevel;
    		// set nextSibling to previous node at this level
    		provider.setNextSibling(previousNodeFromThisLevel, newId);
    	}

    	var newTinyElement = new TinyElement(provider, newId);
    	provider.save(newId, newTinyElement, name, new AssociatedElementData(attributes));
    	provider.setParent(newId, currentNodeOnThisLevel.peek());
    	provider.setPreviousSibling(newId, previousSibling);

    	if(countStart > 1) {
    		// if we be up more than 1 times, create new empty previousSibling on new level
    		previousUsedOnThisLevel.push(null);
    		// set to parent node only first child from next level
    		provider.setFirstChild(currentNodeOnThisLevel.peek(), newId);
    	}
    	
    	currentNodeOnThisLevel.push(newId);
		
		countEnd = 0;
	}

	public void endElement() {
		countEnd++;

		var _currentNodeOnThisLevel = currentNodeOnThisLevel.pop();

    	if(countEnd > 1) {
    		// we pop last node at previous level, and go down to current level
    		previousUsedOnThisLevel.pop();
       	}

    	// replace previous node at current level on new node
		previousUsedOnThisLevel.pop();
		previousUsedOnThisLevel.push(_currentNodeOnThisLevel);

		countStart = 0;
	}
	
	public Document getDocument() {
		return document;	
	}
}