/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager;


import java.util.List;
import lds.indexing.LdIndex;
import lds.indexing.LdIndexer;
import lds.resource.R;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Nasredine CHENIKI
 */
public class ResimLdManager extends DistanceMeasuresLdManager {
    
    private boolean useIndex;

    private LdIndex sameAsIndex;
    private LdIndex subjectsIndex;
    private LdIndex propertyOccurrenceIndex;
    private LdIndex commonObjectsIndex;
    private LdIndex commonSubjectsIndex;
    private LdIndex typlessCommonObjectsIndex;
    private LdIndex typlessCommonSubjectsIndex;        

    private LdIndexer manager;

    public ResimLdManager(LdDataset dataset , boolean useIndex) throws Exception {                
            super(dataset , useIndex);
            this.useIndex = useIndex; 

    }


    @Override
    public void loadIndexes() throws Exception {

        super.loadIndexes();
        
        manager = LdIndexer.getManager();
        
        // TODO: specify an index directory
        String sameAsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_sameAs_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String subjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_subjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String propertyOccurrenceIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_propertyOccurence_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String typlessCommonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_typlessCommonObjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String typlessCommonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_typlessCommonSubjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String commonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_commonObjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String commonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_commonSubjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";

        sameAsIndex = manager.loadIndex(sameAsIndexFile);
        subjectsIndex = manager.loadIndex(subjectsIndexFile);
        propertyOccurrenceIndex = manager.loadIndex(propertyOccurrenceIndexFile);
        typlessCommonObjectsIndex = manager.loadIndex(typlessCommonObjectsIndexFile);
        typlessCommonSubjectsIndex = manager.loadIndex(typlessCommonSubjectsIndexFile);
        commonObjectsIndex = manager.loadIndex(commonObjectsIndexFile);
        commonSubjectsIndex = manager.loadIndex(commonSubjectsIndexFile);

    }


    @Override
    public void closeIndexes(){
        if (useIndex) {

            super.closeIndexes();

            manager.closeIndex(sameAsIndex);
            manager.closeIndex(subjectsIndex);
            manager.closeIndex(propertyOccurrenceIndex);
            manager.closeIndex(commonSubjectsIndex);
            manager.closeIndex(commonObjectsIndex);
            manager.closeIndex(typlessCommonSubjectsIndex);
            manager.closeIndex(typlessCommonObjectsIndex);


        }

    }
       
         
        
    @Override
    public int countSubject(URI link , R a) {
       int count = 0;

       if (useIndex) {
         return subjectsIndex.getIntegerFromIndex(dataset , a.getUri().stringValue()+ ":" + link.stringValue(), baseClassPath + "countSubject" , link , a);

       }

      count = super.countSubject(link , a);
      return count;
    }


    @Override
    public int countSubject(R a) {

       int count = 0;

        if (useIndex) {
         return subjectsIndex.getIntegerFromIndex(dataset , a.getUri().stringValue(), baseClassPath + "countSubject" , a);

       }

       count = super.countSubject(a);

      return count;

    }


    @Override
    public boolean isSameAs(R a, R b) {
       boolean result;

        if (useIndex) {
            return sameAsIndex.getBooleanFromIndex(dataset , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "isSameAs", a , b);

        }

        result = super.isSameAs(a, b);
        return result;

    }

    @Override
     public int countPropertyOccurrence(URI link){
         int count = 0;

         if (useIndex) {
               return propertyOccurrenceIndex.getIntegerFromIndex(dataset , link.stringValue() , baseClassPath + "countPropertyOccurrence", link);
           }

           count = super.countPropertyOccurrence(link);

      return count;

     }


    @Override
    public List<String> getTyplessCommonObjects(R a , R b){

        List<String> list;

        if (useIndex) {
             return typlessCommonObjectsIndex.getListFromIndex(dataset , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "getTyplessCommonObjects" , a , b);
        }  

        list = super.getTyplessCommonObjects(a, b);

        return list;
    }


    @Override
    public List<String> getTyplessCommonSubjects(R a , R b){
        List<String> list;

        if (useIndex) {
             return typlessCommonSubjectsIndex.getListFromIndex(dataset , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "getTyplessCommonSubjects" , a , b);
        }  

        list = super.getTyplessCommonSubjects(a, b);

        return list;



    }

    @Override
    public List<String> getCommonObjects(R a , R b){

        List<String> list;

        if (useIndex) {
             return commonObjectsIndex.getListFromIndex(dataset , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "getCommonObjects" , a , b);
        }  

        list = super.getCommonObjects(a, b);

        return list;
    }


    @Override
    public List<String> getCommonSubjects(R a , R b){
        List<String> list;

        if (useIndex) {
             return commonSubjectsIndex.getListFromIndex(dataset , a.getUri().stringValue()+ ":" + b.getUri().stringValue() , baseClassPath + "getCommonSubjects" , a , b);
        } 

        list = super.getCommonSubjects(a, b);
        return list;

    }  
         
 }