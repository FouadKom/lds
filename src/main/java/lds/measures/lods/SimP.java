/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.lods;

import java.util.List;
import lds.LdManager.SimPLdManager;
import lds.LdManager.ontologies.Ontology;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.feature.Feature;
import lds.resource.R;
import lds.measures.LdSimilarity;
import lds.measures.lods.ontologies.O;
import lds.utility.Utility;
import ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class SimP implements LdSimilarity{
    private boolean useIndexes;
    private List<O> ontologyList;
    private List<O> commonOntologies;
    private boolean dataAugmentation;
    private SimPLdManager simPldManager;
    private int NumberOfResources;
    
    public SimP(Config config) throws Exception {

        if (config.getParam(ConfigParam.LdDatasetMain) == null || config.getParam(ConfigParam.useIndexes) == null || config.getParam(ConfigParam.resourcesCount) == null)
            throw new Exception("Some configuration parameters missing");

        this.simPldManager = new SimPLdManager((LdDataset) config.getParam(ConfigParam.LdDatasetMain) , (Boolean) config.getParam(ConfigParam.useIndexes) );
        this.useIndexes = (Boolean) config.getParam(ConfigParam.useIndexes);
        this.NumberOfResources = (Integer) config.getParam(ConfigParam.resourcesCount);
        if(config.getParam(ConfigParam.dataAugmentation) == null)
            this.dataAugmentation = false;
        else
            this.dataAugmentation = (Boolean) config.getParam(ConfigParam.dataAugmentation);
    }

    @Override
    public double compare(R a, R b) {
        if(a.equals(b))
            return 1;
        
        return simp(a , b);     
        
    }
    
    protected double simp(R a, R b) {
        if(a.equals(b))
            return 1;
        
        List<String> features_a = simPldManager.getFeatures(a);
        List<String> features_b = simPldManager.getFeatures(b);

        if (features_a.isEmpty() && features_b.isEmpty())
            return 0;

        List<String> common_features = Feature.commonFeatures(features_a, features_b);

        if (common_features == null || common_features.isEmpty())
            return 0;
        
        features_a.removeAll(common_features);
        features_b.removeAll(common_features);

        List<String> unique_features_a = Feature.uniqueFeatures(features_a, features_b);
        List<String> unique_features_b = Feature.uniqueFeatures(features_b, features_a);

        double x = PIC(common_features);
        double y = PIC(unique_features_a);
        double z = PIC(unique_features_b);

        if ((x + y + z) == 0)
            return 0;

        double sim = (x / (x + y + z)) < 0 ? 0 : (x / (x + y + z));
        return sim;
        
    }
    
    @Override
    public void closeIndexes() {
        //load prefixes and namespaces index
        Ontology.closeIndexes();
        if (useIndexes) {
            simPldManager.closeIndexes();
        }

    }


    @Override
    public void loadIndexes() throws Exception {
        if (useIndexes) {
            simPldManager.loadIndexes();
        }
        Ontology.loadIndexes();
    }
    

    @Override
    public LdSimilarity getMeasure() {
        return this;
    }
    
    protected double PIC(List<String> properties) {
        double s = 0.0;

        for (String f : properties) {

            double phi_ = phi(f);
            if (phi_ != 0) {
                double x = Utility.log2(phi_ / this.NumberOfResources);
                double log = -x;
                s = s + log;
            }

        }

        return s;
    }
    
    protected double phi(String feature) {

        int count = 0;

        String direction = Feature.getDirection(feature);
        if (direction == null)
            return count;

        String property = Feature.getLink(feature);
        if (property == null)
            return count;

        String resource = Feature.getVertex(feature);
        if (resource == null)
            return count;


        if (direction.equals("In")) {
            count = simPldManager.getIngoingFeatureFrequency(property);
        }

        if (direction.equals("Out")) {
            count = simPldManager.getOutgoingFeatureFrequency(property);
        }


        return count;

    }
    
}
