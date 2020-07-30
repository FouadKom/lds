/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.config;

/**
 *
 * @author Fouad Komeiha
 */
public enum ConfigParam {
     /* Common for all measures */
     LdDatasetMain, //For the main dataset which will be used to query data related to compared reources
     useIndexes, //Boolean specifiying wether indexes are used or not
    
     /* Specific for weighted measures: WLDSD_cw, WTLDSD_cw, WResim, and WTresim */
     LdDatasetSpecific, //For a specific dataset used by the weight class to calculate the weight of certain predicates
     WeightMethod, //Weighting method/algorithim to be used
     
     /* PICSS and EPICS specific*/
     resourcesCount,  //Number of resources in the dataset, used for PICSS and EPICS measures
     
     /* LODS and its submeasures specific*/
     dataAugmentation, //Augment data for similarity calculataion using LODS or its submeasures
     ontologyList, //list of ontologies required to be queried for similairty using LODS or its submeasures
     sup,
     sub;
     
}
