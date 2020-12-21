/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;

import lds.config.Config;
import lds.measures.picss.PICSS;
import lds.resource.R;
import org.junit.Test;
import lds.config.LdConfigFactory;
import lds.measures.Measure;

/**
 *
 * @author Fouad Komeiha
 */
public class measureTest_DBpedia {
    
    @Test
    public void runEngineOnSpecificLdMeasureTest() throws Exception{        
        
        R r1 = new R("http://dbpedia.org/resource/Money");
        R r2 = new R("http://dbpedia.org/resource/Cash");
                
        Config config = LdConfigFactory.createDefaultConf(Measure.PICSS);
        
        PICSS picss = new PICSS(config);
        EPICS epics = new EPICS(config);
        
        
        picss.loadIndexes();
        epics.loadIndexes();
        
        System.out.println("PICSS(r1 , r2) = " + picss.compare(r1, r2));
        System.out.println("EPICS(r1 , r2) = " + epics.compare(r1, r2));
        
        picss.closeIndexes();
        epics.closeIndexes();
    
    }
    
}
