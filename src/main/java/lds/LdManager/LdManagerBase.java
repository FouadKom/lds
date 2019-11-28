package lds.LdManager;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lds.measures.lods.ontologies.O;
import lds.measures.lods.ontologies.O_DBpedia;
import lds.measures.lods.ontologies.O_Schema;
import lds.measures.lods.ontologies.O_Umbel;
import lds.measures.lods.ontologies.O_WikiData;
import lds.measures.lods.ontologies.O_Yago;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.vocabulary.OWL;
import org.openrdf.model.URI;
import lds.resource.R;
import org.apache.jena.rdf.model.Resource;
import sc.research.ldq.LdDataset;
import slib.graph.model.impl.repo.URIFactoryMemory;
import slib.graph.model.repo.URIFactory;
import slib.utils.i.Conf;

public class LdManagerBase implements LdManager {

	protected LdDataset dataset;
	protected Conf config = null;
        protected String baseClassPath = "lds.LdManager.LdManagerBase.";

	public  LdManagerBase(LdDataset dataset) {
		this.dataset = dataset;
	}

	public  List<String> getSameAsResoures(R a) {

            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select ?sameAs " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where { " + a.getTurtle() + " <" + OWL.sameAs + "> ?sameAs. }");
            
            ResultSet rs = dataset.executeSelectQuery(query_cmd.toString());
            List<String> sameAsResources = new ArrayList<String>();
            
            
            
                for (; rs.hasNext();) {
                        QuerySolution qs = rs.nextSolution();
                        String sameAsResource = qs.getResource("sameAs").getURI();
                        sameAsResources.add(sameAsResource);
                }
            


