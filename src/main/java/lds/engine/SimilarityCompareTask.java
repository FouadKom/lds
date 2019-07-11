/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.engine;

import java.util.concurrent.Callable;
import lds.measures.LdSimilarityMeasure;
import lds.resource.R;

/**
 *
 * @author Fouad Komeiha
 */
public class SimilarityCompareTask implements Callable<String> {
    private LdSimilarityMeasure measure;
    private R resource1;
    private R resource2;
    
    public SimilarityCompareTask(LdSimilarityMeasure measure , R  r1 , R r2){
        this.measure = measure;
        this.resource1 = r1;
        this.resource2 = r2;
    }
    
    @Override
    public String call() {
        double score = 0;
        score = measure.compare(resource1, resource2);
        return resource1.getUri().stringValue() + " , " + resource2.getUri().stringValue() + "|" + score;
        
    }
    
}
