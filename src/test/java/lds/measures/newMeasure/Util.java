/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.newMeasure;

import lds.engine.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static lds.engine.Engine_Multithread_Test_LocalRDF.dataSetDir;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import static org.junit.Assert.fail;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import static org.junit.Assert.fail;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;

public class Util {
        
    public static LdDataset getDBpediaDataset() {
		
            PrefixMapping prefixes = new PrefixMappingImpl();

            prefixes.setNsPrefix("dbpedia", "http://dbpedia.org/resource/");
            prefixes.setNsPrefix("dbpedia-owl", "http://dbpedia.org/ontology/");

            LdDataset dataset = null;

            try {
                    dataset = LdDatasetFactory.getInstance()
                                    .service("https://dbpedia.org/sparql")
                                    .name("dbpedia")
                                    .defaultGraph("http://dbpedia.org")
                                    .prefixes(prefixes).create();

            } catch (Exception e) {

                    fail("Error with dataset: " + e.getMessage());
            }

            return dataset;
    }
        
    public static List<R> getDbpediaResources(int limit) {
            List<R> resources = new ArrayList<>();
            Resource resource;
            LdDataset dataset = getDBpediaDataset();

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

                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }

            dataset.close();

            return resources;

    }
    
    public static List<R> getLocalResources(int limit , String datsetPath) {
        List<R> resources = new ArrayList<>();
        Resource resource;
        LdDataset dataset = null;
        
        try {
            dataset = LdDatasetFactory.getInstance().name("Local_Eample_Dataset").file(datsetPath)
                       .defaultGraph("http://graph/dataset").create();

        } catch (Exception e) {
                fail(e.getMessage());
        }
        
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

                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }

            dataset.close();

            return resources;
        
    }
        
        public static SplitedList splitList(List<R> list){
            
            int wholeSize = list.size();
            int halfSize = list.size() / 2;
            
            List<R> firstPart = new ArrayList<>();
            List<R> secondPart = new ArrayList<>();
            
            for(int i = 0 ; i < halfSize ; i++){
                firstPart.add( list.get(i) );
            }
            
            for(int i = halfSize ; i < wholeSize ; i++){
                secondPart.add( list.get(i) );
            }
            
            
            return new SplitedList(firstPart , secondPart);
            
        }
        
        
        public static void listFilesForFolder(File folder , boolean traverseSubDirectory) {
                for (final File fileEntry : folder.listFiles()) {
                    if (fileEntry.isDirectory() && traverseSubDirectory) {
                        Util.listFilesForFolder(fileEntry , traverseSubDirectory);
                    } 
                    else if (fileEntry.isDirectory() && ! traverseSubDirectory) {
                        continue;
                    }
                    else {
                        System.out.println(fileEntry.getPath());
                    }
                }
        }
        
        public static void DeleteFilesForFolder(File folder , boolean traverseSubDirectory) {
                for (final File fileEntry : folder.listFiles()) {
                    if (fileEntry.isDirectory() && traverseSubDirectory) {
                        Util.DeleteFilesForFolder(fileEntry , traverseSubDirectory);
                    }
                    else if (fileEntry.isDirectory() && ! traverseSubDirectory) {
                        continue;
                    }
                    else {
                        fileEntry.delete();
                    }
                }
        }
        
        
        public static class SplitedList{
            private List<R> firstList;
            private List<R> secondList;
            
            public SplitedList(){
                firstList = new ArrayList<>();
                secondList = new ArrayList<>();
            }
            
            public SplitedList(List<R> list1 , List<R> list2){
                firstList = list1;
                secondList = list2;
            }
            
            public List<R> getFirstList(){
                return this.firstList;
            }
            
            public List<R> getSecondList(){
                return this.secondList;
            }
    
        }
       

}