            // dataset.close();
            return sameAsResources;
            

	}
        
        public  List<String> listShareCommonSubject(URI link , R a){
            List<String> shareSubjectwithA = new ArrayList();
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select distinct ?object " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where { ?subject <" + link + "> <" + a.getUri() + ">. "
                                                                   + "?subject <" + link + "> ?object ."
                                                                   + "filter(?object != <" + a.getUri() + "> && isuri(?object))}");
            
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());
            
            while (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                String resource = qs.getResource("object").getURI();
                shareSubjectwithA.add(resource);
            }

            
            // dataset.close();
            
            if(! shareSubjectwithA.isEmpty())
                return shareSubjectwithA;
            else
                return null;
        }
        
       
         public  List<String> listShareCommonObject(URI link , R a){
             
            List<String> shareObjectwithA = new ArrayList();
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select distinct ?subject " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where {<" + a.getUri() + "> <" + link + "> ?object . "
                                                                               + "?subject <" + link + "> ?object ."
                                                                               + "filter(?subject != <" + a.getUri() + ">)}");
            
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());
            
            
            while (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                String resource = qs.getResource("subject").getURI();
                shareObjectwithA.add(resource);

            }
            

            // dataset.close();
            
            if(! shareObjectwithA.isEmpty())
                return shareObjectwithA;
            else
                return null;
        }
        
         
        public  List<String> getObjects(R a){
            
            List<String> directlyConnectedObjects = new ArrayList<>();
            
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select distinct ?object " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where {<" + a.getUri() + "> ?property ?object ."
                    + " filter(isuri(?object)) }");
            
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());
            
            
            while (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                directlyConnectedObjects.add(qs.getResource("object").getURI());

            }
           
            
            // dataset.close();
            
            if(! directlyConnectedObjects.isEmpty())
                return directlyConnectedObjects;
            else
                return null;
            
        }
       
        
        public  List<String> getSubjects(R a){
            
            List<String> directlyConnectedSubjects = new ArrayList<>();
            
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select distinct ?subject " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where {?subject ?property <" + a.getUri() + "> ."
                    + "filter(isuri(?subject))}");
            
            System.out.println(query_cmd.toString());
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());
            
            
            while (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                directlyConnectedSubjects.add(qs.getResource("subject").getURI());

            }

            
            // dataset.close();
            
            if(! directlyConnectedSubjects.isEmpty())
                return directlyConnectedSubjects;
            else
                return null;
            
        }
               

        public  List<String> getSubjects(URI link, R a) {
            List<String> directlyConnectedSubjects = new ArrayList<>();
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select distinct ?subject " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where {?subject <" + link + "> <" + a.getUri() + "> .}");
            

            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());
            
            
            while (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                directlyConnectedSubjects.add(qs.getResource("subject").getURI());

            }
            
            // dataset.close();
            
            if(! directlyConnectedSubjects.isEmpty())
                return directlyConnectedSubjects;
            else
                return null;
        }
        
        
        public  List<String> getObjects(URI link , R a) {
            List<String> directlyConnectedObjects = new ArrayList<>();
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select distinct ?object " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where {<" + a.getUri() + "> <" + link + "> ?object ."
                    + " filter(isuri(?object)) }");
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());
            
            
            while (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                directlyConnectedObjects.add(qs.getResource("object").getURI());

            }
            
            // dataset.close();
            
            if(! directlyConnectedObjects.isEmpty())
                return directlyConnectedObjects;
            else
                return null;
            
        }
        
        @Override
	public  int countPropertyOccurrence(URI link) {
            Literal count = null;
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select (count(?subject) as ?count) " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where { ?subject <" + link + "> ?object. filter(isuri(?object))}");
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());
            
            
            if (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                count = (Literal) qs.getLiteral("count");
                // dataset.close();
                return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

            }
            

            // dataset.close();
            return 0;
                
	}
        
        @Override
        public  List<String> getIngoingEdges(R a){
            Resource edge = null;
		List<String> edges = new ArrayList<>();

		ParameterizedSparqlString query_cmd = dataset.prepareQuery();

                query_cmd.setCommandText("select distinct ?property \n"
                                            + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ "> \n") 
                                            + "where {[] ?property <" + a.getUri() + ">  }");
                                         

		ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());
                
                
		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.nextSolution();
			edge = (Resource) qs.getResource("property");
                        edges.add(edge.toString());
                        

		}
     
                
                // dataset.close();
                
                if(! edges.isEmpty())
                    return edges;
                else
                    return null;
		
        }
        
        @Override
        public  List<String> getOutgoingEdges(R a){
            Resource edge = null;
		List<String> edges = new ArrayList<>();

		ParameterizedSparqlString query_cmd = dataset.prepareQuery();

                query_cmd.setCommandText("select distinct ?property \n"
                                            + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ "> \n") 
                                            + "where {<" + a.getUri() + "> ?property ?object."
                                            + "filter isuri(?object)}");
                                            
              
		ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());
                
                
		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.nextSolution();
			edge = (Resource) qs.getResource("property");
			edges.add(edge.toString());
                        

		}

                
                // dataset.close();
		
                if(! edges.isEmpty())
                    return edges;
                else
                    return null;
        }
        
                
        @Override
        public  boolean isSameAs(R a, R b) {
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("ask {<" + a.getUri() + ">  <" + OWL.sameAs + "> <" + b.getUri() + "> . }");
            
            boolean result = dataset.executeAskQuery(query_cmd.toString());
            
            
            return result;
	}

     

        @Override
        public  boolean isDirectlyConnected(URI link, R a, R b) {
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("ask {<" + a.getUri() + ">  <" + link + "> <" + b.getUri() + "> . }");
            
            boolean result = dataset.executeAskQuery(query_cmd.toString());
            
            
            return result;
        }
        
        
        @Override
        public  int countSubject(URI link, R a) {
            
            Literal count = null;
            
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select (count(?subject) as ?count) " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where {?subject <" + link + "> <" + a.getUri() + "> ."
                    + "filter(isuri(?subject))}");
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());
            
            
            if (resultSet.hasNext()) {
                
                QuerySolution qs = resultSet.nextSolution();
                count = (Literal) qs.getLiteral("count");
                // dataset.close();
                return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

            }

            // dataset.close();
            return 0;
        }
        
        @Override
        public  int countSubject(R a){
            
            Literal count = null;
            
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select (count(distinct ?subject) as ?count) " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where {?subject ?property <" + a.getUri() + ">. }");
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            if (resultSet.hasNext()) {
                
                QuerySolution qs = resultSet.nextSolution();
                count = (Literal) qs.getLiteral("count");
                // dataset.close();
                return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

            }

            // dataset.close();
            return 0;
            
        }
        
        @Override
       public  int countObject(R a){
            
            Literal count = null;
            
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select (count(?object) as ?count) " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where {<" + a.getUri() + "> ?property ?object ."
                    + " filter(isuri(?object)) }");
            

            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            if (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                count = (Literal) qs.getLiteral("count");
                // dataset.close();
                return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

            }

            // dataset.close();
            return 0;
            
        }
       
       
      @Override
      public  int countObject(URI link , R a) {
            Literal count = null;
            
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select (count(distinct ?object) as ?count) " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where {<" + a.getUri() + "> <" + link + "> ?object."
                    + " filter(isuri(?object)) }");
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            if (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                count = (Literal) qs.getLiteral("count");
                // dataset.close();
                return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

            }

            // dataset.close();
            return 0;
        }
      
   

        @Override
        public  int countShareCommonObjects(URI link, R a) {
            Literal count = null;
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select (count(distinct ?subject) as ?count) " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where {<" + a.getUri() + "> <" + link + "> ?object . "
                                                                                       + "?subject <" + link + "> ?object ."
                                                                                     + "filter(?subject != <" + a.getUri() + ">)}");
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            if (resultSet.hasNext()) {
                    QuerySolution qs = resultSet.nextSolution();
                    count = (Literal) qs.getLiteral("count");
                    // dataset.close();
                    return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

            }
                
            // dataset.close();
            return 0;

        }
        

        @Override
        public  int countShareCommonSubjects(URI link, R a) {
            Literal count = null;
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select (count(distinct ?object) as ?count) " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where { ?subject <" + link + "> <" + a.getUri() + ">. "
                                                                                       + "?subject <" + link + "> ?object."
                                                                               + "filter(?object != <" + a.getUri() + "> && isuri(?object) ) }");
            
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            if (resultSet.hasNext()) {
                    QuerySolution qs = resultSet.nextSolution();
                    count = (Literal) qs.getLiteral("count");
                    // dataset.close();
                    return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

            }
                
                // dataset.close();
                return 0;
        }
        
        @Override
        public  int countShareCommonObjects(URI link, R a , R b){
            Literal count = null;
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select (count(distinct ?object) as ?count) " 
                                     + (dataset.getDefaultGraph() == null ? ("") : "from <" 
                                     + dataset.getDefaultGraph()+ ">") 
                                     + " where { <" + a.getUri() + ">  <" + link + "> ?object. "
                                     + "<" + b.getUri() + "> <" + link + "> ?object.}");
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            if (resultSet.hasNext()) {
                    QuerySolution qs = resultSet.nextSolution();
                    count = (Literal) qs.getLiteral("count");
                    // dataset.close();
                    return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

            }

            // dataset.close();
            return 0;
        }
        
        @Override
        public  int countShareCommonSubjects(URI link , R a , R b){
            Literal count = null;
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select (count(distinct ?subject) as ?count) " 
                                        + (dataset.getDefaultGraph() == null ? ("") : "from <" 
                                        + dataset.getDefaultGraph()+ ">") 
                                        + " where { ?subject <" + link + "> <" + a.getUri() + ">. "
                                        + "?subject <" + link + "> <" + b.getUri() + ">.}");
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            if (resultSet.hasNext()) {
			QuerySolution qs = resultSet.nextSolution();
			count = (Literal) qs.getLiteral("count");
                        // dataset.close();
                        return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

            }
    

            // dataset.close();
            return 0;
            
        }
        
        @Override
        public  int countShareTyplessCommonObjects(URI link1 , URI link2 , R a) {
            Literal count = null;
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select (count(?subject) as ?count) " 
                                    + (dataset.getDefaultGraph() == null ? ("") : "from <" 
                                    + dataset.getDefaultGraph()+ ">") 
                                    + " where {<" + a.getUri() + "> <" + link1 + "> ?object . "
                                    + "?subject <" + link2 + "> ?object}");
            
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            if (resultSet.hasNext()) {
                    QuerySolution qs = resultSet.nextSolution();
                    count = (Literal) qs.getLiteral("count");
                    // dataset.close();
                    return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

            }
            

            // dataset.close();
            return 0;
            
            
        }

        @Override
        public  int countShareTyplessCommonSubjects(URI link1 , URI link2 , R a) {
            Literal count = null;
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select (count(?object) as ?count) " 
                                    + (dataset.getDefaultGraph() == null ? ("") : "from <" 
                                    + dataset.getDefaultGraph()+ ">") 
                                     + " where { ?subject <" + link1 + "> <" + a.getUri() + ">. "
                                    + "?subject <" + link2 + "> ?object}");
            
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            if (resultSet.hasNext()) {
                    QuerySolution qs = resultSet.nextSolution();
                    count = (Literal) qs.getLiteral("count");
                    // dataset.close();
                    return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

            }
            
                
            // dataset.close();
            return 0;
        }

        @Override
        public  int countShareTyplessCommonObjects(URI link1 , URI link2 , R a , R b) {
            Literal count = null;
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select (count(distinct ?object) as ?count) " + 
                    (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") 
                     + " where {<" + a.getUri() + "> <" + link1 + "> ?object . "
                     + "<" + b.getUri() + "> <" + link2 + "> ?object .}");
            
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            if (resultSet.hasNext()) {
                    QuerySolution qs = resultSet.nextSolution();
                    count = (Literal) qs.getLiteral("count");
                    // dataset.close();
                    return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

            }

            // dataset.close();
            return 0;
        }

        @Override
        public  int countShareTyplessCommonSubjects(URI link1 , URI link2 , R a , R b) {
            Literal count = null;
            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select (count(distinct ?subject) as ?count) " + 
                    (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") 
                     + " where {?subject <" + link1 + "> <" + a.getUri() + "> . "
                     + "?subject <" + link2 + "> <" + b.getUri() + "> .}");
            
            
            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            if (resultSet.hasNext()) {
                    QuerySolution qs = resultSet.nextSolution();
                    count = (Literal) qs.getLiteral("count");
                    // dataset.close();
                    return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

            }

            // dataset.close();
            return 0;
        }

        @Override
        public Set<URI> getEdges(R a, R b) {
            Set<URI> edges = new HashSet();
            Resource edge;
            URIFactory factory = URIFactoryMemory.getSingleton();

            ParameterizedSparqlString query_cmd = dataset.prepareQuery();

            query_cmd.setCommandText("select distinct ?property \n"
                                    + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ "> \n") 
                                    + "where { \n"
                                    + "{ \n"
                                    + "select distinct ?property \n"
                                    + "where {?subject ?property []. \n" 
                                    + "filter(?subject IN (<" + a.getUri() + "> , <" + b.getUri() +"> )  ) } \n"
                                    + "} \n"
                                    + "union \n"
                                    + "{ \n"
                                    + "select distinct ?property \n"
                                    + "where {[] ?property ?object. \n"
                                    + "filter(?object IN (<" + a.getUri() + "> , <" + b.getUri() +"> )  ) } \n"
                                    + "} \n"
                                    + "}");
            

            ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

            while (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                edge = (Resource) qs.getResource("property");
                edges.add(factory.getURI(edge.toString()));
            }
            
          // dataset.close();

          if(! edges.isEmpty())
                return edges;
            else
                return null;
        }       

    @Override
    public  int countResource() {
        Literal count = null;
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        query_cmd.setCommandText("select (count(distinct ?s) as ?count) " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") 
                                   + " WHERE\n" +
                                "  {\n" +
                                "    ?s ?p ?o .\n" +
                                "    FILTER ( REGEX (STR (?s), \"resource\" ) )\n" +
                               "  }");
        
        
        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        if (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                count = (Literal) qs.getLiteral("count");
                // dataset.close();
                return Integer.parseInt(count.toString().substring(0, count.toString().indexOf("^^")));

        }
        

        // dataset.close();
        return 0;
        
    }
    
    @Override
    public  List<String> getTyplessCommonObjects(R a , R b){
        List<String> commonObjects = new ArrayList();
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        query_cmd.setCommandText("select distinct ?object " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") 
                                                                           + " where {<" + a.getUri() + "> ?property1 ?object . "
                                                                           + "<" + b.getUri() + "> ?property2 ?object."
                                                                           + "filter( isuri(?object) )}");
        
        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            String resource = qs.getResource("object").getURI();
            commonObjects.add(resource);

        }

        // dataset.close();

        if(! commonObjects.isEmpty())
            return commonObjects;
        else
            return null;

    }
        
    @Override
    public  List<String> getTyplessCommonSubjects(R a , R b){
        List<String> commonSubjects = new ArrayList();
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        query_cmd.setCommandText("select distinct ?subject " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") + " where { ?subject ?property1 <" + a.getUri() + "> . "
                                                                           + "?subject ?property2 <" + b.getUri() + ">}");
        
        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            String resource = qs.getResource("subject").getURI();
            commonSubjects.add(resource);

        }

        // dataset.close();

        if(! commonSubjects.isEmpty())
            return commonSubjects;
        else
            return null;

    }
        
    @Override
    public  List<String> getCommonSubjects(R a , R b){
        List<String> commonSubjects = new ArrayList();
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        query_cmd.setCommandText("select distinct ?subject " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") 
                                                             + " where { ?subject ?property <" + a.getUri() + "> . "
                                                             + "?subject ?property <" + b.getUri() + ">}");
        
        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            String resource = qs.getResource("subject").getURI();
            commonSubjects.add(resource);

        }
        

        // dataset.close();

        if(! commonSubjects.isEmpty())
            return commonSubjects;
        else
            return null;

    }
    
    @Override
    public  List<String> getCommonObjects(R a , R b){
        List<String> commonObjects = new ArrayList();
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();

        query_cmd.setCommandText("select distinct ?object " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") 
                                                            + " where {<" + a.getUri() + "> ?property ?object . "
                                                            + "<" + b.getUri() + "> ?property ?object . "
                                                            + "filter(isuri(?object))}");
        
        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            String resource = qs.getResource("object").getURI();
            commonObjects.add(resource);

        }

        // dataset.close();

        if(! commonObjects.isEmpty())
            return commonObjects;
        else
            return null;

    }

    public List<String> getOntologiesPrefixes(R a) {
        List<String> ontologies = new ArrayList();
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();
        

        query_cmd.setCommandText("select distinct ?ontology " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">") 
                                                              + " where {<" + a.getUri() + "> a ?ontology .}");      
       
        
        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            String ontologyPrefix = qs.getResource("ontology").getNameSpace();
            if(!ontologies.contains(ontologyPrefix))
                ontologies.add(ontologyPrefix);            

        }
        
        if(! ontologies.isEmpty())
            return ontologies;
        else
            return null;
    }
    
    
    public List<String> getAugmentdOntologiesPrefixes(R a){
        List<String> ontologies = new ArrayList();
        ParameterizedSparqlString query_cmd = dataset.prepareQuery();
        

        query_cmd.setCommandText("select distinct ?concept " + (dataset.getDefaultGraph() == null ? ("") : "from <" + dataset.getDefaultGraph()+ ">")
                                                             + " where {<" + a.getUri() + "> <http://www.w3.org/2002/07/owl#sameAs> ?concept .}");    
       
        
        ResultSet resultSet = dataset.executeSelectQuery(query_cmd.toString());

        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            String ontologyPrefix = qs.getResource("concept").getNameSpace();
            if(!ontologies.contains(ontologyPrefix))
                ontologies.add(ontologyPrefix);            

        }    
 
        
        if(! ontologies.isEmpty())
            return ontologies;
        else
            return null;
    }
    

    

}
