/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.indexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Fouad Komeiha
 */
public class LdIndexManager {
//    private static List<LdIndex> indexes;
    private static Map<String, LdIndex> indexes;
    
    static private LdIndexManager indexManager;
    
    private LdIndexManager(){
//        indexes = new ArrayList<>();
        indexes = new HashMap<>();
    }
    
    public static LdIndexManager getManager() {
        if(indexManager == null){
            indexManager = new LdIndexManager();            
        }        
        return indexManager;
    }
    
    public LdIndex loadIndex(String indexFilePath) throws Exception{
        LdIndex index = null;
        
//        if(!containsIndex(indexFilePath)){
//           index = new LdIndex(indexFilePath);
//           indexes.add(index);
//        }
//        
//        index = getIndex(indexFilePath);
//
//        return index;

        if(!indexes.containsKey(indexFilePath)){
            index = new LdIndex(indexFilePath);
            indexes.put(indexFilePath , index);
        }
        
        index = indexes.get(indexFilePath);
        return index;
    }
    
    public void closeIndex(LdIndex index){
        if(indexes.containsKey(index.getDBPath())){
            index.close();
            indexes.remove(index.getDBPath());
        }

    }
    
    public void closeAllIndexes(){
        this.indexes = null;
    }
    
//    private boolean containsIndex(String indexFilePath){
//        for(LdIndex index: indexes){
//            if(index.getDBPath().equals(indexFilePath))
//                return true;
//        }
//        
//        return false;
//    }
//    
//    private LdIndex getIndex(String indexFilePath){
//        for(LdIndex index: indexes){
//            if(index.getDBPath().equals(indexFilePath))
//                return index;
//        }
//        
//        return null;
//    }
    
    
}
