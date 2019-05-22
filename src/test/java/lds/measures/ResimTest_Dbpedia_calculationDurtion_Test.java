/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures;

import java.io.File;
import org.junit.Test;

/**
 *
 * @author LENOVO
 */
public class ResimTest_Dbpedia_calculationDurtion_Test {
    
    @Test
    public void isResimWorksCorrectlyOnPaperExample() {
                
          int startNumber = 100;
          int endNumber = 1000;
          int incrementBy = 100;
          
          while(startNumber <= endNumber){
              Util.calculationDuration_Test(startNumber);
              startNumber = startNumber + incrementBy;
              System.out.println("---------------------------------------");
          }
    
    }
}
