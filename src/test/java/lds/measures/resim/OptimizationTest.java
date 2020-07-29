/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lds.measures.resim;

import lds.LdManager.ontologies.Ontology;
import lds.dataset.LdDatasetCreator;
import lds.measures.weight.WeightMethod;
import lds.resource.R;
import org.junit.Test;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;
import test.utility.Util;

/**
 *
 * @author Fouad Komeiha
 */
public class OptimizationTest {
    public static final String datasetDir = System.getProperty("user.dir") + "/src/test/resources/specific_class_set.rdf";    
//    @Test
//    public void OptimizationTest() throws Exception{
    public static void main(String args[])throws Exception{
   
        R r1 = new R("http://dbpedia.org/resource/Bob_Dylan");
        R r2 = new R("http://dbpedia.org/resource/Ronnie_Hawkins");
//        R r1 = new R("http://dbpedia.org/resource/The_Noah");
//        R r2 = new R("http://dbpedia.org/resource/The_Pack_(2010_film)");
        
        double startTime , endTime , duration;
        
        LdDataset datasetMain = LdDatasetCreator.getDBpediaDataset();
        
        LdDataset datasetSpecific  = LdDatasetFactory.getInstance()
                                                     .name("specificSet")
                                                     .file(datasetDir)
                                                     .defaultGraph("http://specificSet/dataset")
                                                     .create();
        
        Conf configSim = new Conf();            
        
        configSim.addParam("LdDatasetMain" , datasetMain);
        configSim.addParam("useIndexes" , false);
        configSim.addParam("LdDatasetSpecific" , datasetSpecific);
        configSim.addParam("WeightMethod" , WeightMethod.RSLAW);
        
        ResourceSimilarity resim_o = new TResim(configSim);
        ResourceSimilarity resim = new TResim(configSim);
        resim_o.loadIndexes();
        resim.loadIndexes();
        
        startTime = System.nanoTime(); 
        
        System.out.println(resim_o.compare(r1, r2));  
        
        //end timing
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("finished in " + duration + " second(s)");
        System.out.println();
        
        startTime = System.nanoTime(); 
        
        System.out.println(resim.compare(r1, r2));
        
        //end timing
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("finished in " + duration + " second(s)");
        System.out.println();
        
        resim.closeIndexes();
        resim_o.closeIndexes();
        
        /*System.out.println(resim_o.compare(r1, r2));
        System.out.println(resim.compare(r1, r2));
        
        Set<URI> edges = resim.edges;
        List<URI> edges_o = resim_o.edges;

        if(edges.size() == edges_o.size())
            System.out.println("Edges have same size");
        
        System.out.println("Cip(a) = " + resim_o.Cip(r1) );
        System.out.println("Cip(a) = " + resim.Cip(r1) );
        System.out.println();
        System.out.println("Cop(a) = " + resim_o.Cop(r1) );
        System.out.println("Cop(a) = " + resim.Cop(r1) );
        System.out.println();*/
            
      /*  for(URI edge: edges){
            /*System.out.println("edge " + edge.stringValue());
            System.out.println("Csip(l , a , b) = " + resim_o.Csip(edge , r1 , r2) );
            System.out.println("Csip(l , a , b) = " + resim.Csip(edge , r1 , r2) );
            System.out.println();
            System.out.println("Csop(l , a , b) = " + resim_o.Csop(edge , r1 , r2) );
            System.out.println("Csop(l , a , b) = " + resim.Csop(edge , r1 , r2) );
            System.out.println();
            System.out.println("Cd(l , a , b) = " + resim_o.Cd(edge , r1 , r2) );
            System.out.println("Cd(l , a , b) = " + resim.Cd(edge , r1 , r2) );
            System.out.println();
            System.out.println("Cd(l , a) = " + resim_o.Cd(edge , r1) );
            System.out.println("Cd(l , a) = " + resim.Cd(edge , r1) );
            System.out.println();
            System.out.println("Cd(l) = " + resim_o.Cd(edge) );
            System.out.println("Cd(l) = " + resim.Cd(edge) );
            System.out.println();
            System.out.println("Cii(l , a , b) = " + resim_o.Cii(edge , r1 , r2) );
            System.out.println("Cii(l , a , b) = " + resim.Cii(edge , r1 , r2) );
            System.out.println();
            System.out.println("Cio(l , a , b) = " + resim_o.Cio(edge , r1 , r2) );
            System.out.println("Cio(l , a , b) = " + resim.Cio(edge , r1 , r2) );
            System.out.println();
            System.out.println("Cii(l , a) = " + resim_o.Cii(edge , r1) );
            System.out.println("Cii(l , a) = " + resim.Cii(edge , r1) );
            System.out.println();
            System.out.println("Cio(l , a) = " + resim_o.Cio(edge , r1) );
            System.out.println("Cio(l , a) = " + resim.Cio(edge , r1) );
            System.out.println();
            System.out.println("Cd_normalized(l , a , b) = " + resim_o.Cd_normalized(edge , r1 , r2) );
            System.out.println("Cd_normalized(l , a , b) = " + resim.Cd_normalized(edge , r1 , r2) );
            System.out.println();
            System.out.println("Cii_normalized(l , a , b) = " + resim_o.Cii_normalized(edge , r1 , r2) );
            System.out.println("Cii_normalized(l , a , b) = " + resim.Cii_normalized(edge , r1 , r2) );
            System.out.println();
            System.out.println("Cio_normalized(l , a , b) = " + resim_o.Cio_normalized(edge , r1 , r2) );
            System.out.println("Cio_normalized(l , a , b) = " + resim.Cio_normalized(edge , r1 , r2) );
            System.out.println(); */
            
           /* for(URI edge2: edges){
                System.out.println(edge + " " + edge2);
                System.out.println("Cii(li , lj , a , b) = " + resim_o.Cii(edge , edge2 , r1 , r2) );
                System.out.println("Cii(li , lj , a , b) = " + resim.Cii(edge , edge2 , r1 , r2) );
                System.out.println();
                System.out.println("Cio(li , lj , a , b) = " + resim_o.Cio(edge , edge2 , r1 , r2) );
                System.out.println("Cio(li , lj , a , b) = " + resim.Cio(edge , edge2 , r1 , r2) );
                System.out.println();
                System.out.println("Cii(li , lj , a) = " + resim_o.Cii(edge , edge2 , r1) );
                System.out.println("Cii(li , lj , a) = " + resim.Cii(edge , edge2 , r1) );
                System.out.println();
                System.out.println("Cio(li , lj , a) = " + resim_o.Cio(edge , edge2 , r1) );
                System.out.println("Cio(li , lj , a) = " + resim.Cio(edge , edge2 , r1) );
                System.out.println();
                System.out.println("Cii(li , lj , b) = " + resim_o.Cii(edge , edge2 , r2) );
                System.out.println("Cii(li , lj , b) = " + resim.Cii(edge , edge2 , r2) );
                System.out.println();
                System.out.println("Cio(li , lj , b) = " + resim_o.Cio(edge , edge2 , r2) );
                System.out.println("Cio(li , lj , b) = " + resim.Cio(edge , edge2 , r2) );
                System.out.println();
                System.out.println("Cii_normalized(li , lj , a , b) = " + resim_o.Cii_normalized(edge , edge2 , r1 , r2) );
                System.out.println("Cii_normalized(li , lj , a , b) = " + resim.Cii_normalized(edge , edge2 , r1 , r2) );
                System.out.println();
                System.out.println("Cio_normalized(li , lj , a , b) = " + resim_o.Cio_normalized(edge , edge2 , r1 , r2) );
                System.out.println("Cio_normalized(li , lj , a , b) = " + resim.Cio_normalized(edge , edge2 , r1 , r2) );
                System.out.println();
                System.out.println();
            }
            
        }*/
        
        resim.closeIndexes();
        resim_o.closeIndexes();
    }
}
