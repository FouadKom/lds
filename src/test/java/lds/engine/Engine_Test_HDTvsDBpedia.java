/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.engine;

import java.util.List;
import java.util.concurrent.ExecutionException;
import lds.dataset.LdDatasetCreator;
import lds.measures.Measure;
import lds.resource.R;
import static org.junit.Assert.fail;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;
import test.utility.Util;

/**
 *
 * @author Fouad Komeiha
 */
public class Engine_Test_HDTvsDBpedia {
    public static final String dataSetDir = System.getProperty("user.dir") + "/src/test/resources/dbpedia2016-04en.hdt";
     
    @Test
    public void Engine_Test_HDTvsDBpedia() throws InterruptedException, ExecutionException, Exception{
            
            double startTime , endTime , duration;
            
            LdDataset dbpedia = LdDatasetCreator.getDBpediaDataset();
            
            LdDataset hdt = LdDatasetCreator.getDBpediaHDTDataset(dataSetDir , "HDTDbpedia");
            
            Util.SplitedList sp = Util.splitList(LdDatasetCreator.getDbpediaResources(20));

            //get two list of Dbpedia resources
            List<R> listOfResources1 = sp.getFirstList();
            List<R> listOfResources2 = sp.getSecondList();
            
            Conf config = new Conf();
            LdSimilarityEngine engine = new LdSimilarityEngine();
            
            ///////////////////////LDSD_cw////////////////////////////////////////////////////////////////////////
            config.addParam("useIndexes", false);
            config.addParam("LdDatasetMain" , dbpedia);
            
            double sum = 0;
            engine.load(Measure.LDSD_cw, config);
            
            startTime = System.nanoTime();
            
            for(int i = 0 ; i < listOfResources1.size() ; i++){
               sum = sum + engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
            }
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("Comparing " + listOfResources1.size() + " pairs using LDSD_dw from DBpedia finished in " + duration + " second(s) ");
            System.out.println("Sum comparing = " + sum);
            System.out.println();
            
            
            engine.close();
            
            config.addParam("LdDatasetMain" , hdt);
            
            engine.load(Measure.LDSD_cw, config);
            
            startTime = System.nanoTime();
            sum = 0;
            for(int i = 0 ; i < listOfResources1.size() ; i++){
                sum = sum + engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
            }
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("Comparing " + listOfResources1.size() + " pairs using LDSD_dw from hdt finished in " + duration + " second(s) ");
            System.out.println("Sum comparing = " + sum);
            System.out.println();
            
            engine.close();
            
            ///////////////////////Resim////////////////////////////////////////////////////////////////////////   
/*            config.addParam("LdDatasetMain" , dbpedia);
            engine.load(Measure.Resim, config);
            
            startTime = System.nanoTime();
            
            for(int i = 0 ; i < listOfResources1.size() ; i++){
                engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
            }
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("Comparing " + listOfResources1.size() + " pairs using Resim from Dbpedia finished in " + duration + " second(s) ");
            System.out.println();
            
            
            engine.close();
            
            config.addParam("LdDatasetMain" , hdt);
            engine.load(Measure.Resim, config);
            
            startTime = System.nanoTime();
            
            for(int i = 0 ; i < listOfResources1.size() ; i++){
                engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
            }
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("Comparing " + listOfResources1.size() + " pairs using Resim from hdt finished in " + duration + " second(s) ");
            System.out.println();
            
            engine.close();
            ///////////////////////PICSS///////////////////////////////////////////////////////////////////////
            config.addParam("resourcesCount" , 2350906);
            config.addParam("useIndexes", false);
            config.addParam("LdDatasetMain" , dbpedia);
            
            engine.load(Measure.PICSS, config);
            
            startTime = System.nanoTime();
            
            for(int i = 0 ; i < listOfResources1.size() ; i++){
                engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
            }
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("Comparing " + listOfResources1.size() + " pairs using PICSS from Dbpedia finished in " + duration + " second(s) ");
            System.out.println();
            
            
            engine.close();
            
            config.addParam("LdDatasetMain" , hdt);
            engine.load(Measure.PICSS, config);
            
            startTime = System.nanoTime();
            
            for(int i = 0 ; i < listOfResources1.size() ; i++){
                engine.similarity(listOfResources1.get(i) , listOfResources2.get(i));
            }
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("Comparing " + listOfResources1.size() + " pairs using PICSS from hdt finished in " + duration + " second(s) ");
            System.out.println();
            
            
            engine.close();
    */       

	}
    
}
