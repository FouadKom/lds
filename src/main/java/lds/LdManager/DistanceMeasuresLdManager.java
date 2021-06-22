/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager;

import java.util.ArrayList;
import java.util.List;
import ldq.LdDataset;
import lds.LdManager.ontologies.Ontology;
import lds.indexing.LdIndex;
import lds.indexing.LdIndexerManager;
import lds.resource.R;
import org.openrdf.model.URI;
import slib.graph.model.impl.repo.URIFactoryMemory;
import slib.graph.model.repo.URIFactory;

/**
 *
 * @author Fouad Komeiha
 */
public class DistanceMeasuresLdManager extends LdManagerBase{
    private boolean useIndex;
    
    protected LdIndex edgesIndex;
    protected LdIndex commonObjectsIndex;
    protected LdIndex commonSubjectsIndex;
    protected LdIndex countShareCommonObjectsIndex;
    protected LdIndex objectsIndex;
    protected LdIndex subjectsIndex;
    
    
    protected LdIndexerManager manager;
    
    public DistanceMeasuresLdManager(LdDataset dataset , boolean useIndex) {
        super(dataset);
        this.useIndex = useIndex;

    }
    
    public void loadIndexes() throws Exception {
        
        manager = LdIndexerManager.getManager();
        
        String edgesIndexFile = System.getProperty("user.dir") + "/Indexes/Distance_Measures/edges_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        edgesIndex = manager.loadIndex(edgesIndexFile);
        
        String objectsIndexFile = System.getProperty("user.dir") + "/Indexes/Distance_Measures/objects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        objectsIndex = manager.loadIndex(objectsIndexFile);
        
        String commonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Distance_Measures/commonObjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        commonObjectsIndex = manager.loadIndex(commonObjectsIndexFile);
        
        String countShareCommonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Distance_Measures/countShareCommonObjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        countShareCommonObjectsIndex = manager.loadIndex(countShareCommonObjectsIndexFile);
        
        String commonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Distance_Measures/commonSubjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        commonSubjectsIndex = manager.loadIndex(commonSubjectsIndexFile);
        
        String countSubjectIndexFile = System.getProperty("user.dir") + "/Indexes/Distance_Measures/subjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        subjectsIndex = manager.loadIndex(countSubjectIndexFile);
    }
    
     public void closeIndexes(){
        if (useIndex) {
            manager.closeIndex(edgesIndex);
            manager.closeIndex(objectsIndex);
            manager.closeIndex(commonObjectsIndex);
            manager.closeIndex(commonSubjectsIndex);
            manager.closeIndex(countShareCommonObjectsIndex);
            manager.closeIndex(subjectsIndex);
        }
     }
    
    @Override
    public List<String> getEdges(R a) { 
        if(useIndex){
            return edgesIndex.getListFromIndex(dataset , Utility.createKey(a) , baseClassPath + "getEdges" , a);
        }
        
        return super.getEdges(a);
    }
    
    @Override
    public List<String> getObjects(R a){
        if(useIndex){
            return objectsIndex.getListFromIndex(dataset , Utility.createKey(a) , baseClassPath + "getObjects" , a );            
        }
        
        return super.getObjects(a);
    }
    
    @Override
    public List<String> getSubjects(R a) {
        if(useIndex){
            return subjectsIndex.getListFromIndex(dataset , Utility.createKey(a) , baseClassPath + "getSubjects" , a );
        }
        
        return super.getSubjects(a);
    } 
    
    @Override
    public List<String> getCommonObjects(R a , R b){
        if(useIndex){
           return commonObjectsIndex.getListFromIndex(dataset , Utility.createKey(a , b) , baseClassPath + "getCommonObjects" , a , b);
        }
        
        return super.getCommonObjects(a, b);
    }
    
