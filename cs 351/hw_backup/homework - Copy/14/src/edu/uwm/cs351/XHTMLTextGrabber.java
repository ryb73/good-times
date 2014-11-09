package edu.uwm.cs351;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.uwm.cs351.util.XMLEntity;
import edu.uwm.cs351.util.XMLReader;

public class XHTMLTextGrabber {
	public static List<String> getText(String stringURL) throws IOException, URISyntaxException {
		URI uri = new URI(stringURL);
		URL url = uri.toURL();
		InputStream in = url.openStream();	
		return getText(new InputStreamReader(new BufferedInputStream(in)));
	}
	
	public static List<String> getText(Reader r) throws IOException {
		List<String> result = new ArrayList<String>();
		XMLReader.read(new Factory(result), r);
		return result;
	}

	private static class Factory implements XMLEntity.Factory {
		class GeneralXHTMLEntity extends IgnoredEntity {
			public GeneralXHTMLEntity(String name) {
				super(name);
			}

			@Override
			public boolean addText(String s) {
				String[] tokens = s.split(" ");
				for(String t : tokens) {
					words.add(t);
				}

				return true;
			}
		}

		class Script extends IgnoredEntity {
			public Script() {
				super("script");
			}
		}

		class Stylesheet extends IgnoredEntity {
			public Stylesheet() {
				super("style");
			}
		}

		private List<String> words;

		public Factory(List<String> results) {
			this.words = results;
		}

		@Override
		public XMLEntity create(String name) {
			if(name.equals("script"))
				return new Script();
			else if(name.equals("style"))
				return new Stylesheet();
			else
				return new GeneralXHTMLEntity(name);
		}

		
	}
	
	// Define an entity for tags whose TEXT should be used (not ignored)
	// There's no need to remove punctuation etc -- it's done already in class Main.
	
	// This main program is useful for debugging XHTMLTextGrabber:
	public static void main(String[] args) throws IOException, URISyntaxException {
		for (String arg : args) {
			List<String> contents = getText(arg);
			for (String text : contents) {
				System.out.println(text);
			}
		}
	}
}
