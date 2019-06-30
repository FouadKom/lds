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
public class LDSD_i extends LDSD{

    public LDSD_i(Conf config) throws Exception {
        super(config);
    }

    @Override
    public double compare(R a, R b) {
        return LDSD_i(a , b);
    }
    
    public double LDSD_i(R a, R b) {
            double d = 1 + Cio(a, b) + Cii(a, b);
            return 1 / d;
    }
    
}
