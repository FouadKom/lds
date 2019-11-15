/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.engine;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;
import lds.LdManager.LdManagerBase;
import lds.benchmark.LdBenchmark;
import lds.indexing.LdIndexer;
import lds.measures.LdSimilarityMeasure;
import lds.measures.Measure;
import lds.resource.LdResourceTriple;
import lds.resource.LdResult;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class SimilarityCompareTask implements Callable<String> {
    private LdSimilarityMeasure measure;
    private LdResourceTriple triple;
    private String resultsFilePath = null;
    
    public SimilarityCompareTask(LdSimilarityMeasure measure , LdResourceTriple triple){
        this.measure = measure;
        this.triple = triple;
    }
    
    public SimilarityCompareTask(LdSimilarityMeasure measure , LdResourceTriple triple , String resultsFilePath){
        this.measure = measure;
        this.triple = triple;
        this.resultsFilePath = resultsFilePath;
    }
    
    /*public SimilarityCompareTask(Measure measureName, Conf config  , R  r1 , R r2){
        Class<?> measureClass;
        LdSimilarityMeasure ldMeasure = null;
        try {
            
            measureClass = Class.forName(Measure.getPath(measureName));
            Constructor<?> measureConstructor = measureClass.getConstructor(Conf.class);
            ldMeasure = (LdSimilarityMeasure) measureConstructor.newInstance(config);
            this.measure = ldMeasure;

        } 
        catch (Exception e) {
                e.printStackTrace();
        }
        this.resource1 = r1;
        this.resource2 = r2;
    }*/
    
    
    
//    @Override
//    public String call() {
//        double score = 0;
//        score = measure.compare(resource1, resource2);
//        return resource1.getUri().stringValue() + " , " + resource2.getUri().stringValue() + "|" + score;
//        
//    }
    
    @Override
    public String call() throws IOException {
        double startTime , endTime , duration = 0;
        double similarityResult = 0;
        
//        
//        startTime = System.nanoTime();
//        score = measure.compare(resource1, resource2);
//        endTime = System.nanoTime();
//        duration = (endTime - startTime) / 1000000000 ;
//        return resource1.getUri().stringValue() + " , " + resource2.getUri().stringValue() + " , " + score + " , " + duration;

        

       startTime = System.nanoTime();

       similarityResult = measure.compare(triple.getResourcePair().getFirstresource() , triple.getResourcePair().getSecondresource());

       endTime = System.nanoTime();
       duration = (endTime - startTime) / 1000000000 ;

       triple.setSimilarityResult(similarityResult);
       LdResult result = new LdResult(triple , duration);

       if(resultsFilePath != null){
            LdBenchmark.writeResultsToFile(result , resultsFilePath);
       }
      
        
       return result.toString();
        
    }

}
