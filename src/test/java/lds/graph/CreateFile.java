package lds.graph;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

/**
 *
 * @author Fouad Komeiha
 */
public class CreateFile {
    private String directory;
    
    public CreateFile(String directory) throws IOException{
      
    	this.directory = directory;
       
        File f = new File(directory);

        f.getParentFile().mkdirs(); 
        f.createNewFile();
        
    }

        
    public void writeToFile(){ 
 
        String ns = "http://www.example.org#";
        String nsRDFS = "http://www.w3.org/2000/01/rdf-schema#";
  
       
        Model model = ModelFactory.createDefaultModel();

        
        addStatement(ns+"Fish", nsRDFS+"subClassOf", ns+"Animal" , model);
        addStatement(ns+"Fish", ns+"livesIn", ns+"Water", model);
        addStatement(ns+"Fish", ns+"affraidOf", ns+"Whale", model);
        
        addStatement(ns+"Mammal", nsRDFS+"subClassOf", ns+"Animal", model);
        addStatement(ns+"Mammal", ns+"has", ns+"Vertebra", model);
        
        addStatement(ns+"Whale", nsRDFS+"subClassOf", ns+"Mammal", model);
        addStatement(ns+"Whale", ns+"livesIn", ns+"Water", model);
        addStatement(ns+"Whale", ns+"eates", ns+"Fish", model);
        
        addStatement(ns+"Cat", nsRDFS+"subClassOf", ns+"Mammal", model);
        addStatement(ns+"Cat", ns+"has", ns+"Fur", model);
        addStatement(ns+"Cat", ns+"affraidOf", ns+"Bear", model);
        
        addStatement(ns+"Bear", nsRDFS+"subClassOf", ns+"Mammal", model);
        addStatement(ns+"Bear", ns+"has", ns+"Fur", model);
        addStatement(ns+"Bear", ns+"eates", ns+"Cat", model);
        
        try{
            FileOutputStream fout=new FileOutputStream(this.directory);
            model.write(fout , "RDF/XML");
        }

        catch (Exception e)
        {
           System.out.println("Failed: " + e);
        }

    }
    
    
     public void addStatement(String s, String p, String o , Model model){
        Resource subject = model.createResource(s);
        Property predicate = model.createProperty(p);
        RDFNode object = model.createResource(o);
        Statement stmt = model.createStatement(subject, predicate, object);
        model.add(stmt);
    }
}
