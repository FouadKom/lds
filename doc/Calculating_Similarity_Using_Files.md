# Similarity calculation using files:

To perform similarity calculation using LDS on files of resources, a user must first initialize a BenchmarkFile object, initialize a LdBenchmark object, initialize an LdSimilarityEngine object, select the LOD measure wanted, and provide the configuration (config) object which will contain all necessary parameters for a measure to perform the calculation. Finally call the similarity method using the LdSimilarityEngine object.

## Code Example:
The following code presents a small example for a similarity calculation using PICSS similarity measure using a file of resources.
In any calculation case, the steps needed to perform the calculation are: <br>
1- [Initialize the dataset object](#1-initialize-the-dataset-object) <br>
2- [Initialize the BenchmarkFile object](#2-initialize-the-BenchmarkFile-object) <br>
3- [Initialize the LdBenchmark object](#3-initialize-the-LdBenchmark-object) <br>
4- [Initialize the config object](#4-initialize-the-config-object) <br>
5- [Initialize the LdSimilarityEngine object](#5-initialize-the-ldsimilarityengine-object) <br>
6- [Load the engine object](#6-load-the-engine-object) <br>
7- [Call the similarity method](#7-call-the-similarity-method) <br>
8- [Close the engine object](#8-close-the-engine-object)

```Java
import lds.benchmark.BenchmarkFile;
import lds.benchmark.LdBenchmark;
import lds.config.Config;
import lds.config.LdConfigFactory;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import lds.dataset.LdDatasetCreator;

public class similarityUsingFilesTest {
    
    
    public static void main(String args[]) throws Exception{

        //Initialize the main dataset object --- this step is not needed when default config is used.
        LdDataset datasetMain = LdDatasetCreator.getDBpediaDataset();   
        

        //Initialize the source BenchmarkFile object
        BenchmarkFile source = new BenchmarkFile(sourceFilePath , ',' , '"');

        //Initialize the BenchmarkFile object --- this step is optional but advised
        BenchmarkFile result = new BenchmarkFile(resultsFilePath , ',' , '"'); 

        //Initialize the LdBenchmark object which handles reading and writing data
        LdBenchmark benchmark = new LdBenchmark(source , result);
       
        /*Initialize the config object which contains the necessary parameters for the measure
        you can use the default conf as follows. This creates a config with default parameters and no indexing by default*/
        Config config = LdConfigFactory.createDeafaultConf(Measure.PICSS);

        //Initialzie the LdSimilarityEngine object
        LdSimilarityEngine engine = new LdSimilarityEngine();

        //load the engine with the wanted similarity measure and the config object
        engine.load(Measure.PICSS , config);

        //Call the similarity method              
        engine.similarity(benchmark , true);      
        
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
- This step is not required if the user will use the default config object for the measure (see section [Initialize the config object](#4-initialize-the-config-object-)). The default config object will use the english Dbpedia chapter by default. <br>
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
- The source file can contain either pairs of resources or a list of resources (a sigle resource on each line). In the second case the LdBenchmark object will create a list of pairs from these resources.<br>
- Creating a BenchmarkFile object for the results file is optional. It can be created the same way as the source file. If not initialized, a file in the current working directory with the same separator and quotations in the source file is created, however it is advised to initiazlize a BenchmarkFile object for the results

- - -

## 3. Initialize the LdBenchmark object:
**Needs:** 
- `import lds.benchmark.LdBenchmark;`

The LdBenchmark object handles reading data from source file and writing results to the result file. In addition it handles correlation calculation.

An  LdBenchmark object is initiazlized as follows:

`LdBenchmark benchmark = new LdBenchmark(source , result);`

- - -

## 4. Initialize the config object:
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

## 5. Initialize the LdSimilarityEngine object:
**Needs:**
- `import lds.engine.LdSimilarityEngine;`

An engine object is the object needed to perform the similarity calculation by the desired measure. An engine object is initialized using LdSimilarityEngine class as follows:

`LdSimilarityEngine engine = new LdSimilarityEngine();`

- - - 


## 6. Load the engine object:
**Needs:**
- `import lds.measures.Measure;`

Loading the engine object specifies for the engine what is the wanted measure for calculation and what are the configuartion parameters needed for the measure. In this example we are calculating the similairty using Resim similairty measure as follows:

`engine.load(Measure.PICSS , config);`

- - - 


## 7. Call the similarity method:
Calling the 'similarity' method calculates the similairty between resources in a file represented using BenchmarkFile object. The results are the writen to the results file:

`engine.similarity(benchmark , true)`

The method takes as parameters the LdBenchmark object, and a boolean which if set to true will skip already caclualted resources in the results file and continue calculation.

**Note:** 

- The true in `similarity(benchmark , true)` is to skip already calculated pairs found in the results file.
- You can perform a multithread calculation process by provding the number of threads wanted for calculation using for example: `similarity(benchmark , 5 , true)`.
The second parameter `5` in the method call represents the number of threads wanted.


- - - 

## 8. Close the engine object:
Closing the engine object ensures that all necessary indexes needed are closed properly. It is advisable to always close the engine after similairt calculation:

`engine.close();`
