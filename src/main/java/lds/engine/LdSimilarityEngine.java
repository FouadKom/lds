package lds.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import lds.LdManager.LdManager;
import lds.indexing.LdIndexer;

import org.openrdf.model.URI;

import lds.measures.LdSimilarityMeasure;
import lds.measures.resim.ResimLdManager;
import lds.measures.resim.Weight;
import lds.measures.resim.WeightMethod;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import slib.graph.model.graph.G;
import slib.sml.sm.core.utils.SMconf;
import slib.utils.ex.SLIB_Ex_Critic;
import slib.utils.i.Conf;

//public class LdSimilarityEngine extends slib.sml.sm.core.engine.SM_Engine {
public class LdSimilarityEngine {
        private LdSimilarityMeasure measure;

	/*public static String LD_MEASURES_NS = "lds.measures.";

	LdDataset dataset;

	public LdSimilarityEngine(G g) throws SLIB_Ex_Critic {
		super(g);
	}

	public LdSimilarityEngine(LdDataset d, G g) throws SLIB_Ex_Critic {
		super(g);
		this.dataset = d;
	}

	public LdSimilarityEngine(LdDataset d) throws SLIB_Ex_Critic {
		super(null);
		this.dataset = d;
	}
        
        public LdSimilarityEngine() throws SLIB_Ex_Critic {
		super(null);
	}*/
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

	/*@Override
	public double compare(SMconf pairwiseConf, URI a, URI b) throws SLIB_Ex_Critic {
		// load intended measure generically
		return 0;

	}*/
        
        
        
        public void load(Measure measureName, Conf config){
            Class<?> measureClass;
            LdSimilarityMeasure ldMeasure = null;
            try {
                    measureClass = Class.forName(Measure.getPath(measureName));
                    Constructor<?> measureConstructor = measureClass.getConstructor(Conf.class);
                    ldMeasure = (LdSimilarityMeasure) measureConstructor.newInstance(config);
                    this.measure = ldMeasure;
                    
                    ldMeasure.loadIndexes();


            } // catch (ClassNotFoundException | IllegalAccessException |
                    // IllegalArgumentException | InstantiationException | NoSuchMethodException |
                    // SecurityException | InvocationTargetException e) {
            catch (Exception e) {
                    e.printStackTrace();
            }

        }
        
        public double similarity(R a, R b){
            double score = 0;
            score = measure.compare(a, b);
            return score;
        }
        
        public void close(){
            measure.closeIndexes();
           
        }
        

	// TODO: transform engine to a factory that inits contructor...

	// keep datasets management separated ? even cache ?
	// keep all that on ldq ?
	// store data locally ?
	// associate a local cache to online dataset on ldq ?&
    
        /*public double similarity(Measure measureName, R a, R b , Conf config){
            Class<?> measureClass;
            double score = 0;
            LdSimilarityMeasure ldMeasure = null;
            try {
                    measureClass = Class.forName(Measure.getPath(measureName));
                    Constructor<?> measureConstructor = measureClass.getConstructor(Conf.class);
                    ldMeasure = (LdSimilarityMeasure) measureConstructor.newInstance(config);


            } // catch (ClassNotFoundException | IllegalAccessException |
                    // IllegalArgumentException | InstantiationException | NoSuchMethodException |
                    // SecurityException | InvocationTargetException e) {
            catch (Exception e) {
                    e.printStackTrace();
            }

            score = ldMeasure.compare(a, b);
            return score;
        }*/

        
        
}
