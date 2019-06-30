package lds.LdManager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerArrayTuple;

import lds.LdManager.LdManagerBase;
import lds.measures.resim.Resim;
import lds.measures.resim.ResimLdManager;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import slib.sml.sm.core.utils.SMconf;
import slib.utils.i.Conf;

public class LdManagerTest {
	
	ResimLdManager resimLdManager;

	@Test
	public void sameAsTest() throws Exception {

		LdDataset dataset = Util.getDBpediaDataset();

		resimLdManager = new ResimLdManager(dataset, true);

		R a = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Paris").create();

		R b = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Dubai").create();

		R c = LdResourceFactory.getInstance().baseUri("http://www.wikidata.org/entity/").name("Q90").create();

		assertFalse(resimLdManager.isSameAs(a, b));

		assertTrue(resimLdManager.isSameAs(a, c));

	}

	@After
	public void deleteOutputFile() {
		resimLdManager.sameAsIndex.close();
	}

}
