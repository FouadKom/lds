/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.openrdf.model.URI;

import sc.research.ldq.LdDataset;
import slib.graph.model.graph.G;
import slib.graph.model.repo.URIFactory;
import slib.graph.model.impl.repo.URIFactoryMemory;

/**
 *
 * @author Fouad Komeiha
 */


public class LdResources {
    
	private static Set<String> trvrsdResources = new HashSet<String>();
    
    
   // public void addIngoingResources(G graph , String r , Integer level, LdDataset dataset){
	 public void addIngoingResources(G graph , String r , Integer level, LdDataset dataset){      
       
		RDFNode subject = null;
        RDFNode property = null;
         
        URI vSubject , vObject , eProperty;
         
        URIFactory factory = URIFactoryMemory.getSingleton();
         
        String queryString = "select distinct ?subject ?property \n" +
                              "where \n" +
                              "{?subject ?property <" + r + ">.\n" +
                              "FILTER(ISURI(?subject))}";


        dataset.prepareQuery();
        
        
       // Query query = QueryFactory.create(queryString);

        //QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
       // QueryExecution qexec=QueryExecutionFactory.create(query, dataset);

        //querying resource r and traversing its subject so level is decremented 
        level = level - 1;

        //adding the resource r to trvrsdResources list so it will not be traversed again 
        trvrsdResources.add(r);
        System.out.println("traversed " + r + " and added to list");
        
        System.out.println(queryString);

        try {                                                                                                                                                                                                                                       
          //  ResultSet results = dataset.executeSelectQuery(queryString);
        	  ResultSet results = dataset.executeSelectQuery(queryString);
            for (; results.hasNext();) {

                QuerySolution soln = results.nextSolution() ;

                subject = (RDFNode)soln.get("subject");
                property = (RDFNode)soln.get("property");

                //vertex subject
                vSubject = factory.getURI(subject.toString());
                //edge property
                eProperty = factory.getURI(property.toString());
                //vertex object
                vObject = factory.getURI(r);

                //add vertex subject to the graph
                graph.addV(vSubject);
                System.out.println("vertex " + vSubject + " added");
                //add edge property to the graph
                graph.addE(vSubject, eProperty ,vObject);
                System.out.println("edge " + eProperty + " added");
                //add vertex object to the graph
                graph.addV(vObject);
                System.out.println("vertex " + vObject + " added");

                System.out.println("---------");

                //if subject of resource r is traversed previously continue the loop without traversing it
                 if(trvrsdResources.contains(subject.toString())){ 
                    System.out.println("Resource: " + subject.toString() + " not traversed since it already exists");
                    continue;
                }

                //check traversed level: if level > 0 then given level in parameters has not been reached 
                if(level > 0){
                    //if subjects of resource r are not previously traversed and level has not reached 0 recursivly traverse them
                    //traversing subjects of r since the method gets ingoing resources
                    //addIngoingResources(graph , subject.toString() , level, dataset); 
                	addIngoingResources(graph , subject.toString() , level, dataset); 
                    }
            }
        }   

        finally {
           dataset.close();
        	//qexec.close();
        }
    }
     
