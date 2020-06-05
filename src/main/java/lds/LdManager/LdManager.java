package lds.LdManager;

import java.util.List;
import java.util.Set;
import lds.resource.R;
import org.openrdf.model.URI;


public interface LdManager {
        
	
	boolean isSameAs(R a, R b); 
         
        
        boolean isDirectlyConnected(URI link , R a, R b);
        
        
        int countShareCommonObjects(URI link , R a);
        
        int countShareCommonSubjects(URI link , R a);
        
        
        int countShareCommonObjects(URI link , R a , R b);
        
        int countShareCommonSubjects(URI link , R a , R b);
        
        
        int countShareTyplessCommonObjects(URI link1 , URI link2 , R a);
        
        int countShareTyplessCommonSubjects(URI link1 , URI link2 , R a);
        

        int countShareTyplessCommonObjects(URI link1 , URI link2 , R a , R b);
        
        int countShareTyplessCommonSubjects(URI link1 , URI link2 , R a , R b);
        
        
        int countObject(R a);        
        
        int countSubject(R a ) ;
        
        
        int countObject(URI link , R a);
        
        int countSubject(URI link , R a );
        

        int countPropertyOccurrence(URI link);
        
        int countResource();
        
        
        List<String> getIngoingEdges(R a);
        
        List<String> getOutgoingEdges(R a);
        
        Set<URI> getEdges(R a , R b);
        
        
        List<String> getTyplessCommonSubjects(R a , R b);
        
        List<String> getTyplessCommonObjects(R a , R b);
        
        
        List<String> getCommonSubjects(R a , R b);
        
        List<String> getCommonObjects(R a , R b);

}