/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.lods;

import lds.measures.lods.ontologies.O;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.LdManager.SimILdManager;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.feature.Feature;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import lds.measures.LdSimilarity;

/**
 *
 * @author Fouad Komeiha
 */
public class SimI implements LdSimilarity {
    private boolean useIndeses;
    private List<O> ontologyList;
    private List<O> commonOntologies;
    private boolean dataAugmentation;
    private SimILdManager simIldManager;
    private Config config;
    
    
    public SimI(Config config) throws Exception{
        if(config.getParam(ConfigParam.useIndexes) == null || config.getParam(ConfigParam.ontologyList) == null || config.getParam(ConfigParam.dataAugmentation) == null)
            throw new Exception("Some configuration parameters missing"); 

        this.simIldManager = new SimILdManager((LdDataset) config.getParam(ConfigParam.LdDatasetMain) , (Boolean) config.getParam(ConfigParam.useIndexes) );
        this.useIndeses = (Boolean) config.getParam(ConfigParam.useIndexes);
        this.ontologyList = (List<O>) config.getParam(ConfigParam.ontologyList);
        this.dataAugmentation = (Boolean) config.getParam(ConfigParam.dataAugmentation);
        this.config = config;
    }

    @Override
    public double compare(R a, R b) {
        
        double score = 0;

        try {
            
            commonOntologies = getCommonOntologies(a, b);
            
        } catch (Exception ex) {
            Logger.getLogger(SimI.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

        if(commonOntologies == null || commonOntologies.isEmpty())
            return -1;

//        for (O commonOntology : commonOntologies) {
//            score = score + calculate_simI_concepts(commonOntology , a , b);
//        }

//        return score/commonOntologies.size();

        return calculate_simI_concepts(a , b);

    }
    
    public Map<String, List<String>> getConcepts(R a){
        Map<String, List<String>> concepts = new HashMap<>();
        
        for (O commonOntology : commonOntologies) {
            List<String> o_concepts = commonOntology.getConcepts(a);
            
//            System.out.println("Concepts from for Resource " + a.toString() + " from ontology " + commonOntology);
//            for(String concept : o_concepts){
//                System.out.println(concept);
//            }
//            System.out.println();

            String ontologyName = commonOntology.toString();

            if(ontologyName.contains("DBpedia") && !o_concepts.isEmpty())
                concepts.put("DBpedia" , o_concepts);
            
            else if(!o_concepts.isEmpty())
                concepts.put(commonOntology.toString() , o_concepts);
        }
        
//        System.out.println("Concepts of Resource " + a.toString() + " are:");
//        for(Map.Entry<String , List<String>> entry: concepts.entrySet()){
//            for(String concept: entry.getValue()){
//                System.out.println(concept);
//            }
//        }
//        System.out.println();

        return concepts;

    }
    
    private double calculate_simI_concepts(R a , R b){
        double score = 0;
        try {
            commonOntologies = getCommonOntologies(a , b);
        } catch (Exception ex) {
            Logger.getLogger(SimI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Map<String, List<String>> concepts_a = getConcepts(a);
        Map<String, List<String>> concepts_b = getConcepts(b);
        
        for (O commonOntology : commonOntologies) {
            String ontologyName = commonOntology.toString();
            
            if(ontologyName.contains("DBpedia"))
                ontologyName = "DBpedia";
            
            List<String> concepts_a_O = concepts_a.get(ontologyName);
            List<String> concepts_b_O = concepts_b.get(ontologyName);
            
            score = score + this.TverskySimilarity_mod(concepts_a_O, concepts_b_O);
        }
        
        return score/commonOntologies.size();
        
    }
    
    private double calculate_simI_concepts_old(O ontology , R a, R b) {
        double score = 0;

        List<String> features_a = new ArrayList<>() , features_b = new ArrayList<>();
        
        System.out.println("Getting concepts for the ontology " + ontology.toString() +"\n");
        features_a = ontology.getConcepts(a);
//        System.out.println("Feature of Resource " + a.getUri().stringValue() + " from ontology " + ontology.toString() +"\n");
//        for(String feature:features_a){
//            System.out.println(feature + "\n");
//        }
       
        features_b = ontology.getConcepts(b);
//        System.out.println("Feature of Resource " + b.getUri().stringValue() + " from ontology " + ontology.toString() +"\n");
//        for(String feature:features_b){
//            System.out.println(feature + "\n");
//        }
        
        if(features_a.isEmpty() && features_b.isEmpty())
            commonOntologies.remove(ontology);

        score = this.TverskySimilarity_mod(features_a, features_b);

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
                if(ontologyList.contains(ontology) && augmentedOntologies_b.contains(ontology) && !commonOntologies.contains(ontology) ){
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
    public LdSimilarity getMeasure() {
        return this;
    }
    
   
    private double TverskySimilarity_mod(List<String> features_a, List<String> features_b) {
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
    
    
    
}
