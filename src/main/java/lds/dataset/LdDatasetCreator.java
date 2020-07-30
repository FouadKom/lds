/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.dataset;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import static org.junit.Assert.fail;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;

public class LdDatasetCreator {
        
    public static LdDataset getDBpediaDataset(DBpediaChapter chapter) {
		
            PrefixMapping prefixes = new PrefixMappingImpl();

            prefixes.setNsPrefix("dbpedia", DBpediaChapter.getResourceNameSpace(chapter));
            prefixes.setNsPrefix("dbpedia-owl", DBpediaChapter.getOntologyNameSpace(chapter));

            LdDataset dataset = null;

            try {
                    dataset = LdDatasetFactory.getInstance()
                                    .service(DBpediaChapter.getServiceURI(chapter))
                                    .name(DBpediaChapter.getName(chapter))
                                    .defaultGraph(DBpediaChapter.getDefaultGraphURI(chapter))
                                    .prefixes(prefixes).create();

            } catch (Exception e) {

                    fail("Error with dataset: " + e.getMessage());
            }

            return dataset;
    }
    
    public static LdDataset getDBpediaDataset() {
        return getDBpediaDataset(DBpediaChapter.En);
    }
    
    public static LdDataset getRemoteDataset(String service , String defaultGraph , String name) {
		
        

            LdDataset dataset = null;

            try {
                    dataset = LdDatasetFactory.getInstance()
                                    .service(service)
                                    .name(name)
                                    .defaultGraph(defaultGraph)
                                    .create();

            } catch (Exception e) {

                    fail("Error with dataset: " + e.getMessage());
            }

            return dataset;
    }
    
    
    
    public static LdDataset getDBpediaMirrorDataset(String service , String name) {
		
            PrefixMapping prefixes = new PrefixMappingImpl();

            prefixes.setNsPrefix("dbpedia", "http://dbpedia.org/resource/");
            prefixes.setNsPrefix("dbpedia-owl", "http://dbpedia.org/ontology/");

            LdDataset dataset = null;

            try {
                    dataset = LdDatasetFactory.getInstance()
                                    .service(service)
                                    .name(name)
                                    .defaultGraph("http://dbpedia.org")
                                    .prefixes(prefixes).create();

            } catch (Exception e) {

                    fail("Error with dataset: " + e.getMessage());
            }

            return dataset;
    }
    
    public static LdDataset getDBpediaHDTDataset(String dataSetDir , String name) {
		
            LdDataset dataSet = null;
        
            try {
                    dataSet = LdDatasetFactory.getInstance()
                                              .name(name)
                                              .file(dataSetDir)
                                              .create();

            } catch (Exception e) {
                    fail(e.getMessage());
            }

            return dataSet;
    }
    
    public static LdDataset getLocalDataset(String dataSetDir , String name){
        LdDataset dataset = null;
        
        try {
            dataset = LdDatasetFactory.getInstance()
                        .name(name)
                        .file(dataSetDir)
                        .defaultGraph("http://graph/dataset")
                        .create();

        } catch (Exception e) {
                fail(e.getMessage());
        }
        
        return dataset;
    }
    
    public static LdDataset getLocalDataset(String dataSetDir , String graph , String name){
        LdDataset dataset = null;
        
        try {
            dataset = LdDatasetFactory.getInstance()
                        .name(name)
                        .file(dataSetDir)
                        .defaultGraph(graph)
                        .create();

        } catch (Exception e) {
                fail(e.getMessage());
        }
        
        return dataset;
    }
        
        
    public static List<R> getDbpediaResources(DBpediaChapter chapter , int limit) {
            List<R> resources = new ArrayList<>();
            Resource resource;
            LdDataset dataset = getDBpediaDataset(chapter);

            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            int offset  = (int)(Math.random() * 5000 + 2077) ;

            try{
            query_cmd.setCommandText("select ?resource from <http://dbpedia.org> where {\n" +
                                     "?resource a ?o .\n" +
                                     "filter(regex(?resource , \"http://dbpedia.org/resource/\", \"i\"))\n" +
                                     "\n" +
                                     "} limit " +  Integer.toString(limit) + "\n" +
                                     "OFFSET " + Integer.toString(offset) );

            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            while (resultSet.hasNext()) {
                    QuerySolution qs = resultSet.nextSolution();
                    resource = (Resource) qs.getResource("resource");
                    resources.add(LdResourceFactory.getInstance().uri(resource.toString()).create());
            }

            }catch (Exception ex) {

                Logger.getLogger(LdDatasetFactory.class.getName()).log(Level.SEVERE, null, ex);
            }

            dataset.close();

            return resources;

    }
    
