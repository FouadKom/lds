/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;

import lds.benchmark.BenchmarkFile;
import lds.benchmark.Correlation;
import lds.benchmark.LdBenchmark;
import lds.conf.LdConfFactory;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import org.junit.Test;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class rg65Test {
    
static String sourcepath = System.getProperty("user.dir") + "/src/test/resources/benchmarks/rg-65_DBpedia.txt";
    
    @Test
    public void rg65Test() throws Exception{

        LdSimilarityEngine engine = new LdSimilarityEngine();
        
        BenchmarkFile source = new BenchmarkFile(sourcepath , ',' , '"');
        /* for normalizing the benchmark values between 0 and 1 */
        source.setMaxValue(3);
        source.setMinValue(0);
        /**/
        
        BenchmarkFile result = new BenchmarkFile(System.getProperty("user.dir") + "/src/test/resources/benchmarks/rg-65_Results_EPICS.csv" , ',' , '"');        
        
        LdBenchmark benchmark = new LdBenchmark(source , result);
        benchmark.setCorrelationMethod(Correlation.PearsonCorrelation);
        
        Conf config = LdConfFactory.createDeafaultConf(Measure.EPICS);
//        config.addParam("threadsNumber" , 1);        
//        config.addParam("extendingMeasure" , "LDSD_dw");

        engine.load(Measure.EPICS , config);
                       
        System.out.println("EPICS Pearson Correlation: " + engine.correlation(benchmark , true));
        
        benchmark.setCorrelationMethod(Correlation.SpearmanCorrelation);
        
        System.out.println("EPICS Spearman Correlation: " + engine.correlation(benchmark , true));
        
        engine.close();
        
//EPICS Pearson Correlation: 0.013169011235697005
//EPICS Spearman Correlation: 0.10704886108930826
        
        /* PICSS */
        result = new BenchmarkFile(System.getProperty("user.dir") + "/src/test/resources/benchmarks/rg-65_Results_PICSS.csv" , ',' , '"');
        benchmark = new LdBenchmark(source , result);
        
        benchmark.setCorrelationMethod(Correlation.PearsonCorrelation);
        
        config = LdConfFactory.createDeafaultConf(Measure.PICSS);
        
        engine.load(Measure.PICSS , config);
        
        System.out.println("PICSS Pearson Correlation: " + engine.correlation(benchmark , true));
        
        benchmark.setCorrelationMethod(Correlation.SpearmanCorrelation);
        
        System.out.println("PICSS Spearman Correlation: " + engine.correlation(benchmark , true));
        
        engine.close(); 
        
//PICSS Pearson Correlation: 0.22996896621386245
//PICSS Spearman Correlation: 0.1798779482887808
    }
}