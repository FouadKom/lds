package lds.indexing;

import java.util.ArrayList;
import java.util.HashMap;
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
import sc.research.ldq.LdDataset;

public class LdIndexer {
    
      String indexFilePath = "";
	DB db = null;

	public LdIndexer(String filePath) throws Exception {
            this.indexFilePath = filePath;
            load(this.indexFilePath);
	}
        
        //Used for reflection in LdSimilarityEngine
        public LdIndexer(){
        }

	public void load(String filePath) throws Exception {
            
            if(Utility.checkPath(filePath)){
                this.indexFilePath = filePath;
                db = DBMaker.fileDB(this.indexFilePath)
                        .fileChannelEnable()
                        .make();
            }
            else
               throw new Exception("Invalid filepath specified");
 
        }
        
        public void reload() throws Exception {
		if (!this.indexFilePath.isEmpty())
			load(this.indexFilePath);
		else
                    throw new Exception("no index file has been specified");
	}           
       

	public void addList(String key, List<String> values) {

            NavigableSet<Object[]> multimap = db.treeSet("index")
                            .serializer(new SerializerArrayTuple(Serializer.STRING, Serializer.STRING))
                            .counterEnable()
                            .createOrOpen();

            for (int i = 0; i < values.size(); i++) {

                    multimap.add(new Object[] { key, values.get(i) });
            }
            db.commit();

	}
             
        
	public void addValue(String key, String value) {
            
            HTreeMap<String, String> map = db.hashMap("index2", Serializer.STRING, Serializer.STRING)
            .counterEnable()
            .createOrOpen();

            map.put(key , value);
	}
        
        public String getValue(String key){
              HTreeMap<String, String> map = db.hashMap("index2", Serializer.STRING, Serializer.STRING)
                .counterEnable()
                .createOrOpen();

              return map.get(key);
        }
        

	public List<String> getList(String key) {

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

	public void close() {
		db.close();
	}
        
        
        public static List<String> getListFromIndex(LdDataset dataset , LdIndexer indexName , String key , String methodPath , Object... args) {
            
            List<String> data = indexName.getList(key);
            if(data != null ){
                return data;
            }

            else{
                updateIndexSet(dataset , indexName , key , methodPath , args);
                return getListFromIndex(dataset , indexName , key , methodPath , args);
            }
           
        }
        
        
        public static int getIntegerFromIndex(LdDataset dataset , LdIndexer indexName , String key , String methodPath , Object... args) {
                
            String data = indexName.getValue(key);
            
            if(data != null && ! data.equals("-1")){
                return Integer.parseInt(data);
            }
            else if(data != null && data.equals("-1")){
                return 0;
            }
            else{
                updateIndexTree(dataset , indexName , key , methodPath , args);
                return getIntegerFromIndex(dataset , indexName , key , methodPath , args);
            }

        }
        
        public static double getDoubleFromIndex(LdDataset dataset , LdIndexer indexName , String key , String methodPath , Object... args) {
                
            String data = indexName.getValue(key);
            
            if(data != null && ! data.equals("-1")){
                return Double.parseDouble(data);
            }
            else if(data != null && data.equals("-1")){
                return 0;
            }
            else{
                updateIndexTree(dataset , indexName , key , methodPath , args);
                return getDoubleFromIndex(dataset , indexName , key , methodPath , args);
            }

        }
        
        //Used only by Weight class
        public static double getDoubleFromIndex(LdIndexer indexName , String key , String methodPath , Object... args) {
            
            String data = indexName.getValue(key);
            
            if(data != null){
                return Double.parseDouble(data);
            }
            
            else{
                updateIndexTree(indexName , key , methodPath , args);
                return getDoubleFromIndex(indexName , key , methodPath , args);
            }

        }
        
        public static boolean getBooleanFromIndex(LdDataset dataset , LdIndexer indexName , String key , String methodPath , Object... args) {
                
            String data = indexName.getValue(key);
            
            if(data != null && ! data.equals("-1")){
                return Boolean.parseBoolean(data);
            }
            else if(data != null && data.equals("-1")){
                return false;
            }
            else{
                updateIndexTree(dataset , indexName , key , methodPath , args);
                return getBooleanFromIndex(dataset , indexName , key , methodPath , args);
            }

        }
        
        public static void updateIndexSet(LdDataset dataset , LdIndexer indexName, String key , String methodPath , Object... args){
 
            String classPath = Utility.getClassPath(methodPath);
            String methodName = Utility.getMethodName(methodPath);
            Object returnedItem = Utility.executeMethod(dataset , classPath , methodName , args);
            
            try{
                if(returnedItem != null){
                        indexName.addList(key , (List<String>)  returnedItem);
                }

                else if(returnedItem == null){

                    List<String> list = new ArrayList<>();
                    list.add("-1");
                    indexName.addList(key , list);

                }
            }
            catch(Exception ex){
                Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
                indexName.close();
            }
                          
        }

        public static void updateIndexTree(LdDataset dataset , LdIndexer indexName, String key, String methodPath , Object... args) {      

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

                    indexName.addValue(key , value);                    
                }

                else if(returnedItem == null){
                    indexName.addValue(key , "-1");
                }
            }
            catch(Exception ex){
                Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
                indexName.close();
            }
        }
        
        
        //Used only for Weight class
        public static void updateIndexTree(LdIndexer indexName, String key, String methodPath , Object... args) {      
            String classPath = Utility.getClassPath(methodPath);
            String methodName = Utility.getMethodName(methodPath);
            Utility.executeMethod(classPath , methodName , args);
            
        }
        
        

}
