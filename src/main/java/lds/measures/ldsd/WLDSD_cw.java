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
public class WLDSD_cw extends LDSD_cw{
    
    public WLDSD_cw(Config config) throws Exception {
        super(config);
        if( config.getParam(ConfigParam.LdDatasetSpecific)== null && config.getParam(ConfigParam.WeightMethod) == null)
            throw new Exception("Some configuration parameters missing"); 
    }
    
    @Override
    public double Cd_normalized(URI l, R a, R b) {
            int cd = Cd(l, a, b) , cd_l = 0;
            double cd_norm = 0 , weight;
            
            weight = this.weight.linkWeight(l , a , b);

            if(cd == 0 || weight == 0.0 ){
                return 0;
            }
            else
            {

                cd_l = Cd(l, a);
                if(cd_l != 0){
                    double x = 1 + Math.log10(cd_l);
                    cd_norm = (double) cd / x;
                }
                else
                  cd_norm = (double) cd;  
            }

            return cd_norm * weight;
    }
     
    
    @Override
    public double Cii_normalized(URI l, R a, R b) {

            int ciiA,  wcii;
            double x, wcii_norm = 0 , weight;

            wcii = Cii(l, a, b);
            
            weight = this.weight.linkWeight(l , a , b);

            wcii = Cii(l, a, b);

            if(wcii == 0 || weight == 0){
                return 0;
            }
            else {


                ciiA = Cii(l, a);

                    if( ciiA != 0){
                        x = 1 + Math.log10( ciiA );
                        wcii_norm = ((double) wcii ) / x;
                    }
                    else
                        wcii_norm = (double) wcii;
                }

            return wcii_norm * weight;
    }
    
    
    @Override
    public double Cio_normalized(URI l, R a, R b) {
        int cioA, wcio;
        double wcio_norm = 0, x , weight;

        wcio = Cio(l, a, b);

        weight = this.weight.linkWeight(l , a , b);

        wcio = Cio(l, a, b);

        if(wcio == 0 || weight == 0){
            return 0;
        }
        else {
            cioA = Cio(l, a);


            if(cioA != 0 ){
                x = 1 + Math.log10( cioA / 2 );

                wcio_norm = ((double) wcio ) / x;
            }
            else
                wcio_norm = (double) wcio ;
        }  

        return wcio_norm * weight;

    }

    
    
}
