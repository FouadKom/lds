/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;

import java.util.List;
import java.util.Set;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;
import test.utility.Util;

/**
 *
 * @author Fouad Komeiha
 */
public class OptimizationTest {
    
    public static void main(String args[]) throws Exception{
        R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("The_Noah").create();
        R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("The_Pack_(2010_film)").create();
        double startTime , endTime , duration;
        
        LdDataset datasetMain = Util.getDBpediaDataset();
        
        Conf configSim = new Conf();            
        
        configSim.addParam("LdDatasetMain" , datasetMain);
        configSim.addParam("useIndexes" , true);
        
        ResourceSimilarityO resim_o = new ResimO(configSim);
        ResourceSimilarity resim = new Resim(configSim);
        resim_o.loadIndexes();
        resim.loadIndexes();
        
        System.out.println(datasetMain.getPrefixes().getNsPrefixURI("dbpedia"));
        
//        System.out.println("Indexes Loaded, Pausing code");
//        Thread.sleep(4000);
         
        /*startTime = System.nanoTime(); 
        
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
        resim_o.closeIndexes();*/
    }
}
