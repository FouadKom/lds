package lds.measures.lods;

import java.util.HashSet;

import org.openrdf.model.URI;

import slib.graph.model.graph.G;

public interface SimI {
	
	public HashSet<String> getConcepts(G onology, URI r);

}
