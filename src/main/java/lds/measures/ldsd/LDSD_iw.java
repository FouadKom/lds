/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.ldsd;

import lds.resource.R;
import org.openrdf.model.URI;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class LDSD_iw extends LDSD{

    public LDSD_iw(Conf config) throws Exception {
        super(config);
    }

    @Override
    public double compare(R a, R b) {
        edges = LDSDLDLoader.getEdges(a, b);
        return LDSD_iw(a , b);
    }
    
    public double LDSD_iw(R a, R b) {
        double cii_norm = 0, cio_norm = 0;

        for (URI l : edges) {

            cii_norm = cii_norm + Cii_normalized(l, a , b);
            cio_norm = cio_norm + Cio_normalized(l, a , b);

        }      	

        return 1 / (1 + cii_norm + cio_norm);
    }
    
}
