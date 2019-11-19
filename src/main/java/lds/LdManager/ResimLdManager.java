/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager;

import lds.measures.resim.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lds.indexing.LdIndexer;
import lds.resource.R;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Literal;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Nasredine CHENIKI
 */
//public class ResimLdManager extends DistanceMeasuresLdManager {
public class ResimLdManager extends LdManagerBase {
    
        private boolean useIndex;
	
        private LdIndexer sameAsIndex;
        private LdIndexer subjectsIndex;
        private LdIndexer propertyOccurrenceIndex;
        private LdIndexer commonObjectsIndex;
        private LdIndexer commonSubjectsIndex;
        private LdIndexer typlessCommonObjectsIndex;
        private LdIndexer typlessCommonSubjectsIndex; 
        
        //Added from DistanceMeasuresLDManager
        protected LdIndexer ingoingEdgesIndex;
        protected LdIndexer outgoingEdgesIndex;
        protected LdIndexer objectsIndex;
        protected LdIndexer countShareCommonObjectsIndex;
        protected LdIndexer countShareCommonSubjectsIndex;
        protected LdIndexer countShareTyplessCommonObjectsIndex;
        protected LdIndexer countShareTyplessCommonSubjectsIndex;
        protected LdIndexer directlyConnectedIndex;
        protected LdIndexer countResourcesIndex;
         //-----------------------------------------------------
        
      
       
        
//        public ResimLdManager(LdDataset dataset , boolean useIndex) throws Exception {                
//		super(dataset , useIndex);
//                this.useIndex = useIndex; 
//
//        }
        
        //added from DistanceMeasuresLdManager - to be deleted
        public ResimLdManager(LdDataset dataset , boolean useIndex) throws Exception {                
		super(dataset);
                this.useIndex = useIndex; 

        }

       //Un comment after deleting all methods from DistanceLdManager
//        @Override
//	public void loadIndexes() throws Exception {
//            
//            super.loadIndexes();
//                
//            // TODO: specify an index directory
//            String sameAsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_sameAs_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
//            String subjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_subjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
//            String propertyOccurrenceIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_propertyOccurence_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
//            String typlessCommonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_typlessCommonObjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
//            String typlessCommonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_typlessCommonSubjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
//            String commonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_commonObjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
//            String commonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_commonSubjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
//
//            sameAsIndex = new LdIndexer(sameAsIndexFile);
//            subjectsIndex = new LdIndexer(subjectsIndexFile);
//            propertyOccurrenceIndex = new LdIndexer(propertyOccurrenceIndexFile);
//            typlessCommonObjectsIndex = new LdIndexer(typlessCommonObjectsIndexFile);
//            typlessCommonSubjectsIndex = new LdIndexer(typlessCommonSubjectsIndexFile);
//            commonObjectsIndex = new LdIndexer(commonObjectsIndexFile);
//            commonSubjectsIndex = new LdIndexer(commonSubjectsIndexFile);
//                
//	}
//        
//        
//        @Override
//        public void closeIndexes(){
//            if (useIndex) {
//                
//                super.closeIndexes();
//                
//                sameAsIndex.close();
//                subjectsIndex.close();
//                propertyOccurrenceIndex.close();
//                commonSubjectsIndex.close();
//                commonObjectsIndex.close();
//                typlessCommonSubjectsIndex.close();
//                typlessCommonObjectsIndex.close();
//                
//                
//            }
//             
//        }
        
        //added from DistanceMeasuresLdManager - to be deleted
        public void loadIndexes() throws Exception {
                
        // TODO: specify an index directory
        String ingoingEdgesIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/ingoingEdges_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String outgoingEdgesIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/outgoingEdges_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String objectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/objects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countShareCommonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/countShareCommonObjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countShareCommonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/countShareCommonSubjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countShareTyplessCommonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/countShareTyplessCommonObjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countShareTyplessCommonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/countShareTyplessCommonSubjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String directlyConnectedIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/directlyConnected_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countResourcesIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/countResources_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        
        String sameAsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_sameAs_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String subjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_subjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String propertyOccurrenceIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_propertyOccurence_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String typlessCommonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_typlessCommonObjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String typlessCommonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_typlessCommonSubjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String commonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_commonObjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String commonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_commonSubjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";

        ingoingEdgesIndex = new LdIndexer(ingoingEdgesIndexFile);
        outgoingEdgesIndex = new LdIndexer(outgoingEdgesIndexFile);
        objectsIndex = new LdIndexer(objectsIndexFile);
        countShareCommonObjectsIndex = new LdIndexer(countShareCommonObjectsIndexFile);
        countShareCommonSubjectsIndex = new LdIndexer(countShareCommonSubjectsIndexFile);
        countShareTyplessCommonObjectsIndex = new LdIndexer(countShareTyplessCommonObjectsIndexFile);
        countShareTyplessCommonSubjectsIndex = new LdIndexer(countShareTyplessCommonSubjectsIndexFile);
        directlyConnectedIndex = new LdIndexer(directlyConnectedIndexFile);
        countResourcesIndex = new LdIndexer(countResourcesIndexFile);
        
        

        sameAsIndex = new LdIndexer(sameAsIndexFile);
        subjectsIndex = new LdIndexer(subjectsIndexFile);
        propertyOccurrenceIndex = new LdIndexer(propertyOccurrenceIndexFile);
        typlessCommonObjectsIndex = new LdIndexer(typlessCommonObjectsIndexFile);
        typlessCommonSubjectsIndex = new LdIndexer(typlessCommonSubjectsIndexFile);
        commonObjectsIndex = new LdIndexer(commonObjectsIndexFile);
        commonSubjectsIndex = new LdIndexer(commonSubjectsIndexFile);

    }

