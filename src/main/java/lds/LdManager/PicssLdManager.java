/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lds.indexing.LdIndexer;

import lds.resource.R;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class PicssLdManager extends HybridMeasuresLdManager{
    
    private boolean useIndex;
    
    private LdIndexer ingoingFeaturesIndex;
    private LdIndexer outgoingFeaturesIndex;
    private LdIndexer countIngoingFeaturesIndex;
    private LdIndexer countOutgoingFeaturesIndex;
    private LdIndexer countResourcesIndex; 
     
    public PicssLdManager(LdDataset dataset , boolean useIndex) throws Exception {                
            super(dataset);
            this.useIndex = useIndex;             
    }
    
    public void loadIndexes() throws Exception{
        
        String ingoingFeaturesIndexFile = System.getProperty("user.dir") + "/Indexes/PICSS/picss_ingoingFeatures_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String outgoingFeaturesIndexFile = System.getProperty("user.dir") + "/Indexes/PICSS/picss_outgoingFeatures_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countOutgoingFeaturesIndexFile = System.getProperty("user.dir") + "/Indexes/PICSS/picss_countOutgoingFeatures_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countIngoingFeaturesIndexFile = System.getProperty("user.dir") + "/Indexes/PICSS/picss_countIngoingFeatures_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
         String countResourcesIndexFile = System.getProperty("user.dir") + "/Indexes/All_Measures/countResources_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        
        ingoingFeaturesIndex = new LdIndexer(ingoingFeaturesIndexFile);
        outgoingFeaturesIndex = new LdIndexer(outgoingFeaturesIndexFile);
        countIngoingFeaturesIndex = new LdIndexer(countOutgoingFeaturesIndexFile);
        countOutgoingFeaturesIndex = new LdIndexer(countIngoingFeaturesIndexFile);
        countResourcesIndex = new LdIndexer(countResourcesIndexFile);
            
    }
    
    public void closeIndexes(){
        if (useIndex) {
            
            ingoingFeaturesIndex.close();
            outgoingFeaturesIndex.close();
            countIngoingFeaturesIndex.close();
            countOutgoingFeaturesIndex.close();
            
        }
        
    }
    
    public List<String> getFeatures(R a){
        List<String> IngoingStrings = new ArrayList<>();
        List<String> OutgoingStrings = new ArrayList<>();
        List<String> features_a = new ArrayList<>();
        
        IngoingStrings = getIngoingFeatures(a);
        OutgoingStrings = getOutgoingFeatures(a);
        
        Optional.ofNullable(IngoingStrings).ifPresent(features_a::addAll);
        Optional.ofNullable(OutgoingStrings).ifPresent(features_a::addAll);
        
        return features_a;
    }
    
    
    @Override
    public List<String> getIngoingFeatures(R a){
        if (useIndex) {
              return LdIndexer.getListFromIndex(dataset , ingoingFeaturesIndex , a.getUri().stringValue() , baseClassPath + "getIngoingFeatures" , a );
        }

        return super.getIngoingFeatures(a);
    }
   
   
    @Override
    public List<String> getOutgoingFeatures(R a){
       if (useIndex) {
             return LdIndexer.getListFromIndex(dataset , outgoingFeaturesIndex , a.getUri().stringValue() , baseClassPath + "getOutgoingFeatures" , a );
       }

       return super.getOutgoingFeatures(a);
    }
    

    @Override
    public int getIngoingFeatureFrequency(String property, String resource) {
        if (useIndex) {
             return LdIndexer.getIntegerFromIndex(dataset , countIngoingFeaturesIndex , property + ":" + resource , baseClassPath + "getIngoingFeatureFrequency" , property , resource );
       }

       return super.getIngoingFeatureFrequency(property , resource);
    }
    

    @Override
    public int getOutgoingFeatureFrequency(String property, String resource) {
        if (useIndex) {
             return LdIndexer.getIntegerFromIndex(dataset , countOutgoingFeaturesIndex , property + ":" + resource , baseClassPath + "getOutgoingFeatureFrequency" , property , resource );
       }

       return super.getOutgoingFeatureFrequency(property , resource);
    }
    
    @Override /////////////////////////////////////////////////// to be checked for correctness
    public int countResource(){
         if (useIndex) {
               return LdIndexer.getIntegerFromIndex(dataset , countResourcesIndex, "resources" , baseClassPath + "countResource" );
         }
         
         return super.countResource();
    }
    
}
