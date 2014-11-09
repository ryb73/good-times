import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import edu.uwm.cs351.Canvas;
import edu.uwm.cs351.Drawing;
import edu.uwm.cs351.ShapeXMLReader;

class Main {
   public static void main(String[] args) {
	   for (final String arg : args) {
		   try {
			   Reader r = new BufferedReader(new FileReader(arg));
			   final Drawing drawing = ShapeXMLReader.readDrawing(r);
			   SwingUtilities.invokeLater(new Runnable() {
				   public void run() {
					   JFrame fm = new JFrame(arg);
					   fm.setSize(300, 300);
					   Canvas pane = new Canvas(drawing);
					   fm.setContentPane(pane);
					   fm.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					   fm.setVisible(true);
					   pane.repaint();
				   }
			   });
			   System.out.println("Content of " + arg + " is");
			   Writer w = new OutputStreamWriter(System.out);
			   drawing.toXML(w);
			   w.flush();
			   System.out.flush();
		   } catch (FileNotFoundException e) {
			   System.err.println("Could not open file " + arg);
		   } catch (IOException e) {
			   System.err.println("Problem reading " + arg+ ": " + e);
		   }
	   }
   }
}
