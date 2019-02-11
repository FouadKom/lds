/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures;

import java.util.Set;
import lds.graph.GraphManager;
import lds.graph.LdResources;
import lds.measures.resim.Resim;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.graph.model.graph.G;
import slib.graph.model.impl.repo.URIFactoryMemory;
import slib.graph.model.repo.URIFactory;
import slib.utils.ex.SLIB_Ex_Critic;

/**
 *
 * @author Fouad Komeiha
 */
public class ResimTest {
	public static final String dataSetDir = System.getProperty("user.dir") + "/src/test/resources/data.rdf";

	@Test
	public void isResimWorksCorrectlyOnPaperExample() throws SLIB_Ex_Critic {
		LdDataset dataSet = null;
		URIFactory factory = URIFactoryMemory.getSingleton();
		G graph = new GraphManager().generateGraph("http://graphResim/dataset");
		URI r1 = factory.getURI("http://www.example.org#Fish"), r2 = factory.getURI("http://www.example.org#Whale");
		LdResources resources = new LdResources();

		try {
			dataSet = LdDatasetFactory.getInstance().name("example").file(dataSetDir)
					.defaultGraph("http://graphResim/dataset").create();

			// adding ingoing and outgoing resources from an RDF dataset to a graph is not
			// working using R class
			// R fish = LdResourceFactory.getInstance()
			// .uri(r1.toString())
			// .create();
			//
			// R whale = LdResourceFactory.getInstance()
			// .uri(r2.toString())
			// .create();
			//
			// R.getInOutResources(graph, fish, 2, dataSet);
			// R.getInOutResources(graph, whale, 2, dataSet);

			resources.addIngoingResources(graph, r1.toString(), 2, dataSet);
			resources.addOutgoingResources(graph, r1.toString(), 2, dataSet);
			resources.addIngoingResources(graph, r2.toString(), 2, dataSet);
			resources.addOutgoingResources(graph, r2.toString(), 2, dataSet);

		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		Set<URI> edges = lds.measures.resim.ResimLdManager.getEdges(dataSet);

		Resim resim = new Resim(dataSet, graph, edges);// This method was provided in class Resim to be used in the test
														// class

		double cii = 0, cio = 0, cii_r1 = 0, cio_r1 = 0, cii_r2 = 0, cio_r2 = 0, cii_norm = 0, cio_norm = 0, pptySim,
				ldsd, ldsdsim, sim;

		for (URI edge : edges) {
			cii = cii + resim.Cii(edge, r1, r2);
			cio = cio + resim.Cio(edge, r1, r2);
			cii_r1 = cii_r1 + resim.Cii(edge, r1);
			cio_r1 = cio_r1 + resim.Cio(edge, r1);
			cii_r2 = cii_r2 + resim.Cii(edge, r2);
			cio_r2 = cio_r2 + resim.Cio(edge, r2);
			cii_norm = cii_norm + resim.Cii_normalized(edge, r1, r2);
			cio_norm = cio_norm + resim.Cio_normalized(edge, r1, r2);
		}

		// components of LDSD in Resim
		assertEquals(0.0, cii, 0.0);
		assertEquals(1.0, cio, 0.0);
		assertEquals(0.0, cii_r1, 0.0);
		assertEquals(2.0, cio_r1, 0.0);
		assertEquals(0.0, cii_r2, 0.0);
		assertEquals(1.0, cio_r2, 0.0);
		assertEquals(1.0, cio_norm, 0.0);
		assertEquals(0.0, cii_norm, 0.0);

		pptySim = resim.PropertySim(r1, r2);
		assertEquals(0.11666666666666665, pptySim, 0.0);

		ldsd = resim.LDSD(r1, r2);
		assertEquals(0.25, ldsd, 0.0);

		ldsdsim = resim.LDSDsim(r1, r2);
		assertEquals(0.75, ldsdsim, 0.0);

		sim = resim.Resim(r1, r2);// w1 and w2 are both set to value = 1
		assertEquals(0.43333333333333335, sim, 0.0);

		sim = resim.Resim(r2, r1);// w1 and w2 are both set to value = 1
		assertEquals(0.43333333333333335, sim, 0.0);

		sim = resim.Resim(r1, r1);
		assertEquals(1.0, sim, 0.0);

	}

}
