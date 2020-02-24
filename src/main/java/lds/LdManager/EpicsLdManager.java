/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lds.resource.R;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class EpicsLdManager extends PicssLdManager{
    
    private static final String baseDirectory = System.getProperty("user.dir") + "\\Indexes\\EPICS";
    
    public EpicsLdManager(LdDataset dataset, boolean useIndex) throws Exception {
        super(dataset, useIndex);
    }
    
    @Override
    public List<String> getFeatures(R a) {
        List<String> IngoingStrings = new ArrayList<>();
        List<String> OutgoingStrings = new ArrayList<>();
        List<String> features_a = new ArrayList<>();
        
        IngoingStrings = getIngoingFeatures(a);
        OutgoingStrings = getOutgoingFeatures(a);
        
        try{
            writeValues(IngoingStrings , a);
            writeValues(OutgoingStrings , a);
        }
        catch(IOException e){
            
        }
        
        Optional.ofNullable(IngoingStrings).ifPresent(features_a::addAll);
        Optional.ofNullable(OutgoingStrings).ifPresent(features_a::addAll);
        
        return features_a;
    }

    public String createDirectory(R a , String fileName ) throws IOException{
               
        String directory = getPath(a , fileName);
        
        File f_a = new File(directory);
        
        if(f_a.exists()){
            return directory;
        }
        
        
        else if(f_a.getParentFile().mkdirs()){
            if( f_a.createNewFile() )
                return directory;
            
            else {
                System.out.println("Unable to create file: \"" + directory + "\"");
                return directory;
            }
        }
        
        
        else {
            System.out.println("Unable to create file: \"" + directory + "\"");
            return directory;
        }
    }
    
    public static String getPath(R a , String fileName ){
        String s =  a.toString() + "/" + fileName;
        
        int len = s.length();
        
        StringBuilder sb = new StringBuilder(len);
        
        sb.append(baseDirectory).append("\\");
        
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i); 
            
            if (ch > ' ' && ch < 0x7F && ch != '.' && ch != ':' && ch != '#' && ch != '/' && ch != '*' && ch != '?' && ch != '<' && ch != '>' && ch != '|' && ch != '*' && ch != '\\' && ch != '/') {
                sb.append(ch);
            }
            
            if( ( ch == '.' && i!= 0)  || ch == '/'){
                sb.append('\\');
            }
            
        }
        
        sb.append(".txt");
        
        return sb.toString();
    }
    
    
    public void writeValues(List<String> features , R a) throws IOException{  
        String directory = null;
        
        for(String f : features){
            
            String s[] = f.split("\\|");            
            String fileName  = s[0] + "|" + s[2];
            String value = s[1];
            
            try{
                directory = createDirectory(a , fileName);
            }

            catch(IOException e){
                System.out.println("Unable to create file: \"" + directory + "\"");
                System.out.println(e.toString());
            }

            try {
                if(directory != null){
                    FileWriter writer = new FileWriter(directory);
                    writer.write(value);
                    writer.write(System.getProperty("line.separator"));
                    writer.close();
                }
                
                else {
                    System.out.println("Unable to create file: \"" + directory + "\"");
                }
            }

            catch(IOException e){
                System.out.println("Unable to write features to file: \"" + directory + "\"");
                System.out.println(e.toString());
            }           
            
        }
        
    }    
    
    
}
