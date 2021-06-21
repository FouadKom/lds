/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager.ontologies;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lds.indexing.LdIndex;
import lds.indexing.LdIndexerManager;
import lds.measures.lods.ontologies.*;
import lds.resource.R;
import org.apache.jena.rdf.model.Resource;
import org.openrdf.model.URI;

/**
 *
 * @author Fouad Komeiha
 */
public class Ontology {
    private static LdIndexerManager manager;
    private static String prefixIndexPath = System.getProperty("user.dir") + "/Indexes/Prefixes/prefixes_index.db";
    private static String namespaceIndexPath = System.getProperty("user.dir") + "/Indexes/Prefixes/namespaces_index.db";
    private static LdIndex prefixIndex;
    private static LdIndex nameSpaceIndex;

    public static O getOntologyFromNameSpace(String nameSpace) {
        O ontology = null;
        switch (nameSpace) {
            case "http://dbpedia.org/ontology/":
                ontology = new O_DBpedia();
                break;
            case "http://dbpedia.org/class/yago/":
                ontology = new O_Yago();
                break;
            case "http://schema.org/":
                ontology = new O_Schema();
                break;
            case "http://umbel.org/umbel/rc/":
                ontology = new O_Umbel();
                break;
            case "http://www.wikidata.org/entity/":
                ontology = new O_WikiData();
                break;
            case "http://de.dbpedia.org/resource/":
                ontology = new O_DBpedia_de();
                break;
            case "http://cs.dbpedia.org/resource/":
                ontology = new O_DBpedia_cs();
                break;
            case "http://el.dbpedia.org/resource/":
                ontology = new O_DBpedia_el();
                break;
            case "http://es.dbpedia.org/resource/":
                ontology = new O_DBpedia_es();
                break;
            case "http://eu.dbpedia.org/resource/":
                ontology = new O_DBpedia_eu();
                break;
            case "http://fr.dbpedia.org/resource/":
                ontology = new O_DBpedia_fr();
                break;
            default:
                break;
        }
        return ontology;
    }
    
    public static O getOntologyFromDefaultGraph(String defaultGraph) {
        O ontology = null;
        
        switch (defaultGraph) {            
            case "http://dbpedia.org":
                ontology = new O_DBpedia();
                break;
            case "https://query.wikidata.org":
                ontology = new O_WikiData();
                break;
            case "https://yago-knowledge.org":
                ontology = new O_Yago();
                break;
            case "http://de.dbpedia.org":
                ontology = new O_DBpedia_de();
                break;
            case "http://cs.dbpedia.org":
                ontology = new O_DBpedia_cs();
                break;
            case "http://el.dbpedia.org":
                ontology = new O_DBpedia_el();
                break;
            case "http://es.dbpedia.org":
                ontology = new O_DBpedia_es();
                break;
            case "http://eu.dbpedia.org":
                ontology = new O_DBpedia_eu();
                break;
            case "http://fr.dbpedia.org":
                ontology = new O_DBpedia_fr();
                break;
            default:
                break;
            
        }
        return ontology;
    } 

    public static List<O> getListOntologyFromPrefixes(List<String> listPrefixes) {
        if(listPrefixes == null)
            return null;
        
        List<O> ontologies = new ArrayList<>();
        for (String ontologyPrefix : listPrefixes) {
            O ontology = null;
            ontology = Ontology.getOntologyFromNameSpace(ontologyPrefix);
            if (ontology != null) {
                ontologies.add(ontology);
            }
        }
        return ontologies;
    }

    
    public static String getPrefixFromNamespace(R r) {
        return Ontology.getPrefixFromNamespace(r.getNamespace());
    }

    public static String getPrefixFromNamespace(URI uri) {
        return Ontology.getPrefixFromNamespace(uri.getNamespace());
    }

    public static String getPrefixFromNamespace(String uri) {
        
//        if(! uri.endsWith("/") || ! uri.endsWith("#")){
//               return uri;         
//        }
                    
        String prefix = prefixIndex.getValue(uri);
        
        if(prefix != null && prefix.contains(":") ){            
            return prefix;
        }
        
        prefix = prefixIndex.generateRandomKey(4);
        
        nameSpaceIndex.addValue(prefix , uri);
        prefixIndex.addValue(uri, prefix + ":");
        return prefix + ":";

    }

