/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager.ontologies;

import java.util.ArrayList;
import java.util.List;
import lds.measures.lods.ontologies.*;
import lds.resource.R;
import org.openrdf.model.URI;

/**
 *
 * @author Fouad Komeiha
 */
public class Ontology {

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
        switch (uri.trim()) {
            case "http://dbpedia.org/resource/":
                return "dbpedia:";
            case "http://dbpedia.org/ontology/":
                return "dbo:";
            case "http://dbpedia.org/property/":
                return "dbp:";
            case "http://en.wikipedia.org/wiki/" :
                return "wiki:";
            case "http://xmlns.com/foaf/0.1/" :
                return "foaf:";
            case "http://www.w3.org/1999/02/22-rdf-syntax-ns#":
                return "rdf:";
            case "http://www.w3.org/ns/rdfa#":
                return "rdfa:";
            case "http://www.openlinksw.com/virtrdf-data-formats#":
                return "rdfdf:";
            case "http://www.w3.org/2000/01/rdf-schema#":
                return "rdfs:";
            case "http://www.w3.org/2002/07/owl#":
                return "owl:";
            case "http://www.w3.org/XML/1998/namespace":
                return "xml:";
            case "http://www.w3.org/2001/XMLSchema#":
                return "xsd:";
            case "http://purl.org/dc/terms/":
                return "dc:";
            case "http://purl.org/linguistics/gold/":
                return "gold:";
            case "http://www.w3.org/ns/prov#"  :
                return "prov:";
            case "http://www.w3.org/ns/ldp#" :
                return "ldp:";
            case "http://dbpedia.org/class/yago/":
                return "yago:";
            case "http://www.wikidata.org/entity/":
                return "wikidata:";
            case "http://schema.org/":
                return "schema:";
            case "http://www.w3.org/2006/vcard/ns#" :
                return "vcard:";
            case "http://rdf.freebase.com/ns/" :
                return "freebase:";
            default:
                return uri;
        }
    }

    public static String getNamespaceFromPrefix(String prefix) {
        switch (prefix) {
            case "dbpedia" :
                return "http://dbpedia.org/resource/";
            case "dbo":
                return "http://dbpedia.org/ontology/";
            case "dbp":
                return "http://dbpedia.org/property/";
            case "wiki" :
                return "http://en.wikipedia.org/wiki/";
            case "foaf" :
                return "http://xmlns.com/foaf/0.1/";
            case "rdf":
                return "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
            case "rdfa":
                return "http://www.w3.org/ns/rdfa#";
            case "rdfdf":
                return "http://www.openlinksw.com/virtrdf-data-formats#";
            case "rdfs":
                return "http://www.w3.org/2000/01/rdf-schema#";
            case "owl":
                return "http://www.w3.org/2002/07/owl#";
            case "xml":
                return "http://www.w3.org/XML/1998/namespace";
            case "xsd":
                return "http://www.w3.org/2001/XMLSchema#";
            case "dc":
                return "http://purl.org/dc/terms/";
            case "gold":
                return "http://purl.org/linguistics/gold/";
            case "prov"  :
                return "http://www.w3.org/ns/prov#";
            case "ldp" :
                return "http://www.w3.org/ns/ldp#";
            case "yago":
                return "http://dbpedia.org/class/yago/";
            case "wikidata":
                return "http://www.wikidata.org/entity/";
            case "schema":
                return "http://schema.org/";
            case "vcard":
                return "http://www.w3.org/2006/vcard/ns#";
            case "freebase":
                return "http://rdf.freebase.com/ns/";    
            default:
                return prefix;
        }
    }
    
    
    
}
