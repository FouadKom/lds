/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lds.LdManager;

import java.util.ArrayList;
import java.util.List;
import lds.indexing.LdIndex;
import lds.indexing.LdIndexer;
import lds.measures.lods.ontologies.O;
import lds.measures.lods.ontologies.O_DBpedia;
import lds.measures.lods.ontologies.O_Schema;
import lds.measures.lods.ontologies.O_Umbel;
import lds.measures.lods.ontologies.O_WikiData;
import lds.measures.lods.ontologies.O_Yago;
import lds.resource.R;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class SimILdManager extends FeaturesMeasuresLdManager{
    private boolean useIndex;
    
    private LdIndex ontologiesIndex;
    private LdIndex augmentedOntologiesIndex;
    
    private LdIndexer manager;

    public SimILdManager(LdDataset dataset , boolean useIndex) {
        super(dataset);
        this.useIndex = useIndex;
    }
    
    public void loadIndexes() throws Exception{
        manager = LdIndexer.getManager();
        
        String ontologiesIndexFile = System.getProperty("user.dir") + "/Indexes/LODS/SimI/simI_ontologies_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String augmentedOntologiesIndexFile = System.getProperty("user.dir") + "/Indexes/LODS/SimI/simI_augmented_ontologies_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        
        ontologiesIndex = manager.loadIndex(ontologiesIndexFile);
        augmentedOntologiesIndex = manager.loadIndex(augmentedOntologiesIndexFile);
            
    }
    
    public void closeIndexes(){
        if (useIndex) {
            manager.closeIndex(ontologiesIndex);
            manager.closeIndex(augmentedOntologiesIndex);
        }
        
    }

    
    public List<O> getOntologies(R a) {
        
        if (useIndex) {
            List<String> listOntologyPrefixes = ontologiesIndex.getListFromIndex(dataset, a.getUri().stringValue() , baseClassPath + "getOntologiesPrefixes" , a);
        
            return Utility.getListOntologyFromPrefixes(listOntologyPrefixes);
        }

        return Utility.getListOntologyFromPrefixes(super.getOntologiesPrefixes(a));
        
    }
    
    
    
   
   public List<O> getAugmentdOntologies(R a) {
        
        if (useIndex) {
            List<String> listOntologyPrefixes = augmentedOntologiesIndex.getListFromIndex(dataset, a.getUri().stringValue() , baseClassPath + "getAugmentdOntologiesPrefixes" , a);
        
            return Utility.getListOntologyFromPrefixes(listOntologyPrefixes);
        }

        return Utility.getListOntologyFromPrefixes(super.getAugmentdOntologiesPrefixes(a));
        
    }
    
    
    
    
}
