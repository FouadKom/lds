/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lds.indexing.LdIndex;
import lds.indexing.LdIndexer;
import lds.resource.R;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class PicssLdManagerO extends HybridMeasuresLdManagerO {
    
    private boolean useIndex;
        
    private LdIndex ingoingFeaturesIndex;
    private LdIndex outgoingFeaturesIndex;
    private LdIndex countIngoingFeaturesIndex;
    private LdIndex countOutgoingFeaturesIndex;
    private LdIndex countResourcesIndex;
    
    private LdIndexer manager;
    
     
    public PicssLdManagerO(LdDataset dataset , boolean useIndex) throws Exception {                
            super(dataset);
            this.useIndex = useIndex;             
    }
    
    public void loadIndexes() throws Exception{
        manager = LdIndexer.getManager();
        
        String ingoingFeaturesIndexFile = System.getProperty("user.dir") + "/Indexes/Opt/PICSS/picss_ingoingFeatures_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String outgoingFeaturesIndexFile = System.getProperty("user.dir") + "/Indexes/Opt/PICSS/picss_outgoingFeatures_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countOutgoingFeaturesIndexFile = System.getProperty("user.dir") + "/Indexes/Opt/PICSS/picss_countOutgoingFeatures_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countIngoingFeaturesIndexFile = System.getProperty("user.dir") + "/Indexes/Opt/PICSS/picss_countIngoingFeatures_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
//        String countResourcesIndexFile = System.getProperty("user.dir") + "/Indexes/All_Measures/countResources_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        
        ingoingFeaturesIndex = manager.loadIndex(ingoingFeaturesIndexFile);
        outgoingFeaturesIndex = manager.loadIndex(outgoingFeaturesIndexFile);
        countIngoingFeaturesIndex = manager.loadIndex(countOutgoingFeaturesIndexFile);
        countOutgoingFeaturesIndex = manager.loadIndex(countIngoingFeaturesIndexFile);
//        countResourcesIndex = new LdIndexer_(countResourcesIndexFile);
            
    }
    
    public void closeIndexes(){
        if (useIndex) {
            
            manager.closeIndex(ingoingFeaturesIndex);
            manager.closeIndex(outgoingFeaturesIndex);
            manager.closeIndex(countIngoingFeaturesIndex);
            manager.closeIndex(countOutgoingFeaturesIndex);            
            
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
              return ingoingFeaturesIndex.getListFromIndex(dataset , Utility.createKey(a) , baseClassPath + "getIngoingFeatures" , a );
        }

        return super.getIngoingFeatures(a);
    }
   
   
    @Override
    public List<String> getOutgoingFeatures(R a){
       if (useIndex) {
             return outgoingFeaturesIndex.getListFromIndex(dataset , Utility.createKey(a) , baseClassPath + "getOutgoingFeatures" , a );
       }

       return super.getOutgoingFeatures(a);
    }
    

    @Override
    public int getIngoingFeatureFrequency(String property, String resource) {
        if (useIndex) {
             return countIngoingFeaturesIndex.getIntegerFromIndex(dataset , property + "|" + resource , baseClassPath + "getIngoingFeatureFrequency" , property , resource );
       }

       return super.getIngoingFeatureFrequency(property , resource);
    }
    

    @Override
    public int getOutgoingFeatureFrequency(String property, String resource) {
        if (useIndex) {
             return countOutgoingFeaturesIndex.getIntegerFromIndex(dataset , property + "|" + resource , baseClassPath + "getOutgoingFeatureFrequency" , property , resource );
       }

       return super.getOutgoingFeatureFrequency(property , resource);
    }
    
    @Override /////////////////////////////////////////////////// to be checked for correctness
    public int countResource(){
         if (useIndex) {
               return countResourcesIndex.getIntegerFromIndex(dataset , "resources" , baseClassPath + "countResource" );
         }
         
         return super.countResource();
    }
    
}