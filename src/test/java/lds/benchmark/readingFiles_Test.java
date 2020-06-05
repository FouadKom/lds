/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.benchmark;

import java.io.IOException;
import java.util.List;
import lds.resource.LdResourceTriple;
import org.junit.Test;

/**
 *
 * @author Fouad Komeiha
 */
public class readingFiles_Test {
    public static final String resourcesFileCsv = System.getProperty("user.dir") + "/src/test/resources/Test_Results.csv";
    public static final String resourcesFileText = System.getProperty("user.dir") + "/src/test/resources/Test.txt";
    
    
//    @Test
//    public void test() throws Exception{
    public static void main(String args[]) throws IOException {
        
          BenchmarkFile source = new BenchmarkFile(resourcesFileText , ',' , '"');
          
          LdBenchmark benchmark = new LdBenchmark(source);
          
          List<LdResourceTriple> sourceTriples = benchmark.readFromFile(true);
          
          System.out.println("Source Triples");
          int i = 0;
          for(LdResourceTriple triple : sourceTriples){
                i++;
                System.out.println(i + "- " + triple.toString( source.getSeparator() ) );
          }
          
          System.out.println();
    }
    
}
