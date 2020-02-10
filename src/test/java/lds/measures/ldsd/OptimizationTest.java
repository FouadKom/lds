/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.ldsd;

import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;
import test.utility.Util;

/**
 *
 * @author Fouad Komeiha
 */
public class OptimizationTest {
    public static final String resourcesFilePath = System.getProperty("user.dir") + "/src/test/resources/OptimTest/facebook_book_resources_LDSD.txt";
    
    @Test
    public void OptimizationTest() throws Exception{ 
        R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("The_Noah").create();
        R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("The_Pack_(2010_film)").create();
        double startTime , endTime , duration;
        
        LdDataset datasetMain = Util.getDBpediaDataset();
        
        Conf configSim = new Conf();            
        
        configSim.addParam("LdDatasetMain" , datasetMain);
        configSim.addParam("useIndexes" , true);
        
        /*LDSDO ldsd_o = new LDSD_cwO(configSim);
        LDSD ldsd = new LDSD_cw(configSim);
        ldsd_o.loadIndexes();
        ldsd.loadIndexes();
                
        startTime = System.nanoTime(); 
        
        System.out.println(ldsd_o.compare(r1, r2));  
        
        //end timing
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("finished in " + duration + " second(s)");
        System.out.println();
        
        startTime = System.nanoTime(); 
        
        System.out.println(ldsd.compare(r1, r2));
        
        //end timing
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("finished in " + duration + " second(s)");
        System.out.println();
        
        ldsd.closeIndexes();
        ldsd_o.closeIndexes();*/
        
        LdSimilarityEngine engine = new LdSimilarityEngine();

        engine.load(Measure.LDSD_cwO  ,configSim);
        
        startTime = System.nanoTime(); 
        
        engine.similarity(resourcesFilePath , false , false);
        
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("LDSD_cwO finished in " + duration + " second(s)");
        System.out.println();
        
        engine.close();
        
        engine.load(Measure.LDSD_cw  ,configSim);
        
        startTime = System.nanoTime(); 
        
        engine.similarity(resourcesFilePath , false , false);
        
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("LDSD_cw finished in " + duration + " second(s)");
        System.out.println();
        
        engine.close();
    }
    
}
