package uff.ic.swlab.common.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class SparqlServer {

    public String dataURL = null;
    public String updateURL = null;
    public String sparqlURL = null;

    public SparqlServer() {
    }

    public SparqlServer(String dataURL, String updateURL, String sparqlURL) {
        this.dataURL = dataURL;
        this.updateURL = updateURL;
        this.sparqlURL = sparqlURL;
    }

    public void putModel(String graphURI, Model model) {
        if (graphURI != null && !graphURI.equals("")) {
            DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(dataURL);
            try {
                accessor.putModel(graphURI, model);
                System.out.println("Dataset <" + graphURI + "> has been loaded.");
            } catch (Throwable e) {
                System.out.println("Error putModel() graph <" + graphURI + ">.");
                System.out.println(e.getMessage());
            }
        }
    }

    private static final long HTTP_TIMEOUT = 10000;

    public List<String> listGraphNames() {
        List<String> graphNames = new ArrayList<>();

        String queryString = "select distinct ?g where {graph ?g {?s ?p ?o.}}";
        try (QueryExecution exec = new QueryEngineHTTP(sparqlURL, queryString)) {
            ((QueryEngineHTTP) exec).setTimeout(HTTP_TIMEOUT);
            ResultSet rs = exec.execSelect();
            while (rs.hasNext())
                graphNames.add(rs.next().getResource("g").getURI());
        } catch (Exception e) {
        }

        return graphNames;
    }
}
