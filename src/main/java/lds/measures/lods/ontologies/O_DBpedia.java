package lds.measures.lods.ontologies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.LdManager.ontologies.DBpediaLdManager;
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
import slib.utils.i.Conf;


public class O_DBpedia implements O {
    private boolean useIndexes;
    private boolean dataAugmentation;
    private LdDataset datasetInitial;
    private DBpediaLdManager dbpedialdManager;  
    private List<String> namespaces = Arrays.asList("\"http://dbpedia.org/ontology/\"" , "\"http://www.w3.org/2002/07/owl\"");
    
    
    @Override
    public void initializeOntology(Conf config) throws Exception {
        if(config.getParam("useIndexes") == null)
            throw new Exception("Some configuration parameters missing");

        this.useIndexes = (Boolean) config.getParam("useIndexes");
        this.dataAugmentation = (Boolean) config.getParam("dataAugmentation");
        this.datasetInitial = (LdDataset) config.getParam("LdDatasetMain");
        
        this.dbpedialdManager = new DBpediaLdManager(datasetInitial , useIndexes);
        
        if(useIndexes){
            try {
                dbpedialdManager.loadIndexes();
            } catch (Exception ex) {
                //Index already opened no need to open it
            }
        }
    }
    
	
    @Override
    public List<String> getConcepts(R a) {        
        return dbpedialdManager.getConcepts(a , namespaces , dataAugmentation);  
    }
        
    
    public void closeIndexes(){
        if(useIndexes){
            try {
                dbpedialdManager.closeIndexes();
            } catch (Exception ex) {
                //Index already closed no need to open it
            }
        }
    }
    
    
    @Override
    public String toString(){
        return "DBpedia";
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
          
        return ontology.toString().equals("DBpedia");
        
    }

    

    
}
