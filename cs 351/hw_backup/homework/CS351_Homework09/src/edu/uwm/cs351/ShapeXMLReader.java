package edu.uwm.cs351;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Stack;

import edu.uwm.cs351.util.XMLTokenizer;

public class ShapeXMLReader {
	public static Drawing readDrawing(Reader r) throws IOException {
		Stack<Shape> pending = new Stack<Shape>();
		Stack<Collection<String>> attrs = new Stack<Collection<String>>(); // used to detect duplicate attributes
		Drawing result = null;
		XMLTokenizer tokens = new XMLTokenizer(r);
		while (tokens.hasNext()) {
			switch (tokens.next()) {
			default:
				throw new IOException("Unhandled token: " +tokens.current());
			case ERROR:
				throw new IOException("XML Error: " + tokens.getCurrentText());
			case OPEN:
				if(pending.isEmpty()) {
					if(result != null) {
						throw new IOException("Only one outer level element allowed.");
					}
					if(!tokens.getCurrentName().equals("Drawing")) {
						throw new IOException("Outer level element must be Drawing.");
					}

					result = new Drawing();
					pending.push(result);
					attrs.push(new LinkedList<String>());
				} else {
					String name = tokens.getCurrentName();
					Shape s = Shape.Factory.create(name);
					if(s == null) {
						throw new IOException("Invalid element name: " + name);
					}

					Shape parent = pending.peek();
					if(!parent.addShape(s)) {
						throw new IOException("Shape \"" + name + "\" is an invalid child shape of \"" +
								parent.elementName() + "\"");
					}
					pending.push(s);
					attrs.push(new LinkedList<String>());
				}

				break;
			case ATTR:
				String attr = tokens.getCurrentName();
				if(attrs.peek().contains(attr))
					throw new IOException("Duplicate attribute: " + attr);
				if(!pending.peek().addAttribute(attr, tokens.getCurrentText()))
					throw new IOException("Illegal attribute/value: " + attr);

				attrs.peek().add(attr);
				break;
			case CLOSE:
				Shape closeShape = pending.peek();
				if(!closeShape.attributesDone())
					throw new IOException(closeShape.elementName() + " missing attributes.");
				break;
			case TEXT:
				Shape s = pending.peek();
				if(!s.addText(tokens.getCurrentText()))
					throw new IOException(s.elementName() + " does not accept nested text.");
				break;
			case ETAG:
				Shape etagShape = pending.peek();
				if(!etagShape.shapesDone())
					throw new IOException(etagShape.elementName() + " missing shapes.");
				if(!etagShape.elementName().equals(tokens.getCurrentName()))
					throw new IOException("Trying to close " + etagShape.elementName() +
							" with </" + tokens.getCurrentName() + ">");

				pending.pop();
				break;
			case ECLOSE:
				Shape ecloseShape = pending.peek();
				if(!ecloseShape.attributesDone())
					throw new IOException(ecloseShape.elementName() + " missing attributes.");
				if(!ecloseShape.shapesDone())
					throw new IOException(ecloseShape.elementName() + " missing shapes.");
				pending.pop();
				break;
			} 
		}
		if (result == null) throw new IOException("Missing single Drawing element.");
		return result;
	}
}
