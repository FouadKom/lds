package lds.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.openrdf.model.URI;

import lds.measures.LdSimilarityMeasure;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import slib.graph.model.graph.G;
import slib.sml.sm.core.utils.SMconf;
import slib.utils.ex.SLIB_Ex_Critic;

public class LdSimilarityEngine extends slib.sml.sm.core.engine.SM_Engine {

	public static String LD_MEASURES_NS = "lds.measures.";

	LdDataset dataset;

	/**
	 * constructor for measure that needs only a graph (sub-ld graph that eventually
	 * connect compared resource)
	 * 
	 * @param g
	 * @throws SLIB_Ex_Critic
	 */
	public LdSimilarityEngine(G g) throws SLIB_Ex_Critic {
		super(g);
	}

	/**
	 * constructor for measure that needs LD dataset + graph (sub-ld graph that
	 * eventually connect compared resource)
	 * 
	 * @param d
	 * @param g
	 * @throws SLIB_Ex_Critic
	 */
	public LdSimilarityEngine(LdDataset d, G g) throws SLIB_Ex_Critic {
		super(g);
		this.dataset = d;
	}

	/**
	 * constructor for measure that needs only LD dataset
	 * 
	 * @param d
	 * @throws SLIB_Ex_Critic
	 */

	public LdSimilarityEngine(LdDataset d) throws SLIB_Ex_Critic {
		super(null);
		this.dataset = d;
	}
	//
	// public SM_LD_Engine(G g) {
	// this.graph = g;
	// this.dataset = null;
	// }
	//
	// public LdSimilarityMeasureBase(LdDataset d, G g) {
	// this.dataset = d;
	// this.graph = g;
	//
	// }
	//
	// public SM_LD_Engine(G g) throws SLIB_Ex_Critic {
	// super(g);
	// // TODO Auto-generated constructor stub
	// }

	@Override
	public double compare(SMconf pairwiseConf, URI a, URI b) throws SLIB_Ex_Critic {
		// load intended measure generically
		return 0;

	}

	// TODO: transform engine to a factory that inits contructor...

	// keep datasets management separated ? even cache ?
	// keep all that on ldq ?
	// store data locally ?
	// associate a local cache to online dataset on ldq ?&

	public double similarity(String measureName, R a, R b) throws SLIB_Ex_Critic {

		Class<?> measureClass;
		double score = 0;
		LdSimilarityMeasure ldMeasure = null;
		try {
			measureClass = Class.forName(LD_MEASURES_NS + measureName);
			Constructor<?> measureConstructor = measureClass.getConstructor();

			ldMeasure = (LdSimilarityMeasure) measureConstructor.newInstance();
		} // catch (ClassNotFoundException | IllegalAccessException |
			// IllegalArgumentException | InstantiationException | NoSuchMethodException |
			// SecurityException | InvocationTargetException e) {
		catch (Exception e) {
			e.printStackTrace();
		}

		score = ldMeasure.compare(a, b, this.getGraph());
		return score;

	}
}
