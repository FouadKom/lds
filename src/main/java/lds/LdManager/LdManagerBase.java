package lds.LdManager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.vocabulary.OWL;
import org.openrdf.model.URI;

import lds.graph.GraphManager;
import lds.graph.LdGraphManager;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import org.apache.jena.rdf.model.Resource;
import sc.research.ldq.LdDataset;
import slib.graph.model.graph.G;
import slib.utils.ex.SLIB_Ex_Critic;
import slib.utils.i.Conf;

public class LdManagerBase implements LdManager {

	LdDataset dataset;
	protected Conf config = null;

	public LdManagerBase(LdDataset dataset) {
		this.dataset = dataset;
	}

	public LdManagerBase(LdDataset dataset, Conf config) {
		this.dataset = dataset;
		this.config = config;
	}

	public G generateGraph(LdDataset dataset, R a, R b, String graphURI) throws HttpException, SLIB_Ex_Critic {

		// TODO: We have to store the graph locally, to avoid quering everytime ?

		G graph = new GraphManager().generateGraph(graphURI);

		LdGraphManager.getInOutResources(graph, a, 2, dataset);
		LdGraphManager.getInOutResources(graph, b, 2, dataset);

		return graph;
	}

	public List<String> getSameAsResoures(R a) {

		ParameterizedSparqlString query_cmd = dataset.prepareQuery();

		query_cmd.setCommandText("select ?sameAs  { " + a.getTurtle() + " <" + OWL.sameAs + "> ?sameAs. }");

		System.out.println("query = " + query_cmd.toString());

		ResultSet rs = dataset.executeSelectQuery(query_cmd.toString());
		List<String> sameAsResources = new ArrayList<String>();

		for (; rs.hasNext();) {
			QuerySolution qs = rs.nextSolution();
			String sameAsResource = qs.getResource("sameAs").getURI();
			sameAsResources.add(sameAsResource);
		}

		return sameAsResources;

	}

	public static int countPropertyOccurrence(URI link, LdDataset dataset) {
		Literal count = null;
		ParameterizedSparqlString query_cmd = dataset.prepareQuery();

		query_cmd.setCommandText(
				"select (count(distinct ?subject) as ?count) where {" + "?subject <" + link + "> ?Object. }");

		// logger.info("query = " + query_cmd.toString());

		ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

		if (resultSet.hasNext()) {
			QuerySolution qs = resultSet.nextSolution();
			count = (Literal) qs.getLiteral("count");

		}

		return Integer.parseInt(count.toString().substring(0, 1));
	}
        
        
        public int countOutgoingEdges(R a , LdDataset dataset){
            Literal count = null;
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select count(distinct ?property) as ?count) where {<" + a.getUri() + "> ?property ?object . }");

            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            while (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                count = (Literal) qs.getLiteral("count");

            }
            
            return Integer.parseInt(count.toString().substring(0, 1));
        }
        
        public int countIngoingEdges(R a , LdDataset dataset){
            Literal count = null;
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select count(distinct ?property) as ?count) where {?subject ?property <" + a.getUri() + "> .}");

            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            while (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                count = (Literal) qs.getLiteral("count");

            }
            
            return Integer.parseInt(count.toString().substring(0, 1));
        }

	public boolean isSameAs(R a, R b) {
		// TODO Auto-generated method stub
		return false;
	}

	// get infos of a resource ? simple sparql

	// get outgoing/ingoing resources ?

	// count outgoing/ingoing resources ?

        @Override
        public int countIngoingEdges(URI link, R a, LdDataset dataset) {
            Literal count = null;
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select count(distinct ?property) as ?count) where {?subject <" + link + "> <" + a.getUri() + "> .}");

            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            while (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                count = (Literal) qs.getLiteral("count");

            }
            
            return Integer.parseInt(count.toString().substring(0, 1));
        }

        @Override
        public int countOutgoingEdges(URI link , R a, LdDataset dataset) {
            Literal count = null;
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select count(distinct ?property) as ?count) where {<" + a.getUri() + "> <" + link + "> ?object . }");

            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            while (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                count = (Literal) qs.getLiteral("count");

            }
            
            return Integer.parseInt(count.toString().substring(0, 1));
        }

        @Override
        public boolean isDirectlyConnected(URI link, R a, R b) {
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("ask {<" + a.getUri() + ">  <" + link + "> <" + b.getUri() + "> . }");

            return dataset.executeAskQuery(query_cmd.toString());
        }

        @Override
        public boolean shareCommonObject(URI link, R a, R b) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean shareCommonSubject(URI link, R a, R b) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int countDirectlyConnected(R a, LdDataset dataset) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int countCommonObjects(R a, LdDataset dataset) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int countCommonSubjects(R a, LdDataset dataset) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

}
