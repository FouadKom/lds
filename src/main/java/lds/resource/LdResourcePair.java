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
       return firstResource.getUri().toString() + " , " + secondResource.getUri().toString();
    }
    
    public String toString(char separator){
       return firstResource.getUri().toString() + " " + separator + " " + secondResource.getUri().toString();
    }
    
    public boolean equals(LdResourcePair pair){
        return this.firstResource.equals(pair.getFirstresource()) && this.secondResource.equals(pair.getSecondresource());
    }
    
}
