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
import lds.indexing.LdIndexer;
import lds.resource.R;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.vocabulary.OWL;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class WikiDataLdManager {
    private boolean useIndex;
    private LdDataset dataSet;
    private LdDataset dataSetInitial;
    
    private String baseClassPath = "lds.LdManager.ontologies.WikiDataLdManager.";
    
    private LdIndexer conceptsIndex;
                      
    
    public WikiDataLdManager(LdDataset dataSetInitial , boolean useIndex) {
        
        try {
            this.dataSet = LdDatasetFactory.getInstance()
                    .service("https://query.wikidata.org/bigdata/namespace/wdq/sparql")
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
             return LdIndexer.getListFromIndex(dataSetInitial , conceptsIndex , a.getUri().stringValue() , baseClassPath + "getConcepts" , dataSetInitial , a , dataAugmentation);
        }
        
        return this.getConcepts(dataSetInitial , a , dataAugmentation);
    }
    
    
    private List<String> getConcepts(LdDataset dataSetInitial , R a , boolean dataAugmentation) {
  
        List<String> concepts = new ArrayList<>();
        List<String> initialConcepts = getInitialConcepts(dataSetInitial , a); //gets initial concepts from the main dataset provided by user
        
        for(String initialConcept: initialConcepts){
            concepts = getSuperConceptsFromInitial(initialConcept);
        }
        
        if(dataAugmentation){
            List<String> sameAsResources = null; // same as resources used for dataAugmentation
            List<String> sameAsConcepts = null; // initial concepts from same as resources used for dataAugmentation
            List<String> augmentedConcepts = null; // superConcepts from intial concepts augmented using sameAs
                
        
            sameAsResources = getSameAsResources(dataSetInitial , a);
            sameAsConcepts = getInitialConceptsFromSameAsResources(dataSet , sameAsResources);
               
        
            for(String sameAsConcept: sameAsConcepts){
                augmentedConcepts = getSuperConceptsFromInitial(sameAsConcept);
            }
            
            concepts.addAll(sameAsConcepts);
            concepts.addAll(augmentedConcepts);
        }
        
        concepts.addAll(initialConcepts);        
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
        String query = "select * where { <" + initialConcept + ">  " + " <http://www.wikidata.org/prop/direct/P279> ?c.}";
                
        query_cmd.setCommandText(query);

        ResultSet rs = dataSet.executeSelectQuery(query_cmd.toString());
        
        for (; rs.hasNext();) {
            QuerySolution soln = rs.nextSolution();
            String sameConcept = soln.getResource("c").getURI();
            concepts.add(sameConcept);

        }
        
        
        /*       
        
        // get same calsses
        query = "select * where { <" + initialConcept + "> <http://www.wikidata.org/prop/direct/P1709> ?same }";

        query_cmd.setCommandText(query);
        rs = dataSet.executeSelectQuery(query_cmd.toString());

        for (; rs.hasNext();) {
            QuerySolution soln = rs.nextSolution();
            String sameConcept = soln.getResource("same").getURI();
            concepts.add(sameConcept);
        }
        */
        
        
        
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
    
    private List<String> getInitialConceptsFromSameAsResources(LdDataset dataSetInitial , List<String> sameAsResources){
        
        List<String> extraConcepts = new ArrayList<>();
        
        ParameterizedSparqlString query_cmd = dataSetInitial.prepareQuery();
        
        for(String sameAsResource : sameAsResources){
        
            // Get instances
            String query = "select * where { <" + sameAsResource + "> <http://www.wikidata.org/prop/direct/P31> ?c }";

            query_cmd.setCommandText(query);

            ResultSet rs = dataSet.executeSelectQuery(query_cmd.toString());

            for (; rs.hasNext();) {
                QuerySolution soln = rs.nextSolution();
                String sameConcept = soln.getResource("c").getURI();

                if (!sameConcept.contains("http://www.wikidata.org/entity/Q16521")) {
                    extraConcepts.add(sameConcept);
                } 
                else {
                    // Get taxons
                    query = "select * where {<" + sameAsResource + ">  <http://www.wikidata.org/prop/direct/P171>* ?pTaxon. }";

                    query_cmd.setCommandText(query);
                    rs = dataSet.executeSelectQuery(query_cmd.toString());

                    for (; rs.hasNext();) {
                        soln = rs.nextSolution();
                        String pTaxon = soln.getResource("pTaxon").getURI();
                        extraConcepts.add(pTaxon);
                    }
                }
            }
        }
        
        return extraConcepts;
        
    }

    public void loadIndexes() throws Exception {
        String conceptsIndexFile = System.getProperty("user.dir") + "/Indexes/Ontologies/DBpedia/concepts_index_" + dataSetInitial.getName().toLowerCase().replace(" ", "_") + ".db";
        conceptsIndex = new LdIndexer(conceptsIndexFile);
    }

    public void closeIndexes() {
         if (useIndex) {
            conceptsIndex.close();
        }
        
    }
    
    
}
