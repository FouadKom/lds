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
import lds.feature.Feature;
import lds.resource.R;
import lds.measures.LdSimilarity;
import lds.measures.picss.PICSS;

/**
 *
 * @author Fouad Komeiha
 */
public class EPICS extends PICSS {
    
    public EPICS(Config config) throws Exception{
        super(config);
    }
    
    
    @Override
    public double compare(R a, R b) {
	double sim = 0;

        try {
            sim= EPICS(a , b);
        } catch (Exception ex) {
            Logger.getLogger(EPICS.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }


        return sim;

    }

    private double EPICS(R a , R b) throws Exception {

        List<String> features_a = ldManager.getFeatures(a);
        List<String> features_b = ldManager.getFeatures(b);
        
        if(features_a.isEmpty() ||  features_b.isEmpty())
            return 0;
        
        List<String> common_features = Feature.commonFeatures(features_a, features_b);        
        
        if(common_features == null || common_features.isEmpty())
            return 0;
        
        features_a.removeAll(common_features);
        features_b.removeAll(common_features);
        
        List<String> similar_features = EPICS_Feature.similarFeatures(features_a , features_b);        
        
        features_a.removeAll(similar_features);
        features_b.removeAll(similar_features);
        
	List<String> unique_features_a = Feature.uniqueFeatures(features_a , features_b);
	List<String> unique_features_b = Feature.uniqueFeatures(features_b, features_a);
        
        double x = PIC(common_features);
	double y = PIC(unique_features_a);
	double z = PIC(unique_features_b);
        double s = PIC(similar_features)/2; //because similar features we get average between the two resources

	if ((x + s + y + z) == 0)
            return 0;
        	
        double sim = ((x + s) / (x + s + y + z)) < 0 ? 0 : ((x + s) / (x + s + y + z));
        return sim;
    }
    
    @Override
    public LdSimilarity getMeasure(){
        return this;
    }
       
    
}
