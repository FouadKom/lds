package lds.engine;

import test.utility.Util;
import java.util.List;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.dataset.LdDatasetCreator;
import lds.measures.Measure;
import lds.resource.R;
import org.junit.Test;
import ldq.LdDataset;

public class Engine_Multithread_Test_DBpedia {

	@Test
	public void runEngineOnSpecificLdMeasureTest() {        
            
            double startTime , endTime , duration;
            
            LdDataset dataSetMain = LdDatasetCreator.getDBpediaDataset();
            
            Util.SplitedList sp = Util.splitList(LdDatasetCreator.getDbpediaResources(20));

            //get two list of Dbpedia resources
            List<R> listOfResources1 = sp.getFirstList();
            List<R> listOfResources2 = sp.getSecondList();
            
            Config config = new Config();
            config.addParam(ConfigParam.useIndexes, false);
            config.addParam(ConfigParam.LdDatasetMain , dataSetMain);
            LdSimilarityEngine engine = new LdSimilarityEngine();
            
            ///////////////////////LDSD_cw////////////////////////////////////////////////////////////////////////
            double sum = 0;
            engine.load(Measure.LDSD_cw, config);
            
            startTime = System.nanoTime();
            
            for(int i = 0 ; i < listOfResources1.size() ; i++){
               sum = sum + engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
            }
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("Comparing " + listOfResources1.size() + " pairs using LDSD_dw without indexes finished in " + duration + " second(s) ");
            System.out.println("Sum comparing = " + sum);
            System.out.println();
            
            
            engine.close();
            
            config.addParam("useIndexes", true);
            
            engine.load(Measure.LDSD_cw, config);
            
            for(int i = 0 ; i < listOfResources1.size() ; i++){
                engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
                System.out.println(listOfResources1.get(i).getUri().stringValue() + " , " + listOfResources2.get(i).getUri().stringValue() + ": ");
            }
            
            engine.close();
            
            engine.load(Measure.LDSD_cw, config);
            
            startTime = System.nanoTime();
            sum = 0;
            for(int i = 0 ; i < listOfResources1.size() ; i++){
                sum = sum + engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
            }
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("Comparing " + listOfResources1.size() + " pairs using LDSD_dw with indexes finished in " + duration + " second(s) ");
            System.out.println("Sum comparing = " + sum);
            System.out.println();
            
            engine.close();
            
            ///////////////////////Resim////////////////////////////////////////////////////////////////////////   
 /*           config.addParam("useIndexes", false);
            engine.load(Measure.Resim, config);
            
            startTime = System.nanoTime();
            
            for(int i = 0 ; i < listOfResources1.size() ; i++){
                engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
            }
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("Comparing " + listOfResources1.size() + " pairs using Resim without indexing finished in " + duration + " second(s) ");
            System.out.println();
            
            
            engine.close();
            
            config.addParam("useIndexes", true);
            engine.load(Measure.Resim, config);
            
            for(int i = 0 ; i < listOfResources1.size() ; i++){
                engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
            }
            
            engine.close();
            
            engine.load(Measure.Resim, config);
            
            startTime = System.nanoTime();
            
            for(int i = 0 ; i < listOfResources1.size() ; i++){
                engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
            }
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("Comparing " + listOfResources1.size() + " pairs using Resim with indexing finished in " + duration + " second(s) ");
            System.out.println();
            
            engine.close();
            ///////////////////////PICSS///////////////////////////////////////////////////////////////////////
            config.addParam("resourcesCount" , 2350906);
            config.addParam("useIndexes", false);
            engine.load(Measure.PICSS, config);
            
            startTime = System.nanoTime();
            
            for(int i = 0 ; i < listOfResources1.size() ; i++){
                engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
            }
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("Comparing " + listOfResources1.size() + " pairs using PICSS without indexing finished in " + duration + " second(s) ");
            System.out.println();
            
            
            engine.close();
            
            config.addParam("useIndexes", true);
            engine.load(Measure.PICSS, config);
                        
            for(int i = 0 ; i < listOfResources1.size() ; i++){
                engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
            }            
            
            engine.close();
            
            engine.load(Measure.PICSS, config);
            
            startTime = System.nanoTime();
            
            for(int i = 0 ; i < listOfResources1.size() ; i++){
                engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
            }
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("Comparing " + listOfResources1.size() + " pairs using PICSS with indexing finished in " + duration + " second(s) ");
            System.out.println();
            
            
 */           engine.close();
           

	}
        
        

}
