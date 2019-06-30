package lds.engine;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openrdf.model.URI;

import lds.graph.LdGraphManager;
import lds.measures.TestUtility;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import lds.utilities.Utility;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.graph.model.graph.G;
import slib.graph.model.impl.repo.URIFactoryMemory;
import slib.graph.model.repo.URIFactory;
import slib.utils.ex.SLIB_Ex_Critic;

public class EngineTest {
	public static final String dataSetDir = System.getProperty("user.dir") + "/src/test/resources/data.rdf";

	@Test
	public void runEngineOnSpecificLdMeasureTest() throws SLIB_Ex_Critic {

		G graph = TestUtility.getLDSDGraphExample();
		Utility.showVerticesAndEdges(graph);

//		LdSimilarityEngine engine = new LdSimilarityEngine(graph);

		R r2 = LdResourceFactory.getInstance().baseUri("http://e/").name("r2").create();
		R r4 = LdResourceFactory.getInstance().baseUri("http://e/").name("r4").create();

//		double score = engine.similarity("ldsd.LDSD", r2, r4);

//		assertEquals(score, 0.5, 0.0);
		
		// simulation for all componenets
		
		// 1. engine ( -> similarity
		
		// 1 sim -> 1 dataset ? (config) 
		
		// for LODS we pass a parameter: ld datasets = {dbpedia, wikidata}
		
		// for each dataset, there is a LdLoader (dataset)
		
		// LdLoader can use Proxy server, LdIndexer, graphManager to generate the graph? :: 
	}

}
