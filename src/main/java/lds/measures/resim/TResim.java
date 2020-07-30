/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;


import java.util.List;
import lds.LdManager.ontologies.Ontology;
import lds.config.Config;
import lds.resource.R;
import org.openrdf.model.URI;



/**
 *
 * @author Fouad Komeiha
 */
public class TResim extends ResourceSimilarity{
   
    
    public TResim(Config config) throws Exception {
        super(config);
    }

    @Override
    public double LDSD(R a, R b) {
        double tcdA_norm = 0, tcdB_norm = 0, tcii_norm = 0, tcio_norm = 0;
        
        for (URI li : edges) {
            tcdA_norm = tcdA_norm + Cd_normalized(li , a, b);
            tcdB_norm = tcdB_norm + Cd_normalized(li , b, a);

            for (URI lj : edges) {
                tcii_norm = tcii_norm + Cii_normalized(li , lj , a , b);
                tcio_norm = tcio_norm + Cio_normalized(li , lj , a , b);

            }
        }
               
        return 1 / (1 + tcdA_norm + tcdB_norm + tcii_norm + tcio_norm);
    }

    
    @Override
    public double Cd_normalized(URI l, R a, R b) {
        int cd = Cd(l, a, b) , cd_l = 0;
        double cd_norm = 0 , x =0;

        if(cd != 0){
            cd_l = Cd(l);
            
            if(cd_l !=0 ){
                x = 1 + Math.log10(cd_l);
                cd_norm = (double) cd / x;
            }
            else
               cd_norm = (double) cd; 
        }

        return cd_norm;

    }
    
    @Override
    public int Cii(URI li , URI lj , R a, R b) {
        return resimLDLoader.countTyplessCommonSubjects(li , lj , a , b);
          
    }

    
    @Override
    public int Cii(URI li , URI lj , R k) {
        return resimLDLoader.countTyplessCommonSubjects(li , lj , k);
    }

    
    @Override
    public double Cii_normalized(URI li , URI lj , R a, R b) {
        int tcii_li_lj = 0, tcii;
        double x, tcii_norm = 0;

        tcii = Cii(li , lj , a, b);

        if(tcii != 0){
            List<String> commonSubjects = resimLDLoader.getCommonSubjects(a, b);
//            List<R> traversedResorces = new ArrayList<>();
             
                for(String resource:commonSubjects){
                    String string[] =  resource.split("\\|");
                    String uri = string[0];
                    
                    R k = new R(Ontology.decompressValue(uri));
//                    if(! traversedResorces.contains(k)){
//                        traversedResorces.add(k);
                        tcii_li_lj = tcii_li_lj + Cii(li , lj , k);
                        
//                    }
                    
                }

                if(tcii_li_lj != 0){
                    x = 1 + Math.log10(tcii_li_lj);
                    tcii_norm = ((double) tcii / x);
                }
                
                else 
                    tcii_norm = (double) tcii;
             
        }

        return tcii_norm;

    }

    
    @Override
    public int Cio(URI li , URI lj , R a, R b) {
        return resimLDLoader.countTyplessCommonObjects(li , lj , a , b);
    }

    
    @Override
    public int Cio(URI li , URI lj , R k) {
        return resimLDLoader.countTyplessCommonObjects(li , lj , k);
    }

    
    @Override
    public double Cio_normalized(URI li , URI lj , R a, R b) {
        int tcio_li_lj = 0, tcio;
        double tcio_norm = 0, x;

        tcio = Cio(li , lj , a, b);

        if(tcio != 0)
        {
            List<String> commonObjects = resimLDLoader.getCommonObjects(a, b);
//            List<R> traversedResorces = new ArrayList<>();
                for(String resource: commonObjects){
                    
                    String string[] =  resource.split("\\|");
                    String uri = string[0];
                    R k = new R(Ontology.decompressValue(uri));
//                    if(! traversedResorces.contains(k)){
//                        traversedResorces.add(k);
                        tcio_li_lj = tcio_li_lj + Cio(li , lj , k);
                        
//                    }
                }
                
                if(tcio_li_lj != 0){                    
                    x = 1 + Math.log10(tcio_li_lj);
                    tcio_norm = ((double) tcio / x);
                }
                
                else 
                    tcio_norm = (double) tcio ;              
            
        }  

        return tcio_norm;

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
