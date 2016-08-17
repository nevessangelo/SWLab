package uff.ic.tic10086.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang3.StringUtils;
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

    private static String clean(String s) {
        StringTokenizer defaultTokenizer = new StringTokenizer(s, " ://.-");
        List<String> l = new ArrayList<>();
        while (defaultTokenizer.hasMoreTokens())
            l.add(defaultTokenizer.nextToken());
        s = String.join(" ", l);
        s = StringUtils.stripAccents(s);
        s = s.toLowerCase();
        return s;
    }

    /**
     * Searches for descriptions of DBpedia resources. The relevant resources
     * are ranked according to the given keywords and the description graph of
     * each resource is added to the given model.
     *
     * @param keywordsString The keywords string.
     * @param limit Number of relevant resources to be retrieved.
     * @param offset The number of relevant resources to be skipped starting
     * from the top of the ranking.
     * @param model The RDF model for adding the description graphs.
     * @return The given model to allow cascading calls.
     */
    public static Model search(String keywordsString, int limit, int offset, Model model) {
        keywordsString = clean(keywordsString);
        String searchQuery = prepareSearchQuery(keywordsString, limit, offset);
        try (QueryExecution exec = new QueryEngineHTTP(SPARQL_ENDPOINT_URL, searchQuery)) {
            ((QueryEngineHTTP) exec).setModelContentType(WebContent.contentTypeRDFXML);
            ResultSet rs = exec.execSelect();

            while (rs.hasNext()) {
                String describeQuery = String.format("describe <%1s>", rs.next().get("s"));
                try (QueryExecution exec2 = new QueryEngineHTTP(SPARQL_ENDPOINT_URL, describeQuery);) {
                    ((QueryEngineHTTP) exec2).setModelContentType(WebContent.contentTypeRDFXML);
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
}
