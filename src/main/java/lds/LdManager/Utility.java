/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lds.LdManager.ontologies.Ontology;
import lds.resource.R;
import org.apache.jena.rdf.model.Resource;
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
        
    public static String createKey(Object ...args){
        String key = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof R) {
                R resource = (R) args[i];
                key = key + compressValue(resource) + "|";
            } else if (args[i] instanceof URI) {
                URI uri = (URI) args[i];
                key = key + compressValue(uri) + "|";
            }
        }
        
        return key;
    }    

    public static String compressValue(Resource resource) {
        String prefix = Ontology.getPrefixFromNamespace(resource.getNameSpace());
        if(! prefix.equals(resource.getNameSpace()))
            return prefix + resource.getLocalName();
        
        return resource.getURI();
    }
    
    public static String compressValue(R r) {
        return compressValue(r.getUri());
    }
    
    public static String compressValue(URI uri) {
        String prefix = Ontology.getPrefixFromNamespace(uri);
        if(! prefix.equals(uri.getNamespace()))
            return prefix + uri.getLocalName();
        
        return uri.toString();
    }

    public static String decompressValue(String value) {
        if(value.contains("http") || !value.contains(":"))
            return value;
        
        String string[] =  value.split("\\:" , 2);
        
        return Ontology.getNamespaceFromPrefix(string[0]) + string[1];
    }
        
}
            
        
   
