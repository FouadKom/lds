/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager;

import java.util.List;
import lds.indexing.LdIndex;
import lds.indexing.LdIndexerManager;
import lds.resource.R;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class HybridMeasuresLdManager extends HybridMeasuresLdManagerBase{
    
    protected boolean useIndex;
        
    protected LdIndex ingoingFeaturesIndex;
    protected LdIndex countResourcesIndex;
    
    private LdIndexerManager manager;
    
     
    public HybridMeasuresLdManager(LdDataset dataset , boolean useIndex) throws Exception {                
            super(dataset);
            this.useIndex = useIndex;             
    }
    
    public void loadIndexes() throws Exception{
        manager = LdIndexerManager.getManager();
        
        String ingoingFeaturesIndexFile = System.getProperty("user.dir") + "/Indexes/Hybrid_Measures/ingoingFeatures_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countResourcesIndexFile = System.getProperty("user.dir") + "/Indexes/Hybrid_Measures/countResources_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        
        ingoingFeaturesIndex = manager.loadIndex(ingoingFeaturesIndexFile);
        countResourcesIndex = manager.loadIndex(countResourcesIndexFile);
            
    }
    
    public void closeIndexes(){
        if (useIndex) {
            
            manager.closeIndex(ingoingFeaturesIndex);
            manager.closeIndex(countResourcesIndex);
            
        }
        
    }
    
    @Override
    public List<String> getIngoingFeatures(R a){
        if (useIndex) {
              return ingoingFeaturesIndex.getListFromIndex(dataset , Utility.createKey(a) , baseClassPath + "getIngoingFeatures" , a );
        }

        return super.getIngoingFeatures(a);
    }
   
    @Override /////////////////////////////////////////////////// to be checked for correctness
    public int countResource(){
         if (useIndex) {
               return countResourcesIndex.getIntegerFromIndex(dataset , "resources" , baseClassPath + "countResource" );
         }
         
         return super.countResource();
    }
   
//    @Override
//    public List<String> getOutgoingFeatures(R a){
//       if (useIndex) {
//             return outgoingFeaturesIndex.getListFromIndex(dataset , Utility.createKey(a) , baseClassPath + "getOutgoingFeatures" , a );
//       }
//
//       return super.getOutgoingFeatures(a);
//    }
//    
//    @Override
//    public List<String> getOutgoingFeatures(R a , boolean filterURI){
//       if (useIndex) {
//             return outgoingFeaturesIndex.getListFromIndex(dataset , Utility.createKey(a) , baseClassPath + "getOutgoingFeatures" , a );
//       }
//
//       return super.getOutgoingFeatures(a);
//    }
        
}
