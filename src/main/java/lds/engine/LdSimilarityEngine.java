package lds.engine;

import java.io.FileNotFoundException;
import java.io.IOException;
import lds.measures.Measure;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lds.benchmark.LdBenchmark;
import lds.benchmark.BenchmarkFile;
import lds.config.Config;
import lds.config.LdConfigFactory;
import lds.resource.LdResourceTriple;
import lds.resource.LdResult;
import lds.resource.R;
import lds.measures.LdSimilarity;



public class LdSimilarityEngine {
    
        private LdSimilarity measure;
        

	public void load(Measure measureName, Config config){
            
            Class<?> measureClass;
            LdSimilarity ldMeasure = null;
            try {
                    measureClass = Class.forName(Measure.getPath(measureName));
                    Constructor<?> measureConstructor = measureClass.getConstructor(Config.class);
                    ldMeasure = (LdSimilarity) measureConstructor.newInstance(config);
                    this.measure = ldMeasure;

                    ldMeasure.loadIndexes();


            } 
            catch (Exception e) {
                    e.printStackTrace();
            }


        }
        
        public void load(Measure measureName){
            
            load(measureName , LdConfigFactory.createDefaultConf(measureName));


        }
        
        
        //normal similarity_MultiThreads calculation of a pair of resources
        public double similarity(R a, R b){
            double score = 0;
            score = measure.compare(a, b);
            return score;
        }
        
        
        public void close(){
            measure.closeIndexes();
           
        }
        
        public void similarity(BenchmarkFile source , BenchmarkFile results , int threadsNum , boolean skipCalculated ) throws IOException, FileNotFoundException, InterruptedException, ExecutionException{
           LdBenchmark benchmark = new LdBenchmark(source , results);
           similarity(benchmark , threadsNum , skipCalculated );
           
        }
        
        public void similarity(BenchmarkFile source , int threadsNum , boolean skipCalculated ) throws IOException, FileNotFoundException, InterruptedException, ExecutionException{
           LdBenchmark benchmark = new LdBenchmark(source);
           similarity(benchmark , threadsNum , skipCalculated );
           
        }
       
      
        public void similarity(LdBenchmark benchmark , int threadsNum , boolean skipCalculated) throws FileNotFoundException, IOException, InterruptedException, ExecutionException{
                                        
           List<LdResourceTriple> SourceTriples = benchmark.readFromFile(skipCalculated);
                      
           if(SourceTriples == null)
               return;
           
           BenchmarkFile resultsFile = benchmark.getResultsFile();
           
           if(threadsNum == 0 || threadsNum == 1){
               similarity_SingleThread(SourceTriples , resultsFile);
           }
           
           else{
               similarity_MultiThreads(SourceTriples , resultsFile , threadsNum);
           }
           
        }
       
        public void similarity(LdBenchmark benchmark , boolean skipCalculated) throws FileNotFoundException, IOException, InterruptedException, ExecutionException{
           similarity(benchmark , 1 , skipCalculated);
        } 
        
        public void similarity(LdBenchmark benchmark) throws FileNotFoundException, IOException, InterruptedException, ExecutionException{
           similarity(benchmark , 1 , false);
        } 
        
        
       //calcuate the similarity_MultiThreads for a list of pairs using multithreading
       private List<LdResult> similarity_MultiThreads(List<LdResourceTriple> resourceTriples , BenchmarkFile resultsFile , int threadsNum) throws InterruptedException, ExecutionException {
          
            ExecutorService executorService = Executors.newFixedThreadPool(threadsNum);

            List<LdResult> resultList = new ArrayList<>();

            List<Callable<String>> lst = new ArrayList<>();
        
            for(LdResourceTriple triple: resourceTriples){
                if(triple.getSimilarityResult() < 0){
                    lst.add(new SimilarityCompareTask(measure.getMeasure() , triple , resultsFile));
                }
            }

            List<Future<String>> tasks = executorService.invokeAll(lst);

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
       
       private void similarity_SingleThread(List<LdResourceTriple> resourceTriples , BenchmarkFile resultsFile) throws IOException{
           double startTime , endTime , duration = 0;
           double similarityResult = 0;
           
           for(LdResourceTriple triple : resourceTriples){

//               if(triple.getSimilarityResult() < 0){

                   startTime = System.nanoTime();

                   similarityResult = measure.compare(triple.getResourcePair().getFirstresource() , triple.getResourcePair().getSecondresource());

                   endTime = System.nanoTime();
                   duration = (endTime - startTime) / 1000000000 ;

                   triple.setSimilarityResult(similarityResult);

                   LdResult result = new LdResult(triple , duration);

                   LdBenchmark.writeResultsToFile(result , resultsFile);
//               }
               
//               LdBenchmark.writeResultsToFile(new LdResult(triple , duration) , resultsFile);

           }
       }
       
      
       
       
       
       
       public double correlation(LdBenchmark benchmark , int threadsNum , boolean skipCalculated) throws FileNotFoundException, IOException, InterruptedException, ExecutionException{
           
           similarity(benchmark , threadsNum , skipCalculated);
           
           return benchmark.calculateCorrelation();                             
           
       }
       
       public double correlation(LdBenchmark benchmark , boolean skipCalculated) throws FileNotFoundException, IOException, InterruptedException, ExecutionException{
           
           similarity(benchmark , 1 , skipCalculated);
           
           return benchmark.calculateCorrelation();                             
           
       }
       
       
       public double correlation(LdBenchmark benchmark, int threadsNum) throws FileNotFoundException, IOException, InterruptedException, ExecutionException{
           
           similarity(benchmark , threadsNum , false);
           
           return benchmark.calculateCorrelation();                             
           
       }
       
       
       public double correlation(LdBenchmark benchmark) throws FileNotFoundException, IOException, InterruptedException, ExecutionException{
           
           similarity(benchmark , 1 , false);
           
           return benchmark.calculateCorrelation();                             
           
       }

    public LdSimilarity getMeasure() {
        return measure;
    }

    public void setMeasure(LdSimilarity measure) {
        this.measure = measure;
    }

    // TODO: transform engine to a factory that inits contructor...

	// keep datasets management separated ? even cache ?
	// keep all that on ldq ?
	// store data locally ?
	// associate a local cache to online dataset on ldq ?&
   
}
