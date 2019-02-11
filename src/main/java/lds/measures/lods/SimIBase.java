package lds.measures.lods;

import java.util.HashSet;

import org.openrdf.model.URI;

import lds.measures.LdSimilarityMeasureBase;
import sc.research.ldq.LdDataset;
import slib.graph.model.graph.G;

public class SimIBase extends LdSimilarityMeasureBase implements SimI {


	public HashSet<String> getConcepts(G onology, URI r) {
		// TODO: ontology loaded by ?
		// get all super concepts (maybe use utilities provided by slib ?)
		return null;
	}

}
