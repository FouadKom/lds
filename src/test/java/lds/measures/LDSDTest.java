package lds.measures;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;
import org.openrdf.model.URI;

import lds.measures.ldsd.LDSD;
import lds.utilities.Utility;
import slib.graph.model.graph.G;
import slib.graph.model.graph.elements.E;
import slib.graph.model.impl.graph.elements.Edge;
import slib.graph.model.impl.graph.memory.GraphMemory;
import slib.graph.model.impl.repo.URIFactoryMemory;
import slib.graph.model.repo.URIFactory;
import slib.utils.ex.SLIB_Exception;


public class LDSDTest {

	@Test
	public void isLDSDWorksCorrectlyOnPaperExample() {

		G graph = TestUtility.getLDSDGraphExample();

		URIFactory factory = URIFactoryMemory.getSingleton();
		URI r1 = factory.getURI("http://e/r1");
		URI r2 = factory.getURI("http://e/r2");
		URI r3 = factory.getURI("http://e/r3");
		URI r4 = factory.getURI("http://e/r4");

		URI l1 = factory.getURI("http://e/l1");
		URI l2 = factory.getURI("http://e/l2");
		URI l3 = factory.getURI("http://e/l3");

		E l1_r1_r2 = new Edge(r1, l1, r2);

		E l2_r1_r2 = new Edge(r1, l2, r2);

	

		System.out.println("- Graph, edges added");
		System.out.println(graph.toString());
		Utility.showVerticesAndEdges(graph);
		// ------------------------------------------------

		LDSD ldsd = new LDSD();

		int simcd = ldsd.cd(r1, r2, l1_r1_r2.getURI(), graph);

		assertEquals(simcd, 1);

		simcd = ldsd.cd(r1, r2, graph);
		assertEquals(simcd, 2);

		simcd = ldsd.cd(r2, r1, graph);
		assertEquals(simcd, 1);

		simcd = ldsd.cd(r1, l1_r1_r2, graph);
		assertEquals(simcd, 1);

		simcd = ldsd.cd(r1, l2_r1_r2, graph);
		assertEquals(simcd, 2);

		double sim = ldsd.LDSD_d(r1, r2, graph);
		assertEquals(sim, 0.25, 0.0);

		sim = ldsd.LDSD_dw(r2, r3, graph);
		assertEquals(sim, 1.0, 0.0);

		sim = ldsd.LDSD_dw(r1, r2, graph);
		assertEquals(sim, 0.270, 0.01);

		int sim_cio = ldsd.cio(r1, r2, l3, graph);
		assertEquals(sim_cio, 1);

		int sim_cii = ldsd.cii(r2, r3, l2, graph);
		assertEquals(sim_cii, 1);

		sim = ldsd.LDSD_i(r1, r2, graph);
		assertEquals(sim, 0.5, 0.0);

		sim = ldsd.LDSD_iw(r1, r2, graph);
		assertEquals(sim, 0.5, 0.0);

		sim = ldsd.LDSD_cw(r2, r4, graph);
		assertEquals(sim, 0.5, 0.0);

	}

}
