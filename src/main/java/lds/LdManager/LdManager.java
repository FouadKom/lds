package lds.LdManager;

import java.util.List;

import org.apache.commons.httpclient.HttpException;

import lds.resource.R;
import sc.research.ldq.LdDataset;
import slib.graph.model.graph.G;
import slib.utils.ex.SLIB_Ex_Critic;

public interface LdManager {
	
	public G generateGraph(LdDataset dataset, R a, R b, String graphURI) throws HttpException, SLIB_Ex_Critic;

	boolean isSameAs(R a, R b);
}