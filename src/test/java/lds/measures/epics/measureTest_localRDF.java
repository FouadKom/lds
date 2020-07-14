/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;

import lds.LdManager.EpicsLdManager;
import lds.measures.picss.PICSS;
import static lds.measures.resim.ResimTest_localRdf.dataSetDir;
import lds.resource.R;
import static org.junit.Assert.fail;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class measureTest_localRDF {
    
    @Test
    public void runEngineOnSpecificLdMeasureTest() throws Exception{  
//    public static void main(String args[]) throws Exception{
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
        config.addParam("resourcesCount" , 9); 
        
        PICSS picss = new PICSS(config);
        EPICS_v1 epics = new EPICS_v1(config);
        
        picss.loadIndexes();
        epics.loadIndexes();
        
        System.out.println("PICSS(r1 , r2) = " + picss.compare(r1, r2));
        System.out.println("EPICS(r1 , r2) = " + epics.compare(r1, r2));
        
        picss.closeIndexes();
        epics.closeIndexes();
        
        
        
    }
    
}
