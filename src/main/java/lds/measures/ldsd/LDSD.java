/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.ldsd;

import java.util.List;
import java.util.Set;
import lds.LdManager.LdsdLdManager;
import lds.LdManager.LdsdLdManagerO;
import lds.LdManager.ontologies.Ontology;
import lds.measures.LdSimilarity;
import lds.measures.weight.WeightO;
import lds.measures.weight.WeightMethod;
import lds.resource.R;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public abstract class LDSD implements LdSimilarity {
    protected List<URI> edges;
    protected LdsdLdManagerO LDSDLDLoader;
    protected LdsdLdManagerO SpecificLDSDLDLoader;
    protected WeightO weight;
    protected boolean useIndeses;
    
    public LDSD(Conf config) throws Exception {
        int confsize = config.getParams().size();
        switch(confsize){
            case 0:
                throw new Exception("Configuration parameters missing"); 
                
            case 1:
                throw new Exception("Some configuration parameters missing"); 
                
            case 2:
                this.LDSDLDLoader = new LdsdLdManagerO((LdDataset) config.getParam("LdDatasetMain") , (Boolean) config.getParam("useIndexes") );
                this.useIndeses = (Boolean) config.getParam("useIndexes");
                break;
                
            case 3:
                throw new Exception("Some configuration parameters missing"); 

            default:
                this.LDSDLDLoader = new LdsdLdManagerO((LdDataset) config.getParam("LdDatasetMain") , (Boolean) config.getParam("useIndexes") );
                this.SpecificLDSDLDLoader = new LdsdLdManagerO((LdDataset) config.getParam("LdDatasetSpecific") , (Boolean) config.getParam("useIndexes") );
                this.weight = new WeightO((WeightMethod)config.getParam("WeightMethod") , LDSDLDLoader , SpecificLDSDLDLoader , (Boolean)config.getParam("useIndexes"));
                this.useIndeses = (Boolean) config.getParam("useIndexes");
                break;

//            default:
//                throw new Exception("Some configuration parameters missing");               
        }
        
    }
        
    @Override
    public void closeIndexes(){
        if(useIndeses){
            LDSDLDLoader.closeIndexes();
            
            if(SpecificLDSDLDLoader != null && weight != null){
                SpecificLDSDLDLoader.closeIndexes();
                weight.closeIndexes();
            }
        }
        
        //close prefixes and namespaces index
        Ontology.closeIndexes();
        
    }
    
    
    @Override
    public void loadIndexes() throws Exception{
        if(useIndeses){
            LDSDLDLoader.loadIndexes();
            
            if(SpecificLDSDLDLoader != null && weight != null){
                SpecificLDSDLDLoader.loadIndexes();
                weight.loadIndexes();
            }
        }
        
        //load prefixes and namespaces index
        Ontology.loadIndexes();
    }
        
        
    @Override
    public abstract double compare(R a , R b);

    /**
     * Returns number of links between a ---n--> b
     * 
     * @param a
     * @param b
     * @param g
     * @return
     */

    public int Cd(R a, R b) {
        int count = 0;
        for(URI edge: edges){
            count = count + Cd(edge , a , b);
        }

        return count;
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
            int cd = Cd(l, a, b) , cd_l_a = 0;
            double cd_norm = 0;

            if(cd != 0){
                cd_l_a = Cd(l, a);
                if(cd_l_a != 0){
                    double x = 1 + Math.log10(cd_l_a);
                    cd_norm = (double) cd / x;
                }
                else
                  cd_norm = (double) cd;  
            }

            return cd_norm;
    }

    //Similar to Resim
    public int Cii(URI l, R a) {
        return LDSDLDLoader.countShareCommonSubjects(l, a);
    }

    //Similar to Resim
    public int Cii(URI l, R a, R b) {
        return LDSDLDLoader.countShareCommonSubjects(l, a , b);

    }


    public int Cii(R a, R b) {

        int count = 0;
        for (URI l : edges) {
            count = count + Cii(l , a , b);
        }

        return count;
    }

    //Similar to Resim with some editing
    public double Cii_normalized(URI l, R a, R b) {

            int ciiA,  cii;
            double x, cii_norm = 0;

            cii = Cii(l, a, b);

            if(cii != 0){
                ciiA = Cii(l, a);

                if( ciiA != 0){
                    x = 1 + Math.log10( ciiA );
                    cii_norm = ((double) cii ) / x;
                }
                else
                    cii_norm = (double) cii;
            }

            return cii_norm;

    }
    

    //Similar to Resim
    public int Cio(URI l, R a) {
        return LDSDLDLoader.countShareCommonObjects(l, a);
    }

    //Similar to Resim
    public int Cio(URI l, R a, R b) {
        return LDSDLDLoader.countShareCommonObjects(l, a , b);

    }

    public int Cio(R a, R b) {

        int count = 0;
        for (URI l : edges) {
            count = count + Cio(l , a , b);
        }

        return count;
    }

    //Similar to Resim
    public double Cio_normalized(URI l, R a, R b) {
        int cioA, cio;
        double cio_norm = 0, x;

        cio = Cio(l, a, b);

        if(cio != 0)
        {
            cioA = Cio(l, a);


            if(cioA != 0 ){
                x = 1 + Math.log10(cioA);

                cio_norm = ((double) cio ) / x;
            }
            else
                cio_norm = (double) cio ;
        }  

        return cio_norm;

    }
    
    
    @Override
    public LdSimilarity getMeasure(){
        return this;
    }

}
