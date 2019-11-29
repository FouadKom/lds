/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.LODS;

import java.util.ArrayList;
import java.util.List;
import lds.measures.lods.ontologies.*;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;
import test.utility.Util;
import lds.measures.lods.SimI;
import lds.resource.LdResourceFactory;
import lds.resource.R;

/**
 *
 * @author Fouad Komeiha
 */
public class SimI_Test {
    
    
    
    public static void main(String args[]) throws Exception{
        LdDataset dataSetMain = Util.getDBpediaDataset();
        
        Conf config = new Conf();
        config.addParam("useIndexes", false);
        config.addParam("LdDatasetMain" , dataSetMain);
        config.addParam("dataAugmentation" , true);

        
        List<O> ontologyList = new ArrayList<>();
        
        O dbpedia = new O_DBpedia();
        ontologyList.add(dbpedia);
        
        O dbpedia_de = new O_DBpedia_de();
        ontologyList.add(dbpedia_de);
        
        O dbpedia_fr = new O_DBpedia_fr();
        ontologyList.add(dbpedia_fr);
        
        O yago = new O_Yago();
        ontologyList.add(yago);

//        O wikiData = new O_WikiData();
//        ontologyList.add(wikiData);
        
        
        config.addParam("ontologyList" , ontologyList);
        
        SimI simi = new SimI(config);
        
        simi.loadIndexes();
        
        R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Paris").create();
        R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("New_York").create();
       
        System.out.println(simi.compare(r1, r2));    
       
        simi.closeIndexes();
        
          
    }
    
    
   // 0.2857142857142857
   // 0.2857142857142857
    
    
    
}
