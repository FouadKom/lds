/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.lods.ontologies;

import java.util.Arrays;
import java.util.List;
import lds.LdManager.ontologies.YagoLdManager;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.resource.R;
import ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class O_Yago implements O{
    
    private List<String> namespaces = Arrays.asList("\"http://dbpedia.org/class/yago/\"");
    private boolean useIndexes;
    private boolean dataAugmentation;
    private LdDataset datasetInitial;
    private YagoLdManager yagoldManager;
    
    @Override
    public void initializeOntology(Config config) throws Exception {
        if(config.getParam(ConfigParam.useIndexes) == null)
            throw new Exception("Some configuration parameters missing");

        this.useIndexes = (Boolean) config.getParam(ConfigParam.useIndexes);
        this.dataAugmentation = (Boolean) config.getParam(ConfigParam.dataAugmentation);
        this.datasetInitial = (LdDataset) config.getParam(ConfigParam.LdDatasetMain);
                
        this.yagoldManager = new YagoLdManager(datasetInitial , useIndexes);
        if(useIndexes){
            try {
                yagoldManager.loadIndexes();
            } catch (Exception ex) {
                //Index already opened no need to open it
            }
        }
    }
       
    
    @Override
    public List<String> getConcepts(R a) {
        
        return yagoldManager.getConcepts(a , namespaces , namespaces , dataAugmentation);       
        
    }
    
    
    @Override
    public String toString(){
        return "Yago";
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
          
        return ontology.toString().equals("Yago");
        
    }

//    @Override
//    public List<String> getCategories(R r) {
//        return yagoldManager.getCategories(r , namespaces , dataAugmentation); 
//    }
    
    @Override
    public List<String> getCategories(R r) {
        return yagoldManager.getCategories(r); 
    }
    
    @Override
    public List<String> getBroaderCategories(R r , int level) {
       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<String> getNarrowerCategories(R r , int level) {
       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

      
}
