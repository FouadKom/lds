/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.lods.ontologies;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.LdManager.ontologies.DBpediaLdManager;
import lds.LdManager.ontologies.YagoLdManager;
import lds.resource.R;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import static org.junit.Assert.fail;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.graph.model.graph.G;
import slib.utils.i.Conf;

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
    public void initializeOntology(Conf config) throws Exception {
        if(config.getParam("useIndexes") == null)
            throw new Exception("Some configuration parameters missing");

        this.useIndexes = (Boolean) config.getParam("useIndexes");
        this.dataAugmentation = (Boolean) config.getParam("dataAugmentation");
        this.datasetInitial = (LdDataset) config.getParam("LdDatasetMain");
                
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
        
        return yagoldManager.getConcepts(a , namespaces , dataAugmentation);       
        
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

      
}
