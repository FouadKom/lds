/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.picss;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fouad Komeiha
 */
public class Utility {  
    
    public static List<String> uniqueFeatures(List<String> a, List<String> b) {
        List<String> result = new ArrayList<>(a);
        
        for (String f : b) {
            if (result.contains(f)) {
                result.remove(f);
            }
        }
        
        return result;
    }
    
    public static List<String> commonFeatures(List<String> a, List<String> b){
        List<String> result = new ArrayList<>(a);
        
        result.retainAll(b);
        
        return result;
    }
    
    public static String getLink(String s){
          String string[] =  s.split("\\|");
          if(string.length < 3)
            return null;
          return string[0].trim();
    }
    
    public static String getVertex(String s){
          String string[] =  s.split("\\|");
          if(string.length < 3)
            return null;
          return string[1].trim();
    }
    
    public static String getDirection(String s){
        String string[] =  s.split("\\|");
        if(string.length < 3)
            return null;
        return string[2].trim();
    }
     
}
