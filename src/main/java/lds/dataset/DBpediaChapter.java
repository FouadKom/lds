/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.dataset;

/**
 *
 * @author Fouad Komeiha
 */
public enum DBpediaChapter {
    Fr,
    En,
    Es,
    El,
    Cs,
    De,
    Eu;
    
    public static String getName(DBpediaChapter chapter){
        if(chapter == Fr){
            return "DBpedia_fr" ;
        }
        if(chapter == En){
            return "DBpedia_en" ;
        }
        if(chapter == Es){
            return "DBpedia_es" ;
        }
        if(chapter == El){
            return "DBpedia_el" ;
        }
        if(chapter == Cs){
            return "DBpedia_cs" ;
        }
        if(chapter == De){
            return "DBpedia_de" ;
        }
        if(chapter == Eu){
            return "DBpedia_eu" ;
        }
        
        return null;
    }
    
    
    public static String getServiceURI(DBpediaChapter chapter){
        if(chapter == Fr){
            return "http://fr.dbpedia.org/sparql" ;
        }
        if(chapter == En){
            return "http://dbpedia.org/sparql" ;
        }
        if(chapter == Es){
            return "http://es.dbpedia.org/sparql" ;
        }
        if(chapter == El){
            return "http://el.dbpedia.org/sparql" ;
        }
        if(chapter == Cs){
            return "http://cs.dbpedia.org/sparql" ;
        }
        if(chapter == De){
            return "http://de.dbpedia.org/sparql" ;
        }
        if(chapter == Eu){
            return "http://eu.dbpedia.org/sparql" ;
        }
        
        return null;
        
    }
    
    public static String getDefaultGraphURI(DBpediaChapter chapter){
        if(chapter == Fr){
            return "http://fr.dbpedia.org" ;
        }
        if(chapter == En){
            return "http://dbpedia.org" ;
        }
        if(chapter == Es){
            return "http://es.dbpedia.org" ;
        }
        if(chapter == El){
            return "http://el.dbpedia.org" ;
        }
        if(chapter == Cs){
            return "http://cs.dbpedia.org" ;
        }
        if(chapter == De){
            return "http://de.dbpedia.org" ;
        }
        if(chapter == Eu){
            return "http://eu.dbpedia.org" ;
        }
        
        return null;
        
    }
    
    public static String getResourceNameSpace(DBpediaChapter chapter){
        if(chapter == Fr){
            return "http://fr.dbpedia.org/resource/" ;
        }
        if(chapter == En){
            return "http://dbpedia.org/resource/" ;
        }
        if(chapter == Es){
            return "http://es.dbpedia.org/resource/" ;
        }
        if(chapter == El){
            return "http://el.dbpedia.org/resource/" ;
        }
        if(chapter == Cs){
            return "http://cs.dbpedia.org/resource/" ;
        }
        if(chapter == De){
            return "http://de.dbpedia.org/resource/" ;
        }
        if(chapter == Eu){
            return "http://eu.dbpedia.org/resource/" ;
        }
        
        return null;
        
    }
    
    public static String getOntologyNameSpace(DBpediaChapter chapter){
        if(chapter == Fr){
            return "http://fr.dbpedia.org/ontology/" ;
        }
        if(chapter == En){
            return "http://dbpedia.org/ontology/" ;
        }
        if(chapter == Es){
            return "http://es.dbpedia.org/ontology/" ;
        }
        if(chapter == El){
            return "http://es.dbpedia.org/ontology/" ;
        }
        if(chapter == Cs){
            return "http://cs.dbpedia.org/ontology/" ;
        }
        if(chapter == De){
            return "http://de.dbpedia.org/ontology/" ;
        }
        if(chapter == Eu){
            return "http://eu.dbpedia.org/ontology/" ;
        }
        
        return null;
    }
}
