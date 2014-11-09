package edu.uwm.cs351;

import java.io.IOException;
import java.io.Writer;

import edu.uwm.cs351.util.XMLEntity;

/**
 * An XML entity that ignores all information and is happy
 * with everything.
 */
public class IgnoredEntity implements XMLEntity {
	private final String elementName;

	public IgnoredEntity(String name) {
		elementName = name;
	}

	public String elementName() {
		return elementName;
	}

	@Override
	public boolean addAttribute(String name, String value) {
		return true;
	}

	@Override
	public boolean addEntity(XMLEntity s) {
		return true;
	}

	@Override
	public boolean addText(String s) {
		return true;
	}

	@Override
	public boolean attributesDone() {
		return true;
	}

	@Override
	public boolean nestedEntitiesDone() {
		return true;
	}

	@Override
	public void toXML(Writer w) throws IOException {
		throw new UnsupportedOperationException("toXML(Writer) method is not supported");
	}
}

