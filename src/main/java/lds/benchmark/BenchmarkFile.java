/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.benchmark;

import java.io.File;

/**
 *
 * @author Fouad Komeiha
 */
public class BenchmarkFile {
    private String filePath;
    private char separator;
    private char quote;
//    //optional for benchmarks
//    private double minValue;
//    private double maxValue;
    
    public BenchmarkFile(String filePath){
        if(checkPath(filePath))
            this.filePath = filePath;
        
        this.separator = ',';
        this.quote = ' ';
        
    }
    
    public BenchmarkFile(String filePath , char separator , char quote){
        if(checkPath(filePath))
            this.filePath = filePath;
        this.separator = separator;
        this.quote = quote;
    }
    
    public void setSeparator(char separator){
        this.separator = separator;
    }
    
    public void setQuote(char quote){
        this.quote = quote;
    }
    
    public char getSeparator(){
        return this.separator;
    }
    
    public char getQuote(){
        return this.quote;
    }
    
//    public void setMinValue(double value){
//        this.minValue = value;
//    }
//    
//    public void setMaxValue(double value){
//        this.maxValue = value;
//    }
//    
//    public double getMinValue(){
//        return this.minValue;
//    }
//    
//    public double getMaxValue(){
//        return this.maxValue;
//    }
    
   
    
    public String getFilePath(){
        return this.filePath;
    }
    
    public boolean checkPath(String path) {
        File file = new File(path);

            if (!file.isDirectory()) {
                file = file.getParentFile();
                if (!file.exists()) {
                    File dir = new File(file.getPath());
                    return dir.mkdirs();
                }
            }

        return true;
    }
    
    public boolean exits(){
        File file = new File(filePath);
        return file.exists();
    }

    
    
}
