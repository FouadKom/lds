package lds.LdManager;

import lds.dataset.LdDatasetCreator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Test;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import ldq.LdDataset;


public class LdManagerTest {
	
	ResimLdManager resimLdManager;

	@Test
	public void sameAsTest() throws Exception {

		LdDataset dataset = LdDatasetCreator.getDBpediaDataset();

		resimLdManager = new ResimLdManager(dataset, true);

		R a = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Paris").create();

		R b = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Dubai").create();

		R c = LdResourceFactory.getInstance().baseUri("http://www.wikidata.org/entity/").name("Q90").create();

		assertFalse(resimLdManager.isSameAs(a, b));

		assertTrue(resimLdManager.isSameAs(a, c));

	}

	@After
	public void deleteOutputFile() {
		resimLdManager.closeIndexes();
	}
    
}