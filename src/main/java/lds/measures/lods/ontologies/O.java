package lds.measures.lods.ontologies;

import java.util.List;
import lds.resource.R;
import slib.utils.i.Conf;

public interface O {
	
    public List<String> getConcepts(R r); 
    
    public void initializeOntology(Conf config) throws Exception;
    
    
    public O getOntology();
    
    @Override
    public String toString();
    
    @Override
    public boolean equals(Object o);


}