    //added from DistanceMeasuresLdManager - to be deleted
    public void closeIndexes(){
        if (useIndex) {

            ingoingEdgesIndex.close();
            outgoingEdgesIndex.close();
            objectsIndex.close();
            countShareCommonObjectsIndex.close();
            countShareCommonSubjectsIndex.close();
            directlyConnectedIndex.close();
            countShareTyplessCommonObjectsIndex.close();
            countShareTyplessCommonSubjectsIndex.close();
            countResourcesIndex.close();
            
            sameAsIndex.close();
            subjectsIndex.close();
            propertyOccurrenceIndex.close();
            commonSubjectsIndex.close();
            commonObjectsIndex.close();
            typlessCommonSubjectsIndex.close();
            typlessCommonObjectsIndex.close();

        }

     }
         
         
        
        @Override
        public int countSubject(URI link , R a) {
            
//           double startTime , endTime , duration;
//           
//           startTime = System.nanoTime();
           
           int count = 0;
                
           if (useIndex) {
             return LdIndexer.getIntegerFromIndex(dataset , subjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue(), baseClassPath + "countSubject" , link , a);

           }
           
           count = super.countSubject(link , a);
           
           //end timing
//          endTime = System.nanoTime();
//          duration = (endTime - startTime) / 1000000000 ;
//          System.out.println("countSubject(" + link.stringValue() + " , " +  a.getUri().stringValue() +") finished in " + duration + " second(s) ");
//          System.out.println();
           
          return count;
	}
        
        
        @Override
        public int countSubject(R a) {
            
//           double startTime , endTime , duration;
//           
//           startTime = System.nanoTime();
           
           int count = 0;

            if (useIndex) {
             return LdIndexer.getIntegerFromIndex(dataset , subjectsIndex , a.getUri().stringValue(), baseClassPath + "countSubject" , a);

           }
            
           count = super.countSubject(a);
           
           //end timing
//          endTime = System.nanoTime();
//          duration = (endTime - startTime) / 1000000000 ;
//          System.out.println("countSubject(" + a.getUri().stringValue() +") finished in " + duration + " second(s) ");
//          System.out.println();
           
          return count;
               
	}

        
        @Override
	public boolean isSameAs(R a, R b) {
            
//           double startTime , endTime , duration;
//           
//           startTime = System.nanoTime();
//           
           boolean result;

            if (useIndex) {
                return LdIndexer.getBooleanFromIndex(dataset , sameAsIndex, a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "isSameAs", a , b);

            }
            
            result = super.isSameAs(a, b);
            
            //end timing
//          endTime = System.nanoTime();
//          duration = (endTime - startTime) / 1000000000 ;
//          System.out.println("isSameAs(" + a.getUri().stringValue() + " , " + b.getUri().stringValue() + ") finished in " + duration + " second(s) ");
//          System.out.println();
            
            return result;
                
	}
        
        @Override
         public int countPropertyOccurrence(URI link){
//             double startTime , endTime , duration;
//           
//             startTime = System.nanoTime();
             
             int count = 0;

             if (useIndex) {
                   return LdIndexer.getIntegerFromIndex(dataset , propertyOccurrenceIndex, link.stringValue() , baseClassPath + "countPropertyOccurrence", link);
               }
             
               count = super.countPropertyOccurrence(link);
               
               //end timing
//          endTime = System.nanoTime();
//          duration = (endTime - startTime) / 1000000000 ;
//          System.out.println("countPropertyOccurrence(" + link.stringValue() + ") finished in " + duration + " second(s) ");
////          System.out.println();
          
          return count;

         }
         
        
        @Override
        public List<String> getTyplessCommonObjects(R a , R b){
            
//            double startTime , endTime , duration;
//           
//            startTime = System.nanoTime();
            
            List<String> list;
             
            if (useIndex) {
                 return LdIndexer.getListFromIndex(dataset , typlessCommonObjectsIndex , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "getTyplessCommonObjects" , a , b);
            }  
            
            list = super.getTyplessCommonObjects(a, b);
            
            //end timing
//            endTime = System.nanoTime();
//            duration = (endTime - startTime) / 1000000000 ;
//            System.out.println("getTyplessCommonObjects(" + a.getUri().stringValue() + " , " + b.getUri().stringValue() + ") finished in " + duration + " second(s) ");
//            System.out.println();
          
            return list;
        }
        
        
        @Override
        public List<String> getTyplessCommonSubjects(R a , R b){
//            double startTime , endTime , duration;
//           
//            startTime = System.nanoTime();
            
            List<String> list;
            
            if (useIndex) {
                 return LdIndexer.getListFromIndex(dataset , typlessCommonSubjectsIndex , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "getTyplessCommonSubjects" , a , b);
            }  
            
            list = super.getTyplessCommonSubjects(a, b);
            
            //end timing
//            endTime = System.nanoTime();
//            duration = (endTime - startTime) / 1000000000 ;
//            System.out.println("getTyplessCommonSubjects(" + a.getUri().stringValue() + " , " + b.getUri().stringValue() + ") finished in " + duration + " second(s) ");
//            System.out.println();
          
            return list;
            
            
            
        }
        
