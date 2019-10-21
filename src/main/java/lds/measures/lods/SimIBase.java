package lds.measures.lods;

import java.util.HashSet;

import org.openrdf.model.URI;

import lds.measures.LdSimilarityMeasure;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import slib.graph.model.graph.G;

public class SimIBase implements SimI , LdSimilarityMeasure{


	public HashSet<String> getConcepts(G onology, URI r) {
		// TODO: ontology loaded by ?
		// get all super concepts (maybe use utilities provided by slib ?)
		return null;
	}

    @Override
    public double compare(R a, R b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void closeIndexes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadIndexes() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LdSimilarityMeasure getMeasure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
