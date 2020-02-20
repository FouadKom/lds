/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lds.measures.resim;

import java.util.List;
import java.util.Set;
import lds.LdManager.ontologies.Ontology;
import lds.resource.R;
import org.junit.Test;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;
import test.utility.Util;

/**
 *
 * @author Fouad Komeiha
 */
public class OptimizationTest {
        
//    @Test
//    public void OptimizationTest() throws Exception{
    
    public static void main(String args[]) throws Exception{
   
        R r1 = new R("http://dbpedia.org/resource/Bob_Dylan");
        R r2 = new R("http://dbpedia.org/resource/Ronnie_Hawkins");
        
        double startTime , endTime , duration;
        
        LdDataset datasetMain = Util.getDBpediaDataset();
        
        Conf configSim = new Conf();            
        
        configSim.addParam("LdDatasetMain" , datasetMain);
        configSim.addParam("useIndexes" , false);
        
        ResourceSimilarityO resim_o = new ResimO(configSim);
        ResourceSimilarity resim = new Resim(configSim);
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
        Ontology.loadIndexes();
        if(edges.size() == edges_o.size())
            System.out.println("Edges have same size");
        
        System.out.println("Cip(a) = " + resim_o.Cip(r1) );
        System.out.println("Cip(a) = " + resim.Cip(r1) );
        System.out.println();
        System.out.println("Cop(a) = " + resim_o.Cop(r1) );
        System.out.println("Cop(a) = " + resim.Cop(r1) );
        System.out.println();
            
        for(URI edge: edges){
            System.out.println("edge " + edge.stringValue());
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
            System.out.println();           
        }
        
        Ontology.closeIndexes();
        resim.closeIndexes();
        resim_o.closeIndexes();*/
    }
}
