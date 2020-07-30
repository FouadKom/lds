/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.dataset.DBpediaChapter;
import lds.dataset.LdDatasetCreator;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import lds.measures.resim.Resim;
import lds.measures.resim.ResourceSimilarity;
import lds.resource.R;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;
import test.utility.Util;

/**
 *
 * @author Fouad Komeiha
 */
public class ResimTest_Dbpedia_calculationDurtion_Test {
    public static File Indexesfolder = new File(System.getProperty("user.dir") + "/Indexes/Resim");
    
    public static void Resim_calculationDuration_Test(int numberOfPairs){
            
            double startTime , endTime , duration;
        
            Util.SplitedList sp = Util.splitList(LdDatasetCreator.getDbpediaResources(DBpediaChapter.En , numberOfPairs * 2));

            //get two list of Dbpedia resources
            List<R> listOfResources1 = sp.getFirstList();
            List<R> listOfResources2 = sp.getSecondList();

            LdDataset dataset = LdDatasetCreator.getDBpediaDataset();
            Config config = new Config();
            config.addParam(ConfigParam.useIndexes, false);
            config.addParam(ConfigParam.LdDatasetMain , dataset);
            
            try {
            //Checking Time to calculate similarity using Dbpedia       
            //start timing
            startTime = System.nanoTime();        
        
            Resim resim = new Resim(config);
            
            resim.loadIndexes();

            for (int i = 0; i < numberOfPairs; i++) {

                R r1 = listOfResources1.get(i);
                R r2 = listOfResources2.get(i);

                resim.compare(r1, r2);
   

            }
            
            resim.closeIndexes();

            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("Comparing " + numberOfPairs + " pairs using Dbpedia finished in " + duration + " second(s)");
            System.out.println();



            //updating Indexes

            Util.DeleteFilesForFolder(Indexesfolder , false);
            //start timing
            startTime = System.nanoTime();
            config.removeParam(ConfigParam.useIndexes);
            config.addParam(ConfigParam.useIndexes, true);
            
            resim.loadIndexes();

            for (int i = 0; i < numberOfPairs; i++) {

                R r1 = listOfResources1.get(i);
                R r2 = listOfResources2.get(i);

                resim.compare(r1, r2);

            }
            resim.closeIndexes();

            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000  ;
            System.out.println("Index creation/updating for " + numberOfPairs + " pairs finished in " + duration + " second(s)");
            System.out.println();        

            //Checking Time to calculate similarity using Indexes
            //start timing
            startTime = System.nanoTime();

            config.removeParam(ConfigParam.useIndexes);
            config.addParam(ConfigParam.useIndexes, true);
            
            resim.loadIndexes();

            for (int i = 0; i < numberOfPairs; i++) {

                R r1 = listOfResources1.get(i);
                R r2 = listOfResources2.get(i);

                resim.compare(r1, r2);

            }

            resim.closeIndexes();

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
    
    @Test
    public void isResimWorksCorrectlyOnPaperExample() {
                
          int startPairNumbers = 10;
          int endPairNumbers = 10;
          int incrementBy = 2;
          
          while(startPairNumbers <= endPairNumbers){
              Resim_calculationDuration_Test(startPairNumbers);
              startPairNumbers = startPairNumbers + incrementBy;
              System.out.println("---------------------------------------");
          }
    
    }
}
