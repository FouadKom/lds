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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.indexing.LdIndex;
import lds.indexing.LdIndexer;
import static lds.measures.epics.Utility.getDirection;
import static lds.measures.epics.Utility.getLink;
import static lds.measures.epics.Utility.getVertex;
import lds.resource.R;
import sc.research.ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class EpicsLdManager extends PicssLdManager{
    
    private static final String baseDirectory = System.getProperty("user.dir") + "\\Indexes\\EPICS";
    private boolean useIndex;    
    private LdIndexer manager;
    
    public EpicsLdManager(LdDataset dataset, boolean useIndex) throws Exception {
        super(dataset, useIndex);
        this.useIndex = useIndex;
    }
    
    @Override
    public List<String> getFeatures(R a) {
        List<String> IngoingStrings = new ArrayList<>();
        List<String> OutgoingStrings = new ArrayList<>();
        List<String> features_a = new ArrayList<>();
        LdIndex featuresMapIndex = null;
        
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
                   
        /*Map<String , List<String>> map = createFeaturesMap(features_a);
        
        featuresMapIndex = loadFeaturesIndex(a);
        
        featuresMapIndex.addMap(map);
        
        closeFeaturesIndex(featuresMapIndex);*/
        
        return features_a;
    }
    
    @Override
    public void loadIndexes() throws Exception{
        super.loadIndexes();
        manager = LdIndexer.getManager();
    }
    
    
    public LdIndex loadFeaturesIndex(R a) { 
        LdIndex featuresMapIndex = null;
        try {
            
            featuresMapIndex = manager.loadIndex(getIndexPath(a)); 
            
        } 
        catch (Exception ex) {
            Logger.getLogger(EpicsLdManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return featuresMapIndex;
    }
    
    @Override
    public void closeIndexes(){
        super.closeIndexes();       
        
    }
    
    public void closeFeaturesIndex(LdIndex featuresMapIndex) {
        manager.closeIndex(featuresMapIndex);
    }
    

    private String createDirectory(R a , String fileName ) throws IOException{
               
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
    
    private static String getPath(String path , String fileName){
        String s =  path + "/" + fileName;
        
        s = s.replace("://" , "\\");
        
        int len = s.length();
        
        StringBuilder sb = new StringBuilder(len);
        
        sb.append(baseDirectory).append("\\");
        
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i); 
            
            if (ch > ' ' && ch < 0x7F && ch != '.' && ch != ':' && ch != '#' && ch != '*' && ch != '?' && ch != '<' && ch != '>' && ch != '|' && ch != '*' && ch != '\\') {
                sb.append(ch);
            }
            
            if( ( ch == '.' && i!= 0)  || ch == '\\'){
                sb.append('/');
            }
            
        }
        
        sb.append(".txt");
        
        return sb.toString();
        
    }
    
    
    public static String getPath(R a , String fileName ){
        return getPath(a.toString() , fileName);
    }
    
    public static String getIndexPath(R a){
        return getPath(a.getNamespace() , a.getLocalName()).replace(".txt" , ".db");       
    }
    
    
    private void writeValues(List<String> features , R a) throws IOException{  
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
                    File file = new File(directory);
                    long lstModified = file.lastModified();
                    Date date = new Date();
                    long currentTime = date.getTime();
                    
                    if(currentTime - lstModified <= 8.64e+7 && file.length() > 0)
                        //file is new and no need to re-write
                        return;
                    
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
    
    
    private Map<String , List<String>> createFeaturesMap(List<String> features){
        List<String> list = features;
        String link_a , node_a , direction_a;
        Map<String , List<String>> map = new HashMap<>();
        List<String> nodes = new ArrayList<>();
        
        for(String feature : list){
           link_a = getLink(feature);
           direction_a = getDirection(feature);
           node_a = getVertex(feature);

           String key = link_a+"|"+direction_a;

           nodes.add(node_a);

           for(String feature2 : list){
               link_a = getLink(feature2);
               direction_a = getDirection(feature2);
               node_a = getVertex(feature2);

               if(key.equals(link_a+"|"+direction_a)){
                   nodes.add(node_a);
               }
           }

           map.put(key, nodes);            
        }
        
        return map;
    }
    
    
}
