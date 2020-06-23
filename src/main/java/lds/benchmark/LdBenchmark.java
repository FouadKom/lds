package lds.benchmark;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import static lds.benchmark.Utility.checkFile;
import lds.resource.LdResourceTriple;
import lds.resource.LdResult;
import lds.resource.R;
import org.apache.commons.csv.*;
import java.nio.file.Paths;


public class LdBenchmark {
    private BenchmarkFile sourceFile;
    private BenchmarkFile resultsFile;
    private Correlation correlation;
    
    
    public LdBenchmark(BenchmarkFile sourceFile , Correlation correlation){
        this.sourceFile = sourceFile;
        this.resultsFile = getResultFilePath();
        this.correlation = correlation;
    }
    
    public LdBenchmark(BenchmarkFile sourceFile , BenchmarkFile resultsFile , Correlation correlation){
        this.sourceFile = sourceFile;
        this.resultsFile = resultsFile;
        this.correlation = correlation;
    }
    
    public LdBenchmark(BenchmarkFile sourceFile , BenchmarkFile resultsFile){
        this.sourceFile = sourceFile;
        this.resultsFile = resultsFile;
    }
    
    public LdBenchmark(BenchmarkFile sourceFile){
        this.sourceFile = sourceFile;
        this.resultsFile = getResultFilePath();
    }
    
    public void setCorrelationMethod(Correlation correlation){
        this.correlation = correlation;
    }
    
    public void setResultsFile(BenchmarkFile resultsFile){
        this.resultsFile = resultsFile;
    }
    
    
    public BenchmarkFile getSourceFile(){
        return this.sourceFile;
    }
    
    
    public BenchmarkFile getResultsFile(){
        return this.resultsFile;
    }
    
/////////////////////////////////////Correlation Calculation/////////////////////////////////////////////////////////////////////////////
    public double calculateCorrelation() throws FileNotFoundException, IOException{
        if(correlation == null){
            System.out.println("Correlation method not specified");
            return 0;
        }
        
        List<String> list = readResultsFromFile(resultsFile);
        List<String> benchMark = readResultsFromFile(sourceFile);

        return Correlation.getCorrelation(list , benchMark , correlation);

    }
    
//    public List<String> readResultsFromFile() throws IOException{
//        List<String> results = new ArrayList<>();
//        
//        Map<String , LdResourceTriple> resultsList = readPairsFromFile(resultsFile);
//        
//        for(Map.Entry<String, LdResourceTriple> entry : resultsList.entrySet()){
//            results.add(Double.toString(entry.getValue().getSimilarityResult()));
//        }
//        
//        return results;
//        
//    }
    
