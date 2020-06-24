/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;

import java.io.File;
import java.util.Date;
import lds.LdManager.EpicsLdManager;
import lds.dataset.LdDatasetCreator;
import lds.resource.R;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;
import test.utility.Util;

/**
 *
 * @author Fouad Komeiha
 */
public class EnhancingTest {
    
    
    @Test
    public void OptimizationTest() throws Exception{
        
        LdDataset dataset = LdDatasetCreator.getDBpediaDataset();
        
        R r1 = new R("http://dbpedia.org/resource/Automobile");
        R r2 = new R("http://dbpedia.org/resource/Car");
        
        double startTime , endTime , duration;
        
        Conf config = new Conf();
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , dataset);
        config.addParam("resourcesCount" , 2350906);
        config.addParam("extendingMeasure" , "LDSD_dw");
        
        EPICS epics = new EPICS(config);
        EPICS epics_e = new EPICS(config);
        
        epics.loadIndexes();
        epics_e.loadIndexes();        
        
        startTime = System.nanoTime();
        
        System.out.println(epics_e.compare(r1, r2));
        
        //end timing
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("finished in " + duration + " second(s)");
        
        startTime = System.nanoTime(); 
        
        System.out.println(epics.compare(r1, r2));  
        
        //end timing
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("finished in " + duration + " second(s)");
        System.out.println();
        
        
        
        epics.closeIndexes();
        epics_e.closeIndexes();
        
        
    }
}
