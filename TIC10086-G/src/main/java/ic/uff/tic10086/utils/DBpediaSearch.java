package ic.uff.tic10086.utils;

import java.util.ArrayList;
import java.util.List;
import org.apache.jena.ext.com.google.common.base.Joiner;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class DBpediaSearch {

    private static final String SPARQL_ENDPOINT_URL = "http://dbpedia.org/sparql";
    private static final String searchQueryPrototype = ""
            + "select distinct ?s \n"
            + "where {graph ?g {?s ?p ?o. \n"
            + "                 ?o <bif:contains> '(%1s)' option(score ?sc).}} \n"
            + "order by desc(?sc * 3e-1 + <sql:rnk_scale> (<LONG::IRI_RANK> (?s))) \n"
            + "limit %2d \n"
            + "offset %3d";

    public static Model search(String keywordsString, int limit, int offset, Model model) {
        String queryString = prepareSearchQuery(keywordsString, limit, offset);
        try (QueryExecution exec = new QueryEngineHTTP(SPARQL_ENDPOINT_URL, queryString)) {
            ResultSet rs = exec.execSelect();
            while (rs.hasNext())
                try {
                    Resource resource = rs.next().getResource("s");
                    String resourceURI = resource.toString();

                    queryString = String.format("construct {<%1s> ?p ?o} where {<%2s> ?p ?o. }", resourceURI, resourceURI);
                    QueryExecution q = QueryExecutionFactory.sparqlService(SPARQL_ENDPOINT_URL, queryString);
                    q.execConstruct(model);

                } catch (Exception e) {
                    System.out.println("*** error ***");
                }
        }
        return model;
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
