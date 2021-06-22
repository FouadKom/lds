package lds.measures.lods.ontologies;

import java.util.Arrays;
import java.util.List;
import lds.LdManager.ontologies.DBpediaLdManager;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.resource.R;
import ldq.LdDataset;


public class O_DBpedia implements O {
    protected boolean useIndexes;
    protected boolean dataAugmentation;     
    protected List<String> namespaces = Arrays.asList("\"http://dbpedia.org/ontology/\"" , "\"http://www.w3.org/2002/07/owl\"");
    
    private LdDataset datasetInitial;
    private DBpediaLdManager dbpedialdManager; 
    
    
    @Override
    public void initializeOntology(Config config) throws Exception {
        if(config.getParam(ConfigParam.useIndexes) == null)
            throw new Exception("Some configuration parameters missing");

        this.useIndexes = (Boolean) config.getParam(ConfigParam.useIndexes);
        this.dataAugmentation = (Boolean) config.getParam(ConfigParam.dataAugmentation);
        this.datasetInitial = (LdDataset) config.getParam(ConfigParam.LdDatasetMain);
        
        this.dbpedialdManager = new DBpediaLdManager(config);
        
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
        return this.getConcepts(a , namespaces , namespaces , dataAugmentation);  
    }
    
    
    protected List<String> getConcepts(R a , List<String> namespacesInitial , List<String> namespacesAugmented , boolean dataAugmentation){
        return dbpedialdManager.getConcepts(a , namespacesInitial , namespacesAugmented , dataAugmentation); 
    }
    
//    @Override
//    public List<String> getCategories(R r) {
//       return this.getCategories(r , namespaces , dataAugmentation);  
//    }
//    
//    
//    protected List<String> getCategories(R a , List<String> namespacesInitial, boolean dataAugmentation){
//        return dbpedialdManager.getCategories(a , namespacesInitial  , dataAugmentation); 
//    }
    
    @Override
    public List<String> getCategories(R r) {
       return dbpedialdManager.getCategories(r);  
    }
    
    @Override
    public List<String> getBroaderCategories(R r , int level) {
       return dbpedialdManager.getBroaderCategories(r , level);  
    }
    
    @Override
    public List<String> getNarrowerCategories(R r , int level) {
       return dbpedialdManager.getNarrowerCategories(r , level);  
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
