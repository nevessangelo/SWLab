package uff.ic.swlab.commons.util.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.WebContent;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uff.ic.swlab.commons.util.DCConf;
import uff.ic.swlab.commons.util.TaskExecutor;

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

    public synchronized void putModel(String graphURI, Model model) throws InterruptedException {
        if (graphURI != null && !graphURI.equals("")) {
            DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(dataURL);
            try {
                Runnable save = () -> {
                    try {
                        accessor.putModel(graphURI, model);
                    } catch (Throwable e) {
                        Logger.getLogger("error").log(Level.ERROR, String.format("Error putModel(%1s,...). Msg: %2s", graphURI, e.getMessage()));
                    }
                };
                TaskExecutor.executeTask(save, "save graph " + graphURI, DCConf.MODEL_WRITE_TIMEOUT);
                Logger.getLogger("info").log(Level.INFO, String.format("Dataset saved (<%1s>).", graphURI));
            } catch (InterruptedException ex) {
                throw new InterruptedException();
            } catch (Throwable e) {
                Logger.getLogger("error").log(Level.ERROR, String.format("Error putModel(%1s,?). Msg: %2s.", graphURI, e.getMessage()));
            }
        }
    }

    public static void readVoIDFromSparql(Model model, String sparqlEndPoint) throws TimeoutException, InterruptedException {
        try {
            Runnable task = () -> {
                String from = "";
                String queryString = "construct {?s ?p ?o}\n %1swhere {?s ?p ?o.}";
                try {
                    Model m = ModelFactory.createDefaultModel();
                    from = listVoIDGraphNames(sparqlEndPoint).stream().map((String n) -> String.format("from <%1s>\n", n)).reduce(from, String::concat);
                    queryString = String.format(queryString, from);
                    try (final QueryExecution exec = new QueryEngineHTTP(sparqlEndPoint, queryString)) {
                        ((QueryEngineHTTP) exec).setModelContentType(WebContent.contentTypeRDFXML);
                        ((QueryEngineHTTP) exec).setTimeout(DCConf.SPARQL_TIMEOUT);
                        exec.execConstruct(m);
                        model.add(m);
                    }
                } catch (Throwable e) {
                }
            };
            TaskExecutor.executeTask(task, "read " + sparqlEndPoint, DCConf.SPARQL_TIMEOUT);
        } catch (InterruptedException ex) {
            throw new InterruptedException();
        } catch (TimeoutException e) {
        }
    }

    private static List<String> listVoIDGraphNames(String sparqlEndPoint) {
        List<String> graphNames = new ArrayList<>();
        String name;
        String queryString = "select distinct ?g where {graph ?g {?s ?p ?o.}}";
        try (final QueryExecution exec = new QueryEngineHTTP(sparqlEndPoint, queryString)) {
            ((QueryEngineHTTP) exec).setTimeout(DCConf.SPARQL_TIMEOUT);
            ResultSet rs = exec.execSelect();
            while (rs.hasNext()) {
                name = rs.next().getResource("g").getURI();
                if (name.contains("void"))
                    graphNames.add(name);
            }
        } catch (Throwable e) {
        }
        return graphNames;
    }

}
