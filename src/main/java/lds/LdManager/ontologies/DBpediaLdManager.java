/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager.ontologies;

import java.util.List;
import lds.indexing.LdIndexer_;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class DBpediaLdManager extends DBpediaOntologiesLdManager {

    private boolean useIndex;
    private LdIndexer_ conceptsIndex;
    private LdDataset dataSetInitial;
    private String endpointURI;
    private String defaultGraph;
    
   
    public DBpediaLdManager(LdDataset dataSetInitial , boolean useIndex) throws Exception {
        super(dataSetInitial);
        
        this.useIndex = useIndex;
        this.dataSetInitial = dataSetInitial;
    
    }
    
    
    public DBpediaLdManager(Conf config) throws Exception {
        super((LdDataset) config.getParam("LdDatasetMain"));
        
        this.useIndex = (Boolean) config.getParam("useIndexes");
        this.dataSetInitial = (LdDataset) config.getParam("LdDatasetMain");
        
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
        String conceptsIndexFile = System.getProperty("user.dir") + "/Indexes/Ontologies/DBpedia/concepts_index_" + dataSetInitial.getName().toLowerCase().replace(" ", "_") + ".db";
        conceptsIndex = new LdIndexer_(conceptsIndexFile);
             
            
    }
    
    public void closeIndexes(){
        if (useIndex) {
            conceptsIndex.close();
        }
        
    }
    

    @Override
    public List<String> getConcepts(R a , List<String> namespacesInitial , List<String> namespacesAugmented , boolean dataAugmentation) {
        if(useIndex){
             return LdIndexer_.getListFromIndex(dataSetInitial , conceptsIndex , a.getUri().stringValue() , baseClassPath + "getConcepts"  , a , namespacesInitial , namespacesAugmented , dataAugmentation);
        }
        
        return super.getConcepts(a , namespacesInitial , namespacesAugmented , dataAugmentation);
    }
    

        
    
    
}
