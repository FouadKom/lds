/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldq;

import java.io.IOException;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.shared.PrefixMapping;

public interface LdDataset {
	
	public ResultSet executeSelectQuery(String query); 
	public Model executeConstructQuery(String query);
	public boolean executeAskQuery(String query);
	public Model executeDescribeQuery(String query);
	public ParameterizedSparqlString prepareQuery();
	
	public String getName();
	public void setName(String name);
	
	public PrefixMapping getPrefixes();

	public void setPrefixes(PrefixMapping prefixes);

	public String getLink();

	public void setLink(String location);
	public String getQuery(String name) throws IOException;
	public void setPath(String path);
	public void close();
	public String getDefaultGraph();
	public void setDefaultGraph(String g);

}
