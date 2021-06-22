/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.picss;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lds.LdManager.PicssLdManager;
import lds.LdManager.ontologies.Ontology;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.feature.Feature;
import lds.resource.R;
import ldq.*;
import lds.measures.LdSimilarity;
import lds.utility.Utility;

/**
 * @author Fouad Komeiha
 */
public class PICSS implements LdSimilarity {
    protected PicssLdManager ldManager;
    protected boolean useIndexes;
    protected int NumberOfResources;

    public PICSS(Config config) throws Exception {
        if (config.getParam(ConfigParam.LdDatasetMain) == null || config.getParam(ConfigParam.useIndexes) == null || config.getParam(ConfigParam.resourcesCount) == null)
            throw new Exception("Some configuration parameters missing");

        this.ldManager = new PicssLdManager((LdDataset) config.getParam(ConfigParam.LdDatasetMain), (Boolean) config.getParam(ConfigParam.useIndexes));
        this.useIndexes = (Boolean) config.getParam(ConfigParam.useIndexes);
        this.NumberOfResources = (Integer) config.getParam(ConfigParam.resourcesCount);
    }

    @Override
    public void closeIndexes() {
        //load prefixes and namespaces index
        Ontology.closeIndexes();
        if (useIndexes) {
            ldManager.closeIndexes();
        }

    }


    @Override
    public void loadIndexes() throws Exception {
        if (useIndexes) {
            ldManager.loadIndexes();
        }
        Ontology.loadIndexes();
    }


    @Override
    public double compare(R a, R b) {
        try{
            double sim = 0;
            sim = PICSS(a, b);
            return sim;
        } catch (Exception ex) {
                Logger.getLogger(PICSS.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
        }

    }

    private double PICSS(R a, R b) {

        List<String> features_a = ldManager.getFeatures(a);
        List<String> features_b = ldManager.getFeatures(b);

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


    protected double PIC(List<String> F) {
        double s = 0.0;

        for (String f : F) {

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
            count = ldManager.getIngoingFeatureFrequency(property, resource);
        }

        if (direction.equals("Out")) {
            count = ldManager.getOutgoingFeatureFrequency(property, resource);
        }


        return count;

    }

    @Override
    public LdSimilarity getMeasure() {
        return this;
    }


}
