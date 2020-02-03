/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;


import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import lds.measures.weight.WeightMethod;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;
import test.utility.Util;

/**
 *
 * @author Fouad Komeiha
 */
public class ResimTest {
    
    public static String nameSpace = "http://dbpedia.org/resource/";
    public static final String datasetDir = System.getProperty("user.dir") + "/src/test/resources/specific_class_set.rdf"; 
    
    @Test
    public void ResimTest() throws Exception{ 
        
        R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("The_Noah").create();
        R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("The_Pack_(2010_film)").create();
        
        
        Conf configSim = new Conf();
        
        LdDataset datasetMain = Util.getDBpediaDataset();
        
        LdDataset datasetSpecific  = LdDatasetFactory.getInstance()
                                                     .name("specificSet")
                                                     .file(datasetDir)
                                                     .defaultGraph("http://specificSet/dataset")
                                                     .create();
        
        //specifying the main dataset that will be used for querying, in our case DBpedia
        configSim.addParam("LdDatasetMain" , datasetMain); 
        
        //using indexes for calculation, change to false of no data indexing is wanted
        configSim.addParam("useIndexes" , true);
        
        
        //Initialzie the engine class object
        LdSimilarityEngine engine = new LdSimilarityEngine();
        
        
        //creates a new similarity class object and passes the config that contains necessary parameters to it, also loads needed indexes if necessary
        //Resim similarity calculaton
        engine.load(Measure.Resim , configSim);
        
        System.out.println( engine.similarity(r1 , r2) );
        
        //ends calculation for the chosen similaarity and closes all indexes if created
        engine.close();
        
        //TResim similarity calculation
        engine.load(Measure.Resim , configSim);
        
        System.out.println( engine.similarity(r1 , r2) ); 
        
        engine.close();
        
    
        //addidng new parameters to the config whichh are needed for WResim and WTResim measures
        //specifiying the specific dataset that is used to calculate weights for links
        configSim.addParam("LdDatasetSpecific" , datasetSpecific);
        
        //providing thw weighting method that will calculate weights for links 
        configSim.addParam("WeightMethod" , WeightMethod.RSLAW);
        
        
        //WResim similarity calculation
        engine.load(Measure.WResim , configSim);
        
        System.out.println( engine.similarity(r1 , r2) );
        
        engine.close();
        
        //WTResim similarity calculation
        engine.load(Measure.WTResim , configSim);
        
        System.out.println( engine.similarity(r1 , r2) );

        engine.close();

        }   
   
    }
       

