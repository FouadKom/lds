/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldq;

import java.io.File;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author LENOVO
 */
public class HDTTest {
    
    @Test
    public void HdtTest() throws Exception{
        
        LdDataset dataSet = LdDatasetFactory.getInstance()
                .name("example")
                .file(System.getProperty("user.dir") + "/src/test/resources/swdf-2012-11-28.hdt")
                .create();
        
        if(dataSet == null)
            System.out.println("Failed to create dataset");

        ParameterizedSparqlString query_cmd = dataSet.prepareQuery();

        query_cmd.setCommandText("select * WHERE {?s ?p ?o} limit 30");               


        ResultSet resultSet = dataSet.executeSelectQuery(query_cmd.toString());
        System.out.println(ResultSetFormatter.asText(resultSet)); 
 
      
//        PrefixMapping prefixes = new PrefixMappingImpl();
//
//        prefixes.setNsPrefix("dbpedia", "http://dbpedia.org/resource/");
//        prefixes.setNsPrefix("dbpedia-owl", "http://dbpedia.org/ontology/");
//
//        LdDataset dataSet = null;
//
//        try {
//                dataSet = LdDatasetFactory.getInstance()
//                                .service("https://dbpedia.org/sparql")
//                                .name("dbpedia")
//                                .defaultGraph("http://dbpedia.org")
//                                .prefixes(prefixes).create();
//
//        } catch (Exception e) {
//
//                fail("Error with dataset: " + e.getMessage());
//        }
//
//        ParameterizedSparqlString query_cmd = dataSet.prepareQuery();
//
//        query_cmd.setCommandText("select * WHERE {?s ?p ?o} limit 30");               
//
//
//        ResultSet resultSet = dataSet.executeSelectQuery(query_cmd.toString());
//        System.out.println(ResultSetFormatter.asText(resultSet)); 

      
      
    }

}
