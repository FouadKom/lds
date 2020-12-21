/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.feature;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fouad Komeiha
 */
public class Feature {
    private String link , vertex, direction;

    public Feature(String link, String vertex, String direction){
        this.link = link;
        this.direction = direction;
        this.vertex = vertex;

    }

    public Feature(String feature){
        new Feature(this.getLink(feature) , this.getVertex(feature) , this.getDirection(feature) );
    }

    public String getLink(){
        return this.link;

    }

    public String getVertex(){
        return this.vertex;
    }

    public String getDirection(){
        return this.direction;
    }

    public static String getLink(String s){
      String string[] =  s.split("\\|");
      if(string.length < 3)
        return null;
      return string[0].trim();
    }

    public static String getVertex(String s){
          String string[] =  s.split("\\|");
          if(string.length < 3)
            return null;
          return string[1].trim();
    }

    public static String getDirection(String s){
        String string[] =  s.split("\\|");
        if(string.length < 3)
            return null;
        return string[2].trim();
    }

    public static List<String> uniqueFeatures(List<String> a, List<String> b) {
        List<String> result = new ArrayList<>(a);
        
        for (String f : b) {
            if (result.contains(f)) {
                result.remove(f);
            }
        }
        
        return result;
    }
    
    public static List<String> commonFeatures(List<String> a, List<String> b){
        List<String> result = new ArrayList<>(a);
        
        result.retainAll(b);
        
        return result;
    }

    @Override
    public String toString(){
        return this.link + "|" + this.vertex + "|" + this.direction;
    }

    @Override
    public boolean equals(Object o){
        if(o == this){
            return true;
        }

        if (!(o instanceof Feature)) { 
            return false; 
        } 

        Feature feature = (Feature) o;
        if(feature.getDirection().equals(this.direction) && feature.getVertex().equals(this.vertex) && feature.getLink().equals(this.link))
            return true;

        return false;
    }
        
        
    }