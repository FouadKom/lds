package lds.utilities;

import java.util.Set;

import org.openrdf.model.URI;

import slib.graph.model.graph.G;
import slib.graph.model.graph.elements.E;

public class Utility {
	
	public static void showVerticesAndEdges(G graph) {

		Set<URI> vertices = graph.getV();
		Set<E> edges = graph.getE();

		System.out.println("-Vertices");
		for (URI v : vertices) {
			System.out.println("\t" + v);
		}

		System.out.println("-Edge");
		for (E edge : edges) {
			System.out.println("\t" + edge);
		}
	}
}
