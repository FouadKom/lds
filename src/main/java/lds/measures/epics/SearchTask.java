/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import lds.LdManager.ontologies.Ontology;
import lds.measures.LdSimilarity;
import static lds.measures.epics.Utility_v1.getDirection;
import static lds.measures.epics.Utility_v1.getLink;
import static lds.measures.epics.Utility_v1.getVertex;
import lds.resource.LdResourceFactory;
import lds.resource.R;

/**
 *
 * @author Fouad komeiha
 */
public class SearchTask implements Callable<List<String>>{
    private String feature;
    private List<String> list;
    private List<String> result;
    private LdSimilarity measure;
    
    public SearchTask(LdSimilarity measure , String feature , List<String> list ){
        this.feature = feature;
        this.list = list;
        this.result = new ArrayList<>();
        this.measure = measure;
        
    }
    
    @Override
    public List<String> call() {
        String link_b , direction_b , node_b ,  link_a , direction_a , node_a;
        double sim = 0;
        
        link_a = Ontology.decompressValue(getLink(feature));
        node_a = Ontology.decompressValue(getVertex(feature));
        direction_a = getDirection(feature);
    
        for(String fb : list){
            link_b = Ontology.decompressValue(getLink(fb));
            direction_b = Ontology.decompressValue(getDirection(fb));
            node_b = getVertex(fb);

            if(link_a.equals(link_b) && direction_a.equals(direction_b)){
               R r1 = LdResourceFactory.getInstance().uri(node_a).create();
               R r2 = LdResourceFactory.getInstance().uri(node_b).create();
               
               
//                try{
//                    Thread.sleep(60000);
//                }
//                catch(InterruptedException e){
//                    System.out.println(e);
//                }
                
                synchronized(this){
                    sim = measure.compare(r1 , r2);
                }
                
                if( sim >= 0.5){
                   
                   result.add(feature);
                   result.add(fb);
                   
               }
            }
        }
        
        return result;
    }
    
}
