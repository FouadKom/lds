/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;

import java.util.List;
import lds.LdManager.ontologies.Ontology;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.resource.R;
import org.openrdf.model.URI;

/**
 *
 * @author Fouad Komeiha
 */
public class WResim extends ResourceSimilarity{

        public WResim(Config config) throws Exception {
        super(config);
        if( config.getParam(ConfigParam.LdDatasetSpecific)== null || config.getParam(ConfigParam.WeightMethod) == null)
            throw new Exception("Some configuration parameters missing");  
        
        
    }
    

    @Override
    public double LDSD(R a, R b) {
        double wcdA_norm = 0, wcdB_norm = 0, wcii_norm = 0, wcio_norm = 0;

        for (URI l : edges) {

                wcdA_norm = wcdA_norm + Cd_normalized(l, a, b);
                wcdB_norm = wcdB_norm + Cd_normalized(l, b, a);
                wcii_norm = wcii_norm + Cii_normalized(l, a , b);
                wcio_norm = wcio_norm + Cio_normalized(l, a , b);

        }      	

        return 1 / (1 + wcdA_norm + wcdB_norm + wcii_norm + wcio_norm);
    }
    
    @Override
    public double Cd_normalized(URI l, R a, R b) {
        int cd = Cd(l, a, b) , cd_l = 0;
        double cd_norm = 0 , weight = 0 , x = 0;  
        
        weight = this.weight.linkWeight(l , a , b);

        if(cd == 0 || weight == 0.0 ){
            return 0;
        }
        else
        {
            cd_l = Cd(l);
            if(cd_l != 0){
                x = 1 + Math.log10(cd_l);
                cd_norm = (double) cd / x;
            }
            else
               cd_norm = (double) cd; 
            
            
        }

        return cd_norm * weight ;

    }
    
    
    @Override
    public double Cii_normalized(URI l, R a, R b) {

        int wciil = 0, wcii;
        double x, wcii_norm = 0 , weight = 0; 
        
        weight = this.weight.linkWeight(l , a , b);

        wcii = Cii(l, a, b);

        if(wcii == 0 || weight == 0){
            return 0;
        }
        else
        {
            List<String> commonObjects = resimLDLoader.getCommonSubjects(a, b);

            for(String resource: commonObjects){
                String string[] =  resource.split("\\|");
                String uri = string[0];
                R k = new R(Ontology.decompressValue(uri));
                wciil = wciil + Cio(l , k);
            }
            
            if(wciil != 0){
                x = 1 + Math.log10((wciil));
                wcii_norm = ((double) wcii / x);
            }
            
            else
               wcii_norm = (double) wcii;
            
                
        }

        return wcii_norm * weight;

    }
    
    
    @Override
    public double Cio_normalized(URI l, R a, R b) {
        int wciol = 0 , wcio;
        double wcio_norm = 0, x  , weight = 0;
        
        weight = this.weight.linkWeight(l , a , b);

        wcio = Cio(l, a, b);

        if(wcio == 0 || weight == 0){
            return 0;
        }
        else
        {
            List<String> commonObjects = resimLDLoader.getCommonObjects(a, b);

            for(String resource: commonObjects){
                String string[] =  resource.split("\\|");
                String uri = string[0];
                R k = new R(Ontology.decompressValue(uri));
                wciol = wciol + Cio(l , k);
            }
            
            if(wciol != 0){
                x = 1 + Math.log10((wciol));
                wcio_norm = ((double) wcio / x);
            }
            else 
                wcio_norm = (double) wcio;
            
            
        }  

        return wcio_norm * weight;

    }

    @Override
    public int Cii(URI li, URI lj, R a, R b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int Cii(URI li, URI lj , R k) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double Cii_normalized(URI li, URI lj, R a, R b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int Cio(URI li, URI lj, R a, R b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int Cio(URI li, URI lj , R k) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double Cio_normalized(URI li, URI lj, R a, R b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
}
