/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.resource;

import lds.resource.R;

/**
 *
 * @author Fouad Komeiha
 */
public class ResourcePair {
    private R firstResource;
    private R secondResource;
    
    public ResourcePair(R r1 , R r2){
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
       return firstResource.getUri().toString() + "," + secondResource.getUri().toString();
    }
    
}
