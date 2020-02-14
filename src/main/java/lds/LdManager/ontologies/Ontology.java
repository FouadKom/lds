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
import lds.indexing.LdIndexer;
import lds.measures.lods.ontologies.*;
import lds.resource.R;
import org.openrdf.model.URI;

/**
 *
 * @author Fouad Komeiha
 */
public class Ontology {
    private static LdIndexer manager;
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

    public static List<O> getListOntologyFromPrefixes(List<String> listPrefixes) {
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
        return Ontology.getPrefixFromNamespace(uri.getNamespace().trim());
    }

    public static String getPrefixFromNamespace(String uri) {
        prepareIndexes();
        
        String prefix = prefixIndex.getValue(uri);
        if(prefix != null && prefix.contains(":") ){            
            return prefix;
        }
        
        String p = Utility.getAlphaNumericString(4);
        
//        while(prefixIndex.getValue(p) != null){
//            p = Utility.getAlphaNumericString(6);
//        }
//        
        
        prefixIndex.addValue(uri, p+":");
        nameSpaceIndex.addValue(p , uri);
        return p+":";

//         return uri;
         
        /*switch (uri) {
            case "http://dbpedia.org/resource/":
                prefixIndex.addValue("http://dbpedia.org/resource/", "dbpedia:");
                return "dbpedia:";
            case "http://dbpedia.org/ontology/":
                prefixIndex.addValue("http://dbpedia.org/ontology/", "dbo:");
                return "dbo:";
            case "http://dbpedia.org/property/":
                prefixIndex.addValue("http://dbpedia.org/property/", "dbp:");
                return "dbp:";
            case "http://en.wikipedia.org/wiki/" :
                prefixIndex.addValue("http://en.wikipedia.org/wiki/", "wiki:");
                return "wiki:";
            case "http://xmlns.com/foaf/0.1/" :
                prefixIndex.addValue("http://xmlns.com/foaf/0.1/", "foaf:");
                return "foaf:";
            case "http://www.w3.org/1999/02/22-rdf-syntax-ns#":
                prefixIndex.addValue("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf:");
                return "rdf:";
            case "http://www.w3.org/ns/rdfa#":
                prefixIndex.addValue("http://www.w3.org/ns/rdfa#", "rdfa:");
                return "rdfa:";
            case "http://www.openlinksw.com/virtrdf-data-formats#":
                prefixIndex.addValue("http://www.openlinksw.com/virtrdf-data-formats#", "rdfdf:");
                return "rdfdf:";
            case "http://www.w3.org/2000/01/rdf-schema#":
                prefixIndex.addValue("http://www.w3.org/2000/01/rdf-schema#", "rdfs:");
                return "rdfs:";
            case "http://www.w3.org/2002/07/owl#":
                prefixIndex.addValue("http://www.w3.org/2002/07/owl#", "dbo:");
                return "owl:";
            case "http://www.w3.org/XML/1998/namespace":
                prefixIndex.addValue("http://www.w3.org/XML/1998/namespace", "xml:");
                return "xml:";
            case "http://www.w3.org/2001/XMLSchema#":
                prefixIndex.addValue("http://www.w3.org/2001/XMLSchema#", "xsd:");
                return "xsd:";
            case "http://purl.org/dc/terms/":
                prefixIndex.addValue("http://purl.org/dc/terms/", "dc:");
                return "dc:";
            case "http://purl.org/linguistics/gold/":
                prefixIndex.addValue("http://purl.org/linguistics/gold/", "gold:");
                return "gold:";
            case "http://www.w3.org/ns/prov#"  :
                prefixIndex.addValue("http://www.w3.org/ns/prov#", "prov:");
                return "prov:";
            case "http://www.w3.org/ns/ldp#" :
                prefixIndex.addValue("http://www.w3.org/ns/ldp#", "ldp:");
                return "ldp:";
            case "http://dbpedia.org/class/yago/":
                prefixIndex.addValue("http://dbpedia.org/class/yago/", "dbo:");
                return "yago:";
            case "http://www.wikidata.org/entity/":
                prefixIndex.addValue("http://www.wikidata.org/entity/", "wikidata:");
                return "wikidata:";
            case "http://schema.org/":
                prefixIndex.addValue("http://schema.org/", "schema:");
                return "schema:";
            case "http://www.w3.org/2006/vcard/ns#" :
                prefixIndex.addValue("http://www.w3.org/2006/vcard/ns#", "vcard:");
                return "vcard:";
            case "http://rdf.freebase.com/ns/" :
                prefixIndex.addValue("http://rdf.freebase.com/ns/", "freebase:");
                return "freebase:";
            default:
                String p = Utility.getAlphaNumericString(6);
                prefixIndex.addValue(uri, p+":");
                nameSpaceIndex.addValue(p , uri);
                return p+":";
//                return uri;
        }*/
    }

