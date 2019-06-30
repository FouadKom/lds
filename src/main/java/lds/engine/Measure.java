/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.engine;

/**
 *
 * @author Fouad Komeiha
 */
public enum Measure {
    Resim ,
    TResim ,
    WResim ,
    WTResim ,
    LDSD ,
    PICSS ,
    LODS ;
    
    public static String getPath(Measure measure){
        if(measure == Resim){
            return "lds.measures.resim.Resim" ;
        }
        if(measure == TResim){
            return "lds.measures.resim.TResim" ;
        }
        if(measure == WResim){
            return "lds.measures.resim.WResim" ;
        }
        if(measure == WTResim){
            return "lds.measures.resim.WTResim" ;
        }
        if(measure == LDSD){
            return "lds.measures.ldsd.LDSD" ;
        }
        if(measure == PICSS){
            return "lds.measures.picss.PICSS" ;
        }
        if(measure == LODS){
            return "lds.measures.lods.LODS" ;
        }
        
        return null;
    }
    
}
