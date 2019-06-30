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
    
        private LdDataset dataset;
        private boolean useIndex;
	
        private String baseClassPath = "lds.LdManager.LdManagerBase.";       
        
	public LdIndexer sameAsIndex;
        public LdIndexer ingoingEdgesIndex;
        public LdIndexer outgoingEdgesIndex;
        public LdIndexer subjectsIndex;
        public LdIndexer objectsIndex;
        public LdIndexer countShareCommonObjectsIndex;
        public LdIndexer countShareCommonSubjectsIndex;
        public LdIndexer countShareTyplessCommonObjectsIndex;
        public LdIndexer countShareTyplessCommonSubjectsIndex;
        public LdIndexer directlyConnectedIndex;
        public LdIndexer propertyOccurrenceIndex;
        public LdIndexer countResourcesIndex;
        public LdIndexer commonObjectsIndex;
        public LdIndexer commonSubjectsIndex;
        public LdIndexer typlessCommonObjectsIndex;
        public LdIndexer typlessCommonSubjectsIndex; 
        
       
        public ResimLdManager(LdDataset dataset , boolean useIndex) throws Exception {                
		super(dataset);
                this.dataset = dataset;
                this.useIndex = useIndex;             
        }
        
        /*public ResimLdManager(Conf config) throws Exception {                
		super((LdDataset)config.getParam("LdDataset"));
                this.dataset = (LdDataset)config.getParam("LdDataset");
                this.useIndex = (Boolean) config.getParam("useIndexes");
                if(useIndex)
                    loadIndexes();               
        }*/

       
	public void loadIndexes() throws Exception {
                
            // TODO: specify an index directory
            String sameAsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_sameAs_index_" + dataset.getName().replace(" ", "_") + ".db";
            String ingoingEdgesIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_ingoingEdges_index_" + dataset.getName().replace(" ", "_") + ".db";
            String outgoingEdgesIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_outgoingEdges_index_" + dataset.getName().replace(" ", "_") + ".db";
            String subjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_subjects_index_" + dataset.getName().replace(" ", "_") + ".db";
            String objectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_objects_index_" + dataset.getName().replace(" ", "_") + ".db";
            String countShareCommonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_countShareCommonObjects_index_" + dataset.getName().replace(" ", "_") + ".db";
            String countShareCommonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_countShareCommonSubjects_index_" + dataset.getName().replace(" ", "_") + ".db";
            String countShareTyplessCommonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_countShareTyplessCommonObjects_index_" + dataset.getName().replace(" ", "_") + ".db";
            String countShareTyplessCommonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_countShareTyplessCommonSubjects_index_" + dataset.getName().replace(" ", "_") + ".db";
            String directlyConnectedIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_directlyConnected_index_" + dataset.getName().replace(" ", "_") + ".db";
            String propertyOccurrenceIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_propertyOccurence_index_" + dataset.getName().replace(" ", "_") + ".db";
            String countResourcesIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_countResources_index_" + dataset.getName().replace(" ", "_") + ".db";
            String typlessCommonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_typlessCommonObjects_index_" + dataset.getName().replace(" ", "_") + ".db";
            String typlessCommonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_typlessCommonSubjects_index_" + dataset.getName().replace(" ", "_") + ".db";
            String commonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_commonObjects_index_" + dataset.getName().replace(" ", "_") + ".db";
            String commonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_commonSubjects_index_" + dataset.getName().replace(" ", "_") + ".db";

            sameAsIndex = new LdIndexer(sameAsIndexFile);
            ingoingEdgesIndex = new LdIndexer(ingoingEdgesIndexFile);
            outgoingEdgesIndex = new LdIndexer(outgoingEdgesIndexFile);
            subjectsIndex = new LdIndexer(subjectsIndexFile);
            objectsIndex = new LdIndexer(objectsIndexFile);
            countShareCommonObjectsIndex = new LdIndexer(countShareCommonObjectsIndexFile);
            countShareCommonSubjectsIndex = new LdIndexer(countShareCommonSubjectsIndexFile);
            countShareTyplessCommonObjectsIndex = new LdIndexer(countShareTyplessCommonObjectsIndexFile);
            countShareTyplessCommonSubjectsIndex = new LdIndexer(countShareTyplessCommonSubjectsIndexFile);
            directlyConnectedIndex = new LdIndexer(directlyConnectedIndexFile);
            propertyOccurrenceIndex = new LdIndexer(propertyOccurrenceIndexFile);
            countResourcesIndex = new LdIndexer(countResourcesIndexFile);
            typlessCommonObjectsIndex = new LdIndexer(typlessCommonObjectsIndexFile);
            typlessCommonSubjectsIndex = new LdIndexer(typlessCommonSubjectsIndexFile);
            commonObjectsIndex = new LdIndexer(commonObjectsIndexFile);
            commonSubjectsIndex = new LdIndexer(commonSubjectsIndexFile);
                
	}
        
        
        public void closeIndexes(){
            if (useIndex) {

                sameAsIndex.close();
                ingoingEdgesIndex.close();
                outgoingEdgesIndex.close();
                subjectsIndex.close();
                objectsIndex.close();
                countShareCommonObjectsIndex.close();
                countShareCommonSubjectsIndex.close();
                directlyConnectedIndex.close();
                propertyOccurrenceIndex.close();
                countResourcesIndex.close();
                countShareTyplessCommonObjectsIndex.close();
                countShareTyplessCommonSubjectsIndex.close();
                commonSubjectsIndex.close();
                commonObjectsIndex.close();
                typlessCommonSubjectsIndex.close();
                typlessCommonObjectsIndex.close();
                
                
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

            if (useIndex) {
                return LdIndexer.getListFromIndex(dataset , ingoingEdgesIndex , a.getUri().stringValue(), baseClassPath + "getIngoingEdges" ,  a);
            }
            return super.getIngoingEdges(a);
        }
        
        
        @Override
        public List<String> getOutgoingEdges(R a){

            if(useIndex){
                return LdIndexer.getListFromIndex(dataset , outgoingEdgesIndex , a.getUri().stringValue(), baseClassPath + "getOutgoingEdges" , a);
            }
            
            return super.getOutgoingEdges(a);
        }     
        
        @Override
        public int countSubject(URI link , R a) {
                
           if (useIndex) {
             return LdIndexer.getIntegerFromIndex(dataset , subjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue(), baseClassPath + "countSubject" , link , a);

           }
           return super.countSubject(link , a);
	}
        
        
        @Override
        public int countSubject(R a) {

            if (useIndex) {
             return LdIndexer.getIntegerFromIndex(dataset , subjectsIndex , a.getUri().stringValue(), baseClassPath + "countSubject" , a);

           }
           return super.countSubject(a);
               
	}
        
        
       @Override
        public int countObject(URI link , R a) {

            if (useIndex) {
                 return LdIndexer.getIntegerFromIndex(dataset , objectsIndex , a.getUri().stringValue()+ ":" + link.stringValue() , baseClassPath + "countObject" , link , a);

           }
           return super.countObject(link , a);
        }              

        @Override
        public int countObject(R a) {
                                
           if (useIndex) {
                 return LdIndexer.getIntegerFromIndex(dataset , objectsIndex , a.getUri().stringValue(), baseClassPath + "countObject" , a);

           }
           return super.countObject(a);
                
	}
        
        @Override
        public int countShareCommonObjects(URI link, R a ) {

               if (useIndex) {
                return LdIndexer.getIntegerFromIndex(dataset , countShareCommonObjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue(), baseClassPath + "countShareCommonObjects" , link , a);
                  
              }
              return super.countShareCommonObjects(link , a);
        }
        
        
         @Override
         public int countShareCommonObjects(URI link , R a, R b){
            
            if (useIndex) {
                return LdIndexer.getIntegerFromIndex(dataset , countShareCommonObjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue() + ":" + b.getUri().stringValue(), baseClassPath + "countShareCommonObjects" , link , a , b);
                
            }
            return super.countShareCommonObjects(link , a , b);
        }
        
        @Override
        public int countShareCommonSubjects(URI link, R a ) {
    
            if (useIndex) {
                 return LdIndexer.getIntegerFromIndex(dataset , countShareCommonSubjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue(), baseClassPath + "countShareCommonSubjects" , link , a);
                 
              }
             return super.countShareCommonSubjects(link , a);
        }
        
        @Override
        public int countShareCommonSubjects(URI link , R a, R b){
                  
            if (useIndex) {
                return LdIndexer.getIntegerFromIndex(dataset , countShareCommonSubjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue() + ":" + b.getUri().stringValue() , baseClassPath + "countShareCommonSubjects" , link , a , b);
            

          }
          return super.countShareCommonSubjects(link , a , b);

        }
         
        @Override
        public int countShareTyplessCommonSubjects(URI li, URI lj, R a, R b){
            if (useIndex) {
               return LdIndexer.getIntegerFromIndex(dataset , countShareTyplessCommonSubjectsIndex , a.getUri().stringValue()+ ":" + li.stringValue()+ ":" + lj.stringValue() + ":" + b.getUri().stringValue() , baseClassPath + "countShareTyplessCommonSubjects" , li , lj , a , b);
            }
          return super.countShareTyplessCommonSubjects(li , lj , a , b);
             
        }
      
        
        @Override
        public int countShareTyplessCommonSubjects(URI li , URI lj , R a){
            if (useIndex) {
                return LdIndexer.getIntegerFromIndex(dataset , countShareTyplessCommonSubjectsIndex , li.stringValue()+ ":" + lj.stringValue() + ":" + a.getUri().stringValue() , baseClassPath + "countShareTyplessCommonSubjects" , li , lj , a);
                
            }
            return super.countShareTyplessCommonSubjects(li , lj , a);
        }
        
        
        @Override
        public int countShareTyplessCommonObjects(URI li, URI lj, R a, R b){
            if (useIndex) {
                return LdIndexer.getIntegerFromIndex(dataset , countShareTyplessCommonObjectsIndex , a.getUri().stringValue()+ ":" + li.stringValue()+ ":" + lj.stringValue() + ":" + b.getUri().stringValue() , baseClassPath + "countShareTyplessCommonObjects" , li , lj , a , b);
                
            }
            return super.countShareTyplessCommonObjects(li , lj , a , b);
        }
        
        
        @Override
        public int countShareTyplessCommonObjects(URI li, URI lj , R a){
            if (useIndex) {
                return LdIndexer.getIntegerFromIndex(dataset , countShareTyplessCommonObjectsIndex , li.stringValue() + ":" + lj.stringValue() + ":" + a.getUri().stringValue() , baseClassPath + "countShareTyplessCommonObjects" , li , lj , a);
                
            }
            return super.countShareTyplessCommonObjects(li , lj , a);
        } 
        
       
        @Override
	public boolean isSameAs(R a, R b) {

            if (useIndex) {
                return LdIndexer.getBooleanFromIndex(dataset , sameAsIndex, a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "isSameAs", a , b);

            }
            return super.isSameAs(a, b);
                
	}
        
        
        
        @Override
         public boolean isDirectlyConnected(URI link, R a, R b) {
                  
              if (useIndex) {
                  return LdIndexer.getBooleanFromIndex(dataset , directlyConnectedIndex, a.getUri().stringValue()+ ":" + link.stringValue() + ":" + b.getUri().stringValue(), baseClassPath + "isDirectlyConnected" , link , a , b);
              }
              return super.isDirectlyConnected(link, a, b);
                
         }
         
         
        @Override
         public int countPropertyOccurrence(URI link){

             if (useIndex) {
                   return LdIndexer.getIntegerFromIndex(dataset , propertyOccurrenceIndex, link.stringValue() , baseClassPath + "countPropertyOccurrence", link);
               }
               return super.countPropertyOccurrence(link);
         }
         
        @Override /////////////////////////////////////////////////// to be checked for correctness
         public int countResource(){
             if (useIndex) {
                   return LdIndexer.getIntegerFromIndex(dataset , propertyOccurrenceIndex, "resources" , baseClassPath + "countResource" );
             }
             return super.countResource();
         }
         
         
        @Override
        public List<String> getTyplessCommonObjects(R a , R b){
            if (useIndex) {
                 return LdIndexer.getListFromIndex(dataset , typlessCommonObjectsIndex , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "getTyplessCommonObjects" , a , b);
            }                                                                                                                                                                                       
            return super.getTyplessCommonObjects(a, b);
        }
        
        
        @Override
        public List<String> getTyplessCommonSubjects(R a , R b){
            if (useIndex) {
                 return LdIndexer.getListFromIndex(dataset , typlessCommonSubjectsIndex , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "getTyplessCommonSubjects" , a , b);
            }                                                                           
            return super.getTyplessCommonSubjects(a, b);
            
        }
        
        @Override
        public List<String> getCommonObjects(R a , R b){
            if (useIndex) {
                 return LdIndexer.getListFromIndex(dataset , commonObjectsIndex , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "getTyplessCommonObjects" , a , b);
            }                                                                                                                                                                                       
            return super.getTyplessCommonObjects(a, b);
        }
        
        
        @Override
        public List<String> getCommonSubjects(R a , R b){
            if (useIndex) {
                 return LdIndexer.getListFromIndex(dataset , commonSubjectsIndex , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "getTyplessCommonSubjects" , a , b);
            }                                                                           
            return super.getTyplessCommonSubjects(a, b);
            
        }
         
         
         
        
    }
       

