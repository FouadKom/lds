# Adding Your Measure:
To extend LDS and add a new measure, there are set of steps and guidelines that should be taken:

## 1. Creating a new Similarity Measure Class:
The first step is to to create a new class for the similarity measure algorithm that implements LdSimilarity. LdSimilarity is the interface containing methods needed for all similarity measure classes. When adding a new measure, a user must implement LdSimilarity and create his similarity measure class. 

The created similarity class must contain all the methods to implement the new measure's algorithm. The next step will be to implement the data retrieval class. For more info see classes:
1. [LDSD](./code_Examples/LDSD.md)
2. [LODS](./code_Examples/LODS.md)
3. [PICSS](./code_Examples/PICSS.md)
4. [ResourceSimilarity](./code_Examples/ResourceSimilarity.md) 

- - -

## 2. Creating a new Data Retrievail Class:
This class can either implements LdManager interface or extend LdManagerBase. Since usually each measure requires specific data and queries, a new data retrieval class should be created. This class will contain all [SPARQL](https://www.w3.org/TR/rdf-sparql-query/) queries needed to retrieve necessary data. Otherwise a user may reuse any of the already implemented data retrieval classes. For more info see class LdManagerBase this class implements LdManager and contains all SPARQL queries needed for similarity measures.

The data retrieval class should use LdIndexer class inorder to retrieve indexed data. For more info see classes:
1. PICSSLdManager
2. HybridMeasuresLdManager
3. DistanceMeasuresLdManager
4. ResimLdManager

- - -

## 3. Adding the new measure to Measure enum:
After creating the measure class and data retrieval class, the new measure must be added to the Measure enum class. In addition, the `String getPath(Measure measure)` method must be updated with the new measure and its fully-qualified name. This method will be used by the LdSimilarityEngine class to load the measure.

- - -

## 4. Using LdSimilarityEngine Class:
Call the similarity measure using the LdSimilarityEngine class. LdSimilarityEngine is the entry component to the library and to remove any unnecessary code clutter, the engine is the class that is used to initialize a measure, load its indexes, call the similarity function and at the end close all indexes used by data retrieval classes ex:

```java
    LdSimilarityEngine engine = new LdSimilarityEngine();
    
    engine.load(Measure.Resim , configSim);

    engine.similarity(r1 , r2);

    engine.close();
```
In this example, r1 and r2 are the resources to be measured, and configSim is the conf object containing all parameters needed for the measure.
