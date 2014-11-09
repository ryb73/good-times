import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Scanner;

import edu.uwm.cs351.FindPath;
import edu.uwm.cs351.WorldReader;
import edu.uwm.cs351.util.LabeledGraph;
import edu.uwm.cs351.util.LabeledGraph.Edge;
import edu.uwm.cs351.util.LabeledGraph.Node;

/**
 * @author Ryan Biwer
 */
public class Test {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner stdin = new Scanner(System.in);
		System.out.print("Select world filename: ");
		String fileName = stdin.next();

		LabeledGraph graph = null;

		try {
			Reader r = new BufferedReader(new FileReader(fileName));
			graph = WorldReader.read(r);
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
			return;
		} catch (IOException e) {
			System.err.println("Error reading file.");
			return;
		}

		String start;
		FindPath pathFinder = new FindPath();
		do {
			System.out.print("Enter starting room or 0 to quit: ");
			start = stdin.next();
			if(!start.equals("0")) {
				System.out.print("Enter end room: ");
				String end = stdin.next();

				Node startNode = graph.findNode(start);
				Node endNode = graph.findNode(end);
				if(startNode == null || endNode == null) {
					System.out.println("Bad node.");
				} else {
					List<Edge> dfsPath = pathFinder.find(startNode, endNode, false);
					List<Edge> bfsPath = pathFinder.find(startNode, endNode, true);
	
					System.out.print("DFS: ");
					printPath(dfsPath);
					System.out.print("BFS: ");
					printPath(bfsPath);
				}
			}
		} while(!start.equals("0"));

		System.out.println("Bye.");
	}

	/**
	 * @param path
	 */
	private static void printPath(List<Edge> path) {
		if(path == null) {
			System.out.println("no path");
		} else if(path.isEmpty()) {
			System.out.println("empty path");
		} else {
			System.out.print(path.get(0).getSource().getName());
			for(Edge e : path) {
				System.out.print("->" + e.getTarget().getName());
			}
			System.out.println();
		}
	}
}