        @Override
        public List<String> getCommonObjects(R a , R b){
//            double startTime , endTime , duration;
//           
//            startTime = System.nanoTime();
            
            List<String> list;
            
            if (useIndex) {
                 return LdIndexer.getListFromIndex(dataset , commonObjectsIndex , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "getCommonObjects" , a , b);
            }  
            
            list = super.getCommonObjects(a, b);
            
            //end timing
//            endTime = System.nanoTime();
//            duration = (endTime - startTime) / 1000000000 ;
//            System.out.println("getCommonObjects(" + a.getUri().stringValue() + " , " + b.getUri().stringValue() + ") finished in " + duration + " second(s) ");
//            System.out.println();
          
            return list;
        }
        
        
        @Override
        public List<String> getCommonSubjects(R a , R b){
//            double startTime , endTime , duration;
//           
//            startTime = System.nanoTime();
            
            List<String> list;
            
            if (useIndex) {
                 return LdIndexer.getListFromIndex(dataset , commonSubjectsIndex , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "getCommonSubjects" , a , b);
            } 
            
            list = super.getCommonSubjects(a, b);
            
            //end timing
//            endTime = System.nanoTime();
//            duration = (endTime - startTime) / 1000000000 ;
//            System.out.println("getCommonSubjects(" + a.getUri().stringValue() + " , " + b.getUri().stringValue() + ") finished in " + duration + " second(s) ");
//            System.out.println();
          
            return list;
            
        }
        
    
    //all methods are added from DistanceMeasuresLdManager - to be deleted
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
    public synchronized List<String> getIngoingEdges(R a) {

        if (useIndex) {
           return LdIndexer.getListFromIndex(dataset , ingoingEdgesIndex , a.getUri().stringValue(), baseClassPath + "getIngoingEdges" ,  a);
        }
        
        return super.getIngoingEdges(a);
    }


    @Override
    public synchronized List<String> getOutgoingEdges(R a){

        if(useIndex){
            return LdIndexer.getListFromIndex(dataset , outgoingEdgesIndex , a.getUri().stringValue(), baseClassPath + "getOutgoingEdges" , a);
        }
        
        return super.getOutgoingEdges(a);
    }
    
    @Override
    public int countObject(URI link , R a) {

        if (useIndex) {
             return LdIndexer.getIntegerFromIndex(dataset , objectsIndex , a.getUri().stringValue()+ ":" + link.stringValue() , baseClassPath + "countObject" , link , a);

       }
        
       return super.countObject(link , a);
    }
    
    @Override
    public boolean isDirectlyConnected(URI link, R a, R b) {

         if (useIndex) {
              return LdIndexer.getBooleanFromIndex(dataset , directlyConnectedIndex, a.getUri().stringValue()+ ":" + link.stringValue() + ":" + b.getUri().stringValue(), baseClassPath + "isDirectlyConnected" , link , a , b);
         }
         
         return super.isDirectlyConnected(link, a, b);

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
    public int countShareTyplessCommonObjects(URI li, URI lj, R a, R b){
        if (useIndex) {
            return LdIndexer.getIntegerFromIndex(dataset , countShareTyplessCommonObjectsIndex , a.getUri().stringValue()+ ":" + li.stringValue()+ ":" + lj.stringValue() + ":" + b.getUri().stringValue() , baseClassPath + "countShareTyplessCommonObjects" , li , lj , a , b);

        }
        
        return super.countShareTyplessCommonObjects(li , lj , a , b);
    }
    

    @Override
    public int countShareTyplessCommonSubjects(URI li, URI lj, R a, R b){
        if (useIndex) {
           return LdIndexer.getIntegerFromIndex(dataset , countShareTyplessCommonSubjectsIndex , a.getUri().stringValue()+ ":" + li.stringValue()+ ":" + lj.stringValue() + ":" + b.getUri().stringValue() , baseClassPath + "countShareTyplessCommonSubjects" , li , lj , a , b);
        }
        
      return super.countShareTyplessCommonSubjects(li , lj , a , b);

    }
    
    @Override /////////////////////////////////////////////////// to be checked for correctness
    public int countResource(){
         if (useIndex) {
               return LdIndexer.getIntegerFromIndex(dataset , countResourcesIndex, "resources" , baseClassPath + "countResource" );
         }
         
         return super.countResource();
    }
    //-----------------------------------------------------
         
         
         
        
    }
       

