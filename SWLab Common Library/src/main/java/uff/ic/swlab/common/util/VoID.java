package uff.ic.swlab.common.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RiotNotFoundException;
import org.apache.jena.riot.WebContent;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.vocabulary.RDF;

public class VoID {

    public static Model retrieveVoID(String[] urls, String[] sparqlEndPoints) throws InterruptedException {
        Model void_ = ModelFactory.createDefaultModel();

        void_.add(VoID.retrieveVoIDFromURL(urls));
        void_.add(VoID.retrieveVoIDFromSparql(sparqlEndPoints));

        return void_;
    }

    private static Model retrieveVoIDFromURL(String[] urls) throws InterruptedException {
        Model void_ = ModelFactory.createDefaultModel();

        Model tempModel;
        try {
            String[] voidURLs = listPotentialVoIDURLs(urls);
            for (String url : voidURLs)
                try {
                    tempModel = ModelFactory.createDefaultModel();
                    RDFDataMgr.read(tempModel, url);
                    interrupted();
                    if (isVoID(tempModel))
                        void_.add(tempModel);
                    interrupted();
                } catch (RiotNotFoundException e1) {
                } catch (InterruptedException e2) {
                    throw new InterruptedException();
                } catch (Throwable e3) {
                    Lang[] langs = {Lang.TURTLE, Lang.RDFXML, Lang.NTRIPLES, Lang.TRIG,
                        Lang.NQUADS, Lang.JSONLD, Lang.RDFJSON, Lang.TRIX, Lang.RDFTHRIFT};
                    boolean read = false;
                    for (Lang lang : langs)
                        try {
                            tempModel = ModelFactory.createDefaultModel();
                            RDFDataMgr.read(tempModel, url, lang);
                            interrupted();
                            if (isVoID(tempModel)) {
                                void_.add(tempModel);
                                read = true;
                                break;
                            }
                            interrupted();
                        } catch (InterruptedException e31) {
                            throw new InterruptedException();
                        } catch (Throwable e32) {
                        }
                    if (!read)
                        try {
                            tempModel = ModelFactory.createDefaultModel();
                            RDFaDataMgr.read(tempModel, url);
                            interrupted();
                            if (isVoID(tempModel))
                                void_.add(tempModel);
                            interrupted();
                        } catch (InterruptedException e31) {
                            throw new InterruptedException();
                        } catch (Throwable e33) {
                        }
                }
        } catch (InterruptedException e1) {
            throw new InterruptedException();
        } catch (Throwable e2) {
        }

        return void_;
    }

    private static final long HTTP_TIMEOUT = 10000;

    private static Model retrieveVoIDFromSparql(String[] sparqlEndPoints) throws InterruptedException {
        Model void_ = ModelFactory.createDefaultModel();

        try {
            Model tempModel;
            String from = "";
            String queryString = "construct {?s ?p ?o}\n %1swhere {?s ?p ?o.}";
            for (String sparqlEndPoint : sparqlEndPoints) {
                tempModel = ModelFactory.createDefaultModel();
                from = listVoIDGraphNames(sparqlEndPoint)
                        .stream()
                        .map((n) -> String.format("from <%1s>\n", n))
                        .reduce(from, String::concat);
                queryString = String.format(queryString, from);
                interrupted();

                try (QueryExecution exec = new QueryEngineHTTP(sparqlEndPoint, queryString)) {
                    ((QueryEngineHTTP) exec).setModelContentType(WebContent.contentTypeRDFXML);
                    ((QueryEngineHTTP) exec).setTimeout(HTTP_TIMEOUT);
                    exec.execConstruct(tempModel);
                    interrupted();
                    if (isVoID(tempModel))
                        void_.add(tempModel);
                } catch (InterruptedException e1) {
                    throw new InterruptedException();
                } catch (Throwable e2) {
                }
            }
        } catch (InterruptedException e1) {
            throw new InterruptedException();
        } catch (Throwable e2) {
        }

        return void_;
    }

    private static List<String> listVoIDGraphNames(String sparqlEndPoint) {
        List<String> graphNames = new ArrayList<>();

        String name;
        String queryString = "select distinct ?g where {graph ?g {?s ?p ?o.}}";
        try (QueryExecution exec = new QueryEngineHTTP(sparqlEndPoint, queryString)) {
            ((QueryEngineHTTP) exec).setTimeout(HTTP_TIMEOUT);
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

    private static String[] listPotentialVoIDURLs(String[] urls) {
        Set<String> voidURLs = new HashSet<>();

        try {
            for (String u : urls) {
                URL url = new URL(u);
                String protocol = url.getProtocol();
                String auth = url.getAuthority();
                String newPath = protocol + "://" + auth;
                voidURLs.add(newPath);
                voidURLs.add(newPath + "/.well-known/void");
                voidURLs.add(newPath + "/.well-known/void.ttl");
                voidURLs.add(newPath + "/.well-known/void.rdf");
                voidURLs.add(newPath + "/void");
                voidURLs.add(newPath + "/void.ttl");
                voidURLs.add(newPath + "/void.rdf");
                voidURLs.add(newPath + "/models/void");
                voidURLs.add(newPath + "/models/void.ttl");
                voidURLs.add(newPath + "/models/void.rdf");
                String[] path = url.getPath().split("/");
                for (int i = 1; i < path.length; i++)
                    if (!path[i].contains("void")) {
                        newPath += "/" + path[i];
                        voidURLs.add(newPath);
                        voidURLs.add(newPath + "/void");
                        voidURLs.add(newPath + "/void.ttl");
                        voidURLs.add(newPath + "/void.rdf");
                    } else {
                        voidURLs.add(newPath + "/" + path[i]);
                        break;
                    }
            }
        } catch (Throwable e) {
        }

        return voidURLs.toArray(new String[0]);
    }

    public static boolean isVoID(Model model) {
        org.apache.jena.rdf.model.Resource voidDataset = model.getResource("http://rdfs.org/ns/void#Dataset");
        org.apache.jena.rdf.model.Resource voidLinkset = model.getResource("http://rdfs.org/ns/void#Linkset");
        boolean hasDataset = model.contains(null, RDF.type, voidDataset);
        boolean hasLinkset = model.contains(null, RDF.type, voidLinkset);
        return hasDataset || hasLinkset;
    }

    private static void interrupted() throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
    }
}
