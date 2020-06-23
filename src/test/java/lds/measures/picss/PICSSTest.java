/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.picss;

import lds.dataset.LdDatasetCreator;
import lds.engine.LdSimilarityEngine;
import lds.measures.LdConfFactory;
import lds.measures.Measure;
import lds.resource.R;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;
import test.utility.Util;

/**
 *
 * @author Fouad Komeiha
 */
public class PICSSTest {
    
    
    @Test
    public void PICSSTest() throws Exception{
        
        LdDataset dataset = LdDatasetCreator.getDBpediaDataset();
                
        //Create conf objects and pass needed configuration parameters
        Conf config = new Conf();
        //using indexes for calculation, change to false of no data indexing is wanted
        config.addParam("useIndexes", false);
        
        //specifying the main dataset that will be used for querying, in our case DBpedia
        config.addParam("LdDatasetMain" , dataset);
        
        //specifiying the number of resources -only resources and not literals- found in the dataset to be used in calculation
        config.addParam("resourcesCount" , 2350906);
        
        
        //Otherwise you can create default conf 
 //     Conf config = LdConfFactory.createDeafaultConf(Measure.PICSS);
        
        R r1 = new R("http://dbpedia.org/resource/Paris");
        R r2 = new R("http://dbpedia.org/resource/New_York");
        
        
        //Initialzie the engine class object
        LdSimilarityEngine engine = new LdSimilarityEngine();
        
        //creates a new similarity class object and passes the config that contains necessary parameters to it, also loads needed indexes if necessary
        //PICSS similarity calculaton

        engine.load(Measure.PICSS , config);        
        /* Note: calling engine.load(Measure.PICSS) creates a default conf and executes the similarity
                 No need to create a conf object with this method
        */
        
        System.out.println( engine.similarity(r1 , r2) );
        
        //ends calculation for the chosen similaarity and closes all indexes if created
        engine.close();
        
        
    }
    
}