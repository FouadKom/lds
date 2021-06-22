/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager;

import java.util.ArrayList;
import java.util.List;
import lds.LdManager.ontologies.Ontology;
import lds.indexing.LdIndex;
import lds.indexing.LdIndexerManager;
import lds.resource.R;
import org.openrdf.model.URI;
import ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class ResimLdManager extends DistanceMeasuresLdManager{
    
    private boolean useIndex;
  
    private LdIndex sameAsIndex;
    private LdIndex propertyOccurrenceIndex;
    private LdIndex objectsIndex;
    private LdIndex countShareCommonSubjectsIndex;
    private LdIndexerManager manager;
    

    public ResimLdManager (LdDataset dataset , boolean useIndex) throws Exception {                
            super(dataset , useIndex);
            this.useIndex = useIndex; 

    }


    @Override
    public void loadIndexes() throws Exception {

        super.loadIndexes();
        
        manager = LdIndexerManager.getManager();
        
        // TODO: specify an index directory
        
        String sameAsResourcesIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_sameAsResources_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        sameAsIndex = manager.loadIndex(sameAsResourcesIndexFile);
        
        String propertyOccurrenceIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/resim_propertyOccurence_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        propertyOccurrenceIndex = manager.loadIndex(propertyOccurrenceIndexFile);
        
        String countShareCommonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Resim/countShareCommonSubjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        countShareCommonSubjectsIndex = manager.loadIndex(countShareCommonSubjectsIndexFile);
        
    }


    @Override
    public void closeIndexes(){
        if (useIndex) {

            super.closeIndexes();
            manager.closeIndex(sameAsIndex);
            manager.closeIndex(propertyOccurrenceIndex);
            manager.closeIndex(countShareCommonSubjectsIndex);
        }

    }
 
    @Override
    public List<String> getSameAsResources(R a) {
        if(useIndex){
            return sameAsIndex.getListFromIndex(dataset , Utility.createKey(a) , baseClassPath + "getSameAsResources" , a );
        }
        
        return super.getSameAsResources(a);       
    }
    
    
    @Override
    public int countPropertyOccurrence(URI link){
        if(useIndex){
            return propertyOccurrenceIndex.getIntegerFromIndex(dataset , Utility.createKey(link) , baseClassPath + "countPropertyOccurrence", link);
        }
        
        return super.countPropertyOccurrence(link);
    
    }
    
    
    @Override
    public int countShareCommonSubjects(URI link , R a){
        if(useIndex){
           return countShareCommonSubjectsIndex.getIntegerFromIndex(dataset , Utility.createKey(a , link), baseClassPath + "countShareCommonSubjects" , link , a);
        }
        
        return super.countShareCommonSubjects(link, a);
    }
    
    public boolean isSameAs(R a, R b) {
        List<String> sameAs_resources_a = getSameAsResources(a);
        if(sameAs_resources_a == null)
            return false;
        else if(sameAs_resources_a.contains(Ontology.compressValue(b)))
            return true;
        
        return false;
    }
    
    public int countTyplessCommonSubjects(URI li, URI lj , R a){
        int count = 0;
        List<String> commonSubjects = getCommonSubjects(a);
        
        if(commonSubjects ==  null || commonSubjects.contains("-1"))
            return count;
        
        for(String items:commonSubjects){
            String string[] =  items.split("\\|");
            String property1 = string[1];
            String property2 = string[2];
            
            if(property1.equals(Ontology.compressValue(li)) && property2.equals(Ontology.compressValue(lj)))
                count++;
        }           
    
        return count;
    }
    
    public int countTyplessCommonObjects(URI li, URI lj , R a){
        int count = 0;
        List<String> commonObjects = getCommonObjects(a);
//        List<String> distinctCommonObjects = new ArrayList<>();
        
        if(commonObjects ==  null || commonObjects.contains("-1"))
            return count;
        
        for(String items:commonObjects){
            String string[] =  items.split("\\|");
//            String object = string[0];
            String property1 = string[1];
            String property2 = string[2];
            
            if(property1.equals(Ontology.compressValue(li)) && property2.equals(Ontology.compressValue(lj)))
                count++;
//                distinctCommonObjects.add(object);
        }           
    
        return count;
//          return distinctCommonObjects.size();
    }
    
}

    
