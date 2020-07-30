/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.indexing;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.resource.R;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad komeiha
 */
public class Utility {
    
    
    //Used only for Weight class 
    public static void executeMethod(String classPath , String methodName , Object... args){
            
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
                method.invoke(_instance , args);
            }
             catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException ex) {
                Logger.getLogger(LdIndexerManager.class.getName()).log(Level.SEVERE, null, ex);
 
            }
            
        }
    
    public static Object executeMethod(LdDataset dataset , String classPath , String methodName , Object... args) {
            
            Object returnedItem = null;
            
            try {  
                Class<?> params[] = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof R) {
                        params[i] = R.class;
                    } else if (args[i] instanceof URI) {
                        params[i] = URI.class;
                    } else if (args[i] instanceof String) {
                        params[i] = String.class;
                    } else if (args[i] instanceof Boolean) {
                        params[i] = boolean.class;
                    } else if (args[i] instanceof List) {
                        params[i] = List.class;
                    }
                }
                
                Class<?> cls = Class.forName(classPath);
                Constructor<?> cons = cls.getConstructor(LdDataset.class);
                Object _instance = cons.newInstance(dataset);
//                Object _instance = cls.newInstance();
                Method method = cls.getDeclaredMethod(methodName, params);
                returnedItem = method.invoke(_instance , args);
            }
             catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException ex) {
                Logger.getLogger(LdIndexerManager.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            
            return returnedItem;
        }
    
    
        public static boolean checkPath(String path){
            File file = new File(path);

            if (!file.isDirectory()){
               file = file.getParentFile();

                if (! file.exists()){
                    File dir = new File(file.getPath());
                    return dir.mkdirs();

                }
            }
            
            return true;
        }
        
    public static String getClassPath(String methodPath){

        return methodPath.substring(0, methodPath.lastIndexOf(".")).trim();
    }

    public static String getMethodName(String methodPath){

        return methodPath.substring(methodPath.lastIndexOf(".") + 1).trim();
    }
    
    public static String getAlphaNumericString(int n) { 
  
        // chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz"; 
  
        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(n); 
  
        for (int i = 0; i < n; i++) { 
  
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index = (int)(AlphaNumericString.length() * Math.random()); 
  
            // add Character one by one in end of sb 
            sb.append(AlphaNumericString.charAt(index)); 
        } 
  
        return sb.toString(); 
    } 
        
        
    
}
