package lds.graph;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;
import org.junit.Test;

import lds.engine.LdSimilarityEngine;
import lds.measures.ldsd.LDSD;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import lds.utilities.Utility;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.graph.io.plotter.GraphPlotter_Graphviz;
import slib.graph.model.graph.G;
import slib.utils.ex.SLIB_Ex_Critic;
import slib.utils.impl.Timer;

public class GraphManagerTest {

	@Test
	public void constructGraphOfOneLevelFromDBpediaAndRunLdsdOnItTest() throws SLIB_Ex_Critic {

		Timer t = new Timer();
		t.start();

		PrefixMapping prefixes = new PrefixMappingImpl();

		prefixes.setNsPrefix("dbpedia", "http://dbpedia.org/resource/");
		prefixes.setNsPrefix("dbpedia-owl", "http://dbpedia.org/ontology/");

		LdDataset dataset = null;

		try {
			dataset = LdDatasetFactory.getInstance()
					.service("http://dbpedia.org/sparql")
					.name("dbpedia")
					.defaultGraph("http://dbpedia.org")
					.prefixes(prefixes).create();

		} catch (Exception e) {

			fail("Error with dataset:" + e.getMessage());
		}

		R r1 = LdResourceFactory.getInstance()
				.baseUri("http://dbpedia.org/resource/")
				.name("Paris").create();

		LdGraphManager graphManager = new LdGraphManager();
		
		//TODO: config object to selection specific properties for eg ?

		G graph = graphManager.generateGraph("http://dbpedia.org/resource/");
		
		
		// TODO: use resources that have smaller set of interconnected IN/OUT resources
		
		R r2 = LdResourceFactory.getInstance()
				.baseUri("http://dbpedia.org/resource/")
				.name("France").create();

		try {
			// TODO assets number of retourned resources with another query that do count
			LdGraphManager.getInOutResources(graph, r1, 2, dataset);
			LdGraphManager.getInOutResources(graph, r2, 2, dataset);
			
			//GraphPlotter_Graphviz.plot(null, graph, null, true);
			
			
		} catch (HttpException e) {
			fail("HTTP Exception!");
		}
		
		
		
//		LdSimilarityEngine engine = new LdSimilarityEngine(graph);
		
		Utility.showVerticesAndEdges(graph);
		
//		double score = engine.similarity("resim.Resim", r1, r2);
		
//		System.out.println("score = " + score);
		System.out.println("getNumberVertices -> " + graph.getNumberVertices());
		
		t.stop();
		t.elapsedTime();
	}
	
	@Test
	public void constrcutRecursivellyResourceGraphTest() throws Exception {
		
		    String dataSetDir = System.getProperty("user.dir") + "/src/test/resources/data.rdf";
	        
	        
	        CreateFile file = new CreateFile(dataSetDir);
	        file.writeToFile();
	        
	        LdResources resources = new LdResources();
	        GraphManager graphMan = new GraphManager();
	        
	        G graph = graphMan.generateGraph("http://dbpedia.org/resource/");
	        G path = graphMan.generateGraph("http://dbpedia.org/path/");
	        Model model = ModelFactory.createDefaultModel();
	        FileManager.get().readModel(model , dataSetDir);
	        
	        LdDataset exemple_dataset = LdDatasetFactory.getInstance()
	        		.name("example")
	        		.file(dataSetDir)
	        		.create();
	        
	        
	        System.out.println("geting ingoing resources for Mammel with level 4"); //I choose level 4 to check if the resource will not be traversed
	        R r = LdResourceFactory.getInstance().baseUri("http://www.example.org#").name("Mammal").create();
	        LdGraphManager.getInOutResources(graph , r , 4 , exemple_dataset);
	        //resources.addIngoingResources(graph , "http://www.example.org#Mammal" , 4 , exemple_dataset);
	        graphMan.showVerticesAndEdges(graph);
	        System.out.println("------------------------------------------------------------------");
	        
	        //clearing the shared list that should hold the traversed resources in between methods
	        resources.clear();
	      
	        System.out.println("geting outgoing resources for fish with level 4");//I choose level 4 to check if the resource will not be traversed
	        //resources.addOutgoingResources(graph ,  "http://www.example.org#Fish" , 4 , exemple_dataset);
	        graphMan.showVerticesAndEdges(graph);
	        System.out.println("------------------------------------------------------------------"); 

	        resources.clear();
	   
	       // resources.getConnectingPath(path , "http://www.example.org#Bear" , "http://www.example.org#Mammal" , 3 , dataset);
	        graphMan.showVerticesAndEdges(path);
	        

	        
	}
	
	

}
