/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager;

import java.util.ArrayList;
import java.util.List;
import lds.resource.R;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.vocabulary.OWL;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class LdManagerBaseO {
    
    protected LdDataset dataset;
    protected Conf config = null;
    protected String baseClassPath = "lds.LdManager.LdManagerBaseO.";

    
    public  LdManagerBaseO(LdDataset dataset) {
	this.dataset = dataset;
    }
    
    
    public List<String> getSubjects(R a) {
        List<String> commonSubjects = new ArrayList();
    
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        query_cmd.setCommandText("select distinct ?subject ?property " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") 
                                                             + " where {?subject ?property <" + a.getUri() + ">}");

        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            String resource = Utility.compressValue(qs.getResource("subject"));
            String property = Utility.compressValue(qs.getResource("property"));
            commonSubjects.add(resource+"|"+property);

        }
        // dataset.close();

        if(! commonSubjects.isEmpty())
            return commonSubjects;
        else
            return null;
    } 
 
    

    public List<String> getSameAsResources(R a) {
        List<String> sameAsresources = new ArrayList();
        
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        query_cmd.setCommandText("select distinct ?object " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") 
                                                             + " where { <" + a.getUri() + ">  <" + OWL.sameAs + "> ?object }");

        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            String resource = Utility.compressValue(qs.getResource("object"));
            sameAsresources.add(resource);

        }

        if(! sameAsresources.isEmpty())
            return sameAsresources;
        else
            return null;
    }
    
    
    public int countPropertyOccurrence(URI link){
        Literal count = null;
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        query_cmd.setCommandText("select (count(?subject) as ?count) " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where { ?subject <" + link + "> ?object. filter(isuri(?object))}");

        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());


        if (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            count = (Literal) qs.getLiteral("count");
            // dataset.close();
            return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

        }

        return 0;
    }
    
    
    public List<String> getCommonObjects(R a , R b){
        List<String> commonObjects = new ArrayList();
            
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        query_cmd.setCommandText("select distinct ?object  ?property1  ?property2 " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") 
                                                                           + " where {<" + a.getUri() + "> ?property1 ?object . "
                                                                           + "<" + b.getUri() + "> ?property2 ?object."
                                                                           + "filter( isuri(?object) )}");

        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            String resource = Utility.compressValue(qs.getResource("object"));
            String property1 = Utility.compressValue(qs.getResource("property1"));
            String property2 = Utility.compressValue(qs.getResource("property2"));
            commonObjects.add(resource+"|"+property1+"|"+property2);

        }

        if(! commonObjects.isEmpty())
            return commonObjects;
        else
            return null;
    }
    
    public List<String> getCommonSubjects(R a , R b){
        List<String> commonSubjects = new ArrayList();
        
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        query_cmd.setCommandText("select distinct ?subject  ?property1  ?property2 " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") 
                                                                           + " where {?subject ?property1 <" + a.getUri() + ">. "
                                                                           + " ?subject ?property2 <" + b.getUri() + ">}");

        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            String resource = Utility.compressValue(qs.getResource("subject"));
            String property1 = Utility.compressValue(qs.getResource("property1"));
            String property2 = Utility.compressValue(qs.getResource("property2"));
            commonSubjects.add(resource+"|"+property1+"|"+property2);
        }

        if(! commonSubjects.isEmpty())
            return commonSubjects;
        else
            return null;
    }
    
    public List<String> getEdges(R a) { 
        List<String> edges = new ArrayList();
        
        String edge;

        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        query_cmd.setCommandText("select distinct ?property \n"
                                + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ "> \n") 
                                + "where { \n"
                                + "{ \n"
                                + "select distinct ?property \n"
                                + "where {<" + a.getUri() + "> ?property ?object. \n" 
                                + "filter(isuri(?object)) } \n"
                                + "} \n"
                                + "union \n"
                                + "{ \n"
                                + "select distinct ?property \n"
                                + "where {[] ?property <" + a.getUri() + ">} \n"
                                + "} \n"
                                + "}");            

        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            edge = Utility.compressValue(qs.getResource("property"));
            edges.add(edge);
        }

      // dataset.close();

      if(! edges.isEmpty())
            return edges;
      else
            return null;
    }

    
    public List<String> getObjects(R a){
        List<String> objects =  new ArrayList<>();
        
        String object , property;


        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        query_cmd.setCommandText("select distinct ?object ?property " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where {<" + a.getUri() + "> ?property ?object ."
                + " filter(isuri(?object)) }");


        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            object = Utility.compressValue(qs.getResource("object"));
            property = Utility.compressValue(qs.getResource("property"));
            objects.add(object+"|"+property);
            // dataset.close();
        }

        if(! objects.isEmpty())
                return objects;
            else
                return null;
            
        }
    
    public int countShareCommonObjects(URI link , R a){
        Literal count = null;
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        query_cmd.setCommandText("select (count(distinct ?subject) as ?count) " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where {<" + a.getUri() + "> <" + link + "> ?object . "
                                                                                   + "?subject <" + link + "> ?object ."
                                                                                 + "filter(?subject != <" + a.getUri() + ">)}");

        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        if (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                count = (Literal) qs.getLiteral("count");
                // dataset.close();
                return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

        }

        // dataset.close();
        return 0;
    }
    
    public int countShareCommonSubjects(URI link , R a){
        Literal count = null;
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        query_cmd.setCommandText("select (count(distinct ?object) as ?count) " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where { ?subject <" + link + "> <" + a.getUri() + ">. "
                                                                                   + "?subject <" + link + "> ?object."
                                                                           + "filter(?object != <" + a.getUri() + "> && isuri(?object) ) }");


        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        if (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                count = (Literal) qs.getLiteral("count");
                // dataset.close();
                return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

        }

            // dataset.close();
            return 0;

    }
        
}
    