    public static String getNamespaceFromPrefix(String prefix) {
        prepareIndexes();
        
        String namespace = nameSpaceIndex.getValue(prefix);
        if(namespace != null && namespace.contains("http")){
               return namespace;       
        }
        
        return prefix;
        
        /*switch (prefix) {
            case "dbpedia" :
                nameSpaceIndex.addValue("dbpedia", "http://dbpedia.org/resource/");
                return "http://dbpedia.org/resource/";
            case "dbo":
                nameSpaceIndex.addValue("dbo", "http://dbpedia.org/ontology/");
                return "http://dbpedia.org/ontology/";
            case "dbp":
                nameSpaceIndex.addValue("dbp", "http://dbpedia.org/property/");
                return "http://dbpedia.org/property/";
            case "wiki" :
                nameSpaceIndex.addValue("wiki", "http://en.wikipedia.org/wiki/");
                return "http://en.wikipedia.org/wiki/";
            case "foaf" :
                nameSpaceIndex.addValue("foaf", "http://xmlns.com/foaf/0.1/");
                return "http://xmlns.com/foaf/0.1/";
            case "rdf":
                nameSpaceIndex.addValue("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
                return "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
            case "rdfa":
                nameSpaceIndex.addValue("rdfa", "http://www.w3.org/ns/rdfa#");
                return "http://www.w3.org/ns/rdfa#";
            case "rdfdf":
                nameSpaceIndex.addValue("rdfdf", "http://www.openlinksw.com/virtrdf-data-formats#");
                return "http://www.openlinksw.com/virtrdf-data-formats#";
            case "rdfs":
                nameSpaceIndex.addValue("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
                return "http://www.w3.org/2000/01/rdf-schema#";
            case "owl":
                nameSpaceIndex.addValue("owl", "http://www.w3.org/2002/07/owl#");
                return "http://www.w3.org/2002/07/owl#";
            case "xml":
                nameSpaceIndex.addValue("xml", "http://www.w3.org/XML/1998/namespace");
                return "http://www.w3.org/XML/1998/namespace";
            case "xsd":
                nameSpaceIndex.addValue("xsd", "http://www.w3.org/2001/XMLSchema#");
                return "http://www.w3.org/2001/XMLSchema#";
            case "dc":
                nameSpaceIndex.addValue("dc", "http://purl.org/dc/terms/");
                return "http://purl.org/dc/terms/";
            case "gold":
                nameSpaceIndex.addValue("gold", "http://purl.org/linguistics/gold/");
                return "http://purl.org/linguistics/gold/";
            case "prov"  :
                nameSpaceIndex.addValue("prov", "http://www.w3.org/ns/prov#");
                return "http://www.w3.org/ns/prov#";
            case "ldp" :
                nameSpaceIndex.addValue("ldp", "http://www.w3.org/ns/ldp#");
                return "http://www.w3.org/ns/ldp#";
            case "yago":
                nameSpaceIndex.addValue("yago", "http://dbpedia.org/resource/");
                return "http://dbpedia.org/class/yago/";
            case "wikidata":
                nameSpaceIndex.addValue("wikidata", "http://www.wikidata.org/entity/");
                return "http://www.wikidata.org/entity/";
            case "schema":
                nameSpaceIndex.addValue("schema", "http://schema.org/");
                return "http://schema.org/";
            case "vcard":
                nameSpaceIndex.addValue("vcard", "http://www.w3.org/2006/vcard/ns#");
                return "http://www.w3.org/2006/vcard/ns#";
            case "freebase":
                nameSpaceIndex.addValue("freebase", "http://rdf.freebase.com/ns/");
                return "http://rdf.freebase.com/ns/";    
            default:
                
                return prefix;
        }*/
    }
    
    public static void prepareIndexes(){
        manager = LdIndexer.getManager();
        File prefixIndexFile = new File(prefixIndexPath);
        File nameSpaceIndexFile = new File(namespaceIndexPath);
        
        try {
            if(! prefixIndexFile.exists()){
                prefixIndex = manager.loadIndex(prefixIndexPath);
//                updatePrefixIndex();
            }
            else
                prefixIndex = manager.loadIndex(prefixIndexPath);

            if(! nameSpaceIndexFile.exists()){
                nameSpaceIndex = manager.loadIndex(namespaceIndexPath);
//                updateNameSpaceIndex();
            }
            else
                nameSpaceIndex = manager.loadIndex(namespaceIndexPath);
        } 
        catch (Exception ex) {
                Logger.getLogger(Ontology.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
       
}
