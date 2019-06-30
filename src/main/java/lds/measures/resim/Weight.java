/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;


import lds.LdManager.LdManager;
import lds.indexing.LdIndexer;
import lds.resource.R;
import org.openrdf.model.URI;

/**
 *
 * @author Fouad Komeiha
 */
public class Weight {
    private static WeightMethod method;
    private static LdManager datasetLoader;
    private static LdManager specificClassLoader;   
    private static LdIndexer weightsIndex;
    private static boolean useIndex;
    
    
    public Weight(WeightMethod method , LdManager ldManagerMain , LdManager ldManagerSpecific , boolean useIndexes) throws Exception{
        this.method = method;
        this.datasetLoader = ldManagerMain;
        this.specificClassLoader = ldManagerSpecific;
        this.useIndex = useIndexes;
    }
    
    /*public Weight(Conf config) throws Exception{
        this.method = (WeightMethod) config.getParam("WeightMethod");
        this.datasetLoader = (LdManager) config.getParam("LdManagerMain");
        this.specificClassLoader = (LdManager) config.getParam("LdManagerSpecific");
        this.useIndex = (Boolean) config.getParam("useIndexes");
        if(useIndex)
            loadIndexes();
    }*/
    
    //Used for reflection in LdSimilarityEngine
    public Weight() throws Exception{
       
    }    
    
    public void loadIndexes() throws Exception {
        String weightsIndexFile = System.getProperty("user.dir") + "/Indexes/Weights/weight_index.db";
        weightsIndex = new LdIndexer(weightsIndexFile);
               
    }
    
    public void closeIndexes(){
        if(useIndex)
            weightsIndex.close();
    }
    
    public double linkWeight(URI l , R a , R b) {
        double weight = 0;
        if (useIndex) {
            if(this.method == WeightMethod.RSLAW){
                weight = LdIndexer.getDoubleFromIndex(weightsIndex , l.stringValue() , "lds.measures.resim.Weight.calculateWeights_RSLAW" , a , b);
                

            }
            else if(this.method == WeightMethod.ITW){
                weight = LdIndexer.getDoubleFromIndex(weightsIndex , a.getUri().toString()+ ":" + l.stringValue()+ ":" + b.getUri().toString()  , "lds.measures.resim.Weight.calculateWeights_ITW" , a , b);
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
                return rescaleWeight(min , max , w);
            }
        }
        return 1;
    }   
    
    public double RSLAW(URI link){ 
        return (double) specificClassLoader.countPropertyOccurrence(link)/datasetLoader.countPropertyOccurrence(link);
        
    }
    
    public double ITW(URI link , R a , R b){     
        double linkFrequency = 0 , inverseResourceFrequency = 0 , x = 0 , y =0;
        
        x = (double) ( datasetLoader.countObject(link, a) + datasetLoader.countSubject(link , a) ) / ( datasetLoader.countSubject(a) + datasetLoader.countObject(a) ) ;
       
        
        y = (double) ( datasetLoader.countObject(link, b) + datasetLoader.countSubject(link , b) ) / ( datasetLoader.countSubject(b) + datasetLoader.countObject(b) ) ;
        
        
        linkFrequency =  ( x + y ) / 2;
        
        if(specificClassLoader.countPropertyOccurrence(link) != 0)
                
            inverseResourceFrequency = Math.log10((double) specificClassLoader.countResource()/specificClassLoader.countPropertyOccurrence(link));
        
        else
            
            inverseResourceFrequency = 1;
        
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
        double min = 0 , max = 0 , weight = 0;
        
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
        double min = 0, weight = 0;
        
        if(useIndex){
            min = LdIndexer.getDoubleFromIndex(weightsIndex , "minWeight:" + a.getUri().stringValue() + ":" + b.getUri().stringValue() , "lds.measures.resim.Weight.calculateWeights_ITW" , a , b);
        }
        
        else{
            for(URI edge:datasetLoader.getEdges(a, b)){
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
            max = LdIndexer.getDoubleFromIndex(weightsIndex , "maxWeight:" + a.getUri().stringValue() + ":" + b.getUri().stringValue() , "lds.measures.resim.Weight.calculateWeights_ITW" , a , b);
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