    public void addOutgoingResources(G graph ,String r , Integer level){

        RDFNode object = null;
        RDFNode property = null;
        URI vSubject , vObject , eProperty;
         
        URIFactory factory = URIFactoryMemory.getSingleton();
         
        String queryString = "select distinct ?property ?object\n" +
                              "where \n" +
                              "{<" + r + "> ?property ?object.\n" +
                              "FILTER(ISURI(?object))}";

        Query query = QueryFactory.create(queryString);

        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

        //querying resource r and traversing its objects so level is decremented 
        level = level - 1; 

        //adding the resource r to trvrsdResources list so it will not be traversed again 
        trvrsdResources.add(r);
        System.out.println("traversed " + r + " and added to list");

        try {                                                                                                                                                                                                                                       
            ResultSet results = qexec.execSelect();
            for (; results.hasNext();) {

                QuerySolution soln = results.nextSolution() ;

                object = (RDFNode)soln.get("object");
                property = (RDFNode)soln.get("property");

                //vertex subvject
                vSubject = factory.getURI(r);
                //edge property
                eProperty = factory.getURI(property.toString());
                //vertex object
                vObject = factory.getURI(object.toString());

                //add vertex subject to the graph
                graph.addV(vSubject);
                System.out.println("vertex " + vSubject + " added");
                //add edge property to the graph
                graph.addE(vSubject, eProperty ,vObject);
                System.out.println("edge " + eProperty + " added");
                //add vertex object to the graph
                graph.addV(vObject);
                System.out.println("vertex " + vObject + " added");

                System.out.println("---------");

                //if object of resource r is traversed previously continue the loop without traversing it
                if(trvrsdResources.contains(object.toString())){
                    System.out.println("Resource: " + object.toString() + " not traversed since it already exists");
                    continue;
                }

                //check traversed level: if level > 0 then given level in parameters has not been reached 
                if(level > 0){
                    //if objects of resource r are not previously traversed and level has not reached 0 recursivly traverse them
                    //traversing object of r since the method gets outgoing resources
                    addOutgoingResources(graph , object.toString() , level);
                }
            }
        }   

        finally {
           qexec.close();
        }
    }
       
       
    public void addIngoingResources(G graph , String r , Integer level , Dataset dataset){
         
       RDFNode subject = null;
       RDFNode property = null;
         
       URI vSubject , vObject , eProperty;
         
       URIFactory factory = URIFactoryMemory.getSingleton();
         
       String queryString = "select distinct ?subject ?property \n" +
                            "where \n" +
                            "{?subject ?property <" + r + ">.\n" +
                            "FILTER(ISURI(?subject)) }";

       Query query = QueryFactory.create(queryString) ;
       QueryExecution queryexec = QueryExecutionFactory.create(query, dataset) ;

       //querying resource r and traversing its subject so level is decremented 
       level = level - 1;

       //adding the resource r to trvrsdResources list so it will not be traversed again 
       trvrsdResources.add(r);
       System.out.println("traversed " + r + " and added to list");

       try {                                                                                                                                                                                                                                       
            ResultSet results = queryexec.execSelect();
            for (; results.hasNext();) {
                
                QuerySolution soln = results.nextSolution() ;

                subject = (RDFNode)soln.get("subject");
                property = (RDFNode)soln.get("property");

                //vertex subject
                vSubject = factory.getURI(subject.toString());
                //edge property
                eProperty = factory.getURI(property.toString());
                //vertex object
                vObject = factory.getURI(r);

                //add vertex subject to the graph
                graph.addV(vSubject);
                System.out.println("vertex " + vSubject + " added");
                //add edge property to the graph
                graph.addE(vSubject, eProperty ,vObject);
                System.out.println("edge " + eProperty + " added");
                //add vertex object to the graph
                graph.addV(vObject);
                System.out.println("vertex " + vObject + " added");

                System.out.println("---------");

                //if subject of resource r is traversed previously continue the loop without traversing it
                 if(trvrsdResources.contains(subject.toString())){ 
                    System.out.println("Resource: " + subject.toString() + " not traversed since it already exists");
                    continue;
                }

                if(level > 0){
                  //if subjects of resource r are not previously traversed and level has not reached 0 recursivly traverse them
                  //traversing subjects of r since the method gets ingoing resources
                  addIngoingResources(graph , subject.toString() , level , dataset);

                }
            }
        } 

        finally{             
            queryexec.close();
        }
    }
     
