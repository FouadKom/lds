/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.indexing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerArrayTuple;
import ldq.LdDataset;

/**
 *
 * @author Fouad Komeiha
 */
public class LdIndex {
    String indexFilePath = "";
    DB db = null;
    
    public LdIndex(String indexFilePath) throws Exception{
        this.indexFilePath = indexFilePath;
        this.db = load();
        
    }
    
    public DB getDB(){
        return this.db;
    }
    
    public String getDBPath(){
        return this.indexFilePath;
    }
    
    public DB load() throws Exception {
           
        if(Utility.checkPath(indexFilePath)){
            db = DBMaker.fileDB(this.indexFilePath)
                    .fileChannelEnable()
                    .closeOnJvmShutdown()
                    .checksumHeaderBypass()
                    .make();

            return db;
        }
        else{
           throw new Exception("Invalid Index filepath specified. Index not creaed and will not be used");

        }      
    }
    
    public void close() {
	db.close();
    }
    
    public void addMap(Map<String, List<String>> map) {
       for(Map.Entry<String, List<String>> entry : map.entrySet()){
           addList(entry.getKey() , entry.getValue());
       }
    }
    
    
    public  void addList(String key, List<String> values) {

        NavigableSet<Object[]> multimap = db.treeSet("index")
                        .serializer(new SerializerArrayTuple(Serializer.STRING, Serializer.STRING))
                        .counterEnable()
                        .createOrOpen();

        for (int i = 0; i < values.size(); i++) {

                multimap.add(new Object[] { key, values.get(i) });
        }
        db.commit();

    }
             
        
    public  void addValue(String key, String value) {

        HTreeMap<String, String> map = db.hashMap("index2", Serializer.STRING, Serializer.STRING)
        .counterEnable()
        .createOrOpen();

        map.put(key , value);
    }

    public  String getValue(String key){
          HTreeMap<String, String> map = db.hashMap("index2", Serializer.STRING, Serializer.STRING)
            .counterEnable()
            .createOrOpen();

          return map.get(key);
    }


    public  List<String> getList(String key) {

            NavigableSet<Object[]> multimap = db.treeSet("index")
                            .serializer(new SerializerArrayTuple(Serializer.STRING, Serializer.STRING))
                            .counterEnable()                         
                            .createOrOpen();

            Set<Object[]> resultSet = multimap.subSet(new Object[] { key }, // lower interval
                            new Object[] { key, null });// upper interval bound, null is positive infinity

            List<Object[]> resultList = new ArrayList<Object[]>(resultSet); // TOFIX, is it necessary to convert ?!

            if (resultList.isEmpty() || resultList.contains("-1"))
                    return null;

            List<String> result = new ArrayList<String>();

            for (Object[] element : resultList) {
                    result.add(element[1].toString());
            }
            return result;
    }
    
    public String generateRandomKey(int keySize){
        int tries = 0;
        int maxTries = 4;
        String key = Utility.getAlphaNumericString(keySize);
        
        while( getValue(key) != null && getList(key) != null && tries != maxTries) {
            key = Utility.getAlphaNumericString(keySize);
            tries++;
        }
        
        if(tries == maxTries){
            keySize++;
            return generateRandomKey(keySize);
        }
        
        return key;
        
    }
    
    public String generateRandomKey(int keySize , int maxTries){
        int tries = 0;
        String key = Utility.getAlphaNumericString(keySize);
        
        while( getValue(key) != null && getList(key) != null && tries != maxTries) {
            key = Utility.getAlphaNumericString(keySize);
            tries++;
        }
        
        if(tries == maxTries){
            keySize++;
            return generateRandomKey(keySize , maxTries);
        }
        
        return key;
        
    }
    

    public List<String> getListFromIndex(LdDataset dataset, String key , String methodPath , Object... args) {

        List<String> data = this.getList(key);
        if(data != null){
            return data;
        }

        else{
            updateIndexSet(dataset , key , methodPath , args);
            return getListFromIndex(dataset , key , methodPath , args);
        }

    }



    public int getIntegerFromIndex(LdDataset dataset , String key , String methodPath , Object... args) {

        String data = this.getValue(key);

        if(data != null && ! data.equals("-1")){
            return Integer.parseInt(data);
        }
        else if(data != null && data.equals("-1")){
            return 0;
        }
        else{
            updateIndexTree(dataset , key , methodPath , args);
            return getIntegerFromIndex(dataset  , key , methodPath , args);
        }

    }

    public  double getDoubleFromIndex(LdDataset dataset , String key , String methodPath , Object... args) {

        String data = this.getValue(key);

        if(data != null && ! data.equals("-1")){
            return Double.parseDouble(data);
        }
        else if(data != null && data.equals("-1")){
            return 0;
        }
        else{
            updateIndexTree(dataset , key , methodPath , args);
            return getDoubleFromIndex(dataset , key , methodPath , args);
        }

    }

    //Used only by Weight class
    public double getDoubleFromIndex(String key , String methodPath , Object... args) {

        String data = this.getValue(key);

        if(data != null){
            return Double.parseDouble(data);
        }

        else{
            updateIndexTree(key , methodPath , args);
            return getDoubleFromIndex(key , methodPath , args);
        }

    }

    public  boolean getBooleanFromIndex(LdDataset dataset , String key , String methodPath , Object... args) {

        String data = this.getValue(key);

        if(data != null && ! data.equals("-1")){
            return Boolean.parseBoolean(data);
        }
        else if(data != null && data.equals("-1")){
            return false;
        }
        else{
            updateIndexTree(dataset , key , methodPath , args);
            return getBooleanFromIndex(dataset , key , methodPath , args);
        }

    }

    public  synchronized void updateIndexSet(LdDataset dataset , String key , String methodPath , Object... args){

        String classPath = Utility.getClassPath(methodPath);
        String methodName = Utility.getMethodName(methodPath);
        Object returnedItem = Utility.executeMethod(dataset , classPath , methodName , args);

        try{
            if(returnedItem != null){
                    this.addList(key , (List<String>)  returnedItem);
            }

            else if(returnedItem == null){

                List<String> list = new ArrayList<>();
                list.add("-1");
                this.addList(key , list);

            }
        }
        catch(Exception ex){
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
            this.close();
        }          
    }

    public synchronized void updateIndexTree(LdDataset dataset, String key, String methodPath , Object... args) {      

        String classPath = Utility.getClassPath(methodPath);
        String methodName = Utility.getMethodName(methodPath);
        Object returnedItem = Utility.executeMethod(dataset , classPath , methodName , args);

        boolean boolValue = false;
        int intValue = 0;
        double dblValue = 0;
        String value = null;

        try{
            if(returnedItem != null){
                if(returnedItem instanceof Boolean){
                    boolValue = (Boolean) returnedItem;
                    value = Boolean.toString(boolValue);
                }

                if(returnedItem instanceof Integer){
                    intValue = (Integer) returnedItem;
                    value = Integer.toString(intValue);
                }

                if(returnedItem instanceof Double){
                    dblValue = (Double) returnedItem;
                    value = Double.toString(dblValue);
                }

                this.addValue(key , value);                    
            }

            else if(returnedItem == null){
                this.addValue(key , "-1");
            }
        }
        catch(Exception ex){
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
            this.close();
        }
    }


    //Used only for Weight class
    public static synchronized void updateIndexTree(String key, String methodPath , Object... args) {      
        String classPath = Utility.getClassPath(methodPath);
        String methodName = Utility.getMethodName(methodPath);
        Utility.executeMethod(classPath , methodName , args);
    }

    
            
}
