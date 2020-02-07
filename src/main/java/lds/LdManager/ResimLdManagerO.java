/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager;

import java.util.ArrayList;
import java.util.List;
import lds.indexing.LdIndex;
import lds.indexing.LdIndexer;
import lds.resource.R;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class ResimLdManagerO extends DistanceMeasuresLdManagerO{
    
    private boolean useIndex;
  
    private LdIndex subjectsIndex;
    private LdIndex sameAsIndex;
    private LdIndex propertyOccurrenceIndex;
    private LdIndex objectsIndex;
    private LdIndex countShareCommonSubjectsIndex;
    private LdIndexer manager;
    

    public ResimLdManagerO (LdDataset dataset , boolean useIndex) throws Exception {                
            super(dataset , useIndex);
            this.useIndex = useIndex; 

    }


    @Override
    public void loadIndexes() throws Exception {

        super.loadIndexes();
        
        manager = LdIndexer.getManager();
        
        // TODO: specify an index directory
        
        String countSubjectIndexFile = System.getProperty("user.dir") + "/Indexes/Opt/Resim/resim_countSubject_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        subjectsIndex = manager.loadIndex(countSubjectIndexFile);
        
        String sameAsResourcesIndexFile = System.getProperty("user.dir") + "/Indexes/Opt/Resim/resim_sameAsResources_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        sameAsIndex = manager.loadIndex(sameAsResourcesIndexFile);
        
        String propertyOccurrenceIndexFile = System.getProperty("user.dir") + "/Indexes/Opt/Resim/resim_propertyOccurence_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        propertyOccurrenceIndex = manager.loadIndex(propertyOccurrenceIndexFile);
        
        String countShareCommonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Opt/Resim/countShareCommonSubjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        countShareCommonSubjectsIndex = manager.loadIndex(countShareCommonSubjectsIndexFile);
        
    }


    @Override
    public void closeIndexes(){
        if (useIndex) {

            super.closeIndexes();

            manager.closeIndex(subjectsIndex);
            manager.closeIndex(sameAsIndex);
            manager.closeIndex(propertyOccurrenceIndex);
            manager.closeIndex(countShareCommonSubjectsIndex);
        }

    }
    
    
    @Override
    public List<String> getSubjects(R a) {
        if(useIndex){
            return subjectsIndex.getListFromIndex(dataset , a.getUri().stringValue() , baseClassPath + "getSubjects" , a );
        }
        
        return super.getSubjects(a);
    } 
 
    

    @Override
    public List<String> getSameAsResources(R a) {
        if(useIndex){
            return sameAsIndex.getListFromIndex(dataset , a.getUri().stringValue() , baseClassPath + "getSameAsResources" , a );
        }
        
        return super.getSameAsResources(a);       
    }
    
    
    @Override
    public int countPropertyOccurrence(URI link){
        if(useIndex){
            return propertyOccurrenceIndex.getIntegerFromIndex(dataset , link.stringValue() , baseClassPath + "countPropertyOccurrence", link);
        }
        
        return super.countPropertyOccurrence(link);
    
    }
    
    
    @Override
    public int countShareCommonSubjects(URI link , R a){
        if(useIndex){
           return countShareCommonSubjectsIndex.getIntegerFromIndex(dataset , a.getUri().stringValue()+ ":" + link.stringValue(), baseClassPath + "countShareCommonSubjects" , link , a);
        }
        
        return super.countShareCommonSubjects(link, a);
    }
    
    public boolean isSameAs(R a, R b) {
        List<String> sameAs_resources_a = getSameAsResources(a);
        if(sameAs_resources_a == null)
            return false;
        else if(sameAs_resources_a.contains(b.getUri().stringValue()))
            return true;
        
        return false;
    }
    
    
    public int countSubject(R a) {
//        return (getSubjects(a)== null ? 0 : getSubjects(a).size());
    
        List<String> list = getSubjects(a);
        
        if(list == null)
            return 0;
        
        List<String> subjects = new ArrayList<>();
        
        for(String item: list){
            String string[] =  item.split("\\|");
            String subject = string[0];

            if(subjects.isEmpty() || ! subjects.contains(subject))
                subjects.add(subject);
        }
        
        return subjects.size();
                
    }

    public int countSubject(URI l, R a) {
        List<String> subjects_a = getSubjects(a);
        int count = 0;
        
        if(subjects_a == null)
            return count;
        
        for(String subjects: subjects_a){
            String string[] =  subjects.split("\\|");
            if(string[1].equals(l.stringValue()))
                count++;
            
        }
        
        return count;
    }
    
}

    
