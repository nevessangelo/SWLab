package uff.ic.swlab.datasetcrawler.model;

import eu.trentorise.opendata.jackan.CkanClient;
import eu.trentorise.opendata.jackan.model.CkanDataset;
import eu.trentorise.opendata.jackan.model.CkanDatasetRelationship;
import eu.trentorise.opendata.jackan.model.CkanPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.WebContent;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uff.ic.swlab.commons.util.DCConf;
import uff.ic.swlab.commons.util.helper.URLHelper;

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
        } catch (Throwable e) {
            return null;
        }
    }

    public String getNameURI() {
        try {
            return getCtalogDatasetAPI() + doc.getName();
        } catch (Throwable e) {
            return null;
        }
    }

    public String getHomepage() {
        try {
            String url = doc.getUrl();
            return URLHelper.normalize(url);
        } catch (Exception e) {
            return null;
        }
    }

    public String[] getTopics() {
        try {
            List<String> tags = new ArrayList<>();
            doc.getTags().stream().forEach((tag) -> {
                try {
                    if (!tag.getName().equals(""))
                        tags.add(tag.getName().trim());
                } catch (Throwable t) {
                }
            });
            return (new HashSet<>(tags)).toArray(new String[0]);
        } catch (Throwable e) {
            return new String[0];
        }
    }

    public String getUriSpace() {
        try {
            for (CkanPair extra : doc.getExtras())
                try {
                    if (extra.getKey().toLowerCase().contains("namespace"))
                        return extra.getValue().trim();
                } catch (Throwable e) {
                }
        } catch (Throwable e) {
        }
        return null;
    }

    public String[] getExampleURLs() {
        try {
            List<String> examples = new ArrayList<>();
            doc.getResources().stream().forEach((resource) -> {
                try {
                    if (resource.getDescription().toLowerCase().contains("example")
                            || resource.getFormat().toLowerCase().contains("example")
                            || resource.getUrl().toLowerCase().contains("example"))
                        examples.add(URLHelper.normalize(resource.getUrl()));
                } catch (Throwable e) {
                }
            });
            return (new HashSet<>(examples)).toArray(new String[0]);
        } catch (Throwable e) {
            return new String[0];
        }
    }

    public String[] getDumpURLs() {
        try {
            List<String> dumps = new ArrayList<>();
            doc.getResources().stream().forEach((resource) -> {
                try {
                    if (resource.getDescription().toLowerCase().contains("dump")
                            || resource.getFormat().toLowerCase().contains("dump")
                            || resource.getUrl().toLowerCase().contains("dump"))
                        dumps.add(URLHelper.normalize(resource.getUrl()));
                } catch (Throwable e) {
                }
            });
            return (new HashSet<>(dumps)).toArray(new String[0]);
        } catch (Throwable e) {
            return new String[0];
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
                        sparqlEndPoints.add(URLHelper.normalize(resource.getUrl()));
                } catch (Throwable e) {
                }
            });
            return (new HashSet<>(sparqlEndPoints)).toArray(new String[0]);
        } catch (Throwable e) {
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
                        voids.add(URLHelper.normalize(resource.getUrl()));
                } catch (Throwable e) {
                }
            });
            return (new HashSet<>(voids)).toArray(new String[0]);
        } catch (Throwable e) {
            return new String[0];
        }
    }

    public String[] getURLs(Dataset dataset) {
        String[] urls;
        Set<String> set = new HashSet<>();
        set.add(dataset.getHomepage());
        set.add(dataset.getUriSpace());
        set.addAll(Arrays.asList(dataset.getExampleURLs()));
        set.addAll(Arrays.asList(dataset.getVoIDURLs()));
        set.addAll(Arrays.asList(dataset.getDumpURLs()));
        set = set.stream().filter((String line) -> line != null).collect(Collectors.toSet());
        urls = set.toArray(new String[0]);
        return urls;
    }

    public String getTitle() {
        try {
            String title = doc.getTitle();
            if (!title.equals(""))
                return title;
        } catch (Throwable t) {
        }
        return null;
    }

    public String getDescription() {
        try {
            String notes = doc.getTitle();
            if (!notes.equals(""))
                return notes;
        } catch (Throwable t) {
        }
        return null;
    }

    public Calendar getCreateDate() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(doc.getMetadataCreated());
            return cal;
        } catch (Throwable t) {
            return null;
        }
    }

    public Calendar getModifiedDate() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(doc.getMetadataModified());
            return cal;
        } catch (Throwable t) {
            return null;
        }
    }

    private Set<Entry<String, Integer>> getLinks() {
        Map<String, Integer> links = new HashMap<>();
        String ns = getCtalogDatasetAPI();
        try {
            String key;
            for (CkanPair ex : doc.getExtras())
                try {
                    key = ex.getKey();
                    if (key.startsWith("links:"))
                        links.put(ns + key.replace("links:", "").trim().replaceAll(" ", "_"), Integer.parseInt(ex.getValue()));
                } catch (Throwable e) {
                }
        } catch (Throwable e) {
        }
        return links.entrySet();
    }

    private Set<Entry<String, Integer>> getLinks2() {
        Map<String, Integer> links = new HashMap<>();
        try {
            for (CkanDatasetRelationship rel : doc.getRelationshipsAsSubject())
                links.put(cc.getDataset(rel.getObject()).getName(), Integer.parseInt(rel.getComment()));
        } catch (Throwable e) {
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
                } catch (Throwable e) {
                }
        } catch (Throwable e) {
        }
        return null;
    }

    public Set<Entry<String, Integer>> getClasses() {
        Map<String, Integer> classes = new HashMap<>();

        try {
            for (String sparqlEndPoint : getSparqlEndPoints()) {
                Thread task = new Thread() {
                    @Override
                    public void run() {
                        String queryString = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                                + "select ?class (count(?s) as ?freq)\n"
                                + "WHERE {{?s rdf:type ?class} union {graph ?g {?s rdf:type ?class}}}\n"
                                + "group by ?class\n"
                                + "order by desc(?freq)\n"
                                + "limit 500";
                        try (QueryExecution exec = new QueryEngineHTTP(sparqlEndPoint, queryString)) {
                            ((QueryEngineHTTP) exec).setModelContentType(WebContent.contentTypeRDFXML);
                            ((QueryEngineHTTP) exec).setTimeout(DCConf.SPARQL_TIMEOUT);
                            ResultSet rs = exec.execSelect();
                            while (rs.hasNext()) {
                                QuerySolution qs = rs.next();
                                Resource class_ = qs.getResource("class");
                                Literal freq = qs.getLiteral("freq");
                                try {
                                    if (!class_.getURI().equals(""))
                                        classes.put(class_.getURI(), freq.getInt());
                                } catch (Throwable t) {
                                }
                            }
                        } catch (Throwable t) {
                        }
                    }
                };
                task.setDaemon(true);
                task.start();
                task.join(DCConf.SPARQL_TIMEOUT);
                if (task.isAlive()) {
                    task.stop();
                    Logger.getLogger("timeout").log(Level.WARN, "Timeout while reading " + sparqlEndPoint + ".");
                    throw new TimeoutException("Timeout while reading " + sparqlEndPoint + ".");
                }
            }
        } catch (Throwable e2) {
        }

        return classes.entrySet();
    }

    public Set<Entry<String, Integer>> getProperties() {
        Map<String, Integer> properties = new HashMap<>();

        try {
            for (String sparqlEndPoint : getSparqlEndPoints()) {
                Thread task = new Thread() {
                    @Override
                    public void run() {
                        String queryString = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                                + "select ?property (count(?s) as ?freq)\n"
                                + "WHERE {{?s ?property []} union {graph ?g {?s ?property []}}}\n"
                                + "group by ?property\n"
                                + "order by desc(?freq)\n"
                                + "limit 500";
                        try (QueryExecution exec = new QueryEngineHTTP(sparqlEndPoint, queryString)) {
                            ((QueryEngineHTTP) exec).setModelContentType(WebContent.contentTypeRDFXML);
                            ((QueryEngineHTTP) exec).setTimeout(DCConf.SPARQL_TIMEOUT);
                            ResultSet rs = exec.execSelect();
                            while (rs.hasNext()) {
                                QuerySolution qs = rs.next();
                                Resource property = qs.getResource("property");
                                Literal freq = qs.getLiteral("freq");
                                try {
                                    if (!property.getURI().equals(""))
                                        properties.put(property.getURI(), freq.getInt());
                                } catch (Throwable t) {
                                }
                            }
                        } catch (Throwable t) {
                        }
                    }
                };
                task.setDaemon(true);
                task.start();
                task.join(DCConf.SPARQL_TIMEOUT);
                if (task.isAlive()) {
                    task.stop();
                    Logger.getLogger("timeout").log(Level.WARN, "Timeout while reading " + sparqlEndPoint + ".");
                    throw new TimeoutException("Timeout while reading " + sparqlEndPoint + ".");
                }
            }
        } catch (Throwable e2) {
        }

        return properties.entrySet();
    }

    public Model makeVoID() {
        Model void_ = ModelFactory.createDefaultModel();

        Resource voidDataset = void_.createResource("http://rdfs.org/ns/void#Dataset");
        Resource voidLinkset = void_.createResource("http://rdfs.org/ns/void#Linkset");

        Property voidUriSpace = void_.createProperty("http://rdfs.org/ns/void#uriSpace");
        Property voidDataDump = void_.createProperty("http://rdfs.org/ns/void#dataDump");
        Property voidSubset = void_.createProperty("http://rdfs.org/ns/void#subset");
        Property voidSubjectsTarget = void_.createProperty("http://rdfs.org/ns/void#sujectsTarget");
        Property voidObjectstarget = void_.createProperty("http://rdfs.org/ns/void#objectsTarget");
        Property voidClassPartition = void_.createProperty("http://rdfs.org/ns/void#classPartition");
        Property voidClass = void_.createProperty("http://rdfs.org/ns/void#class");
        Property voidPropertyPartition = void_.createProperty("http://rdfs.org/ns/void#propertyPartition");
        Property voidProperty = void_.createProperty("http://rdfs.org/ns/void#property");
        Property voidTriples = void_.createProperty("http://rdfs.org/ns/void#triples");

        Resource dataset = void_.createResource(getNameURI(), voidDataset);
        Set<Entry<String, Integer>> links = getLinks();
        links.addAll(getLinks2());
        links.stream().forEach((link) -> {
            Resource linkset = void_.createResource(null, voidLinkset)
                    .addProperty(voidSubjectsTarget, dataset)
                    .addProperty(voidObjectstarget, void_.createResource(link.getKey()));
            Integer triples = link.getValue();
            if (triples != null && !triples.equals(""))
                linkset.addLiteral(voidTriples, link.getValue());
            dataset.addProperty(voidSubset, linkset);
        });

        Set<Entry<String, Integer>> classes = getClasses();
        classes.stream().forEach((class_) -> {
            dataset.addProperty(voidClassPartition, void_.createResource()
                    .addProperty(voidClass, class_.getKey())
                    .addLiteral(voidTriples, void_.createTypedLiteral(class_.getValue())));
        });

        Set<Entry<String, Integer>> properties = getProperties();
        properties.stream().forEach((property) -> {
            dataset.addProperty(voidPropertyPartition, void_.createResource()
                    .addProperty(voidProperty, property.getKey())
                    .addLiteral(voidTriples, void_.createTypedLiteral(property.getValue())));
        });

        List<String> dumps = Arrays.asList(getDumpURLs());
        dumps.stream().forEach((dump) -> {
            dataset.addProperty(voidDataDump, void_.createResource(dump));
        });

        List<String> tags = Arrays.asList(getTopics());
        tags.stream().forEach((tag) -> {
            dataset.addProperty(FOAF.topic, tag);
        });

        String title = getTitle();
        if (title != null && !title.equals(""))
            dataset.addProperty(DCTerms.title, title);

        String description = getDescription();
        if (description != null && !description.equals(""))
            dataset.addProperty(DCTerms.description, description);

        String homepage = getHomepage();
        if (homepage != null && !homepage.equals(""))
            dataset.addProperty(FOAF.homepage, void_.createResource(homepage));

        String uriSpace = getUriSpace();
        if (uriSpace != null && !uriSpace.equals(""))
            dataset.addProperty(voidUriSpace, uriSpace);

        String triples = getTriples();
        if (triples != null && !triples.equals(""))
            dataset.addProperty(voidTriples, triples);

        Calendar created = getCreateDate();
        if (created != null)
            dataset.addProperty(DCTerms.created, void_.createTypedLiteral(created));

        Calendar modified = getModifiedDate();
        if (modified != null)
            dataset.addProperty(DCTerms.modified, void_.createTypedLiteral(modified));

        return void_;
    }
}
