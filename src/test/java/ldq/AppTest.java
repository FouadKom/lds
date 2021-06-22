/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.vocabulary.RDFS;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	public static final String path = System.getProperty("user.dir") + "/src/test/resources/";
	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
		BasicConfigurator.configure();
		
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
		
	}

	public void testCreateAndQueryDBpediaFrDataset() {
		
		PrefixMapping prefixes = new PrefixMappingImpl();

		prefixes.setNsPrefix("dbpedia", "http://dbpedia.org/resource/");
		prefixes.setNsPrefix("dbpedia-fr", "http://fr.dbpedia.org/resource/");
		prefixes.setNsPrefix("dbpedia-owl", "http://dbpedia.org/ontology/");

		LdDataset fr_DBpedia_dataset = null;
		try {
			fr_DBpedia_dataset = LdDatasetFactory.getInstance()
								 .service("http://fr.dbpedia.org/sparql")
								 .name("fr-dbpedia")
								 .prefixes(prefixes)
								 .create();

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		  
		ParameterizedSparqlString query_cmd = fr_DBpedia_dataset.prepareQuery();

		String match_label = "Château de Cheverny";

		query_cmd.setCommandText("select ?resource where {" 
		   + "?resource <"+ RDFS.label + "> ?label. " + "}");

		query_cmd.setLiteral("label", match_label, "fr");

		logger.info("query = " + query_cmd.toString());

		ResultSet resultSet = fr_DBpedia_dataset.executeSelectQuery(query_cmd.toString());

		List<String> mapped_resources = new ArrayList<String>();

		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.nextSolution();
			mapped_resources.add(qs.toString());
		}

	}

	public void testLoadingDBpediaDatasetFromRepositoryAndExecuteAskQuery() {

		try {
			LdDataset dbpedia = LdDatasetFactory.getInstance()
												.repository(path + "/datasets/")
												.name("dbpedia")
												.load();

			ParameterizedSparqlString query_cmd = dbpedia.prepareQuery();
			
			query_cmd.setCommandText(dbpedia.getQuery("askIfAlgeriaExists"));

			logger.info("askIfAlgeriaExists Query = " + query_cmd.toString());

			boolean result = dbpedia.executeAskQuery(query_cmd.toString());
			assertEquals(true, result);

		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

	}
	// TODO: for annotation
	// if (mapped_resources.isEmpty()) {
	// call spotlight api
	// Get get =
	// Http.get("http://api.dbpedia-spotlight.org/fr/annotate?text=Ch%C3%A2teau%20de%20Cheverny")
	// .header("accept", "application/json");
	// System.out.println(get.text());
	// System.out.println(get.headers());
	// System.out.println(get.responseCode());
}
