/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.picss;

import lds.resource.LdResourceFactory;
import lds.resource.R;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;
import test.utility.Util;

/**
 *
 * @author Fouad Komeiha
 */
public class PICSSTest_paperEvaluation_Test {
    
    
    @Test
    public void isLDSDWorksCorrectlyOnPaperExample() throws Exception{
//    public static void main(String args[]) throws Exception{
        
        LdDataset dataset = Util.getDBpediaDataset();
                
        Conf config = new Conf();
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , dataset);
        config.addParam("resourcesCount" , 2350906);
        
        R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Bob_Dylan").create();
        R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Ronnie_Hawkins").create();
        
        PICSS picss = new PICSS(config);
        
        picss.loadIndexes();
        
        System.out.println(picss.compare(r1, r2));
        
        picss.closeIndexes();
        
        
    }
    
}
