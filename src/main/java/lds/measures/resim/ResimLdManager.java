/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lds.LdManager.LdManagerBase;
import lds.indexing.LdIndexer;
import lds.resource.R;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;

import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;
import slib.graph.model.impl.repo.URIFactoryMemory;
import slib.graph.model.repo.URIFactory;
import slib.utils.i.Conf;

/**
 *
 * @author Nasredine CHENIKI
 */
public class ResimLdManager extends LdManagerBase {
    	
	// TODO: specify an index directory
	String sameAsIndexFile = "resim_sameAs_index.db";
        String ingoingEdgesIndexFile = "resim_ingoingEdges_index.db";
        String outgoingEdgesIndexFile = "resim_outgoingEdges_index.db";
        String ingoingTypedEdgesIndexFile = "resim_ingoingTypedEdges_index.db";
        String outgoingTypedEdgesIndexFile = "resim_outgoingTypedEdges_index.db";
        String shareCommonObjectsIndexFile = "resim_shareCommonObjects_index.db";
        String shareCommonSubjectsIndexFile = "resim_shareCommonSubjects_index.db";
//        String directlyConnectedIndexFile = "resim_directlyConnected_index.db";
        
	public LdIndexer sameAsIndex;
        public LdIndexer ingoingEdgesIndex;
        public LdIndexer outgoingEdgesIndex;
        public LdIndexer ingoingTypedEdgesIndex;
        public LdIndexer outgoingTypedEdgesIndex;
        public LdIndexer shareCommonObjectsIndex;
        public LdIndexer shareCommonSubjectsIndex;
//        public LdIndexer directlyConnectedIndex;
        

	public ResimLdManager(LdDataset dataset, Conf config) {
		super(dataset, config);

		if (config.getParam("useIndexes").equals(true))
			loadIndexes();
	}

	private void loadIndexes() {
		sameAsIndex = new LdIndexer(sameAsIndexFile);
                ingoingEdgesIndex = new LdIndexer(ingoingEdgesIndexFile);
                outgoingEdgesIndex = new LdIndexer(outgoingEdgesIndexFile);
                ingoingTypedEdgesIndex = new LdIndexer(ingoingTypedEdgesIndexFile);
                outgoingTypedEdgesIndex = new LdIndexer(outgoingTypedEdgesIndexFile);
                shareCommonObjectsIndex = new LdIndexer(shareCommonObjectsIndexFile);
                shareCommonSubjectsIndex = new LdIndexer(shareCommonSubjectsIndexFile);
                
	}
        
        public void closeIndexes(){
                sameAsIndex.close();
                ingoingEdgesIndex.close();
                outgoingEdgesIndex.close();
                ingoingTypedEdgesIndex.close();
                outgoingTypedEdgesIndex.close();
                shareCommonObjectsIndex.close();
             
         }
         

	// public static int getCountOutgoing(URI a , LdDataset dataset){
	// Literal count = null;
	// ParameterizedSparqlString query_cmd = dataset.prepareQuery();
	//
	// query_cmd.setCommandText("select (count(distinct ?property) as ?count) where
	// {"
	// + "<"+ a.toString() + "> ?property ?Object. }");
	//
	//
	// logger.info("query = " + query_cmd.toString());
	//
	// ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());
	//
	// if(resultSet.hasNext()) {
	// QuerySolution qs = resultSet.nextSolution();
	// count = (Literal) qs.getLiteral("count") ;
	// }
	//
	// return Integer.parseInt(count.toString());
	// }
	//
	// public static int getCountIngoing(URI a , LdDataset dataset){
	// Literal count = null;
	// ParameterizedSparqlString query_cmd = dataset.prepareQuery();
	//
	// query_cmd.setCommandText("select (count(distinct ?property) as ?count) where
	// {"
	// + "?Object ?property <"+ a.toString() + ">. }");
	//
	//
	// logger.info("query = " + query_cmd.toString());
	//
	// ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());
	//
	// if(resultSet.hasNext()) {
	// QuerySolution qs = resultSet.nextSolution();
	// count = (Literal) qs.getLiteral("count") ;
	// }
	//
	// return Integer.parseInt(count.toString());
	// }
        
        
        // TOFIX: Cannot get all edges of a dataset
	public static Set<URI> getEdges(R a , R b) {
		Resource edge = null;
		Set<URI> edges = new HashSet();
		URIFactory factory = URIFactoryMemory.getSingleton();

		ParameterizedSparqlString query_cmd = dataset.prepareQuery();

		query_cmd.setCommandText("select distinct ?property " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where {?subject ?property ?object. "
//                        + "filter(?subject IN (<" + a.getUri() + "> , <" + b.getUri() +"> ) || ?object IN (<" + a.getUri() + "> , <" + b.getUri() +"> ) ) }");
                          + "filter( isuri(?object) ) } limit 10");

		ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.nextSolution();
			edge = (Resource) qs.getResource("property");
			edges.add(factory.getURI(edge.toString()));

		}
                
                dataset.close();
		return edges;

	}
        
