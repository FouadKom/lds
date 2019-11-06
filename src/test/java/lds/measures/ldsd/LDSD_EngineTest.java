/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.ldsd;

import lds.LdManager.LdsdLdManager;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import static lds.measures.ldsd.LDSDTest_localRDF.dataSetDir;
import lds.measures.weight.Weight;
import lds.measures.weight.WeightMethod;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import static org.junit.Assert.fail;
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
public class LDSD_EngineTest {
    public static final String dataSetDir = System.getProperty("user.dir") + "/src/test/resources/sparql.rdf";
    
    @Test
    public void runEngineOnSpecificLdMeasureTest() throws Exception{
    
//    public static void main(String args[]) throws Exception{
        
        LdDataset dataSetMain = null;
        LdDataset dataSetSpecific = null;
        
        try {
            dataSetSpecific = LdDatasetFactory.getInstance().name("LDSD_example").file(dataSetDir)
                                .defaultGraph("http://graphLDSD/dataset").create();

        } catch (Exception e) {
                fail(e.getMessage());
        }
        

        dataSetMain = Util.getDBpediaDataset();
                
        Conf config = new Conf();
        config.addParam("useIndexes", true);
        config.addParam("LdDatasetMain" , dataSetMain);
        config.addParam("LdDatasetSpecific" , dataSetSpecific);
        config.addParam("WeightMethod", WeightMethod.ITW);
        
        R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Johnny_Cash").create();
        R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("June_Carter_Cash").create();
        
        LdSimilarityEngine engine = new LdSimilarityEngine();

        
        engine.load(Measure.LDSD_cw , config);
        System.out.println( engine.similarity(r1 , r2) );
        System.out.println( engine.similarity(r1 , r1) );
        System.out.println( engine.similarity(r2 , r2) );
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
