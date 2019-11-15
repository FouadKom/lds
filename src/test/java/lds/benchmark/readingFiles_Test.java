/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.benchmark;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import lds.benchmark.*;
import lds.resource.LdResourceTriple;

/**
 *
 * @author Fouad Komeiha
 */
public class readingFiles_Test {
    public static final String resourcesFileCsv = System.getProperty("user.dir") + "/src/test/resources/Test.csv";
    public static final String resourcesFileText = System.getProperty("user.dir") + "/src/test/resources/Test.txt";
    
    public static void main(String args[]) throws FileNotFoundException, IOException{
        List<LdResourceTriple> triples = LdBenchmark.readListFromFile(resourcesFileText);
        for(LdResourceTriple triple : triples){
            System.out.println(triple.toString('|') +"\n");
        }
        
        triples = LdBenchmark.readListFromFile(resourcesFileText);
        for(LdResourceTriple triple : triples){
            System.out.println(triple.toString('|') +"\n");
        }

    }
    
}
