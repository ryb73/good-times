package edu.uwm.cs351;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import edu.uwm.cs351.util.Edge;
import edu.uwm.cs351.util.Graph;
import edu.uwm.cs351.util.GraphNode;

public class GraphInterface {
	
	private Graph graph;
	private BufferedReader fin;
	
	public GraphInterface(String file_name) {
		try {
			fin = new BufferedReader(new FileReader(file_name));
			
			if (!fin.ready()) {   
				System.out.println("unable to open file for read"); 
				System.exit(1);
			}
			
			graph = new Graph();
			int i = 0;
			String line = "";
			
			// THE NAMES OF NODES ARE LISTED ONE PER LINE, TILL # SENTINEL
			line = fin.readLine(); 
			while(line.charAt(0) != '#') {
				GraphNode newNode = new GraphNode(line); // construct node
				//System.out.println("Adding node: " + newNode.getName());
				graph.addNode(newNode);                 // add node to graph 
				line = fin.readLine();
			}
			//fin.ignore(2);                                // skip the # sentinel
			
			// THE EDGES ARE LISTED IN FORMAT A->B ONE PER LINE TILL # SENTINEL
			// THE EDGES ARE LISTED IN DESCENDING ALPHABETICAL ORDER SO THAT
			// DFS CAN BE PERFORMED IN ALPHABETICAL ORDER (LIFO)
			line = fin.readLine();
			while(line.charAt(0) != '#') {   
				i = line.indexOf('-');
				
				String tag1 = line.substring(0,i); //.c_str());
				String tag2 = line.substring(i+2,line.length());
				GraphNode source = null;
				GraphNode destination = null;
				try {  
					source = graph.getNode(tag1);
					destination = graph.getNode(tag2);
				} catch(Exception e) { 
					System.out.println(e.getStackTrace());
				}

				Edge newEdge = new Edge(source, destination);
				//System.out.println("Adding edge from: " + source.getName() + " to " + destination.getName());
				source.addEdge(newEdge);
				line = fin.readLine();
			}
			fin.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	public Vector<GraphNode> breadthFirstSearch(String start, String end) throws IllegalArgumentException {
		if (!graph.isValidName(start)) {
			throw new IllegalArgumentException("source node is not a valid node in the graph");
		}
		if (!graph.isValidName(end)) {
			throw new IllegalArgumentException("destination node is not a valid node in the graph");
		}
		return graph.breadthFirstSearch(graph.getNode(start), graph.getNode(end));
	}

}
