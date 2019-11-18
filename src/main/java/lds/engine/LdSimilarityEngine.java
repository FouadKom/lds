package lds.engine;

import lds.benchmark.LdBenchmark;
import java.io.FileNotFoundException;
import java.io.IOException;
import lds.measures.Measure;
import lds.resource.LdResourcePair;
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
import java.util.concurrent.TimeUnit;
import lds.benchmark.Utility;
import lds.indexing.LdIndexer;
import lds.measures.LdSimilarityMeasure;
import lds.resource.LdResourceTriple;
import lds.resource.LdResult;
import lds.resource.R;
import slib.utils.i.Conf;



//public class LdSimilarityEngine extends slib.sml.sm.core.engine.SM_Engine {
public class LdSimilarityEngine {
    
        private LdSimilarityMeasure measure;
        private Measure measureName;
        private Conf config;
        private LdIndexer resultsIndex;
        

	public void load(Measure measureName, Conf config){
            
            this.config = config;
            this.measureName = measureName;
            
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
        
        
        public void close(){
            measure.closeIndexes();
           
        }     
        
        
       //calcuate the similariy for a list of pairs using multithreading
       public List<LdResult> similarity(List<LdResourceTriple> resourceTriples , String resultsFilePath) throws InterruptedException, ExecutionException {
           
//           ExecutorService executorService = Executors.newFixedThreadPool(resourcePairs.size());
             ExecutorService executorService = Executors.newFixedThreadPool(10);
//            ExecutorService executorService = Executors.newCachedThreadPool();
//            ExecutorService executorService = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

//            ThreadGroup threadGroup = new ThreadGroup("workers");
//            
//            ExecutorService executorService = Executors
//                    .newFixedThreadPool(6 , new ThreadFactory() {
//                  @Override
//                  public Thread newThread(Runnable r) {
//                    return new Thread(threadGroup, r);
//                  }
//                });

            List<LdResult> resultList = new ArrayList<>();

            List<Callable<String>> lst = new ArrayList<>();
        
            for(LdResourceTriple triple: resourceTriples){
                if(triple.getSimilarityResult() < 0){
                    lst.add(new SimilarityCompareTask(measure.getMeasure() , triple , resultsFilePath));
                }
            }

            // returns a list of Futures holding their status and results when all complete
            List<Future<String>> tasks = executorService.invokeAll(lst);

//            System.out.println(threadGroup.activeCount() + " Active threads");

            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(10, TimeUnit.HOURS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException ex) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }

            for(Future<String> task : tasks)
            {

                if(task.isDone()){
                    String s[] = task.get().split(",");
                    resultList.add( new LdResult( new R(s[0].trim()) , new R(s[1].trim()) , Double.parseDouble(s[2].trim()) , Double.parseDouble(s[3].trim()) ));
                }

            }

           return resultList;
        }
              
       
       //calcuate the similariy for a list of pairs using multithreading
       public void similarity2(List<LdResourcePair> resourcePairs) throws Exception{
           int i = 0;
           
           String resultsIndexFile = System.getProperty("user.dir") + "/Indexes/" + measureName.toString() + "_MultiThreading_Results/results_index.db";
           resultsIndex = new LdIndexer(resultsIndexFile);
           
           SimilarityCompareTaskRunnable[] threads = new SimilarityCompareTaskRunnable[resourcePairs.size()];
           
           for(LdResourcePair pair: resourcePairs){
                threads[i] = new SimilarityCompareTaskRunnable(measure.getMeasure() , pair.getFirstresource() , pair.getSecondresource() , resultsIndex);
//                threads[i] = new SimilarityCompareTaskRunnable(this.measure , pair.getFirstresource() , pair.getSecondresource() , resultsIndex);
                threads[i].start();
                
                i++;
           }
           
           try{
               for(int j = 0 ; j < i ; j++){
                   threads[j].join();
               }
           }catch(InterruptedException ie) {
               ie.printStackTrace();
           }
           
           resultsIndex.close();
           
       }
       
       //calculate the similatiy for a list of generated pairs from a file contatining list of resources and write the result to a new file
       public void similarity(String resourcesFilePath , boolean useMultiThreading) throws FileNotFoundException, IOException, InterruptedException, ExecutionException{
           
           double startTime , endTime , duration = 0;
                             
           List<LdResourceTriple> triples = LdBenchmark.readListFromFile(resourcesFilePath);
           
           if(triples == null)
               return;
           
           String resultsFilePath = LdBenchmark.getResultFilePath(resourcesFilePath);
           
           LdResult result = null;
           
           double similarityResult = 0;
           
           if(useMultiThreading)
               similarity(triples , resultsFilePath);
           
           else{
               for(LdResourceTriple triple : triples){

                   if(triple.getSimilarityResult() < 0){

                       startTime = System.nanoTime();

                       similarityResult = measure.compare(triple.getResourcePair().getFirstresource() , triple.getResourcePair().getSecondresource());

                       endTime = System.nanoTime();
                       duration = (endTime - startTime) / 1000000000 ;

                       triple.setSimilarityResult(similarityResult);
                       
                       result = new LdResult(triple , duration);
                       
                       LdBenchmark.writeResultsToFile(result , resultsFilePath);
                   }              

               }
           }

           
       }
       
        
        

	// TODO: transform engine to a factory that inits contructor...

	// keep datasets management separated ? even cache ?
	// keep all that on ldq ?
	// store data locally ?
	// associate a local cache to online dataset on ldq ?&
   
}
