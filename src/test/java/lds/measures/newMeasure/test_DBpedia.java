/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.newMeasure;

import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import lds.measures.weight.WeightMethod;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class test_DBpedia {
    public static void main(String args[]) throws Exception{
        
        String datasetDir2 = System.getProperty("user.dir") + "/src/test/resources/specific_class_set.rdf"; 
        
        LdDataset dataset = lds.measures.picss.Util.getDBpediaDataset();
        
        LdDataset datasetSpecific  = LdDatasetFactory.getInstance()
                                                     .name("specificSet")
                                                     .file(datasetDir2)
                                                     .defaultGraph("http://specificSet/dataset")
                                                     .create();
                
        Conf config = new Conf();
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , dataset);
        config.addParam("LdDatasetSpecific" , datasetSpecific);
        config.addParam("WeightMethod" , WeightMethod.RSLAW);
        
        
        R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("French_Kiss_(1995_film)").create();
        R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Frequency_(film)").create();
        
        LdSimilarityEngine engine = new LdSimilarityEngine();
        
        ///////////// LDSD_d ////////////////////////////
        engine.load(Measure.LDSD_d, config);
        
        System.out.println("LDSD_d(r1 , r2) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// LDSD_dw ////////////////////////////
        engine.load(Measure.LDSD_dw, config);
        
        System.out.println("LDSD_dw(r1 , r2) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// LDSD_i ////////////////////////////
        engine.load(Measure.LDSD_i, config);
        
        System.out.println("LDSD_i(r1 , r2) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// LDSD_iw ////////////////////////////
        engine.load(Measure.LDSD_iw, config);
        
        System.out.println("LDSD_iw(r1 , r2) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// LDSD_cw ////////////////////////////
        engine.load(Measure.LDSD_cw, config);
        
        System.out.println("LDSD_cw(r1 , r2) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// TLDSD_cw ////////////////////////////
        engine.load(Measure.TLDSD_cw, config);
        
        System.out.println("TLDSD_cw(r1 , r2) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// WLDSD_cw ////////////////////////////
        engine.load(Measure.WLDSD_cw, config);
        
        System.out.println("WLDSD_cw(r1 , r2) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// WTLDSD_cw ////////////////////////////
        engine.load(Measure.WTLDSD_cw, config);
        
        System.out.println("WTLDSD_cw(r1 , r2) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// Resim ////////////////////////////
        engine.load(Measure.Resim, config);
        
        System.out.println("Resim(r1 , r2) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// TResim ////////////////////////////
        engine.load(Measure.TResim, config);
        
        System.out.println("TResim(r1 , r2) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// TResim ////////////////////////////
        engine.load(Measure.WResim, config);
        
        System.out.println("WResim(r1 , r2) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// TResim ////////////////////////////
        engine.load(Measure.WTResim, config);
        
        System.out.println("WTResim(r1 , r2) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        ///////////// PICSS ////////////////////////////
        config.addParam("resourcesCount" , 2350906);       
        engine.load(Measure.PICSS, config);
        
        System.out.println("PICSS(r1 , r2) = " + engine.similarity(r1, r2));
        
        engine.close();
        
        
    }
    
}
