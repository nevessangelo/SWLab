package uff.ic.swlab.datacrawler;

import eu.trentorise.opendata.jackan.CkanClient;
import eu.trentorise.opendata.jackan.model.CkanDataset;
import eu.trentorise.opendata.jackan.model.CkanDatasetRelationship;
import eu.trentorise.opendata.jackan.model.CkanPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;

public class Dataset {

    private final CkanDataset doc;
    private final CkanClient cc;

    public Dataset(CkanClient cc, CkanDataset doc) {
        this.doc = doc;
        this.cc = cc;
    }

    public String getCtalogDatasetAPI() {
        try {
            return cc.getCatalogUrl() + "/api/rest/dataset/";
        } catch (Exception e) {
            return null;
        }
    }

    public String getNameURI() {
        try {
            return getCtalogDatasetAPI() + doc.getName();
        } catch (Exception e) {
            return null;
        }
    }

    public String getHomepage() {
        try {
            return doc.getUrl();
        } catch (Exception e) {
            return null;
        }
    }

    public String[] getSparqlEndPoints() {
        try {
            List<String> sparqlEndPoints = new ArrayList<>();
            doc.getResources().stream().forEach((resource) -> {
                try {
                    if (resource.getDescription().toLowerCase().contains("sparql")
                            || resource.getFormat().toLowerCase().contains("sparql")
                            || resource.getUrl().toLowerCase().contains("sparql"))
                        sparqlEndPoints.add(resource.getUrl());
                } catch (Throwable e) {
                }
            });
            return sparqlEndPoints.toArray(new String[0]);
        } catch (Exception e) {
            return new String[0];
        }
    }

    public String[] getNamespaces() {
        try {
            List<String> namespaces = new ArrayList<>();
            for (CkanPair extra : doc.getExtras())
                try {
                    if (extra.getKey().toLowerCase().contains("namespace")) {
                        namespaces.add(extra.getValue());
                        break;
                    }
                } catch (Throwable e) {
                }
            return namespaces.toArray(new String[0]);
        } catch (Exception e) {
            return new String[0];
        }
    }

    public String[] getExampleURLs() {
        try {
            List<String> examples = new ArrayList<>();
            doc.getResources().stream().forEach((resource) -> {
                try {
                    if (resource.getDescription().toLowerCase().contains("example")
                            || resource.getFormat().toLowerCase().contains("example")
                            || resource.getUrl().toLowerCase().contains("example"))
                        examples.add(resource.getUrl());
                } catch (Throwable e) {
                }
            });
            return examples.toArray(new String[0]);
        } catch (Exception e) {
            return new String[0];
        }
    }

    public String[] getVoIDURLs() {
        try {
            List<String> voids = new ArrayList<>();
            doc.getResources().stream().forEach((resource) -> {
                try {
                    if (resource.getDescription().toLowerCase().contains("void")
                            || resource.getFormat().toLowerCase().contains("void")
                            || resource.getUrl().toLowerCase().contains("void"))
                        voids.add(resource.getUrl());
                } catch (Throwable e) {
                }
            });
            return voids.toArray(new String[0]);
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
        Set<Entry<String, String>> links = getLinks();
        links.addAll(getLinks2());
        links.stream().forEach((link) -> {
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

    private Set<Entry<String, String>> getLinks2() {
        Map<String, String> links = new HashMap<>();
        try {
            for (CkanDatasetRelationship rel : doc.getRelationshipsAsSubject())
                links.put(cc.getDataset(rel.getObject()).getName(), rel.getComment());
        } catch (Throwable e) {
        }
        return links.entrySet();
    }

    private Set<Entry<String, String>> getLinks() {
        Map<String, String> links = new HashMap<>();
        try {
            String key, value;
            for (CkanPair ex : doc.getExtras())
                try {
                    key = ex.getKey();
                    value = ex.getValue();
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
            List<CkanPair> extras = doc.getExtras();
            for (CkanPair d : extras)
                try {
                    if (d.getKey().toLowerCase().equals("triples"))
                        return d.getValue();
                } catch (Exception e) {
                }
        } catch (Exception e) {
        }
        return null;
    }
}
