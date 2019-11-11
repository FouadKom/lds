/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.engine;

import test.utility.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import lds.measures.Measure;
import lds.resource.R;
import lds.resource.ResourcePair;
import static org.junit.Assert.fail;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;
import org.junit.Test;

/**
 *
 * @author Fouad Komeiha
 */
public class Engine_Multithread_Test_HdtFiles {
    public static final String dataSetDir = System.getProperty("user.dir") + "/src/test/resources/dbpedia2016-04en.hdt";


   @Test
    public void engine_multithread_test_hdtfiles() throws InterruptedException , ExecutionException , Exception {
   // public static void main(String args[]) throws InterruptedException, ExecutionException, Exception{
        double startTime , endTime , duration;
        
        LdDataset dataSet = null;
        
        try {
                dataSet = LdDatasetFactory.getInstance()
					  .name("example")
					  .file(dataSetDir)
                                          .create();

        } catch (Exception e) {
                fail(e.getMessage());
        }
        
        Util.SplitedList sp = Util.splitList(Util.getLocalResources(10 , dataSetDir));
        List<R> listOfResources1 = sp.getFirstList();
        List<R> listOfResources2 = sp.getSecondList();
        
        List<ResourcePair> pairs = new ArrayList<>();
        
        Conf config = new Conf();
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , dataSet);
//        config.addParam("resourcesCount" , 2350906); //used only for PICSS number of resources in DBpedia
        
        
        for(int i = 0 ; i < listOfResources1.size() ; i++){
            ResourcePair pair = new ResourcePair(listOfResources1.get(i) , listOfResources2.get(i));
            pairs.add(pair);
        }
        
        LdSimilarityEngine engine = new LdSimilarityEngine();

        engine.load(Measure.LDSD_cw  ,config);

        startTime = System.nanoTime();

        engine.similarity(pairs);    
//        engine.similarity2(pairs);

        //end timing
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("Comparing " + pairs.size() + " pairs from local RDF using multithreading finished in " + duration + " second(s) ");
        System.out.println();
        
//        for( Map.Entry<String,Double> entry : results.entrySet()){
//                System.out.println( entry.getKey() + " : " + entry.getValue());
//        }
        
        System.out.println();

        startTime = System.nanoTime();

        for(int i = 0 ; i < listOfResources1.size() ; i++){
            engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
//            System.out.println(listOfResources1.get(i).getUri().toString() + " , " + listOfResources2.get(i).getUri().toString() + " : " + engine.similarity(listOfResources1.get(i) , listOfResources2.get(i)));
        }

        //end timing
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("Comparing " + listOfResources1.size() + " pairs from local RDF without multithreading finished in " + duration + " second(s) ");
        System.out.println(); 


        engine.close();
        
    }
    
}
