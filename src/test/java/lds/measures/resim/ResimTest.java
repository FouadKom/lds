/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;


import lds.config.Config;
import lds.config.ConfigParam;
import lds.config.LdConfigFactory;
import lds.dataset.LdDatasetCreator;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import lds.resource.R;
import org.junit.Test;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class ResimTest {
    
    public static String nameSpace = "http://dbpedia.org/resource/";
    public static final String datasetDir = System.getProperty("user.dir") + "/src/test/resources/specific_class_set.rdf"; 
    
    @Test
    public void ResimTest() throws Exception{ 
           
        R r1 = new R("http://dbpedia.org/resource/The_Noah");
        R r2 = new R("http://dbpedia.org/resource/The_Pack_(2010_film)");
        
        
        LdDataset datasetSpecific = LdDatasetCreator.getLocalDataset(datasetDir, "Resim_example");        
        
        //Initialzie the engine class object
        LdSimilarityEngine engine = new LdSimilarityEngine();
        
        /*Intiialize the conf object which contains the necessary parameters for the measure
        you can use the default conf as follows. This creattes a conf with default parameters and no indexing by default*/
        Config config = LdConfigFactory.createDefaultConf(Measure.Resim);   
        //creates a new similarity class object and passes the config that contains necessary parameters to it, also loads needed indexes if necessary
        //Resim similarity calculaton
        engine.load(Measure.Resim , config);
        
        System.out.println( engine.similarity(r1 , r2) );
        
        //ends calculation for the chosen similaarity and closes all indexes if created
        engine.close();
        
        
        config = LdConfigFactory.createDefaultConf(Measure.TResim);
        //TResim similarity calculation
        engine.load(Measure.TResim , config);
        
        System.out.println( engine.similarity(r1 , r2) ); 
        
        engine.close();      
    
        config = LdConfigFactory.createDefaultConf(Measure.WResim);
        
        /*Note:
        Using the default conf for measures that use weighting algorithims such as : WResm, WTResim 
        requires adding the specific dataset object which is used for weight calculation.
        To add the specific dataset use the following:
        */ 
        config.addParam(ConfigParam.LdDatasetSpecific , datasetSpecific);        
        
        /*Note:
        Using the default conf for measures that use weighting algorithims such as : WResm, WTResim uses ITW algorithim by default.
        To change the weighting algorithim use the following:
        
        config.addParam("WeightMethod" , WeightMethod.RSLAW);        
        */ 
        
        //WResim similarity calculation
        engine.load(Measure.WResim , config);
        
        System.out.println( engine.similarity(r1 , r2) );
        
        engine.close();
        
        //WTResim similarity calculation
        engine.load(Measure.WTResim , config);
        
        System.out.println( engine.similarity(r1 , r2) );

        engine.close();

        }   
   
    }
       

