/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.engine;

import java.io.FileNotFoundException;
import java.io.IOException;
import lds.measures.Measure;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;
/**
 *
 * @author Fouad Komeiha
 */
public class Engine_BenchMark_Test {
    
    public static void main(String args[]) throws FileNotFoundException, IOException{
        
        
        LdDataset dataSetMain = Util.getDBpediaDataset();
            
        Conf config = new Conf();
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , dataSetMain);
        
        LdSimilarityEngine engine = new LdSimilarityEngine();

        engine.load(Measure.LDSD_cw , config);
        
        engine.similarity("C:\\Users\\LENOVO\\Downloads\\test.txt" , "C:\\Users\\LENOVO\\Downloads\\test_results.txt");
        
        engine.close();
        
        
        
    }
    
}
