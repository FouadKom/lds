/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.benchmark;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import static org.junit.Assert.fail;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;
import test.utility.Util;

/**
 *
 * @author Fouad Komeiha
 */
public class writingResults_Test {
    public static final String dataSetDir = System.getProperty("user.dir") + "/src/test/resources/data.rdf";
//    public static final String resourcesFileCsv = System.getProperty("user.dir") + "/src/test/resources/Test.txt";
    public static final String resourcesFileCsv = System.getProperty("user.dir") + "/src/test/resources/missing_resources_2.csv";
    
    @Test
    public void test() throws Exception{
//    public static void main(String args[]) throws Exception{

        LdDataset dataSet = Util.getDBpediaDataset();
        
        LdSimilarityEngine engine = new LdSimilarityEngine();
        
        Conf config = new Conf();
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , dataSet);

        engine.load(Measure.Resim, config);
        
        engine.similarity(resourcesFileCsv , false , false);
        
        engine.close();
    }
}
