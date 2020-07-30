/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.picss;

import lds.config.Config;
import lds.config.ConfigParam;
import static lds.dataset.LdDatasetCreator.getDBpediaHDTDataset;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import org.junit.Test;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class PICSSTest_hdtFiles {
    
    
    @Test
    public void isPICSSWorksCorrectlyOnPaperExample() throws Exception{
        
        LdDataset dataset = getDBpediaHDTDataset("/src/test/resources/dbpedia2016-04en.hdt" , "example");

        Config config = new Config();
        config.addParam(ConfigParam.useIndexes, false);
        config.addParam(ConfigParam.LdDatasetMain , dataset);
        config.addParam(ConfigParam.resourcesCount , 2350906);
        
        R r1 = new R("http://dbpedia.org/resource/Bob_Dylan");
        R r2 = new R("http://dbpedia.org/resource/Ronnie_Hawkins");
        
        PICSS picss = new PICSS(config);
        
        picss.loadIndexes();
        
        System.out.println(picss.compare(r1, r2));
        
        picss.closeIndexes();
        
        
    }
    
}

