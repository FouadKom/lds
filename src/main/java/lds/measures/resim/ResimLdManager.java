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
	String sameAsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_sameAs_index.db";
        String ingoingEdgesIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_ingoingEdges_index.db";
        String outgoingEdgesIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_outgoingEdges_index.db";
        String subjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_subjects_index.db";
        String objectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_objects_index.db";
        String shareCommonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_shareCommonObjects_index.db";
        String shareCommonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_shareCommonSubjects_index.db";
        String directlyConnectedIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_directlyConnected_index.db";
        String propertyOccurrenceIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_propertyOccurence_index.db";
        
	public LdIndexer sameAsIndex;
        public LdIndexer ingoingEdgesIndex;
        public LdIndexer outgoingEdgesIndex;
        public LdIndexer subjectsIndex;
        public LdIndexer objectsIndex;
        public LdIndexer shareCommonObjectsIndex;
        public LdIndexer shareCommonSubjectsIndex;
        public LdIndexer directlyConnectedIndex;
        public LdIndexer propertyOccurrenceIndex;
        

	public ResimLdManager(LdDataset dataset, Conf config) {
		super(dataset, config);

		if (config.getParam("useIndexes").equals(true))
			loadIndexes();
	}

	private void loadIndexes() {
		sameAsIndex = new LdIndexer(sameAsIndexFile);
                ingoingEdgesIndex = new LdIndexer(ingoingEdgesIndexFile);
                outgoingEdgesIndex = new LdIndexer(outgoingEdgesIndexFile);
                subjectsIndex = new LdIndexer(subjectsIndexFile);
                objectsIndex = new LdIndexer(objectsIndexFile);
                shareCommonObjectsIndex = new LdIndexer(shareCommonObjectsIndexFile);
                shareCommonSubjectsIndex = new LdIndexer(shareCommonSubjectsIndexFile);
                directlyConnectedIndex = new LdIndexer(directlyConnectedIndexFile);
                propertyOccurrenceIndex = new LdIndexer(propertyOccurrenceIndexFile);
                
	}
        
        public void closeIndexes(){
            if (this.config.getParam("useIndexes").equals(true)) {
                sameAsIndex.close();
                ingoingEdgesIndex.close();
                outgoingEdgesIndex.close();
                subjectsIndex.close();
                objectsIndex.close();
                shareCommonObjectsIndex.close();
                shareCommonSubjectsIndex.close();
                directlyConnectedIndex.close();
                propertyOccurrenceIndex.close();
            }
             
         }
         
     
        @Override
	public Set<URI> getEdges(R a , R b) {                                
		Set<URI> Ingoingedges_a = new HashSet();
                Set<URI> Ingoingedges_b = new HashSet();
                Set<URI> Outgoingedges_a = new HashSet();
                Set<URI> Outgoingedges_b = new HashSet();
                
                Set<URI> edges = new HashSet();
                
                Ingoingedges_a = getIngoingEdges(a);
                Ingoingedges_b = getIngoingEdges(b);
                Outgoingedges_a = getOutgoingEdges(a);
                Outgoingedges_b = getOutgoingEdges(b);
                
                edges.addAll(Ingoingedges_a);
                edges.addAll(Ingoingedges_b);
                edges.addAll(Outgoingedges_a);
                edges.addAll(Outgoingedges_b);
                
//                if(edges == null || edges.isEmpty()){
//                    return super.getEdges(a , b);
//                }
                
		return edges;

                
	}
        
        
        @Override
        public Set<URI> getIngoingEdges(R a) {
            if (this.config.getParam("useIndexes").equals(true)) {
                Set<URI> ingoingEdges_a = Utility.toURI(ingoingEdgesIndex.getList(a.getUri().stringValue()));
                
                if(ingoingEdges_a != null){
                    return ingoingEdges_a;
                }
                else{
                    ingoingEdges_a = super.getIngoingEdges(a);
                    
                    if(ingoingEdges_a != null ){
                        ingoingEdgesIndex.addList(a.getUri().stringValue() , Utility.toList(ingoingEdges_a));
                        return ingoingEdges_a;
                    }
                    
                    else
                        return null;
                    
                }
            }
            
            return super.getIngoingEdges(a);          
        }
        
        
        @Override
        public Set<URI> getOutgoingEdges(R a){
           if (this.config.getParam("useIndexes").equals(true)) {
                Set<URI> outgoingEdges_a = Utility.toURI(outgoingEdgesIndex.getList(a.getUri().stringValue()));
                
                if(outgoingEdges_a != null){
                    return outgoingEdges_a;
                }
                    
                
                else{
                    outgoingEdges_a = super.getOutgoingEdges(a);
                    
                    if(outgoingEdges_a != null ){
                        outgoingEdgesIndex.addList(a.getUri().stringValue() , Utility.toList(outgoingEdges_a));
                        return outgoingEdges_a;
                    }
                    else
                        return null;
                }
            }            
            return super.getOutgoingEdges(a); 
        }     
        
        @Override
        public int countSubject(URI link , R a) {
                
                if (this.config.getParam("useIndexes").equals(true)) {
                    
                    String subjects_a = subjectsIndex.getValue(a.getUri().stringValue()+ ":" + link.stringValue());
                    
                    if(subjects_a != null && ! subjects_a.equals("-1")){

                        return Integer.parseInt(subjects_a);

                    }
                    else if(subjects_a != null && subjects_a.equals("-1")){
                        return 0;
                    }
                    else
                    {
                        subjects_a = Integer.toString(super.countSubject(link , a));
                        
                        if(subjects_a != null){
                            subjectsIndex.addValue(a.getUri().stringValue()+ ":" + link.stringValue() , subjects_a);
                           
                            return Integer.parseInt(subjects_a);
                        }
                        
                        subjectsIndex.addValue(a.getUri().stringValue()+ ":" + link.stringValue() , "-1");
                           
                        return 0;
                        
                    }                      

                }
                
                return super.countSubject(link , a);
	}
        
        
        @Override
        public int countSubject(R a) {
                
                if (this.config.getParam("useIndexes").equals(true)) {
                    
                    String subjects_a = subjectsIndex.getValue(a.getUri().stringValue());
                    
                    
                    if(subjects_a != null && ! subjects_a.equals("-1")){
                        return Integer.parseInt(subjects_a);

                    }
                    else if(subjects_a != null && subjects_a.equals("-1")){
                        
                        return 0;
                    }
                    else
                    {
                        subjects_a = Integer.toString(super.countSubject(a));
                        
                        if(subjects_a != null){
                            subjectsIndex.addValue(a.getUri().stringValue(), subjects_a);
                                                        
                            return Integer.parseInt(subjects_a);
                        }
                        
                        subjectsIndex.addValue(a.getUri().stringValue(), "-1");
                        return 0;
                    }                      

                }
                
               return super.countSubject(a);
               
	}
        
        
       @Override
        public int countObject(URI link , R a) {
                
                if (this.config.getParam("useIndexes").equals(true)) {
                    
                    String objects_a = objectsIndex.getValue(a.getUri().stringValue()+ ":" + link.stringValue());                    
                    
                    if(objects_a != null && !objects_a.equals("-1")){
                        return Integer.parseInt(objects_a);

                    }
                    
                    else if (objects_a != null && objects_a.equals("-1")){
                        return 0;
                    }
                    
                    else
                    {
                        objects_a = Integer.toString(super.countObject(link , a));
                        
                        if(objects_a != null){
                            objectsIndex.addValue(a.getUri().stringValue()+ ":" + link.stringValue() , objects_a);
                                                        
                            return Integer.parseInt(objects_a);
                        }
                        objectsIndex.addValue(a.getUri().stringValue()+ ":" + link.stringValue() , "-1");
                        return 0;
                        
                    }                      

                }
                
                return super.countObject(link , a);
	}
        
        
        @Override
        public int countObject(R a) {
                
                if (this.config.getParam("useIndexes").equals(true)) {
                    
                    
                    String objects_a = objectsIndex.getValue(a.getUri().stringValue());
                    

                    if(objects_a != null && !objects_a.equals("-1")){
                        return Integer.parseInt(objects_a);

                    }
                    else if(objects_a != null && objects_a.equals("-1")){
                        
                        return 0;
                    }    
                    else
                    {
                        objects_a = Integer.toString(super.countObject(a));
                        
                        if(objects_a != null){
                            objectsIndex.addValue(a.getUri().stringValue(), objects_a);
                                                       
                            return Integer.parseInt(objects_a);                          
                        }
                        
                        objectsIndex.addValue(a.getUri().stringValue(), "-1");
                        return 0;
                        
                    }                      

                }
                
               return super.countObject(a);
	}
        
        @Override
        public int countShareCommonObjects(URI link, R a ) {
                          
            if (this.config.getParam("useIndexes").equals(true)) {
                    
                    List<String> shareCommonObjects_a = shareCommonObjectsIndex.getList(a.getUri().stringValue()+ ":" + link.stringValue());
                    
                    if(shareCommonObjects_a != null && ! shareCommonObjects_a.contains("-1")){
                        return shareCommonObjects_a.size();

                    }
                    else if(shareCommonObjects_a != null && shareCommonObjects_a.contains("-1")){
                        return 0;
                    }
                    else
                    {                     
                        shareCommonObjects_a = super.listShareCommonObject(link, a) ;
                        
                        if(shareCommonObjects_a != null && !shareCommonObjects_a.isEmpty() ){
                            shareCommonObjectsIndex.addList(a.getUri().stringValue()+ ":" + link.stringValue() , shareCommonObjects_a);
                                                        
                            return shareCommonObjects_a.size();
                        }
                        
                        shareCommonObjects_a.add("-1");
                        shareCommonObjectsIndex.addList(a.getUri().stringValue()+ ":" + link.stringValue() , shareCommonObjects_a);
                        return 0;
                    }                      

                }
                
                return super.countShareCommonObjects(link , a);
        }
        
         @Override
         public boolean shareCommonObject(URI link , R a, R b){
              if (this.config.getParam("useIndexes").equals(true)) {
                    
                    List<String> shareCommonObjects_a = shareCommonObjectsIndex.getList(a.getUri().stringValue()+ ":" + link.stringValue());
                    
                    if(shareCommonObjects_a != null && !shareCommonObjects_a.contains("-1")){
                        return shareCommonObjects_a.contains(b.getUri().stringValue());

                    }
                    else if(shareCommonObjects_a != null && shareCommonObjects_a.contains("-1")){
                        return false;
                    }
                    else
                    {                     
                        shareCommonObjects_a = super.listShareCommonObject(link, a);
                        
                        if(shareCommonObjects_a != null && !shareCommonObjects_a.isEmpty() ){
                            shareCommonObjectsIndex.addList(a.getUri().stringValue()+ ":" + link.stringValue() ,  shareCommonObjects_a);
                                                        
                            return shareCommonObjects_a.contains(b.getUri().stringValue());
                        }
                        
                        shareCommonObjects_a.add("-1");
                        shareCommonObjectsIndex.addList(a.getUri().stringValue()+ ":" + link.stringValue() ,  shareCommonObjects_a);
                        return false;
                    }                      

                }
                
                return super.shareCommonObject(link , a , b);
        }
        
        @Override
        public int countShareCommonSubjects(URI link, R a ) {
             if (this.config.getParam("useIndexes").equals(true)) {
                    
                    List<String> shareCommonSubjects_a = shareCommonSubjectsIndex.getList(a.getUri().stringValue()+ ":" + link.stringValue());
                    
                    if(shareCommonSubjects_a != null && !shareCommonSubjects_a.contains("-1")){
                        return shareCommonSubjects_a.size();

                    }
                    else if(shareCommonSubjects_a != null && shareCommonSubjects_a.contains("-1")){
                        return 0;
                    }
                    else
                    {   
                        shareCommonSubjects_a = super.listShareCommonSubject(link, a);                        
                        
                        if(shareCommonSubjects_a != null && !shareCommonSubjects_a.isEmpty() ){
                            shareCommonSubjectsIndex.addList(a.getUri().stringValue()+ ":" + link.stringValue() , shareCommonSubjects_a );
                                                        
                            return shareCommonSubjects_a.size();
                        }
                        
                        shareCommonSubjects_a.add("-1");
                        shareCommonSubjectsIndex.addList(a.getUri().stringValue()+ ":" + link.stringValue() , shareCommonSubjects_a);
                        return 0;
                    }                      

                }
                
                return super.countShareCommonSubjects(link , a);
        }
        
        @Override
         public boolean shareCommonSubject(URI link , R a, R b){
             if (this.config.getParam("useIndexes").equals(true)) {
                    
                    List<String> shareCommonSubjects_a = shareCommonSubjectsIndex.getList(a.getUri().stringValue()+ ":" + link.stringValue());
                    
                    if(shareCommonSubjects_a != null && !shareCommonSubjects_a.contains("-1")){
                        return shareCommonSubjects_a.contains(b.getUri().stringValue());

                    }
                    else if(shareCommonSubjects_a != null && shareCommonSubjects_a.contains("-1")){
                        return false;
                    }
                    else
                    {   
                        shareCommonSubjects_a = super.listShareCommonSubject(link, a);
                        
                        if(shareCommonSubjects_a != null && !shareCommonSubjects_a.isEmpty()){
                            shareCommonSubjectsIndex.addList(a.getUri().stringValue()+ ":" + link.stringValue() , shareCommonSubjects_a );
                                                        
                            return shareCommonSubjects_a.contains(b.getUri().stringValue());
                        }
                        
                        shareCommonSubjects_a.add("-1");
                        shareCommonSubjectsIndex.addList(a.getUri().stringValue()+ ":" + link.stringValue() , shareCommonSubjects_a );
                        return false;
                    }                      

                }
                
                return super.shareCommonSubject(link , a , b);
        }

       
        @Override
	public boolean isSameAs(R a, R b) {
                    if (this.config.getParam("useIndexes").equals(true)) {

			String sameAs_a = sameAsIndex.getValue(a.getUri().stringValue()+ ":" + b.getUri().stringValue());

			if (sameAs_a == null) {

				// get sameAs from LOD dataset
				try {
					sameAs_a = Boolean.toString(super.isSameAs(a, b));
					// index
					
                                        if(sameAs_a != null){
                                            sameAsIndex.addValue(a.getUri().stringValue()+ ":" + b.getUri().stringValue() , sameAs_a);
                                                                                        
                                            return Boolean.parseBoolean(sameAs_a);
                                        }
                                        
                                        sameAsIndex.addValue(a.getUri().stringValue()+ ":" + b.getUri().stringValue() , "-1");
                                        return false;
                                        
				} catch (Exception e) {
					// TODO: throw exception
					System.out.println("Error:" + e.getMessage());
				}

			}
                        else if(sameAs_a.equals("-1")){
                            return false;
                        }

			else {
                            return Boolean.parseBoolean(sameAs_a);
			}
		}
		return super.isSameAs(a, b);
	}
        
        
        
        @Override
         public boolean isDirectlyConnected(URI link, R a, R b) { //sameAs can be replaced with this?
//             
//             if (this.config.getParam("useIndexes").equals(true)) {
//                    
//                    List<String> objects_a = directlyConnectedIndex.getList(a.getUri().stringValue()+ ":" + link.stringValue());
//                    
//                    if(objects_a != null){
//                        
//                        return objects_a.contains(b.getUri().stringValue());
//
//                    }
//                    else
//                    {   
//                        objects_a = super.getOutgoingEdges(link , a);
//                        
//                        if(objects_a != null){
//                            directlyConnectedIndex.addList(a.getUri().stringValue()+ ":" + link.stringValue() , objects_a);
//                                                        
//                            return objects_a.contains(b.getUri().stringValue());
//                        }
//                        
//                        return false;
//                        
//                    }                      
//
//                }
//                
//                return super.isDirectlyConnected(link, a, b);

                if (this.config.getParam("useIndexes").equals(true)) {
                    
                    String directlyConnected  = directlyConnectedIndex.getValue(a.getUri().stringValue()+ ":" + link.stringValue() + ":" + b.getUri().stringValue());
                    
                    if(directlyConnected != null && !directlyConnected.equals("-1")){
                        return Boolean.parseBoolean(directlyConnected);

                    }
                    
                    else if(directlyConnected != null && directlyConnected.equals("-1")){
                        return false;
                    }
                    
                    else
                    {   
                        directlyConnected = Boolean.toString(super.isDirectlyConnected(link, a, b));
                        
                        if(directlyConnected != null){
                            directlyConnectedIndex.addValue(a.getUri().stringValue()+ ":" + link.stringValue() + ":" + b.getUri().stringValue() , directlyConnected);
                            return Boolean.parseBoolean(directlyConnected);
                        }
                        
                        
                        directlyConnectedIndex.addValue(a.getUri().stringValue()+ ":" + link.stringValue() + ":" + b.getUri().stringValue() , "-1");
                        return false;
                        
                    }                      

                }
                
                return super.isDirectlyConnected(link, a, b);
                
         }
         
         
        @Override
         public int countPropertyOccurrence(URI link){
             if (this.config.getParam("useIndexes").equals(true)) {
                 
                 String countOccurence = propertyOccurrenceIndex.getValue(link.stringValue());
                 
                 if(countOccurence != null && ! countOccurence.equals("-1")){
                     return Integer.parseInt(countOccurence);
                 }
                 
                 else if(countOccurence != null && countOccurence.equals("-1"))
                     return 0;
                 
                 else{
                     
                     countOccurence = Integer.toString(super.countPropertyOccurrence(link));
                     
                     if(countOccurence != null){
                         propertyOccurrenceIndex.addValue(link.stringValue() , countOccurence);
                         return Integer.parseInt(countOccurence);
                     }
                     
                     propertyOccurrenceIndex.addValue(link.stringValue() , "-1");
                     return 0;
                     
                 }           
                 
             }
             
             return super.countPropertyOccurrence(link);
             
         }      
         
        
    }
       

