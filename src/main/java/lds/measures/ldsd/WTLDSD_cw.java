/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.ldsd;

import lds.config.Config;
import lds.config.ConfigParam;
import lds.resource.R;
import org.openrdf.model.URI;

/**
 *
 * @author Fouad Komeiha
 */
public class WTLDSD_cw extends LDSD_cw{
    
    public WTLDSD_cw(Config config) throws Exception {
        super(config);
        if( config.getParam(ConfigParam.LdDatasetSpecific)== null && config.getParam(ConfigParam.WeightMethod) == null)
            throw new Exception("Some configuration parameters missing"); 
    }
    
    @Override
    public double LDSD_cw(R a, R b) {
        double tcdA_norm = 0, tcdB_norm = 0, tcii_norm = 0, tcio_norm = 0;
        
        for (URI li : edges) {
            tcdA_norm = tcdA_norm + Cd_normalized(li , a, b);
            tcdB_norm = tcdB_norm + Cd_normalized(li , b, a);

            for (URI lj : edges) {
                tcii_norm = tcii_norm + Cii_normalized(li , lj , a , b);
                tcio_norm = tcio_norm + Cio_normalized(li , lj , a , b);

            }
        }
               
        return 1 / (1 + tcdA_norm + tcdB_norm + tcii_norm + tcio_norm);
    }
    
    @Override
     public double Cd_normalized(URI l, R a, R b) {
            int cd = Cd(l, a, b) , cd_l = 0;
            double cd_norm = 0;

            if(cd != 0){
                cd_l = Cd(l, a);
                if(cd_l != 0){
                    double x = 1 + Math.log10(cd_l);
                    cd_norm = (double) cd / x;
                }
                else
                  cd_norm = (double) cd;  
            }

            return cd_norm * this.weight.linkWeight(l, a, b);
    }
     
     public int Cii(URI li , URI lj , R a, R b) {
        return LDSDLDLoader.countTyplessCommonSubjects(li , lj , a , b);
          
    }
    
    
    public double Cii_normalized(URI li , URI lj , R a, R b) {
        int wtcii;
        double x, wtcii_norm = 0 ,weight_li , weight_lj;

        wtcii = Cii(li , lj , a, b);
        
        weight_li = weight.linkWeight(li , a , b);
        weight_lj = weight.linkWeight(lj , a , b);
        
        if(wtcii == 0 || weight_li == 0 || weight_lj == 0){
            return 0;
        }
        else 
        {

           x = 1 + Math.log10(wtcii);
           wtcii_norm = ((double) wtcii / x);
        }
          
        return wtcii_norm;

    }
    
    public int Cio(URI li , URI lj , R a, R b) {
        return LDSDLDLoader.countTyplessCommonObjects(li , lj , a , b);
    }
    
    public double Cio_normalized(URI li , URI lj , R a, R b) {
        int wtcio;
        double wtcio_norm = 0, x ,weight_li , weight_lj;

        wtcio = Cio(li , lj , a, b);
        
        weight_li = weight.linkWeight(li , a , b);
        weight_lj = weight.linkWeight(lj , a , b);

        if(wtcio == 0 || weight_li == 0 || weight_lj == 0){
            return 0;
        }
        else 
        {

            x = 1 + Math.log10(wtcio);
            wtcio_norm = ((double) wtcio / x);

        }
        return wtcio_norm;
    }
    
}
