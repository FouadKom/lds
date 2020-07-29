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
public class ws353Test {
    static String sourcepath = System.getProperty("user.dir") + "/src/test/resources/benchmarks/wikipediaSimilarity-353/wikipediaSimilarity353_DBpedia.csv";
    
    @Test
    public void ws353Test() throws Exception{

        LdSimilarityEngine engine = new LdSimilarityEngine();
        
        BenchmarkFile source = new BenchmarkFile(sourcepath , ',' , '"');
        /* for normalizing the benchmark values between 0 and 1 */
        source.setMaxValue(10);
        source.setMinValue(0);
        /**/
        
        BenchmarkFile result = new BenchmarkFile(System.getProperty("user.dir") + "/src/test/resources/benchmarks/wikipediaSimilarity-353/wordsim-353_Results_EPICS.csv" , ',' , '"');        
        
        LdBenchmark benchmark = new LdBenchmark(source , result);
        
        benchmark.setCorrelationMethod(Correlation.PearsonCorrelation);
        
        
        Conf config = LdConfFactory.createDeafaultConf(Measure.EPICS);

        engine.load(Measure.EPICS , config);
        
        engine.similarity(benchmark, true);
                       
        System.out.println("EPICS Pearson Correlation: " + engine.correlation(benchmark , true));
        
        benchmark.setCorrelationMethod(Correlation.SpearmanCorrelation);
        
        System.out.println("EPICS Spearman Correlation: " + engine.correlation(benchmark , true));
        
        engine.close();

        
        /* PICSS */
        result = new BenchmarkFile(System.getProperty("user.dir") + "/src/test/resources/benchmarks/wikipediaSimilarity-353/wordsim-353_Results_PICSS.csv" , ',' , '"');
        benchmark = new LdBenchmark(source , result);
        
        benchmark.setCorrelationMethod(Correlation.PearsonCorrelation);
        
        engine.load(Measure.PICSS , config);
        
        engine.similarity(benchmark , true);
        
        System.out.println("PICSS Pearson Correlation: " + engine.correlation(benchmark , true));
        
        benchmark.setCorrelationMethod(Correlation.SpearmanCorrelation);
        
        System.out.println("PICSS Spearman Correlation: " + engine.correlation(benchmark , true));
        
        engine.close();

    }
}