    public static String getNamespaceFromPrefix(String prefix) {
        
        String namespace = nameSpaceIndex.getValue(prefix);
        
        if(namespace != null){
               return namespace;       
        }
        
        return prefix + ":";
    }
    
    public static void loadIndexes(){
        manager = LdIndexerManager.getManager();
        
        try {
        prefixIndex = manager.loadIndex(prefixIndexPath); 
        nameSpaceIndex = manager.loadIndex(namespaceIndexPath);
        } 
        catch (Exception ex) {
                Logger.getLogger(Ontology.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        /*File prefixIndexFile = new File(prefixIndexPath);
        File nameSpaceIndexFile = new File(namespaceIndexPath);
        
        try {
            if(prefixIndexFile.exists()){
                prefixIndex = manager.loadIndex(prefixIndexPath);               
            }
            else {
                prefixIndex = manager.loadIndex(prefixIndexPath);
                updatePrefixIndex();
            }

            if(nameSpaceIndexFile.exists()){
                nameSpaceIndex = manager.loadIndex(namespaceIndexPath);                
            }
            else {
                nameSpaceIndex = manager.loadIndex(namespaceIndexPath);
                updateNameSpaceIndex();
            }
        } 
        catch (Exception ex) {
                Logger.getLogger(Ontology.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    public static void closeIndexes(){
        manager.closeIndex(prefixIndex);
        manager.closeIndex(nameSpaceIndex);
    }
    
    public static void updatePrefixIndex(){
               
        prefixIndex.addValue("http://dbpedia.org/resource/", "dbpedia:");
        prefixIndex.addValue("http://dbpedia.org/ontology/", "dbo:");
        prefixIndex.addValue("http://dbpedia.org/property/", "dbp:");
        prefixIndex.addValue("http://en.wikipedia.org/wiki/", "wiki:");
        prefixIndex.addValue("http://xmlns.com/foaf/0.1/", "foaf:");
        prefixIndex.addValue("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf:");
        prefixIndex.addValue("http://www.w3.org/ns/rdfa#", "rdfa:");
        prefixIndex.addValue("http://www.openlinksw.com/virtrdf-data-formats#", "rdfdf:");
        prefixIndex.addValue("http://www.w3.org/2000/01/rdf-schema#", "rdfs:");
        prefixIndex.addValue("http://www.w3.org/2002/07/owl#", "dbo:");
        prefixIndex.addValue("http://www.w3.org/XML/1998/namespace", "xml:");
        prefixIndex.addValue("http://www.w3.org/2001/XMLSchema#", "xsd:");
        prefixIndex.addValue("http://purl.org/dc/terms/", "dc:");
        prefixIndex.addValue("http://purl.org/linguistics/gold/", "gold:");
        prefixIndex.addValue("http://www.w3.org/ns/prov#", "prov:");
        prefixIndex.addValue("http://www.w3.org/ns/ldp#", "ldp:");
        prefixIndex.addValue("http://dbpedia.org/class/yago/", "dbo:");
        prefixIndex.addValue("http://www.wikidata.org/entity/", "wikidata:");
        prefixIndex.addValue("http://schema.org/", "schema:");
        prefixIndex.addValue("http://www.w3.org/2006/vcard/ns#", "vcard:");
        prefixIndex.addValue("http://rdf.freebase.com/ns/", "freebase:");                     

    }
    
    public static void updateNameSpaceIndex(){
        
        nameSpaceIndex.addValue("dbpedia", "http://dbpedia.org/resource/");
        nameSpaceIndex.addValue("dbo", "http://dbpedia.org/ontology/");
        nameSpaceIndex.addValue("dbp", "http://dbpedia.org/property/");
        nameSpaceIndex.addValue("wiki", "http://en.wikipedia.org/wiki/");
        nameSpaceIndex.addValue("foaf", "http://xmlns.com/foaf/0.1/");
        nameSpaceIndex.addValue("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        nameSpaceIndex.addValue("rdfa", "http://www.w3.org/ns/rdfa#");
        nameSpaceIndex.addValue("rdfdf", "http://www.openlinksw.com/virtrdf-data-formats#");
        nameSpaceIndex.addValue("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        nameSpaceIndex.addValue("owl", "http://www.w3.org/2002/07/owl#");
        nameSpaceIndex.addValue("xml", "http://www.w3.org/XML/1998/namespace");
        nameSpaceIndex.addValue("xsd", "http://www.w3.org/2001/XMLSchema#");
        nameSpaceIndex.addValue("dc", "http://purl.org/dc/terms/");
        nameSpaceIndex.addValue("gold", "http://purl.org/linguistics/gold/");
        nameSpaceIndex.addValue("prov", "http://www.w3.org/ns/prov#");
        nameSpaceIndex.addValue("ldp", "http://www.w3.org/ns/ldp#");
        nameSpaceIndex.addValue("yago", "http://dbpedia.org/resource/");
        nameSpaceIndex.addValue("wikidata", "http://www.wikidata.org/entity/");
        nameSpaceIndex.addValue("schema", "http://schema.org/");
        nameSpaceIndex.addValue("vcard", "http://www.w3.org/2006/vcard/ns#");
        nameSpaceIndex.addValue("freebase", "http://rdf.freebase.com/ns/");
        
    }

    public static String compressValue(Resource resource) {
        String nameSpace = resource.getNameSpace();
        String localName = resource.getLocalName();
        String r = resource.getURI();
        if (!nameSpace.endsWith("/") || !nameSpace.endsWith("#")) {
            if (r.contains("#")) {
                nameSpace = r.substring(0, r.lastIndexOf("#") + 1).trim();
                localName = r.substring(r.lastIndexOf("#") + 1).trim();
            } else {
                nameSpace = r.substring(0, r.lastIndexOf("/") + 1).trim();
                localName = r.substring(r.lastIndexOf("/") + 1).trim();
            }
        }
        //        if( nameSpace.equals(resource.getURI()) || nameSpace.charAt(nameSpace.length()-1) != '/'){
        //            nameSpace = nameSpace.substring(0 , nameSpace.lastIndexOf("/") + 1);
        //        }
        //
        String prefix = Ontology.getPrefixFromNamespace(nameSpace);
        if (prefix.equals(nameSpace) || prefix.equals(r) || !prefix.contains(":")) {
            return resource.getURI();
        }
        return prefix + localName;
    }

    public static String compressValue(URI uri) {
        String nameSpace = uri.getNamespace();
        String localName = uri.getLocalName();
        String r = uri.stringValue();
        if (!nameSpace.endsWith("/") || !nameSpace.endsWith("#")) {
            if (r.contains("#")) {
                nameSpace = r.substring(0, r.lastIndexOf("#") + 1).trim();
                localName = r.substring(r.lastIndexOf("#") + 1).trim();
            } else {
                nameSpace = r.substring(0, r.lastIndexOf("/") + 1).trim();
                localName = r.substring(r.lastIndexOf("/") + 1).trim();
            }
        }
        //        if( nameSpace.equals(uri.stringValue()) || nameSpace.charAt(nameSpace.length()-1) != '/'){
        //            nameSpace = nameSpace.substring(0 , nameSpace.lastIndexOf("/") + 1);
        //        }
        String prefix = Ontology.getPrefixFromNamespace(uri);
        if (prefix.equals(nameSpace) || prefix.equals(r) || !prefix.contains(":")) {
            return uri.toString();
        }
        return prefix + localName;
    }

    public static String compressValue(R r) {
        return compressValue(r.getUri());
    }
    
    public static String decompressValue(String value){
        if (value.contains("http") || !value.contains(":")) {
            return value;
        }
        
        String namespace = null, localName = null;
        try{
        String string[] =  value.split("\\:" , 2);
        namespace = Ontology.getNamespaceFromPrefix(string[0]);
        localName = string[1];
        }
        catch(Exception e){
            System.out.println("Error " + e.toString() + " when decompressing value: " + value);
        }
        return namespace + localName;
    }
    
       
}
