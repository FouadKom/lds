/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.lods.ontologies;

import java.util.Arrays;
import java.util.List;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.resource.R;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class O_DBpedia_eu extends O_DBpedia{
    
    private List<String> namespacesInitial = Arrays.asList("\"http://eu.dbpedia.org/resource/\"");
    private List<String> namespacesAugmentation = super.namespaces;
    private String endpointURI = "https://eu.dbpedia.org/sparql";
    
   @Override
    public void initializeOntology(Config config) throws Exception {
        if(config.getParam(ConfigParam.useIndexes) == null)
            throw new Exception("Some configuration parameters missing");

        config.addParam("endpointURI", endpointURI);
        
        super.initializeOntology(config);
    }
    
    
    @Override
    public List<String> getConcepts(R a) {        
        return super.getConcepts(a , namespacesInitial , namespacesAugmentation , dataAugmentation);   
    }

    @Override
    public String toString(){
        return "DBpedia_eu";
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
          
        return ontology.toString().equals("DBpedia_eu");
        
    }
    
}