    private List<String> readResultsFromFile(BenchmarkFile file) throws FileNotFoundException, IOException{
        /*List<String> results = new ArrayList<>();
        
        if(sourceFile.getFilePath() != null )
            return null;
        
        File file =  new File(sourceFile.getFilePath()); 
        Scanner sc = new Scanner(file);
        
        double minValue = sourceFile.getMinValue();
        double maxValue = sourceFile.getMaxValue();
  
        while (sc.hasNextLine()){
             String line[] = sc.nextLine().split(Character.toString(sourceFile.getSeparator()));
             
             String simValue =  Double.toString(Utility.normalizeValue(Double.parseDouble(line[2]) , minValue , maxValue));
             
             results.add(simValue);
        }
        
        
        
        return results;*/
        List<String> results = new ArrayList<>();
        
        String filePath = file.getFilePath();
        
        double minValue = file.getMinValue();
        double maxValue = file.getMaxValue();
                
        BufferedReader reader = Files.newBufferedReader(Paths.get(filePath));

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(file.getSeparator())
                                                       .withQuote(file.getQuote())
                                                       .withRecordSeparator("\r\n")
                                                       .parse(reader);
        String c3 , result;
        int i = 0;
        for (CSVRecord record : records) {
            i++;
            try{
                c3 = record.get(2);                    
                result = Double.toString(normalizeValue(Double.parseDouble(c3) , minValue , maxValue));
                results.add(result);
            }
            catch(Exception e){
                System.out.println("Exception " + e.toString() + " at line " + i + " while reading benchmark file \"" + file.getFilePath() + "\"");
            }
        }
        
        return results;    
    }
    
    
    private List<LdResourceTriple> readRowsFromBenchmarks(BenchmarkFile file) throws FileNotFoundException, IOException{
        
        /*String baseURI = "http://dbpedia.org/resource/"; //TODO: fix base URI to be as a parameter or dynamic
        List<LdResourceTriple> listTriples = new ArrayList<>();
        
        if(sourceFile.getFilePath() != null)
            return null;
        
        File file =  new File(sourceFile.getFilePath()); 
        Scanner sc = new Scanner(file); 
        
        double minValue = sourceFile.getMinValue();
        double maxValue = sourceFile.getMaxValue();
  
        while (sc.hasNextLine()){
             String line[] = sc.nextLine().split(Character.toString(sourceFile.getSeparator()));
          
             R r1 = new R(baseURI + line[0].substring(0, 1).toUpperCase() + line[0].substring(1));
             R r2 = new R(baseURI + line[1].substring(0, 1).toUpperCase() + line[1].substring(1));
             double simValue =  normalizeValue(Double.parseDouble(line[2]) , minValue , maxValue);
             
             LdResourceTriple triple = new LdResourceTriple(r1 , r2 ,  simValue);
             
             listTriples.add(triple);
        }        
        
        
        return listTriples;*/
        
        String filePath = file.getFilePath();
        
        double minValue = file.getMinValue();
        double maxValue = file.getMaxValue();
        
        List<LdResourceTriple> listTriples = new ArrayList<>();
        
        BufferedReader reader = Files.newBufferedReader(Paths.get(filePath));

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(file.getSeparator())
                                                       .withQuote(file.getQuote())
                                                       .withRecordSeparator("\r\n")
                                                       .parse(reader);

        R firstResource = null, secondResource = null;
        double result = -1;
        String c1 = null, c2 = null , c3 = null;
        char quote = file.getQuote();
        
        int i = 0;
        for (CSVRecord record : records) {
            i++;
            try{
                c1 = record.get(0);
                c2 = record.get(1);
                c3 = record.get(2);
                    
                result = normalizeValue(Double.parseDouble(c3) , minValue , maxValue);
                
                if(c1.contains("http://")){ 
                    firstResource = new R(c1.replace(quote , ' ').trim());
                }

                else{
                    firstResource = new R("http://dbpedia.org/resource/" , c1.replace(quote , ' ').trim());
                }

                if(c2.contains("http://") ){ 
                    secondResource = new R(c2.replace(quote , ' ').trim());
                }

                else{
                    secondResource = new R("http://dbpedia.org/resource/" , c2.replace(quote , ' ').trim());
                }
                
                LdResourceTriple triple = new LdResourceTriple(firstResource , secondResource,  result);
             
                listTriples.add(triple);
              
            }
            catch(Exception e){
                System.out.println("Exception " + e.toString() + " at line " + i + " while reading benchmark file \"" + file.getFilePath() + "\"");
            }
        }
        
        return listTriples;
        
    }
    
    private double normalizeValue(double value , double min , double max){
        return (value - min)/(max - min);
    }
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
    
   private BenchmarkFile getResultFilePath(){
        String mainFilePath = sourceFile.getFilePath();
        char separator = sourceFile.getSeparator();
        char quote = sourceFile.getQuote();
        
        if(!checkFile(mainFilePath))
            return null;
        
        if(mainFilePath.endsWith(".csv")){
            return new BenchmarkFile(mainFilePath.replace(".csv", "_Results.csv") , separator , quote);

        }

        else if(mainFilePath.endsWith(".txt")){
            return new BenchmarkFile(mainFilePath.replace(".txt", "_Results.csv") , separator , quote);
        }
        
        return null;
    }
    
   

////////////////////////////////////////Writing to Files//////////////////////////////////////////////////////////////////////////   
    public synchronized void writeResultsToFile(LdResult results) throws IOException{
        String filePath = resultsFile.getFilePath();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:ms");
        Date date = new Date();

        String results_TimeFilePath = filePath.replace(".csv", "_Duration.csv");
        
        FileWriter duration_writer = new FileWriter(results_TimeFilePath , true);
        FileWriter results_writer = new FileWriter(filePath , true);
        
        results_writer.write(results.getResourceTriple().toString(resultsFile.getSeparator()));
        results_writer.write(System.getProperty("line.separator"));
        
        duration_writer.write(results.toString(resultsFile.getSeparator()) + resultsFile.getSeparator() + " Written on: " + dateFormat.format(date));
        duration_writer.write(System.getProperty("line.separator"));
        
        results_writer.close();
        duration_writer.close();
        
//        Writer writer = Files.newBufferedWriter(Paths.get(filePath));
//        CSVPrinter printer = CSVFormat.DEFAULT.printer();
//        
//        printer.print(results.getResourceTriple().toString(resultsFile.getSeparator() , resultsFile.getQuote()));
//        
//        printer.flush();
//        writer.close();

    }
    
