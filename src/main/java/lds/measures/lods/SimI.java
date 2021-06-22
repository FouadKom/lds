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
import ldq.LdDataset;
import lds.measures.LdSimilarity;

/**
 *
 * @author Fouad Komeiha
 */
public class SimI extends LODS implements LdSimilarity {
    private boolean useIndeses;
    private List<O> ontologyList;
    private List<O> commonOntologies;
    private boolean dataAugmentation;
    private SimILdManager simIldManager;
    private Config config;
    
    
    public SimI(Config config) throws Exception{
        super(config);
        if(config.getParam(ConfigParam.LdDatasetMain) == null || config.getParam(ConfigParam.useIndexes) == null || config.getParam(ConfigParam.ontologyList) == null )
            throw new Exception("Some configuration parameters missing"); 

        this.simIldManager = new SimILdManager((LdDataset) config.getParam(ConfigParam.LdDatasetMain) , (Boolean) config.getParam(ConfigParam.useIndexes) );
        this.useIndeses = (Boolean) config.getParam(ConfigParam.useIndexes);
        this.ontologyList = (List<O>) config.getParam(ConfigParam.ontologyList);
        if(config.getParam(ConfigParam.dataAugmentation) == null)
            this.dataAugmentation = false;
        else
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
    
    private Map<String, List<String>> getConcepts(R a){
        Map<String, List<String>> concepts = new HashMap<>();
        
        for (O commonOntology : commonOntologies) {
            List<String> o_concepts = commonOntology.getConcepts(a);
            
            String ontologyName = commonOntology.toString();

            if(ontologyName.contains("DBpedia") && !o_concepts.isEmpty())
                concepts.put("DBpedia" , o_concepts);
            
            else if(!o_concepts.isEmpty())
                concepts.put(commonOntology.toString() , o_concepts);
        }
        
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
            
            score = score + LODS.TverskySimilarity_mod(concepts_a_O, concepts_b_O);
        }
        
        return score/commonOntologies.size();
        
    }
 
/*
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

        score = LODS.TverskySimilarity_mod(features_a, features_b);

        return score;
        
    }
*/

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
    
}
