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
        
	public LdIndexer sameAsIndex;
        public LdIndexer ingoingEdgesIndex;
        public LdIndexer outgoingEdgesIndex;
        public LdIndexer ingoingTypedEdgesIndex;
        public LdIndexer outgoingTypedEdgesIndex;

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
	public static Set<URI> getEdges(LdDataset dataset) {
		Resource edge = null;
		Set<URI> edges = new HashSet();
		URIFactory factory = URIFactoryMemory.getSingleton();

		ParameterizedSparqlString query_cmd = dataset.prepareQuery();

		query_cmd.setCommandText("select ?property where {?subject ?property ?object. }");

		ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.nextSolution();
			edge = (Resource) qs.getResource("property");
			edges.add(factory.getURI(edge.toString()));

		}
		return edges;

	}
        
        @Override
        public int countIngoingEdges(URI link , R a , LdDataset dataset) {
		      
                if (this.config.getParam("useIndexes").equals(true)) {
                    
                    
                    String ingoingEdges_a = ingoingTypedEdgesIndex.getValue(a.getUri().stringValue()+ ":" + link.stringValue());

                    if(ingoingEdges_a != null){
                        return Integer.parseInt(ingoingEdges_a);

                    }
                    else
                    {
                        ingoingTypedEdgesIndex.addValue(a.getUri().stringValue() , Integer.toString(super.countIngoingEdges(link , a, dataset)));

                        return Integer.parseInt(ingoingTypedEdgesIndex.getValue(a.getUri().stringValue()));
                    }                      

                }
                
            return super.countIngoingEdges(link , a, dataset);
	}
        
        @Override
        public int countIngoingEdges(R a , LdDataset dataset) {
		      
                if (this.config.getParam("useIndexes").equals(true)) {
                    
                    
                    String ingoingEdges_a = ingoingEdgesIndex.getValue(a.getUri().stringValue());

                    if(ingoingEdges_a != null){
                        return Integer.parseInt(ingoingEdges_a);

                    }
                    else
                    {
                        ingoingEdgesIndex.addValue(a.getUri().stringValue() , Integer.toString(super.countIngoingEdges(a, dataset)));

                        return Integer.parseInt(ingoingEdgesIndex.getValue(a.getUri().stringValue()));
                    }                      

                }
                
            return super.countOutgoingEdges(a, dataset);
	}
        
        
        @Override
        public int countOutgoingEdges(URI link , R a , LdDataset dataset) {
                
                if (this.config.getParam("useIndexes").equals(true)) {
                    
                    String outgoingEdges_a = outgoingTypedEdgesIndex.getValue(a.getUri().stringValue() + ":" + link.stringValue());
                    
                    if(outgoingEdges_a != null){
                        return Integer.parseInt(outgoingEdges_a);
                        
                    }
                    else
                    {
                        
                        outgoingTypedEdgesIndex.addValue(a.getUri().stringValue() , Integer.toString(super.countOutgoingEdges(link , a, dataset)));
                        
                        return Integer.parseInt(outgoingTypedEdgesIndex.getValue(a.getUri().stringValue()));
                        
                    }
                }
                
            return super.countOutgoingEdges(link , a, dataset);
	}
        
        
        @Override
        public int countOutgoingEdges(R a , LdDataset dataset) {
                
                if (this.config.getParam("useIndexes").equals(true)) {
                    
                    String outgoingEdges_a = outgoingEdgesIndex.getValue(a.getUri().stringValue());
                    
                    if(outgoingEdges_a != null){
                        return Integer.parseInt(outgoingEdges_a);
                        
                    }
                    else
                    {
                        
                        outgoingEdgesIndex.addValue(a.getUri().stringValue() , Integer.toString(super.countOutgoingEdges( a, dataset)));
                        
                        return Integer.parseInt(outgoingEdgesIndex.getValue(a.getUri().stringValue()));
                        
                    }
                }
                
            return super.countOutgoingEdges(a, dataset);
	}
        
        

	/**
	 * NEW methods based on index ----------------
     * @param a
     * @param b
     * @return 
	 */

        @Override
	public boolean isSameAs(R a, R b) {

		if (this.config.getParam("useIndexes").equals(true)) {

			List<String> sameAs_a = sameAsIndex.getList(a.getUri().stringValue());

			if (sameAs_a == null) {

				// get sameAs from LOD dataset
				try {
					sameAs_a = super.getSameAsResoures(a);
					// index
					sameAsIndex.addList(a.getUri().stringValue(), sameAs_a);

					return sameAs_a.contains(b.getUri().stringValue());
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
       
}
