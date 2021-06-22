/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager.ontologies;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.indexing.LdIndex;
import lds.indexing.LdIndexerManager;
import lds.resource.R;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.vocabulary.OWL;
import ldq.LdDataset;
import ldq.LdDatasetFactory;

/**
 *
 * @author Fouad Komeiha
 */
public class WikiDataLdManager {
    private boolean useIndex;
    private LdDataset dataSet;
    private LdDataset dataSetInitial;
    
    private String baseClassPath = "lds.LdManager.ontologies.WikiDataLdManager.";
    
    private LdIndexerManager manager;
    private LdIndex conceptsIndex;
    private LdIndex categoriesIndex;
                      
    
    public WikiDataLdManager(LdDataset dataSetInitial , boolean useIndex) {
        
        try {
            this.dataSet = LdDatasetFactory.getInstance()
                                           .service("http://query.wikidata.org/bigdata/namespace/wdq/sparql")
                                           .name("wikiData")
                                           .create();
            
        } catch (Exception ex) {
            Logger.getLogger(WikiDataLdManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        this.useIndex = useIndex;
        this.dataSetInitial = dataSetInitial;
       
    }
    
    public List<String> getConcepts(R a , boolean dataAugmentation){
        if(useIndex){
            
            return conceptsIndex.getListFromIndex(dataSetInitial , a.getUri().stringValue() , baseClassPath + "getConcepts" , dataSetInitial , a , dataAugmentation);
        }
        
        return this.getConcepts(dataSetInitial , a , dataAugmentation);
    }
    
    
    private List<String> getConcepts(LdDataset dataSetInitial , R a , boolean dataAugmentation) {
  
        List<String> concepts = new ArrayList<>();
        List<String> initialConcepts = getInitialConcepts(dataSetInitial , a); //gets initial concepts from the main dataset provided by user
        
        for(String initialConcept: initialConcepts){  
            System.out.println("Super Concepts for initial concept: " + initialConcept + " are:");
            if(concepts.isEmpty()){
                concepts = getSuperConceptsFromInitial(initialConcept);
                
                }
                else{
                    concepts.addAll(getSuperConceptsFromInitial(initialConcept));
                }
            
            for(String concept: concepts){ 
                System.out.println(concept + "\n");
            }
        }
        
        System.out.println("Finished Getting initial concepts and their superclasses");
        
        if(dataAugmentation){
            List<String> sameAsResources = new ArrayList<>(); // same as resources used for dataAugmentation
            List<String> sameAsConcepts = new ArrayList<>(); // initial concepts from same as resources used for dataAugmentation
            List<String> augmentedConcepts = new ArrayList<>(); // superConcepts from intial concepts augmented using sameAs
                
        
            sameAsResources = getSameAsResources(dataSetInitial , a);
            
            System.out.println(sameAsResources.size() + " sameAs resources");
            
            sameAsConcepts = getInitialConceptsFromSameAsResources(sameAsResources);
            System.out.println("Finished Getting initial concepts of same as resources");   
            
            for(String sameAsConcept: sameAsConcepts){
                if(sameAsConcept.equals("http://www.wikidata.org/entity/Q208511"))
                    continue;
                
                if(augmentedConcepts.isEmpty()){
                    augmentedConcepts = getSuperConceptsFromInitial(sameAsConcept);
                 
                }
                else{
                    augmentedConcepts.addAll(getSuperConceptsFromInitial(sameAsConcept));
                }
                
                
            }
            
            concepts.addAll(sameAsConcepts);
            concepts.addAll(augmentedConcepts);
        }
        
        concepts.addAll(initialConcepts);  
        
        System.out.println("Finished Getting all concepts");
        return concepts;
    }
    
    private List<String> getInitialConcepts(LdDataset dataSetInitial , R a){
        
        List<String> initialConcepts = new ArrayList<>();
        
        ParameterizedSparqlString query_cmd = dataSetInitial.prepareQuery();       

        String query = "SELECT * " + (dataSetInitial.getDefaultGraph() == null ? ("") : " from <" + dataSetInitial.getDefaultGraph()+ ">") 
                                   + "WHERE  { <" + a.getUri() + "> a ?c."
                                   + "FILTER  ( REGEX (STR (?c), \"http://www.wikidata.org/entity/\") )}";


        query_cmd.setCommandText(query);
        ResultSet rs = dataSetInitial.executeSelectQuery(query_cmd.toString());

        for (; rs.hasNext();) {
            QuerySolution soln = rs.nextSolution();
            String sameConcept = soln.getResource("c").getURI();
            initialConcepts.add(sameConcept);

        }
        
        return initialConcepts;
    }
    
    
    
    private List<String> getSuperConceptsFromInitial(String initialConcept){
        List<String> concepts = new ArrayList<>();
        
        ParameterizedSparqlString query_cmd = dataSet.prepareQuery();
        
        // get superClasses:
        String query = "select * where { <" + initialConcept + ">  " + " <http://www.wikidata.org/prop/direct/P279>* ?c."
                                            + " filter (?c != <" + initialConcept + ">)}";
                
        query_cmd.setCommandText(query);

        ResultSet rs = dataSet.executeSelectQuery(query_cmd.toString());
        
        for (; rs.hasNext();) {
            QuerySolution soln = rs.nextSolution();
            String sameConcept = soln.getResource("c").getURI();
            
        }    
        
        System.out.println(initialConcept + " : " + concepts.size());
        return concepts;
    }
    
    
    private List<String> getSameAsResources(LdDataset dataSetInitial , R a){
        
        List<String> sameAsResources = new ArrayList<>();
        
        ParameterizedSparqlString query_cmd = dataSetInitial.prepareQuery(); 
        
        String query = "SELECT * " + (dataSetInitial.getDefaultGraph() == null ? ("") : " from <" + dataSetInitial.getDefaultGraph()+ ">") 
                                   + " WHERE  { <" + a.getUri() + "> <" + OWL.sameAs + "> ?c."
                                   + " FILTER  ( REGEX (STR (?c), \"http://www.wikidata.org/entity/\") )}";
            
            
        query_cmd.setCommandText(query);
        ResultSet rs = dataSetInitial.executeSelectQuery(query_cmd.toString());

        for (; rs.hasNext();) {
            QuerySolution soln = rs.nextSolution();
            String sameResource = soln.getResource("c").getURI();
            sameAsResources.add(sameResource);
        }

        
        return sameAsResources;      
        
    }
    
    private List<String> getInitialConceptsFromSameAsResources(List<String> sameAsResources){
        
        List<String> extraConcepts = new ArrayList<>();
        
        ParameterizedSparqlString query_cmd = dataSet.prepareQuery();
        
        
        for(String sameAsResource : sameAsResources){
            System.out.println("Initial Concepts for sameAs resource: " + sameAsResource + " are:");
        
            // Get instances
            String query = "select * where { <" + sameAsResource + "> <http://www.wikidata.org/prop/direct/P31> ?c }";

            query_cmd.setCommandText(query);

            ResultSet rs = dataSet.executeSelectQuery(query_cmd.toString());

            for (; rs.hasNext();) {
                QuerySolution soln = rs.nextSolution();
                String sameConcept = soln.getResource("c").getURI();
                
                System.out.println(sameConcept + "\n");

                if (!sameConcept.contains("http://www.wikidata.org/entity/Q16521")) {
                    extraConcepts.add(sameConcept);
                } 
//                else {
//                    // Get taxons
//                    query = "select * where {<" + sameAsResource + ">  <http://www.wikidata.org/prop/direct/P171>* ?pTaxon. }";
//
//                    query_cmd.setCommandText(query);
//                    rs = dataSet.executeSelectQuery(query_cmd.toString());
//
//                    for (; rs.hasNext();) {
//                        soln = rs.nextSolution();
//                        String pTaxon = soln.getResource("pTaxon").getURI();
//                        extraConcepts.add(pTaxon);
//                    }
//                }
            }
        }
        
        return extraConcepts;
        
    }
    
    public List<String> getCategories(R a , boolean dataAugmentation){
        if(useIndex){
            
            return categoriesIndex.getListFromIndex(dataSetInitial , a.getUri().stringValue() , baseClassPath + "getCategories" , dataSetInitial , a , dataAugmentation);
        }
        
        return this.getCategories(dataSetInitial , a , dataAugmentation);
    }
    
    private List<String> getCategories(LdDataset dataSetInitial, R a, boolean dataAugmentation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadIndexes() throws Exception {
        manager = LdIndexerManager.getManager();
        String conceptsIndexFile = System.getProperty("user.dir") + "/Indexes/Ontologies/DBpedia/concepts_index_" + dataSetInitial.getName().toLowerCase().replace(" ", "_") + ".db";
        String categoriesIndexFile = System.getProperty("user.dir") + "/Indexes/Ontologies/DBpedia/categories_index_" + dataSetInitial.getName().toLowerCase().replace(" ", "_") + ".db";
        categoriesIndex = manager.loadIndex(categoriesIndexFile);
        conceptsIndex = manager.loadIndex(conceptsIndexFile);
    }

    public void closeIndexes() {
         if (useIndex) {
            manager.closeIndex(conceptsIndex);
        }
        
    }

    
    
    
}
