/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;

import lds.LdManager.EpicsLdManager;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import test.utility.Util;

/**
 *
 * @author Fouad Komeiha
 */
public class EnhancingTest {
    public static void main(String args[]) throws Exception{
        LdDataset datasetMain = Util.getDBpediaDataset();
        
        R r1 = new R("http://dbpedia.org/resource/Paris");        
        
        EpicsLdManager manager =  new EpicsLdManager(datasetMain , false);
        
        manager.loadIndexes();
        
        manager.getFeatures(r1);
        
        manager.closeIndexes();
        
        
    }
}
