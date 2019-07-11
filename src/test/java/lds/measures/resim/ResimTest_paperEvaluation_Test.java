/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;

import lds.measures.picss.Util;
import lds.measures.resim.Resim;
import lds.measures.resim.ResourceSimilarity;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;

/**
 *
 * @author Fouad Komeiha
 */
public class ResimTest_paperEvaluation_Test {
    
    
    @Test
    public void isResimWorksCorrectlyOnPaperExample() throws Exception {
        
    	LdDataset dataset = Util.getDBpediaDataset();
                
        Conf config = new Conf();
        config.addParam("useIndexes", true);
        config.addParam("LdDatasetMain" , dataset);
        
        ResourceSimilarity resim = new Resim(config);       


        R car = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Car").create();
        R automobile = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Automobile").create();
        R flight = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Flight").create();
        R money = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Money").create();
        R currency = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Currency").create();
        R business_operations = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Business_operations").create();
        R cash = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Cash").create();
        R bank = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Bank").create();
        R demand_deposit = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Demand_deposit").create();
        R professor = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Professor").create();
        R doctor_of_medicine = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Doctor_of_Medicine").create();
        R cucumber = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Cucumber").create();
        R nursing = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Nursing").create();
        R bus_driver = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Bus_driver").create();
        R ocean = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Ocean").create();
        R sea = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Sea").create();
        R continent = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Continent").create();
        R computer = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Computer").create();
        R keyboard = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Keyboard").create();
        R news = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("News").create();
        R internet = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Internet").create();
        R software = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Software").create();
        R laboratory = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Laboratory").create();
                
        resim.loadIndexes();
        
        assertTrue(resim.compare(car, automobile) > resim.compare(car, flight));
        assertTrue(resim.compare(money, currency) > resim.compare(money, business_operations));
        assertTrue(resim.compare(money, cash) < resim.compare(money, bank));
        assertTrue(resim.compare(money, cash) > resim.compare(money, demand_deposit));
        assertTrue(resim.compare(professor, doctor_of_medicine) > resim.compare(professor, cucumber));
        assertTrue(resim.compare(doctor_of_medicine, nursing) > resim.compare(doctor_of_medicine, bus_driver));
        assertTrue(resim.compare(ocean, sea) > resim.compare(ocean, continent));
        assertTrue(resim.compare(computer, keyboard) < resim.compare(computer, news));
        assertTrue(resim.compare(computer, internet) > resim.compare(computer, news));
        assertTrue(resim.compare(computer, software) > resim.compare(computer, laboratory));


        resim.closeIndexes();
  }    
    
}
