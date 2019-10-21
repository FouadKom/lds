/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;

import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import static lds.measures.resim.ResimTest_localRdf.dataSetDir;
import lds.resource.R;
import static org.junit.Assert.fail;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class test_localRDF {
    
    public static void main(String args[]){
        LdDataset dataSet = null;

        R r1 = new R("http://www.example.org#Fish");
        R r2 = new R("http://www.example.org#Whale");

        try {
                dataSet = LdDatasetFactory.getInstance().name("example").file(dataSetDir)
                                .defaultGraph("http://graphResim/dataset").create();

        } catch (Exception e) {
                fail(e.getMessage());
        }
            
        Conf config = new Conf();
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , dataSet);
        
        
        
        LdSimilarityEngine engine = new LdSimilarityEngine();
        
        ///////////// LDSD_d ////////////////////////////
        engine.load(Measure.LDSD_d, config);
        
        System.out.println("LDSD_d(Fish , Whale) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// LDSD_dw ////////////////////////////
        engine.load(Measure.LDSD_dw, config);
        
        System.out.println("LDSD_dw(Fish , Whale) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// LDSD_i ////////////////////////////
        engine.load(Measure.LDSD_i, config);
        
        System.out.println("LDSD_i(Fish , Whale) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// LDSD_iw ////////////////////////////
        engine.load(Measure.LDSD_iw, config);
        
        System.out.println("LDSD_iw(Fish , Whale) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// LDSD_cw ////////////////////////////
        engine.load(Measure.LDSD_cw, config);
        
        System.out.println("LDSD_cw(Fish , Whale) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// TLDSD_cw ////////////////////////////
        engine.load(Measure.TLDSD_cw, config);
        
        System.out.println("TLDSD_cw(Fish , Whale) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// Resim ////////////////////////////
        engine.load(Measure.Resim, config);
        
        System.out.println("Resim(Fish , Whale) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// TResim ////////////////////////////
        engine.load(Measure.TResim, config);
        
        System.out.println("TResim(Fish , Whale) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// PICSS ////////////////////////////
        config.addParam("resourcesCount" , 9);        
        engine.load(Measure.PICSS, config);
        
        System.out.println("PICSS(Fish , Whale) = " + engine.similarity(r1, r2));
        
        engine.close();
        
    }
    
}
