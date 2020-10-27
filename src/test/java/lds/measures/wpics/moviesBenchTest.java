/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.wpics;

import lds.benchmark.BenchmarkFile;
import lds.benchmark.Correlation;
import lds.benchmark.LdBenchmark;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.config.LdConfigFactory;
import lds.dataset.LdDatasetCreator;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import lds.measures.weight.WeightMethod;
import org.junit.Test;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class moviesBenchTest {
    
    public static String sourcepath = System.getProperty("user.dir") + "/src/test/resources/benchmarks/Movies_bench.txt";
    public static String datasetDir = System.getProperty("user.dir") + "/src/test/resources/specific_class_set.rdf"; 
    
//    @Test
//    public void moviesBenchTest() throws Exception{
    public static void main(String args[]) throws Exception{
        /* WPICS */
        LdSimilarityEngine engine = new LdSimilarityEngine();
        
        BenchmarkFile source = new BenchmarkFile(sourcepath , ',' , '"');
        
        BenchmarkFile result = new BenchmarkFile(System.getProperty("user.dir") + "/src/test/resources/benchmarks/Movies_bench_Results_WPICS.csv" , ',' , '"'); 
        
        
        LdBenchmark benchmark = new LdBenchmark(source , result);
        benchmark.setCorrelationMethod(Correlation.PearsonCorrelation);
        
        Config config = LdConfigFactory.createDeafaultConf(Measure.WPICS);
        LdDataset dataSetSpecific = LdDatasetCreator.getLocalDataset(datasetDir, "LDSD_example"); 
        config.addParam(ConfigParam.LdDatasetSpecific , dataSetSpecific);
        config.removeParam(ConfigParam.WeightMethod);
        config.addParam(ConfigParam.WeightMethod , WeightMethod.ITW);

        engine.load(Measure.WPICS , config);
                       
        System.out.println("WPICS Pearson Correlation: " + engine.correlation(benchmark , true));
        
        benchmark.setCorrelationMethod(Correlation.SpearmanCorrelation);
        
        System.out.println("WPICS Spearman Correlation: " + engine.correlation(benchmark , true));
        
        engine.close();
        
        
        /* PICSS */
        result = new BenchmarkFile(System.getProperty("user.dir") + "/src/test/resources/benchmarks/Movies_bench_Results_PICSS.csv" , ',' , '"');
        benchmark = new LdBenchmark(source , result);
        
        benchmark.setCorrelationMethod(Correlation.PearsonCorrelation);
        
        engine.load(Measure.PICSS , config);
        
        System.out.println("PICSS Pearson Correlation: " + engine.correlation(benchmark , true));
        
        benchmark.setCorrelationMethod(Correlation.SpearmanCorrelation);
        
        System.out.println("PICSS Spearman Correlation: " + engine.correlation(benchmark , true));
        
        engine.close();
        

          

    }
    

}
