package edu.uwm.cs351;

import java.util.Vector;

import edu.uwm.cs351.util.Edge;
import edu.uwm.cs351.util.GraphNode;

public class SearchState {
	
	private GraphNode first;
	private GraphNode last;
    private Vector<Edge> path;
    
	public SearchState(GraphNode initial) { 
		path = new Vector<Edge>();
		first = initial;
		last = initial;
	}
	
	public GraphNode lastNode() { 
		return last; 
	}
	
	public SearchState extend(Edge edge) {
		SearchState ss = clone();
		ss.path.add(edge);
		ss.last = edge.getDest();
		return ss;
	}

	public Vector<GraphNode> getVector() {  
		Vector<GraphNode> solution = new Vector<GraphNode>();
		solution.add(new GraphNode(first.getName()));
		for (Edge e : path) {
			solution.add(new GraphNode(e.getDest().getName()));
		}
		return solution;
	}
	
	public SearchState clone() {
		SearchState clone = new SearchState(first);
		for(Edge e : path) {
			clone.path.add(e);
		}
		clone.last = last;
		return clone;
	}

	public void print() {  
		System.out.println(first.getName());
		for (Edge e : path) {
			System.out.println(" -> " + e.getDest().getName());
		}
	}
}
