/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.indexing.LdIndexer;
import lds.resource.R;
import org.openrdf.model.URI;
import slib.graph.model.impl.repo.URIFactoryMemory;
import slib.graph.model.repo.URIFactory;

/**
 *
 * @author Fouad Komeiha
 */
public class Utility {
    
        public static Set<URI> toURI(List<String> list){
        Set<URI> listURI = new HashSet();
        URIFactory factory = URIFactoryMemory.getSingleton();
        if(list != null){
            for(String value:list){
                listURI.add(factory.getURI(value));
            }

            return listURI;
        }
        return null;
            
        }
        
        public static List<String> toList(Set<URI> list){
            List<String> listString = new ArrayList<>();
            if(list != null ){
                for(URI value:list){
                    listString.add(value.stringValue());
                }
                
                return listString;
            }
            return null;            
        }
        
        public static List<String> getListFromIndex(LdIndexer indexName , String key , String methodName , Object[] args) {
            
            List<String> data = indexName.getList(key);
            if(data != null){
                return data;
            }

            else{
                updateIndexSet(indexName , key , methodName , args);
                return getListFromIndex(indexName , key , methodName , args);
            }
           
        }
        
        public static int getIntegerFromIndex(LdIndexer indexName , String key , String methodName , Object[] args) {
                
            String data = indexName.getValue(key);
            
            if(data != null && ! data.equals("-1")){
                return Integer.parseInt(data);
            }
            else if(data != null && data.equals("-1")){
                return 0;
            }
            else{
                updateIndexTree(indexName , key , methodName , args);
                return getIntegerFromIndex(indexName , key , methodName , args);
            }

        }
        
        public static boolean getBooleanFromIndex(LdIndexer indexName , String key , String methodName , Object[] args) {
                
            String data = indexName.getValue(key);
            
            if(data != null && ! data.equals("-1")){
                return Boolean.parseBoolean(data);
            }
            else if(data != null && data.equals("-1")){
                return false;
            }
            else{
                updateIndexTree(indexName , key , methodName , args);
                return getBooleanFromIndex(indexName , key , methodName , args);
            }

        }
        
        public static void updateIndexSet(LdIndexer indexName, String key , String methodName , Object[] args){
 
            
            Object returnedItem = executeMethod("lds.LdManager.LdManagerBase" , methodName , args);
            
            try{
                if(returnedItem != null){
                        indexName.addList(key , (List<String>)  returnedItem);
                }

                else if(returnedItem == null){

                    List<String> list = new ArrayList<>();
                    list.add("-1");
                    indexName.addList(key , list);

                }
            }
            catch(Exception ex){
                Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
                indexName.close();
            }
                          
        }

        public static void updateIndexTree(LdIndexer indexName, String key, String methodName , Object[] args) {      

            Object returnedItem = executeMethod("lds.LdManager.LdManagerBase" , methodName , args);
            
            boolean boolValue = false;
            int intValue = 0;
            String value = null;
            
            try{
                if(returnedItem != null){
                    if(returnedItem instanceof Boolean){
                        boolValue = (Boolean) returnedItem;
                        value = Boolean.toString(boolValue);
                    }

                    if(returnedItem instanceof Integer){
                        intValue = (Integer) returnedItem;
                        value = Integer.toString(intValue);
                    }

                    indexName.addValue(key , value);                    
                }

                else if(returnedItem == null){
                    indexName.addValue(key , "-1");
                }
            }
            catch(Exception ex){
                Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
                indexName.close();
            }
        }
            
            
        private static Object executeMethod(String classPath , String methodName , Object[] args){
            
            Object returnedItem = null;
            try {  
                Class<?> params[] = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof R) {
                        params[i] = R.class;
                    } else if (args[i] instanceof URI) {
                        params[i] = URI.class;
                    }
                }
                
                Class<?> cls = Class.forName(classPath);
                Object _instance = cls.newInstance();
                Method method = cls.getDeclaredMethod(methodName, params);
                returnedItem = method.invoke(_instance , args);
            }
             catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException ex) {
                Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            
            return returnedItem;
        }
    }
            
        
   