    @Override
    public List<String> getCommonSubjects(R a , R b){
        if(useIndex){
           return commonSubjectsIndex.getListFromIndex(dataset , Utility.createKey(a , b) , baseClassPath + "getCommonSubjects" , a , b);
        }
        
       return super.getCommonSubjects(a, b);
    }
    
    ////////////////////////////////////////////
    @Override
    public List<String> getCommonObjects(R a){
        if(useIndex){
           return commonObjectsIndex.getListFromIndex(dataset , Utility.createKey(a) , baseClassPath + "getCommonObjects" , a);
        }
        
        return super.getCommonObjects(a);
    }
    
    @Override
    public List<String> getCommonSubjects(R a){
        if(useIndex){
           return commonSubjectsIndex.getListFromIndex(dataset , Utility.createKey(a) , baseClassPath + "getCommonSubjects" , a);
        }
        
       return super.getCommonSubjects(a);
    }
    /////////////////////////////////////////////
    
    @Override
    public int countShareCommonObjects(URI link , R a){
        if(useIndex){
            return countShareCommonObjectsIndex.getIntegerFromIndex(dataset , Utility.createKey(a , link), baseClassPath + "countShareCommonObjects" , link , a);
        }
        
        return super.countShareCommonObjects(link, a);        
    }
    
    
    public List<URI> getEdges(R a , R b) {
        URIFactory factory = URIFactoryMemory.getSingleton();
        
        List<URI> edges = new ArrayList<>();
        List<String> edges_a = getEdges(a);
        
        List<String> edges_b = getEdges(b);
        
        if(( edges_a == null || edges_a.contains("-1")) ||  ( edges_b == null || edges_b.contains("-1")) )
            return null;
        
        if(! edges_a.contains("-1") && ! edges_b.contains("-1")){
            edges_a.addAll(edges_b);
        
            edges_a.forEach((edge) -> {
                URI e = factory.getURI(Ontology.decompressValue(edge));
                if(!edges.contains(e))
                    edges.add(e);
            });
        }
        return edges;
        
    }
    
    public int countObject(R a) {
//        return (getObjects(a) == null ? 0 : getObjects(a).size());

        List<String> list = getObjects(a);
        
        if(list == null)
            return 0;
        
        List<String> objects = new ArrayList<>();
        
        for(String item: list){
            String string[] =  item.split("\\|");
            String object = string[0];

            if(objects.isEmpty() || ! objects.contains(object))
                objects.add(object);
        }
        
        return objects.size();

    }
    
    public int countObject(URI l,  R a) {
        List<String> objects_a = getObjects(a);
        int count = 0;
        
        if(objects_a == null)
            return count;
        
        for(String objects: objects_a){
            String string[] =  objects.split("\\|");
            if(string[1].equals(Ontology.compressValue(l)))
                count++;
            
        }
        
        return count;
    }
        
    public int countSubject(R a) {
//        return (getSubjects(a)== null ? 0 : getSubjects(a).size());
    
        List<String> list = getSubjects(a);
        
        if(list == null)
            return 0;
        
        List<String> subjects = new ArrayList<>();
        
        for(String item: list){
//            System.out.println(item);
            String string[] =  item.split("\\|");
            String subject = string[0];

            if(subjects.isEmpty() || ! subjects.contains(subject) )
                subjects.add(subject);
        }
        
        return subjects.size();
                
    }

    public int countSubject(URI l, R a) {
        List<String> subjects_a = getSubjects(a);
        int count = 0;
        String uri = Ontology.compressValue(l);
        
        if(subjects_a == null)
            return count;
        
        for(String subjects: subjects_a){
//            System.out.println(subjects);
            String string[] =  subjects.split("\\|" , 2);
            if(string[1].equals(uri))
                count++;
            
        }
        
        return count;
    }
    
