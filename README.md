# lds
**LDS** is a JAVA Library for the calculaton of **LOD** based semantic similarity. **LDS** implements similarity measures such as:

* *LDSD:* Linked Data Semantic Distance

* *Resim:* Resource Similarity

* *LODS:* Linked Open Data Similarity (simI submeasure only)

* *PICSS:* Partitioned Information Content Semantic Similarity

In addition **LDS** implements several extensions for *LDSD* and *Resim*.

## Getting Started

### Prerequisites
[Apache Maven](https://maven.apache.org/) is needed for the instalation and build of the library.

### Installing

```
git clone https://github.com/FouadKom/lds.git
cd lds 
mvn clean install -DskipTests
```
## Running the tests
Tests were created for each implemented similarity measure and necessary data were queried from DBpedia.

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
* [Apache Jena](https://jena.apache.org/)

## Licence
