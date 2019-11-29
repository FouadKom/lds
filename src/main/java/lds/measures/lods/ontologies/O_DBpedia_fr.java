/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.lods.ontologies;

import java.util.Arrays;
import java.util.List;
import lds.resource.R;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class O_DBpedia_fr extends O_DBpedia{
    
    private List<String> namespaces = Arrays.asList("\"http://fr.dbpedia.org/resource/\"");
    
    @Override
    public List<String> getConcepts(R a) {        
        return super.getConcepts(a , namespaces , dataAugmentation);  
    }
    
    @Override
    public String toString(){
        return "DBpedia_fr";
    }
    
    @Override
    public O getOntology() {
        return this;
    }
    
    @Override
    public boolean equals(Object o){
        
        if (o == this) { 
            return true; 
        } 
  
        if (!(o instanceof O)) { 
            return false; 
        } 
           
        O ontology = (O) o; 
          
        return ontology.toString().equals("DBpedia_fr");
        
    }
    
}
