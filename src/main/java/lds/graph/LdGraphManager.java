package lds.graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.httpclient.HttpException;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.vocabulary.RDFS;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.util.RDFLoader;
import org.openrdf.rio.ParserConfig;

import lds.resource.LdResourceFactory;
import lds.resource.R;
import lds.utilities.Utility;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.graph.io.conf.GDataConf;
import slib.graph.io.conf.GraphConf;
import slib.graph.io.loader.GraphLoaderGeneric;
import slib.graph.io.util.GFormat;
import slib.graph.model.graph.G;
import slib.graph.model.impl.graph.memory.GraphMemory;
import slib.graph.model.impl.repo.URIFactoryMemory;
import slib.graph.model.repo.URIFactory;
import slib.utils.ex.SLIB_Ex_Critic;
import slib.utils.ex.SLIB_Exception;

public class LdGraphManager extends RDFLoader {

	private static Set<String> trvrsdResources = new HashSet<String>();

	public LdGraphManager() {
		super(null, null);
	}

	public LdGraphManager(ParserConfig config, ValueFactory vf) {
		super(config, vf);
		// TODO Auto-generated constructor stub
	}

	public G generateGraph(String GraphURI) throws SLIB_Ex_Critic {

		URIFactory factory = URIFactoryMemory.getSingleton();
		URI uriGraph = factory.getURI(GraphURI);
		G graph = new GraphMemory(uriGraph);

		return graph;

	}

	public static G construct(Model m) {

		// TODO: convert stream ?
		// https://stackoverflow.com/questions/5778658/how-to-convert-outputstream-to-inputstream
		// IOUtils.copy(out, in);

		File temp = null;

		try {
			temp = File.createTempFile("temp-file-name", ".tmp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println("Temp file : " + temp.getAbsolutePath());

		try {
			m.write(new FileOutputStream(temp), "N-TRIPLE");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		URIFactory factory = URIFactoryMemory.getSingleton();
		URI graphURI = factory.getURI("http://ld-graph/");
		G g = new GraphMemory(graphURI);

		GDataConf dataConf = new GDataConf(GFormat.NTRIPLES, temp.getAbsolutePath());

		// We now create the configuration we will specify to the generic loader
		GraphConf gConf = new GraphConf();
		gConf.addGDataConf(dataConf);
		// gConf.addGAction(actionRerootConf);

		try {
			GraphLoaderGeneric.load(gConf, g);
		} catch (SLIB_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return g;

	}

	public static void getInOutResources(G graph, R r, int i, LdDataset dataset) throws HttpException {
		addIngoingResourcesFromDataset(graph, r, i, dataset);
		trvrsdResources.clear();
		addOutgoingResourcesFromDataset(graph, r, i, dataset);

	}

	// TODO: rename this method to constructResourcesGraph
	// is it the right way to do it ?
	public static void addIngoingResourcesFromDataset(G graph, R r, Integer level, LdDataset dataset)
			throws HttpException {

		// TODO: use dataset to make generic query
		// what about specific queries ?
		// add other method with query param ??

		// TODO: HERE: verify wheather you're using last Fouad's code ?

		RDFNode subject = null;
		RDFNode property = null;

		URI vSubject, vObject, eProperty;

		URIFactory factory = URIFactoryMemory.getSingleton();

		// TODO: some dataset do not have graph,in that case getDefaultGraph() will be
		// empty !

		// TODO: use contruct and load graph from jena model.

		String query = "select distinct ?subject ?property \n" + "where \n {";
		if (dataset.getDefaultGraph() != null)
			query += " GRAPH <" + dataset.getDefaultGraph() + ">";

		query += "{?subject ?property " + r.getTurtle() + ".\n" + "}}"; // TODO: filter deleted because it takes more
		// time

		level = level - 1;
		trvrsdResources.add(r.getUri().toString());

		System.out.println(query);

		try {
			ResultSet results = dataset.executeSelectQuery(query);
			for (; results.hasNext();) {

				QuerySolution soln = results.nextSolution();

				subject = (RDFNode) soln.get("subject");

				if (!subject.isResource())
					break;

				property = (RDFNode) soln.get("property");

				// vertex subject
				vSubject = factory.getURI(subject.toString());
				// edge property
				eProperty = factory.getURI(property.toString());
				// vertex object
				vObject = r.getUri();

				// add vertex subject to the graph
				graph.addV(vSubject);
				// add edge property to the graph
				graph.addE(vSubject, eProperty, vObject);
				// add vertex object to the graph
				graph.addV(vObject);

				// if subject of resource r is traversed previously continue the loop without
				// traversing it
				if (trvrsdResources.contains(subject.toString())) {
					continue;
				}

				// check traversed level: if level > 0 then given level in parameters has not
				// been reached
				if (level > 0) {
					// if subjects of resource r are not previously traversed and level has not
					// reached 0 recursivly traverse them
					// traversing subjects of r since the method gets ingoing resources
					addIngoingResourcesFromDataset(graph,
							LdResourceFactory.getInstance().uri(subject.toString()).create(), level, dataset);
				}
			}

		}

		finally {
			dataset.close();

		}

	}

	public static void addOutgoingResourcesFromDataset(G graph, R r, Integer level, LdDataset dataset) {

		RDFNode object = null;
		RDFNode property = null;

		URI vSubject, vObject, eProperty;

		URIFactory factory = URIFactoryMemory.getSingleton();

		String query = "select distinct ?property ?object\n" + "where \n" + "{" + " GRAPH" + " <"
				+ dataset.getDefaultGraph() + "> {" + r.getTurtle() + " ?property ?object.\n" + "}}"; // TODO: get graph
																										// from
																										// dataset

		level = level - 1;

		trvrsdResources.add(r.getUri().toString());
		try {
			ResultSet results = dataset.executeSelectQuery(query);
			for (; results.hasNext();) {

				QuerySolution soln = results.nextSolution();

				object = (RDFNode) soln.get("object");
				property = (RDFNode) soln.get("property");

				if (!object.isResource())
					break;

				vSubject = r.getUri();
				eProperty = factory.getURI(property.toString());
				vObject = factory.getURI(object.toString());

				graph.addV(vSubject);

				graph.addE(vSubject, eProperty, vObject);
				graph.addV(vObject);

				if (trvrsdResources.contains(object.toString())) {
					System.out.println("Resource: " + object.toString() + " not traversed since it already exists");
					continue;
				}

				if (level > 0) {

					addOutgoingResourcesFromDataset(graph,
							LdResourceFactory.getInstance().uri(object.toString()).create(), level, dataset);

				}

			}

		}

		finally {
			// trvrsdResources.clear();
			dataset.close();

		}
	}

	// public static Model contruct(LdDataset dataset, URI r) {
	//
	// ParameterizedSparqlString query_cmd = dataset.prepareQuery();
	//
	// String match_label = "Château de Cheverny";
	//
	// query_cmd.setCommandText("construct {?resource ?p ?o} where {" + "GRAPH
	// <http://dbpedia.org> {" + "?resource <"
	// + RDFS.label + "> ?label." + "?resource ?p ?o." + "}}");
	//
	// System.out.println("query = " + query_cmd.toString());
	//
	// Model m = dataset.executeConstructQuery(query_cmd.toString());
	//
	// return m;
	// }

}
