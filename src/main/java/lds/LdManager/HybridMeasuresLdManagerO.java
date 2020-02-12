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
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad komeiha
 */
public class HybridMeasuresLdManagerO extends LdManagerBaseO {
    protected String baseClassPath = "lds.LdManager.HybridMeasuresLdManagerO.";
    
    public HybridMeasuresLdManagerO(LdDataset dataset) {
        super(dataset);
    }

    public List<String> getIngoingFeatures(R a) {
        String edge = null;
        String subject = null;
        
        List<String> features = new ArrayList<>();

        ParameterizedSparqlString query_cmd = dataset.prepareQuery(); 

        query_cmd.setCommandText("select distinct ?property ?subject\n "
                                    + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ "> \n") 
                                    + "where {?subject ?property <" + a.getUri() + ">  }");


        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        while (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                subject = Utility.compressValue(qs.getResource("subject"));
                edge = Utility.compressValue(qs.getResource("property"));
                features.add(edge + "|" + subject + "|" + "In");

        }

        dataset.close();
        
        if(! features.isEmpty())
            return features;
        else
            return null;
    }

    public List<String> getOutgoingFeatures(R a) {
        String edge = null;
        String object = null;
        
        List<String> features = new ArrayList<>();

        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        query_cmd.setCommandText("select distinct ?property ?object\n "
                                    + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ "> \n") 
                                    + "where {<" + a.getUri() + "> ?property ?object. "
                                    + "filter isuri(?object)}");


        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        while (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                object = Utility.compressValue(qs.getResource("object"));
                edge = Utility.compressValue(qs.getResource("property"));
                features.add(edge + "|" +  object + "|" + "Out");

        }

        dataset.close();

        if(! features.isEmpty())
            return features;
        else
            return null;
    }
    

    public int getOutgoingFeatureFrequency(String property, String resource) {
        int count=0;
        
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();
        
        String query = "select (count(?subject) as ?count)\n"
                                    + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ "> \n") 
                                    + "where {?subject  <" + Utility.decompressValue(property) + "> <" + Utility.decompressValue(resource) + ">}";
        
        query_cmd.setCommandText(query);
        
        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());
       
        if (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            count = qs.getLiteral("count").getInt();

        }

        dataset.close();
        return count;
    }
    

    public int getIngoingFeatureFrequency(String property, String resource) {
       int count =0;
       
       ParameterizedSparqlString query_cmd = dataset.prepareQuery();
        
       query_cmd.setCommandText("select (count(?object) as ?count)\n"
                                    + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ "> \n") 
                                    + "where {<" + Utility.decompressValue(resource) + ">  <" + Utility.decompressValue(property) + "> ?object. "
                                    + "filter isuri(?object)}");
               

       ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());
       
       
       if (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            count = qs.getLiteral("count").getInt();

        }

        dataset.close();
        return count;

    }
    
    
    public  int countResource() {
        int count = 0;
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        query_cmd.setCommandText("select (count(distinct ?s) as ?count) " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") 
                                   + " WHERE\n" +
                                "  {\n" +
                                "    ?s ?p ?o .\n" +
                                "    FILTER ( REGEX (STR (?s), \"resource\" ) )\n" +
                               "  }");
        
        
        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        if (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                count =  qs.getLiteral("count").getInt();

        }

        return count;
        
    }
}
