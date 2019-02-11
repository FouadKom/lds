package lds.benchmark;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.junit.Test;

import lds.graph.LdGraphManager;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.graph.model.graph.G;
import slib.indexer.IndexHash;
import slib.utils.ex.SLIB_Ex_Critic;

public class Benchmarking {

	@Test
	public void test() throws IOException, SLIB_Ex_Critic, ClassNotFoundException {

		// bench of pairs <r1,r2>

		// B benchmark = BenchmarkFactory.getInstance()
		// .file("")
		// .baseUri("")
		// .create();
		//
		// // Combiner bench,
		// B benchmark = BenchmarkFactory.getInstance()
		// .file1("")
		// .file2("")
		// .baseUri("")
		// .create();

		String resources_file1 = System.getProperty("user.dir") + "/src/test/java/resources/resources_twitter_10.csv";
		String resources_file2 = System.getProperty("user.dir") + "/src/test/java/resources/resources_youtube_10.csv";

		File f1 = new File(resources_file1);
		File f2 = new File(resources_file2);

		System.out.println("Reading files using Apache IO:");

		Set<String> resources_map_1 = new HashSet<String>(FileUtils.readLines(f1, "UTF-8"));
		Set<String> resources_map_2 = new HashSet<String>(FileUtils.readLines(f2, "UTF-8"));

		// for each element in resources_map_1
		// get resources graph then : store it at a local jena TDB

		LdDataset dbpedia_dataset = null;
		PrefixMapping prefixes = new PrefixMappingImpl();

		// TODO: create local dataset config
		prefixes.setNsPrefix("dbpedia", "http://dbpedia.org/resource/");
		prefixes.setNsPrefix("dbpedia-owl", "http://dbpedia.org/ontology/");

		try {
			dbpedia_dataset = LdDatasetFactory.getInstance().service("http://dbpedia.org/sparql").name("dbpedia")
					.defaultGraph("http://dbpedia.org").prefixes(prefixes).create();

		} catch (Exception e) {

			fail("Error with dataset:" + e.getMessage());
		}

		LdGraphManager graphManager = new LdGraphManager();

		String cache_dir = "/Users/nasredine/dev-tmp/cache/";
		String resource_uri_base = "http://dbpedia.org/resource/";

		for (String resource1 : resources_map_1) {

			G graph = graphManager.generateGraph("http://dbpedia.org/resource/");

			String filename = cache_dir + resource1 + ".obj";

			if (new File(filename).exists()) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
				graph = (G) ois.readObject(); // cast is needed.
				ois.close();
			}

			else {
				R r = LdResourceFactory.getInstance().uri(resource_uri_base + resource1).create();

				LdGraphManager.getInOutResources(graph, r, 1, dbpedia_dataset);
				// store object
				// try , in cache delete the file if was created
				
				
				
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
				oos.writeObject(graph);
				oos.close();
				//
				graph = null;
			}

		}

		// for (String resource2 : resources_map_2) {
		// System.out.println(resource2 + "*" + resource2);
		// }

		// Map<R,R> resources = benchmark.getResources();

		//
		//
		// // 2 types of benchmarks r combiner with the rest of resources OR
		// // r1 combiner with r2 ...
		// Map<R> resources = benchmark.getResources();
		// Map<R,R> resources = benchmark.getResources();
		//
		// LdLoader loader = LodsLoader().load(resources); // standard loader ?
		//
		//
		// Evaluation e = EvaluationFactory.getInstance()
		// .
	}

}
