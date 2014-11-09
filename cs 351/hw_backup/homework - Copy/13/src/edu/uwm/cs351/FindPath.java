package edu.uwm.cs351;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import edu.uwm.cs351.util.LabeledGraph.Edge;
import edu.uwm.cs351.util.LabeledGraph.Node;

public class FindPath {

	private final Map<Node,Edge> found = new HashMap<Node,Edge>();
	
	public List<Edge> find(Node from, Node to, boolean wantBest) {
		found.clear();
		found.put(from,null); // mark the starting node
		if (wantBest) {
			doBFS(from);
		} else {
			doDFS(from);
		}
		return createPath(from,to);
	}
	
	private void doDFS(Node n) {
		// depth first search (recursive)
		// for each node adjacent to n
		// if not found already, record that it is found and continue searching

		for(Edge e : n.edges()) {
			Node target = e.getTarget();
			if(!found.containsKey(target)) {
				found.put(target, e);
				doDFS(target);
			}
		}
	}
	
	public void doBFS(Node from) {
		Queue<Node> worklist = new LinkedList<Node>();
		worklist.add(from); // start with the empty path
		// repeatedly take something out of the work list and 
		// check out adjoining unfound nodes.
		// For each, record having found it and put the new node in the work list

		while(!worklist.isEmpty()) {
			Node n = worklist.poll();
			for(Edge e : n.edges()) {
				Node target = e.getTarget();
				if(!found.containsKey(target)) {
					found.put(target, e);
					worklist.add(target);
				}
			}
		}
	}
	
	public List<Edge> createPath(Node from, Node to) {
		List<Edge> result = new LinkedList<Edge>();
		// TODO: trace backwards through the "found" map.
		// If a node wasn't found, return null instead

		if(!found.containsKey(to))
			return null;

		Edge edge = found.get(to);
		while(edge != null) {
			result.add(0, edge);
			edge = found.get(edge.getSource());
		}

		return result;
	}

}
