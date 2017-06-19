package uff.ic.swlab.w3cvoid;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.WebContent;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public abstract class DatasetDescription {

    protected abstract Model getStructure();

    protected abstract Model getRootResources();

}
