package edu.uwm.cs351.util;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

import edu.uwm.cs351.SearchState;

public class Graph {
	
	private Vector<GraphNode> nodes;
	
	public Graph() { 
		nodes = new Vector<GraphNode>(); 
	}

	public void addNode (GraphNode new_node) {
		nodes.add(new_node);
	}

	public boolean isValidName(String i) {  
		for (GraphNode it : nodes) {
			if (it.getName().equals(i)) {
				return true;
			}
		}
		System.out.println("Node " + i + " invalid");
		return false;
	}

	public GraphNode getNode(String a) throws IllegalArgumentException { 
		for (GraphNode it : nodes) {
	        if (it.getName().equals(a)) {
	        	return it;
	        }
		}
		throw new IllegalArgumentException(a + ": no such node is not present");
	}

	public void print() {  
	   for (GraphNode it : nodes) {
		   it.print();
	   }
	}

	
	public Vector<GraphNode> breadthFirstSearch(GraphNode from, GraphNode to) { 
		// use a stack to track all the remaining places to explore
		Queue<SearchState> to_check = new LinkedList<SearchState>();
		
		// use a vector to track the locations that have been visited
		Vector<GraphNode> visited = new Vector<GraphNode>();
		Vector<GraphNode> solution = new Vector<GraphNode>();
		
		if (from.equals(to)) {  
			System.out.println("your're back at the beginning, nothing to do");
			return solution;
		}
		to_check.add(new SearchState(from));
		
		// loop as long as there are possibilities to explore
		while (!to_check.isEmpty()) {    
			// get the next possibility to check
			//SearchState path = to_check.remove();
			SearchState path = to_check.peek();
			to_check.remove();
			GraphNode r = path.lastNode();

	        // make sure it is not previously visited
	        if (!visited.contains(r)) {  
	        	// check for the goal
	        	// found it; print the path
	        	if (r.equals(to)) {
	        		visited.add(r);
	        		//return visited;
	        		return path.getVector();
	        	}
	        	
	        	// mark location as visited
	        	visited.add(r);
	        	
	        	// now extend search through all edges leading from this node
	        	for (Edge e : r.getEdges()) {
	        		to_check.add(path.extend(e));
	        	}
	        }
	    }
	    System.out.println("can't get from " + from.getName() + " to " + to.getName());
	    return visited;
	}
}
