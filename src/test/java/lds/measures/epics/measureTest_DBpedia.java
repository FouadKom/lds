/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;

import java.util.ArrayList;
import java.util.List;
import lds.dataset.LdDatasetCreator;
import lds.measures.picss.PICSS;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class measureTest_DBpedia {
    
    @Test
    public void runEngineOnSpecificLdMeasureTest() throws Exception{        
        LdDataset dataset = LdDatasetCreator.getDBpediaDataset();
        
        R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Plane").create();
        R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Car").create();
                
        Conf config = new Conf();
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , dataset);
        config.addParam("resourcesCount" , 2350906);
        config.addParam("threadsNumber" , 5);        
        config.addParam("extendingMeasure" , "LDSD_dw");
        
//        PICSS picss = new PICSS(config);
        EPICS_v1 epics = new EPICS_v1(config);
        
        
//        picss.loadIndexes();
        epics.loadIndexes();
        
//        System.out.println("PICSS(r1 , r2) = " + picss.compare(r1, r2));
        System.out.println("EPICS(r1 , r2) = " + epics.compare(r1, r2));
        
//        picss.closeIndexes();
        epics.closeIndexes();
    
    }
    
}
