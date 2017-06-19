package uff.ic.swlab.commons.util;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.WebContent;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class SWLabHost {

    private static final String PRIMARY_HOSTNAME = "swlab.ic.uff.br";
    private static final String PRIMARY_PORT = "";

    private static final String ALTERNATE_HOSTNAME = "www.paes-leme.name";
    private static final String ALTERNATE_PORT = ":8080";

    public static final String HOSTNAME = PRIMARY_HOSTNAME;
    public static final String BASE_URL = "http://" + PRIMARY_HOSTNAME + PRIMARY_PORT + "/";

    private static final String SPARQL_ENDPOINT_URL = SWLabHost.BASE_URL + "fuseki/%1s/sparql";

    public static String getSPARQLEndpoint(String datasetname) {
        return String.format(SPARQL_ENDPOINT_URL, datasetname);
    }

    public static Model execConstruct(String queryString, String endpoint) {
        Model result = ModelFactory.createDefaultModel();
        try (final QueryExecution exec = new QueryEngineHTTP(endpoint, queryString)) {
            ((QueryEngineHTTP) exec).setModelContentType(WebContent.contentTypeRDFXML);
            exec.execConstruct(result);
        }
        return result;
    }

}