    public void addOutgoingResources(G graph ,String r , Integer level , LdDataset dataset){
          
        RDFNode object = null;
        RDFNode property = null;
     
        URI vSubject , vObject , eProperty;
         
        URIFactory factory = URIFactoryMemory.getSingleton();
             
        String queryString = "select distinct ?property ?object\n" +
                              "where \n" +
                              "{<" + r + "> ?property ?object.\n" +
                              "FILTER(ISURI(?object))}";

       

        //querying resource r and traversing its objects so level is decremented 
        level = level - 1; 

        //adding the resource r to trvrsdResources list so it will not be traversed again 
        trvrsdResources.add(r);
        System.out.println("traversed " + r + " and added to list");

        try {                                                                                                                                                                                                                                       
            ResultSet results = dataset.executeSelectQuery(queryString);
            for (; results.hasNext();) {

                QuerySolution soln = results.nextSolution() ;

                object = (RDFNode)soln.get("object");
                property = (RDFNode)soln.get("property");

                //vertex subvject
                vSubject = factory.getURI(r);
                //edge property
                eProperty = factory.getURI(property.toString());
                //vertex object
                vObject = factory.getURI(object.toString());

                //add vertex subject to the graph
                graph.addV(vSubject);
                System.out.println("vertex " + vSubject + " added");
                //add edge property to the graph
                graph.addE(vSubject, eProperty ,vObject);
                System.out.println("edge " + eProperty + " added");
                //add vertex object to the graph
                graph.addV(vObject);
                System.out.println("vertex " + vObject + " added");

                System.out.println("---------");

                //if object of resource r is traversed previously continue the loop without traversing it
                if(trvrsdResources.contains(object.toString())){
                    System.out.println("Resource: " + object.toString() + " not traversed since it already exists");
                    continue;
                }

                //check traversed level: if level > 0 then given level in parameters has not been reached 
                if(level > 0){
                    //if objects of resource r are not previously traversed and level has not reached 0 recursivly traverse them
                    //traversing object of r since the method gets outgoing resources
                    addOutgoingResources(graph , object.toString() , level , dataset);
                }
            }
        }   

        finally{              
            dataset.close();
        }
           
    }
       
    public void getConnectingPath(G graph , String r1 , String r2 , int level , Dataset dataset){
           
        RDFNode object = null;
        RDFNode property = null;

        URI vSubject , vObject , eProperty;

        URIFactory factory = URIFactoryMemory.getSingleton();

        String queryString = "select distinct ?property ?object\n" +
                             "where \n" +
                             "{<" + r1 + "> ?property ?object.\n" +
                             "FILTER(ISURI(?object))}";

        Query query = QueryFactory.create(queryString) ;
        QueryExecution queryexec = QueryExecutionFactory.create(query, dataset) ;


        //querying resource r1 and traversing its objects so level is decremented 
        level = level - 1; 

        //adding the resource r1 to trvrsdResources list so it will not be traversed again 
        trvrsdResources.add(r1);
        System.out.println("traversed " + r1 + " and added to list");

        try {                                                                                                                                                                                                                                       
            ResultSet results = queryexec.execSelect();

            for (; results.hasNext() ;) {

                QuerySolution soln = results.nextSolution() ;

                object = (RDFNode)soln.get("object");
                property = (RDFNode)soln.get("property");

                //vertex subvject
                vSubject = factory.getURI(r1);
                //edge property
                eProperty = factory.getURI(property.toString());
                //vertex object
                vObject = factory.getURI(object.toString());

                //add vertex subject to the graph
                graph.addV(vSubject);
                System.out.println("vertex " + vSubject + " added");
                //add edge property to the graph
                graph.addE(vSubject, eProperty ,vObject);
                System.out.println("edge " + eProperty + " added");
                //add vertex object to the graph
                graph.addV(vObject);
                System.out.println("vertex " + vObject + " added");

                System.out.println("---------");

               //if resource r2 is reached stop querying
                if(object.toString().equalsIgnoreCase(r2)){
                    System.out.println("Resource " + r2 +" found, method stopped");
                    return;
                }

                //if object of resource r1 is traversed previously continue the loop without traversing it
                if(trvrsdResources.contains(object.toString())){
                    System.out.println("Resource: " + object.toString() + " not traversed since it already exists");
                    continue;
                }

               //check traversed level: if level > 0 then given level in parameters has not been reached 
               if(level > 0)
                   /*if objects of resource r are not previously traversed 
                    and level has not reached 0 
                    and resource r2 has not been found, 
                   recursivly traverse objects of r1 to search for r2*/
                    getConnectingPath(graph , object.toString() , r2 , level, dataset);
                }

        }

        finally{             
          queryexec.close();
        }
            
    }
      
    /*This method is declared to clear the share variable trvrsdResources
    this variable is used to hold traversed resources during recursion and 
    because of rescursion it should be cleared outside the methods
    */
    
  public void clear(){
       this.trvrsdResources.clear();
   }
      
      
}