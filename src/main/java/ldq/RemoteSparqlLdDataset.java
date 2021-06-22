/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldq;

import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

/**
 * The Class SparqlLdDataset.
 */
public class RemoteSparqlLdDataset extends LdDatasetBase implements LdDataset {

	public RemoteSparqlLdDataset() {

	}

	public ResultSet executeSelectQuery(String query) {
		initQueryExecution(query);
		return QueryExecutionFactory.sparqlService(this.link, query).execSelect();

	}

	private void initQueryExecution(String query) {
		queryExecution = QueryExecutionFactory.create(query);

	}

	public boolean executeAskQuery(String query) {
		initQueryExecution(query);
		return QueryExecutionFactory.sparqlService(this.link, query).execAsk();
	}

	public Model executeConstructQuery(String query) {
		initQueryExecution(query);
		return QueryExecutionFactory.sparqlService(this.link, query).execConstruct();
	}

	public Model executeDescribeQuery(String query) {
		initQueryExecution(query);
		return QueryExecutionFactory.sparqlService(this.link, query).execDescribe();
	}

	public void close() {
		queryExecution.close();

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
