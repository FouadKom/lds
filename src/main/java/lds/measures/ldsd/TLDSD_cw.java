/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.ldsd;

import lds.config.Config;
import lds.resource.R;
import org.openrdf.model.URI;

/**
 *
 * @author Fouad Komeiha
 */
public class TLDSD_cw extends LDSD_cw{
    
    public TLDSD_cw(Config config) throws Exception {
        super(config);
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
    
    
    public int Cii(URI li , URI lj , R a, R b) {
        return LDSDLDLoader.countTyplessCommonSubjects(li , lj , a , b);
          
    }
    
    
    public double Cii_normalized(URI li , URI lj , R a, R b) {
        int tcii;
        double x, tcii_norm = 0;

        tcii = Cii(li , lj , a, b);

        if(tcii != 0){
            x = 1 + Math.log10(tcii);
            tcii_norm = ((double) tcii / x);
        }   
                
        return tcii_norm;

    }
    
    public int Cio(URI li , URI lj , R a, R b) {
        return LDSDLDLoader.countTyplessCommonObjects(li , lj , a , b);
    }
    
    public double Cio_normalized(URI li , URI lj , R a, R b) {
        int tcio;
        double tcio_norm = 0, x;

        tcio = Cio(li , lj , a, b);

        if(tcio != 0)
        {
           x = 1 + Math.log10(tcio);
           tcio_norm = ((double) tcio / x);
        }      

        return tcio_norm;

    }

    
}
