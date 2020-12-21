/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager;

import java.util.ArrayList;
import java.util.List;
import lds.resource.R;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.vocabulary.OWL;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class FeaturesMeasuresLdManagerBase extends LdManagerBase{
    
    public FeaturesMeasuresLdManagerBase(LdDataset dataset) {
        super(dataset);
    }    
    
    
    public List<String> getOntologiesPrefixes(R a) {
        List<String> ontologies = new ArrayList();
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();
        char endingChar = 0;

        query_cmd.setCommandText("select distinct ?ontology " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") 
                                                              + " where {<" + a.getUri() + "> a ?ontology .}");      
       
        
        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            String ontologyPrefix = qs.getResource("ontology").getNameSpace();
            
            if(ontologyPrefix.contains("/"))
                endingChar = '/';
            else if(ontologyPrefix.contains("#"))
                endingChar = '#';
            
            if(ontologyPrefix.lastIndexOf(endingChar) != (ontologyPrefix.length() -1) ){
                ontologyPrefix = ontologyPrefix.substring(0, ontologyPrefix.lastIndexOf(endingChar) + 1);
            }
            
            if(!ontologies.contains(ontologyPrefix))
                ontologies.add(ontologyPrefix);            

        }
        
        if(! ontologies.isEmpty())
            return ontologies;
        else
            return null;
    }
    
    
    public List<String> getAugmentdOntologiesPrefixes(R a){
        List<String> ontologies = new ArrayList();
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();
        char endingChar = 0;

        query_cmd.setCommandText("select distinct ?concept " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">")
                                                             + " where {<" + a.getUri() + "> <" + OWL.sameAs + "> ?concept .}");    

        
        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            String ontologyPrefix = qs.getResource("concept").getNameSpace();
            
            if(ontologyPrefix.contains("/"))
                endingChar = '/';
            else if(ontologyPrefix.contains("#"))
                endingChar = '#';
            
            if(ontologyPrefix.lastIndexOf(endingChar) != (ontologyPrefix.length() -1) ){
                ontologyPrefix = ontologyPrefix.substring(0, ontologyPrefix.lastIndexOf(endingChar) + 1);
            }
            
            if(!ontologies.contains(ontologyPrefix)){
                ontologies.add(ontologyPrefix);   
            }

        }    
        
        if(! ontologies.isEmpty())
            return ontologies;
        else
            return null;
    }
    
    
}
