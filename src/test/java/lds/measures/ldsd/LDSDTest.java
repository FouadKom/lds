/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.ldsd;

import lds.dataset.LdDatasetCreator;
import lds.engine.LdSimilarityEngine;
import lds.conf.LdConfFactory;
import lds.measures.Measure;
import lds.measures.weight.WeightMethod;
import lds.resource.R;
import static org.junit.Assert.fail;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;
import test.utility.Util;

/**
 *
 * @author Fouad Komeiha
 */
public class LDSDTest {
    public static final String datasetDir = System.getProperty("user.dir") + "/src/test/resources/specific_class_set.rdf"; 
    
    @Test
    public void LDSDTest() throws Exception{
        
        LdDataset dataSetMain = LdDatasetCreator.getDBpediaDataset();
        LdDataset dataSetSpecific = LdDatasetCreator.getLocalDataset(datasetDir, "LDSD_example");
               
        Conf config = LdConfFactory.createDeafaultConf(Measure.LDSD_cw);
        
        R r1 = new R("http://dbpedia.org/resource/The_Noah");
        R r2 = new R("http://dbpedia.org/resource/The_Pack_(2010_film)");
        
        LdSimilarityEngine engine = new LdSimilarityEngine();

        //creates a new similarity class object and passes the config that contains necessary parameters to it, also loads needed indexes if necessary
        //LDSD similarity calculation
        engine.load(Measure.LDSD_cw , config);
        System.out.println( engine.similarity(r1 , r2) );
        //ends calculation for the chosen similaarity and closes all indexes if created
        engine.close();
        
        config = LdConfFactory.createDeafaultConf(Measure.TLDSD_cw);
        
        engine.load(Measure.TLDSD_cw , config);
        System.out.println( engine.similarity(r1 , r2) );
        engine.close();
        
        config = LdConfFactory.createDeafaultConf(Measure.WLDSD_cw);
        config.addParam(datasetDir, dataSetSpecific);
        
        engine.load(Measure.WLDSD_cw , config);
        System.out.println( engine.similarity(r1 , r2) );
        engine.close();
        

        engine.load(Measure.WTLDSD_cw , config);
        System.out.println( engine.similarity(r1, r2) );
        engine.close();

        
    }
    
}
