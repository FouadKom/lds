/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager.ontologies;

import java.util.List;
import lds.dataset.LdDatasetCreator;
import lds.indexing.LdIndex;
import lds.indexing.LdIndexerManager;
import lds.resource.R;
import ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class YagoLdManager extends DBpediaOntologiesLdManager {
    
    private boolean useIndex;
    private LdIndex conceptsIndex;
    private LdIndex categoriesIndex;
    private LdIndexerManager manager;
    private LdDataset dataSetInitial;
    private LdDataset datasetMain;
    
    public YagoLdManager(LdDataset dataSetInitial , boolean useIndex) throws Exception {
        super(dataSetInitial);
        
        this.datasetMain = LdDatasetCreator.getDBpediaDataset();
        this.useIndex = useIndex;
        this.dataSetInitial = dataSetInitial;
        
    }
    
    public void loadIndexes() throws Exception {
        manager = LdIndexerManager.getManager();
        String conceptsIndexFile = System.getProperty("user.dir") + "/Indexes/Ontologies/Yago/concepts_index_" + dataSetInitial.getName().toLowerCase().replace(" ", "_") + ".db";
        String categoriesIndexFile = System.getProperty("user.dir") + "/Indexes/Ontologies/Yago/categories_index_" + dataSetInitial.getName().toLowerCase().replace(" ", "_") + ".db";
        conceptsIndex = manager.loadIndex(conceptsIndexFile);
        categoriesIndex = manager.loadIndex(conceptsIndexFile);
             
            
    }
    
    public void closeIndexes(){
        if (useIndex) {
            manager.closeIndex(conceptsIndex);
            manager.closeIndex(categoriesIndex);
        }
        
    }
    
    
    @Override
    public List<String> getConcepts(R a , List<String> namespacesInitial , List<String> namespacesAugmented , boolean dataAugmentation) {
        if(useIndex){
             return conceptsIndex.getListFromIndex(dataSetInitial , a.getUri().stringValue() , baseClassPath + "getConcepts"  , a , namespacesInitial , namespacesAugmented , dataAugmentation);
        }
        
        return super.getConcepts(a , namespacesInitial , namespacesAugmented , dataAugmentation);
    }
    
//    @Override
//    public List<String> getCategories(R a , List<String> namespacesInitial , boolean dataAugmentation) {
//        if(useIndex){
//             return categoriesIndex.getListFromIndex(dataSetInitial , a.getUri().stringValue() , baseClassPath + "getCategories"  , a , namespacesInitial , dataAugmentation);
//        }
//        
//        return super.getCategories(a , namespacesInitial , dataAugmentation);
//    }
    
    @Override
    public List<String> getCategories(R a ) {
        if(useIndex){
             return categoriesIndex.getListFromIndex(dataSetInitial , a.getUri().stringValue() , baseClassPath + "getCategories"  , a );
        }
        
        return super.getCategories(a );
    }
    
    
}
