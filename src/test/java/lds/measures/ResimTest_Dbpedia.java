/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lds.LdManager.LdManager;
import lds.LdManager.LdManagerBase;
import static lds.measures.ResimTest.resimLdManager;
import lds.measures.resim.Resim;
import lds.measures.resim.ResimLdManager;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;
import slib.graph.model.impl.repo.URIFactoryMemory;
import slib.graph.model.repo.URIFactory;
import slib.utils.ex.SLIB_Ex_Critic;
import slib.utils.i.Conf;

/**
 *
 * @author LENOVO
 */
public class ResimTest_Dbpedia {
    public static ResimLdManager resimLdManager;
    
    
//    @Test
//    public void isResimWorksCorrectlyOnPaperExample() throws SLIB_Ex_Critic{
    
    public static void main(String args[]) throws SLIB_Ex_Critic {
        
    	LdDataset dataset = Util.getDBpediaDataset();
                
        Conf config = new Conf();
        config.addParam("useIndexes", true);
        resimLdManager = new ResimLdManager(dataset, config);
        Resim resim = new Resim(resimLdManager);       


        R car = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Computer").create();
        R automobile = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Keyboard").create();
        
        

        Set<URI> edges =  resimLdManager.getEdges(car , automobile);

            double cii = 0, cio = 0, cii_car = 0, cio_car = 0, cii_automobile = 0, cio_automobile = 0, cii_norm = 0, cio_norm = 0, pptySim = 0,
				ldsd = 0, ldsdsim = 0, sim = 0 , cd_automobile_norm = 0 , cd_car_norm = 0 , csip = 0 , csop = 0;
            
            
            double x = 0, y = 0, ip = 0, op = 0;
           
		

		for (URI edge : edges) {
                        System.out.println("Edge: " + edge);
			cii = cii + resim.Cii(edge, car, automobile);
			cio = cio + resim.Cio(edge, car , automobile );
			
                        cii_car = cii_car + resim.Cii(edge, car);
                        cio_car = cio_car + resim.Cio(edge, car);
                      
			cii_automobile = cii_automobile + resim.Cii(edge, automobile);
			cio_automobile = cio_automobile + resim.Cio(edge, automobile);
                        
                        cd_car_norm = cd_car_norm + resim.Cd_normalized(edge, car, automobile);
			cd_automobile_norm = cd_automobile_norm + resim.Cd_normalized(edge, automobile , car);
                        
                        cio_norm = cio_norm + resim.Cio_normalized(edge, car, automobile);
			cii_norm = cii_norm + resim.Cii_normalized(edge, car, automobile);

                        csip = csip + resim.Csip(edge, car, automobile);
                        csop = csop + resim.Csop(edge, car, automobile);                      
                        
                        x = x + ( (double) csip / resim.Cd(edge));
			y = y + ( (double) csop / resim.Cd(edge));
                        
                        

                }
                
                ip = x / (resim.Cip(car) + resim.Cip(automobile));
                op = y / (resim.Cop(car) + resim.Cop(automobile));
                pptySim = ip + op;
                
                ldsd = 1 / (1 + cd_car_norm + cd_automobile_norm + cii_norm + cio_norm);
                ldsdsim = 1 - ldsd;
                
                sim = (pptySim + (2*ldsdsim) )/3;
			
                System.out.println(cii);
                System.out.println(cio);
                System.out.println(cii_car);
                System.out.println(cio_car);
                System.out.println(cii_automobile);
                System.out.println(cio_automobile);
                System.out.println(cio_norm);
                System.out.println(cii_norm);
                System.out.println(pptySim);
                System.out.println(ldsdsim);
                
                System.out.println(csip);
                System.out.println(csop);
                
                
                System.out.println(resim.compare(car, automobile));
                
                resimLdManager.closeIndexes();
                   

//        
//        sim = resim.Resim(a , c);
//        assertEquals(1.0, sim, 0.0);
               
//          System.out.println(resim.Resim(car , automobile));
//        System.out.println(manager.countIngoingEdges(car));
//            for (String s: resimLdManager.getIngoingEdges(car) ){
//                    System.out.println(s + "\n");
//            }

//            ParameterizedSparqlString query_cmd = dataset.prepareQuery();
//
//            query_cmd.setCommandText("select distinct ?subject where {?subject ?property <" + car.getUri() + "> .}");
//
//            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());
//
//            while (resultSet.hasNext()) {
//                QuerySolution qs = resultSet.nextSolution();
//                Resource subject = qs.getResource("subject");
////                Resource property = qs.getResource("property");
////                System.out.println(qs.getLiteral("count").toString());
//                System.out.println(subject.toString().replace("http://dbpedia.org/resource/", "dbr:") );
//
//
//            }
//            String q= "select (count(distinct ?subject) as ?count) from <http://dbpedia.org> where {?subject ?property <" + car.getUri() + "> .}";
//            Query query = QueryFactory.create(q);
//            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
//            ResultSet results = qexec.execSelect();
//            
//            while (results.hasNext()) {
//                QuerySolution qs = results.nextSolution();
////                Resource subject = qs.getResource("subject");
////                System.out.println(subject.toString().replace("http://dbpedia.org/resource/", "dbr:") );
//                 System.out.println(qs.getLiteral("count"));
//
//
//            }

            
            
 

    }
    
    
}
