/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
        String countShareCommonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_countShareCommonObjects_index.db";
        String countShareCommonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_countShareCommonSubjects_index.db";
        String directlyConnectedIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_directlyConnected_index.db";
        String propertyOccurrenceIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_propertyOccurence_index.db";
        
	public LdIndexer sameAsIndex;
        public LdIndexer ingoingEdgesIndex;
        public LdIndexer outgoingEdgesIndex;
        public LdIndexer subjectsIndex;
        public LdIndexer objectsIndex;
        public LdIndexer shareCommonObjectsIndex;
        public LdIndexer shareCommonSubjectsIndex;
        public LdIndexer countShareCommonObjectsIndex;
        public LdIndexer countShareCommonSubjectsIndex;
        public LdIndexer directlyConnectedIndex;
        public LdIndexer propertyOccurrenceIndex;
        

	public ResimLdManager(LdDataset dataset, Conf config) {
		super(dataset, config);
		if (config.getParam("useIndexes").equals(true))
			loadIndexes();
	}

	private void loadIndexes() {
//                double startTime , endTime , duration;
//                
//                startTime = System.nanoTime();
                
		sameAsIndex = new LdIndexer(sameAsIndexFile);
                ingoingEdgesIndex = new LdIndexer(ingoingEdgesIndexFile);
                outgoingEdgesIndex = new LdIndexer(outgoingEdgesIndexFile);
                subjectsIndex = new LdIndexer(subjectsIndexFile);
                objectsIndex = new LdIndexer(objectsIndexFile);
                shareCommonObjectsIndex = new LdIndexer(shareCommonObjectsIndexFile);
                shareCommonSubjectsIndex = new LdIndexer(shareCommonSubjectsIndexFile);
                countShareCommonObjectsIndex = new LdIndexer(countShareCommonObjectsIndexFile);
                countShareCommonSubjectsIndex = new LdIndexer(countShareCommonSubjectsIndexFile);
                directlyConnectedIndex = new LdIndexer(directlyConnectedIndexFile);
                propertyOccurrenceIndex = new LdIndexer(propertyOccurrenceIndexFile);
                
//                endTime = System.nanoTime();
//                duration = (endTime - startTime) / 1000000000  ;
//                System.out.println("Loading Resim Indexes took " + duration + " second(s)");
                
	}
        
        public void closeIndexes(){
            if (this.config.getParam("useIndexes").equals(true)) {
                
//                double startTime , endTime , duration;
//                
//                startTime = System.nanoTime();
                
                sameAsIndex.close();
                ingoingEdgesIndex.close();
                outgoingEdgesIndex.close();
                subjectsIndex.close();
                objectsIndex.close();
                shareCommonObjectsIndex.close();
                shareCommonSubjectsIndex.close();
                countShareCommonObjectsIndex.close();
                countShareCommonSubjectsIndex.close();
                directlyConnectedIndex.close();
                propertyOccurrenceIndex.close();
                
//                endTime = System.nanoTime();
//                duration = (endTime - startTime) / 1000000000  ;
//                System.out.println("Closing Resim Indexes took " + duration + " second(s)");
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
               
                Optional.ofNullable(Utility.toURI(Ingoingedges_a)).ifPresent(edges::addAll);
                Optional.ofNullable(Utility.toURI(Ingoingedges_b)).ifPresent(edges::addAll);
                Optional.ofNullable(Utility.toURI(Outgoingedges_a)).ifPresent(edges::addAll);
                Optional.ofNullable(Utility.toURI(Outgoingedges_b)).ifPresent(edges::addAll);                
                
//                if(edges == null || edges.isEmpty()){
//                    return super.getEdges(a , b);
//                }
                
		return edges;                
	}
        
        
        @Override
        public List<String> getIngoingEdges(R a) {

            if (this.config.getParam("useIndexes").equals(true)) {
                return LdIndexer.getListFromIndex(ingoingEdgesIndex , a.getUri().stringValue(), "getIngoingEdges" ,  a);
            }
            return super.getIngoingEdges(a);
        }
        
        
        @Override
        public List<String> getOutgoingEdges(R a){

            if(this.config.getParam("useIndexes").equals(true)){
                return LdIndexer.getListFromIndex(outgoingEdgesIndex , a.getUri().stringValue(), "getOutgoingEdges" , a);
            }
            
            return super.getOutgoingEdges(a);
        }     
        
        @Override
        public int countSubject(URI link , R a) {
                
           if (this.config.getParam("useIndexes").equals(true)) {
             return LdIndexer.getIntegerFromIndex(subjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue(), "countSubject" , link , a);

           }
           return super.countSubject(link , a);
	}
        
        
        @Override
        public int countSubject(R a) {

            if (this.config.getParam("useIndexes").equals(true)) {
             return LdIndexer.getIntegerFromIndex(subjectsIndex , a.getUri().stringValue(), "countSubject" , a);

           }
           return super.countSubject(a);
               
	}
        
        
       @Override
        public int countObject(URI link , R a) {

            if (this.config.getParam("useIndexes").equals(true)) {
                 return LdIndexer.getIntegerFromIndex(objectsIndex , a.getUri().stringValue()+ ":" + link.stringValue() , "countObject" , link , a);

           }
           return super.countObject(link , a);
        }
                

        
        
        @Override
        public int countObject(R a) {
                                
           if (this.config.getParam("useIndexes").equals(true)) {
                 return LdIndexer.getIntegerFromIndex(objectsIndex , a.getUri().stringValue(), "countObject" , a);

           }
           return super.countObject(a);
                
	}
        
        @Override
        public int countShareCommonObjects(URI link, R a ) {

               if (this.config.getParam("useIndexes").equals(true)) {
                return LdIndexer.getIntegerFromIndex(countShareCommonObjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue(), "countShareCommonObjects" , link , a);
                  
              }
              return super.countShareCommonObjects(link , a);
        }
        
         @Override
         public boolean shareCommonObject(URI link , R a, R b){
            
            if (this.config.getParam("useIndexes").equals(true)) {
                return LdIndexer.getBooleanFromIndex(shareCommonObjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue() + ":" + b.getUri().stringValue(), "shareCommonObject" , link , a , b);
                
            }
            return super.shareCommonObject(link , a , b);
        }
        
        @Override
        public int countShareCommonSubjects(URI link, R a ) {
    
            if (this.config.getParam("useIndexes").equals(true)) {
                 return LdIndexer.getIntegerFromIndex(countShareCommonSubjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue(), "countShareCommonSubjects" , link , a);
                 
              }
             return super.countShareCommonSubjects(link , a);
        }
        
        @Override
         public boolean shareCommonSubject(URI link , R a, R b){
                  
            if (this.config.getParam("useIndexes").equals(true)) {
                return LdIndexer.getBooleanFromIndex(shareCommonSubjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue() + ":" + b.getUri().stringValue() , "shareCommonSubject" , link , a , b);
            

          }
          return super.shareCommonSubject(link , a , b);

        }
         
/*        @Override
        public boolean shareTyplessCommonSubject(URI li, URI lj, R k, R a, R b){
            if (this.config.getParam("useIndexes").equals(true)) {
            Object[] args = new Object[2];
            args[0] = li;
            args[1] = lj;
            args[2] = k;
            args[3] = a;
            args[4] = b;

            List<String> list = Utility.getListFromIndex(shareCommonSubjectsIndex , a.getUri().stringValue()+ ":" + li.stringValue()+ ":" + lj.stringValue() + ":" + b.getUri().stringValue() , "listShareCommonSubject" , args);
            if(list != null && !list.contains("-1")){
                return list.contains(b.getUri().stringValue());

            }
            else if(list != null && list.contains("-1")){
                return false;
            }

          }
          return super.shareCommonSubject(link , a , b);
             
        }
         
        public boolean shareTyplessCommonObject(URI li, URI lj, R k, R a, R b){
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

        }*/
       
        @Override
	public boolean isSameAs(R a, R b) {

            if (this.config.getParam("useIndexes").equals(true)) {
                return LdIndexer.getBooleanFromIndex(sameAsIndex, a.getUri().stringValue()+ ":" + b.getUri().stringValue() , "isSameAs", a , b);

            }
            return super.isSameAs(a, b);
                
	}
        
        
        
        @Override
         public boolean isDirectlyConnected(URI link, R a, R b) {
                  
              if (this.config.getParam("useIndexes").equals(true)) {
                  return LdIndexer.getBooleanFromIndex(directlyConnectedIndex, a.getUri().stringValue()+ ":" + link.stringValue() + ":" + b.getUri().stringValue(), "isDirectlyConnected" , link , a , b);
              }
              return super.isDirectlyConnected(link, a, b);
                
         }
         
         
        @Override
         public int countPropertyOccurrence(URI link){

             if (this.config.getParam("useIndexes").equals(true)) {
                   return LdIndexer.getIntegerFromIndex(propertyOccurrenceIndex, link.stringValue() , "countPropertyOccurrence", link);
               }
               return super.countPropertyOccurrence(link);
         }
         
         
         
        
    }
       

