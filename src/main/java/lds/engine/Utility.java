package lds.engine;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import lds.resource.ResourcePair;
import org.openrdf.model.URI;
import slib.graph.model.graph.G;
import slib.graph.model.graph.elements.E;

public class Utility {
	
    public static void showVerticesAndEdges(G graph) {

        Set<URI> vertices = graph.getV();
        Set<E> edges = graph.getE();

        System.out.println("-Vertices");
        for (URI v : vertices) {
                System.out.println("\t" + v);
        }

        System.out.println("-Edge");
        for (E edge : edges) {
                System.out.println("\t" + edge);
        }
    }
    
    public static List<ResourcePair> generateRandomResourcePairs (List<String> resourceList) {
        List<ResourcePair> pairs = new ArrayList<>();
        
        for(String firstResource : resourceList){
            
            for(String secondResource : resourceList){
                
                if(! firstResource.equals(secondResource) ){
                    R r1 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name(firstResource).create();
                    R r2 = LdResourceFactory.getInstance().baseUri("http://dbpedia.org/resource/").name(secondResource).create();
                    
                    ResourcePair pair = new ResourcePair(r1 , r2);
                    
                    pairs.add(pair);
                }
            }
        }
        
        return pairs;
    }
    
    
    public static List<String> readListFromFile(String filePath) throws FileNotFoundException{
        if(! checkPath(filePath) )
            return null;
        
        List<String> resourceList = new ArrayList<>();
        
        File file =  new File(filePath); 
        Scanner sc = new Scanner(file); 
  
        while (sc.hasNextLine()) 
          resourceList.add(sc.nextLine());
        
        return resourceList;
    }
    
    
    public static void writeListToFile(List<Double> resultsList , String filePath) throws IOException{
        if(! checkPath(filePath) )
            return;
        
        FileWriter writer = new FileWriter(filePath);
        
        for(Double result : resultsList){
            writer.write(result + "\n");
        }
        
        writer.close();
    }
    
    
    public static double checkCorrelation(String listPath , String benchMarkPath) throws FileNotFoundException{
        if( !checkPath(listPath) && !checkPath(benchMarkPath) )
            return -1;
        
        List<String> list = readListFromFile(listPath);
        List<String> benchMark = readListFromFile(benchMarkPath);
        
        return calculateCorrelation(list , benchMark);
        
    }
    
    
    
    //Pearson Correlation
    private static double calculateCorrelation( List<String> xs, List<String> ys){

            if(xs.size() != ys.size())
                return -1;

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
    
    
    private static boolean checkPath(String path){
        File file = new File(path);

        if (!file.isDirectory()){
           file = file.getParentFile();

            if (! file.exists()){
                File dir = new File(file.getPath());
                return dir.mkdirs();

            }
        }

        return true;
    }
}
