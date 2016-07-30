package ic.uff.tic10086.virtuoso;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.ext.com.google.common.base.Joiner;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class DBpediaSearch {

    private static final String searchQueryPrototype = ""
            + "select distinct ?s \n"
            + "where {graph ?g {?s ?p ?o. \n"
            + "                 ?o <bif:contains> '(%1s)' option(score ?sc).}} \n"
            + "order by desc(?sc * 3e-1 + <sql:rnk_scale> (<LONG::IRI_RANK> (?s))) \n"
            + "limit %2d \n"
            + "offset %3d";

    public static void main(String[] args) {
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run() throws FileNotFoundException {
        String dbpediaSparqlEndpoint = "http://dbpedia.org/sparql";
        String keywordsString = "Cidade das Artes";
        Model model = ModelFactory.createDefaultModel();
        //model.setNsPrefix("owl", "http://www.w3.org/2002/07/owl#");

        String queryString = prepareSearchQuery(keywordsString, 20, 0);
        try (QueryExecution exec = new QueryEngineHTTP(dbpediaSparqlEndpoint, queryString)) {
            ResultSet rs = exec.execSelect();
            while (rs.hasNext())
                try {
                    Resource resource = rs.next().getResource("s");
                    System.out.println(resource.toString());

                    queryString = String.format("construct {<%1s> ?p ?o} where {<%2s> ?p ?o. }", resource.toString(), resource.toString());
                    QueryExecution q = QueryExecutionFactory.sparqlService(dbpediaSparqlEndpoint, queryString);
                    q.execConstruct(model);
                } catch (Exception e) {
                    System.out.println("*** error ***");
                }
        }
        FileOutputStream out = new FileOutputStream("./src/main/resources/dat/rdf/example.ttl", false);
        model.write(out, "TURTLE");
    }

    private static String prepareSearchQuery(String keywordsString, int limit, int offset) {
        String[] keywords = keywordsString.split("\\s");
        List<String> keywords2 = new ArrayList<>();
        for (String keyword : keywords)
            if (keyword != null && !keyword.equals(""))
                keywords2.add(keyword.toUpperCase());
        String keywordsConjunction = Joiner.on(" AND ").join(keywords2);
        String query = String.format(searchQueryPrototype, keywordsConjunction, limit, offset);
        return query;
    }
}
