/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.conf;

import java.util.Map;
import lds.dataset.DBpediaChapter;
import lds.dataset.LdDatasetCreator;
import lds.measures.Measure;
import lds.measures.weight.WeightMethod;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;
/**
 *
 * @author Fouad Komeiha
 */
public class LdConfFactory {
    
    public static Conf createDeafaultConf(Measure measure){
        Conf config = new Conf();
        
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , LdDatasetCreator.getDBpediaDataset(DBpediaChapter.En));
        
        if(Measure.getName(measure).equals("PICSS") || Measure.getName(measure).equals("EPICS"))
            config.addParam("resourcesCount" , 2350906);
        
        if(Measure.getName(measure).contains("W")){
            System.out.println("Using default conf creator for weighted similarity measures requires providing specific datset");
            config.addParam("WeightMethod" , WeightMethod.ITW);
        }
            
        return config;
    }
    
    public static Conf createConf(Object... params){
        Conf config = new Conf();
        String key = null;
        Boolean useIndex = null;
        LdDataset dataset = null;
        WeightMethod weightMethod = null;
        int val = 0;
        int i =0;
        
//        for (int i = 0; i < params.length; i++) {
        while(i < params.length){
                        
            if (params[i] instanceof Param) {
                
                key = params[i].toString();                 
                        
                if (params[i+1] instanceof Boolean && key != null && ! key.isEmpty() && key.equals("useIndexes")) {
                        useIndex = (Boolean) params[i+1];
                }

                else if(params[i+1] instanceof Integer && key != null && ! key.isEmpty() && key.equals("resourcesCount")){
                    val = (Integer) params[i+1];
                }
                
                else if(params[i+1] instanceof LdDataset && key != null && ! key.isEmpty() && key.contains("LdDataset")){
                    dataset = (LdDataset) params[i+1];
                }
                
                else if(params[i+1] instanceof WeightMethod && key != null && ! key.isEmpty() && key.equals("WeightMethod")){
                    weightMethod = (WeightMethod) params[i+1];
                }
                
                if(key != null){
                    if(key.equals("resourcesCount")){
                        config.addParam(key , val);
                    }
                    else if(key.contains("LdDataset")){
                        config.addParam(key , dataset);
                    }
                    else if(key.equals("WeightMethod")){
                        config.addParam(key , weightMethod);
                    }
                    else if(key.equals("useIndexes")){
                        config.addParam(key , useIndex);
                    }
                }
            }
            
            i+= 2;    
            
        }
            
        return config;
    }
    
    public static Conf createConf(Map<Param , Object> params){
        Conf config = new Conf();
        
        for (Map.Entry<Param , Object> entry : params.entrySet()){
            config.addParam(entry.getKey().toString() , entry.getValue());
        }
            
        return config;
    }
    
    
    

}
