/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;

import lds.LdManager.EpicsLdManager;
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
public class EnhancingTest {
    
    
    @Test
    public void OptimizationTest() throws Exception{
        
        LdDataset dataset = Util.getDBpediaDataset();
        
        R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Automobile").create();
        R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Car").create();
        
        double startTime , endTime , duration;
        
        Conf config = new Conf();
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , dataset);
        config.addParam("resourcesCount" , 2350906);
        config.addParam("extendingMeasure" , "LDSD_dw");
        
        EPICS epics = new EPICS(config);
        EPICSE epics_e = new EPICSE(config);
        
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
