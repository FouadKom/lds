/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.lods;

import lds.measures.lods.ontologies.O;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.measures.LdSimilarityMeasure;
import lds.resource.R;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class LODS implements LdSimilarityMeasure{
    private boolean useIndeses;
    private List<O> ontologyList;
    private boolean dataAugmentation;
    
    private Conf config;
    
    public LODS(Conf config) throws Exception{
        if(config.getParam("useIndexes") == null || config.getParam("ontologyList") == null || config.getParam("dataAugmentation") == null || config.getParam("datasetMain") == null
                || config.getParam("sup") == null || config.getParam("sub") == null)
            throw new Exception("Some configuration parameters missing"); 
        
        this.config = config;

    }
    

    @Override
    public double compare(R a, R b) {
        double LODS = 0.0 , simI = 0.0 , simC = 0.0 , simP = 0.0;
        
        SimI lods_simI = null;
        SimC lods_simC = null;
        SimP lods_simP = null;
        
        try {
            
            lods_simI = new SimI(config);
            lods_simC = new SimC(config);
            lods_simP = new SimP(config);
            
        } catch (Exception ex) {
            Logger.getLogger(LODS.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (a.equals(b)) {
                LODS = 1.0;
        }
        
        else {

            simI = lods_simI.compare(a, b);
            simC = lods_simC.compare(a, b);
            simP = lods_simP.compare(a, b);

            List<Double> scores = new ArrayList<>();

            if (simI != -1)
                    scores.add(simI);
            if (simC != -1)
                    scores.add(simC);
            if (simP != -1)
                    scores.add(simP);

            for (Double score : scores) {
                    LODS += score;
            }

            LODS = LODS / scores.size();

        }
        return LODS;
    }
    
   

    @Override
    public void closeIndexes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadIndexes() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LdSimilarityMeasure getMeasure() {
        return this;
    }
    
}
