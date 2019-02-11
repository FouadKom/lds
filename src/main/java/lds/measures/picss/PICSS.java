/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.picss;


import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.measures.LdSimilarityMeasureBase;
import lds.resource.R;
import org.apache.commons.httpclient.HttpException;
import org.openrdf.model.URI;
import sc.research.ldq.*;
import slib.utils.ex.SLIB_Ex_Critic;

/**
 *
 * @author Fouad Komeiha
 */
public class PICSS extends LdSimilarityMeasureBase{
    private LdDataset dataset;
    
    public PICSS(LdDataset dataset){
        this.dataset = dataset;
    }    
    
    
    @Override
    public double compare(R a, R b) {
	double sim = 0;
        try {
            sim= PICSS(a.getUri() , b.getUri());
        } catch (SLIB_Ex_Critic ex) {
            Logger.getLogger(PICSS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HttpException ex) {
            Logger.getLogger(PICSS.class.getName()).log(Level.SEVERE, null, ex);
        }
    return sim;

    }    
    
    public double PIC(Set<String> Fa){
        
        double pic = 0.0;
        double ic = 0.0;
        int N = Utility.getResourcesNum(dataset);
        for(String s : Fa){
            
            int featureFrequency = Utility.getFeatureFrequency(dataset, s);
            ic = (double)featureFrequency/N;
            pic = pic - Utility.logb(ic , 2);
                
        }
        return pic;
    }
    
    public double PICSS(URI a, URI b) throws SLIB_Ex_Critic, HttpException{
        Set<String> Fa ,Fb;
        Fa = Utility.getFeaturesSet(a , dataset);
        Fb = Utility.getFeaturesSet(b , dataset);
        
        Set<String> interSection = Utility.intersection(Fa , Fb);
        Set<String> diffFa = Utility.difference(Fa , Fb);
        Set<String> diffFb = Utility.difference(Fb , Fa);
        
        double picINT = PIC(interSection);
        double picDiffFa = PIC(diffFa);
        double picDiffFb = PIC(diffFb);
        
        
        return (picINT/(picINT + picDiffFa + picDiffFb));
        
    }
    
    
    
}
