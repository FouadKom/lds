package lds.benchmark;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import static lds.benchmark.Utility.checkFile;
import static lds.benchmark.Utility.parseLine;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import lds.resource.LdResourcePair;
import lds.resource.LdResourceTriple;
import lds.resource.LdResult;
import org.openrdf.model.URI;
import slib.graph.model.graph.G;
import slib.graph.model.graph.elements.E;

public class LdBenchmark {  
    
    
    public double checkCorrelation(String listPath , String benchMarkPath) throws FileNotFoundException, IOException{
        if( ! Utility.checkFile(listPath) && ! Utility.checkFile(benchMarkPath) )
            return -1;

        List<String> list = readResultsFromFile(listPath);
        List<String> benchMark = LdBenchmark.readRowsFromFile(benchMarkPath);

        return calculateCorrelation(list , benchMark);

    }
    
    public static List<String> readRowsFromFile(String filePath) throws FileNotFoundException{
        if(! Utility.checkFile(filePath) )
            return null;
        
        List<String> resourceList = new ArrayList<>();
        
        File file =  new File(filePath); 
        Scanner sc = new Scanner(file); 
  
        while (sc.hasNextLine()){
          String[] splited = sc.nextLine().split("\\s+");
          
          if(Utility.isNumeric(splited[1]))
            resourceList.add(splited[1]);
        }
        
        return resourceList;
    }
    
    
    public static List<String> readResultsFromFile(String filePath) throws IOException{
        if(! Utility.checkFile(filePath) )
            return null;
        
        List<String> results = new ArrayList<>();
        
        Map<String , LdResourceTriple> resultsList = readListFromCsvFile(filePath);
        
        for(Map.Entry<String, LdResourceTriple> entry : resultsList.entrySet()){
            results.add(Double.toString(entry.getValue().getSimilarityResult()));
        }
        
        return results;
        
    }
        
    
    public static synchronized void writeResultsToFile(LdResult results , String filePath) throws IOException{
        if(! Utility.checkPath(filePath) )
            return;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:ms");
        Date date = new Date();

        String results_TimeFilePath = filePath.replace(".csv", "_Duration.csv");
        
        FileWriter duration_writer = new FileWriter(results_TimeFilePath , true);
        FileWriter results_writer = new FileWriter(filePath , true);
        
        results_writer.write(results.getResourceTriple().toString('|'));
        results_writer.write(System.getProperty("line.separator"));
        
        duration_writer.write(results.toString('|') + " | Written on: " + dateFormat.format(date));
        duration_writer.write(System.getProperty("line.separator"));
        
        results_writer.close();
        duration_writer.close();

    }
    
    /*public static synchronized void updateFile(LdResourceTriple triples , String filePath) throws FileNotFoundException, IOException {
         if(! Utility.checkFile(filePath) )
            return;
         
        Scanner scanner = new Scanner(new File(filePath)); 
        
        String newfirstResource = triples.getResourcePair().getFirstresource().getUri().toString();
        String newSecondResource = triples.getResourcePair().getSecondresource().getUri().toString();
        String newResult = Double.toString(triples.getSimilarityResult());
        
        String tempfilePath = filePath.replace(".csv" , "_temp.csv");
        
        R firstResource = null;
        R secondResource = null;
        double result = 0;
        
        
        while (scanner.hasNext()) {
              List<String> line = parseLine(scanner.nextLine());
              
              if(newfirstResource.equals(line.get(0).trim()) && newSecondResource.equals(line.get(1).trim()))
                    writeTriplesToFile(triples , tempfilePath);
              
              else{
                firstResource  = new R(line.get(0).trim());
                secondResource = new R(line.get(1).trim());
                result = Double.parseDouble(line.get(2).trim());
                writeTriplesToFile(new LdResourceTriple(firstResource , secondResource , result) , tempfilePath);
                
              }
         
         
        }
        
        scanner.close();
        File oldfile = new File(filePath);
        oldfile.delete();
        
        File newfile = new File(tempfilePath);
        newfile.renameTo(oldfile);
        
    }*/
    
    public static List<LdResourceTriple> readListFromFile(String initialfilePath , boolean skipCalculated) throws FileNotFoundException, IOException{
        if(! Utility.checkFile(initialfilePath))
            return null;

        Map<String , LdResourceTriple> mainList = new HashMap<>();
        Map<String , LdResourceTriple> resultsList = new HashMap<>();
        
        String resultsFilePath = null;
        
        List<LdResourceTriple> remainingList = new ArrayList<>();            

        if(initialfilePath.endsWith(".csv")){
            mainList = readListFromCsvFile(initialfilePath);
            resultsFilePath = initialfilePath.replace(".csv", "_Results.csv");

        }

        else if(initialfilePath.endsWith(".txt")){
            mainList = readListFromTextFile(initialfilePath);
            resultsFilePath = initialfilePath.replace(".txt", "_Results.csv");
        }
        
      
        if(skipCalculated && Utility.checkFile(resultsFilePath)){
            resultsList = readListFromCsvFile(resultsFilePath);
        }
        
        remainingList = getRemainingTriples(mainList , resultsList);

        mainList = null;
        resultsList = null;
        

        return remainingList;
        
    }        
        
    
    public static Map<String , LdResourceTriple> readListFromTextFile(String filePath) throws FileNotFoundException, IOException{
        
        List<String> resourceList = new ArrayList<>();
        
        File file =  new File(filePath); 
        Scanner sc = new Scanner(file); 
  
        while (sc.hasNextLine()){
          resourceList.add(sc.nextLine());
        }
        
        Map<String , LdResourceTriple> triples = generateResourceTriple(resourceList);
        
        return triples;
    }
    
