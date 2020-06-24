/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.conf;

import lds.engine.LdSimilarityEngine;
import lds.measures.weight.WeightMethod;

/**
 *
 * @author Fouad Komeiha
 */
public enum Param {
     LdDatasetMain,
     useIndexes,
     LdDatasetSpecific,
     WeightMethod,
     resourcesCount;
     
     
     
    public static String toString(Param param){
        return param.toString();
    }
}
