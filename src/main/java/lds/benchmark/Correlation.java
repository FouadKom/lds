/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.benchmark;
import java.util.List;
import org.apache.commons.math3.stat.correlation.*;

/**
 *
 * @author Fouad Komeiha
 */
public enum Correlation {
    PearsonCorrelation,
    SpearmanCorrelation;
    
    public static double getCorrelation(List<String> xs, List<String> ys , Correlation method){
        double[] xArray = new double[xs.size()];
        double[] yArray = new double[ys.size()];
        
        for(int i = 0; i < xs.size(); ++i) {
              double x = Double.parseDouble(xs.get(i));
              xArray[i] = x;
        }
        
        for(int i = 0; i < ys.size(); ++i) {
              double y = Double.parseDouble(ys.get(i));
              yArray[i] = y;
        }
        
        if(method == PearsonCorrelation){
            PearsonsCorrelation pearsons = new PearsonsCorrelation();
            return pearsons.correlation(xArray, yArray);
        }
        else if(method == SpearmanCorrelation){
            SpearmansCorrelation pearsons = new SpearmansCorrelation();
            return pearsons.correlation(xArray, yArray);
        }
        
        return 0;
    }
    
}
