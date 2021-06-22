/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lds.LdManager.ResimLdManager;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.dataset.DBpediaChapter;
import lds.dataset.LdDatasetCreator;
import test.utility.Util;
import lds.measures.resim.Resim;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import ldq.LdDataset;
import slib.utils.i.Conf;
import test.utility.Util.SplitedList;

/**
 *
 * @author Foouad Komeiha
 */
public class ResimTest_Dbpedia_valueEquality_Test {
    
    public static ResimLdManager resimLdManager;

    public static void Resim_valueEquality_Test(int pairNumbers) throws Exception{
        
        SplitedList sp = Util.splitList(LdDatasetCreator.getDbpediaResources(DBpediaChapter.En , pairNumbers * 2));

        //get two list of Dbpedia resources
        List<R> listOfResources1 = sp.getFirstList();
        List<R> listOfResources2 = sp.getSecondList();
        
        //create a map to hold the compared resources as a key and their similarity as a value
        Map<String, Double> list = new HashMap<>();
        
        LdDataset dataset = LdDatasetCreator.getDBpediaDataset();
        Config config = new Config();
        
        //Checking similarity using Dbpedia      
        config.addParam(ConfigParam.useIndexes, false);
        config.addParam(ConfigParam.LdDatasetMain , dataset);
        
        Resim resim = new Resim(config);
        
        resim.loadIndexes();
        
        for (int i = 0; i < pairNumbers; i++) {
            
            R r1 = listOfResources1.get(i);
            R r2 = listOfResources2.get(i);
       
            double value =  resim.compare(r1 , r2);
            
            list.put(r1.getUri().toString() + "," + r2.getUri().toString(), value);
          
        }              
        
        resim.closeIndexes();
        
        //Checking similarity using Indexes
        config.removeParam(ConfigParam.useIndexes);
        config.addParam(ConfigParam.useIndexes, false);
        config.addParam(ConfigParam.LdDatasetMain , dataset);
        resim = new Resim(config);
        
        resim.loadIndexes();
        
        for (Map.Entry<String, Double> entry : list.entrySet()) {
            String[] resourcePair = entry.getKey().split(",");

            R r1 = LdResourceFactory.getInstance().uri(resourcePair[0].trim()).create();
            R r2 = LdResourceFactory.getInstance().uri(resourcePair[1].trim()).create();

            double calcuValue = resim.compare(r1 , r2);
            double value = entry.getValue();
            
            try{
            assertEquals(value , calcuValue , 0.0);
            
            }
            catch(Exception e){
               System.out.println("Exception: " + e + " when comparing pairs " + entry.getKey());
               resim.closeIndexes();
            }

        }
        
        resim.closeIndexes();      
        
       
    }
    

    @Test
    public void isResimWorksCorrectlyOnPaperExample() throws FileNotFoundException, Exception {    
//    public static void main(String args[]) throws FileNotFoundException, Exception{
                
          int startPairNumbers = 2;
          int endPairNumbers = 4;
          int incrementBy = 2;
          
          while(startPairNumbers <= endPairNumbers){
              Resim_valueEquality_Test(startPairNumbers);
              startPairNumbers = startPairNumbers + incrementBy;
          }  
    
    }
}
