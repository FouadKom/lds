/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.lods;

import lds.measures.lods.ontologies.O;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.LdManager.SimILdManager;
import lds.measures.LdSimilarityMeasure;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class SimI implements LdSimilarityMeasure {
    private boolean useIndeses;
    private List<O> ontologyList;
    private boolean dataAugmentation;
    private SimILdManager simIldManager;
    private Conf config;
    
    public SimI(Conf config) throws Exception{
        if(config.getParam("useIndexes") == null || config.getParam("ontologyList") == null || config.getParam("dataAugmentation") == null)
            throw new Exception("Some configuration parameters missing"); 

        this.simIldManager = new SimILdManager((LdDataset) config.getParam("LdDatasetMain") , (Boolean) config.getParam("useIndexes") );
        this.useIndeses = (Boolean) config.getParam("useIndexes");
        this.ontologyList = (List<O>) config.getParam("ontologyList");
        this.dataAugmentation = (Boolean) config.getParam("dataAugmentation");
        this.config = config;
    }

    @Override
    public double compare(R a, R b) {
        
        double score = 0;

        List<O> commonOntologies = null;
        try {
            
            commonOntologies = getCommonOntologies(a, b);
            
        } catch (Exception ex) {
            Logger.getLogger(SimI.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(commonOntologies == null || commonOntologies.isEmpty())
            return -1;

        for (O commonOntology : commonOntologies) {
            score = score + calculate_simI_concepts(commonOntology , a , b);
        }

        return score/commonOntologies.size();

    }
    
    public double calculate_simI_concepts(O ontology , R a, R b) {
        double score = 0;

        List<String> features_a, features_b;

        features_a = ontology.getConcepts(a);
        features_b = ontology.getConcepts(b);

        score = Utility.TverskySimilarity_mod(features_a, features_b);

        return score;
        
    }
    
     public List<O> getCommonOntologies(R a, R b) throws Exception {
        List<O> commonOntologies = new ArrayList<>();
        
        List<O> onologies_a = getOntologies(a);
        List<O> onologies_b = getOntologies(b);
        
        for(O ontology: onologies_a){
            if(ontologyList.contains(ontology) && onologies_b.contains(ontology)){
                    ontology.initializeOntology(config);
                    commonOntologies.add(ontology);
            }
        }
        
        if(dataAugmentation){
            List<O> augmentedOntologies_a = getAugmentedOntologies(a);
            List<O> augmentedOntologies_b = getAugmentedOntologies(b);
            
            for(O ontology: augmentedOntologies_a){
                if(ontologyList.contains(ontology) && augmentedOntologies_b.contains(ontology) && !commonOntologies.contains(ontology)){
                        ontology.initializeOntology(config);
                        commonOntologies.add(ontology);
                }
            }
            
        }
        
        return commonOntologies;
    }

    public List<O> getOntologies(R a) {        
        return simIldManager.getOntologies(a);

    }
    
    public List<O> getAugmentedOntologies(R a){
        return simIldManager.getAugmentdOntologies(a);
    }


    @Override
    public void closeIndexes(){
        if(useIndeses){
            simIldManager.closeIndexes();
        }
        
    }    
    
    @Override
    public void loadIndexes() throws Exception{
        if(useIndeses){
            simIldManager.loadIndexes();
        }
    }

    @Override
    public LdSimilarityMeasure getMeasure() {
        return this;
    }
    
   
    
    
    
    
}
