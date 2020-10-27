/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.config.Config;
import lds.measures.LdSimilarity;
import lds.measures.picss.WPICS;
import lds.resource.R;

/**
 *
 * @author Fouad komeihaa
 */
public class WEPICS extends WPICS{
    
    public WEPICS(Config config) throws Exception {
        super(config);
    }
    
    @Override
    public double compare(R a, R b) {
	double sim = 0;

        try {
            sim= WEPICS(a , b);
        } catch (Exception ex) {
            Logger.getLogger(EPICS.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }


        return sim;

    }

    private double WEPICS(R a , R b) throws Exception {

        List<String> features_a = ldManager.getFeatures(a);
        List<String> features_b = ldManager.getFeatures(b);
        
        if(features_a.isEmpty() ||  features_b.isEmpty())
            return 0;
        
        List<String> common_features = Utility.commonFeatures(features_a, features_b);        
        
        if(common_features == null || common_features.isEmpty())
            return 0;
        
        features_a.removeAll(common_features);
        features_b.removeAll(common_features);
        
        List<String> similar_features = Utility.similarFeatures(features_a , features_b);        
        
        features_a.removeAll(similar_features);
        features_b.removeAll(similar_features);
        
	List<String> unique_features_a = Utility.uniqueFeatures(features_a , features_b);
	List<String> unique_features_b = Utility.uniqueFeatures(features_b, features_a);
        
        double x = PIC(common_features , a);
	double y = PIC(unique_features_a , a);
	double z = PIC(unique_features_b , b);
        double s = PIC(similar_features , a)/2; //because similar features we get average between the two resources

	if ((x + s + y + z) == 0)
            return 0;
        	
        double sim = ((x + s) / (x + s + y + z)) < 0 ? 0 : ((x + s) / (x + s + y + z));
        return sim;
    }
    
    @Override
    public LdSimilarity getMeasure() {
        return this;
    }
    
    
}
