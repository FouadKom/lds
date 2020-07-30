//package lds.indexing;
//
//import static org.junit.Assert.*;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.TemporaryFolder;
//import static org.hamcrest.CoreMatchers.*;
//
//public class LdIndexerTest {
//
//	@Rule
//	public TemporaryFolder tempFolder = new TemporaryFolder();
//
//	@Test
//	public void indexKeyWithListOfValuesTest() throws Exception {
//
//		// Create a temporary file.
//		//final File tempIndexFile = tempFolder.newFile("test.txt");
//		
//		// TOFIX: File is empty exception with temp files generated in tmp file of the VM
//		//LdIndexer index = new LdIndexer_(tempIndexFile.getAbsolutePath());
//		
//		File tempIndexFile = new File("/tmp/test.db");
//		LdIndexer_ index = new LdIndexer_(tempIndexFile.getAbsolutePath());
//
//		List<String> list = new ArrayList<String>();
//		//list.add("dbp:Paris");
//		//list.add("dbp-es:Paris");
//		index.addList("Paris", list);
//
//		index.close();
//
//		//
//		index.reload();
//		List<String> result = index.getList("Paris");
//		index.close();
//		tempIndexFile.delete();
//		
//		assertTrue(list.size() == result.size() && 
//			    list.containsAll(result) && result.containsAll(list));
//
//		
//	}
//
//}
