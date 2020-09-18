/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.ldsd;

import java.util.logging.Level;
import java.util.logging.Logger;
import lds.config.Config;
import lds.resource.R;
import org.openrdf.model.URI;

/**
 *
 * @author Fouad Komeiha
 */
public class LDSD_iw extends LDSD{

    public LDSD_iw(Config config) throws Exception {
        super(config);
    }

    @Override
    public double compare(R a, R b) {
       try{
           edges = LDSDLDLoader.getEdges(a, b); 
           if(edges == null)
                return 0;
           return LDSD_iw_sim(a , b);
       } catch (Exception ex) {
                Logger.getLogger(LDSD_iw.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
       }
    }
    
    public double LDSD_iw(R a, R b) {
        double cii_norm = 0, cio_norm = 0;

        for (URI l : edges) {

            cii_norm = cii_norm + Cii_normalized(l, a , b);
            cio_norm = cio_norm + Cio_normalized(l, a , b);

        }      	

        return 1 / (1 + cii_norm + cio_norm);
    }
    
    public double LDSD_iw_sim(R a, R b) {
        return 1 - LDSD_iw(a , b);
    }
    
}
