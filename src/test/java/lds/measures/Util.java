/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.measures.resim.Resim;
import lds.measures.resim.ResimLdManager;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import static org.junit.Assert.fail;
import lds.measures.resim.ResimLdManager;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;

import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;

public class Util {
        public static File Indexesfolder = new File(System.getProperty("user.dir") + "/Indexes/Resim");
    
        public static void calculationDuration_Test(int numberOfPairs){
            
            double startTime , endTime , duration;
        
            SplitedList sp = Util.splitList(Util.getDbpediaResources(numberOfPairs * 2));

            //get two list of Dbpedia resources
            List<R> listOfResources1 = sp.getFirstList();
            List<R> listOfResources2 = sp.getSecondList();

            LdDataset dataset = Util.getDBpediaDataset();
            Conf config = new Conf();
            
            try {
            //Checking Time to calculate similarity using Dbpedia       
            //start timing
            startTime = System.nanoTime();

            config.addParam("useIndexes", false);
            ResimLdManager resimLdManager = new ResimLdManager(dataset, config);        
            Resim resim = new Resim(resimLdManager);

            for (int i = 0; i < numberOfPairs; i++) {

                R r1 = listOfResources1.get(i);
                R r2 = listOfResources2.get(i);

                resim.compare(r1 , r2);

            }

            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("Comparing " + numberOfPairs + " pairs using Dbpedia finished in " + duration + " second(s)");
            System.out.println();



            //updating Indexes

            Util.DeleteFilesForFolder(Indexesfolder , false);
            //start timing
            startTime = System.nanoTime();
            config.removeParam("useIndexes");
            config.addParam("useIndexes", true);
            resimLdManager = new ResimLdManager(dataset, config);

            resim = new Resim(resimLdManager);    

            for (int i = 0; i < numberOfPairs; i++) {

                R r1 = listOfResources1.get(i);
                R r2 = listOfResources2.get(i);

                resim.compare(r1 , r2);

            }
            resimLdManager.closeIndexes();

            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000  ;
            System.out.println("Index creation/updating for " + numberOfPairs + " pairs finished in " + duration + " second(s)");
            System.out.println();        

            //Checking Time to calculate similarity using Indexes
            //start timing
            startTime = System.nanoTime();

            config.removeParam("useIndexes");
            config.addParam("useIndexes", true);
            resimLdManager = new ResimLdManager(dataset, config);
            resim = new Resim(resimLdManager);

            for (int i = 0; i < numberOfPairs; i++) {

                R r1 = listOfResources1.get(i);
                R r2 = listOfResources2.get(i);

                resim.compare(r1 , r2);

            }

            resimLdManager.closeIndexes();

            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000  ;
            System.out.println("Comparing " + numberOfPairs + " pairs using Indexes finished in " + duration + " second(s)");
            System.out.println();
            
            }
            catch (Exception ex) {

                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }


        }

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
        
        
        private static class SplitedList{
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
