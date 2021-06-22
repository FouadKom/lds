/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;

import java.util.List;
import lds.measures.weight.WeightMethod;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.LdManager.ResimLdManager;
import lds.LdManager.ontologies.Ontology;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.resource.R;
import org.openrdf.model.URI;
import ldq.LdDataset;
import lds.measures.LdSimilarity;
import lds.measures.weight.Weight;

/**
 *
 * @author Fouad Komeiha
 */
public abstract class ResourceSimilarity implements LdSimilarity {
    protected List<URI> edges;
    protected ResimLdManager resimLDLoader;
    protected ResimLdManager SpecificResimLdLoader;
    protected Weight weight;
    protected boolean useIndeses;
    
    public ResourceSimilarity(Config config) throws Exception{
        int confsize = config.getSize();
        switch(confsize){
            case 0:
                throw new Exception("Configuration parameters missing"); 
                
            case 1:
                throw new Exception("Some configuration parameters missing");  
                
            case 2:
                this.resimLDLoader = new ResimLdManager((LdDataset) config.getParam(ConfigParam.LdDatasetMain) , (Boolean) config.getParam(ConfigParam.useIndexes) );
                this.useIndeses = (Boolean) config.getParam(ConfigParam.useIndexes);
                break;
                
            case 3:
                throw new Exception("Some configuration parameters missing");
                
            default:
                this.resimLDLoader = new ResimLdManager((LdDataset) config.getParam(ConfigParam.LdDatasetMain) , (Boolean) config.getParam(ConfigParam.useIndexes) );
                this.SpecificResimLdLoader = new ResimLdManager((LdDataset) config.getParam(ConfigParam.LdDatasetSpecific) , (Boolean) config.getParam(ConfigParam.useIndexes) );
                this.weight = new Weight((WeightMethod)config.getParam(ConfigParam.WeightMethod) , resimLDLoader , SpecificResimLdLoader , (Boolean)config.getParam(ConfigParam.useIndexes));
                this.useIndeses = (Boolean) config.getParam(ConfigParam.useIndexes);
                break;            
        }            
    }
    
    
    @Override
    public void closeIndexes(){
        if(useIndeses){
            resimLDLoader.closeIndexes();
            
            if(SpecificResimLdLoader != null && weight != null){
                SpecificResimLdLoader.closeIndexes();
                weight.closeIndexes();
            }
        }
        
        //close prefixes and namespaces index
        Ontology.closeIndexes();
        
        
    }
    
    @Override
    public void loadIndexes() throws Exception{
        if(useIndeses){
            resimLDLoader.loadIndexes();
            
            if(SpecificResimLdLoader != null && weight != null){
                SpecificResimLdLoader.loadIndexes();
                weight.loadIndexes();
            }
        }
        
        //load prefixes and namespaces index
        Ontology.loadIndexes();
    }

