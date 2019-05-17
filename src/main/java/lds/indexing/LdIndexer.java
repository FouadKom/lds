package lds.indexing;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerArrayTuple;

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

}
