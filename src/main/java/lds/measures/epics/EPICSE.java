/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.epics;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.LdManager.EpicsLdManager;
import lds.indexing.LdIndex;
import lds.resource.R;
import sc.research.ldq.*;
import slib.utils.i.Conf;
import lds.measures.LdSimilarity;

/**
 *
 * @author Fouad Komeiha
 */
public class EPICSE implements LdSimilarity{
    private EpicsLdManager ldManager;
    private boolean useIndeses;
    private int NumberOfResources;
    private Conf config;
    
    public EPICSE(Conf config) throws Exception{
        if( config.getParam("LdDatasetMain")== null || config.getParam("useIndexes")== null || config.getParam("resourcesCount")== null )
            throw new Exception("Some configuration parameters missing"); 
        
        this.config = config;
        this.ldManager = new EpicsLdManager((LdDataset) config.getParam("LdDatasetMain") , (Boolean) config.getParam("useIndexes") );
        this.useIndeses = (Boolean) config.getParam("useIndexes");
        this.NumberOfResources = (Integer) config.getParam("resourcesCount");
    }

    
    @Override
    public void closeIndexes(){
        if(useIndeses){
            ldManager.closeIndexes();
        }
        
    }
    
    
    @Override
    public void loadIndexes() throws Exception{
        if(useIndeses){
            ldManager.loadIndexes();
        }
    }
    
    
    @Override
    public double compare(R a, R b) {
	double sim = 0;

        try {
            sim= EPICS(a , b);
        } catch (Exception ex) {
            Logger.getLogger(EPICS.class.getName()).log(Level.SEVERE, null, ex);
        }


        return sim;

    }

    private double EPICS(R a , R b) throws Exception {

        List<String> features_a = ldManager.getFeatures(a);
        List<String> features_b = ldManager.getFeatures(b);
        
        if(features_a.isEmpty() &&  features_b.isEmpty())
            return 0;
        
        List<String> common_features = UtilityE.commonFeatures(features_a, features_b);
        
        features_a.removeAll(common_features);
        features_b.removeAll(common_features);
        
        /*LdIndex featuresIndex_a = ldManager.loadFeaturesIndex(a);
        LdIndex featuresIndex_b = ldManager.loadFeaturesIndex(b);
        
        List<String> similar_features = UtilityE.similarFeatures(features_a , features_b , featuresIndex_b , config);        
        
        ldManager.closeFeaturesIndex(featuresIndex_a);
        ldManager.closeFeaturesIndex(featuresIndex_b);*/
        
        List<String> similar_features = UtilityE.similarFeatures(a , b , features_a, features_b , config);
        
        features_a.removeAll(similar_features);
        features_b.removeAll(similar_features);
        
	List<String> unique_features_a = UtilityE.uniqueFeatures(features_a , features_b);
	List<String> unique_features_b = UtilityE.uniqueFeatures(features_b, features_a);
        
        double x = PIC(common_features);
	double y = PIC(unique_features_a);
	double z = PIC(unique_features_b);
        double s = PIC(similar_features)/2; //because similar features we get average

	if ((x + s + y + z) == 0)
            return 0;
        
	return ( (x + s) / (x +  s + y + z));
    }
    
    
    private double PIC(List<String> F) {
	double s = 0.0;
        
	for (String f : F) {
            
            double phi_ = phi(f);
            if (phi_ != 0) {
                double x = Math.log(phi_ / this.NumberOfResources);
                double log = -x;
                s = s + log;
            }
            
	}
        
	return s;
    }
    
 
    
    private double phi(String feature) {

	int count = 0;
        
        String direction = Utility.getDirection(feature);
        String property = Utility.getLink(feature);
        String resource = Utility.getVertex(feature);
        
        if(direction.equals("In")){
            count = ldManager.getIngoingFeatureFrequency(property , resource);
        }
        
        if(direction.equals("Out")){
            count = ldManager.getOutgoingFeatureFrequency(property , resource);
        }

	return count;

    }
    
    @Override
    public LdSimilarity getMeasure(){
        return this;
    }
    
    
    
    
}
