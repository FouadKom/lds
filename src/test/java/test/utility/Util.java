/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lds.resource.R;

public class Util {
        
    public static SplitedList splitList(List<R> list){
            
            int wholeSize = list.size();
            int halfSize = list.size() / 2;
            
            List<R> firstPart = new ArrayList<>();
            List<R> secondPart = new ArrayList<>();
            
            for(int i = 0 ; i < halfSize ; i++){
                firstPart.add( list.get(i) );
            }
            
            for(int i = halfSize ; i < wholeSize ; i++){
                secondPart.add( list.get(i) );
            }
            
            
            return new SplitedList(firstPart , secondPart);
            
        }
        
        
        public static void listFilesForFolder(File folder , boolean traverseSubDirectory) {
                for (final File fileEntry : folder.listFiles()) {
                    if (fileEntry.isDirectory() && traverseSubDirectory) {
                        Util.listFilesForFolder(fileEntry , traverseSubDirectory);
                    } 
                    else if (fileEntry.isDirectory() && ! traverseSubDirectory) {
                        continue;
                    }
                    else {
                        System.out.println(fileEntry.getPath());
                    }
                }
        }
        
        public static void DeleteFilesForFolder(File folder , boolean traverseSubDirectory) {
                for (final File fileEntry : folder.listFiles()) {
                    if (fileEntry.isDirectory() && traverseSubDirectory) {
                        Util.DeleteFilesForFolder(fileEntry , traverseSubDirectory);
                    }
                    else if (fileEntry.isDirectory() && ! traverseSubDirectory) {
                        continue;
                    }
                    else {
                        fileEntry.delete();
                    }
                }
        }
        
        
        public static class SplitedList{
            private List<R> firstList;
            private List<R> secondList;
            
            public SplitedList(){
                firstList = new ArrayList<>();
                secondList = new ArrayList<>();
            }
            
            public SplitedList(List<R> list1 , List<R> list2){
                firstList = list1;
                secondList = list2;
            }
            
            public List<R> getFirstList(){
                return this.firstList;
            }
            
            public List<R> getSecondList(){
                return this.secondList;
            }
    
        }
       

}
