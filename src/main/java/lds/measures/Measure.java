/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures;

/**
 *
 * @author Fouad Komeiha
 */
public enum Measure {
    Resim ,
    TResim ,
    WResim ,
    WTResim ,
    LDSD_d ,
    LDSD_dw ,
    LDSD_i ,
    LDSD_iw ,
    LDSD_cw ,
    TLDSD_cw ,
    WLDSD_cw ,
    WTLDSD_cw ,
    PICSS ,
    EPICS ,
    LODS ,
    SimI ,
    SimP ,
    SimC ;
    
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
        if(measure == LDSD_d){
            return "lds.measures.ldsd.LDSD_d" ;
        }
        if(measure == LDSD_dw){
            return "lds.measures.ldsd.LDSD_dw" ;
        }
        if(measure == LDSD_i){
            return "lds.measures.ldsd.LDSD_i" ;
        }
        if(measure == LDSD_iw){
            return "lds.measures.ldsd.LDSD_iw" ;
        }
        if(measure == LDSD_cw){
            return "lds.measures.ldsd.LDSD_cw" ;
        }
        if(measure == TLDSD_cw){
            return "lds.measures.ldsd.TLDSD_cw" ;
        }
        if(measure == WLDSD_cw){
            return "lds.measures.ldsd.WLDSD_cw" ;
        }
        if(measure == WTLDSD_cw){
            return "lds.measures.ldsd.WTLDSD_cw" ;
        }
        if(measure == PICSS){
            return "lds.measures.picss.PICSS" ;
        }
        if(measure == LODS){
            return "lds.measures.lods.LODS" ;
        }        
        if(measure == EPICS){
            return "lds.measures.epics.EPICS" ;
        }
        if(measure == SimI){
            return "lds.measures.lods.SimI" ;
        }
        if(measure == SimP){
            return "lds.measures.lods.SimP" ;
        }
        if(measure == SimC){
            return "lds.measures.lods.SimC" ;
        }
        
        return null;
    }
    
    public static String getName(Measure measure){
        return measure.toString();
    }
    
}
