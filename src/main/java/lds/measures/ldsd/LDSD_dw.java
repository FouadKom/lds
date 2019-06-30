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
public class LDSD_dw extends LDSD{

    public LDSD_dw(Conf config) throws Exception {
        super(config);
    }

    @Override
    public double compare(R a, R b) {
        return LDSD_dw(a, b);
    }
    
    public double LDSD_dw(R a, R b) {
            double cdA_norm = 0, cdB_norm = 0;

            for (URI l : edges) {

                    cdA_norm = cdA_norm + Cd_normalized(l, a, b);
                    cdB_norm = cdB_norm + Cd_normalized(l, b, a);

            }      	

            return 1 / (1 + cdA_norm + cdB_norm );
    }
    
    /**
	 * Nbr of nodes from a ---- l ---> count(x)
	 * 
	 * @param a
	 * @param l
	 * @param g
	 * @return
	 */
        
        //similar to Resim
	public int Cd(URI l, R a) {
            return LDSDLDLoader.countObject(l , a);
        }

	/**
	 * returns 1 if a ----- count(l) ----> b
	 * 
	 * @param a
	 * @param b
	 * @param l
	 * @param g
	 * @return
	 */
        
        //similar to Resim
	public int Cd(URI l, R a, R b) {
            if(LDSDLDLoader.isDirectlyConnected(l, a, b))
                return 1;
            else
                return 0;
        }
        
        
        //similar to Resim
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

		return cd_norm;
	}
    
}
