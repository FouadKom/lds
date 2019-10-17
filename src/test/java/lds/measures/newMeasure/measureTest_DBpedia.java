/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.newMeasure;

import java.util.ArrayList;
import java.util.List;
import lds.measures.picss.PICSS;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class measureTest_DBpedia {
    public static void main(String args[]) throws Exception{
        
        String datasetDir2 = System.getProperty("user.dir") + "/src/test/resources/specific_class_set.rdf"; 
        
        LdDataset dataset = lds.measures.picss.Util.getDBpediaDataset();
        
        LdDataset datasetSpecific  = LdDatasetFactory.getInstance()
                                                     .name("specificSet")
                                                     .file(datasetDir2)
                                                     .defaultGraph("http://specificSet/dataset")
                                                     .create();
        
        R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Four_Weddings_and_a_Funeral").create();
        R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Beauty_and_the_Beast").create();
                
        Conf config = new Conf();
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , dataset);
        config.addParam("resourcesCount" , 2350906); 
        
//        PICSS picss = new PICSS(config);
        EPICS epics = new EPICS(config);
        
//        picss.loadIndexes();
        epics.loadIndexes();
        
//        System.out.println("PICSS(r1 , r2) = " + picss.compare(r1, r2));
        System.out.println("EPICS(r1 , r2) = " + epics.compare(r1, r2));
        
//        picss.closeIndexes();
        epics.closeIndexes();
        
    }
    
}