        @Override
        public int countIngoingEdges(URI link , R a) {
		      
                if (this.config.getParam("useIndexes").equals(true)) {
                    
                    List<String> ingoingTypedEdges_a = ingoingTypedEdgesIndex.getList(a.getUri().stringValue()+ ":" + link.stringValue());
                    
                    if(ingoingTypedEdges_a != null){
                        
                        return ingoingTypedEdges_a.size();

                    }
                    else
                    {
                        ingoingTypedEdges_a = super.getIngoingEdges(link , a);
                        
                        if(ingoingTypedEdges_a != null){
                            ingoingTypedEdgesIndex.addList(a.getUri().stringValue()+ ":" + link.stringValue() , ingoingTypedEdges_a);
                            return ingoingTypedEdges_a.size();
                        }
                        
                        return 0;
                        
                    }                      

                }
                
                return super.countIngoingEdges(link , a);
	}
        
        @Override
        public int countIngoingEdges(R a) {
		      
                if (this.config.getParam("useIndexes").equals(true)) {
                    
                    
                    List<String> ingoingEdges_a = ingoingEdgesIndex.getList(a.getUri().stringValue());

                    if(ingoingEdges_a != null){
                        
                        return ingoingEdges_a.size();

                    }
                    else
                    {
                        ingoingEdges_a = super.getIngoingEdges(a);
                        
                        if(ingoingEdges_a != null){
                            ingoingEdgesIndex.addList(a.getUri().stringValue(), ingoingEdges_a);
                            return ingoingEdges_a.size();
                        }
                        
                        return 0;
                        
                    }                      

                }
                
               return super.countIngoingEdges(a);
               
	}
        
        
       @Override
        public int countOutgoingEdges(URI link , R a) {
                
                if (this.config.getParam("useIndexes").equals(true)) {
                    
                    List<String> outgoingTypedEdges_a = outgoingTypedEdgesIndex.getList(a.getUri().stringValue()+ ":" + link.stringValue());
                    
                    if(outgoingTypedEdges_a != null){
                        
                        return outgoingTypedEdges_a.size();

                    }
                    else
                    {
                        outgoingTypedEdges_a = super.getOutgoingEdges(link , a);
                        
                        if(outgoingTypedEdges_a != null){
                            outgoingTypedEdgesIndex.addList(a.getUri().stringValue()+ ":" + link.stringValue() , outgoingTypedEdges_a);
                            return outgoingTypedEdges_a.size();
                        }
                        
                        return 0;
                        
                    }                      

                }
                
                return super.countOutgoingEdges(link , a);
	}
        
        
        @Override
        public int countOutgoingEdges(R a) {
                
                if (this.config.getParam("useIndexes").equals(true)) {
                    
                    
                    List<String> outgoingEdges_a = outgoingEdgesIndex.getList(a.getUri().stringValue());

                    if(outgoingEdges_a != null){
                        
                        return outgoingEdges_a.size();

                    }
                    else
                    {
                        outgoingEdges_a = super.getOutgoingEdges(a);
                        
                        if(outgoingEdges_a != null){
                            outgoingEdgesIndex.addList(a.getUri().stringValue(), outgoingEdges_a);
                            return outgoingEdges_a.size();                            
                        }
                        
                        return 0;
                        
                    }                      

                }
                
               return super.countOutgoingEdges(a);
	}
        
        @Override
        public int countShareCommonObjects(URI link, R a ) {
            if (this.config.getParam("useIndexes").equals(true)) {
                    
                    List<String> shareCommonObjects_a = shareCommonObjectsIndex.getList(a.getUri().stringValue()+ ":" + link.stringValue());
                    
                    if(shareCommonObjects_a != null){
                        
                        return shareCommonObjects_a.size();

                    }
                    else
                    {                     
                        shareCommonObjects_a = super.listShareCommonObject(link, a) ;
                        
                        if(shareCommonObjects_a != null ){
                            shareCommonObjectsIndex.addList(a.getUri().stringValue()+ ":" + link.stringValue() , shareCommonObjects_a);
                            return shareCommonObjects_a.size();
                        }
                        
                        return 0;
                    }                      

                }
                
                return super.countShareCommonObjects(link , a);
        }
        
