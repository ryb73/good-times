package edu.uwm.cs351.util;

public class Edge {
	
	private GraphNode source;
    private GraphNode dest;
    
	public Edge(GraphNode s, GraphNode d) {
		source = s;
		dest = d;
	}
	
	public GraphNode getSource() {
		return source;
	}
	
	public GraphNode getDest() {
		return dest;
	}
	
}
