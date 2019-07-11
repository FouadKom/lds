package lds.engine;

import java.io.FileNotFoundException;
import java.io.IOException;
import lds.measures.Measure;
import lds.resource.ResourcePair;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import lds.measures.LdSimilarityMeasure;
import lds.resource.R;
import slib.utils.i.Conf;



//public class LdSimilarityEngine extends slib.sml.sm.core.engine.SM_Engine {
public class LdSimilarityEngine {
        private LdSimilarityMeasure measure;

	public void load(Measure measureName, Conf config){
            Class<?> measureClass;
            LdSimilarityMeasure ldMeasure = null;
            try {
                    measureClass = Class.forName(Measure.getPath(measureName));
                    Constructor<?> measureConstructor = measureClass.getConstructor(Conf.class);
                    ldMeasure = (LdSimilarityMeasure) measureConstructor.newInstance(config);
                    this.measure = ldMeasure;
                    
                    ldMeasure.loadIndexes();


            } 
            catch (Exception e) {
                    e.printStackTrace();
            }

        }
        
        
        //normal similarity calculation of a pair of resources
        public double similarity(R a, R b){
            double score = 0;
            score = measure.compare(a, b);
            return score;
        }
        
        
       //calcuate the similariy for a list of pairs using multithreading
       public Map<String , Double> similarity(List<ResourcePair> resourcePairs) throws InterruptedException, ExecutionException {
       
//            ExecutorService executorService = Executors.newFixedThreadPool(5);
//            ExecutorService executorService = Executors.newCachedThreadPool();
//            ExecutorService executorService = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

            ThreadGroup threadGroup = new ThreadGroup("workers");
            
            ExecutorService executorService = Executors
                    .newFixedThreadPool(100 , new ThreadFactory() {
                  @Override
                  public Thread newThread(Runnable r) {
                    return new Thread(threadGroup, r);
                  }
                });
            
            Map<String , Double> map = new HashMap<>();

            List<Callable<String>> lst = new ArrayList<>();

            for(ResourcePair pair: resourcePairs){
                lst.add(new SimilarityCompareTask(measure , pair.getFirstresource() , pair.getSecondresource()));
            }

            // returns a list of Futures holding their status and results when all complete
            List<Future<String>> tasks = executorService.invokeAll(lst);
            
            System.out.println(threadGroup.activeCount() + " Active threads");
            
            for(Future<String> task : tasks)
            {
                String s[] = task.get().split("\\|");
                map.put( s[0]  , Double.parseDouble(s[1]));
            }

            executorService.shutdown();

            return map;        
        }
       
       
       //calculate the similatiy for a list of generated pairs from a file contatining list of resources and write the result to a new file
       public void similarity(String resourcesFilePath , String resultsFilePath) throws FileNotFoundException, IOException{
           
           List<String> resourceList  =  Utility.readListFromFile(resourcesFilePath);
           List<ResourcePair> pairs = Utility.generateRandomResourcePairs(resourceList);
           List<Double> results  = new ArrayList<>();
           
           double result = 0;
           
           for(ResourcePair pair : pairs){
               result = measure.compare(pair.getFirstresource() , pair.getSecondresource());
               results.add(result);
           }
          
           Utility.writeListToFile(results , resultsFilePath);
       }
        
        
        
        public void close(){
            measure.closeIndexes();
           
        }
        

	// TODO: transform engine to a factory that inits contructor...

	// keep datasets management separated ? even cache ?
	// keep all that on ldq ?
	// store data locally ?
	// associate a local cache to online dataset on ldq ?&
   
}
