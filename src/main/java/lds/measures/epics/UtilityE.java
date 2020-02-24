/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lds.LdManager.EpicsLdManager;
import lds.engine.SimilarityCompareTask;
import lds.engine.SimilarityCompareTaskRunnable;
import lds.measures.Measure;
import lds.measures.ldsd.*;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;
import lds.measures.LdSimilarity;
import static lds.measures.epics.Utility.getVertex;

/**
 *
 * @author Fouad Komeiha
 */
public class UtilityE {  
    
    public static List<String> uniqueFeatures(List<String> a, List<String> b) {
        List<String> result = new ArrayList<>(a);
        
        for (String f : b) {
            if (result.contains(f)) {
                result.remove(f);
            }
        }
        
        return result;
    }
    
    public static List<String> similarFeatures(R r_a , R r_b , List<String> a, List<String> b , Conf config) throws Exception {
        List<String> result = new ArrayList<>();
        
        int threadNum = 0; 
        
        LdDataset dataset = (LdDataset) config.getParam("LdDatasetMain");
        boolean useIndex = (Boolean) config.getParam("useIndexes");
        String extendingMeasure = (String) config.getParam("extendingMeasure");
                
        Conf ldsd_conf = new Conf();
        ldsd_conf.addParam("useIndexes", useIndex);
        ldsd_conf.addParam("LdDatasetMain" , dataset); 
        
        LDSD ldsd = null;
        
        switch (extendingMeasure) {
            case "LDSD_d":
                ldsd = new LDSD_d(ldsd_conf);
                break;
                
            case "LDSD_dw":
                ldsd = new LDSD_dw(ldsd_conf);
                break;
                
            case "LDSD_i":
                ldsd = new LDSD_i(ldsd_conf);
                break;
            
            case "LDSD_iw":
                ldsd = new LDSD_iw(ldsd_conf);
                break;
                
            case "LDSD_cw":
                ldsd = new LDSD_cw(ldsd_conf);
                break;
                
            
        }                
        
        ldsd.loadIndexes();     
        
        //To use single thread uncomment this part and comment the parts after it    
        /*if(config.getParam("threadsNumber") == null || (Integer)config.getParam("threadsNumber") == 1 ){
            //To use singlethread uncomment this parts and comment the parts after it
            String link_a , link_b , direction_a , direction_b;
            double sim;
            for(String fa : a){
               link_a = getLink(fa);
               direction_a = getDirection(fa);

               for (String fb : b) {
                    link_b = getLink(fb);
                    direction_b = getDirection(fb);

                    if(link_a.equals(link_b) && direction_a.equals(direction_b)){

                        R r1 = LdResourceFactory.getInstance().uri(getVertex(fa)).create();
                        R r2 = LdResourceFactory.getInstance().uri(getVertex(fb)).create();

                        sim = ldsd.compare(r1 , r2);

                        if( sim >= 0.5){
                            result.add(fa);
                            result.add(fb);
                        }
                    }

                } 
            }
       }*/
       ///////////////////////////////////////////////////////////////////////        

       //To use multithread runnable uncomment this part and comment the parts after it
       /*SearchTask[] threads = new SearchTask[a.size()];

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
       }*/
       //////////////////////////////////////////////////////////////////////
       
       
       //To use multithread callable uncomment this part and comment the parts before and after it   
       if(config.getParam("threadsNumber") != null) {          
            
            threadNum = (Integer) config.getParam("threadsNumber");

            ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

            List<Callable<List<String>>> lst = new ArrayList<>();

            for(String fa: a){

                lst.add(new SearchTask_(ldsd.getMeasure() , fa , b));

            }

            List<Future<List<String>>> tasks = executorService.invokeAll(lst);

            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(10, TimeUnit.HOURS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException ex) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }

            for(Future<List<String>> task : tasks)
            {

                if(task.isDone()){
                    result.addAll(task.get());
                }

            }
        
       }
       //////////////////////////////////////////////////////////////////////
       
       //To use properties saved in the created files uncomment this part and comment the parts before it 
       if(config.getParam("threadsNumber") == null || (Integer)config.getParam("threadsNumber") == 1 ){
            //To use singlethread uncomment this parts and comment the parts after it
            String link_a , node_b , direction_a;
            double sim;
            for(String fa : a){
               link_a = getLink(fa);
               direction_a = getDirection(fa);
               R r1 = LdResourceFactory.getInstance().uri(getVertex(fa)).create();
                              
               String path = EpicsLdManager.getPath(r_b , link_a+"|"+direction_a );
               
               File file =  new File(path);
               if(file.exists()){
                   Scanner sc = new Scanner(file); 

                   while (sc.hasNextLine()){
                       node_b = sc.nextLine().trim();
                       R r2 = LdResourceFactory.getInstance().uri(node_b).create();
                       sim = ldsd.compare(r1 , r2);
                            if( sim >= 0.5){
                                result.add(fa);
                                result.add(link_a+"|"+node_b+"|"+direction_a);
                            }
                   }
               }

            }
       }    
       //////////////////////////////////////////////////////////////////////  
       
           
       ldsd.closeIndexes();
       return result;
        
    }
    
    
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
    
	
    // Function that matches input str with a given wildcard pattern 
   /* static boolean strmatch(String str, String pattern, int n, int m) 
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
    } */
     
}
