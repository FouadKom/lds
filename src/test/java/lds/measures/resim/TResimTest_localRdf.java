/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;

import java.util.Set;
import lds.LdManager.ResimLdManager;
import lds.measures.resim.ResourceSimilarity;
import lds.measures.resim.TResim;
import lds.resource.R;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class TResimTest_localRdf {
    
    	public static final String dataSetDir = System.getProperty("user.dir") + "/src/test/resources/data.rdf";
	public static ResimLdManager tresimLdManager;

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

		Conf config = new Conf();
		config.addParam("useIndexes", true);
                config.addParam("LdDatasetMain" , dataSet);

		ResourceSimilarity tresim = new TResim(config);
                
                double cii = 0, cio = 0, cii_k = 0 , cio_k = 0 , cii_norm = 0, cio_norm = 0, pptySim,
				ldsdsim, sim , cd_r1 =0 , cd_r2 = 0 , cd_norm_r1 = 0 , cd_norm_r2 = 0;
                
		/*Set<URI> edges = tresimLdManager.getEdges(r1, r2);

		

		for (URI firstEdge : edges) {
                    
                    for(URI secondEdge : edges){
			cii = cii + tresim.Cii(firstEdge , secondEdge , r1, r2);

			cio = cio + tresim.Cio(firstEdge, secondEdge , r1, r2);

			cio_norm = cio_norm + tresim.Cio_normalized(firstEdge, secondEdge , r1, r2);

			cii_norm = cii_norm + tresim.Cii_normalized(firstEdge, secondEdge , r1, r2);

                    }
                    
                    cd_r1 = cd_r1 + tresim.Cd(firstEdge , r1 , r2);
                    cd_norm_r1 = cd_norm_r1 + tresim.Cd_normalized(firstEdge, r1, r2);
                    cd_r2 = cd_r2 + tresim.Cd(firstEdge , r2 , r1);                   
                    cd_norm_r2 = cd_norm_r2 + tresim.Cd_normalized(firstEdge, r2, r1);

		} 
               

		// components of LDSD in Resim
                assertEquals(1.0 , cd_r1 , 0.0);
                assertEquals(1.0 , cd_r2 , 0.0);
                assertEquals(0.7686217868402407 , cd_norm_r2 , 0.0);
                assertEquals(0.7686217868402407 , cd_norm_r2 , 0.0);
		assertEquals(0.0, cii, 0.0);
		assertEquals(1.0, cio, 0.0);
		assertEquals(0.0, cii_norm, 0.0);
		assertEquals(1.0, cio_norm, 0.0);*/
                
                tresim.loadIndexes();

		sim = tresim.compare(r1, r2);
		assertEquals(0.41698033075235674, sim, 0.0);

		pptySim = tresim.PropertySim(r1, r2);
		assertEquals(0.11666666666666665, pptySim, 0.0);

		ldsdsim = tresim.LDSDsim(r1, r2);
                assertEquals(0.7172939948380468, ldsdsim, 0.0);

		sim = tresim.compare(r2, r1);
		assertEquals(0.41698033075235674, sim, 0.0);

		sim = tresim.compare(r1, r1);
		assertEquals(1.0, sim, 0.0);

		tresim.closeIndexes();

	}
    
}
