/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lds.LdManager;


import java.util.List;
import lds.LdManager.ontologies.Ontology;
import lds.indexing.LdIndex;
import lds.indexing.LdIndexerManager;
import lds.measures.lods.ontologies.O;
import lds.resource.R;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class SimILdManager extends FeaturesMaesuresLdManager {
    private boolean useIndex;
    
    private LdIndex ontologiesIndex;
    private LdIndex augmentedOntologiesIndex;
    
    private LdIndexerManager manager;

    public SimILdManager(LdDataset dataset , boolean useIndex) {
        super(dataset);
        this.useIndex = useIndex;
    }
    
    public void loadIndexes() throws Exception{
        manager = LdIndexerManager.getManager();
        
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
        
            return Ontology.getListOntologyFromPrefixes(listOntologyPrefixes);
        }

        return Ontology.getListOntologyFromPrefixes(super.getOntologiesPrefixes(a));
        
    }
    
    
    
   
   public List<O> getAugmentdOntologies(R a) {
        
        if (useIndex) {
            List<String> listOntologyPrefixes = augmentedOntologiesIndex.getListFromIndex(dataset, a.getUri().stringValue() , baseClassPath + "getAugmentdOntologiesPrefixes" , a);
        
            return Ontology.getListOntologyFromPrefixes(listOntologyPrefixes);
        }

        return Ontology.getListOntologyFromPrefixes(super.getAugmentdOntologiesPrefixes(a));
        
    }
    
    
    
    
}
