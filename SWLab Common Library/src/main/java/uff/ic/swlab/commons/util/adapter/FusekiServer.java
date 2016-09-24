package uff.ic.swlab.commons.util.adapter;

import java.util.ArrayList;
import java.util.List;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uff.ic.swlab.commons.util.DCConf;

public class FusekiServer {

    public String dataURL = null;
    public String updateURL = null;
    public String sparqlURL = null;

    private FusekiServer() {
    }

    public FusekiServer(String server) {
        dataURL = server + "/data";
        updateURL = server + "/update";
        sparqlURL = server + "/sparql";
    }

    public FusekiServer(String dataURL, String updateURL, String sparqlURL) {
        this.dataURL = dataURL;
        this.updateURL = updateURL;
        this.sparqlURL = sparqlURL;
    }

    public void putModel(String graphURI, Model model) {
        if (graphURI != null && !graphURI.equals("")) {
            DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(dataURL);
            try {
                accessor.putModel(graphURI, model);
                Logger.getLogger("datasetcrawler").log(Level.INFO, "Dataset loaded: <" + graphURI + ">.");
            } catch (Throwable e) {
                Logger.getLogger("datasetcrawler").log(Level.ERROR, "Error putModel() (<" + graphURI + ">). Msg: " + e.getMessage());
            }
        }
    }

    public List<String> listGraphNames() {
        List<String> graphNames = new ArrayList<>();

        String queryString = "select distinct ?g where {graph ?g {?s ?p ?o.}}";
        try (QueryExecution exec = new QueryEngineHTTP(sparqlURL, queryString)) {
            ((QueryEngineHTTP) exec).setTimeout(DCConf.SPARQL_TIMEOUT);
            ResultSet rs = exec.execSelect();
            while (rs.hasNext())
                graphNames.add(rs.next().getResource("g").getURI());
        } catch (Exception e) {
        }

        return graphNames;
    }
}
