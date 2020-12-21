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
import lds.config.Config;
import lds.config.ConfigParam;
import lds.feature.Feature;
import lds.resource.R;
import lds.measures.LdSimilarity;

/**
 *
 * @author Fouad Komeiha
 */
public class LODS implements LdSimilarity{
    private boolean useIndeses;
    private List<O> ontologyList;
    private boolean dataAugmentation;
    
    private SimI lods_simI = null;
    private SimC lods_simC = null;
    private SimP lods_simP = null;
    
    private Config config;
    
    public LODS(Config config) throws Exception{
        if(config.getParam(ConfigParam.useIndexes) == null || config.getParam(ConfigParam.ontologyList) == null || config.getParam(ConfigParam.dataAugmentation) == null || config.getParam(ConfigParam.LdDatasetMain) == null
                || config.getParam(ConfigParam.sup) == null || config.getParam(ConfigParam.sub) == null)
            throw new Exception("Some configuration parameters missing"); 
        
        this.config = config;

    }
    

    @Override
    public double compare(R a, R b) {
        double LODS = 0.0 , simI = 0.0 , simC = 0.0 , simP = 0.0;
        
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
        lods_simI.closeIndexes();
        lods_simP.closeIndexes();
        lods_simC.closeIndexes();
        
    }

    @Override
    public void loadIndexes() throws Exception {
        lods_simI.loadIndexes();
        lods_simP.loadIndexes();
        lods_simC.loadIndexes();
      
    }

    @Override
    public LdSimilarity getMeasure() {
        return this;
    }
        
}
