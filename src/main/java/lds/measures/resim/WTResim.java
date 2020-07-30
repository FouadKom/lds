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
import lds.resource.LdResourceFactory;
import lds.resource.R;
import org.openrdf.model.URI;


/**
 *
 * @author Fouad Komeiha
 */
public class WTResim extends ResourceSimilarity{
    
     public WTResim(Config config) throws Exception {
        super(config);
        if( config.getParam(ConfigParam.LdDatasetSpecific)== null || config.getParam(ConfigParam.WeightMethod) == null)
            throw new Exception("Some configuration parameters missing"); 
    }

    @Override
    public double LDSD(R a, R b) {
        double wtcdA_norm = 0, wtcdB_norm = 0, wtcii_norm = 0, wtcio_norm = 0;
        
        for (URI li : edges) {
            wtcdA_norm = wtcdA_norm + Cd_normalized(li , a, b);
            wtcdB_norm = wtcdB_norm + Cd_normalized(li , b, a);
            
            for (URI lj : edges) {
                wtcii_norm = wtcii_norm + Cii_normalized(li , lj , a , b);
                wtcio_norm = wtcio_norm + Cio_normalized(li , lj , a , b);

            }
        }     	

        return 1 / (1 + wtcdA_norm + wtcdB_norm + wtcii_norm + wtcio_norm );
    }
    
    @Override
    public double Cd_normalized(URI l, R a, R b) {
        int cd = Cd(l, a, b) ,cd_l = 0;
        double cd_norm = 0 , weight_l = 0;
        
        weight_l = weight.linkWeight(l , a , b);

        if(cd == 0 || weight_l == 0){
            return 0;
        }
        else
        {
            cd_l = Cd(l);
            if(cd_l != 0){
                double x = 1 + Math.log10(cd_l);

                cd_norm = (double) cd / x;
            }
            else
                cd_norm = (double) cd;
        }

        return cd_norm * weight_l ;

    }
    
    @Override
    public int Cii(URI li, URI lj , R k) { //Similar to TCii in TResim
        return resimLDLoader.countTyplessCommonSubjects(li , lj , k);
        
    }
    
    @Override
    public int Cii(URI li, URI lj, R a, R b) {//Similar to TCii in TResim
       return resimLDLoader.countTyplessCommonSubjects(li , lj , a , b);
    }
    
    
    @Override
    public double Cii_normalized(URI li , URI lj , R a, R b) {
        int wtcii_li_lj = 0, wtcii;
        double x, wtcii_norm = 0 , weight_li = 0 , weight_lj = 0;
        
        weight_li = weight.linkWeight(li , a , b);
        weight_lj = weight.linkWeight(lj , a , b);

        wtcii = Cii(li , lj , a, b);

        if(wtcii == 0 || weight_li == 0 || weight_lj == 0){
            return 0;
        }
        else 
        {
            List<String> commonSubjects = resimLDLoader.getCommonSubjects(a, b) ;
            
            for(String resource:commonSubjects){
 //                R k = LdResourceFactory.getInstance().uri(resource).create();
                String string[] =  resource.split("\\|");
                String uri = string[0];
                R k = new R(Ontology.decompressValue(uri));

                wtcii_li_lj = wtcii_li_lj + Cii(li , lj ,k);
            }
            
            if(wtcii_li_lj != 0){
                x = 1 + Math.log10(wtcii_li_lj);

                wtcii_norm = ((double) wtcii / x);
            }
            else
                wtcii_norm = (double) wtcii;
            
        }

        return wtcii_norm * weight_lj * weight_li;
    }
    
    
    @Override
    public int Cio(URI li, URI lj, R a, R b) { //TCio  in TResim
        return resimLDLoader.countTyplessCommonObjects(li , lj , a , b);
    }
    

    @Override
    public int Cio(URI li, URI lj , R k) { //TCio  in TResim
        return resimLDLoader.countTyplessCommonObjects(li , lj , k);
    } 
    

    @Override
    public double Cio_normalized(URI li, URI lj, R a, R b) {
        int wtcio_li_lj = 0, wtcio;
        double wcio_norm = 0, x , weight_li = 0 , weight_lj = 0;
        
        weight_li = weight.linkWeight(li , a , b);
        weight_lj = weight.linkWeight(lj , a , b);
        
        wtcio = Cio(li , lj , a, b);

        if(wtcio == 0 || weight_li == 0 || weight_lj == 0){
            return 0;
        }
        else
        {
            List<String> commonObjects = resimLDLoader.getCommonObjects(a, b);
            
            for(String resource: commonObjects){
 //                R k = LdResourceFactory.getInstance().uri(resource).create();
                String string[] =  resource.split("\\|");
                String uri = string[0];
                R k = new R(Ontology.decompressValue(uri));

               wtcio_li_lj = wtcio_li_lj + Cio(li , lj ,k);
            }
            
            if(wtcio_li_lj != 0){
                x = 1 + Math.log10(wtcio_li_lj);

                wcio_norm = ((double) wtcio / x);
            }
            else
                wcio_norm = (double) wtcio;
        }  

        return wcio_norm * weight_lj * weight_li;
    }

    @Override
    public double Cii_normalized(URI l, R a, R b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double Cio_normalized(URI l, R a, R b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    
    
    
}
