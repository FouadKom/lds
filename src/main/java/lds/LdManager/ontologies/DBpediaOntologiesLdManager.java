/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager.ontologies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lds.measures.lods.ontologies.O;
import lds.resource.R;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.vocabulary.OWL;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;

/**
 *
 * @author Fouad Komeiha
 */
public class DBpediaOntologiesLdManager {
    protected LdDataset dataSet;
    protected LdDataset dataSetInitial;    
    protected String baseClassPath = "lds.LdManager.ontologies.OntologiesLdManager.";
    
    public DBpediaOntologiesLdManager(LdDataset dataSetInitial) throws Exception {
        PrefixMapping prefixes = new PrefixMappingImpl();
        prefixes.setNsPrefix("dbpedia", "http://dbpedia.org/resource/");
        prefixes.setNsPrefix("dbpedia-owl", "http://dbpedia.org/ontology/");
  
        this.dataSet = LdDatasetFactory.getInstance()
//                                       .service("https://dbpedia.org/sparql")
                                       .name("dbpedia")
//                                       .defaultGraph("http://dbpedia.org")
                                       .prefixes(prefixes).create(); 
        
        this.dataSetInitial = dataSetInitial;
    }
    
    
    public List<String> getConcepts(R a , List<String> namespacesInitial , List<String> namespacesAugmented , boolean dataAugmentation) {
        
        List<String> concepts = new ArrayList<>();
        List<String> initialConcepts = getInitialConcepts(dataSetInitial , a , namespacesInitial); //gets initial concepts from the main dataset provided by user
        
        concepts.addAll(initialConcepts);
//        System.out.println(initialConcepts.size() + " Initial Concepts\n");
//        System.out.println(concepts.size() + " size of concepts list after adding initial concepts\n");
        
        if(dataAugmentation){
            List<R> sameAsResources = new ArrayList<>(); // same as resources used for dataAugmentation
            List<String> sameAsConcepts = new ArrayList<>(); // initial concepts from same as resources used for dataAugmentation              
        
            sameAsResources = getSameAsResources(dataSetInitial , a , namespacesInitial);
            
            for(R sameAsResource: sameAsResources){
                if(sameAsConcepts.isEmpty()){
                    sameAsConcepts = getInitialConcepts(dataSet , sameAsResource , namespacesAugmented);
//                    System.out.println(sameAsConcepts.size() + " Augmented Concepts");
                }
                
                else{
                    sameAsConcepts.addAll(getInitialConcepts(dataSet , sameAsResource , namespacesAugmented));
//                    System.out.println(sameAsConcepts.size() + " Augmented Concepts");
                    
                }
            }      
    
            if(! sameAsConcepts.isEmpty() ){
                concepts.addAll(sameAsConcepts);
//                System.out.println(concepts.size() + " size of concepts list after adding augmented concepts\n");
            }
        }
        
        
        return concepts;
    }
    
    
    private List<String> getInitialConcepts(LdDataset dataSetInitial , R a , List<String> namespaces){
        
        List<String> initialConcepts = new ArrayList<>();
        
        ParameterizedSparqlString query_cmd = dataSetInitial.prepareQuery();      
        
        String queryStr = "SELECT * " + (dataSetInitial.getDefaultGraph() == null ? ("") : "from <" + dataSetInitial.getDefaultGraph()+ ">") 
                                      + " WHERE  { <" + a.getUri() + "> a ?c."
                                      + " FILTER  ( REGEX (STR (?c), ";
        
        for(String namespace: namespaces){
            queryStr = queryStr + namespace + ") ||  REGEX (STR (?c), ";
        }
        
        queryStr = queryStr.substring(0 , queryStr.length() - 23);
        
        queryStr = queryStr + ") )}";
        
//        System.out.println("Getting initial concepts from initial dataset\n" + queryStr + "\n");
               
        query_cmd.setCommandText(queryStr);

        ResultSet rs = dataSetInitial.executeSelectQuery(query_cmd.toString());

        for (; rs.hasNext();) {
            QuerySolution soln = rs.nextSolution();
            String sameConcept = soln.getResource("c").getURI();
            initialConcepts.add(sameConcept);

        }
        
        return initialConcepts;
    }
    
    
    private List<R> getSameAsResources(LdDataset dataSetInitial , R a , List<String> namespaces){
        
        List<R> sameAsResources = new ArrayList<>();
        
        ParameterizedSparqlString query_cmd = dataSetInitial.prepareQuery(); 
        
        String queryStr = "SELECT * " + (dataSetInitial.getDefaultGraph() == null ? ("") : " from <" + dataSetInitial.getDefaultGraph()+ ">") 
                                + " WHERE  { <" + a.getUri() + "> <" + OWL.sameAs + "> ?c."
                                + " FILTER  ( REGEX (STR (?c), ";
        
        for(String namespace: namespaces){
            queryStr = queryStr + namespace + ") ||  REGEX (STR (?c), ";
        }
        
        queryStr = queryStr.substring(0 , queryStr.length() - 23);
        
        queryStr = queryStr + ") )}";
        
//        System.out.println("Getting getSameAsResources from initial dataset\n" + queryStr + "\n");
        
        query_cmd.setCommandText(queryStr);
        
        ResultSet rs = dataSetInitial.executeSelectQuery(query_cmd.toString());

        for (; rs.hasNext();) {
            QuerySolution soln = rs.nextSolution();
            R sameResource = new R(soln.getResource("c").getURI());
            sameAsResources.add(sameResource);
        }

        
        return sameAsResources;      
        
    }
    
}
