/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.LODS;

import lds.config.Config;
import lds.config.ConfigParam;
import lds.dataset.LdDatasetCreator;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import lds.resource.R;
import org.junit.Test;
import ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class SimP_Test {
    
    @Test
    public void SimI_Test() throws Exception{
        LdDataset dataSetMain = LdDatasetCreator.getDBpediaDataset();

        Config config = new Config();
        config.addParam(ConfigParam.useIndexes, true);
        config.addParam(ConfigParam.LdDatasetMain , dataSetMain);
        config.addParam(ConfigParam.resourcesCount , 2350906);        
        
        R r1 = new R("http://dbpedia.org/resource/Coast");
        R r2 = new R("http://dbpedia.org/resource/Shore");
        
        //Initialzie the engine class object
        LdSimilarityEngine engine = new LdSimilarityEngine();
        
        //creates a new similarity class object and passes the config that contains necessary parameters to it, also loads needed indexes if necessary
        //PICSS similarity calculaton
        engine.load(Measure.SimP , config);        
        System.out.println( engine.similarity(r1 , r2) );
        
        //ends calculation for the chosen similaarity and closes all indexes if created
        engine.close();
    }
    
}
