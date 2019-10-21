/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;


import java.util.List;
import java.util.Set;
import lds.LdManager.LdManager;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import lds.measures.picss.Util;
import lds.measures.resim.Resim;
import lds.measures.resim.ResourceSimilarity;
import lds.measures.resim.TResim;
import lds.measures.resim.WResim;
import lds.measures.resim.WTResim;
import lds.measures.weight.Weight;
import lds.measures.weight.WeightMethod;
import lds.resource.LdResourceFactory;
import static org.junit.Assert.fail;
import lds.resource.R;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class ResourceSimilarity_EngineTest {
    
    public static String nameSpace = "http://dbpedia.org/resource/";
    public static final String dataSetDir = System.getProperty("user.dir") + "/src/test/resources/data.rdf";
    public static final String datasetDir2 = System.getProperty("user.dir") + "/src/test/resources/specific_class_set.rdf"; 
    
    @Test
    public void isLDSDWorksCorrectlyOnPaperExample() throws Exception{ 
//    public static void main(String args[]) throws Exception {
        
       
//        ResimLdManager resimLdManagerMain;
//        ResimLdManager resimLdManagerSpecific;
//        
//        Weight weight;
        
        R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("The_Noah").create();
        R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("The_Pack_(2010_film)").create();
        
        
        Conf configSim = new Conf();
//        Conf configLdManager = new Conf();
//        Conf configLdManagerSpecific = new Conf();
//        Conf configWeight = new Conf();
//        
        LdDataset datasetMain = Util.getDBpediaDataset();
        
        LdDataset datasetSpecific  = LdDatasetFactory.getInstance()
                                                     .name("specificSet")
                                                     .file(datasetDir2)
                                                     .defaultGraph("http://specificSet/dataset")
                                                     .create();
//        
//        configLdManager.addParam("useIndexes" , true); 
//        configLdManager.addParam("LdDataset" , datasetMain);               
//        resimLdManagerMain = new ResimLdManager(configLdManager);
//        resimLdManagerMain = new ResimLdManager(datasetMain , true);
//        
//        configLdManagerSpecific.addParam("useIndexes" , true);
//        configLdManagerSpecific.addParam("LdDataset" , datasetSpecific);        
//        resimLdManagerSpecific = new ResimLdManager(configLdManagerSpecific);
//        resimLdManagerSpecific = new ResimLdManager(datasetSpecific , true);
//        
//        configWeight.addParam("useIndexes" , true);
//        configWeight.addParam("LdManagerMain" , resimLdManagerMain);
//        configWeight.addParam("LdManagerSpecific" , resimLdManagerSpecific);
//        configWeight.addParam("WeightMethod" , WeightMethod.RSLAW);
//        weight = new Weight(configWeight);
//        weight = new Weight(WeightMethod.RSLAW , resimLdManagerMain , resimLdManagerSpecific , true);
//
//        
//        configSim.addParam("LdManager" , resimLdManagerMain);
//        configSim.addParam("Weight" , weight); 


        
        
//        ResourceSimilarity resim = new Resim(configSim);
//        ResourceSimilarity tresim = new TResim(configSim);
//        ResourceSimilarity wresim = new WResim(configSim);
//        ResourceSimilarity wtresim = new WTResim(configSim);
        
        /*double cii = 0, cio = 0, cii_r1 = 0, cio_r1 = 0, cii_r2 = 0, cio_r2 = 0, cii_norm = 0, cio_norm = 0 , cd_r1 = 0 , cd_norm_r1 = 0 ,cd_r2 = 0 , cd_norm_r2 = 0;
        
                
        Set<URI> edges = resimLdManagerMain.getEdges(r1, r2); 
        for (URI edge : edges) {
            
            cd_r1 = cd_r1 + wtresim.Cd(edge , r1 , r2);
            System.out.println("Cd(" + edge + ", " + r1.getUri().toString() + ", " + r2.getUri().toString() + "): " + wtresim.Cd(edge, r1, r2) );

            cd_norm_r1 = cd_norm_r1 + wtresim.Cd_normalized(edge , r1 , r2);
            System.out.println("Cd(" + edge + ", " + r1.getUri().toString() + ", " + r2.getUri().toString() + "): " + wtresim.Cd(edge, r1, r2) );

            cd_r2 = cd_r2 + wtresim.Cd(edge , r2 , r1);
            System.out.println("Cd(" + edge + ", " + r2.getUri().toString() + ", " + r1.getUri().toString() + "): " + wtresim.Cd(edge, r2, r1) );

            cd_norm_r2 = cd_norm_r2 + wtresim.Cd_normalized(edge , r1 , r2);
                System.out.println("Cd(" + edge + ", " + r2.getUri().toString() + ", " + r1.getUri().toString() + "): " + wtresim.Cd_normalized(edge, r2, r1) );
            
            for(URI secondedge : edges){    
                
                cii = cii + wtresim.Cii(edge, secondedge , r1, r2);
                System.out.println("Cii(" + edge + ", " + secondedge + ", " + r1.getUri().toString() + ", " + r2.getUri().toString() + "): " + wtresim.Cii(edge, secondedge , r1, r2) );

                cio = cio + wtresim.Cio(edge, secondedge , r1, r2);
                System.out.println("Cio(" + edge + ", " + secondedge + ", " + r1.getUri().toString() + ", " + r2.getUri().toString() + "): " + wtresim.Cio(edge, secondedge , r1, r2) );

                cio_norm = cio_norm + wtresim.Cio_normalized(edge, secondedge , r1, r2);
                System.out.println("Cio_norm(" + edge + ", " + secondedge + ", " + r1.getUri().toString() + ", " + r2.getUri().toString() + "): " + wtresim.Cio_normalized(edge, secondedge , r1, r2) );

                cii_norm = cii_norm + wtresim.Cii_normalized(edge, secondedge , r1, r2);
                System.out.println("Cii_norm(" + edge + ", " + secondedge + ", " + r1.getUri().toString() + ", " + r2.getUri().toString() + "): " + wtresim.Cii_normalized(edge, secondedge , r1, r2) );

                System.out.println();
            }

        }
        System.out.println(wtresim.compare(r1, r2));     

        System.out.println(wtresim.LDSD(r1, r2));
        
        System.out.println(wtresim.PropertySim(r1, r2));
        
        
        Set<URI> edges = resimLdManagerMain.getEdges(r1, r2);
        Set<URI> edgesSpecific = resimLdManagerSpecific.getEdges(r1, r2);
        
        for(URI edge : edges){
            System.out.println(edge.toString());
        }
        System.out.println();
        for(URI edge : edgesSpecific){
            System.out.println(edge.toString());
        }*/
        
        configSim.addParam("LdDatasetMain" , datasetMain);        
        configSim.addParam("useIndexes" , true);
 
        
        
        double resim_val = 0 , tresim_val = 0 , wresim_val = 0 , wtresim_val = 0;
        
        LdSimilarityEngine engine = new LdSimilarityEngine();
        
        engine.load(Measure.Resim , configSim);
        
        resim_val = engine.similarity(r1, r2);
        assertEquals(0.359840288900709 , resim_val , 0.0);
        
        engine.close();
        
        engine.load(Measure.TResim , configSim);
        
        tresim_val = engine.similarity(r1, r2);
        assertEquals(0.41353355588185264 , tresim_val , 0.0); 
        
        engine.close();
        
        configSim.addParam("LdDatasetSpecific" , datasetSpecific);
        configSim.addParam("WeightMethod" , WeightMethod.ITW);
        
        engine.load(Measure.WResim , configSim);
        
        wresim_val = engine.similarity( r1, r2);
        System.out.println(wresim_val);
//        assertEquals(1.5576569118036048E-7 , wresim_val , 0.0);
        
        engine.close();
        
        engine.load(Measure.WTResim , configSim);
        
        wtresim_val = engine.similarity(r1, r2);
        System.out.println(wtresim_val);
//        assertEquals(1.5576569118036048E-7 , wtresim_val , 0.0);

        engine.close();




        }   
   
    }
       

