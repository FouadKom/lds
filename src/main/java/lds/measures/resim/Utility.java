/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        if(list != null  && !list.contains("-1")){
            for(String value:list){
                listURI.add(factory.getURI(value));
            }

            return listURI;
        }
        return null;
            
        }
        
        public static List<String> toList(Set<URI> list){
            List<String> listString = new ArrayList<>();
            if(list != null){
                for(URI value:list){
                    listString.add(value.stringValue());
                }
                
                return listString;
            }
            return null;            
        }
    }
            
        
   