    /*@Override
    public List<String> getCommonObjects(R a , R b){
       List<String> objects = getCommonObjects(a);
        
       List<String> commonObjects = new ArrayList<>();
                
       if(objects == null || objects.contains("-1"))
            return null;
        
        
       for(String items:objects){
            String string[] =  items.split("\\|");
            String object = string[0];
            if(b.toString().equals(Ontology.decompressValue(object)))
                commonObjects.add(object);
       } 
        
       return commonObjects;
    }
    
    @Override
    public List<String> getCommonSubjects(R a , R b){
        List<String> subjects = getCommonSubjects(a);
        
        List<String> commonSubjects = new ArrayList<>();
                
        if(subjects == null || subjects.contains("-1"))
            return null;
        
        
        for(String items:subjects){
            String string[] =  items.split("\\|");
            String subject = string[0];
            
            if(b.toString().equals(Ontology.decompressValue(subject)))
                commonSubjects.add(subject);
        } 
        
        return commonSubjects;
    }*/
    
    public boolean isDirectlyConnected(URI l, R a, R b) {
        List<String> objects = getObjects(a);
        
        if(objects == null)
            return false;
        
        else if(objects.contains(Ontology.compressValue(b)+"|"+Ontology.compressValue(l)))
            return true;
        
        return false;
    }
    
    public int countCommonObjects(URI link, R a , R b){
        int count = 0;
        List<String> commonObjects = getCommonObjects(a , b);        
        String l = Ontology.compressValue(link);
        
        if(commonObjects == null || commonObjects.contains("-1"))
            return count;
                
        for(String items:commonObjects){
            String string[] =  items.split("\\|");
            String property1 = string[1];
            String property2 = string[2];
            
            if(property1.equals(l) && property2.equals(l))
                count++;
        }           
    
        return count;
    }
    
    public int countShareCommonObjects(URI l, R a, R b) {
        int count = 0;
        List<String> commonObjects = getCommonObjects(a , b);
        String link = Ontology.compressValue(l);
        
        if(commonObjects == null || commonObjects.contains("-1"))
            return count;
        
        for(String items:commonObjects){
            String string[] =  items.split("\\|");
            String property1 = string[1];
            String property2 = string[2];
            
            if(property1.equals(link) && property2.equals(link))
                count++;
        }           
    
        return count;
    }
    
    public int countTyplessCommonObjects(URI li, URI lj, R a , R b){
        int count = 0;
        List<String> commonObjects = getCommonObjects(a , b);
        
         if(commonObjects == null || commonObjects.contains("-1"))
            return count;
        
        for(String items:commonObjects){
            String string[] =  items.split("\\|");
            String property1 = string[1];
            String property2 = string[2];
            
            if(property1.equals(Ontology.compressValue(li)) && property2.equals(Ontology.compressValue(lj)))
                count++;
        }           
    
        return count;
    }
    
    public int countShareCommonSubjects(URI l, R a, R b) {
        int count = 0;
        List<String> commonSubjects = getCommonSubjects(a , b);
        String link = Ontology.compressValue(l);
        if(commonSubjects == null || commonSubjects.contains("-1"))
            return count;
        
        for(String items:commonSubjects){
            String string[] =  items.split("\\|");
            String property1 = string[1];
            String property2 = string[2];
            
            if(property1.equals(link) && property2.equals(link))
                count++;
        }           
    
        return count;
    }
    
    public int countTyplessCommonSubjects(URI li, URI lj , R a , R b){
        int count = 0;
        List<String> commonSubjects = getCommonSubjects(a , b);
        
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

    
    public int countCommonSubjects(URI link, R a , R b){
        int count = 0;
        List<String> commonSubjects = getCommonSubjects(a , b);
        String l = Ontology.compressValue(link);
        
        if(commonSubjects == null || commonSubjects.contains("-1"))
            return count;
        
        
        for(String items:commonSubjects){
            String string[] =  items.split("\\|");
            String property1 = string[1];
            String property2 = string[2];
            
            if(property1.equals(l) && property2.equals(l))
                count++;
        }           
    
        return count;
    }
    
    
}
