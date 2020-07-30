/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.ldsd;

import lds.config.Config;
import lds.config.ConfigParam;
import lds.dataset.LdDatasetCreator;
import lds.engine.LdSimilarityEngine;
import lds.config.LdConfigFactory;
import lds.measures.Measure;
import lds.resource.R;
import org.junit.Test;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class LDSDTest {
    public static final String datasetDir = System.getProperty("user.dir") + "/src/test/resources/specific_class_set.rdf"; 
    
    @Test
    public void LDSDTest() throws Exception{  
        
        LdDataset dataset = LdDatasetCreator.getDBpediaDataset();
        LdDataset dataSetSpecific = LdDatasetCreator.getLocalDataset(datasetDir, "LDSD_example");              
        
        R r1 = new R("http://dbpedia.org/resource/The_Noah");
        R r2 = new R("http://dbpedia.org/resource/The_Pack_(2010_film)");
        
        //Initialzie the engine class object
        LdSimilarityEngine engine = new LdSimilarityEngine();
        
        /*Intiialize the conf object which contains the necessary parameters for the measure
        you can use the default conf as follows. This creattes a conf with default parameters and no indexing by default*/
        Config config = LdConfigFactory.createDeafaultConf(Measure.LDSD_cw);

        //creates a new similarity class object and passes the config that contains necessary parameters to it, also loads needed indexes if necessary
        //LDSD similarity calculation
        engine.load(Measure.LDSD_d , config);
        System.out.println( engine.similarity(r1 , r2) );
        //ends calculation for the chosen similarity and closes all indexes if created
        engine.close();
        
        engine.load(Measure.LDSD_dw , config);
        System.out.println( engine.similarity(r1 , r2) );
        //ends calculation for the chosen similarity and closes all indexes if created
        engine.close();
        
        engine.load(Measure.LDSD_i , config);
        System.out.println( engine.similarity(r1 , r2) );
        //ends calculation for the chosen similarity and closes all indexes if created
        engine.close();
        
        engine.load(Measure.LDSD_iw , config);
        System.out.println( engine.similarity(r1 , r2) );
        //ends calculation for the chosen similarity and closes all indexes if created
        engine.close();
        
        engine.load(Measure.LDSD_cw , config);
        System.out.println( engine.similarity(r1 , r2) );
        //ends calculation for the chosen similarity and closes all indexes if created
        engine.close();
        
        config = LdConfigFactory.createDeafaultConf(Measure.TLDSD_cw);
        
        engine.load(Measure.TLDSD_cw , config);
        System.out.println( engine.similarity(r1 , r2) );
        engine.close();
        
        config = LdConfigFactory.createDeafaultConf(Measure.WLDSD_cw);       
        
        /*Note:
        Using the default conf for measures that use weighting algorithims such as : WLDSD_cw, WTLDSD_cw 
        requires adding the specific dataset object which is used for weight calculation.
        To add the specific dataset use the following:
        */ 
        config.addParam(ConfigParam.LdDatasetSpecific , dataSetSpecific);        
        
        /*Note:
        Using the default conf for measures that use weighting algorithims such as : WLDSD_cw, WTLDSD_cw  uses ITW algorithim by default.
        To change the weighting algorithim use the following:
        
        config.addParam("WeightMethod" , WeightMethod.RSLAW);        
        */ 
        
        engine.load(Measure.WLDSD_cw , config);
        System.out.println( engine.similarity(r1 , r2) );
        engine.close();
        

        engine.load(Measure.WTLDSD_cw , config);
        System.out.println( engine.similarity(r1, r2) );
        engine.close();

        
    }
    
}
