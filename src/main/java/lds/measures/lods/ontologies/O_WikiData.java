/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.lods.ontologies;

import java.util.Arrays;

import java.util.List;
import lds.LdManager.ontologies.WikiDataLdManager;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;

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
    public void initializeOntology(Conf config) throws Exception {
        this.useIndex = (Boolean) config.getParam("useIndexes");
        this.dataAugmentation = (Boolean) config.getParam("dataAugmentation");
        this.datasetInitial = (LdDataset) config.getParam("LdDatasetMain");
        
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
