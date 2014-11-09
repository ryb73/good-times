import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.Collator;
import java.util.Comparator;

import edu.uwm.cs351.WordList;
import edu.uwm.cs351.XHTMLTextGrabber;

class Main {
	
	private static Comparator<String> comp = new Comparator<String>() {
		private Collator c = Collator.getInstance();

		public int compare(String o1, String o2) {
			return c.compare(o1, o2);
		}
	};
	
   public static void main(String[] args) {
	   WordList words = new WordList();
	   for (int i=0; i < args.length; ++i) {
		   if (args[i].equals("--words")) {
			   if (i+1 == args.length) usage();
			   try {
				   BufferedReader r = new BufferedReader(new FileReader(args[++i]));
				   String s;
				   while ((s = r.readLine()) != null) {
					   words.add(s);
				   }
				   words.sort(comp);
			   } catch (FileNotFoundException e) {
				   System.err.println("Could not open file '" + args[i] + "'");
				   System.exit(1);
			   } catch (IOException e) {
				   System.err.println("Error reading file '" + args[i] + "': " + e.getMessage());
				   System.exit(1);
			   }
		   } else if (args[i].equals("--check")) {
			   if (i+1 == args.length) usage();
			   try {
				   	WordList little = new WordList();
				   for (String w : XHTMLTextGrabber.getText(args[++i])) {
					   w = trimNonLetters(w);
					   // ignore empty strings and strings with dots in them -- presumably abbreviations/urls
					   if (w.length() > 0 && w.indexOf('.') == -1) little.add(w);
				   }
				   words.merge(comp, little);
				   for (String w : little) {
					   System.out.println("Misspelled? " + w);
				   }
			   } catch (FileNotFoundException e) {
				   System.err.println("Could not open file '" + args[i] + "'");
				   System.exit(1);
			   } catch (IOException e) {
				   System.err.println("Error reading file '" + args[i] + "': " + e.getMessage());
				   System.exit(1);
			   } catch (URISyntaxException e) {
				   System.err.println("URL error: " + args[i] + ": " + e.getMessage());
			   }
		   } else usage();
	   }
   }

   private static String trimNonLetters(String w) {
	   int i = 0, j = w.length();
	   while (j > 0 && !Character.isLetter(w.charAt(j-1))) {
		   --j;
	   }
	   while (j > i && !Character.isLetter(w.charAt(i))) {
		   ++i;
	   }
	   w = w.substring(i,j);
	   return w;
   }
	   
   public static void usage() {
	   System.err.println("Usage: java Main {[--words <file>] [--check <url>]}\n");
	   System.exit(1);
   }
}
