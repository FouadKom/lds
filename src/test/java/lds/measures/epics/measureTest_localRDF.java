/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;

import lds.config.Config;
import lds.config.ConfigParam;
import lds.dataset.LdDatasetCreator;
import lds.measures.picss.PICSS;
import static lds.measures.resim.ResimTest_localRdf.dataSetDir;
import lds.resource.R;
import org.junit.Test;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class measureTest_localRDF {
    
    @Test
    public void runEngineOnSpecificLdMeasureTest() throws Exception{  
        LdDataset dataSet = LdDatasetCreator.getLocalDataset(dataSetDir , "example");

        R r1 = new R("http://www.example.org#Fish");
        R r2 = new R("http://www.example.org#Whale");

        Config config = new Config();
        config.addParam(ConfigParam.useIndexes, false);
        config.addParam(ConfigParam.LdDatasetMain , dataSet);
        config.addParam(ConfigParam.resourcesCount , 9); 
        
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
