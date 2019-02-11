/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.picss;

import java.util.HashSet;
import java.util.Set;
import org.apache.jena.query.ParameterizedSparqlString;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;
import slib.graph.model.graph.G;
import slib.graph.model.graph.elements.E;
import slib.graph.model.graph.utils.Direction;
import lds.graph.LdGraphManager;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import org.apache.commons.httpclient.HttpException;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import static slib.graph.io.loader.GraphLoaderGeneric.logger;
import slib.utils.ex.SLIB_Ex_Critic;

/**
 *
 * @author Fouad Komeiha
 */
public class Utility {
    
    public static Set<String> getFeaturesSet(URI uri , LdDataset dataset) throws SLIB_Ex_Critic, HttpException{
        Set<String> Fa = new HashSet<String>();
        RDFNode property = null;
        RDFNode object = null;
        RDFNode subject = null;
        String f;
        
        ParameterizedSparqlString query_cmdOutgoing = dataset.prepareQuery();
        
        query_cmdOutgoing.setCommandText("select ?property ?object where {" 
           + "<" + uri.toString() + "> ?property ?object. }");


        ResultSet resultSetOutgoing = dataset.executeSelectQuery(query_cmdOutgoing.toString());

        while(resultSetOutgoing.hasNext()) {
                QuerySolution qs = resultSetOutgoing.nextSolution();
                property = qs.get("property");
                object =  qs.get("object");
                if(object.isLiteral())
                    continue;
                
                f = "(" + property.toString() + ", " + object.toString() + ", Out)";
                Fa.add(f);         
                
        }
        
        ParameterizedSparqlString query_cmdIngoing = dataset.prepareQuery();
        
        query_cmdIngoing.setCommandText("select ?subject ?property where {" 
           + "?subject ?property <" + uri.toString() + ">. }");


        ResultSet resultSetIngoing = dataset.executeSelectQuery(query_cmdIngoing.toString());

        while(resultSetIngoing.hasNext()) {
                QuerySolution qs = resultSetIngoing.nextSolution();
                property = qs.get("property");
                subject =  qs.get("subject");
                if(subject.isLiteral())
                    continue;
                
                f = "(" + property.toString() + ", " + subject.toString() + ", In)";
                Fa.add(f);         
                
        }
				
        return Fa;
        
    }
    
    public static int getFeatureFrequency(LdDataset dataset , String feature){
        String link = getLink(feature);
        String direction = getDirection(feature);
        String resource = getVertex(feature);
        RDFNode count = null;
        
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        if(direction.equals("In"))
            query_cmd.setCommandText("select (count(distinct ?object) as ?count) where {" 
               + "<"+ resource +">  <"+ link + "> ?object. " + "}");
        
        else
            query_cmd.setCommandText("select (count(distinct ?subject) as ?count) where {" 
               + "?subject <"+ link + "> <"+ resource +">. }");


        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        if(resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                count = qs.get("count") ; 
        }
        
        return Integer.parseInt(count.toString().substring(0, 1));
    }
    
    public static int getResourcesNum(LdDataset dataset){
        return 9;//based on the attached dataset for testing and should be edited to return the number of resources in a given dataset
    }
    
    
    public static Set<String> difference(Set<String> a, Set<String> b) {
        Set<String> result = new HashSet<String>(a);
        for (String element : b) {
            if (result.contains(element)) {
                result.remove(element);
            }
        }
        return result;
    }
    
    public static Set<String> intersection(Set<String> a, Set<String> b){
        Set<String> result = new HashSet<String>(a);
        
        result.retainAll(b);
        return result;
    }
    
    public static String getLink(String s){
        return (s.substring(s.indexOf('(') + 1 , s.indexOf(',')));
    }
    
    public static String getVertex(String s){
        return (s.substring(s.indexOf(',') + 2 , s.indexOf(',' , s.indexOf(',') + 2) ));
    }
    
    public static String getDirection(String s){
        if(s.substring(s.indexOf(')') - 3 , s.indexOf(')')).equals("Out"))
            return "Out";
        else
            return "In";
    }
    
    public static double logb( double a, int b ){
        return Math.log(a) / Math.log(b);
    }
    
    
}
