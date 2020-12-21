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
import lds.LdManager.ontologies.Ontology;
import lds.feature.Feature;

/**
 *
 * @author Fouad Komeiha
 */
public class EPICS_Feature extends Feature{
    
    public EPICS_Feature(String link, String vertex, String direction) {
        super(link, vertex, direction);
    }
    
    public static List<String> similarFeatures(List<String> a, List<String> b) throws Exception {
        List<String> result = new ArrayList<>();
        
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
        
        return result;
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
    
}
