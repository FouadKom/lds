/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.benchmark;

import lds.config.Config;
import lds.config.ConfigParam;
import lds.dataset.LdDatasetCreator;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import org.junit.Test;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class writingResults_Test {
    public static final String resourcesFileText = System.getProperty("user.dir") + "/src/test/resources/Test.txt";
    public static final String resourcesFileCsv = System.getProperty("user.dir") + "/src/test/resources/missing_resources_2.csv";
    
    @Test
    public void test() throws Exception{

        LdDataset dataSet = LdDatasetCreator.getDBpediaDataset();
        
        LdSimilarityEngine engine = new LdSimilarityEngine();
        
        BenchmarkFile source = new BenchmarkFile(resourcesFileText , ',' , '"');
          
        LdBenchmark benchmark = new LdBenchmark(source);
        
        Config config = new Config();
        config.addParam(ConfigParam.useIndexes, false);
        config.addParam(ConfigParam.LdDatasetMain , dataSet);

        engine.load(Measure.LDSD_cw, config);
        
        engine.similarity(benchmark , false);
        
        engine.close();
    }
}
