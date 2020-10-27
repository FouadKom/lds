/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.picss;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.LdManager.LdsdLdManager;
import lds.LdManager.ontologies.Ontology;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.measures.LdSimilarity;
import lds.measures.weight.Weight;
import lds.measures.weight.WeightMethod;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import slib.graph.model.impl.repo.URIFactoryMemory;

/**
 *
 * @author Fouad Komeiha
 */
public class WPICS extends PICSS{
    protected Weight weight;
    protected LdsdLdManager SpecificLoader;
    protected LdsdLdManager MainLoader;
    
    public WPICS(Config config) throws Exception {
        super(config);
        this.SpecificLoader = new LdsdLdManager((LdDataset) config.getParam(ConfigParam.LdDatasetSpecific) , (Boolean) config.getParam(ConfigParam.useIndexes) );
        this.MainLoader = new LdsdLdManager((LdDataset) config.getParam(ConfigParam.LdDatasetMain) , (Boolean) config.getParam(ConfigParam.useIndexes) );
        this.weight = new Weight((WeightMethod)config.getParam(ConfigParam.WeightMethod) , MainLoader , SpecificLoader , (Boolean)config.getParam(ConfigParam.useIndexes));
    }
    
    @Override
    public double compare(R a, R b) {
        try{
            double sim = 0;
            sim = WPICS(a, b);
            return sim;
        } catch (Exception ex) {
                Logger.getLogger(PICSS.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
        }

    }
    
    private double WPICS(R a, R b) {

        List<String> features_a = ldManager.getFeatures(a);
        List<String> features_b = ldManager.getFeatures(b);

        if (features_a.isEmpty() && features_b.isEmpty())
            return 0;

        List<String> common_features = Utility.commonFeatures(features_a, features_b);

        if (common_features == null || common_features.isEmpty())
            return 0;
        
        features_a.removeAll(common_features);
        features_b.removeAll(common_features);

        List<String> unique_features_a = Utility.uniqueFeatures(features_a, features_b);
        List<String> unique_features_b = Utility.uniqueFeatures(features_b, features_a);

        double x1 = PIC(common_features , a);
        double x2 = PIC(common_features , b);
        
        double x = (x1 + x2)/2;
        double y = PIC(unique_features_a , a);
        double z = PIC(unique_features_b , b);

        if ((x + y + z) == 0)
            return 0;

        double sim = (x / (x + y + z)) < 0 ? 0 : (x / (x + y + z));
        return sim;
    }
    
    protected double PIC(List<String> F , R res) {
        double s = 0.0;

        for (String f : F) {

            Phi phi_ = phi(f , res);
            if (phi_ != null) {
                double x = Utility.log2(phi_ .getValue() / this.NumberOfResources);
                double log = - (x * phi_.getWeight());
                s = s + log;
            }

        }

        return s;
    }
    
   
//    protected double phi(String feature , R res) {

//        int count = 0;
//        
//        double weight = 0;
//
//        String direction = Utility.getDirection(feature);
//        if (direction == null)
//            return count;
//
//        String property = Utility.getLink(feature);
//        if (property == null)
//            return count;
//
//        String resource = Utility.getVertex(feature);
//        if (resource == null)
//            return count;
//        
//        weight = this.weight.linkWeight(URIFactoryMemory.getSingleton().getURI(property) , res, new R(resource));
//
//        if (direction.equals("In")) {
//            count = ldManager.getIngoingFeatureFrequency(property, resource);
//        }
//
//        if (direction.equals("Out")) {
//            count = ldManager.getOutgoingFeatureFrequency(property, resource);
//        }
//
//
//        return count;
//    }
    
    protected Phi phi(String feature , R res) {    
        int count = 0;
        
        double weight = 0;

        String direction = Utility.getDirection(feature);
        if (direction == null)
            return null;

        String property = Utility.getLink(feature);
        if (property == null)
            return null;

        String resource = Utility.getVertex(feature);
        if (resource == null)
            return null;
        
        weight = this.weight.linkWeight(URIFactoryMemory.getSingleton().getURI(Ontology.decompressValue(property)) , res, new R(Ontology.decompressValue(resource)));

        if (direction.equals("In")) {
            count = ldManager.getIngoingFeatureFrequency(property, resource);
        }

        if (direction.equals("Out")) {
            count = ldManager.getOutgoingFeatureFrequency(property, resource);
        }


        return new Phi(count , weight);          

    }
    

    @Override
    public LdSimilarity getMeasure() {
        return this;
    }
    
    private class Phi {
       private double value;
       private double weight;
       
       private Phi(double value, double weight){
           this.value = value;
           this.weight = weight;
       }
       
       public double getValue(){
           return this.value;
       }
       
       public double getWeight(){
           return this.weight;
       }
    
    }
    
}
