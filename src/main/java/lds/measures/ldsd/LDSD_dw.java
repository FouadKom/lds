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
public class LDSD_dw extends LDSD{

    public LDSD_dw(Config config) throws Exception {
        super(config);
    }

    @Override
    public double compare(R a, R b) {
       try{
           edges = LDSDLDLoader.getEdges(a, b);
           if(edges == null)
                return 0;
           return LDSD_dw_sim(a, b);
       } catch (Exception ex) {
                Logger.getLogger(LDSD_dw.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
       }
    }
    
    public double LDSD_dw(R a, R b) {
            double cdA_norm = 0, cdB_norm = 0;

            for (URI l : edges) {

                    cdA_norm = cdA_norm + Cd_normalized(l, a, b);
                    cdB_norm = cdB_norm + Cd_normalized(l, b, a);

            }      	

            return 1 / (1 + cdA_norm + cdB_norm );
    }
    
    public double LDSD_dw_sim(R a, R b) {
        return 1 - LDSD_dw(a , b);
    }
    
}
