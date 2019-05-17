/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import lds.LdManager.LdManager;
import lds.measures.LdSimilarityMeasureBase;
import lds.resource.R;
import org.openrdf.model.URI;
import slib.utils.ex.SLIB_Ex_Critic;

/**
 *
 * @author Fouad Komeiha
 */
public class Resim extends LdSimilarityMeasureBase {
	public Set<URI> edges;

	public LdManager resimLDLoader;

	public Resim() {

	}

	public Resim(LdManager resimLDLoader) {
		this.resimLDLoader = resimLDLoader;
	}
        
        
        @Override
	public double compare(R a, R b) {
		double sim = 0;
		try {
                        
			sim = Resim(a, b);

		} catch (SLIB_Ex_Critic ex) {
			Logger.getLogger(Resim.class.getName()).log(Level.SEVERE, null, ex);
		}
		return sim;
	}

	public double Resim(R a, R b) throws SLIB_Ex_Critic {
		
		int w1 = 1, w2 = 2;
		double x = 0, y = 0;
                
                this.edges = resimLDLoader.getEdges(a , b);
		// TODO: if we're in the same dataset, it's not necessary to check sameAs ?
		if (a.equals(b) || resimLDLoader.isSameAs(a, b))
			return 1;
                        
		x = w1 * PropertySim(a, b);
		y = w2 * LDSDsim(a, b);
		return (x + y) / (w1 + w2);

	}

	public double PropertySim(R a, R b) {
		double x = 0, y = 0, ip = 0, op = 0, ppty = 0;

		for (URI e : edges) {
			x = x + ((double) Csip(e, a, b) / Cd(e));
			y = y + ((double) Csop(e, a, b) / Cd(e));
		}

		ip = x / (Cip(a) + Cip(b));
		op = y / (Cop(a) + Cop(b));

		ppty = ip + op;

		return ppty;
	}

	public double LDSD(R a, R b) {
                double cdA_norm = 0, cdB_norm = 0, cii_norm = 0, cio_norm = 0;

		for (URI l : edges) {
                    
			cdA_norm = cdA_norm + Cd_normalized(l, a, b);
			cdB_norm = cdB_norm + Cd_normalized(l, b, a);
			cii_norm = cii_norm + Cii_normalized(l, a , b);
			cio_norm = cio_norm + Cio_normalized(l, a , b);

		}      	
                
		return 1 / (1 + cdA_norm + cdB_norm + cii_norm + cio_norm);
	}

	public double LDSDsim(R a, R b) {
		return 1 - LDSD(a, b);
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

	public double Cd_normalized(URI l, R a, R b) {
		int cd = Cd(l, a, b);
		double cd_norm = 0;
                
                if(cd != 0){
                    double x = 1 + Math.log(Cd(l, a));

                    cd_norm = (double) cd / x;
                }

		return cd_norm;

	}

	public int Cii(URI l, R a, R b) {
            if (resimLDLoader.shareCommonSubject(l, a , b))
                return 1;
            return 0;
	}

	public int Cii(URI l, R a) {
            return resimLDLoader.countShareCommonSubjects(l, a);
	}

	public double Cii_normalized(URI l, R a, R b) {

		int ciiA, ciiB, cii;
		double x, cii_norm = 0;

		cii = Cii(l, a, b);
                
                if(cii != 0){
                    ciiA = Cii(l, a);
                    ciiB = Cii(l, b);
                    x = 1 + Math.log((ciiA + ciiB) / 2);

                    cii_norm = ((double) cii / x);
                }

		return cii_norm;

	}

	public int Cio(URI l, R a, R b) {

            if(resimLDLoader.shareCommonObject(l, a , b))
                return 1;
            return 0;
	}

	public int Cio(URI l, R a) {
            return resimLDLoader.countShareCommonObjects(l, a);
	}

	public double Cio_normalized(URI l, R a, R b) {
            int cioA, cioB, cio;
            double cio_norm = 0, x;

            cio = Cio(l, a, b);

            if(cio != 0)
            {
                cioA = Cio(l, a);
                cioB = Cio(l, b);
                x = 1 + Math.log((cioA + cioB) / 2);

                cio_norm = ((double) cio / x);
            }  

            return cio_norm;

	}

}
