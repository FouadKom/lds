/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.LODS;

import java.util.ArrayList;
import java.util.List;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.dataset.LdDatasetCreator;
import lds.measures.lods.ontologies.*;
import sc.research.ldq.LdDataset;
import lds.measures.lods.SimC;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import org.junit.Test;


/**
 *
 * @author Fouad Komeiha
 */
public class SimC_Test {
//    
//    @Test
//    public void SimI_Test() throws Exception{
    public static void main(String args[]) throws Exception{
        LdDataset dataSetMain = LdDatasetCreator.getDBpediaDataset();

        Config config = new Config();
        config.addParam(ConfigParam.useIndexes, false);
        config.addParam(ConfigParam.LdDatasetMain , dataSetMain);
        config.addParam(ConfigParam.dataAugmentation , false);
        config.addParam(ConfigParam.sup, 1);
        config.addParam(ConfigParam.sub, 1);

        List<O> ontologyList = new ArrayList<>();

        O dbpedia = new O_DBpedia();
        ontologyList.add(dbpedia);

        /*O dbpedia_de = new O_DBpedia_de();
        ontologyList.add(dbpedia_de);

        O dbpedia_fr = new O_DBpedia_fr();
        ontologyList.add(dbpedia_fr);

        O yago = new O_Yago();
        ontologyList.add(yago);*/

//        O wikiData = new O_WikiData();
//        ontologyList.add(wikiData);        

        config.addParam(ConfigParam.ontologyList, ontologyList);

        SimC simc = new SimC(config);

        simc.loadIndexes();

//        R r1 = new R("http://dbpedia.org/resource/New_York_City");
//        R r2 = new R("http://dbpedia.org/resource/London");

         R r1 = new R("http://dbpedia.org/resource/The_Noah");
         R r2 = new R("http://dbpedia.org/resource/The_Pack_(2010_film)");

        System.out.println(simc.compare(r1, r2)); 

        simc.closeIndexes();
    }
}
