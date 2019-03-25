package lds.LdManager;

import java.util.List;

import org.apache.commons.httpclient.HttpException;

import lds.resource.R;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;
import slib.graph.model.graph.G;
import slib.utils.ex.SLIB_Ex_Critic;

public interface LdManager {
	
	public G generateGraph(LdDataset dataset, R a, R b, String graphURI) throws HttpException, SLIB_Ex_Critic;

	boolean isSameAs(R a, R b);
        
        boolean isDirectlyConnected(URI link , R a, R b);
        
        boolean shareCommonObject(URI link , R a, R b);
        
        boolean shareCommonSubject(URI link , R a, R b);
        

        int countShareCommonObjects(URI link , R a);
        
        int countShareCommonSubjects(URI link , R a);
        
        int countIngoingEdges(R a ) ;
        
        int countIngoingEdges(URI link , R a );

        int countOutgoingEdges(R a);

        int countOutgoingEdges(URI link , R a);
}