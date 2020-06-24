/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.picss;

import lds.dataset.LdDatasetCreator;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;
import test.utility.Util;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

/**
 *
 * @author Fouad Komeiha
 */
public class PICSSTest_DBpedia_Mirror {


    @Test
    public void isPICSSWorksCorrectlyOnPaperExample() throws Exception{

        LdDataset dataset = LdDatasetCreator.getDBpediaMirrorDataset("http://localhost:8891/sparql" , "dbpedia");

        Conf config = new Conf();
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , dataset);
        config.addParam("resourcesCount" , 2350906);

        R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Bob_Dylan").create();
        R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Ronnie_Hawkins").create();

        PICSS picss = new PICSS(config);

        picss.loadIndexes();

        System.out.println(picss.compare(r1, r2));

        picss.closeIndexes();

    }
}
