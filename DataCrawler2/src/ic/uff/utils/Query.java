package ic.uff.utils;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.WebContent;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class Query {

	public static ResultSet exeSelect(String sparqlEndPoint, String queryString, int limit, int offset) {
		try (QueryExecution exec = new QueryEngineHTTP(sparqlEndPoint, queryString)) {
			((QueryEngineHTTP) exec).setModelContentType(WebContent.contentTypeRDFXML);
			return exec.execSelect();
		} catch (Exception e) {
		}
		return null;
	}

	public static Model execConstruct(String sparqlEndPoint, String queryString, int limit, int offset) {
		try (QueryExecution exec = new QueryEngineHTTP(sparqlEndPoint, queryString)) {
			((QueryEngineHTTP) exec).setModelContentType(WebContent.contentTypeRDFXML);
			return exec.execConstruct();
		} catch (Exception e) {
		}
		return null;
	}

}
