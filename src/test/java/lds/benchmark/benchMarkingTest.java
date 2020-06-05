package lds.benchmark;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;
import test.utility.Util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Fouad Komeiha
 */
public class benchMarkingTest {
    
//    public static void main(String args[]) throws Exception{
//        LdDataset dataSet = Util.getDBpediaDataset();
//        
//        Conf config = new Conf();
//        config.addParam("useIndexes", true);
//        config.addParam("LdDatasetMain" , dataSet);
//        
//        LdSimilarityEngine engine = new LdSimilarityEngine();
//        
//        BenchmarkFile file = new BenchmarkFile("C:\\Users\\LENOVO\\Documents\\NetBeansProjects\\lds\\src\\test\\resources\\missing_resources_2.csv" , ' ' , '"');
//        
//        LdBenchmark_R benchmark = new LdBenchmark_R(file , Benchmark.rg65);
//
//        engine.load(Measure.LDSD_cw  ,config);
//        
//        engine.correlation(benchmark);
//        
//        engine.close();
//        
//    }
}
