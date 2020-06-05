/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.ldsd;

import lds.LdManager.ontologies.Ontology;
import lds.resource.R;
import org.openrdf.model.URI;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class LDSD_cw extends LDSD{
    
    public LDSD_cw(Conf config) throws Exception {
        super(config);
    }

    @Override
    public double compare(R a, R b) {
        
        edges = LDSDLDLoader.getEdges(a, b);
        return LDSD_cw_sim(a, b);
    }
    
  
    public double LDSD_cw(R a, R b) {
       double cdA_norm = 0, cdB_norm = 0, cii_norm = 0, cio_norm = 0;

        for (URI l : edges) {

                cdA_norm = cdA_norm + Cd_normalized(l, a, b);
                cdB_norm = cdB_norm + Cd_normalized(l, b, a);
                cii_norm = cii_norm + Cii_normalized(l, a , b);
                cio_norm = cio_norm + Cio_normalized(l, a , b);

        }      	

        return 1 / (1 + cdA_norm + cdB_norm + cii_norm + cio_norm);
    }
    
    public double LDSD_cw_sim(R a, R b) {
        return 1 - LDSD_cw(a , b);
    }
    
}
