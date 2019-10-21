/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.ldsd;


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
public class LDSDTest_localRDF {
    public static final String dataSetDir = System.getProperty("user.dir") + "/src/test/resources/data.rdf";
    
    
    @Test
    public void isLDSDWorksCorrectlyOnPaperExample() throws Exception{
    
//    public static void main(String args[]) throws Exception{

    
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
        
        double ldsd_dSim = 0 , ldsd_dwSim = 0 , ldsd_iSim = 0 , ldsd_iwSim = 0 , ldsd_cwSim = 0 , tldsd_cwSim = 0 ;

        LDSD ldsd_d = new LDSD_d(config) ,
             ldsd_dw = new LDSD_dw(config) ,
             ldsd_i = new LDSD_i(config) ,
             ldsd_iw = new LDSD_iw(config) ,
             ldsd_cw = new LDSD_cw(config) ,
             tldsd_cw = new TLDSD_cw(config);

        ldsd_d.loadIndexes();
        
        ldsd_dSim = ldsd_d.compare(r1, r2);
        assertEquals(0.66666666666666666, ldsd_dSim , 0.0);
        
        ldsd_dSim = ldsd_d.compare(r2, r1);
        assertEquals(0.66666666666666666, ldsd_dSim , 0.0);
        
        ldsd_d.closeIndexes();
        
        ldsd_dw.loadIndexes();
        
        ldsd_dwSim = ldsd_dw.compare(r1, r2);
        assertEquals(0.66666666666666666, ldsd_dwSim , 0.0);
        
        ldsd_dwSim = ldsd_dw.compare(r1, r1);
        assertEquals(0.0, ldsd_dwSim , 0.0);
        
        ldsd_dwSim = ldsd_dw.compare(r2, r2);
        assertEquals(0.0, ldsd_dwSim , 0.0);
        
        ldsd_dw.closeIndexes();
 
        ldsd_i.loadIndexes();
        
        ldsd_iSim = ldsd_i.compare(r1, r2);
        assertEquals(0.5 , ldsd_iSim , 0.0);
        
        ldsd_i.closeIndexes();
        
        ldsd_iw.loadIndexes();
        
        ldsd_iwSim = ldsd_iw.compare(r1, r2);
        assertEquals(0.5 , ldsd_iwSim , 0.0);
        
        ldsd_iw.closeIndexes();
        
        ldsd_cw.loadIndexes();
        
        ldsd_cwSim = ldsd_cw.compare(r1, r2);
        assertEquals(0.75 , ldsd_cwSim , 0.0);
        
        ldsd_cw.closeIndexes();
        
        tldsd_cw.loadIndexes();
        
        tldsd_cwSim = tldsd_cw.compare(r1, r2);
        assertEquals(0.75 , tldsd_cwSim , 0.0);
        
        tldsd_cw.closeIndexes();
        
        
        
        /*LdsdLdManager ldsdLoader = new LdsdLdManager(dataSet , true);
        ldsd_d.compare(r1, r2);
        
        double cd_a_b = 0, cd_b_a = 0, cd_a_norm = 0, cd_b_norm = 0, cd_a = 0,
			cd_b = 0 , cii = 0 , cio =0 , cii_a = 0 , cio_a = 0 , cii_norm = 0 , cio_norm = 0;

        
        for(URI edge : ldsdLoader.getEdges(r1, r2)){
            
            
            cd_a_b = cd_a_b + ldsd_d.Cd(edge , r1 , r2);
            System.out.println("Cd(" + edge.stringValue() + " , " + r1.getUri().stringValue() + " , " +  r2.getUri().stringValue() + "): " + ldsd_d.Cd(edge , r1 , r2));
            
            System.out.println("------------------------------------------------------");
            
            cd_a = cd_a + ldsd_d.Cd(edge , r1);
            System.out.println("Cd(" + edge.stringValue() + " , " + r1.getUri().stringValue() + "): " + ldsd_d.Cd(edge , r1));
            
            System.out.println("------------------------------------------------------");
            
            cd_a_norm = cd_a_norm + ldsd_d.Cd_normalized(edge, r1, r2);
            System.out.println("Cd_normalized(" + edge.stringValue() + " , " + r1.getUri().stringValue() + " , " +  r2.getUri().stringValue() + "): " + ldsd_d.Cd_normalized(edge, r1, r2));
            
            System.out.println();
            
            
            cd_b_a = cd_b_a + ldsd_d.Cd(edge , r2 , r1);
            System.out.println("Cd(" + edge.stringValue() + " , " + r2.getUri().stringValue() + " , " +  r1.getUri().stringValue() + "): " + ldsd_d.Cd(edge , r2 , r1));
            
            System.out.println("------------------------------------------------------");
            
            cd_b = cd_b + ldsd_d.Cd(edge , r2);
            System.out.println("Cd(" + edge.stringValue() + " , " + r2.getUri().stringValue() + "): " + ldsd_d.Cd(edge , r2));
            
            System.out.println("------------------------------------------------------");
            
            cd_b_norm = cd_b_norm + ldsd_d.Cd_normalized(edge, r2, r1);
            System.out.println("Cd_normalized(" + edge.stringValue() + " , " + r2.getUri().stringValue() + " , " +  r1.getUri().stringValue() + "): " + ldsd_d.Cd_normalized(edge, r2, r1));
            
            System.out.println();
            
            cii = cii + ldsd_d.Cii(edge, r1, r2);
            System.out.println("Cii(" + edge.stringValue() + " , " + r1.getUri().stringValue() +  " , " + r2.getUri().stringValue() + "): " + ldsd_d.Cii(edge , r1 , r2));
            
            System.out.println("------------------------------------------------------");
            
            cii_a = cii_a + ldsd_d.Cii(edge, r1);
            System.out.println("Cii(" + edge.stringValue() + " , " + r1.getUri().stringValue() + "): " + ldsd_d.Cii(edge , r1 ));
            
            System.out.println("------------------------------------------------------");
            
            cii_norm = cii_norm + ldsd_d.Cii_normalized(edge, r1, r2);
            System.out.println("Cii_normalized(" + edge.stringValue() + " , " + r1.getUri().stringValue() +  " , " + r2.getUri().stringValue() + "): " + ldsd_d.Cii_normalized(edge , r1 , r2));
            
            System.out.println();
            
            cio = cio + ldsd_d.Cio(edge, r1, r2);
            System.out.println("Cio(" + edge.stringValue() + " , " + r1.getUri().stringValue() + " , " + r2.getUri().stringValue() + "): " + ldsd_d.Cio(edge , r1 , r2));
            
            System.out.println("------------------------------------------------------");
            
            cio_a = cio_a + ldsd_d.Cio(edge, r1);
            System.out.println("Cio(" + edge.stringValue() + " , " + r1.getUri().stringValue() + "): " + ldsd_d.Cio(edge , r1));
            
            System.out.println("------------------------------------------------------");
            
            cio_norm = cio_norm + ldsd_d.Cio_normalized(edge, r1, r2);
            System.out.println("Cio_normalized(" + edge.stringValue() + " , " + r1.getUri().stringValue() +  " , " + r2.getUri().stringValue() + "): " + ldsd_d.Cio_normalized(edge , r1 , r2));
            
            
            System.out.println();
        }*/
        
        
        
    
    }
    
}
