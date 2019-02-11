package lds.measures;

import org.openrdf.model.URI;

import slib.graph.model.graph.G;
import slib.graph.model.graph.elements.E;
import slib.graph.model.impl.graph.elements.Edge;
import slib.graph.model.impl.graph.memory.GraphMemory;
import slib.graph.model.impl.repo.URIFactoryMemory;
import slib.graph.model.repo.URIFactory;

public class TestUtility {

	public static G getLDSDGraphExample() {
		// in memory graph that represents example's example.
		URIFactory factory = URIFactoryMemory.getSingleton();

		URI uriGraph = factory.getURI("http://e/");
		URI r1 = factory.getURI("http://e/r1");
		URI r2 = factory.getURI("http://e/r2");
		URI r3 = factory.getURI("http://e/r3");
		URI r4 = factory.getURI("http://e/r4");

		URI l1 = factory.getURI("http://e/l1");
		URI l2 = factory.getURI("http://e/l2");
		URI l3 = factory.getURI("http://e/l3");

		// We create a graph which is loaded in memory
		G graph = new GraphMemory(uriGraph);

		E l1_r1_r2 = new Edge(r1, l1, r2);

		E l2_r1_r2 = new Edge(r1, l2, r2);

		E l3_r1_r4 = new Edge(r1, l3, r4);

		E l2_r1_r3 = new Edge(r1, l2, r3);

		E l1_r2_r1 = new Edge(r2, l1, r1);
		E l3_r2_r4 = new Edge(r2, l3, r4);

		graph.addE(l1_r1_r2);
		graph.addE(l2_r1_r2);
		graph.addE(l3_r1_r4);
		graph.addE(l2_r1_r3);

		graph.addE(l1_r2_r1);
		graph.addE(l3_r2_r4);

		return graph;

	}

}
