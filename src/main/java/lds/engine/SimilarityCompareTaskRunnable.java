/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.engine;

import java.lang.reflect.Constructor;
import lds.indexing.LdIndexer_;
import lds.measures.Measure;
import lds.resource.R;
import slib.utils.i.Conf;
import lds.measures.LdSimilarity;

/**
 *
 * @author Fouad Komeiha
 */
public class SimilarityCompareTaskRunnable extends Thread{
    
    private LdSimilarity measure;
    private R resource1;
    private R resource2;
    private String sim;
    private LdIndexer_ resultsIndex;

    
    
    public SimilarityCompareTaskRunnable(LdSimilarity measure , R  r1 , R r2 , LdIndexer_ resultsIndex) throws Exception{
        this.measure = measure;
        this.resource1 = r1;
        this.resource2 = r2;
        this.resultsIndex = resultsIndex;
        
    }

    
//    public SimilarityCompareTaskRunnable(Measure measureName, Conf config  , R  r1 , R r2 , LdIndexer_ index){
//        Class<?> measureClass;
//        LdSimilarity ldMeasure = null;
//        try {
//
//            measureClass = Class.forName(Measure.getPath(measureName));
//            Constructor<?> measureConstructor = measureClass.getConstructor(Conf.class);
//            ldMeasure = (LdSimilarity) measureConstructor.newInstance(config);
//            this.measure = ldMeasure;
//
//        } 
//        catch (Exception e) {
//                e.printStackTrace();
//        }
//        this.resource1 = r1;
//        this.resource2 = r2;
//        
//        this.resultsIndex = index;
//    }

    @Override
    public void run() {
        double score = 0;
        score = measure.compare(resource1, resource2);
        this.sim = resource1.getUri().stringValue() + " , " + resource2.getUri().stringValue() + "|" + score;
        resultsIndex.addValue(resource1.getUri().stringValue() + " , " + resource2.getUri().stringValue() , sim);
        
    }
    
}