    public static List<R> getDbpediaResources(int limit){
        return getDbpediaResources(DBpediaChapter.En , limit);
    }
    
    public static List<R> getDbpediaMirrorResources(String service , String name , int limit) {
            List<R> resources = new ArrayList<>();
            Resource resource;
            LdDataset dataset = getDBpediaMirrorDataset(service , name);

            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            int offset  = (int)(Math.random() * 5000 + 2077) ;

            try{
            query_cmd.setCommandText("select ?resource from <http://dbpedia.org> where {\n" +
                                     "?resource a ?o .\n" +
                                     "filter(regex(?resource , \"http://dbpedia.org/resource/\", \"i\"))\n" +
                                     "\n" +
                                     "} limit " +  Integer.toString(limit) + "\n" +
                                     "OFFSET " + Integer.toString(offset) );

            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            while (resultSet.hasNext()) {
                    QuerySolution qs = resultSet.nextSolution();
                    resource = (Resource) qs.getResource("resource");
                    resources.add(LdResourceFactory.getInstance().uri(resource.toString()).create());
            }

            }catch (Exception ex) {

                Logger.getLogger(LdDatasetFactory.class.getName()).log(Level.SEVERE, null, ex);
            }

            dataset.close();

            return resources;

    }
    
    public static List<R> getHDTResources(String datasetDir , String name , int limit) {
        List<R> resources = new ArrayList<>();
        Resource resource;
        LdDataset dataset = getDBpediaHDTDataset(datasetDir , name);
        
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();
        
        int offset  = (int)(Math.random() * 5000 + 2077) ;
        
        try{
            query_cmd.setCommandText("select ?resource where {\n" +
                                     "?resource a ?o .\n" +
                                     "filter(regex(?resource , \"http://dbpedia.org/resource/\", \"i\"))\n" +
                                     "\n" +
                                     "} limit " +  Integer.toString(limit) + "\n" +
                                     "OFFSET " + Integer.toString(offset) );

            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            while (resultSet.hasNext()) {
                    QuerySolution qs = resultSet.nextSolution();
                    resource = (Resource) qs.getResource("resource");
                    resources.add(LdResourceFactory.getInstance().uri(resource.toString()).create());
            }

            }catch (Exception ex) {

                Logger.getLogger(LdDatasetFactory.class.getName()).log(Level.SEVERE, null, ex);
            }

            dataset.close();

            return resources;
        
    }
    
    public static List<R> getLocalResources(String dataSetDir , String name , int limit) {
        List<R> resources = new ArrayList<>();
        Resource resource;
        LdDataset dataset = getLocalDataset(dataSetDir , name);
        
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();
        
        try{
            query_cmd.setCommandText("select distinct ?resource where {\n" +
                                     "?resource ?p ?o} limit " +  Integer.toString(limit));

            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            while (resultSet.hasNext()) {
                    QuerySolution qs = resultSet.nextSolution();
                    resource = (Resource) qs.getResource("resource");
                    resources.add(LdResourceFactory.getInstance().uri(resource.toString()).create());
            }

            }catch (Exception ex) {

                Logger.getLogger(LdDatasetFactory.class.getName()).log(Level.SEVERE, null, ex);
            }

            dataset.close();

            return resources;
        
    }

}
