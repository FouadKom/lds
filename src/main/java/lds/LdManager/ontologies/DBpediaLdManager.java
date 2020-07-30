/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager.ontologies;

import java.util.List;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.indexing.LdIndex;
import lds.indexing.LdIndexerManager;
import lds.resource.R;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class DBpediaLdManager extends DBpediaOntologiesLdManager {

    private boolean useIndex;
    private LdIndex conceptsIndex;
    private LdDataset dataSetInitial;
    private String endpointURI;
    private String defaultGraph;
    private LdIndexerManager manager;
    
   
    public DBpediaLdManager(LdDataset dataSetInitial , boolean useIndex) throws Exception {
        super(dataSetInitial);
        
        this.useIndex = useIndex;
        this.dataSetInitial = dataSetInitial;
    
    }
    
    
    public DBpediaLdManager(Config config) throws Exception {
        super((LdDataset) config.getParam(ConfigParam.LdDatasetMain));
        
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
        conceptsIndex = manager.loadIndex(conceptsIndexFile);
             
            
    }
    
    public void closeIndexes(){
        if (useIndex) {
            manager.closeIndex(conceptsIndex);
        }
        
    }
    

    @Override
    public List<String> getConcepts(R a , List<String> namespacesInitial , List<String> namespacesAugmented , boolean dataAugmentation) {
        if(useIndex){
             return conceptsIndex.getListFromIndex(dataSetInitial , a.getUri().stringValue() , baseClassPath + "getConcepts"  , a , namespacesInitial , namespacesAugmented , dataAugmentation);
        }
        
        return super.getConcepts(a , namespacesInitial , namespacesAugmented , dataAugmentation);
    }
    

        
    
    
}
