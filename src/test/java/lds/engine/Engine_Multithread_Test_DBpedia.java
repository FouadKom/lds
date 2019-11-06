package lds.engine;

import test.utility.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.measures.Measure;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import lds.resource.ResourcePair;
import static org.junit.Assert.*;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import slib.utils.ex.SLIB_Ex_Critic;
import slib.utils.i.Conf;

public class Engine_Multithread_Test_DBpedia {
	public static final String dataSetDir = System.getProperty("user.dir") + "/src/test/resources/data.rdf";

//	@Test
//	public void runEngineOnSpecificLdMeasureTest() {        
        
        public static void main(String args[]) throws InterruptedException, ExecutionException, Exception{
            
            double startTime , endTime , duration;
            
            LdDataset dataSetMain = Util.getDBpediaDataset();
            
            Conf config = new Conf();
            config.addParam("useIndexes", true);
            config.addParam("LdDatasetMain" , dataSetMain);
            config.addParam("resourcesCount" , 2350906);
            
            Util.SplitedList sp = Util.splitList(Util.getDbpediaResources(100));

            //get two list of Dbpedia resources
            List<R> listOfResources1 = sp.getFirstList();
            List<R> listOfResources2 = sp.getSecondList();
            List<ResourcePair> pairs = new ArrayList<>();
            
//            List<R> listOfResources1 = new ArrayList<>();
//            List<R> listOfResources2 = new ArrayList<>();
//            List<ResourcePair> pairs = new ArrayList<>();
//            
//            
//            listOfResources1.add(LdResourceFactory.getInstance().uri("http://dbpedia.org/resource/Frank_Whittle").create());
//            listOfResources1.add(LdResourceFactory.getInstance().uri("http://dbpedia.org/resource/Franz_Joseph_I_of_Austria").create());
//            listOfResources1.add(LdResourceFactory.getInstance().uri("http://dbpedia.org/resource/Frans_Hals").create());
//            
//            listOfResources2.add(LdResourceFactory.getInstance().uri("http://dbpedia.org/resource/Franz_Mertens").create());
//            listOfResources2.add(LdResourceFactory.getInstance().uri("http://dbpedia.org/resource/Franz_Xaver_von_Baader").create());
//            listOfResources2.add(LdResourceFactory.getInstance().uri("http://dbpedia.org/resource/Franz_Schubert").create());
            
            
            
            for(int i = 0 ; i < listOfResources1.size() ; i++){
                ResourcePair pair = new ResourcePair(listOfResources1.get(i) , listOfResources2.get(i));
                pairs.add(pair);
            }
            
            LdSimilarityEngine engine = new LdSimilarityEngine();
            
            engine.load(Measure.PICSS, config);            
            
            startTime = System.nanoTime();
            
            Map<String , Double> results; 
            try {
                results = engine.similarity(pairs);
//            engine.similarity2(pairs);
          
            for( Map.Entry<String,Double> entry : results.entrySet()){
                    System.out.println( entry.getKey() + ":" + entry.getValue());
            }
            
             } catch (InterruptedException ex) {
                Logger.getLogger(Engine_Multithread_Test_DBpedia.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(Engine_Multithread_Test_DBpedia.class.getName()).log(Level.SEVERE, null, ex);
            }

            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("Comparing " + pairs.size() + " pairs from Dbpedia using multithreading finished in " + duration + " second(s) ");
            System.out.println();
            
            startTime = System.nanoTime();
            
            for(int i = 0 ; i < listOfResources1.size() ; i++){
//                engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
                System.out.println(engine.similarity(listOfResources1.get(i) , listOfResources2.get(i)));
            }
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("Comparing " + listOfResources1.size() + " pairs from Dbpedia without multithreading finished in " + duration + " second(s) ");
            System.out.println();
            
            
            engine.close();
            

	}
        
        

}
