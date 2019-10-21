/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import lds.measures.Measure;
import static lds.measures.resim.ResimTest_localRdf.dataSetDir;
import lds.resource.R;
import lds.resource.ResourcePair;
import static org.junit.Assert.fail;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class Engine_Multithread_Test_LocalRDF {
    public static final String dataSetDir = System.getProperty("user.dir") + "/src/test/resources/data.rdf";
    
    public static void main(String args[]) throws InterruptedException, ExecutionException{
        double startTime , endTime , duration;
        
        LdDataset dataSet = null;

        R r1 = new R("http://www.example.org#Fish");
        R r2 = new R("http://www.example.org#Whale");
        
        R r3 = new R("http://www.example.org#Bear");
        R r4 = new R("http://www.example.org#Cat");
        
        R r5 = new R("http://www.example.org#Mammal");
        R r6 = new R("http://www.example.org#Fish");
        
        R r7 = new R("http://www.example.org#Animal");
        R r8 = new R("http://www.example.org#Mammal");
        
        R r9 = new R("http://www.example.org#Water");
        R r10 = new R("http://www.example.org#Whale");
        
        R r11 = new R("http://www.example.org#Bear");
        R r12 = new R("http://www.example.org#Vertebra");

        try {
                dataSet = LdDatasetFactory.getInstance().name("example").file(dataSetDir)
                                .defaultGraph("http://graphResim/dataset").create();

        } catch (Exception e) {
                fail(e.getMessage());
        }

        Conf config = new Conf();
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , dataSet);
        
        List<R> listOfResources1 = new ArrayList<>();
        
        listOfResources1.add(r1);
        listOfResources1.add(r3);
        listOfResources1.add(r5);
        listOfResources1.add(r7);
        listOfResources1.add(r9);
        listOfResources1.add(r11);
        
        List<R> listOfResources2 = new ArrayList<>();
        
        listOfResources2.add(r2);
        listOfResources2.add(r4);
        listOfResources2.add(r6);
        listOfResources2.add(r8);
        listOfResources2.add(r10);
        listOfResources2.add(r12);
        
        List<ResourcePair> pairs = new ArrayList<>();

        for(int i = 0 ; i < listOfResources1.size() ; i++){
            ResourcePair pair = new ResourcePair(listOfResources1.get(i) , listOfResources2.get(i));
            pairs.add(pair);
        }
        
        LdSimilarityEngine engine = new LdSimilarityEngine();

        engine.load(Measure.Resim , config);


        startTime = System.nanoTime();

        Map<String , Double> results = engine.similarity(pairs);          


            for( Map.Entry<String,Double> entry : results.entrySet()){
                    System.out.println( entry.getKey() + ":" + entry.getValue());
            }

        //end timing
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("Comparing " + pairs.size() + " pairs from local RDF using multithreading finished in " + duration + " second(s) ");
        System.out.println();

        startTime = System.nanoTime();

        for(int i = 0 ; i < listOfResources1.size() ; i++){
            System.out.println(listOfResources1.get(i).getUri().toString() + " , " + listOfResources2.get(i).getUri().toString() + " : " + engine.similarity(listOfResources1.get(i) , listOfResources2.get(i)));
        }

        //end timing
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("Comparing " + listOfResources1.size() + " pairs from local RDF without multithreading finished in " + duration + " second(s) ");
        System.out.println();


        engine.close();
        
    }
    
}