         @Override
         public boolean shareCommonObject(URI link , R a, R b){
              if (this.config.getParam("useIndexes").equals(true)) {
                    
                    List<String> shareCommonObjects_a = shareCommonObjectsIndex.getList(a.getUri().stringValue()+ ":" + link.stringValue());
                    
                    if(shareCommonObjects_a != null){
                        
                        return shareCommonObjects_a.contains(b.getUri().stringValue());

                    }
                    else
                    {                     
                        shareCommonObjects_a = super.listShareCommonObject(link, a);
                        
                        if(shareCommonObjects_a != null){
                            shareCommonObjectsIndex.addList(a.getUri().stringValue()+ ":" + link.stringValue() ,  shareCommonObjects_a);
                            return shareCommonObjects_a.contains(b.getUri().stringValue());
                        }
                            
                        return false;
                    }                      

                }
                
                return super.shareCommonObject(link , a , b);
        }
        
        @Override
        public int countShareCommonSubjects(URI link, R a ) {
             if (this.config.getParam("useIndexes").equals(true)) {
                    
                    List<String> shareCommonSubjects_a = shareCommonSubjectsIndex.getList(a.getUri().stringValue()+ ":" + link.stringValue());
                    
                    if(shareCommonSubjects_a != null){
                        
                        return shareCommonSubjects_a.size();

                    }
                    else
                    {   
                        shareCommonSubjects_a = super.listShareCommonSubject(link, a);                        
                        
                        if(shareCommonSubjects_a != null){
                            shareCommonSubjectsIndex.addList(a.getUri().stringValue()+ ":" + link.stringValue() , shareCommonSubjects_a );
                            return shareCommonSubjects_a.size();
                        }
                            
                        
                        return 0;
                    }                      

                }
                
                return super.countShareCommonSubjects(link , a);
        }
        
        @Override
         public boolean shareCommonSubject(URI link , R a, R b){
             if (this.config.getParam("useIndexes").equals(true)) {
                    
                    List<String> shareCommonSubjects_a = shareCommonSubjectsIndex.getList(a.getUri().stringValue()+ ":" + link.stringValue());
                    
                    if(shareCommonSubjects_a != null){
                        
                        return shareCommonSubjects_a.contains(b.getUri().stringValue());

                    }
                    else
                    {   
                        shareCommonSubjects_a = super.listShareCommonSubject(link, a);
                        
                        if(shareCommonSubjects_a != null){
                            shareCommonSubjectsIndex.addList(a.getUri().stringValue()+ ":" + link.stringValue() , shareCommonSubjects_a );
                            return shareCommonSubjects_a.contains(b.getUri().stringValue());
                        }
                        
                        return false;
                    }                      

                }
                
                return super.shareCommonSubject(link , a , b);
        }

       
        @Override
	public boolean isSameAs(R a, R b) {

		if (this.config.getParam("useIndexes").equals(true)) {

			List<String> sameAs_a = sameAsIndex.getList(a.getUri().stringValue());

			if (sameAs_a == null) {

				// get sameAs from LOD dataset
				try {
					sameAs_a = super.getSameAsResoures(a);
					// index
					
                                        if(sameAs_a != null){
                                            sameAsIndex.addList(a.getUri().stringValue(), sameAs_a);
                                            return sameAs_a.contains(b.getUri().stringValue());
                                        }
                                        
                                        return false;
                                        
				} catch (Exception e) {
					// TODO: throw exception
					System.out.println("Error:" + e.getMessage());
				}

			}

			else {
				System.out.println("sameAs using index");

				return sameAs_a.contains(b.getUri().stringValue());
			}
		}
		return super.getSameAsResoures(a).contains(b.getUri().stringValue());
	}
        
        
        
        @Override
         public boolean isDirectlyConnected(URI link, R a, R b) { //sameAs can be replaced with this?
             
             if (this.config.getParam("useIndexes").equals(true)) {
                    
                    List<String> outgoingTypedEdges_a = outgoingTypedEdgesIndex.getList(a.getUri().stringValue()+ ":" + link.stringValue());
                    
                    if(outgoingTypedEdges_a != null){
                        
                        return outgoingTypedEdges_a.contains(b.getUri().stringValue());

                    }
                    else
                    {   
                        outgoingTypedEdges_a = super.getOutgoingEdges(link , a);
                        
                        if(outgoingTypedEdges_a != null){
                            outgoingTypedEdgesIndex.addList(a.getUri().stringValue()+ ":" + link.stringValue() , outgoingTypedEdges_a);
                            return outgoingTypedEdges_a.contains(b.getUri().stringValue());
                        }
                        
                        return false;
                        
                    }                      

                }
                
                return super.isDirectlyConnected(link, a, b);
                
         }
         
         
        
    }
       

