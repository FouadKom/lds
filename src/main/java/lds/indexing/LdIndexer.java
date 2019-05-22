package lds.indexing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.measures.resim.Utility;
import lds.resource.R;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerArrayTuple;
import org.openrdf.model.URI;

public class LdIndexer {

	String indexFilePath = "";
	DB db = null;

	public LdIndexer(String filePath) {
		this.indexFilePath = filePath;
		load(this.indexFilePath);
	}

	public void load(String filePath) {
		this.indexFilePath = filePath;
		db = DBMaker.fileDB(this.indexFilePath)
                            .fileChannelEnable()
                            .make();
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

		if (resultList.isEmpty())
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
        
        public static List<String> getListFromIndex(LdIndexer indexName , String key , String methodName , Object... args) {
            
            List<String> data = indexName.getList(key);
            if(data != null){
                return data;
            }

            else{
                updateIndexSet(indexName , key , methodName , args);
                return getListFromIndex(indexName , key , methodName , args);
            }
           
        }
        
        public static int getIntegerFromIndex(LdIndexer indexName , String key , String methodName , Object... args) {
                
            String data = indexName.getValue(key);
            
            if(data != null && ! data.equals("-1")){
                return Integer.parseInt(data);
            }
            else if(data != null && data.equals("-1")){
                return 0;
            }
            else{
                updateIndexTree(indexName , key , methodName , args);
                return getIntegerFromIndex(indexName , key , methodName , args);
            }

        }
        
        public static boolean getBooleanFromIndex(LdIndexer indexName , String key , String methodName , Object... args) {
                
            String data = indexName.getValue(key);
            
            if(data != null && ! data.equals("-1")){
                return Boolean.parseBoolean(data);
            }
            else if(data != null && data.equals("-1")){
                return false;
            }
            else{
                updateIndexTree(indexName , key , methodName , args);
                return getBooleanFromIndex(indexName , key , methodName , args);
            }

        }
        
        public static void updateIndexSet(LdIndexer indexName, String key , String methodName , Object... args){
 
            
            Object returnedItem = executeMethod("lds.LdManager.LdManagerBase" , methodName , args);
            
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

        public static void updateIndexTree(LdIndexer indexName, String key, String methodName , Object... args) {      

            Object returnedItem = executeMethod("lds.LdManager.LdManagerBase" , methodName , args);
            
            boolean boolValue = false;
            int intValue = 0;
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
        
        private static Object executeMethod(String classPath , String methodName , Object... args){
            
            Object returnedItem = null;
            
            try {  
                Class<?> params[] = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof R) {
                        params[i] = R.class;
                    } else if (args[i] instanceof URI) {
                        params[i] = URI.class;
                    }
                }
                
                Class<?> cls = Class.forName(classPath);
                Object _instance = cls.newInstance();
                Method method = cls.getDeclaredMethod(methodName, params);
                returnedItem = method.invoke(_instance , args);
            }
             catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException ex) {
                Logger.getLogger(LdIndexer.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            
            return returnedItem;
        }

}
