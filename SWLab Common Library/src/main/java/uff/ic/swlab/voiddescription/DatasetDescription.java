package uff.ic.swlab.voiddescription;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.WebContent;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public abstract class DatasetDescription {

    protected abstract Model getStructure();

    protected Model execConstruct(String queryString, String endpoint) {
        Model result = ModelFactory.createDefaultModel();
        try (QueryExecution exec = new QueryEngineHTTP(endpoint, queryString)) {
            ((QueryEngineHTTP) exec).setModelContentType(WebContent.contentTypeRDFXML);
            exec.execConstruct(result);
        }
        return result;
    }
}
