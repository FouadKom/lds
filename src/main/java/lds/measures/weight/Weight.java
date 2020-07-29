/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.weight;


import java.util.List;
import lds.LdManager.DistanceMeasuresLdManager;
import lds.indexing.LdIndex;
import lds.indexing.LdIndexerManager;
import lds.resource.R;
import org.openrdf.model.URI;

/**
 *
 * @author Fouad Komeiha
 */
public class Weight {
    private static WeightMethod method;
    private static DistanceMeasuresLdManager datasetLoader;
    private static DistanceMeasuresLdManager specificClassLoader;   
    private static LdIndex weightsIndex;
    private static LdIndexerManager manager;
    private static boolean useIndex;
    
    private String baseClassPath = "lds.measures.weight.Weight.";
                                    
    
    
    public Weight(WeightMethod method , DistanceMeasuresLdManager ldManagerMain , DistanceMeasuresLdManager ldManagerSpecific , boolean useIndexes) throws Exception{
        this.method = method;
        this.datasetLoader = ldManagerMain;
        this.specificClassLoader = ldManagerSpecific;
        this.useIndex = useIndexes;
    }
    
    
    //Used for reflection in LdSimilarityEngine
    public Weight() throws Exception{
       
    } 
    
    
    public void loadIndexes() throws Exception {
        manager = LdIndexerManager.getManager();
        String weightsIndexFile = System.getProperty("user.dir") + "/Indexes/Weights/weight_index.db";
        weightsIndex = manager.loadIndex(weightsIndexFile);
        
               
    }
    
    public void closeIndexes(){
        if(useIndex)
           manager.closeIndex(weightsIndex);
    }
    
    public double linkWeight(URI l , R a , R b) {
        double weight = 0;
        if (useIndex) {
            if(this.method == WeightMethod.RSLAW){
                weight = weightsIndex.getDoubleFromIndex(l.stringValue() , baseClassPath + "calculateWeights_RSLAW" , a , b);
                

            }
            else if(this.method == WeightMethod.ITW){
                weight = weightsIndex.getDoubleFromIndex(a.getUri().toString()+ ":" + l.stringValue()+ ":" + b.getUri().toString()  , baseClassPath + "calculateWeights_ITW" , a , b);
            }
            
     
           
           return weight;
        }
        
        else{
            if(this.method == WeightMethod.RSLAW){
                return RSLAW(l);

            }
            else if(this.method == WeightMethod.ITW){
                double w = 0 , min = 0 , max = 0;
                
                min = getMinWeight(a , b);
                max = getMaxWeight(a , b);
                w = ITW(l , a , b);
                return rescaleWeight(w , min , max);
            }
        }
        return 1;
    }    
    
    public double RSLAW(URI link){ 
        return (double) specificClassLoader.countPropertyOccurrence(link)/datasetLoader.countPropertyOccurrence(link);
        
    }
    
    public double ITW(URI link , R a , R b){     
        double linkFrequency = 0 , inverseResourceFrequency = 0 , x = 0 , y =0;
        
        if(specificClassLoader.countPropertyOccurrence(link) == 0)
            return 0;
        
        x = (double) ( datasetLoader.countObject(link, a) + datasetLoader.countSubject(link , a) ) / ( datasetLoader.countSubject(a) + datasetLoader.countObject(a) ) ;
       
        
        y = (double) ( datasetLoader.countObject(link, b) + datasetLoader.countSubject(link , b) ) / ( datasetLoader.countSubject(b) + datasetLoader.countObject(b) ) ;
        
        
        linkFrequency =  ( x + y ) / 2;
        
        
        inverseResourceFrequency = Math.log10((double) specificClassLoader.countResource()/specificClassLoader.countPropertyOccurrence(link));
        
       
        return linkFrequency * inverseResourceFrequency;
        
    }
    
    //to rescale weights between [0 - 1] needed only for ITW weight method
    public double rescaleWeight(double weight , double min , double max){
        return (weight - min)/(max - min);
    }
    
    public void calculateWeights_RSLAW(R a , R b){
        double weight = 0;
        
        for(URI edge:datasetLoader.getEdges(a, b)){
            weight = RSLAW(edge);                
            weightsIndex.addValue(edge.stringValue() , Double.toString(weight));              
                
        }
    }
    
    public void calculateWeights_ITW(R a , R b){
        
        List<URI> edges = datasetLoader.getEdges(a, b);
        double min = ITW(edges.iterator().next() , a , b) , max = 0 , weight = 0;
        
        for(URI edge:datasetLoader.getEdges(a, b)){
            weight = ITW(edge , a , b);
                
            if (weight > max){
                max = weight;
            }

            if(weight < min){
                min = weight;
            }

            weightsIndex.addValue("minWeight:" + a.getUri().stringValue() + ":" + b.getUri().stringValue() , Double.toString(min));
            weightsIndex.addValue("maxWeight:" + a.getUri().stringValue() + ":" + b.getUri().stringValue() , Double.toString(max));

        }
            
        for(URI edge:datasetLoader.getEdges(a, b)){
            double w = ITW(edge , a , b);
            weight = rescaleWeight(w  , min , max);
            weightsIndex.addValue(a.getUri().stringValue() + ":" + edge.stringValue() + ":" + b.getUri().stringValue() , Double.toString(weight));
            

        }
        
    }
    
    public double getMinWeight(R a , R b){
        double min = 0 , weight = 0;
        List<URI> edges = datasetLoader.getEdges(a, b);
        
        if(useIndex){
            min = weightsIndex.getDoubleFromIndex("minWeight:" + a.getUri().stringValue() + ":" + b.getUri().stringValue() , "lds.measures.resim.Weight.calculateWeights_ITW" , a , b);
        }
        
        else{
            min = ITW(edges.iterator().next() , a , b);
            
            for(URI edge: edges){
                weight = ITW(edge , a , b);

                
                if(weight < min){
                    min = weight;
                }
            }
        }
        
        return min;         
    }
    
    public double getMaxWeight(R a , R b){
        double max = 0, weight = 0;
        
        if(useIndex){
            max = weightsIndex.getDoubleFromIndex("maxWeight:" + a.getUri().stringValue() + ":" + b.getUri().stringValue() , "lds.measures.resim.Weight.calculateWeights_ITW" , a , b);
        }
        else{
            for(URI edge:datasetLoader.getEdges(a, b)){
                weight = ITW(edge , a , b);

                if(weight > max){
                    max = weight;
                }
            }
        }
        
        return max;         
    }
    
}
