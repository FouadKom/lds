/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;

import lds.benchmark.BenchmarkFile;
import lds.benchmark.Correlation;
import lds.benchmark.LdBenchmark;
import lds.config.Config;
import lds.config.LdConfigFactory;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import org.junit.Test;

/**
 *
 * @author Fouad Komeiha
 */
public class mc30Test {
    
    static String sourcepath = System.getProperty("user.dir") + "/src/test/resources/benchmarks/mc-30/mc-30_DBpedia.csv";
    
    @Test
    public void mc30Test() throws Exception{  
        LdSimilarityEngine engine = new LdSimilarityEngine();
        
        BenchmarkFile source = new BenchmarkFile(sourcepath , ',' , '"');
        
//        /* for normalizing the benchmark values between 0 and 1 */
//        source.setMaxValue(4); 
//        source.setMinValue(0);
//        /**/
        
        BenchmarkFile result = new BenchmarkFile(System.getProperty("user.dir") + "/src/test/resources/benchmarks/mc-30/mc-30_Results_EPICS.csv" , ',' , '"');        
        
        LdBenchmark benchmark = new LdBenchmark(source , result);
        benchmark.setCorrelationMethod(Correlation.PearsonCorrelation);
        
        Config config = LdConfigFactory.createDefaultConf(Measure.EPICS);

        engine.load(Measure.EPICS , config);
                       
        System.out.println("EPICS Pearson Correlation: " + engine.correlation(benchmark , true));
        
        benchmark.setCorrelationMethod(Correlation.SpearmanCorrelation);
        
        System.out.println("EPICS Spearman Correlation: " + engine.correlation(benchmark , true));
        
        engine.close();
        

        
        /* PICSS */
        result = new BenchmarkFile(System.getProperty("user.dir") + "/src/test/resources/benchmarks/mc-30/mc-30_Results_PICSS.csv" , ',' , '"');
        benchmark = new LdBenchmark(source , result);
        
        benchmark.setCorrelationMethod(Correlation.PearsonCorrelation);
        
        engine.load(Measure.PICSS , config);
        
        System.out.println("PICSS Pearson Correlation: " + engine.correlation(benchmark , true));
        
        benchmark.setCorrelationMethod(Correlation.SpearmanCorrelation);
        
        System.out.println("PICSS Spearman Correlation: " + engine.correlation(benchmark , true));
        
        engine.close();  
        

          

    }
    

}
