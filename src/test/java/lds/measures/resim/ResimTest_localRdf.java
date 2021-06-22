/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;

import lds.LdManager.ResimLdManager;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.resource.R;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import ldq.LdDataset;
import ldq.LdDatasetFactory;

/**
 *
 * @author Fouad Komeiha
 */
public class ResimTest_localRdf {
	public static final String dataSetDir = System.getProperty("user.dir") + "/src/test/resources/data.rdf";
	public static ResimLdManager resimLdManager;

	@Test
	public void isResimWorksCorrectlyOnPaperExample() throws Exception{


		LdDataset dataSet = null;

		R r1 = new R("http://www.example.org#Fish");
		R r2 = new R("http://www.example.org#Whale");

		try {
			dataSet = LdDatasetFactory.getInstance().name("example").file(dataSetDir)
					.defaultGraph("http://graphResim/dataset").create();

		} catch (Exception e) {
			fail(e.getMessage());
		}

		Config config = new Config();
                config.addParam(ConfigParam.useIndexes, false);
                config.addParam(ConfigParam.LdDatasetMain , dataSet);

		ResourceSimilarity resim = new Resim(config);
                
                resim.loadIndexes();
                
                double cii = 0, cio = 0, cii_r1 = 0, cio_r1 = 0, cii_r2 = 0, cio_r2 = 0, cii_norm = 0, cio_norm = 0, pptySim,
				ldsd, ldsdsim, sim;
                
		/*Set<URI> edges = resimLdManager.getEdges(r1, r2);

		

		for (URI edge : edges) {
			cii = cii + resim.Cii(edge, r1, r2);
                        cio = cio + resim.Cio(edge, r1, r2);
                        cii_r1 = cii_r1 + resim.Cii(edge, r1);
                        cio_r1 = cio_r1 + resim.Cio(edge, r1);
                        cii_r2 = cii_r2 + resim.Cii(edge, r2);
                        cio_r2 = cio_r2 + resim.Cio(edge, r2);
                        cio_norm = cio_norm + resim.Cio_normalized(edge, r1, r2);
                        cii_norm = cii_norm + resim.Cii_normalized(edge, r1, r2);


		}

		// components of LDSD in Resim
		assertEquals(0.0, cii, 0.0);
		assertEquals(1.0, cio, 0.0);
		assertEquals(0.0, cii_r1, 0.0);
		assertEquals(2.0, cio_r1, 0.0);
		assertEquals(0.0, cii_r2, 0.0);
		assertEquals(3.0, cio_r2, 0.0);
		assertEquals(0.0, cii_norm, 0.0);
		assertEquals(1.0, cio_norm, 0.0);*/

		sim = resim.compare(r1, r2 , 1, 2);
		assertEquals(0.5388888888888889, sim, 0.0);

		pptySim = resim.PropertySim(r1, r2);
		assertEquals(0.11666666666666665, pptySim, 0.0);


                ldsdsim = resim.LDSDsim(r1, r2);
		assertEquals(0.75, ldsdsim, 0.0);

		sim = resim.compare(r2, r1 , 1 , 2);
		assertEquals(0.5388888888888889, sim, 0.0);

		sim = resim.compare(r1, r1);
		assertEquals(1.0, sim, 0.0);

		resim.closeIndexes();

	}

}
