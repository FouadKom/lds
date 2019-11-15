/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lds.engine.SimilarityCompareTask;
import lds.engine.SimilarityCompareTaskRunnable;
import lds.measures.LdSimilarityMeasure;
import lds.measures.Measure;
import lds.measures.ldsd.LDSD;
import lds.measures.ldsd.LDSD_cw;
import lds.measures.ldsd.LDSD_d;
import lds.measures.ldsd.LDSD_dw;
import lds.measures.ldsd.LDSD_iw;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import lds.resource.LdResourcePair;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;

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
    
    public static List<String> similarFeatures(List<String> a, List<String> b , Conf config) throws Exception {
        List<String> result = new ArrayList<>();
        String node_a , node_b ,link_a , link_b , direction_a , direction_b;
        double sim;
        
        LdDataset dataset = (LdDataset) config.getParam("LdDatasetMain");
        boolean useIndex = (Boolean) config.getParam("useIndexes");
        
        Conf ldsd_conf = new Conf();
        ldsd_conf.addParam("useIndexes", useIndex);
        ldsd_conf.addParam("LdDatasetMain" , dataset);        
        LDSD ldsd = new LDSD_cw(ldsd_conf);
        ldsd.loadIndexes();
        
        
        /*Class<?> measureClass;
        LdSimilarityMeasure ldMeasure = null;
        try {
                measureClass = Class.forName(Measure.getPath(measureName));
                Constructor<?> measureConstructor = measureClass.getConstructor(Conf.class);
                ldMeasure = (LdSimilarityMeasure) measureConstructor.newInstance(config);
//                    this.measure = ldMeasure;
                ldMeasure.loadIndexes();


        } 
        catch (Exception e) {
                e.printStackTrace();
        }*/
       
//        HashMap<String, String> map_a = new HashMap();
//        HashMap<String, String> map_b = new HashMap();
//        
//        for(String fa : a){
//           node_a = getVertex(fa);
//           link_a = getLink(fa);
//           direction_a = getDirection(fa);
//           map_a.put(node_a , link_a + "|" + direction_a);
//        }
//        
//        for(String fb : b){
//           node_b = getVertex(fb);
//           link_b = getLink(fb);
//           direction_b = getDirection(fb);
//           map_b.put(node_b , link_b + "|" + direction_b);
//        }
//        
//        
//        for (Entry entry : map_a.entrySet()) {
//            String f = entry.getValue().toString();
//            
//            
//            if(map_b.containsValue(f)){
//                String node_1 = entry.getKey().toString();
//                
//            }
//            
//        }
           SearchTask[] threads = new SearchTask[a.size()];
           
           int i = 0;
           
           for(String fa: a){
                threads[i] = new SearchTask(ldsd.getMeasure() , fa , b , result);
                threads[i].start();
                
                i++;
           }
           
           try{
               for(int j = 0 ; j < i ; j++){
                   threads[j].join();
               }
           }catch(InterruptedException ie) {
               ie.printStackTrace();
           }
        
//        for(String fa : a){
//           link_a = getLink(fa);
//           direction_a = getDirection(fa);
//           
//           for (String fb : b) {
//                link_b = getLink(fb);
//                direction_b = getDirection(fb);
//
//                if(link_a.equals(link_b) && direction_a.equals(direction_b)){
//
////                    sim = checkFeatureSimilarity(getVertex(fa) , getVertex(fb) , config);
//                    R r1 = LdResourceFactory.getInstance().uri(getVertex(fa)).create();
//                    R r2 = LdResourceFactory.getInstance().uri(getVertex(fb)).create();
//        
//                    sim = ldsd.compare(r1 , r2);
//
//                    if( sim >= 0.5){
//                        result.add(fa);
//                        result.add(fb);
//                    }
//                }
//            
//            } 
//        }

        ldsd.closeIndexes();
//        ldMeasure.closeIndexes();
        return result;
        
    }
    
//    public static double checkFeatureSimilarity(String a , String b , Conf config) throws Exception{
//        
//        LdDataset dataset = (LdDataset) config.getParam("LdDatasetMain");
//        boolean useIndex = (Boolean) config.getParam("useIndexes");
//        
//        Conf ldsd_conf = new Conf();
//        ldsd_conf.addParam("useIndexes", useIndex);
//        ldsd_conf.addParam("LdDatasetMain" , dataset);
//        
//        double sim = 0;
//        LDSD ldsd = new LDSD_d(ldsd_conf);
//        ldsd.loadIndexes();
//        
//        
//        R r1 = LdResourceFactory.getInstance().uri(a).create();
//        R r2 = LdResourceFactory.getInstance().uri(b).create();
//        
//        sim = ldsd.compare(r1 , r2);
//        ldsd.closeIndexes();
//        
//        return sim;
//        
//        
//    }
    
    public static List<String> commonFeatures(List<String> a, List<String> b){
        List<String> result = new ArrayList<>(a);
        
        result.retainAll(b);
        
        return result;
    }
    
    public static String getLink(String s){
          String string[] =  s.split("\\|");
          return string[0].trim();
    }
    
    public static String getVertex(String s){
          String string[] =  s.split("\\|");
          return string[1].trim();
    }
    
    public static String getDirection(String s){
        String string[] =  s.split("\\|");
        return string[2].trim();
    }	 
    
	
    // Function that matches input str with 
    // given wildcard pattern 
    static boolean strmatch(String str, String pattern, int n, int m) 
    { 
            // empty pattern can only match with 
            // empty string 
            if (m == 0) 
                    return (n == 0); 

            // lookup table for storing results of 
            // subproblems 
            boolean[][] lookup = new boolean[n + 1][m + 1]; 

            // initailze lookup table to false 
            for(int i = 0; i < n + 1; i++) 
                    Arrays.fill(lookup[i], false); 


            // empty pattern can match with empty string 
            lookup[0][0] = true; 

            // Only '*' can match with empty string 
            for (int j = 1; j <= m; j++) 
                    if (pattern.charAt(j - 1) == '*') 
                            lookup[0][j] = lookup[0][j - 1]; 

            // fill the table in bottom-up fashion 
            for (int i = 1; i <= n; i++) 
            { 
                    for (int j = 1; j <= m; j++) 
                    { 
                            // Two cases if we see a '*' 
                            // a) We ignore '*'' character and move 
                            // to next character in the pattern, 
                            //	 i.e., '*' indicates an empty sequence. 
                            // b) '*' character matches with ith 
                            //	 character in input 
                            if (pattern.charAt(j - 1) == '*') 
                                    lookup[i][j] = lookup[i][j - 1] || 
                                                            lookup[i - 1][j]; 

                            // Current characters are considered as 
                            // matching in two cases 
                            // (a) current character of pattern is '?' 
                            // (b) characters actually match 
                            else if (pattern.charAt(j - 1) == '?' || 
                                    str.charAt(i - 1) == pattern.charAt(j - 1)) 
                                    lookup[i][j] = lookup[i - 1][j - 1]; 

                            // If characters don't match 
                            else lookup[i][j] = false; 
                    } 
            } 

            return lookup[n][m]; 
    } 


     
}
