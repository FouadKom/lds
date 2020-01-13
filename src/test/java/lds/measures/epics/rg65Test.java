/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;

import java.io.FileWriter;
import java.util.List;
import lds.benchmark.LdBenchmark;
import lds.measures.picss.PICSS;
import lds.resource.LdResourcePair;
import lds.resource.LdResourceTriple;
import lds.resource.R;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;
import test.utility.Util;

/**
 *
 * @author Fouad Komeiha
 */
public class rg65Test {
    
static String datsetpath = System.getProperty("user.dir") + "/src/test/resources/benchmarks/rg-65.txt";
    
    @Test
    public void rg65Test() throws Exception{  
        String outputFilePath = System.getProperty("user.dir") + "/src/test/resources/rg65/rg_DBpedia.csv"; 
        if(! lds.benchmark.Utility.checkPath(outputFilePath) )
            return;
        
        
        LdDataset dataset = Util.getDBpediaDataset();
//        LdDataset dataset = Util.getDBpediaHDTDataset(dataSetDir);
        
        double startTime , endTime , duration;
        
        Conf config = new Conf();
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , dataset);
        config.addParam("resourcesCount" , 2350906); 
        
        
        PICSS picss = new PICSS(config);
        
        picss.loadIndexes();
        
               
        EPICS epics = null;
        String epicsVal = null;
        
        FileWriter results_writer = new FileWriter(outputFilePath , true);
        
        results_writer.write("Pair | Benchmark | EPICS_LDSD_d | Duration | EPICS_LDSD_dw | Duration | EPICS_LDSD_i | Duration | EPICS_LDSD_iw | Duration | EPICS_LDSD_cw | Duration | PICSS | Duration");
        results_writer.write(System.getProperty("line.separator")); 
        
        results_writer.close();
        
        List<LdResourceTriple> triples= LdBenchmark.readRowsFromBenchmarks(datsetpath , 0.0 , 4.0);
        
        for(LdResourceTriple triple: triples ){
            
                
            String row = new String();
            
            LdResourcePair pair= triple.getResourcePair();
            R r1 = pair.getFirstresource();
            R r2 = pair.getSecondresource();
            
            String benchMark = Double.toString(triple.getSimilarityResult());
            
            row = row + pair.toString() + " | " + benchMark + " | ";
            
            
            /////////////////////////////////EPICS_LDSD_d/////////////////////////////////////////////////////
            try{
                
            config.addParam("extendingMeasure", "LDSD_d");
        
            epics = new EPICS(config); 
            
            startTime = System.nanoTime();
            
            epicsVal = Double.toString(epics.compare(r1, r2));
        
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("EPICS with LDSD_d finished in " + duration + " second(s) ");
            System.out.println(); 
            
            row = row + epicsVal +  " | " + duration + " | ";
            
            }
            
            catch(Exception ex){
                System.out.println(ex.toString());
                row = row + "Error | Error | ";
            }
            
            ////////////////////////////////EPICS_LDSD_dw//////////////////////////////////////////////////////
            try{
            config.addParam("extendingMeasure", "LDSD_dw");
        
            epics = new EPICS(config); 
            
            startTime = System.nanoTime();
            
            epicsVal = Double.toString(epics.compare(r1, r2));
        
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("EPICS with LDSD_dw finished in " + duration + " second(s) ");
            System.out.println(); 
             
            row = row + epicsVal +  " | " + duration + " | ";
            
            }
            
            catch(Exception ex){
                System.out.println(ex.toString());
                row = row + "Error | Error | ";
            }
            
            ////////////////////////////////EPICS_LDSD_i//////////////////////////////////////////////////////
            try{
                
            config.addParam("extendingMeasure", "LDSD_i");
        
            epics = new EPICS(config);
            
            startTime = System.nanoTime();
            
            epicsVal = Double.toString(epics.compare(r1, r2));
        
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("EPICS with LDSD_i finished in " + duration + " second(s) ");
            System.out.println();
            
            row = row + epicsVal +  " | " + duration + " | ";
            
            }
            
            catch(Exception ex){
                System.out.println(ex.toString());
                row = row + "Error | Error | ";
            }
            
            ///////////////////////////////EPICS_LDSD_iw////////////////////////////////////////////////////////
            try{
                
            config.addParam("extendingMeasure", "LDSD_iw");
        
            epics = new EPICS(config);
            
            startTime = System.nanoTime();
            
            epicsVal = Double.toString(epics.compare(r1, r2));
        
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("EPICS with LDSD_iw finished in " + duration + " second(s) ");
            System.out.println();
            
            row = row + epicsVal +  " | " + duration + " | ";
            
            }
            
            catch(Exception ex){
                System.out.println(ex.toString());
                row = row + "Error | Error | ";
            }            
            
            ///////////////////////////////EPICS_LDSD_cw///////////////////////////////////////////////////////
            try{
                
            config.addParam("extendingMeasure", "LDSD_cw");
        
            epics = new EPICS(config);
            
            startTime = System.nanoTime();
            
            epicsVal = Double.toString(epics.compare(r1, r2));
        
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("EPICS with LDSD_cw finished in " + duration + " second(s) ");
            System.out.println(); 
            
            row = row + epicsVal +  " | " + duration + " | ";
            
            }
            
            catch(Exception ex){
                System.out.println(ex.toString());
                row = row + "Error | Error | ";
            }
            
            ///////////////////////////////PICSS////////////////////////////////////////////////////////
            try{
                
            startTime = System.nanoTime();
            
            String picssVal = Double.toString(picss.compare(r1, r2));
            
            //end timing
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000000 ;
            System.out.println("PICSS finished in " + duration + " second(s) ");
            System.out.println(); 
            
            row = row + picssVal +  " | " + duration;
            
            }
            
            catch(Exception ex){
                System.out.println(ex.toString());
                row = row + "Error | Error | ";
            }
            
            //Write Results to the final file///////////////////////////////////////////////////////////
            results_writer = new FileWriter(outputFilePath , true);
            results_writer.write(row);
            results_writer.write(System.getProperty("line.separator"));            
            results_writer.close();

        
        }
        
        picss.closeIndexes();
    
    }
    
    
}

