/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.engine;

import test.utility.Util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import lds.benchmark.BenchmarkFile;
import lds.measures.Measure;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;
import lds.benchmark.LdBenchmark;
import lds.dataset.LdDatasetCreator;
/**
 *
 * @author Fouad Komeiha
 */
public class Engine_BenchMark_Test {
    
    public static void main(String args[]) throws FileNotFoundException, IOException, InterruptedException, ExecutionException{
        
        
        LdDataset dataSetMain = LdDatasetCreator.getDBpediaDataset();
            
        Conf config = new Conf();
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , dataSetMain);
        
        LdSimilarityEngine engine = new LdSimilarityEngine();

        engine.load(Measure.LDSD_cw , config);
        
        BenchmarkFile sourceFile = new BenchmarkFile("C:\\Users\\LENOVO\\Downloads\\test.txt");
        
        engine.similarity(sourceFile , 1 , true);
        
        engine.close();
       
        
        
        
    }
    
}
