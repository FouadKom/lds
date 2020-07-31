# Correlation Calcualtion Using LdBenchmark:

To perform a correlation calculation using LDS library a user must first initialize a BenchmarkFile object, initialize a LdBenchmark object, initialize an LdSimilarityEngine object, select the LOD measure wanted, and provide the configuration (config) object which will contain all necessary parameters for a measure to perform the calculation. Finally call the correlation method using the LdSimilarityEngine object.

## Code Example:
The following code presents a small example for a correlation calculation using PICSS similarity measure.
In any calculation case, the steps needed to perform the calculation are: <br>
1- [Initialize the dataset object](#1-initialize-the-dataset-object-) <br>
2- [Initialize the BenchmarkFile object](#2-initialize-the-BenchmarkFile-object-) <br>
3- [Initialize the LdBenchmark object](#3-initialize-the-LdBenchmark-object-) <br>
4- [Set the needed correlation method](#4-set-the-needed-correlation-method-) <br>
5- [Initialize the config object](#5-initialize-the-config-object-) <br>
6- [Initialize the LdSimilarityEngine object](#6-initialize-the-ldsimilarityengine-object-) <br>
7- [Load the engine object](#7-load-the-engine-object-) <br>
8- [Call the correlation method](#8-call-the-correlation-method-) <br>
9- [Close the engine object](#9-close-the-engine-object-)

```Java
import lds.benchmark.BenchmarkFile;
import lds.benchmark.Correlation;
import lds.benchmark.LdBenchmark;
import lds.config.Config;
import lds.config.LdConfigFactory;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import lds.dataset.LdDatasetCreator;

public class mc30Test {
    
    
    public static void main(String args[]) throws Exception{

        //Initialize the main dataset object --- this step is not needed when default config is used.
        LdDataset datasetMain = LdDatasetCreator.getDBpediaDataset();   
        

        //Initialize the source BenchmarkFile object
        BenchmarkFile source = new BenchmarkFile(sourceFilePath , ',' , '"');

        /* for normalizing the benchmark values between 0 and 1 */
        source.setMaxValue(4); 
        source.setMinValue(0);

        //Initialize the BenchmarkFile object --- this step is optional but advised
        BenchmarkFile result = new BenchmarkFile(resultsFilePath , ',' , '"'); 

        //Initialize the LdBenchmark object which handles reading and writing data and correlation calculation
        LdBenchmark benchmark = new LdBenchmark(source , result);

        //Set the CorrelationMethod wanted
        benchmark.setCorrelationMethod(Correlation.PearsonCorrelation);
        
        /*Initialize the config object which contains the necessary parameters for the measure
        you can use the default conf as follows. This creates a config with default parameters and no indexing by default*/
        Config config = LdConfigFactory.createDeafaultConf(Measure.PICSS);

        //Initialzie the LdSimilarityEngine object
        LdSimilarityEngine engine = new LdSimilarityEngine();

        //load the engine with the wanted similarity measure and the config object
        engine.load(Measure.PICSS , config);

        //Call the correlation method              
        System.out.println("PICSS Pearson Correlation: " + engine.correlation(benchmark , false));        

        /*You can perform more than one correlation calculation process using same source BenchmarkFile, and LdBenchmark object 
        by just changing the CorrelationMethod wanted and caalling the engine.correlation() method again*/

        benchmark.setCorrelationMethod(Correlation.SpearmanCorrelation);
        
        System.out.println("PICSS Spearman Correlation: " + engine.correlation(benchmark , false));
        
        //ends calculation for the chosen similarity and closes all indexes if created
        engine.close();
    }
    

}
```

## 1. Initialize the dataset object:
**Needs:**
- `import lds.dataset.LdDatasetCreator;`

To perform the similarity calculation a user must initialize the dataset object from which necessary data for calculation will be queried using SPARQL. To do so the LdDatasetCreator class is used as follows:

`LdDataset datasetMain = LdDatasetCreator.getDBpediaDataset();` 

The above command creates an object of the english chapter DBpedia. Specifiying the DBpedia chapter (ex: `LdDatasetCreator.getDBpediaDataset(DBpediaChapter.fr);`) as a parameter is required if another chapter is wanted.

**Note:** 
- To create an object of a remote dataset other than DBpedia, use the `LdDatasetCreator.getRemoteDataset(service , defaultGraph , name)` method where `service` is a string representing the URI of the remote endpoint, `defaultGraph` is a string representing the default graph URI, and `name` is a string represeting the name of the dataset which can be any value. <br>
- This step is not required if the user will use the default config object for the measure (see section [Initialize the config object](#5-initialize-the-config-object-)). The default config object will use the english Dbpedia chapter by default. <br>
- Using weighted measures such as: WLDSD_cw, WTLDSD_cw, WResim, WTResim requires an extra dataset (LdDatasetSpecific) which is used to calculate weights for links.

- - - 

## 2. Initialize the BenchmarkFile object:
**Needs:** 
- `import lds.benchmark.BenchmarkFile;`

The BenchmarkFile represents the source or the results file which can be text or csv files. A BenchmarkFile is initialized as follows:

`BenchmarkFile source = new BenchmarkFile(sourcepath , ',' , '"');`

The initialization requires the  path of the file (here `sourcepath`), the separator (here `,`), and the quotations (here `"`). Quotations are optional but its advised to use quotations to avoid any reading problems with resources having underscore ( _ ) in their URI and It is enough to only use quotations with these resources.

**Note:** 
- The resources in the file must be in the form of URIs according to the dataset that they are found in.<br>
- If the estimation values in the benchmark are not normalized between 0 and 1 the `setMaxValue(double maxValue)`, and `setMinValue(double minValue)` methods should be used to set the max and min values in the benchmark inorder for the results to be normalized between 0 and 1 before correlation calculation. In our example the max value in the benchmark was 4 and the min value was 0.
- Creating a BenchmarkFile object for the results file is optional. It can be created the same way as the source file. If not initialized, a file in the current working directory with the same separator and quotations in the source file is created, however it is advised to initiazlize a BenchmarkFile object for the results

- - -

## 3. Initialize the LdBenchmark object:
**Needs:** 
- `import lds.benchmark.LdBenchmark;`

The LdBenchmark object handles reading data from source file and writing results to the result file. In addition it handles correlation calculation.

An  LdBenchmark object is initiazlized as follows:

`LdBenchmark benchmark = new LdBenchmark(source , result);`

- - -

## 4. Set the needed correlation method:
**Needs:**
- `import lds.benchmark.Correlation;`

After initializing the LdBenchmark, the correlation method is set as follows:

`benchmark.setCorrelationMethod(Correlation.PearsonCorrelation);`

**Note:** Currently LDS supports Pearson and Spearman correlations.

- - -

## 5. Initialize the config object:
**Needs:** 
- `import lds.config.Config;`

A config object holds all the necessary configurations for a similarity measure to perform the calculation. To initialize a config object the Config class is needed, this class is based on the Conf class from [slib](http://www.semantic-measures-library.org) library.

A config object can be initialized in sevral ways:

1- Using LdConfigFactory class where you create a default config object with the basic parameters:

`Config config = LdConfigFactory.createDeafaultConf(Measure.Resim);`

The  `createDeafaultConf()` takes the wanted measure as a parameter and creates the basic config object for it. Using this method however, requires adding some new parameters for some measures which can't be created by default.

2- Creating the config object and manually passing all the needed parameters fo a measure. In this case for example, Resim needs the main dataset and to specify    wether indexing is needed or not:

`Config config = new Config();` <br>
`config.addParam(ConfigParam.LdDatasetMain , dataset); ` <br>
`config.addParam(ConfigParam.useIndexes , false);`

**Note:**
- Each similairty measure require its own config parameters to be defined before calculation.
- To make it easier for the user to add parameters, the ConfigParam enum is created. addParam() method takes as parameters a ConfigParam parameter name and the object needed. The necessary paarmeters for each measure are explained [here](./Similarity_Measures_Configuration_Parameters.md).


- - - 

## 6. Initialize the LdSimilarityEngine object:
**Needs:**
- `import lds.engine.LdSimilarityEngine;`

An engine object is the object needed to perform the similarity calculation by the desired measure. An engine object is initialized using LdSimilarityEngine class as follows:

`LdSimilarityEngine engine = new LdSimilarityEngine();`

- - - 


## 7. Load the engine object:
**Needs:**
- `import lds.measures.Measure;`

Loading the engine object specifies for the engine what is the wanted measure for calculation and what are the configuartion parameters needed for the measure. In this example we are calculating the similairty using Resim similairty measure as follows:

`engine.load(Measure.PICSS , config);`

- - - 


## 8. Call the correlation method:
Calling the 'correlation' method calculates the correlation between two the benchmark and the similarity measure and returns a double representing the similairty between them:

`engine.correlation(benchmark , false)`

The method takes as parameters the LdBenchmark object, and a boolean which if set to true will skip already caclualted resources in the results file and continue calculation.

**Note:** 

- You can perform more than one correlation calculation using the same similarity measure before closing the engine.
- Changing the similairty measure, requires: <br>
    1- Closing the enigne <br>
    2- Loading it with the new measure and updated config object if necessary <br>
    3- Closing it after calculation.
- Changing the CorrelationMethod with the same similarity measure doesn't require closing the engine and loading it again. You just need to set the new method using `benchmark.setCorrelationMethod(Correlation);`


- - - 

## 9. Close the engine object:
Closing the engine object ensures that all necessary indexes needed are closed properly. It is advisable to always close the engine after similairt calculation:

`engine.close();`
