/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;

import static lds.measures.resim.ResimTest_localRdf.dataSetDir;
import lds.resource.R;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class OptimizationTest_localRdf {
    
    
    @Test
    public void OptimizationTest() throws Exception{ 
        LdDataset dataSet = null;

		R r1 = new R("http://www.example.org#Fish");
		R r2 = new R("http://www.example.org#Whale");

		try {
			dataSet = LdDatasetFactory.getInstance().name("example").file(dataSetDir)
					.defaultGraph("http://graphResim/dataset").create();

		} catch (Exception e) {
			fail(e.getMessage());
		}
                
                double pptySim,	ldsdsim, sim;

		Conf config = new Conf();
		config.addParam("useIndexes", false);
                config.addParam("LdDatasetMain" , dataSet);

		ResourceSimilarityO resim = new ResimO(config);
                
                resim.loadIndexes();
                
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
