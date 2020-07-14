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
public class LdResourcePair {
    private R firstResource;
    private R secondResource;
    
    public LdResourcePair(R r1 , R r2){
        this.firstResource = r1;
        this.secondResource = r2;
    }
    
    public R getFirstresource(){
        return this.firstResource;
    }
    
    public R getSecondresource(){
        return this.secondResource;
    }
    
    @Override
    public String toString(){
       return firstResource.toString() + " , " + secondResource.toString();
    }
    
    public String toString(char separator){
       return firstResource.toString() + " " + separator + " " + secondResource.toString();
    }
    
    public String toString(char separator , char quote){
       return quote + firstResource.toString() + quote + " " + separator + " " + quote + secondResource.toString() + quote;
    }
    
    
    @Override
    public boolean equals(Object o){
        if(o == this){
            return true;
        }
        
        if (!(o instanceof LdResourcePair)) { 
            return false; 
        } 
        
        LdResourcePair pair = (LdResourcePair) o;
                return this.firstResource.equals(pair.getFirstresource()) && this.secondResource.equals(pair.getSecondresource());
    }
}
