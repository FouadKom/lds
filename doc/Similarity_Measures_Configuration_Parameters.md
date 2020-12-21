# Similarity Measures Configuration Parameters

The table below summarizes the configuration parameters for each measure:

| Measure | Config parameters | Function                                                                                       |
|---------|---------------------------------------|---------------------------------------------------------------------------------------|
| LDSD_d  | 1. "LdDatasetMain"<br>2. "useIndexes" | The main dataset object for querying data <br> Specifies wether true or false for index usage                                               |
| LDSD_dw | 1. "LdDatasetMain"<br>2. "useIndexes" | The main dataset object for querying data <br> Specifies wether true or false for index usage                                               |
| LDSD_i  |1. "LdDatasetMain"<br>2. "useIndexes" | The main dataset object for querying data <br> Specifies wether true or false for index usage                                               |
| LDSD_iw | 1. "LdDatasetMain"<br>2. "useIndexes" | The main dataset object for querying data <br> Specifies wether true or false for index usage                                              |
| LDSD_cw | 1. "LdDatasetMain"<br>2. "useIndexes" | The main dataset object for querying data <br> Specifies wether true or false for index usage                                                 |
| TLDSD   | 1. "LdDatasetMain"<br>2. "useIndexes" | The main dataset object for querying data <br> Specifies wether true or false for index usage                                              |
| WLDSD   | 1. "LdDatasetMain"<br>2. "LdDatasetSpecific" <br> 3. "useIndexes"<br>4. "WeightMethod" | The main dataset object for querying data<br>The specific dataset used for weight calculaton<br>true or false for index usag<br>The weight calculation method
| Resim   |1. "LdDatasetMain"<br>2. "useIndexes" | The main dataset object for querying data <br> Specifies wether true or false for index usage                                                 |
| TResim  |1. "LdDatasetMain"<br>2. "useIndexes" | The main dataset object for querying data <br> Specifies wether true or false for index usage                                                |
| WResim  | 1. "LdDatasetMain"<br>2. "LdDatasetSpecific" <br> 3. "useIndexes"<br>4. "WeightMethod" | The main dataset object for querying data<br>The specific dataset used for weight calculaton<br>Specifies wether true or false for index usage<br>The weight calculation method                                                 |
| WTResim | 1. "LdDatasetMain"<br>2. "LdDatasetSpecific" <br> 3. "useIndexes"<br>4. "WeightMethod" | The main dataset object for querying data<br>The specific dataset used for weight calculaton<br>Specifies wether true or false for index usage<br>The weight calculation method
| PICSS   | 1. "LdDatasetMain"<br>2. "useIndexes"<br>3. "resourcesCount" | The main dataset object for querying data <br>Specifies wether true or false for index usage <br>Number of resouces in  main dataset |
| EPICS   | 1. "LdDatasetMain"<br>2. "useIndexes"<br>3. "resourcesCount" | The main dataset object for querying data <br>Specifies wether true or false for index usage <br>Number of resouces in  main dataset |
| LODS (SimP submeasure)  | 1. "LdDatasetMain"<br>2. "useIndexes"<br>3. "resourcesCount" | The main dataset object for querying data <br>Specifies wether true or false for index usage <br>Number of resouces in  main dataset |
| LODS (SimI submeasure)  | 1. "LdDatasetMain"<br>2. "useIndexes"<br>3. "dataAugmentation"<br>4. "ontologyList"| The main dataset object for querying data <br>Specifies wether true or false for index usage <br> Specifies wether true or false for data augmentation to calculate the similarity <br> The list of ontologies to be used for calculation|