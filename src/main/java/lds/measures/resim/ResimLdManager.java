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
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;
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
		List<String> Ingoingedges_a = new ArrayList<>();
                List<String> Ingoingedges_b = new ArrayList<>();
                List<String> Outgoingedges_a = new ArrayList<>();
                List<String> Outgoingedges_b = new ArrayList<>();
                
                Set<URI> edges = new HashSet();
                
                Ingoingedges_a = getIngoingEdges(a);
                Ingoingedges_b = getIngoingEdges(b);
                Outgoingedges_a = getOutgoingEdges(a);
                Outgoingedges_b = getOutgoingEdges(b);
                
                edges.addAll(Utility.toURI(Ingoingedges_a));
                edges.addAll(Utility.toURI(Ingoingedges_b));
                edges.addAll(Utility.toURI(Outgoingedges_a));
                edges.addAll(Utility.toURI(Outgoingedges_b));
                
//                if(edges == null || edges.isEmpty()){
//                    return super.getEdges(a , b);
//                }
                
		return edges;

                
	}
        
        
        @Override
        public List<String> getIngoingEdges(R a) {

            if (this.config.getParam("useIndexes").equals(true)) {
                Object[] args = new Object[1];
                args[0] = a;
                return Utility.getListFromIndex(ingoingEdgesIndex , a.getUri().stringValue(), "getIngoingEdges" ,  args);
            }
            return super.getIngoingEdges(a);
        }
        
        
        @Override
        public List<String> getOutgoingEdges(R a){

            if(this.config.getParam("useIndexes").equals(true)){
                Object[] args = new Object[1];
                args[0] = a;
                return Utility.getListFromIndex(outgoingEdgesIndex , a.getUri().stringValue(), "getOutgoingEdges" , args);
            }
            
            return super.getOutgoingEdges(a);
        }     
        
        @Override
        public int countSubject(URI link , R a) {
                
           if (this.config.getParam("useIndexes").equals(true)) {
             Object[] args = new Object[2];
             args[0] = link;
             args[1] = a;
             return Utility.getIntegerFromIndex(subjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue(), "countSubject" , args);

           }
           return super.countSubject(link , a);
	}
        
        
        @Override
        public int countSubject(R a) {

            if (this.config.getParam("useIndexes").equals(true)) {
             Object[] args = new Object[1];
             args[0] = a;
             return Utility.getIntegerFromIndex(subjectsIndex , a.getUri().stringValue(), "countSubject" , args);

           }
           return super.countSubject(a);
               
	}
        
        
       @Override
        public int countObject(URI link , R a) {

            if (this.config.getParam("useIndexes").equals(true)) {
                 Object[] args = new Object[2];
                 args[0] = link;
                 args[1] = a;
                 return Utility.getIntegerFromIndex(objectsIndex , a.getUri().stringValue()+ ":" + link.stringValue() , "countObject" , args);

           }
           return super.countObject(a);
        }
                

        
        
        @Override
        public int countObject(R a) {
                                
           if (this.config.getParam("useIndexes").equals(true)) {
                 Object[] args = new Object[1];
                 args[0] = a;
                 return Utility.getIntegerFromIndex(objectsIndex , a.getUri().stringValue(), "countObject" , args);

           }
           return super.countObject(a);
                
	}
        
        @Override
        public int countShareCommonObjects(URI link, R a ) {
            
              if (this.config.getParam("useIndexes").equals(true)) {
                 Object[] args = new Object[2];
                 args[0] = link;
                 args[1] = a;

                 List<String> list = Utility.getListFromIndex(shareCommonObjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue(), "listShareCommonObject" , args);
                 if(list != null && ! list.contains("-1")){
                        return list.size();

                    }
                    else if(list != null && list.contains("-1")){
                        return 0;

                    }
              }
              return super.countShareCommonObjects(link , a);

              
             }
        
         @Override
         public boolean shareCommonObject(URI link , R a, R b){

            if (this.config.getParam("useIndexes").equals(true)) {
                Object[] args = new Object[2];
                args[0] = link;
                args[1] = a;

                List<String> list = Utility.getListFromIndex(shareCommonObjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue(), "listShareCommonObject" , args);
                if(list != null && !list.contains("-1")){
                    return list.contains(b.getUri().stringValue());

                }
                else if(list != null && list.contains("-1")){
                    return false;
                }
            }
            return super.shareCommonObject(link , a , b);
        }
        
        @Override
        public int countShareCommonSubjects(URI link, R a ) {
    
                if (this.config.getParam("useIndexes").equals(true)) {
                    Object[] args = new Object[2];
                    args[0] = link;
                    args[1] = a;
                    
                    List<String> list = Utility.getListFromIndex(shareCommonSubjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue(), "listShareCommonSubject" , args);
                    if(list != null && !list.contains("-1")){
                        return list.size();

                    }
                    else if(list != null && list.contains("-1")){
                        return 0;
                    }
                }
                return super.countShareCommonSubjects(link , a);
        }
        
        @Override
         public boolean shareCommonSubject(URI link , R a, R b){
                  
          if (this.config.getParam("useIndexes").equals(true)) {
            Object[] args = new Object[2];
            args[0] = link;
            args[1] = a;

            List<String> list = Utility.getListFromIndex(shareCommonSubjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue(), "listShareCommonSubject" , args);
            if(list != null && !list.contains("-1")){
                return list.contains(b.getUri().stringValue());

            }
            else if(list != null && list.contains("-1")){
                return false;
            }

          }
          return super.shareCommonSubject(link , a , b);

        }

       
        @Override
	public boolean isSameAs(R a, R b) {

            if (this.config.getParam("useIndexes").equals(true)) {
                Object[] args = new Object[2];
                args[0] = a;
                args[1] = b;

                return Utility.getBooleanFromIndex(sameAsIndex, a.getUri().stringValue()+ ":" + b.getUri().stringValue() , "isSameAs", args);

            }
            return super.isSameAs(a, b);
                
	}
        
        
        
        @Override
         public boolean isDirectlyConnected(URI link, R a, R b) { //sameAs can be replaced with this?
                  
              if (this.config.getParam("useIndexes").equals(true)) {
                  Object[] args = new Object[3];
                  args[0] = link;
                  args[1] = a;
                  args[2] = b;

                  return Utility.getBooleanFromIndex(directlyConnectedIndex, a.getUri().stringValue()+ ":" + link.stringValue() + ":" + b.getUri().stringValue(), "isDirectlyConnected" , args);
              }
              return super.isDirectlyConnected(link, a, b);
                
         }
         
         
        @Override
         public int countPropertyOccurrence(URI link){

             if (this.config.getParam("useIndexes").equals(true)) {
                   Object[] args = new Object[1];
                   args[0] = link;

                   return Utility.getIntegerFromIndex(propertyOccurrenceIndex, link.stringValue() , "countPropertyOccurrence", args);
               }
               return super.countPropertyOccurrence(link);
         }      
         
        
    }
       

