/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lds.LdManager.ontologies.Ontology;
import lds.indexing.LdIndex;
import lds.measures.ldsd.*;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class Utility_v2 {  
    
    public static List<String> uniqueFeatures(List<String> a, List<String> b) {
        List<String> result = new ArrayList<>(a);
        
        for (String f : b) {
            if (result.contains(f)) {
                result.remove(f);
            }
        }
        
        return result;
    }
    
    public static List<String> similarFeatures(List<String> a, List<String> b) throws Exception {
        List<String> result = new ArrayList<>();
        double sim;
        
       /* LdDataset dataset = (LdDataset) config.getParam("LdDatasetMain");
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
        
               
        if(config.getParam("threadsNumber") != null) {
            Map<String , List<String>> map_b = createFeaturesMap(b);
            
//            featuresIndex_b.addMap(map_b);
            
            int threadNum = (Integer) config.getParam("threadsNumber");
            System.out.println(threadNum);
            ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

            List<Callable<List<String>>> lst = new ArrayList<>();

            for(String fa: a){
                
                String link_a = Ontology.decompressValue(getLink(fa));
                String direction_a = getDirection(fa);
                String node_a = Ontology.decompressValue(getVertex(fa));
                R r1 = LdResourceFactory.getInstance().uri(node_a).create();
                
                String key = link_a+"|"+direction_a;
                
                lst.add(new SearchTask(ldsd.getMeasure() , fa , map_b.get(key) ));

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

                if(task!= null && task.isDone()){
                    result.addAll(task.get());
                }

            }
        
       }*/
        
//        if(config.getParam("threadsNumber") == null || (Integer)config.getParam("threadsNumber") == 1 ){d

            Map<String , List<String>> map_b = createFeaturesMap(b);
            Map<String , List<String>> map_a = createFeaturesMap(a);
            
            for(Map.Entry<String , List<String>> entry : map_a.entrySet()){
                String key = entry.getKey();                
                List<String> features_b = map_b.get(key);
                
                if(features_b != null && ! features_b.isEmpty()){
                    List<String> features_a = entry.getValue();
                    Optional.ofNullable(features_a).ifPresent(result::addAll);
                    Optional.ofNullable(features_b).ifPresent(result::addAll);
                }
                
            }
            
          
           
//            featuresIndex_b.addMap(map_b);
                    
           /* for(String fa : a){
               String link_a = Ontology.decompressValue(getLink(fa));
               String direction_a = Ontology.decompressValue(getDirection(fa));
               String node_a = Ontology.decompressValue(getVertex(fa));
               R r1 = LdResourceFactory.getInstance().uri(node_a).create();
               String key = link_a+"|"+direction_a;

//               List<String> nodes = featuresIndex_b.getList(key);
               List<String> nodes = map_b.get(key);
               
               if(nodes == null || nodes.isEmpty())
                   continue;

               for(String node_b : nodes){
                   R r2 = LdResourceFactory.getInstance().uri(node_b).create();
//                   sim = ldsd.compare(r1 , r2);
//                   if( sim >= 0.5){
                       result.add(fa);
                       result.add(link_a+"|"+node_b+"|"+direction_a);
//                   }
               }

            }*/
                   
//        }
                
//        ldsd.closeIndexes();
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
    
	
    private static Map<String , List<String>> createFeaturesMap(List<String> features){
        List<String> list = features;
        String link_a , node_a , direction_a;
        Map<String , List<String>> map = new HashMap<>();
        
        for(String feature : list){
           List<String> nodes = new ArrayList<>();
           link_a = Ontology.decompressValue(getLink(feature));
           direction_a = getDirection(feature);
           node_a = Ontology.decompressValue(getVertex(feature));
           
           String key = link_a+"|"+direction_a; //create key
         
           if(map ==null || map.isEmpty()){
//                nodes.add(node_a);// add node to the list to be added to the map
                nodes.add(link_a+"|"+node_a+"|"+direction_a);// add node to the list to be added to the map
                map.put(key, nodes); 
           }
           
           else{
               nodes = map.get(key);
               
               if(nodes == null || nodes.isEmpty()){
                   nodes = new ArrayList<>();
//                   nodes.add(node_a);
                   nodes.add(link_a+"|"+node_a+"|"+direction_a);                   
                   map.put(key , nodes);
               }
               
               else if(! nodes.contains(node_a)){
//                    nodes.add(node_a);
                    nodes.add(link_a+"|"+node_a+"|"+direction_a);  
                    map.put(key , nodes);
               }
            }
        }
                
        return map;
    }
    
    public static double log2(double N){ 

            double result = (double)(Math.log(N) / Math.log(2)); 

            return result;
    } 
    
}
