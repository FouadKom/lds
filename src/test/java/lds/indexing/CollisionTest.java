/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.indexing;


/**
 *
 * @author Fouad Komeiha
 */
public class CollisionTest {
    protected static LdIndex index;  
    protected static LdIndexer manager;
    static String indexFile = System.getProperty("user.dir") + "/Indexes/Prefixes/namespaces_index.db";
        
    
    public static void main(String args[]) throws Exception {
        manager = LdIndexer.getManager();
        
        index = manager.loadIndex(indexFile);

        String key1 = index.generateRandomKey(2);
        
        index.addValue(key1, "Value1");
        
        String key2 = index.generateRandomKey(2);
           
        index.addValue(key2, "Value2");        
        
        System.out.println(index.getValue(key1));

        System.out.println(index.getValue(key2));
        
        manager.closeIndex(index);
        
        
    }
    
}
