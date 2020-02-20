/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.picss;

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
    
    @Test
    public void OptimizationTest() throws Exception{ 

        R r1 = new R("http://dbpedia.org/resource/Paris");
        R r2 = new R("http://dbpedia.org/resource/London");
        
        double startTime , endTime , duration;
        
        LdDataset datasetMain = Util.getDBpediaDataset();
        
        Conf configSim = new Conf();            
        
        configSim.addParam("LdDatasetMain" , datasetMain);
        configSim.addParam("resourcesCount" , 2350906);
        configSim.addParam("useIndexes" , false);
        
        PICSSO picss_o = new PICSSO(configSim);
        PICSS picss = new PICSS(configSim);
        picss_o.loadIndexes();
        picss.loadIndexes();
         
        startTime = System.nanoTime(); 
        
        System.out.println(picss_o.compare(r1, r2));  
        
        //end timing
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("finished in " + duration + " second(s)");
        System.out.println();
        
        startTime = System.nanoTime();
        
        System.out.println(picss.compare(r1, r2));
        
        //end timing
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("finished in " + duration + " second(s)");
        

    }
    
}
