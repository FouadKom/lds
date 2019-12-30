# lds
**LDS**(Linked Data Similarity) is a JAVA Library for the calculaton of **LOD** based semantic similarity. **LDS** implements similarity measures such as:

* *LDSD:* Linked Data Semantic Distance, and its extensions:
   * *TLDSD:* Typeless Linked Data Semantic Distance
   * *WLDSD:* Weighted Linked Data Semantic Distance
   * *WTLDSD:* Weighted Typeless Linked Data Semantic Distance

* *Resim:* Resource Similarity, and its extensions:
  * *TResim:* Typeless Resource Similarity
  * *WResim:* Weighted Resource Similarity
  * *WTResim:* Weighted Typeless Resource Similarity

* *LODS:* Linked Open Data Similarity (simI submeasure only)

* *PICSS:* Partitioned Information Content Semantic Similarity

## Getting Started

### Prerequisites
[Apache Maven](https://maven.apache.org/) is needed for the installation and build of the library.

### Installing

``` 
mvn clean install -DskipTests
```
## Running the tests
Tests were created for each implemented similarity measure.

### LDSD measure test

```
mvn test -Dtest=lds.measures.ldsd.LDSDTest
```

### Resim measure test
 
```
mvn test -Dtest=lds.measures.ldsd.ResimTest
```

### LODS (simI submeasure) test

```
mvn test -Dtest=lds.measures.ldsd.LODSTest
```

### PICSS measure test

```
mvn test -Dtest=lds.measures.ldsd.PICSSTest
```

## Built With
* [Java](https://www.java.com/download/)
* [Apache Maven](https://maven.apache.org/)
* [Semantic Measure Library](http://www.semantic-measures-library.org)
* [Apache Jena](https://jena.apache.org/)

## Licence
