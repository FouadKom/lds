/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lds.measures.lods.ontologies.*;
import lds.measures.weight.Weight;
import lds.measures.weight.WeightMethod;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;
import slib.graph.model.impl.repo.URIFactoryMemory;
import slib.graph.model.repo.URIFactory;

/**
 *
 * @author Fouad Komeiha
 */
public class Utility {
    
        public static Set<URI> toURI(List<String> list){
        Set<URI> listURI = new HashSet();
        URIFactory factory = URIFactoryMemory.getSingleton();
        if(list != null  && !list.contains("-1")){
            for(String value:list){
                listURI.add(factory.getURI(value));
            }

            return listURI;
        }
        return null;
            
        }
        
        public static List<String> toList(Set<URI> list){
            List<String> listString = new ArrayList<>();
            if(list != null){
                for(URI value:list){
                    listString.add(value.stringValue());
                }
                
                return listString;
            }
            return null;            
        }
        
        public static O getOntologyFromNameSpace(String nameSpace){
            O ontology = null;            
            
            switch(nameSpace){
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
        
        public static List<O> getListOntologyFromPrefixes(List<String> listPrefixes){
            List<O> ontologies= new ArrayList<>();
            
            for(String ontologyPrefix: listPrefixes){
                O ontology = null;
                ontology = Utility.getOntologyFromNameSpace(ontologyPrefix);
                
                if(ontology != null){
                    ontologies.add(ontology);
                }
            }
            
            return ontologies;
        }
        
    }
            
        
   
