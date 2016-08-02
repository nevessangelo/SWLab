package ic.uff.tic10086.utils;

import java.util.ArrayList;
import java.util.List;
import org.apache.jena.ext.com.google.common.base.Joiner;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.WebContent;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class DBpediaSearch {

    private static final String SPARQL_ENDPOINT_URL = "http://dbpedia.org/sparql";
    private static final String searchQueryPrototype = ""
            + "select distinct ?s \n"
            + "where {graph ?g {?s ?p ?o. \n"
            + "                 ?o <bif:contains> '(%1s)' option(score ?sc). \n"
            + "                 FILTER regex(str(?s), \"http://dbpedia.org/resource/\")}} \n"
            + "order by desc(?sc * 3e-1 + <sql:rnk_scale> (<LONG::IRI_RANK> (?s))) \n"
            + "limit %2d \n"
            + "offset %3d";

    public static Model search(String keywordsString, int limit, int offset, Model model) {
        String queryString = prepareSearchQuery(keywordsString, limit, offset);
        try (QueryExecution exec = new QueryEngineHTTP(SPARQL_ENDPOINT_URL, queryString)) {
            ((QueryEngineHTTP) exec).setModelContentType(WebContent.contentTypeJSONLD);
            ResultSet rs = exec.execSelect();
            while (rs.hasNext()) {
                String queryString2 = String.format("describe <%1s>", rs.next().get("s"));
                try (QueryExecution exec2 = new QueryEngineHTTP(SPARQL_ENDPOINT_URL, queryString);) {
                    ((QueryEngineHTTP) exec).setModelContentType(WebContent.contentTypeJSONLD);
                    exec2.execDescribe(model);
                } catch (Exception e) {
                    System.out.println("*** Error while describing resource. ****");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + keywordsString);
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
