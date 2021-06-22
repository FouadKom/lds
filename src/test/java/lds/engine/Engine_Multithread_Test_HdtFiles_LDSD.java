/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.engine;

import java.util.concurrent.ExecutionException;
import lds.benchmark.BenchmarkFile;
import lds.measures.Measure;
import static org.junit.Assert.fail;
import ldq.LdDataset;
import ldq.LdDatasetFactory;
import org.junit.Test;
import lds.config.Config;
import lds.config.ConfigParam;


/**
 *
 * @author Fouad Komeiha
 */
public class Engine_Multithread_Test_HdtFiles_LDSD {
    public static final String dataSetDir = System.getProperty("user.dir") + "/src/test/resources/dbpedia2016-04en.hdt";
    public static final String resourcesFilePath1 = System.getProperty("user.dir") + "/src/test/resources/facebook_book_resources_LDSD.txt";
    public static final String resourcesFilePath2 = System.getProperty("user.dir") + "/src/test/resources/yahoo_movies_resources.txt";


   @Test
    public void engine_multithread_test_hdtfiles() throws InterruptedException , ExecutionException , Exception {
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
        
//        Util.SplitedList sp = Util.splitList(Util.getLocalResources(10 , dataSetDir));
//        List<R> listOfResources1 = sp.getFirstList();
//        List<R> listOfResources2 = sp.getSecondList();
//        
//        List<LdResourcePair> pairs = new ArrayList<>();
//
//        for(int i = 0 ; i < listOfResources1.size() ; i++){
//            LdResourcePair pair = new LdResourcePair(listOfResources1.get(i) , listOfResources2.get(i));
//            pairs.add(pair);
//        }       
        
        Config config = new Config();
        config.addParam(ConfigParam.useIndexes, false);
        config.addParam(ConfigParam.LdDatasetMain , dataSet);
        
        BenchmarkFile sourceFile = new BenchmarkFile(resourcesFilePath1);
        
        LdSimilarityEngine engine = new LdSimilarityEngine();

        engine.load(Measure.LDSD_cw  ,config);
        
        startTime = System.nanoTime();
        
        engine.similarity(sourceFile , 4 , false);
        
        //end timing
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("Comparing with multithreading finished in " + duration + " second(s) ");
        System.out.println(); 
        
        startTime = System.nanoTime();
        
        engine.similarity(sourceFile , 1 , false);
        
        //end timing
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("Comparing without multithreading finished in " + duration + " second(s) ");
        System.out.println(); 


        engine.close();
        
    }
    
}
