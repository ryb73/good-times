import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import edu.uwm.cs351.FindPath;
import edu.uwm.cs351.WorldReader;
import edu.uwm.cs351.util.LabeledGraph;
import edu.uwm.cs351.util.LabeledGraph.Edge;
import edu.uwm.cs351.util.LabeledGraph.Node;

class Main {
   public static void main(String[] args) {
	   LabeledGraph g = null;
	   FindPath find = new FindPath();
	   for (int i=0; i < args.length; ++i) {
		   if (args[i].equals("--print-length")) {
			   try {
				   LabeledGraph.printLength = Integer.parseInt(args[++i]);
			   } catch (IndexOutOfBoundsException e) {
				   usage();
			   } catch (NumberFormatException e) {
				   usage();
			   }
		   } else if (args[i].equals("--file")) {
			   try {
				   Reader r = new BufferedReader(new FileReader(args[++i]));
				   g = WorldReader.read(r);
				   System.out.println(g);
			   } catch (FileNotFoundException e) {
				   System.err.println("Could not open file '" + args[i] + "'");
				   System.exit(1);
			   } catch (IOException e) {
				   System.err.println("Error reading file '" + args[i] + "': " + e.getMessage());
				   System.exit(1);
			   } catch (IndexOutOfBoundsException e) {
				   usage();
			   }
		   } else if (args[i].equals("--route")) {
			   if (g == null) usage();
			   try {
				   Node n1 = g.findNode(args[++i]);
				   checkNode(args[i],n1);
				   Node n2 = g.findNode(args[++i]);
				   checkNode(args[i],n2);
				   List<Edge> p1 = find.find(n1, n2, false);
				   printPath(n1,p1);
				   List<Edge> p2 = find.find(n1, n2, true);
				   printPath(n1,p2);
			   } catch (IndexOutOfBoundsException e) {
				   usage();
			   }
		   } else usage();
	   }
   }

   private static void usage() {
	   System.err.println("usage: java Main { [--print-length <n>] [--file <filename> {--route <s1> <s2>}]}\n");
	   System.exit(1);
   }

   private static void checkNode(String name, Node n) {
	   if (n == null) {
		   System.err.println("No node found with name '" + name + "'");
		   System.exit(1);
	   }
   }
   
   private static void printPath(Node n, List<Edge> path) {
	   if (path == null) {
		   System.out.print("No Path");
	   } else {
		   System.out.print(n.getName());
		   for (Edge e : path) {
			   System.out.print("-" + e.getLabel() + "->" + e.getTarget().getName());
		   }
	   }
	   System.out.println();
   }
}
