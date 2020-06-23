/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.benchmark;

import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;
import test.utility.Util;

/**
 *
 * @author Fouad Komeiha
 */
public class writingResults_Test {
    public static final String resourcesFileText = System.getProperty("user.dir") + "/src/test/resources/Test.txt";
    public static final String resourcesFileCsv = System.getProperty("user.dir") + "/src/test/resources/missing_resources_2.csv";
    
    @Test
    public void test() throws Exception{
//    public static void main(String args[]) throws Exception{

        LdDataset dataSet = Util.getDBpediaDataset();
        
        LdSimilarityEngine engine = new LdSimilarityEngine();
        
        BenchmarkFile source = new BenchmarkFile(resourcesFileText , ',' , '"');
          
        LdBenchmark benchmark = new LdBenchmark(source);
        
        Conf config = new Conf();
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , dataSet);

        engine.load(Measure.LDSD_cw, config);
        
        engine.similarity(benchmark , false);
        
        engine.close();
    }
}
