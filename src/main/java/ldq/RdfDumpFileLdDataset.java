/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldq;

import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;


/**
 * The Class SparqlLdDataset.
 */
public class RdfDumpFileLdDataset extends LdDatasetBase implements LdDataset {
	
	Model model;

	public RdfDumpFileLdDataset() {

	}

	public ResultSet executeSelectQuery(String query) {
		initQueryExecution(query);
		return queryExecution.execSelect();

	}

	

	public boolean executeAskQuery(String query) {
		initQueryExecution(query);
		return  queryExecution.execAsk();
	}

	// TODO: see weather we need to call construct on dataset ?

	public Model executeConstructQuery(String query) {
		initQueryExecution(query);
		return queryExecution.execConstruct();
	}

	public Model executeDescribeQuery(String query) {
		initQueryExecution(query);
		return queryExecution.execDescribe();
	}

	public void close() {
		 queryExecution.close();

	}
	
	private void initQueryExecution(String query) {
		
		this.model = ModelFactory.createDefaultModel();
        FileManager.get().readModel(model , this.link);        
        queryExecution = QueryExecutionFactory.create(query, model);
		
	}

	

	// if (this.config.dataset_type == LdDatasetType.REMOTE_SPARQL_ENDPOINT)
	// else {
	// if (model == null) {
	// FileManager.get().addLocatorClassLoader(SparqlLdDataset.class.getClassLoader());
	// model = FileManager.get().loadModel(this.config.dataset_location);
	// }
	// return QueryExecutionFactory.create(query, model).execSelect();
	// }

	// TODO
	// if type is dump file and it does not exists locally download it
	// if url of file available in config file
	// store data locally / cache

}
