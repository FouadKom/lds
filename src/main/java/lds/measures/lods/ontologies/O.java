package lds.measures.lods.ontologies;

import java.util.List;
import lds.config.Config;
import lds.resource.R;

public interface O {
	
    public List<String> getConcepts(R r); 
    
    public List<String> getCategories(R r); 
    
    public List<String> getBroaderCategories(R r , int level);
    
    public List<String> getNarrowerCategories(R r , int level);
    
    public void initializeOntology(Config config) throws Exception;
    
    public O getOntology();
    
    @Override
    public String toString();
    
    @Override
    public boolean equals(Object o);


}
