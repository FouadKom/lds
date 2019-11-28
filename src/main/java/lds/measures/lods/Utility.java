/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.lods;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fouad Komeiha
 */
public class Utility {
    
    public static double TverskySimilarity_mod(List<String> features_a, List<String> features_b) {
		List<String> a = uniqueFeatures(features_a, features_b);
		int features_a_not_b = a.size();
                
		List<String> b = uniqueFeatures(features_b, features_a);
		int features_b_not_a = b.size();
                
		List<String> c = commonFeatures(features_a, features_b);
		int commonFeatures = c.size();
                
		double similarity = 0.0;
                
		if (commonFeatures == 0)
			similarity = 0;
		else
			similarity = (double) commonFeatures / ( (double) commonFeatures + (double) features_a_not_b + (double) features_b_not_a );

		return similarity;
	}
    
    private static List<String> uniqueFeatures(List<String> features_a, List<String> features_b) {
            List<String> uniqueList = new ArrayList<>();
            for (String feature_a : features_a) {
                    if (!features_b.contains(feature_a)) {
                            uniqueList.add(feature_a);
                    }

            }
            return uniqueList;
    }

    private static List<String> commonFeatures(List<String> features_a, List<String> features_b) {
            List<String> commonList = new ArrayList<>();
            for (String feature_a : features_a) {
                    if (features_b.contains(feature_a)) {
                            commonList.add(feature_a);
                    }

            }
            return commonList;
    }
    
}
