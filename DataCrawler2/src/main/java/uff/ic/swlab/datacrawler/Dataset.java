package uff.ic.swlab.datacrawler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.bson.Document;

public class Dataset {

    Document doc = null;

    public Dataset(Document doc) {
        this.doc = doc;
    }

    public String getCtalogDatasetAPI() {
        try {
            return doc.get("extras2", Document.class).getString("catalog_dataset_api");
        } catch (Exception e) {
            return null;
        }
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

    public String[] getExampleURLs() {
        try {
            ArrayList<String> examples = doc.get("extras2", Document.class).get("examples", ArrayList.class);
            return examples != null ? examples.toArray(new String[0]) : new String[0];
        } catch (Exception e) {
            return new String[0];
        }
    }

    public String[] getVoIDURLs() {
        try {
            ArrayList<String> voids = doc.get("extras2", Document.class).get("voids", ArrayList.class);
            return voids != null ? voids.toArray(new String[0]) : new String[0];
        } catch (Exception e) {
            return new String[0];
        }
    }

    public String[] getURLs(Dataset dataset) {
        String[] urls;
        Set<String> set = new HashSet<>();
        set.add(dataset.getHomepage());
        set.addAll(Arrays.asList(dataset.getNamespaces()));
        set.addAll(Arrays.asList(dataset.getExampleURLs()));
        set.addAll(Arrays.asList(dataset.getVoIDURLs()));
        set = set.stream().filter((String line) -> line != null).collect(Collectors.toSet());
        urls = set.toArray(new String[0]);
        return urls;
    }

    public Model makeVoID() {
        Model void_ = ModelFactory.createDefaultModel();

        String ns = getCtalogDatasetAPI().replace("{}", "");
        Resource voidDataset = void_.createResource("http://rdfs.org/ns/void#Dataset");
        Resource voidLinkset = void_.createResource("http://rdfs.org/ns/void#Linkset");
        Property voidSubset = void_.createProperty("http://rdfs.org/ns/void#subset");
        Property voidSubjectsTarget = void_.createProperty("http://rdfs.org/ns/void#sujectsTarget");
        Property voidObjectstarget = void_.createProperty("http://rdfs.org/ns/void#objectsTarget");
        Property voidTriples = void_.createProperty("http://rdfs.org/ns/void#triples");

        Resource dataset = void_.createResource(getNameURI(), voidDataset);
        getLinks().stream().forEach((link) -> {
            Resource linkset = void_.createResource(null, voidLinkset)
                    .addProperty(voidSubjectsTarget, dataset)
                    .addProperty(voidObjectstarget, void_.createResource(ns + link.getKey()));
            String triples = link.getValue();
            if (triples != null && !triples.equals(""))
                linkset.addProperty(voidTriples, link.getValue());

            dataset.addProperty(voidSubset, linkset);
        });

        String homepage = getHomepage();
        if (homepage != null && !homepage.equals(""))
            dataset.addProperty(FOAF.homepage, void_.createResource(homepage));
        String triples = getTriples();
        if (triples != null && !triples.equals(""))
            dataset.addProperty(voidTriples, triples);

        return void_;
    }

    private Set<Entry<String, String>> getLinks() {
        String key, value;
        Map<String, String> links = new HashMap<>();
        try {
            ArrayList<Document> extras = doc.get("extras", ArrayList.class);
            for (Document ex : extras)
                try {
                    key = ex.getString("key");
                    value = ex.getString("value");
                    if (key.startsWith("links:"))
                        links.put(key.replace("links:", ""), value);
                } catch (Exception e) {
                }
        } catch (Exception e) {
        }
        return links.entrySet();
    }

    private String getTriples() {
        try {
            ArrayList<Document> extras = doc.get("extras", ArrayList.class);
            for (Document d : extras)
                try {
                    if (d.getString("key").equals("triples"))
                        return d.getString("value");
                } catch (Exception e) {
                }
        } catch (Exception e) {
        }
        return null;
    }

}
