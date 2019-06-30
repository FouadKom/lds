package lds.measures;


import lds.resource.R;


public class LdSimilarityMeasureBase implements LdSimilarityMeasure {

        @Override
	public double compare(R a, R b) {
		// TODO Auto-generated method stub
		return 0;
	}

        @Override
        public double compare(R a, R b, int w1 , int w2) {
            // TODO Auto-generated method stub
                    return 0;
        }

    @Override
    public void closeIndexes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadIndexes() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
