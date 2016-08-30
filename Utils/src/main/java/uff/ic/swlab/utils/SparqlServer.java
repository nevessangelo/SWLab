package uff.ic.swlab.utils;

import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

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

    public void putModel(String graphUri, Model model) {
        if (model.size() > 5 && graphUri != null && !graphUri.equals("")) {
            DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(dataURL);
            try {
                accessor.putModel(graphUri, model);
            } catch (Exception e) {
                Logger.getRootLogger().log(Priority.INFO, "Authority " + graphUri + " error VoID.");
                System.out.println("Authority " + graphUri + " error VoID.");
            }
        } else if (model.size() <= 5 && graphUri != null && !graphUri.equals("")) {
            Logger.getRootLogger().log(Priority.INFO, "Authority " + graphUri + " no VoID.");
            System.out.println("Authority " + graphUri + " no VoID.");
        }
    }
}
