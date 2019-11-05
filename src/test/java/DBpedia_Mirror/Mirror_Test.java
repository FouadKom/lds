/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBpedia_Mirror;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;



 /*
 * @author Fouad Komeiha
 */
public class Mirror_Test {
   
    /*@Test
    public void mirror_test(){
        String queryString = "select * WHERE {?s ?p ?o. FILTER ( REGEX (STR (?s), \"resource\" ) )} limit 30";        
//        String queryString = " Select * where { ?s ?p ?o} limit 30";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:8891/sparql", query);
//        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
        
        try {
            ResultSet results = qexec.execSelect();
            System.out.println(ResultSetFormatter.asText(results)); 
        }   

        finally {
           qexec.close();
        }
    }*/
    
    @Test
    public void HdtTest() throws Exception{
            LdDataset dataSet = LdDatasetFactory.getInstance()
                    .name("example")
                    .file("file.hdt")
                    .create();          
            
            
            ParameterizedSparqlString query_cmd = dataSet.prepareQuery();

            query_cmd.setCommandText("select * WHERE {?s ?p ?o. FILTER ( REGEX (STR (?s), \"resource\" ) )} limit 30");               
            
            
            ResultSet resultSet = dataSet.executeSelectQuery(query_cmd.toString());
            System.out.println(ResultSetFormatter.asText(resultSet)); 
       
            
    }
    
}
