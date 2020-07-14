# Similarity Calculation:
To perform a similarity calculation using LDS library a user must first initialize a LdSimilarityEngine object, select the LOD measure wanted, and provide the configuration (conf) object which will contain all necessary parameters for a measure to perform the calculation.

<a name="example"></a>
## Code Example:
The following code presents a small example for similarity calculation using Resim similarity measure.
In any calculation case, the steps needed to perform the calculation are:
1. [Initialize the resources](#resources)
2. [Initialize the dataset object](#dataset)
3. [Initialize the conf object](#conf)
4. [Add necessary parameters to conf object](#parametersConf)
5. [Initialize the LdSimilarityEngine object](#engine)
6. [Load the engine object](#loadEngine)
7. [Call the similarity method](#similarity)
9. [Close the engine object](#closeEngine)

```java
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import lds.resource.R;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;
import slib.utils.i.Conf;

public class ResimTest {
       
    public static void main(String args[]){ 
        
        //initialize two movie resources from DBpedia dataset to calculate the similarity between them
        R r1 = new R("http://dbpedia.org/resource/The_Noah");
        R r2 = new R("http://dbpedia.org/resource/The_Pack_(2010_film)");
        
        //initialize the dataset from which necessary data will be queried
        LdDataset datasetMain = Util.getDBpediaDataset();

        //Initialize the conf object
        Conf configSim = new Conf();
        
        /* Add necessary parameters to conf object*/
        //specify the main dataset that will be used for querying, in this case DBpedia
        configSim.addParam("LdDatasetMain" , datasetMain); 
        
        //specify wether indexes are used for calculation, change to false otherwise
        configSim.addParam("useIndexes" , true);        
        
        //initialize the engine object
        LdSimilarityEngine engine = new LdSimilarityEngine();
        
        //load the engine with the wanted similarity measure and the conf object
        engine.load(Measure.Resim , configSim);
        
        //calculate the similarity between the two resources
        engine.similarity(r1 , r2);
        
        //close the engine
        engine.close();

   }   
   
}

```
<a name="resources"></a>
## Initialize the resources:
**Needs:** 
- `import lds.resource.R;`

The first step in the similarity calculation process is to initialize the two resources that will be compared. To do so the R class is used. In this example we are using two movie resources from DBpedia dataset:

`R r1 = new R("http://dbpedia.org/resource/The_Noah");` **Note:** The url of each resource changes according to the dataset its found in


<a name="dataset"></a>
## Initialize the dataset object:
**Needs:**
- `import sc.research.ldq.LdDataset;`
- `import sc.research.ldq.LdDatasetFactory;`

To perform the similarity calculation a user must initialize the dataset object from which necessary data for calculation will be queried using SPARQL. To do so the LdDataset class is used as follows:

`LdDataset datasetMain = Util.getDBpediaDataset();` **Note:** Currently `Util.getDBpediaDataset()` is used for testing purposes.

<a name="engine"></a>
## Initialize the LdSimilarityEngine object:
**Needs:** 
- `import lds.engine.LdSimilarityEngine;`
- `import lds.measures.Measure;`

<a name="conf"></a>
## Initialize the conf object:
**Needs:** 
- `import slib.utils.i.Conf;`

A conf object holds all the necessary configurations for a similarity measure to perform the calculation. To initialize a conf object the Conf class is needed, this class is used from the [slib](http://www.semantic-measures-library.org) library and is initialized as follows:

`Conf configSim = new Conf();`

<a name="parametersConf"></a>
## Add necessary parameters to conf object:
As mentioned in the previous section, a conf object holds all the necessary configurations for a similarity measure to perform the calculation. However, each measure needs its own parameters. The table below summarizes the configuration parameters for each measure:
| Measure | Conf parameters                                                                                                              |
|---------|------------------------------------------------------------------------------------------------------------------------------|
| LDSD_d  | 1. "LdDatasetMain": the main datasetobject<br>2. "useIndexes": true or false                                                 |
| LDSD_dw | 1. "LdDatasetMain": the main datasetobject<br>2. "useIndexes": true or false                                                 |
| LDSD_i  | 1. "LdDatasetMain": the main datasetobject<br>2. "useIndexes": true or false                                                 |
| LDSD_iw | 1. "LdDatasetMain": the main datasetobject<br>2. "useIndexes": true or false                                                 |
| LDSD_cw | 1. "LdDatasetMain": the main datasetobject<br>2. "useIndexes": true or false                                                 |
| TLDSD   | 1. "LdDatasetMain": the main datasetobject<br>2. "useIndexes": true or false                                                 |
| WLDSD   | 1. "LdDatasetMain": the main datasetobject<br>2. "LdDatasetSpecific": the specific dataset used for weight calculaton<br> 3. "useIndexes": true or false<br>4. "WeightMethod": the weight calculation method
| Resim   | 1. "LdDatasetMain": the main datasetobject<br>2. "useIndexes": true or false                                                 |
| TResim  | 1. "LdDatasetMain": the main datasetobject<br>2. "useIndexes": true or false                                                 |
| WResim  | 1. "LdDatasetMain": the main datasetobject<br>2. "LdDatasetSpecific": the specific dataset used for weight calculaton<br> 3. "useIndexes": true or false<br>4. "WeightMethod": the weight calculation method                                                  |
| WTResim | 1. "LdDatasetMain": the main datasetobject<br>2. "LdDatasetSpecific": the specific dataset used for weight calculaton<br> 3. "useIndexes": true or false<br>4. "WeightMethod": the weight calculation method
| PICSS   | 1. "LdDatasetMain": the main datasetobject<br>2. "useIndexes": true or false<br>3. "resourcesCount": number of resouces in  main datasetobject |

<a name="engine"></a>
## Initialize the LdSimilarityEngine object:
**Needs:**
- `import lds.engine.LdSimilarityEngine;`

An engine object is the object needed to perform the similarity calculation by the desired measure. An engine object is initialized using LdSimilarityEngine class as follows:

`LdSimilarityEngine engine = new LdSimilarityEngine();`

<a name="loadEngine"></a>
## Load the engine object:
Loading the engine object specifies for the engine what is the wanted measure for calculation and what are the configuartion parameters needed for the measure. In this example we are calculating the similairty using Resim similairty measure as follows:

`engine.load(Measure.Resim , configSim);`

<a name="similarity"></a>
## Call the similarity method:
Calling the 'similarity' method calculates the similairty between to given resources and returns a double representing the similairty between them:

`engine.similarity(r1 , r2);`

<a name="closeEngine"></a>
## Close the engine object:
Closing the engine object ensures that all necessary indexes needed are closed properly. It is advisable to always close the engine after similairt calculation.

**Note:** 

- You can perform more than one similairty calculation using the same similarity measure before closing the engine.
- Changing the similairty measure, requires closing the enigne, loading it with the new measure and updated conf object if necessary, and closing it after calculation. 