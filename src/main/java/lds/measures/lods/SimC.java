/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.lods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.LdManager.SimCLdManager;
import lds.LdManager.SimILdManager;
import lds.LdManager.ontologies.Ontology;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.resource.R;
import lds.measures.LdSimilarity;
import lds.measures.lods.ontologies.O;
import ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class SimC extends LODS implements LdSimilarity{
    private boolean useIndeses;
    private List<O> ontologyList;
    private List<O> commonOntologies;
    private boolean dataAugmentation;
    private SimCLdManager simCldManager;
    private Config config;
    private LdDataset dataset;
    
    private int supLevel;
    private int subLevel;

    public SimC(Config config) throws Exception {
        super(config);
//        if(config.getParam(ConfigParam.LdDatasetMain) == null || config.getParam(ConfigParam.useIndexes) == null || config.getParam(ConfigParam.ontologyList) == null )
//            throw new Exception("Some configuration parameters missing"); 
        
        if(config.getParam(ConfigParam.LdDatasetMain) == null || config.getParam(ConfigParam.useIndexes) == null)
            throw new Exception("Some configuration parameters missing"); 
        
        this.dataset = (LdDataset) config.getParam(ConfigParam.LdDatasetMain);
        
        this.simCldManager = new SimCLdManager((LdDataset) config.getParam(ConfigParam.LdDatasetMain) , (Boolean) config.getParam(ConfigParam.useIndexes) );
        this.useIndeses = (Boolean) config.getParam(ConfigParam.useIndexes);
        this.ontologyList = (List<O>) config.getParam(ConfigParam.ontologyList);
        
        if(config.getParam(ConfigParam.dataAugmentation) == null)
            this.dataAugmentation = false;
        else
            this.dataAugmentation = (Boolean) config.getParam(ConfigParam.dataAugmentation);
        this.config = config;
        
        if(config.getParam(ConfigParam.sub) == null && config.getParam(ConfigParam.sup) == null){
            this.subLevel = 0;
            this.subLevel = 0;
        }
        else{
            this.subLevel = (Integer) config.getParam(ConfigParam.sub);
            this.supLevel = (Integer) config.getParam(ConfigParam.sup);
        }
    }

    @Override
    public double compare(R a, R b){
        
        double score = 0;

        try {
            
            commonOntologies = getCommonOntologies(a, b);
            
        } catch (Exception ex) {
            Logger.getLogger(SimI.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

        if(commonOntologies == null || commonOntologies.isEmpty())
            return -1;

      
        try {
            score = calculate_simC_categories(a , b);
        } catch (Exception ex) {
            Logger.getLogger(SimC.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
       
        return score;

    }
    
    /*
    private double calculate_simC_categories(R a, R b) { 
        double score = 0;       
        
            
        try {
            commonOntologies = getCommonOntologies(a , b);

        } catch (Exception ex) {
            Logger.getLogger(SimI.class.getName()).log(Level.SEVERE, null, ex);
        }

        Map<String, List<String>> categories_a = getCategories(a);
        Map<String, List<String>> categories_b = getCategories(b);


        for (O commonOntology : commonOntologies) {
            String ontologyName = commonOntology.toString();

            if(ontologyName.contains("DBpedia"))
                ontologyName = "DBpedia";

            List<String> categories_a_O = categories_a.get(ontologyName);
            List<String> categories_b_O = categories_b.get(ontologyName);
            
            System.out.println(categories_a_O);
            System.out.println(categories_b_O);

            score = score + LODS.TverskySimilarity_mod(categories_a_O, categories_b_O);
        }
        
       
        
        return score/commonOntologies.size();
        
    }
    
    private Map<String, List<String>> getCategories(R a){
        Map<String, List<String>> categories = new HashMap<>();       
        
        
        for (O commonOntology : commonOntologies) {
            List<String> o_categories = commonOntology.getCategories(a);
            
            String ontologyName = commonOntology.toString();

            if(ontologyName.contains("DBpedia") && !o_categories.isEmpty())
                categories.put("DBpedia" , o_categories);
            
            else if(!o_categories.isEmpty())
                categories.put(commonOntology.toString() , o_categories);
        }
        
        return categories;

    }
    */

    
    private double calculate_simC_categories(R a, R b) throws Exception { 
        String defaultGraph = dataset.getDefaultGraph();
        
        O initialOntology = Ontology.getOntologyFromDefaultGraph(defaultGraph);
        
        initialOntology.initializeOntology(config);
        
        Map<String, List<String>> categories_a = this.getCategories(a, initialOntology , supLevel , subLevel); 
        Map<String, List<String>> categories_b = this.getCategories(b, initialOntology , supLevel , subLevel);

        /*Map<String, List<String>> categories_a = new HashMap<>(); 
        Map<String, List<String>> categories_b = new HashMap<>();        
        
        categories_a.putAll(this.get_sub_categories(a, initialOntology, subLevel));
        categories_a.putAll(this.get_sup_categories(a, initialOntology, supLevel));
        
        categories_b.putAll(this.get_sub_categories(b, initialOntology, subLevel));
        categories_b.putAll(this.get_sup_categories(b, initialOntology, supLevel));
        
        if(dataAugmentation){
            commonOntologies = getCommonOntologies(a , b);
            categories_a.putAll(this.get_augmented_categories(a, initialOntology, commonOntologies));
            categories_b.putAll(this.get_augmented_categories(b, initialOntology, commonOntologies));
            
        }*/
        
        if(dataAugmentation){
            commonOntologies = getCommonOntologies(a , b);
            
            
        }
        
        return get_score(categories_a , categories_b);
        
//        String initialOntologyName = initialOntology.toString();
//        
//        if(initialOntologyName.contains("DBpedia"))
//                    initialOntologyName = "DBpedia";
//        
//        initialOntology.initializeOntology(config);
//        
//        Map<String, List<String>> categories_a = new HashMap<>();
//        Map<String, List<String>> categories_b = new HashMap<>();
//        
//        categories_a.put(initialOntologyName , initialOntology.getCategories(a));
//        categories_b.put(initialOntologyName , initialOntology.getCategories(b));
//                
//        if(dataAugmentation){
//            
//            try {
//                commonOntologies = getCommonOntologies(a , b);
//                commonOntologies.remove(initialOntology);
//
//            } catch (Exception ex) {
//                Logger.getLogger(SimI.class.getName()).log(Level.SEVERE, null, ex);
//            }              
//
//            for (O commonOntology : commonOntologies) {
//                String ontologyName = commonOntology.toString();
//
//                if(ontologyName.contains("DBpedia"))
//                    ontologyName = "DBpedia";
//                
//                categories_a.put(ontologyName , commonOntology.getCategories(a));
//                categories_b.put(ontologyName , commonOntology.getCategories(b));
//
//                List<String> categories_a_O = categories_a.get(ontologyName);
//                List<String> categories_b_O = categories_b.get(ontologyName);                
//
//                score = score + LODS.TverskySimilarity_mod(categories_a_O , categories_b_O);
//                size = size + commonOntologies.size();
//            }
//        }
//        
//       List<String> categories_a_initial = categories_a.get(initialOntologyName);
//       List<String> categories_b_initial = categories_b.get(initialOntologyName);
//       
//       score = score + LODS.TverskySimilarity_mod(categories_a_initial , categories_b_initial);
//       
//       return score/size;
        
    }
    
    private double get_score(Map<String, List<String>> categories_a , Map<String, List<String>> categories_b){
        double score = 0;
        for (Map.Entry<String, List<String>> entry : categories_a.entrySet()) {
            String key = entry.getKey();
            List<String> categories_a_O = entry.getValue();
            List<String> categories_b_O = categories_b.get(key);
            score = score + LODS.TverskySimilarity_mod(categories_a_O , categories_b_O);

        }
        
        return score/categories_a.size();
        
    }

//    private double calculate_simC_categories_level_0(R a, R b) throws Exception{
//        double score = 0;
//        int size =1;
//        
//        String defaultGraph = dataset.getDefaultGraph();
//        
//        O initialOntology = Ontology.getOntologyFromDefaultGraph(defaultGraph);
//        
//        String initialOntologyName = initialOntology.toString();
//        
//        if(initialOntologyName.contains("DBpedia"))
//                    initialOntologyName = "DBpedia";
//        
//        initialOntology.initializeOntology(config);
//       
//        List<String> categories_a_initial = initialOntology.getCategories(a);
//        List<String> categories_b_initial = initialOntology.getCategories(b);
//       
//        score = score + LODS.TverskySimilarity_mod(categories_a_initial , categories_b_initial);
//       
//        return score;
//    }
    
    private Map<String , List<String>> getCategories(R a,  O initialOntology , int super_level , int sub_level) throws Exception {
        Map<String , List<String>> categories = get_initial_categories(a , initialOntology);
        
        String initialOntologyName = initialOntology.toString();        
         
        if(initialOntologyName.contains("DBpedia"))
                    initialOntologyName = "DBpedia";
        
        List<String> categories_a = categories.get(initialOntologyName);
        
        for(String category:categories_a){
                categories.putAll(get_Extra_Categories(new R(category) , initialOntology , super_level , sub_level));
        }        
        
        return categories;
    }
    
    private Map<String , List<String>> get_initial_categories(R a,  O initialOntology ) throws Exception {
        Map<String , List<String>> categories = new HashMap<>();
         
        String initialOntologyName = initialOntology.toString();        
         
        if(initialOntologyName.contains("DBpedia"))
                    initialOntologyName = "DBpedia";
        
        initialOntology.initializeOntology(config);
       
       categories.put(initialOntologyName, initialOntology.getCategories(a));
       
       return categories;
    }
    
    private Map<String , List<String>> get_Extra_Categories(R a,  O initialOntology , int super_level , int sub_level) throws Exception {
        Map<String , List<String>> categories = new HashMap<>();
        
        String ontologyName = initialOntology.toString();

        if(ontologyName.contains("DBpedia"))
            ontologyName = "DBpedia";
        
        if( (super_level > supLevel || super_level > 2) || (sub_level > supLevel || sub_level > 2))
            return categories;   
        
        List<String> categories_a = initialOntology.getBroaderCategories(a, 0);
        categories_a.addAll(initialOntology.getNarrowerCategories(a, 0));
        
        categories.put(ontologyName, categories_a);
        
        for(String category:categories_a){
            R subject = new R(category);
            O subject_ontology = Ontology.getOntologyFromNameSpace(subject.getNamespace());
            subject_ontology.initializeOntology(config);
            
            categories.putAll(get_Broader_Categories(subject , subject_ontology  , super_level + 1));
            categories.putAll(get_Narrower_Categories(subject , subject_ontology  , sub_level + 1));
        }
        
        return categories;
    }
    
     private Map<String , List<String>> get_Broader_Categories(R a,  O initialOntology , int super_level ) throws Exception {
        Map<String , List<String>> categories = new HashMap<>();
        
        String ontologyName = initialOntology.toString();

        if(ontologyName.contains("DBpedia"))
            ontologyName = "DBpedia";
        
        if( super_level > supLevel || super_level > 2)
            return categories;   
        
        List<String> categories_a = initialOntology.getBroaderCategories(a, 0);
         
        categories.put(ontologyName, categories_a);
        
        for(String category:categories_a){
            R subject = new R(category);
            O subject_ontology = Ontology.getOntologyFromNameSpace(subject.getNamespace());
            subject_ontology.initializeOntology(config);
            
            categories.putAll(get_Broader_Categories(subject , subject_ontology  , super_level + 1));

        }
        
        return categories;
    }
    
    private Map<String , List<String>> get_Narrower_Categories(R a,  O initialOntology , int level) throws Exception {
        Map<String , List<String>> categories = new HashMap<>();
        
        String ontologyName = initialOntology.toString();

        if(ontologyName.contains("DBpedia"))
            ontologyName = "DBpedia";
        
        if(level > supLevel || level > 2)
            return categories;        
        
        List<String> categories_a = initialOntology.getBroaderCategories(a, 0);
        categories.put(ontologyName, categories_a);
        
        for(String category:categories_a){
            R subject = new R(category);
            O subject_ontology = Ontology.getOntologyFromNameSpace(subject.getNamespace());
            subject_ontology.initializeOntology(config);
            
            categories.putAll(get_Narrower_Categories(subject , subject_ontology  , level + 1));
        }
        
        return categories;
    }
    
    
//    private double calculate_simC_categories_level_0_augmented(R a, R b , O initialOntology) throws Exception{
//        double score = 0;
//        
//        try {
//           commonOntologies = getCommonOntologies(a , b);
//           commonOntologies.remove(initialOntology);
//
//        } catch (Exception ex) {
//            Logger.getLogger(SimI.class.getName()).log(Level.SEVERE, null, ex);
//        }              
//
//        for (O commonOntology : commonOntologies) {
//            String ontologyName = commonOntology.toString();
//
//            if(ontologyName.contains("DBpedia"))
//                ontologyName = "DBpedia";
//
//            List<String> categories_a_O = commonOntology.getCategories(a);
//            List<String> categories_b_O = commonOntology.getCategories(a);                
//
//            score = score + LODS.TverskySimilarity_mod(categories_a_O , categories_b_O);
//        }
//            
//        return score/commonOntologies.size();
//    }
    
    private Map<String , List<String>> get_augmented_categories(R a,  O initialOntology , List<O> commonOntologies) {
        Map<String , List<String>> categories = new HashMap<>();
        
        commonOntologies.remove(initialOntology);
        
        for (O commonOntology : commonOntologies) {
            String ontologyName = commonOntology.toString();

            if(ontologyName.contains("DBpedia"))
                ontologyName = "DBpedia";

            categories.put(ontologyName , commonOntology.getCategories(a));             

        }
        
        return categories;
        
    }
      

    @Override
    public void closeIndexes() {
        if(useIndeses){
            simCldManager.closeIndexes();
        }
        Ontology.closeIndexes();
    }

    @Override
    public void loadIndexes() throws Exception {
        if(useIndeses){
            simCldManager.loadIndexes();
        }
        Ontology.loadIndexes();
    }

    @Override
    public LdSimilarity getMeasure() {
        return this;
    }


    
}
