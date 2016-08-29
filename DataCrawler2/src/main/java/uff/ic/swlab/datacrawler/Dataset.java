package uff.ic.swlab.datacrawler;

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
        return doc.get("extras2", Document.class).getString("homepage");
    }

    public String[] getSparqlEndPoints() {
        try {
            String[] sparqlEndPoints = doc.get("extras2", Document.class).get("sparqlEndPoints", String[].class);
            return sparqlEndPoints != null ? sparqlEndPoints : new String[0];
        } catch (Exception e) {
            return new String[0];
        }
    }

    public String[] getNamespaces() {
        try {
            String[] namespaces = doc.get("extras2", Document.class).get("namespaces", String[].class);
            return namespaces != null ? namespaces : new String[0];
        } catch (Exception e) {
            return new String[0];
        }
    }

    public String[] getVoids() {
        try {
            String[] voids = doc.get("extras2", Document.class).get("voids", String[].class);
            return voids != null ? voids : new String[0];
        } catch (Exception e) {
            return new String[0];
        }
    }
}
