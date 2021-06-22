/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.lods.ontologies;

import java.util.Arrays;

import java.util.List;
import lds.LdManager.ontologies.WikiDataLdManager;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.resource.R;
import ldq.LdDataset;


/**
 *
 * @author Fouad Komeiha
 */
public class O_WikiData implements O {
    private boolean useIndex;
    private boolean dataAugmentation;
    private LdDataset datasetInitial;

    private WikiDataLdManager wikiDataldManager;
    
    private List<String> namespaces = Arrays.asList("\"http://www.wikidata.org/entity/\"");

    @Override
    public void initializeOntology(Config config) throws Exception {
        this.useIndex = (Boolean) config.getParam(ConfigParam.useIndexes);
        this.dataAugmentation = (Boolean) config.getParam(ConfigParam.dataAugmentation);
        this.datasetInitial = (LdDataset) config.getParam(ConfigParam.LdDatasetMain);
        
        this.wikiDataldManager = new WikiDataLdManager(datasetInitial , useIndex);
        
        if(useIndex){
            try {
                wikiDataldManager.loadIndexes();
            } catch (Exception ex) {
                //Index already opened no need to open it
            }
        }
        
        
    }
    
    @Override
    public List<String> getConcepts(R a){
        return wikiDataldManager.getConcepts(a , dataAugmentation);
        
    }
    
    @Override
    public List<String> getCategories(R a) {
        return wikiDataldManager.getCategories(a , dataAugmentation);
    }
    
    @Override
    public List<String> getBroaderCategories(R r , int level) {
       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<String> getNarrowerCategories(R r , int level) {
       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void closeIndexes(){
        if(useIndex){
            try {
                wikiDataldManager.closeIndexes();
            } catch (Exception ex) {
                //Index already closed no need to open it
            }
        }
    }
    
    
    @Override
    public String toString(){
        return "WikiData";
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
          
        return ontology.toString().equals("WikiData");
        
    }

}
