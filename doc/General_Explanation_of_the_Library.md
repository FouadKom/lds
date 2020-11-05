
# General Explanation:
**LDS** (Linked Data Similarity) is a JAVA Library for the calculation of **LOD** based semantic similarity.

## Implemented Similarity Measures:
* *LDSD:* Linked Data Semantic Distance [1], and its extensions:
   * *TLDSD:* Typeless Linked Data Semantic Distance [2]
   * *WLDSD:* Weighted Linked Data Semantic Distance [2]
   * *WTLDSD:* Weighted Typeless Linked Data Semantic Distance [2]

* *Resim:* Resource Similarity [3], and its extensions:
  * *TResim:* Typeless Resource Similarity [2]
  * *WResim:* Weighted Resource Similarity [2]
  * *WTResim:* Weighted Typeless Resource Similarity [2]

* *LODS:* Linked Open Data Similarity how to a[4]

* *PICSS:* Partitioned Information Content Semantic Similarity [5]

- - -

## Architecture
![LDS Architecture](./img/lds_arch.png)

<!-- LDS is madeup of four main components: -->

### **Similarity Engine (LdSimilarityEngine):**
The similarity engine is considered the entry point to similarity calculation. The user of the library uses it to specify his desired measure in addition to other configuration parameters. 
The engine orchestrates the whole process of calculation and performs 4 main steps:
* Initializes the similarity measure object chosen by the user in the parameters. <br>
* Loads the necessary index files in case the user wants data indexing, using LdIndexer component. <br>
* Sends concepts to be compared to LdSimilarity component for similarity calculation. <br>
* Receives results from LdSimilarity and returns the similarity value to the user.

In addition to this, LdSimilarityEngine allows the calculation to be done using benchmark files (using LdBenchmark component). In this case, the engine handles the process of reading benchmark input and performing the above steps to produce similarity results outputs. LdSimilarityEngine also supports multithread similarity calculation for a large number of resources to ensure faster delivery of results.

- - -

### **LOD-based Similarity (LdSimilarity):**
LdSimilarity Component is the component responsible for similarity calculation. It consists of several subclasses denoting each similarity measure. An LdSimilarity object is initialized by the LdSimilarityEngine once a user has specified his desired measure and the configuration parameters.
Once initialized, an LdSimilarity object receives necessary data from LdManager and applies the similarity measure algorithm on retrieved data. After calculation the value representing the similarity is returned to LdSimilarityEngine.

LdSimilarity can be implemented to add a new similarity measure. A full explanation on adding your similairty measure is avilable [here](./Adding_Your_Measure.md).


- - -

### **Linked data Manager (LdManager):**
LdManager is the component responsible for data retrieval. Similar to LdSimilarity, it has several subclasses for different measures as each measure reacquires different types of data to be retrieved. Its main function is to retrieve data for similarity calculation by querying data sources chosen by the user. Linked data sources can be RDF files, HDT files and remote SPARQL endpoints. In case of index usage, LdManager acts as an intermediate between LdSimilarity and LdIndexer. LdManager is also responsible for updating indexes in case data is not previously indexed.

- - -

### **Linked Data Indexer (LdIndexer):**
LdIndexer handles all operations related to indexes. It manages the creation of indexes, data insertion, data retrieval, and handles indexes updating with the help of LdManager. To store data, LdIndexer uses key-value pair indexes and to avoid overhead on index sizes, we implemented a simple compression and decompression mechanism for stored data. In addition, a key check process is done before data storage to avoid key duplication.

- - -

### **Linked Data Benchmark (LdBenchmark):**
LdBenchmark is not a main component of LDS, in the sense that it is not a part of the calculation process. However, it is used especially in testing and measure evaluations. To do so, LdBenchmark provides the ability to read files with pairs of resources, or read files with resource lists and generates random pairs from these lists. This allows LdSimilarityEngine to calculate the similarity for each pair. In addition, it allows to measure the correlation between similarity results and a given benchmark file. 

A full explanation on correlation calculation using LdBenchmark is avilable [here](./Calculationg_Correlation_Using_LdBenchmark.md).

## **Similarity Calculation Process:**
The follwoing figure represents a flow chart on how the similarity operation works:

<p align="center">
<!-- ![LDS Flow_Chart](./img/LDS_flow_chart.png) -->
<img width="700" height="1100" src="./img/LDS_flow_chart.png">
</p>

<!-- - - -

 ## **References:**
[1] Passant, Alexandre. “Measuring Semantic Distance on Linking Data and Using it for Resources Recommendations.” AAAI Spring Symposium: Linked Data Meets Artificial Intelligence (2010).

[2] Alfarhood, Sultan. “Exploiting Semantic Distance in Linked Open Data for Recommendation.” (2017).

[3] Piao, Guangyuan and John G. Breslin. “Measuring semantic distance for linked open data-enabled recommender systems.” SAC '16 (2016).

[4] Cheniki, Nasredine, Abdelkader Belkhir, Yacine Sam and Nizar Messai. “LODS: A Linked Open Data Based Similarity Measure.” 2016 IEEE 25th International Conference on Enabling Technologies: Infrastructure for Collaborative Enterprises (WETICE) (2016): 229-234.

[5] Meymandpour, Rouzbeh, and Davis, J. G. “A semantic similarity measure for linked data: An information content-based approach.” Knowledge-Based Systems, 109, 276–293. https://doi.org/10.1016/j.knosys.2016.07.012 (2016). -->

