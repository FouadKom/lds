package lds.measures;

import lds.resource.R;
import slib.graph.model.graph.G;




public interface LdSimilarity {
    
	public double compare(R a, R b) ;
//        public double compare(R a , R b , int w1 , int w2);
        
        public void closeIndexes();
        public void loadIndexes() throws Exception;
        
        public LdSimilarity getMeasure();

}
