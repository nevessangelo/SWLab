package uff.ic.swlab.datacrawler;

import java.util.ArrayList;
import org.bson.Document;

public class Dataset {

    Document doc = null;

    public Dataset(Document doc) {
        this.doc = doc;
    }

    public String getNameURI() {
        try {
            return doc.get("extras2", Document.class).getString("name_uri");
        } catch (Exception e) {
            return null;
        }
    }

    public String getHomepage() {
        try {
            return doc.get("extras2", Document.class).getString("homepage");
        } catch (Exception e) {
            return null;
        }
    }

    public String[] getSparqlEndPoints() {
        try {
            ArrayList<String> sparqlEndPoints = doc.get("extras2", Document.class).get("sparql_endpoints", ArrayList.class);
            return sparqlEndPoints != null ? sparqlEndPoints.toArray(new String[0]) : new String[0];
        } catch (Exception e) {
            return new String[0];
        }
    }

    public String[] getNamespaces() {
        try {
            ArrayList<String> namespaces = doc.get("extras2", Document.class).get("namespaces", ArrayList.class);
            return namespaces != null ? namespaces.toArray(new String[0]) : new String[0];
        } catch (Exception e) {
            return new String[0];
        }
    }

    public String[] getExamples() {
        try {
            ArrayList<String> examples = doc.get("extras2", Document.class).get("examples", ArrayList.class);
            return examples != null ? examples.toArray(new String[0]) : new String[0];
        } catch (Exception e) {
            return new String[0];
        }
    }

    public String[] getVoids() {
        try {
            ArrayList<String> voids = doc.get("extras2", Document.class).get("voids", ArrayList.class);
            return voids != null ? voids.toArray(new String[0]) : new String[0];
        } catch (Exception e) {
            return new String[0];
        }
    }
}
