/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.resource;

/**
 *
 * @author Fouad Komeiha
 */
public class LdResult {
    private LdResourceTriple triple;
    private double duration;
    
    public LdResult(LdResourceTriple triple , double duration){
        this.triple = triple;
        this.duration = duration;
    }
    
    public LdResult(R firstResource , R secondResource , double result , double duration){
        this.triple = new LdResourceTriple(firstResource , secondResource , result);
        this.duration = duration;
    }
    
    public LdResourceTriple getResourceTriple(){
        return this.triple;
    }
    
    public double getDuration(){
        return this.duration;
    }
    
    @Override
    public String toString(){
        return this.triple.toString() + " , " + Double.toString(duration);
    }
    
    
    public String toString(char separator){
        return this.triple.toString(separator) + " " + separator + " " + Double.toString(duration);
    }
    
    public String toString(char separator , char quote){
        return this.triple.toString(separator , quote) + " " + separator + " " + Double.toString(duration);
    }
    
}
