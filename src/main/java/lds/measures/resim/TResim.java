/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;


import java.util.Set;
import lds.LdManager.LdManager;
import lds.resource.R;
import org.openrdf.model.URI;


/**
 *
 * @author Fouad Komeiha
 */
public class TResim extends Resim{
    public Set<URI> edges;

    public LdManager resimLDLoader;
    
    public TResim() {
        
    }
    
    @Override
    public double compare(R a, R b) {
        double sim = 0;
        sim = TResim(a, b);
        return sim;
    }
    
    public double TResim(R a , R b){
        double x = 0;

        this.edges = resimLDLoader.getEdges(a , b);
        if (a.equals(b) || resimLDLoader.isSameAs(a, b))
                return 0;
        
        x = LDSDsim(a, b);
        
        if(x != 1)
            return x;
            
        return PropertySim(a, b);                

    }

    @Override
    public double LDSDsim(R a, R b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double PropertySim(R a, R b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
//    public double TCii(URI li , URI lj , R k , R a , R b){
//        return resimLDLoader.shareTyplessCommonSubject(li , lj , k ,a, b);
//    }
    
}
