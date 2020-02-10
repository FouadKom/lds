/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;

import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
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
    public static final String resourcesFilePath = System.getProperty("user.dir") + "/src/test/resources/OptimTest/facebook_book_resources_Resim.txt";
    
    public static void main(String args[]) throws Exception{
        R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("The_Noah").create();
        R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("The_Pack_(2010_film)").create();
        
        double startTime , endTime , duration;
        
        LdDataset datasetMain = Util.getDBpediaDataset();
        
        Conf configSim = new Conf();            
        
        configSim.addParam("LdDatasetMain" , datasetMain);
        configSim.addParam("useIndexes" , true);
        
        /*ResourceSimilarityO resim_o = new ResimO(configSim);
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
        resim_o.closeIndexes();*/
        
        LdSimilarityEngine engine = new LdSimilarityEngine();

        engine.load(Measure.ResimO  ,configSim);
        
        startTime = System.nanoTime(); 
        
        engine.similarity(resourcesFilePath , false , false);
        
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("ResimO finished in " + duration + " second(s)");
        System.out.println();
        
        engine.close();
        
        engine.load(Measure.Resim  ,configSim);
        
        startTime = System.nanoTime(); 
        
        engine.similarity(resourcesFilePath , false , false);
        
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000000 ;
        System.out.println("Resim finished in " + duration + " second(s)");
        System.out.println();
        
        engine.close();
    }
}
