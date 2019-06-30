/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.ldsd;

import lds.resource.R;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class LDSD_d extends LDSD{

    public LDSD_d(Conf config) throws Exception {
        super(config);
    }

    @Override
    public double compare(R a, R b) {
       return LDSD_d(a , b);
    }
    
    
    public double LDSD_d(R a, R b) {
            double d = 1 + Cd(a, b) + Cd(b, a);
            return 1 / d;
    }
    
    
    
}
