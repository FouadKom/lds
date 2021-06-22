/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.lods;

import lds.measures.lods.ontologies.O;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.LdManager.LodsLdManager;
import lds.LdManager.SimILdManager;
import lds.LdManager.ontologies.Ontology;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.feature.Feature;
import lds.resource.R;
import lds.measures.LdSimilarity;
import ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class LODS implements LdSimilarity{
    private boolean useIndeses;
    private List<O> ontologyList;
    private boolean dataAugmentation;
    
    private SimI lods_simI = null;
    private SimC lods_simC = null;
    private SimP lods_simP = null;
    
    private Config config;
    
    protected LodsLdManager lodsldManager;
    protected Integer NumberOfResources;
    
    public LODS(Config config) throws Exception{
        if(config.getParam(ConfigParam.useIndexes) == null || config.getParam(ConfigParam.ontologyList) == null || config.getParam(ConfigParam.dataAugmentation) == null)
            throw new Exception("Some configuration parameters missing"); 

        this.lodsldManager = new LodsLdManager((LdDataset) config.getParam(ConfigParam.LdDatasetMain) , (Boolean) config.getParam(ConfigParam.useIndexes) );
        this.useIndeses = (Boolean) config.getParam(ConfigParam.useIndexes);
        this.ontologyList = (List<O>) config.getParam(ConfigParam.ontologyList);
        this.dataAugmentation = (Boolean) config.getParam(ConfigParam.dataAugmentation);
        this.config = config;
        this.NumberOfResources = (Integer) config.getParam(ConfigParam.resourcesCount);

    }
    

    @Override
    public double compare(R a, R b) {
        double LODS = 0.0 , simI = 0.0 , simC = 0.0 , simP = 0.0;
        
        try {
            
            lods_simI = new SimI(config);
            lods_simC = new SimC(config);
            lods_simP = new SimP(config);
            
        } catch (Exception ex) {
            Logger.getLogger(LODS.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (a.equals(b)) {
                LODS = 1.0;
        }
        
        else {

            simI = lods_simI.compare(a, b);
            simC = lods_simC.compare(a, b);
            simP = lods_simP.compare(a, b);

            List<Double> scores = new ArrayList<>();

            if (simI != -1)
                    scores.add(simI);
            if (simC != -1)
                    scores.add(simC);
            if (simP != -1)
                    scores.add(simP);

            for (Double score : scores) {
                    LODS += score;
            }

            LODS = LODS / scores.size();

        }
        return LODS;
    }
    
   

    @Override
    public void closeIndexes() {
        lods_simI.closeIndexes();
        lods_simP.closeIndexes();
        lods_simC.closeIndexes();
        
        Ontology.closeIndexes();
        
    }

    @Override
    public void loadIndexes() throws Exception {
        lods_simI.loadIndexes();
        lods_simP.loadIndexes();
        lods_simC.loadIndexes();
        
        Ontology.loadIndexes();
      
    }

    @Override
    public LdSimilarity getMeasure() {
        return this;
    }
    
    protected static double TverskySimilarity_mod(List<String> features_a, List<String> features_b) {
        List<String> a = Feature.uniqueFeatures(features_b, features_b);
        int features_a_not_b = a.size();

        List<String> b = Feature.uniqueFeatures(features_b, features_a);
        int features_b_not_a = b.size();

        List<String> c = Feature.commonFeatures(features_a, features_b);
        int commonFeatures = c.size();

        double similarity = 0.0;

        if (commonFeatures == 0)
                similarity = 0;
        else
                similarity = (double) commonFeatures / ( (double) commonFeatures + (double) features_a_not_b + (double) features_b_not_a );

        return similarity;
    }
    
    protected List<O> getCommonOntologies(R a, R b) throws Exception {
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
                if(ontologyList.contains(ontology) && augmentedOntologies_b.contains(ontology) && !commonOntologies.contains(ontology) ){
                        ontology.initializeOntology(config);
                        commonOntologies.add(ontology);
                }
            }
            
        }
        
        return commonOntologies;
    }

    protected List<O> getOntologies(R a) {        
        return lodsldManager.getOntologies(a);

    }
    
    protected List<O> getAugmentedOntologies(R a){
        return lodsldManager.getAugmentdOntologies(a);
    }
        
}
