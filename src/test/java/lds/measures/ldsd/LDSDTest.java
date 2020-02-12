/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.ldsd;

import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import lds.measures.weight.WeightMethod;
import lds.resource.LdResourceFactory;
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
        
        LdDataset dataSetMain = null;
        LdDataset dataSetSpecific = null;
        
        try {
            dataSetSpecific = LdDatasetFactory.getInstance().name("LDSD_example").file(datasetDir)
                                .defaultGraph("http://graphLDSD/dataset").create();

        } catch (Exception e) {
                fail(e.getMessage());
        }
        

        dataSetMain = Util.getDBpediaDataset();
                
        Conf config = new Conf();
        
        //using indexes for calculation, change to false of no data indexing is wanted
        config.addParam("useIndexes", false);
        
        //specifying the main dataset that will be used for querying, in our case DBpedia
        config.addParam("LdDatasetMain" , dataSetMain);
        
        //specifiying the specific dataset that is used to calculate weights for links
        config.addParam("LdDatasetSpecific" , dataSetSpecific);
        
        //providing thw weighting method that will calculate weights for links 
        config.addParam("WeightMethod", WeightMethod.ITW);
        
        R r1 = new R("http://dbpedia.org/resource/The_Noah");
        R r2 = new R("http://dbpedia.org/resource/The_Pack_(2010_film)");
        
        LdSimilarityEngine engine = new LdSimilarityEngine();

        //creates a new similarity class object and passes the config that contains necessary parameters to it, also loads needed indexes if necessary
        //LDSD similarity calculation
        engine.load(Measure.LDSD_cw , config);
        System.out.println( engine.similarity(r1 , r2) );
        //ends calculation for the chosen similaarity and closes all indexes if created
        engine.close();
        
        
        engine.load(Measure.TLDSD_cw , config);
        System.out.println( engine.similarity(r1 , r2) );
        engine.close();
        

        engine.load(Measure.WLDSD_cw , config);
        System.out.println( engine.similarity(r1 , r2) );
        engine.close();
        

        engine.load(Measure.WTLDSD_cw , config);
        System.out.println( engine.similarity(r1, r2) );
        engine.close();

        
    }
    
}
