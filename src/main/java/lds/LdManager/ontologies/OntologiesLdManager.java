/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager.ontologies;

import java.util.ArrayList;
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
public class OntologiesLdManager {
    protected LdDataset dataSet;
    protected LdDataset dataSetInitial;
    
    protected String baseClassPath = "lds.LdManager.ontologies.OntologiesLdManager.";
    
    public OntologiesLdManager(LdDataset dataSetInitial) throws Exception {
        PrefixMapping prefixes = new PrefixMappingImpl();
        prefixes.setNsPrefix("dbpedia", "http://dbpedia.org/resource/");
        prefixes.setNsPrefix("dbpedia-owl", "http://dbpedia.org/ontology/");
  
        this.dataSet = LdDatasetFactory.getInstance()
                .service("https://dbpedia.org/sparql")
                .name("dbpedia")
                .defaultGraph("http://dbpedia.org")
                .prefixes(prefixes).create(); 
        
        this.dataSetInitial = dataSetInitial;
    }
    
//    public List<String> getConcepts(R a , List<String> namespaces){
//        List<String> dbpedia_features = new ArrayList<>();
//        
//        ParameterizedSparqlString query_cmd = dataset.prepareQuery();
//        
//        String queryStr = "SELECT * " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") 
//                                                   + "WHERE  { <" + a.getUri() + "> a ?c."
//                                                   + "FILTER  ( REGEX (STR (?c), ";
//        
//        for(String namespace: namespaces){
//            queryStr = queryStr + namespace + ") ||  REGEX (STR (?c), ";
//        }
//        
//        queryStr = queryStr.substring(0 , queryStr.length() - 23);
//        
//        queryStr = queryStr + ") )}";
//               
//        query_cmd.setCommandText(queryStr);
//            
//        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());
//
//        while (resultSet.hasNext()) {
//            QuerySolution soln = resultSet.nextSolution();
//            Resource c = soln.getResource("c");
//            dbpedia_features.add(c.getURI());
//        }
//        
//        return dbpedia_features;
//    }
    
    
       public List<String> getConcepts(R a , List<String> namespaces , boolean dataAugmentation) {
  
        List<String> concepts = new ArrayList<>();
        List<String> initialConcepts = getInitialConcepts(dataSetInitial , a , namespaces); //gets initial concepts from the main dataset provided by user
        
//        for(String initialConcept: initialConcepts){
//            concepts = getSuperConceptsFromInitial(initialConcept);
//        }
        
        concepts.addAll(initialConcepts);
        
        if(dataAugmentation){
            List<R> sameAsResources = null; // same as resources used for dataAugmentation
            List<String> sameAsConcepts = null; // initial concepts from same as resources used for dataAugmentation
            List<String> augmentedConcepts = null; // superConcepts from intial concepts augmented using sameAs                
        
            sameAsResources = getSameAsResources(dataSetInitial , a , namespaces);
            System.out.println(sameAsResources.size());
            
            for(R sameAsResource: sameAsResources){
                if(sameAsConcepts.isEmpty()){
                    sameAsConcepts = getInitialConcepts(dataSetInitial , sameAsResource , namespaces);
                    System.out.println("Size of sameAsConcepts for " + a.getUri().toString() + " is " + sameAsConcepts.size());
                }
                else{
                    sameAsConcepts.addAll(getInitialConcepts(dataSetInitial , sameAsResource , namespaces));
                    System.out.println("Size of sameAsConcepts for " + a.getUri().toString() + " is " + sameAsConcepts.size());
                }
            }      
    
            if(sameAsConcepts != null){
//                for(String sameAsConcept: sameAsConcepts){
//                    augmentedConcepts = getSuperConceptsFromInitial(sameAsConcept);
//                }
//            
                concepts.addAll(sameAsConcepts);
//                concepts.addAll(augmentedConcepts);
            }
        }
        
        return concepts;
    }
    
    private List<String> getInitialConcepts(LdDataset dataSetInitial , R a , List<String> namespaces){
        
        List<String> initialConcepts = new ArrayList<>();
        
        ParameterizedSparqlString query_cmd = dataSetInitial.prepareQuery();      
        
        String queryStr = "SELECT * " + (dataSetInitial.getDefaultGraph() == null ? ("") : "from <" + dataSetInitial.getDefaultGraph()+ ">") 
                                      + "WHERE  { <" + a.getUri() + "> a ?c."
                                      + "FILTER  ( REGEX (STR (?c), ";
        
        for(String namespace: namespaces){
            queryStr = queryStr + namespace + ") ||  REGEX (STR (?c), ";
        }
        
        queryStr = queryStr.substring(0 , queryStr.length() - 23);
        
        queryStr = queryStr + ") )}";
               
        query_cmd.setCommandText(queryStr);

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
        String query = "SELECT * " + (dataSet.getDefaultGraph() == null ? ("") : "from <" + dataSet.getDefaultGraph()+ ">") 
                                   + "WHERE  { <" + initialConcept + "> <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?c.}";
                
        query_cmd.setCommandText(query);

        ResultSet rs = dataSet.executeSelectQuery(query_cmd.toString());
        
        for (; rs.hasNext();) {
            QuerySolution soln = rs.nextSolution();
            String sameConcept = soln.getResource("c").getURI();
            concepts.add(sameConcept);

        }
        
        return concepts;
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
        
        System.out.println(queryStr);
        
        query_cmd.setCommandText(queryStr);
        
        ResultSet rs = dataSetInitial.executeSelectQuery(query_cmd.toString());

        for (; rs.hasNext();) {
            QuerySolution soln = rs.nextSolution();
            R sameResource = new R(soln.getResource("c").getURI());
            sameAsResources.add(sameResource);
        }

        
        return sameAsResources;      
        
    }
    
//    private List<String> getInitialConceptsFromSameAsResources(LdDataset dataSetInitial , List<String> sameAsResources){
//        
//        List<String> extraConcepts = new ArrayList<>();
//        
//        ParameterizedSparqlString query_cmd = dataSetInitial.prepareQuery();
//        
//        for(String sameAsResource : sameAsResources){
//        
//            // Get instances
//            String query = "select * where { <" + sameAsResource + "> <http://www.wikidata.org/prop/direct/P31> ?c }";
//
//            query_cmd.setCommandText(query);
//
//            ResultSet rs = dataSet.executeSelectQuery(query_cmd.toString());
//
//            for (; rs.hasNext();) {
//                QuerySolution soln = rs.nextSolution();
//                String sameConcept = soln.getResource("c").getURI();
//
//                if (!sameConcept.contains("http://www.wikidata.org/entity/Q16521")) {
//                    extraConcepts.add(sameConcept);
//                } 
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
//            }
//        }
//        
//        return extraConcepts;
//        
//    }
    
}
