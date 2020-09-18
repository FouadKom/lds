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

/**
 *
 * @author Fouad Komeiha
 */
public class LDSD_d extends LDSD{

    public LDSD_d(Config config) throws Exception {
        super(config);
        
    }

    @Override
    public double compare(R a, R b) {
      try{
          edges = LDSDLDLoader.getEdges(a, b); 
          if(edges == null)
                return 0;

          return LDSD_d_sim(a , b);
      } catch (Exception ex) {
                Logger.getLogger(LDSD_d.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
      }
    }    
    
    public double LDSD_d(R a, R b) {
            double d = 1 + Cd(a, b) + Cd(b, a);
            return 1 / d;
    }
    
    public double LDSD_d_sim(R a, R b) {
        return 1 - LDSD_d(a , b);
    }
    
    
    
}
