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
import lds.indexing.LdIndexerManager;
import lds.resource.R;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class SimPLdManager extends HybridMeasuresLdManager {
    protected boolean useIndex;
        
    private LdIndex ingoingFeaturesIndex;
    private LdIndex outgoingFeaturesIndex;
    protected LdIndex countIngoingFeaturesIndex;
    protected LdIndex countOutgoingFeaturesIndex;
    private LdIndex countResourcesIndex;
    
    private LdIndexerManager manager;

    public SimPLdManager(LdDataset dataset , boolean useIndex) throws Exception {
        super(dataset , useIndex);
    }
    
    @Override
    public void loadIndexes() throws Exception{
        super.loadIndexes();
        manager = LdIndexerManager.getManager();
        
        String outgoingFeaturesIndexFile = System.getProperty("user.dir") + "/Indexes/LODS/SimP/simp_outgoingFeatures_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countOutgoingFeaturesIndexFile = System.getProperty("user.dir") + "/Indexes/LODS/SimP/simp_countOutgoingFeatures_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countIngoingFeaturesIndexFile = System.getProperty("user.dir") + "/Indexes/LODS/SimP/simp_countIngoingFeatures_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        
        outgoingFeaturesIndex = manager.loadIndex(outgoingFeaturesIndexFile);
        countIngoingFeaturesIndex = manager.loadIndex(countOutgoingFeaturesIndexFile);
        countOutgoingFeaturesIndex = manager.loadIndex(countIngoingFeaturesIndexFile);
            
    }
    
    @Override
    public void closeIndexes(){
        super.closeIndexes();
        
        if (useIndex) {
            
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
    public List<String> getOutgoingFeatures(R a){
       if (useIndex) {
             return outgoingFeaturesIndex.getListFromIndex(dataset , Utility.createKey(a) , baseClassPath + "getOutgoingFeatures" , a );
       }

       return super.getOutgoingFeatures(a , false);
    }
    
    @Override
    public int getIngoingFeatureFrequency(String property) {
        if (useIndex) {
             return countIngoingFeaturesIndex.getIntegerFromIndex(dataset , property  , baseClassPath + "getIngoingFeatureFrequency" , property);
       }

       return super.getIngoingFeatureFrequency(property);
    }
    

    @Override
    public int getOutgoingFeatureFrequency(String property) {
        if (useIndex) {
             return countOutgoingFeaturesIndex.getIntegerFromIndex(dataset , property  , baseClassPath + "getOutgoingFeatureFrequency" , property);
       }

       return super.getOutgoingFeatureFrequency(property);
    }    
}
