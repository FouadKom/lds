/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager.ontologies;

import java.util.List;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.dataset.LdDatasetCreator;
import lds.indexing.LdIndex;
import lds.indexing.LdIndexerManager;
import lds.resource.R;
import ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class DBpediaLdManager extends DBpediaOntologiesLdManager {

    private boolean useIndex;
    private LdIndex conceptsIndex;
    private LdIndex initialCategoriesIndex;
    private LdIndex broaderCategoriesIndex;
    private LdIndex narrowerCategoriesIndex;
    
    private LdDataset dataSetInitial;
    private String endpointURI;
    private String defaultGraph;
    private LdIndexerManager manager;
    
    private LdDataset datasetMain;
    
   
    public DBpediaLdManager(LdDataset dataSetInitial , boolean useIndex) throws Exception {
        
        super(dataSetInitial);
        this.datasetMain = LdDatasetCreator.getDBpediaDataset();
        this.useIndex = useIndex;
        this.dataSetInitial = dataSetInitial;
    
    }
    
    
    public DBpediaLdManager(Config config) throws Exception {
        super((LdDataset) config.getParam(ConfigParam.LdDatasetMain));
        
        this.datasetMain = LdDatasetCreator.getDBpediaDataset();
        this.useIndex = (Boolean) config.getParam(ConfigParam.useIndexes);
        this.dataSetInitial = (LdDataset) config.getParam(ConfigParam.LdDatasetMain);
        
        endpointURI = (String) config.getParam("endpointURI");
        defaultGraph = (String) config.getParam("defaultGraph");
        
        if(endpointURI != null && defaultGraph != null){
            super.dataSet.setLink(endpointURI);
            super.dataSet.setDefaultGraph(defaultGraph);
        }
        else{
            super.dataSet.setLink("http://dbpedia.org");
            super.dataSet.setDefaultGraph("http://dbpedia.org");
        }
    
    }
    
    public void loadIndexes() throws Exception {
        manager = LdIndexerManager.getManager();
        String conceptsIndexFile = System.getProperty("user.dir") + "/Indexes/Ontologies/DBpedia/concepts_index_" + dataSetInitial.getName().toLowerCase().replace(" ", "_") + ".db";
        String InitialCategoriesIndexFile = System.getProperty("user.dir") + "/Indexes/Ontologies/DBpedia/categories_index_" + dataSetInitial.getName().toLowerCase().replace(" ", "_") + ".db";
        String broaderCategoriesIndexFile = System.getProperty("user.dir") + "/Indexes/Ontologies/DBpedia/categories_index_" + dataSetInitial.getName().toLowerCase().replace(" ", "_") + ".db";
        String narrowerCategoriesIndexFile = System.getProperty("user.dir") + "/Indexes/Ontologies/DBpedia/categories_index_" + dataSetInitial.getName().toLowerCase().replace(" ", "_") + ".db";
        
        conceptsIndex = manager.loadIndex(conceptsIndexFile);
        initialCategoriesIndex = manager.loadIndex(InitialCategoriesIndexFile);
        broaderCategoriesIndex = manager.loadIndex(broaderCategoriesIndexFile);
        narrowerCategoriesIndex = manager.loadIndex(narrowerCategoriesIndexFile);
        
             
            
    }
    
    public void closeIndexes(){
        if (useIndex) {
            manager.closeIndex(conceptsIndex);
            manager.closeIndex(initialCategoriesIndex);
            manager.closeIndex(broaderCategoriesIndex);
            manager.closeIndex(narrowerCategoriesIndex);
        }
        
    }
    

    @Override
    public List<String> getConcepts(R a , List<String> namespacesInitial , List<String> namespacesAugmented , boolean dataAugmentation) {
        if(useIndex){
             return conceptsIndex.getListFromIndex(dataSetInitial , a.getUri().stringValue() , baseClassPath + "getConcepts"  , a , namespacesInitial , namespacesAugmented , dataAugmentation);
        }
        
        return super.getConcepts(a , namespacesInitial , namespacesAugmented , dataAugmentation);
    }
    
    
    /*
    @Override
    public List<String> getCategories(R a , List<String> namespacesInitial , boolean dataAugmentation) {
        if(useIndex){
             return categoriesIndex.getListFromIndex(dataSetInitial , a.getUri().stringValue() , baseClassPath + "getCategories"  , a , namespacesInitial , dataAugmentation);
        }
        
        return super.getCategories(a , namespacesInitial, dataAugmentation);
    }
    */
    
    @Override
    public List<String> getCategories(R a) {
        if(useIndex){
             return initialCategoriesIndex.getListFromIndex(datasetMain , a.getUri().stringValue() , baseClassPath + "getCategories"  , a);
        }
        
        return super.getCategories(a);
    }
    
    @Override
    public List<String> getBroaderCategories(R a , int level) {
        if(useIndex){
             return broaderCategoriesIndex.getListFromIndex(datasetMain , a.getUri().stringValue() , baseClassPath + "getCategories"  , a);
        }
        
        return super.getBroaderCategories(a , level);
    }

    @Override
    public List<String> getNarrowerCategories(R a , int level) {
        if(useIndex){
             return narrowerCategoriesIndex.getListFromIndex(datasetMain , a.getUri().stringValue() , baseClassPath + "getCategories"  , a);
        }
        
        return super.getNarrowerCategories(a , level);
    }
    

        
    
    
}