    @Override
    public double compare(R a, R b) {
               
        double sim = 0;
        try {

                sim = Resim(a, b);


        } catch (Exception ex) {
                Logger.getLogger(Resim.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
        }
        
        return sim;
    }
    
    
    public double compare(R a, R b , int w1 , int w2) {
        
        double sim = 0;
        try {
                sim = Resim(a, b , w1 , w2);


        } catch (Exception ex) {
                Logger.getLogger(Resim.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
        }
        
        return sim;
    }


    public double Resim(R a, R b) {

            int w1 = 1, w2 = 1;
            double x = 0, y = 0;

            this.edges = resimLDLoader.getEdges(a , b);
            
            if(edges == null)
                return 0;
            // TODO: if we're in the same dataset, it's not necessary to check sameAs ?
            if (a.equals(b) || resimLDLoader.isSameAs(a, b))
                    return 1;

            x = w1 * PropertySim(a, b);
            y = w2 * LDSDsim(a, b);
            return (x + y) / (w1 + w2);

    } 
    
    public double Resim(R a, R b , int w1 , int w2) {

            double x = 0, y = 0;

            this.edges = resimLDLoader.getEdges(a , b);
            // TODO: if we're in the same dataset, it's not necessary to check sameAs ?
            if (a.equals(b) || resimLDLoader.isSameAs(a, b))
                    return 1;

            x = w1 * PropertySim(a, b);
            y = w2 * LDSDsim(a, b);
            return (x + y) / (w1 + w2);

    } 

    public abstract double LDSD(R a , R b);

    
    public double LDSDsim(R a, R b) {
            return 1 - LDSD(a, b);
    }

    public double PropertySim(R a, R b) {
            double x = 0, y = 0, cip = 0 , cop = 0 ,ip = 0, op = 0, ppty = 0;

            for (URI e : edges) {
                    int cd = Cd(e);
                    x = x + ((double) Csip(e, a, b) / cd);
                    y = y + ((double) Csop(e, a, b) / cd);
            }
            
            if (x !=0)
                cip = Cip(a) + Cip(b);
            else 
                ip = 0;
            
            if(y != 0)
                 cop = Cop(a) + Cop(b);
            else
                op = 0;
            
            if(cip != 0)
                    ip = x / cip;
            else
                ip = 0;
            
            if(cop != 0 )
                op = y / cop;
            
            else
                op = 0;

            ppty = ip + op;

            return ppty;
    }

    public int Cip(R a) {
        return resimLDLoader.countSubject(a);
    }

    public int Cop(R a) {
        return resimLDLoader.countObject(a);
    }

    public int Csip(URI l, R a, R b) {
        if ( resimLDLoader.countSubject(l , a) > 0 && resimLDLoader.countSubject(l , b ) > 0 )
            return 1;
        return 0;
    }

    public int Csop(URI l, R a, R b) {
        if (resimLDLoader.countObject(l , a) > 0 && resimLDLoader.countObject(l , b ) > 0)
            return 1;
        return 0;
    }

    public int Cd(URI l, R a, R b) {
        if(resimLDLoader.isDirectlyConnected(l, a, b))
            return 1;
        else
            return 0;
    }

    public int Cd(URI l, R a) {
        return resimLDLoader.countObject(l , a);
    }

    public int Cd(URI l) {
        return resimLDLoader.countPropertyOccurrence(l);
    }

    public abstract double Cd_normalized(URI l, R a, R b);

    public int Cii(URI l, R a, R b) {
        return resimLDLoader.countShareCommonSubjects(l, a , b);

    }

    public int Cii(URI l, R a) {
        return resimLDLoader.countShareCommonSubjects(l, a);
    }

    public abstract double Cii_normalized(URI l, R a, R b);

    public int Cio(URI l, R a, R b) {
        return resimLDLoader.countShareCommonObjects(l, a , b);

    }

    public int Cio(URI l, R a) {
        return resimLDLoader.countShareCommonObjects(l, a);
    }

    public abstract double Cio_normalized(URI l, R a, R b);

    public abstract int Cii(URI li , URI lj , R a, R b);
    
    public abstract int Cii(URI li , URI lj , R k);
    
    public abstract double Cii_normalized(URI li , URI lj , R a, R b);
    
    public abstract int Cio(URI li , URI lj , R a, R b);
    
    public abstract int Cio(URI li , URI lj , R k);
    
    public abstract double Cio_normalized(URI li , URI lj , R a, R b);
    
    
    /*public ResimLdManager getMainLdManager(){
        return this.resimLDLoader;
    }
    
    public ResimLdManager getSpecificLdManager(){
        return this.SpecificResimLdLoader;
    }
    
    public Weight getWeight(){
        return this.weight;
    }
    
    
    public void setMainLdManager(ResimLdManager manager){
        this.resimLDLoader = manager;
    }
    
    public void setSpecificLdManager(ResimLdManager manager){
        this.SpecificResimLdLoader = manager;
    }
    
    public void setWeight(Weight weight){
        this.weight = weight;
    }*/
    
    @Override
    public LdSimilarity getMeasure(){
        return this;
    }
    
    
}
