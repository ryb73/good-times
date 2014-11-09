package edu.uwm.cs351;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import edu.uwm.cs351.util.LabeledGraph;
import edu.uwm.cs351.util.XMLEntity;
import edu.uwm.cs351.util.XMLReader;
import edu.uwm.cs351.util.LabeledGraph.Node;

public class WorldReader {
	public static LabeledGraph read(Reader r) throws IOException {
		WorldFactory f = new WorldFactory();
		XMLEntity w = XMLReader.read(f, r);
		if (w instanceof WorldFactory.World) {
			return ((WorldFactory.World)w).getGraph();
		}
		throw new IOException("World file must have <World> entity at top-level.");
	}
	
	private static class WorldFactory implements XMLEntity.Factory {

		public XMLEntity create(String name) {
			if (name.equals("World")) return new World();
			if (name.equals("Room")) return new Room();
			if (name.equals("Door")) return new Door();
			return new IgnoredEntity(name);
		}

		/**
		 * Represents a world containing a number of rooms.
		 * @author Ryan Biwer
		 */
		class World extends IgnoredEntity {
			private LabeledGraph graph;

			public World() {
				super("World");
				graph = new LabeledGraph();
			}

			public LabeledGraph getGraph() {
				return graph;
			}

			@Override
			public boolean addEntity(XMLEntity s) {
				if(s instanceof Room) {
					Room room = (Room)s;
					room.setGraph(graph);
				} else if(s instanceof Door) {
					throw new IllegalArgumentException("Door element not accepted as " +
							"child of World element");
				}

				return super.addEntity(s);
			}
		}

		/**
		 * Represents a room containing a number of doors.
		 * @author Ryan Biwer
		 */
		class Room extends IgnoredEntity {
			private LabeledGraph graph;
			private Node node = null;
			private String name = null;

			public Room() {
				super("Room");
				new HashSet<Door>();
			}

			public void setGraph(LabeledGraph graph) {
				this.graph = graph;
			}

			@Override
			public boolean addAttribute(String name, String value) {
				if(name.equals("name")) {
					this.name = value;
					if(graph != null)
						node = graph.getNode(value);
				}

				return super.addAttribute(name, value);
			}

			@Override
			public boolean attributesDone() {
				return name != null;
			}

			@Override
			public boolean addEntity(XMLEntity s) {
				if(s instanceof Door) {
					Door d = (Door)s;
					d.setNode(node);
					d.setGraph(graph);
				}

				return super.addEntity(s);
			}
		}

		/**
		 * Represents a door from one room to another.
		 * @author Ryan Biwer
		 */
		class Door extends IgnoredEntity {
			private String direction = null;
			private String destination = null;
			private Node node = null;
			private LabeledGraph graph = null;

			public Door() {
				super("Door");
			}

			public void setGraph(LabeledGraph graph) {
				this.graph = graph;
			}

			public void setNode(Node node) {
				this.node = node;
			}

			@Override
			public boolean addAttribute(String name, String value) {
				if(name.equals("dir")) {
					direction = value;

					if(attributesDone() && node != null) {
						node.addEdge(direction, graph.getNode(destination));
					}

					return true;
				} else if(name.equals("dest")) {
					destination = value;

					if(attributesDone() && node != null) {
						node.addEdge(direction, graph.getNode(destination));
					}

					return true;
				}

				return super.addAttribute(name, value);
			}

			@Override
			public boolean attributesDone() {
				return direction != null && destination != null;
			}
		}
	}
}
