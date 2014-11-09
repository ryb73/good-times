package edu.uwm.cs351.util;

import java.util.Vector;

public class GraphNode {
	
	private String name;
	private Vector<Edge> edges;
	
	public GraphNode(String n) {
		name = n;
		edges = new Vector<Edge>();
	}
	
	public void addEdge(Edge p_edge) throws IllegalArgumentException {  
		if(!p_edge.getSource().equals(this)) {
			throw new IllegalArgumentException("illegal edge addition requested");
		}
		edges.add(p_edge);
	}
	
	public String getName() {
		return name;
	}
	
	public Vector<Edge> getEdges() {
		return edges;
	}
	
	public void print() {
		System.out.println(name);
		//for (unsigned i = 0; i < edges.size(); i++)
		//out << edges[i]->source->name << "->" << edges[i]->dest->name << endl;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphNode other = (GraphNode) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