    public static Map<String , LdResourceTriple> readListFromCsvFile(String filePath) throws FileNotFoundException, IOException{
        
        Map<String , LdResourceTriple> resourceList = new HashMap<>();
        
        Scanner scanner = new Scanner(new File(filePath));
        
        while (scanner.hasNext()) {
              List<String> line = parseLine(scanner.nextLine() , '|' , '"');
              
              if(line.isEmpty())
                  continue;
              
              R firstResource = new R(line.get(0).trim());
              R secondResource = new R(line.get(1).trim());
              double result = -1;
              String value = null;
              if(line.size() == 3){
                 value = line.get(2);
                 
                 if(value != null || !value.isEmpty())
                    result = Double.parseDouble(line.get(2));
                 
                 else 
                    result = -1;
              }
              
              LdResourceTriple triple = new LdResourceTriple(firstResource , secondResource , result);

              resourceList.put(firstResource.getUri().toString() + secondResource.getUri().toString() , triple);
              
              

        }
        scanner.close();
        
        return resourceList;
    
    }
    
    
    
    public static List<LdResourcePair> generateResourcePairs (List<String> resourceList) {
        List<LdResourcePair> pairs = new ArrayList<>();
        
        for(String firstResource : resourceList){
            
            for(String secondResource : resourceList){
                
                if(! firstResource.equals(secondResource) ){
                    R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name(firstResource).create();
                    R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name(secondResource).create();
                    
                    LdResourcePair pair = new LdResourcePair(r1 , r2);
                    
                    pairs.add(pair);
                }
            }
        }
        
        return pairs;
    }
    
    public static Map<String , LdResourceTriple> generateResourceTriple (List<String> resourceList) {
        Map<String , LdResourceTriple> triples = new HashMap<>(); 
        
        for(String firstResource : resourceList){
            
            for(String secondResource : resourceList){
                
                if(! firstResource.equals(secondResource) ){
                    R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name(firstResource).create();
                    R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name(secondResource).create();
                    
                    LdResourceTriple triple = new LdResourceTriple(r1 , r2 , -1);
                    
                    triples.put(r1.getUri().toString() + r2.getUri().toString(), triple);
                }
            }
        }
        
        return triples;
    }
    

//    public static List<LdResourceTriple> generateResourceTriple (List<String> resourceList) {
//        List<LdResourceTriple> triples = new ArrayList<>();
//        
//        for(String firstResource : resourceList){
//            
//            for(String secondResource : resourceList){
//                
//                if(! firstResource.equals(secondResource) ){
//                    R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name(firstResource).create();
//                    R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name(secondResource).create();
//                    
//                    LdResourcePair pair = new LdResourcePair(r1 , r2);
//                    LdResourceTriple triple = new LdResourceTriple(pair , -1);
//                    
//                    triples.add(triple);
//                }
//            }
//        }
//        
//        return triples;
//    }    
    
    //Pearson Correlation
    public static double calculateCorrelation( List<String> xs, List<String> ys){

            if(xs.size() != ys.size()){
                System.out.println("Results List and Benchmark are not of the same size, Benchmarks format should be: \"R1,R2 <value>\"");
                return -1;
            }

            double sx = 0.0;
            double sy = 0.0;
            double sxx = 0.0;
            double syy = 0.0;
            double sxy = 0.0;

            int n = xs.size();

            for(int i = 0; i < n; ++i) {
              double x = Double.parseDouble(xs.get(i));
              double y = Double.parseDouble(ys.get(i));

              sx += x;
              sy += y;
              sxx += x * x;
              syy += y * y;
              sxy += x * y;
            }

           double num = (n * sxy) - (sx * sy) ;
           double denom = Math.sqrt( (n * sxx - Math.pow(sx , 2)) * (n * syy - Math.pow(sy , 2)));

           return num/denom;
    }
    
    
    public static List<LdResourceTriple> getRemainingTriples(Map<String , LdResourceTriple> maintriples, Map<String , LdResourceTriple> resultsList) {
        List<LdResourceTriple> remainingTriples = new ArrayList<>();
        
        if(resultsList.isEmpty() || resultsList == null){
            for(Map.Entry<String, LdResourceTriple> entry : maintriples.entrySet()){

                remainingTriples.add(entry.getValue());

            }
        }
        
        else{
            for(Map.Entry<String, LdResourceTriple> entry : maintriples.entrySet()){
                if(! resultsList.containsKey(entry.getKey())){
                    remainingTriples.add(entry.getValue());
                }
            }
        }
        
        return remainingTriples;          

    }
    
     public static String getResultFilePath(String mainFilePath){
        if(!checkFile(mainFilePath))
            return null;
        
        if(mainFilePath.endsWith(".csv")){
            return mainFilePath.replace(".csv", "_Results.csv");

        }

        else if(mainFilePath.endsWith(".txt")){
            return mainFilePath.replace(".txt", "_Results.csv");
        }
        
        return null;
    }
    
        
}
