/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.newMeasure;

import java.util.ArrayList;
import java.util.List;
import static lds.engine.Utility.calculateCorrelation;

/**
 *
 * @author Fouad Komeiha
 */
public class CorrelationCalculation {
    public static void main(String args[]){
        List<String> picss = new ArrayList<>();
        List<String> epics_ldsd_d = new ArrayList<>();
        List<String> epics_ldsd_dw = new ArrayList<>();
        List<String> epics_ldsd_cw = new ArrayList<>();
        List<String> benchMark = new ArrayList<>();
        
//        picss.add("0.0");
//        picss.add("0.0");
//        picss.add("0.0");
//        picss.add("0.0");
//        picss.add("0.0");
//        picss.add("0.0");
//        picss.add("0.0");
//        picss.add("0.0");
//        picss.add("0.03453189914830913");
//        picss.add("0.015684703676084315");
//        picss.add("0.0");

        picss.add("0.00");
        picss.add("0.00");
        picss.add("0.00");
        picss.add("0.00");
        picss.add("0.00");
        picss.add("0.00");
        picss.add("0.00");
        picss.add("0.00");
        picss.add("0.00");
        picss.add("0.02");
        picss.add("0.00");
        
//        epics_ldsd_d.add("0.0");
//        epics_ldsd_d.add("0.0015617275131199191");
//        epics_ldsd_d.add("0.0");
//        epics_ldsd_d.add("0.0");
//        epics_ldsd_d.add("0.0");
//        epics_ldsd_d.add("0.0012423979660674518");
//        epics_ldsd_d.add("0.0");
//        epics_ldsd_d.add("0.0");
//        epics_ldsd_d.add("0.03453189914830913");
//        epics_ldsd_d.add("0.01849599441754029");
//        epics_ldsd_d.add("0.0");

        epics_ldsd_d.add("0.00");
        epics_ldsd_d.add("0.00");
        epics_ldsd_d.add("0.00");
        epics_ldsd_d.add("0.00");
        epics_ldsd_d.add("0.00");
        epics_ldsd_d.add("0.00");
        epics_ldsd_d.add("0.00");
        epics_ldsd_d.add("0.00");
        epics_ldsd_d.add("0.00");
        epics_ldsd_d.add("0.02");
        epics_ldsd_d.add("0.00");
        
        
//        epics_ldsd_dw.add("0.0");
//        epics_ldsd_dw.add("0.0015617275131199191");
//        epics_ldsd_dw.add("0.026742366251361078");
//        epics_ldsd_dw.add("0.03250867928013474");
//        epics_ldsd_dw.add("0.020207810276280016");
//        epics_ldsd_dw.add("0.0012423979660674518");
//        epics_ldsd_dw.add("0.0");
//        epics_ldsd_dw.add("0.0");
//        epics_ldsd_dw.add("0.03453189914830913");
//        epics_ldsd_dw.add("0.015684703676084315");
//        epics_ldsd_dw.add("0.045456506622513916 ");
        
        epics_ldsd_dw.add("0.00");
        epics_ldsd_dw.add("0.00");
        epics_ldsd_dw.add("0.03");
        epics_ldsd_dw.add("0.03");
        epics_ldsd_dw.add("0.02");
        epics_ldsd_dw.add("0.00");
        epics_ldsd_dw.add("0.00");
        epics_ldsd_dw.add("0.00");
        epics_ldsd_dw.add("0.03");
        epics_ldsd_dw.add("0.01");
        epics_ldsd_dw.add("0.04");
        
//        epics_ldsd_cw.add("0.0");
//        epics_ldsd_cw.add("0.07171466305598363");
//        epics_ldsd_cw.add("0.622544653689773");
//        epics_ldsd_cw.add("0.6341092637674883");
//        epics_ldsd_cw.add("0.5439369538367785");
//        epics_ldsd_cw.add("0.08299227199284263");
//        epics_ldsd_cw.add("0.0");
//        epics_ldsd_cw.add("0.0");
//        epics_ldsd_cw.add("0.6045123971617493");
//        epics_ldsd_cw.add("0.6788400756498794");
//        epics_ldsd_cw.add("0.5919108150421246");

        epics_ldsd_cw.add("0.00");
        epics_ldsd_cw.add("0.07");
        epics_ldsd_cw.add("0.62");
        epics_ldsd_cw.add("0.63");
        epics_ldsd_cw.add("0.54");
        epics_ldsd_cw.add("0.08");
        epics_ldsd_cw.add("0.00");
        epics_ldsd_cw.add("0.00");
        epics_ldsd_cw.add("0.60");
        epics_ldsd_cw.add("0.67");
        epics_ldsd_cw.add("0.59");
        
        benchMark.add("0.62");
        benchMark.add("0.16");
        benchMark.add("0.80");
        benchMark.add("0.10");
        benchMark.add("0.46");
        benchMark.add("0.63");
        benchMark.add("0.15");
        benchMark.add("0.29");
        benchMark.add("0.67");
        benchMark.add("0.12");
        benchMark.add("0.43");
        
        System.out.println(calculateCorrelation(picss , benchMark));
        System.out.println(calculateCorrelation(epics_ldsd_d , benchMark));
        System.out.println(calculateCorrelation(epics_ldsd_dw , benchMark));
        System.out.println(calculateCorrelation(epics_ldsd_cw , benchMark));
        
    }
    
}
