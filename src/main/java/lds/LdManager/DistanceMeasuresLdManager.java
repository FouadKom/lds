/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lds.indexing.LdIndex;
import lds.indexing.LdIndexer;
import lds.resource.R;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class DistanceMeasuresLdManager extends LdManagerBase{
    private boolean useIndex;
    
    protected LdIndex ingoingEdgesIndex;
    protected LdIndex outgoingEdgesIndex;
    protected LdIndex objectsIndex;
    protected LdIndex countShareCommonObjectsIndex;
    protected LdIndex countShareCommonSubjectsIndex;
    protected LdIndex countShareTyplessCommonObjectsIndex;
    protected LdIndex countShareTyplessCommonSubjectsIndex;
    protected LdIndex directlyConnectedIndex;
    protected LdIndex countResourcesIndex;
    
    protected LdIndexer manager;
    
    
    public DistanceMeasuresLdManager(LdDataset dataset , boolean useIndex) {
        super(dataset);
        this.useIndex = useIndex;

    }
    
    public void loadIndexes() throws Exception {
        manager = LdIndexer.getManager();
        
        // TODO: specify an index directory
        String ingoingEdgesIndexFile = System.getProperty("user.dir") + "/Indexes/Distance_Measures/ingoingEdges_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String outgoingEdgesIndexFile = System.getProperty("user.dir") + "/Indexes/Distance_Measures/outgoingEdges_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String objectsIndexFile = System.getProperty("user.dir") + "/Indexes/Distance_Measures/objects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countShareCommonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Distance_Measures/countShareCommonObjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countShareCommonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Distance_Measures/countShareCommonSubjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countShareTyplessCommonObjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Distance_Measures/countShareTyplessCommonObjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countShareTyplessCommonSubjectsIndexFile = System.getProperty("user.dir") + "/Indexes/Distance_Measures/countShareTyplessCommonSubjects_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String directlyConnectedIndexFile = System.getProperty("user.dir") + "/Indexes/Distance_Measures/directlyConnected_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";
        String countResourcesIndexFile = System.getProperty("user.dir") + "/Indexes/All_Measures/countResources_index_" + dataset.getName().toLowerCase().replace(" ", "_") + ".db";

        ingoingEdgesIndex = manager.loadIndex(ingoingEdgesIndexFile);
        outgoingEdgesIndex = manager.loadIndex(outgoingEdgesIndexFile);
        objectsIndex = manager.loadIndex(objectsIndexFile);
        countShareCommonObjectsIndex = manager.loadIndex(countShareCommonObjectsIndexFile);
        countShareCommonSubjectsIndex = manager.loadIndex(countShareCommonSubjectsIndexFile);
        countShareTyplessCommonObjectsIndex = manager.loadIndex(countShareTyplessCommonObjectsIndexFile);
        countShareTyplessCommonSubjectsIndex = manager.loadIndex(countShareTyplessCommonSubjectsIndexFile);
        directlyConnectedIndex = manager.loadIndex(directlyConnectedIndexFile);
        countResourcesIndex = manager.loadIndex(countResourcesIndexFile);

    }


    public void closeIndexes(){
        if (useIndex) {

            manager.closeIndex(ingoingEdgesIndex);
            manager.closeIndex(outgoingEdgesIndex);
            manager.closeIndex(objectsIndex);
            manager.closeIndex(countShareCommonObjectsIndex);
            manager.closeIndex(countShareCommonSubjectsIndex);
            manager.closeIndex(directlyConnectedIndex);
            manager.closeIndex(countShareTyplessCommonObjectsIndex);
            manager.closeIndex(countShareTyplessCommonSubjectsIndex);
            manager.closeIndex(countResourcesIndex);

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
    public synchronized List<String> getIngoingEdges(R a) {

        if (useIndex) {
           return ingoingEdgesIndex.getListFromIndex(dataset , a.getUri().stringValue(), baseClassPath + "getIngoingEdges" ,  a);
        }
        
        return super.getIngoingEdges(a);
    }


    @Override
    public synchronized List<String> getOutgoingEdges(R a){

        if(useIndex){
            return outgoingEdgesIndex.getListFromIndex(dataset , a.getUri().stringValue(), baseClassPath + "getOutgoingEdges" , a);
        }
        
        return super.getOutgoingEdges(a);
    }
    
    @Override
    public int countObject(URI link , R a) {

        if (useIndex) {
             return objectsIndex.getIntegerFromIndex(dataset , a.getUri().stringValue()+ ":" + link.stringValue() , baseClassPath + "countObject" , link , a);

       }
        
       return super.countObject(link , a);
    }
    
    @Override
    public boolean isDirectlyConnected(URI link, R a, R b) {

         if (useIndex) {
              return directlyConnectedIndex.getBooleanFromIndex(dataset , a.getUri().stringValue()+ ":" + link.stringValue() + ":" + b.getUri().stringValue(), baseClassPath + "isDirectlyConnected" , link , a , b);
         }
         
         return super.isDirectlyConnected(link, a, b);

    }
    
    
    @Override
    public int countShareCommonObjects(URI link, R a ) {

        if (useIndex) {
            return countShareCommonObjectsIndex.getIntegerFromIndex(dataset , a.getUri().stringValue()+ ":" + link.stringValue(), baseClassPath + "countShareCommonObjects" , link , a);

        }
        
        return super.countShareCommonObjects(link , a);
    }


    @Override
    public int countShareCommonObjects(URI link , R a, R b){

        if (useIndex) {
            return countShareCommonObjectsIndex.getIntegerFromIndex(dataset , a.getUri().stringValue()+ ":" + link.stringValue() + ":" + b.getUri().stringValue(), baseClassPath + "countShareCommonObjects" , link , a , b);

        }
        
        return super.countShareCommonObjects(link , a , b);
    }
    

    @Override
    public int countShareCommonSubjects(URI link, R a ) {

        if (useIndex) {
             return countShareCommonSubjectsIndex.getIntegerFromIndex(dataset , a.getUri().stringValue()+ ":" + link.stringValue(), baseClassPath + "countShareCommonSubjects" , link , a);

        }
        
        return super.countShareCommonSubjects(link , a);
    }
    

    @Override
    public int countShareCommonSubjects(URI link , R a, R b){

        if (useIndex) {
            return countShareCommonSubjectsIndex.getIntegerFromIndex(dataset , a.getUri().stringValue()+ ":" + link.stringValue() + ":" + b.getUri().stringValue() , baseClassPath + "countShareCommonSubjects" , link , a , b);


      }
        
      return super.countShareCommonSubjects(link , a , b);

    }
    
    
    @Override
    public int countShareTyplessCommonObjects(URI li, URI lj, R a, R b){
        if (useIndex) {
            return countShareTyplessCommonObjectsIndex.getIntegerFromIndex(dataset , a.getUri().stringValue()+ ":" + li.stringValue()+ ":" + lj.stringValue() + ":" + b.getUri().stringValue() , baseClassPath + "countShareTyplessCommonObjects" , li , lj , a , b);

        }
        
        return super.countShareTyplessCommonObjects(li , lj , a , b);
    }
    

    @Override
    public int countShareTyplessCommonSubjects(URI li, URI lj, R a, R b){
        if (useIndex) {
           return countShareTyplessCommonSubjectsIndex.getIntegerFromIndex(dataset , a.getUri().stringValue()+ ":" + li.stringValue()+ ":" + lj.stringValue() + ":" + b.getUri().stringValue() , baseClassPath + "countShareTyplessCommonSubjects" , li , lj , a , b);
        }
        
      return super.countShareTyplessCommonSubjects(li , lj , a , b);

    }
    
    @Override /////////////////////////////////////////////////// to be checked for correctness
    public int countResource(){
         if (useIndex) {
               return countResourcesIndex.getIntegerFromIndex(dataset , "resources" , baseClassPath + "countResource" );
         }
         
         return super.countResource();
    }
    

    
}
