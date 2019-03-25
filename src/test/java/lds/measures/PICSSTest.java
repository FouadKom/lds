/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;
import org.openrdf.model.URI;

import lds.measures.picss.PICSS;
import org.apache.commons.httpclient.HttpException;
import static org.junit.Assert.fail;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.graph.model.impl.repo.URIFactoryMemory;
import slib.graph.model.repo.URIFactory;
import slib.utils.ex.SLIB_Ex_Critic;

/**
 *
 * @author Fouad Komeiha
 */
public class PICSSTest {
	public static final String dataSetDir = System.getProperty("user.dir") + "/src/test/resources/data.rdf";

//	@Test
//	public void isPICSSWorksCorrectlyOnPaperExample() throws SLIB_Ex_Critic, HttpException {
//		LdDataset dataSet = null;
//		URIFactory factory = URIFactoryMemory.getSingleton();
//
//		try {
//			dataSet = LdDatasetFactory.getInstance().name("example").file(dataSetDir).create();
//
//		} catch (Exception e) {
//			fail(e.getMessage());
//			e.printStackTrace();
//		}
//
//		PICSS picss = new PICSS(dataSet);
//
//		URI r1 = factory.getURI("http://www.example.org#Fish"), r2 = factory.getURI("http://www.example.org#Whale");
//
//		Set<String> F_r1 = lds.measures.picss.Utility.getFeaturesSet(r1, dataSet),
//				F_r2 = lds.measures.picss.Utility.getFeaturesSet(r2, dataSet),
//				F_common = lds.measures.picss.Utility.intersection(F_r1, F_r2),
//				F_r1_only = lds.measures.picss.Utility.difference(F_r1, F_r2),
//				F_r2_only = lds.measures.picss.Utility.difference(F_r2, F_r1);
//
//		double pic_F_r1, pic_F_r2, picF_r1_only, picF_r2_only, picF_common, comp;
//
//		pic_F_r1 = picss.PIC(F_r1);
//		assertEquals(10.67970000576925, pic_F_r1, 0.0);
//
//		pic_F_r2 = picss.PIC(F_r2);
//		assertEquals(10.094737505048094, pic_F_r2, 0.0);
//
//		picF_r1_only = picss.PIC(F_r1_only);
//		assertEquals(8.509775004326938, picF_r1_only, 0.0);
//
//		picF_r2_only = picss.PIC(F_r2_only);
//		assertEquals(7.924812503605781, picF_r2_only, 0.0);
//
//		picF_common = picss.PIC(F_common);
//		assertEquals(2.1699250014423126, picF_common, 0.0);
//
//		comp = picss.PICSS(r1, r2);
//		assertEquals(0.11663433805905218, comp, 0.0);
//
//		comp = picss.PICSS(r2, r1);
//		assertEquals(0.11663433805905216, comp, 0.0);
//
//		comp = picss.PICSS(r1, r1);
//		assertEquals(1.0, comp, 0.0);
//
//	}
}
