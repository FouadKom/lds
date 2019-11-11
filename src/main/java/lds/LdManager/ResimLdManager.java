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
public class ResimLdManager extends DistanceMeasuresLdManager {
    
        private boolean useIndex;
	
        private LdIndexer sameAsIndex;
        private LdIndexer subjectsIndex;
        private LdIndexer propertyOccurrenceIndex;
        private LdIndexer commonObjectsIndex;
        private LdIndexer commonSubjectsIndex;
        private LdIndexer typlessCommonObjectsIndex;
        private LdIndexer typlessCommonSubjectsIndex; 
      
       
        
        public ResimLdManager(LdDataset dataset , boolean useIndex) throws Exception {                
		super(dataset , useIndex);
                this.useIndex = useIndex; 

        }

       
        @Override
	public void loadIndexes() throws Exception {
            
            super.loadIndexes();
                
            // TODO: specify an index directory
            String sameAsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_sameAs_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
            String subjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_subjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
            String propertyOccurrenceIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_propertyOccurence_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
            String typlessCommonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_typlessCommonObjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
            String typlessCommonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_typlessCommonSubjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
            String commonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_commonObjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
            String commonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_commonSubjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";

            sameAsIndex = new LdIndexer(sameAsIndexFile);
            subjectsIndex = new LdIndexer(subjectsIndexFile);
            propertyOccurrenceIndex = new LdIndexer(propertyOccurrenceIndexFile);
            typlessCommonObjectsIndex = new LdIndexer(typlessCommonObjectsIndexFile);
            typlessCommonSubjectsIndex = new LdIndexer(typlessCommonSubjectsIndexFile);
            commonObjectsIndex = new LdIndexer(commonObjectsIndexFile);
            commonSubjectsIndex = new LdIndexer(commonSubjectsIndexFile);
                
	}
        
        
        @Override
        public void closeIndexes(){
            if (useIndex) {
                
                super.closeIndexes();
                
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
            
           double startTime , endTime , duration;
           
           startTime = System.nanoTime();
           
           int count = 0;
                
           if (useIndex) {
             return LdIndexer.getIntegerFromIndex(dataset , subjectsIndex , a.getUri().stringValue()+ ":" + link.stringValue(), baseClassPath + "countSubject" , link , a);

           }
           
           count = super.countSubject(link , a);
           
           //end timing
          endTime = System.nanoTime();
          duration = (endTime - startTime) / 1000000000 ;
          System.out.println("countSubject(" + link.stringValue() + " , " +  a.getUri().stringValue() +") finished in " + duration + " second(s) ");
          System.out.println();
           
          return count;
	}
        
        
        @Override
        public int countSubject(R a) {
            
           double startTime , endTime , duration;
           
           startTime = System.nanoTime();
           
           int count = 0;

            if (useIndex) {
             return LdIndexer.getIntegerFromIndex(dataset , subjectsIndex , a.getUri().stringValue(), baseClassPath + "countSubject" , a);

           }
            
           count = super.countSubject(a);
           
           //end timing
          endTime = System.nanoTime();
          duration = (endTime - startTime) / 1000000000 ;
          System.out.println("countSubject(" + a.getUri().stringValue() +") finished in " + duration + " second(s) ");
          System.out.println();
           
          return count;
               
	}

        
        @Override
	public boolean isSameAs(R a, R b) {
            
            double startTime , endTime , duration;
           
           startTime = System.nanoTime();
           
           boolean result;

            if (useIndex) {
                return LdIndexer.getBooleanFromIndex(dataset , sameAsIndex, a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "isSameAs", a , b);

            }
            
            result = super.isSameAs(a, b);
            
            //end timing
          endTime = System.nanoTime();
          duration = (endTime - startTime) / 1000000000 ;
          System.out.println("isSameAs(" + a.getUri().stringValue() + " , " + b.getUri().stringValue() + ") finished in " + duration + " second(s) ");
          System.out.println();
            
            return result;
                
	}
        
        @Override
         public int countPropertyOccurrence(URI link){
             double startTime , endTime , duration;
           
             startTime = System.nanoTime();
             
             int count = 0;

             if (useIndex) {
                   return LdIndexer.getIntegerFromIndex(dataset , propertyOccurrenceIndex, link.stringValue() , baseClassPath + "countPropertyOccurrence", link);
               }
             
               count = super.countPropertyOccurrence(link);
               
               //end timing
          endTime = System.nanoTime();
          duration = (endTime - startTime) / 1000000000 ;
          System.out.println("countPropertyOccurrence(" + link.stringValue() + ") finished in " + duration + " second(s) ");
          System.out.println();
          
          return count;

         }
         
        
        @Override
        public List<String> getTyplessCommonObjects(R a , R b){
            
            double startTime , endTime , duration;
           
            startTime = System.nanoTime();
            
            List<String> list;
             
            if (useIndex) {
                 return LdIndexer.getListFromIndex(dataset , typlessCommonObjectsIndex , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "getTyplessCommonObjects" , a , b);
            }  
            
            list = super.getTyplessCommonObjects(a, b);
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("getTyplessCommonObjects(" + a.getUri().stringValue() + " , " + b.getUri().stringValue() + ") finished in " + duration + " second(s) ");
            System.out.println();
          
            return list;
        }
        
        
        @Override
        public List<String> getTyplessCommonSubjects(R a , R b){
            double startTime , endTime , duration;
           
            startTime = System.nanoTime();
            
            List<String> list;
            
            if (useIndex) {
                 return LdIndexer.getListFromIndex(dataset , typlessCommonSubjectsIndex , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "getTyplessCommonSubjects" , a , b);
            }  
            
            list = super.getTyplessCommonSubjects(a, b);
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("getTyplessCommonSubjects(" + a.getUri().stringValue() + " , " + b.getUri().stringValue() + ") finished in " + duration + " second(s) ");
            System.out.println();
          
            return list;
            
            
            
        }
        
        @Override
        public List<String> getCommonObjects(R a , R b){
            double startTime , endTime , duration;
           
            startTime = System.nanoTime();
            
            List<String> list;
            
            if (useIndex) {
                 return LdIndexer.getListFromIndex(dataset , commonObjectsIndex , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "getCommonObjects" , a , b);
            }  
            
            list = super.getCommonObjects(a, b);
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("getCommonObjects(" + a.getUri().stringValue() + " , " + b.getUri().stringValue() + ") finished in " + duration + " second(s) ");
            System.out.println();
          
            return list;
        }
        
        
        @Override
        public List<String> getCommonSubjects(R a , R b){
            double startTime , endTime , duration;
           
            startTime = System.nanoTime();
            
            List<String> list;
            
            if (useIndex) {
                 return LdIndexer.getListFromIndex(dataset , commonSubjectsIndex , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "getCommonSubjects" , a , b);
            } 
            
            list = super.getCommonSubjects(a, b);
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("getCommonSubjects(" + a.getUri().stringValue() + " , " + b.getUri().stringValue() + ") finished in " + duration + " second(s) ");
            System.out.println();
          
            return list;
            
        }
         
         
         
        
    }
       

