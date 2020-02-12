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
import slib.graph.model.impl.repo.URIFactoryMemory;
import slib.graph.model.repo.URIFactory;

/**
 *
 * @author Fouad Komeiha
 */
public class DistanceMeasuresLdManagerO extends LdManagerBaseO{
    private boolean useIndex;
    
    protected LdIndex edgesIndex;
    protected LdIndex commonObjectsIndex;
    protected LdIndex commonSubjectsIndex;
    protected LdIndex countShareCommonObjectsIndex;
    protected LdIndex objectsIndex;
    
    
    protected LdIndexer manager;
    
    public DistanceMeasuresLdManagerO(LdDataset dataset , boolean useIndex) {
        super(dataset);
        this.useIndex = useIndex;

    }
    
    public void loadIndexes() throws Exception {
        
        manager = LdIndexer.getManager();
        
        String edgesIndexFile = System.getProperty("user.dir") + "/Indexes/Opt/Distance_Measures/edges_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        edgesIndex = manager.loadIndex(edgesIndexFile);
        
        String objectsIndexFile = System.getProperty("user.dir") + "/Indexes/Opt/Distance_Measures/objects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        objectsIndex = manager.loadIndex(objectsIndexFile);
        
        String commonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Opt/Distance_Measures/commonObjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        commonObjectsIndex = manager.loadIndex(commonObjectsIndexFile);
        
        String countShareCommonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Opt/Distance_Measures/countShareCommonObjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        countShareCommonObjectsIndex = manager.loadIndex(countShareCommonObjectsIndexFile);
        
        String commonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Opt/Resim/commonSubjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        commonSubjectsIndex = manager.loadIndex(commonSubjectsIndexFile);
    }
    
     public void closeIndexes(){
        if (useIndex) {
            manager.closeIndex(edgesIndex);
            manager.closeIndex(objectsIndex);
            manager.closeIndex(commonObjectsIndex);
            manager.closeIndex(commonSubjectsIndex);
            manager.closeIndex(countShareCommonObjectsIndex);
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
        
        if(! edges_a.contains("-1") && ! edges_b.contains("-1")){
            edges_a.addAll(edges_b);
        
            edges_a.forEach((edge) -> {
                URI e = factory.getURI(Utility.decompressValue(edge));
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
            if(string[1].equals(Utility.compressValue(l)))
                count++;
            
        }
        
        return count;
    }
    
    public boolean isDirectlyConnected(URI l, R a, R b) {
        List<String> objects = getObjects(a);
        
        if(objects == null)
            return false;
        
        else if(objects.contains(Utility.compressValue(b)+"|"+Utility.compressValue(l)))
            return true;
        
        return false;
    }
    
    public int countCommonObjects(URI link, R a , R b){
        int count = 0;
        List<String> commonObjects = getCommonObjects(a , b);        
        String l = Utility.compressValue(link);
        
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
        String link = Utility.compressValue(l);
        
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
            
            if(property1.equals(Utility.compressValue(li)) && property2.equals(Utility.compressValue(lj)))
                count++;
        }           
    
        return count;
    }
    
    public int countShareCommonSubjects(URI l, R a, R b) {
        int count = 0;
        List<String> commonSubjects = getCommonSubjects(a , b);
        String link = Utility.compressValue(l);
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
            
            if(property1.equals(Utility.compressValue(li)) && property2.equals(Utility.compressValue(lj)))
                count++;
        }           
    
        return count;
    }

    
    public int countCommonSubjects(URI link, R a , R b){
        int count = 0;
        List<String> commonSubjects = getCommonSubjects(a , b);
        String l = Utility.compressValue(link);
        
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
