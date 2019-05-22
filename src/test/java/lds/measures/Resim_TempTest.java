/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures;

import java.io.File;
import java.util.List;
import java.util.Set;
import lds.measures.resim.Resim;
import lds.measures.resim.ResimLdManager;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;
import slib.utils.i.Conf;

/**
 *
 * @author LENOVO
 */
public class Resim_TempTest {
    public static ResimLdManager resimLdManager;
    public static String nameSpace = "http://dbpedia.org/resource/";
    
    

    public static void main(String args[]) {
        
        /*R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Tiger").create();
        R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name("Cat").create();
        
        Conf config = new Conf();
        LdDataset dataset = Util.getDBpediaDataset();
        
        
        config.addParam("useIndexes", false);
        
        ResimLdManager resimLdManager = new ResimLdManager(dataset, config);
        
        Resim resim = new Resim(resimLdManager);
        
        Set<URI> edges = resimLdManager.getEdges(r1, r2);
        
        double cii = 0, cio = 0, cii_r1 = 0, cio_r1 = 0, cii_r2 = 0, cio_r2 = 0, cii_norm = 0, cio_norm = 0, csip = 0 , csop = 0 , cip_r1 = 0 , cip_r2 =0, cop_r1 = 0 , cop_r2 = 0 
                , cd_edge = 0, cd_r1 = 0 , cd_r2 = 0 , cd = 0;
        
        System.out.println("Dbpedia");
        for (URI edge : edges) {
//                System.out.println("cd_r1 " + resim.Cd(edge, r1) + " at edge: " + edge.toString());
                cii = cii + resim.Cii(edge, r1, r2);
                cio = cio + resim.Cio(edge, r1, r2);

                cii_r1 = cii_r1 + resim.Cii(edge, r1);
                cio_r1 = cio_r1 + resim.Cio(edge, r1);

                cii_r2 = cii_r2 + resim.Cii(edge, r2);
                cio_r2 = cio_r2 + resim.Cio(edge, r2);

                cio_norm = cio_norm + resim.Cio_normalized(edge, r1, r2);
                cii_norm = cii_norm + resim.Cii_normalized(edge, r1, r2);

                csip = csip + resim.Csip(edge, r1, r2);
                csop = csop + resim.Csop(edge, r1, r2);

                cip_r1 = cip_r1 + resim.Cip(r1);
                cip_r2 = cip_r2 + resim.Cip(r2);

                cop_r1 = cip_r1 + resim.Cop(r1);
                cop_r2 = cip_r2 + resim.Cop(r2);
                
                cd_edge = cd_edge + resim.Cd(edge);
                cd = cd + resim.Cd(edge, r1, r2);
                cd_r1 = cd_r1 + resim.Cd(edge, r1);
                cd_r2 = cd_r2 + resim.Cd(edge, r2);
//                
                
                
        }
                
        System.out.println("Dbpedia");
        System.out.println("cii " + cii);
	System.out.println("cio " + cio);
	System.out.println("cii_r1 " + cii_r1);
	System.out.println("cio_r1 " + cio_r1);
	System.out.println("cii_r2 " + cii_r2);
	System.out.println("cio_r2 " + cio_r2);
	System.out.println("cii_norm " + cii_norm);
	System.out.println("cio_norm " + cio_norm);
        System.out.println("cip_r1 " + cip_r1);
        System.out.println("cip_r2 " + cip_r2);
        System.out.println("cop_r1 " + cop_r1);
        System.out.println("cop_r2 " + cop_r2);
        System.out.println("cd_edge " + cd_edge);
        System.out.println("cd " + cd);
        System.out.println("cd_r1 " + cd_r1);
        System.out.println("cd_r2 " + cd_r2);
        System.out.println();
        
        
        config.addParam("useIndexes", true);
        
        resimLdManager = new ResimLdManager(dataset, config);
        
        resim = new Resim(resimLdManager);
        
        edges = resimLdManager.getEdges(r1, r2);
        
        cii = 0; cio = 0; cii_r1 = 0; cio_r1 = 0; cii_r2 = 0; cio_r2 = 0; cii_norm = 0; cio_norm = 0; csip = 0 ; csop = 0 ; cip_r1 = 0 ; cip_r2 =0; cop_r1 = 0 ; cop_r2 = 0 
                ; cd_edge = 0; cd_r1 = 0 ; cd_r2 = 0 ; cd = 0;
        
        System.out.println("Indexes");
        for (URI edge : edges) {
//                 System.out.println("cd_r1 " + resim.Cd(edge, r1) + " at edge: " + edge.toString());
                cii = cii + resim.Cii(edge, r1, r2);
                cio = cio + resim.Cio(edge, r1, r2);

                cii_r1 = cii_r1 + resim.Cii(edge, r1);
                cio_r1 = cio_r1 + resim.Cio(edge, r1);

                cii_r2 = cii_r2 + resim.Cii(edge, r2);
                cio_r2 = cio_r2 + resim.Cio(edge, r2);

                cio_norm = cio_norm + resim.Cio_normalized(edge, r1, r2);
                cii_norm = cii_norm + resim.Cii_normalized(edge, r1, r2);

                csip = csip + resim.Csip(edge, r1, r2);
                csop = csop + resim.Csop(edge, r1, r2);

                cip_r1 = cip_r1 + resim.Cip(r1);
                cip_r2 = cip_r2 + resim.Cip(r2);

                cop_r1 = cip_r1 + resim.Cop(r1);
                cop_r2 = cip_r2 + resim.Cop(r2);
                
                cd_edge = cd_edge + resim.Cd(edge);
                cd = cd + resim.Cd(edge, r1, r2);
                cd_r1 = cd_r1 + resim.Cd(edge, r1);
                cd_r2 = cd_r2 + resim.Cd(edge, r2);
        }
                
        System.out.println("Indexes");
        System.out.println("cii " + cii);
	System.out.println("cio " + cio);
	System.out.println("cii_r1 " + cii_r1);
	System.out.println("cio_r1 " + cio_r1);
	System.out.println("cii_r2 " + cii_r2);
	System.out.println("cio_r2 " + cio_r2);
	System.out.println("cii_norm " + cii_norm);
	System.out.println("cio_norm " + cio_norm);
        System.out.println("cip_r1 " + cip_r1);
        System.out.println("cip_r2 " + cip_r2);
        System.out.println("cop_r1 " + cop_r1);
        System.out.println("cop_r2 " + cop_r2);
        System.out.println("cd_edge " + cd_edge);
        System.out.println("cd " + cd);
        System.out.println("cd_r1 " + cd_r1);
        System.out.println("cd_r2 " + cd_r2);
        System.out.println();
        
        resimLdManager.closeIndexes();
        
        
    */    
    }
       
}
