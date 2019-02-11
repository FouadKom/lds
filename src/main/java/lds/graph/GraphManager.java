/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.graph;

import slib.utils.ex.SLIB_Ex_Critic;
import java.util.Set;
import org.openrdf.model.URI;
import slib.graph.model.graph.G;
import slib.graph.model.graph.elements.E;
import slib.graph.model.graph.utils.Direction;
import slib.graph.model.impl.graph.memory.GraphMemory;
import slib.graph.model.impl.repo.URIFactoryMemory;
import slib.graph.model.repo.URIFactory;

/**
 *
 * @author Nasredine CHENIKI & Fouad Komeiha 
 */
public class GraphManager {
    
    public G generateGraph(String GraphURI) throws SLIB_Ex_Critic {

        URIFactory factory = URIFactoryMemory.getSingleton();
        URI uriGraph = factory.getURI(GraphURI); 
        G graph = new GraphMemory(uriGraph);

        return graph;

    }
    
    public Set<E> getIngouingEdges(G graph , String vertexURI){
        URIFactory factory = URIFactoryMemory.getSingleton();
        URI vertex = factory.getURI(vertexURI); 
        return graph.getE(vertex ,Direction.IN); 
        
    }
    
    public Set<E> getIngouingEdges(G graph , String vertexURI , String predicateURI){
        URIFactory factory = URIFactoryMemory.getSingleton();
        URI predicate = factory.getURI(predicateURI);
        URI vertex = factory.getURI(vertexURI);
        return graph.getE(predicate , vertex ,Direction.IN); 
        
    }
    
     public Set<E> getOutgouingEdges(G graph , String vertexURI){
        URIFactory factory = URIFactoryMemory.getSingleton();
        URI vertex = factory.getURI(vertexURI); 
        return graph.getE(vertex ,Direction.OUT); 
        
    }
     
     public Set<E> getOutgouingEdges(G graph , String vertexURI , String predicateURI){
        URIFactory factory = URIFactoryMemory.getSingleton();
        URI predicate = factory.getURI(predicateURI);
        URI vertex = factory.getURI(vertexURI);
        return graph.getE(predicate , vertex ,Direction.OUT); 
        
    }
     
     public Set<E> getEdges(G graph , String vertexURI){
        URIFactory factory = URIFactoryMemory.getSingleton();
        URI vertex = factory.getURI(vertexURI);
        return graph.getE(vertex ,Direction.BOTH); 
        
    }
     
    public Set<E> getEdges(G graph , String vertexURI , String predicateURI){
        URIFactory factory = URIFactoryMemory.getSingleton();
        URI predicate = factory.getURI(predicateURI);
        URI vertex = factory.getURI(vertexURI);
        return graph.getE(predicate, vertex, Direction.BOTH);
        
    }
    
    
    public void showVerticesAndEdges(G graph) {
        Set<URI> vertices = graph.getV();
        Set<E> edges = graph.getE();

        System.out.println("-Vertices");
        for(URI v : vertices){
            System.out.println("\t"+v);
        }

        System.out.println("-Edge");
        for(E edge : edges){
            System.out.println("\t"+edge);
        }
    }
    
    public void removeVertex(G graph , String vertexURI){
        URIFactory factory = URIFactoryMemory.getSingleton();
        URI vertex = factory.getURI(vertexURI);
        graph.removeV(vertex);
        System.out.println("Vertex \"" + vertexURI +"\" removed with all its associated edges");
    }
    
}
