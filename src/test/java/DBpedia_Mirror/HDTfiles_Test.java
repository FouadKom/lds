/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBpedia_Mirror;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;

/**
 *
 * @author Fouad Komeiha
 */
public class HDTfiles_Test {
    
    @Test
    public void HDTfiles_Test() throws Exception{
            LdDataset dataSet = LdDatasetFactory.getInstance()
                    .name("example")
                    .file(System.getProperty("user.dir") + "/src/test/resources/swdf-2012-11-28.hdt")
                    .create();          
            
            
            ParameterizedSparqlString query_cmd = dataSet.prepareQuery();

            query_cmd.setCommandText("select * WHERE {?s ?p ?o. FILTER ( REGEX (STR (?s), \"resource\" ) )} limit 30");               
            
            
            ResultSet resultSet = dataSet.executeSelectQuery(query_cmd.toString());
            System.out.println(ResultSetFormatter.asText(resultSet)); 
       
            
    }
    
}
