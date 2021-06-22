/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldq;

import java.io.IOException;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdtjena.HDTGraph;

public class HDTFileLdDataset extends LdDatasetBase implements LdDataset {

	HDT hdt;
	String hdt_path;
	HDTGraph graph;
	Model model;

	public HDTFileLdDataset(String hdt_path) {
		this.hdt_path = hdt_path;
		init();
	}

	private void init() {
		try {
			hdt = HDTManager.mapIndexedHDT(hdt_path, null);
			graph = new HDTGraph(hdt);
			model = ModelFactory.createModelForGraph(graph);
			System.out.println("HDT loaded!");
		} catch (Exception e) {
			System.out.println("Halt! HDT not loaded!");
		}

	}

	public ResultSet executeSelectQuery(String query) {
		Query HDTquery = QueryFactory.create(query);
        QueryExecution queryExec = QueryExecutionFactory.create(HDTquery, model);
        ResultSet results = queryExec.execSelect();
       return results;
	}

	public Model executeConstructQuery(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean executeAskQuery(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	public Model executeDescribeQuery(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

}
