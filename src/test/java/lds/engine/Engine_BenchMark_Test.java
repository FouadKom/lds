/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.engine;

import lds.benchmark.BenchmarkFile;
import lds.measures.Measure;
import sc.research.ldq.LdDataset;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.dataset.LdDatasetCreator;
import org.junit.Test;
/**
 *
 * @author Fouad Komeiha
 */
public class Engine_BenchMark_Test {
    
    @Test
    public void runEngineOnSpecificLdMeasureTest() throws Exception {           
        
        LdDataset dataSetMain = LdDatasetCreator.getDBpediaDataset();
            
        Config config = new Config();
        config.addParam(ConfigParam.useIndexes, false);
        config.addParam(ConfigParam.LdDatasetMain , dataSetMain);
        
        LdSimilarityEngine engine = new LdSimilarityEngine();

        engine.load(Measure.LDSD_cw , config);
        
        BenchmarkFile sourceFile = new BenchmarkFile("C:\\Users\\LENOVO\\Downloads\\test.txt");
        
        engine.similarity(sourceFile , 1 , true);
        
        engine.close();
       
        
        
        
    }
    
}
