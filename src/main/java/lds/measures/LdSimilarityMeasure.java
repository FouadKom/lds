package lds.measures;

import org.openrdf.model.URI;

import lds.resource.R;
import slib.graph.model.graph.G;




public interface LdSimilarityMeasure {
    
	public double compare(R a, R b, G g) ;
	public double compare(R a, R b) ;

}