    public static synchronized void writeResultsToFile(LdResult results , BenchmarkFile file) throws IOException{
        String filePath = file.getFilePath();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:ms");
        Date date = new Date();

        String results_TimeFilePath = filePath.replace(".csv", "_Duration.csv");
        
        FileWriter duration_writer = new FileWriter(results_TimeFilePath , true);
        FileWriter results_writer = new FileWriter(filePath , true);
        
        results_writer.write(results.getResourceTriple().toString(file.getSeparator() , file.getQuote()));
        results_writer.write(System.getProperty("line.separator"));
        
        duration_writer.write(results.toString(file.getSeparator() , file.getQuote()) + file.getSeparator() + " Written on: " + dateFormat.format(date));
        duration_writer.write(System.getProperty("line.separator"));
        
        results_writer.close();
        duration_writer.close();

//        Writer writer = Files.newBufferedWriter(Paths.get(filePath));
//        CSVPrinter printer = CSVFormat.DEFAULT.printer();
//        
//        printer.print(results.getResourceTriple().toString(file.getSeparator() , file.getQuote()));
//        
//        printer.flush();
//        writer.close();
    }
    
    public synchronized void writeTriplesToFile(LdResourceTriple triple) throws IOException{
        String filePath = resultsFile.getFilePath();
        
        FileWriter results_writer = new FileWriter(filePath , true);
        
        results_writer.write(triple.toString(resultsFile.getSeparator() , resultsFile.getQuote()));
        results_writer.write(System.getProperty("line.separator"));
        
        results_writer.close();

    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////  
    
    
  
////////////////////////////////////////Reading from Files//////////////////////////////////////////////////////////////////////////       
    public List<LdResourceTriple> readFromFile() throws FileNotFoundException, IOException{

        Map<String , LdResourceTriple> mainList = new HashMap<>();

        int columns = getColumnCountFromFile(sourceFile);
        
        if(columns == 1){
            mainList = readListFromFile(sourceFile);
        }
        else {
            mainList = readPairsFromFile(sourceFile);
        }

        List<LdResourceTriple> remainingList =  new ArrayList<>(mainList.values());
        mainList = null;

        return remainingList;
        
    }
    
    public List<LdResourceTriple> readFromFile(boolean skipCalculated) throws FileNotFoundException, IOException{

        Map<String , LdResourceTriple> mainList = new HashMap<>();

        int columns = getColumnCountFromFile(sourceFile);
        
        if(columns == 1){
            mainList = readListFromFile(sourceFile);
        }
        else {
            mainList = readPairsFromFile(sourceFile);
        }
        
        List<LdResourceTriple> remainingList =  null;
        
        if(skipCalculated){
            Map<String , LdResourceTriple> resultsList = readPairsFromFile(resultsFile);
            remainingList = getRemainingTriples(mainList , resultsList);
            resultsList = null;
            mainList = null;
            return remainingList;
        }
        
        remainingList = new ArrayList<>(mainList.values());
        mainList = null;
        return remainingList;
        
    }
    
    private int getColumnCountFromFile(BenchmarkFile sourceFile) throws IOException{
         String filePath = sourceFile.getFilePath();
        BufferedReader reader = Files.newBufferedReader(Paths.get(filePath));

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(sourceFile.getSeparator())
                                                       .withQuote(sourceFile.getQuote())
                                                       .withRecordSeparator("\r\n")
                                                       .parse(reader);        
        CSVRecord record = records.iterator().next();
            return record.size();
                
       
        
    }
    
    private List<LdResourceTriple> getRemainingTriples(Map<String , LdResourceTriple> maintriples, Map<String , LdResourceTriple> resultsList) {
        List<LdResourceTriple> remainingTriples = null;
        
        if(resultsList == null || resultsList.isEmpty()){
//            for(Map.Entry<String, LdResourceTriple> entry : maintriples.entrySet()){
//
//                remainingTriples.add(entry.getValue());
//
//            }
            remainingTriples = new ArrayList<>(maintriples.values());
        }
        
        else{
            remainingTriples = new ArrayList<>();            
            for(Map.Entry<String, LdResourceTriple> entry : maintriples.entrySet()){                                
                if(! resultsList.containsKey(entry.getKey())){
                    remainingTriples.add(entry.getValue());
                }
            }
        }
        
        return remainingTriples;          

    }        
    
    private Map<String , LdResourceTriple> readListFromFile(BenchmarkFile sourceFile) throws FileNotFoundException, IOException{
        String filePath = sourceFile.getFilePath();
        
        List<String> resourceList = new ArrayList<>();
       
        BufferedReader reader = Files.newBufferedReader(Paths.get(filePath));

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(sourceFile.getSeparator())
                                                       .withQuote(sourceFile.getQuote())
                                                       .withRecordSeparator("\r\n")
                                                       .parse(reader);
        
        for (CSVRecord record : records) {
            resourceList.add(record.get(0));
        }
        
        Map<String , LdResourceTriple> triples = generateResourceTriple(resourceList);
        
        return triples;
    }
    
    
    private Map<String , LdResourceTriple> readPairsFromFile(BenchmarkFile sourceFile) throws FileNotFoundException, IOException{
        String filePath = sourceFile.getFilePath();
        
        Map<String , LdResourceTriple> resourceList = new HashMap<>();
        
        BufferedReader reader = Files.newBufferedReader(Paths.get(filePath));

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(sourceFile.getSeparator())
                                                       .withQuote(sourceFile.getQuote())
                                                       .withRecordSeparator("\r\n")
                                                       .parse(reader);

        R firstResource = null, secondResource = null;
        double result = -1;
        String c1 = null, c2 = null , c3 = null;
        char quote = sourceFile.getQuote();
        
        int i = 0;
        for (CSVRecord record : records) {
            i++;
            try{
                c1 = record.get(0);
                c2 = record.get(1);

                if(record.size() >= 3){
                    //System.out.println(record.size());
                    c3 = record.get(2);
                    if( ! Utility.isNumeric(c3)){
                        c3 = record.get(3);
                    }
                    
                    result = Double.parseDouble(c3);
                }

                if(c1.contains("http://")){ 
                    firstResource = new R(c1.replace(quote , ' ').trim());
                }

                else{
                    firstResource = new R("http://dbpedia.org/resource/" , c1.replace(quote , ' ').trim());
                }

                if(c2.contains("http://") ){ 
                    secondResource = new R(c2.replace(quote , ' ').trim());
                }

                else{
                    secondResource = new R("http://dbpedia.org/resource/" , c2.replace(quote , ' ').trim());
                }              
              
            }
            catch(Exception e){
                System.out.println("Exception " + e.toString() + " at line " + i + " while reading file \"" + sourceFile.getFilePath() + "\"");
            }
              
            LdResourceTriple triple = new LdResourceTriple(firstResource , secondResource , result);
            resourceList.put(triple.getResourcePair().toString(' ').trim() , triple);
        }
        
        return resourceList;
    
    }
    
    
//    private List<LdResourcePair> generateResourcePairs (List<String> resourceList) {
//        List<LdResourcePair> pairs = new ArrayList<>();
//        
//        for(String firstResource : resourceList){
//            
//            for(String secondResource : resourceList){
//                
//                if(! firstResource.equals(secondResource) ){
//                    R r1 = new R("http://dbpedia.org/resource/" , firstResource);
//                    R r2 = new R("http://dbpedia.org/resource/" , secondResource);
//                    
//                    LdResourcePair pair = new LdResourcePair(r1 , r2);
//                    
//                    pairs.add(pair);
//                }
//            }
//        }
//        
//        return pairs;
//    }
    
    private Map<String , LdResourceTriple> generateResourceTriple (List<String> resourceList) {
        Map<String , LdResourceTriple> triples = new HashMap<>(); 
        
        for(String firstResource : resourceList){
            
            for(String secondResource : resourceList){
                R r1 = null;
                R r2 = null;
                
                if(! firstResource.equals(secondResource) ){
                    
                    if(firstResource.contains("http://") && secondResource.contains("http://")){
                        r1 = new R(firstResource);
                        r2 = new R(secondResource);
                    }
                    else{
                        r1 = new R("http://dbpedia.org/resource/" , firstResource);
                        r2 = new R("http://dbpedia.org/resource/" , secondResource);
                    }
                    
                    LdResourceTriple triple = new LdResourceTriple(r1 , r2 , -1);                    
                    triples.put(triple.getResourcePair().toString(' ').trim(), triple);
                }
            }
        }
        
        return triples;
    }
        
}
