/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures;

import lds.dataset.DBpediaChapter;
import lds.dataset.LdDatasetCreator;
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
        
        if(Measure.getName(measure).equals("PICSS"))
            config.addParam("resourcesCount" , 2350906);
        
            
        return config;
    }
    
    
    

}
