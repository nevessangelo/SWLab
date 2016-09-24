package uff.ic.swlab.commons.util.helper;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.WebContent;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.vocabulary.RDF;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uff.ic.swlab.commons.util.DCConf;
import uff.ic.swlab.commons.util.riot.RDFDataMgr;

public abstract class VoIDHelper {

    public static boolean isVoID(Model model) {
        org.apache.jena.rdf.model.Resource voidDataset = model.getResource("http://rdfs.org/ns/void#Dataset");
        org.apache.jena.rdf.model.Resource voidLinkset = model.getResource("http://rdfs.org/ns/void#Linkset");
        boolean hasDataset = model.contains(null, RDF.type, voidDataset);
        boolean hasLinkset = model.contains(null, RDF.type, voidLinkset);
        return hasDataset || hasLinkset;
    }

    public static Model getContent(String[] urls, String[] sparqlEndPoints) {
        Model void_ = ModelFactory.createDefaultModel();
        void_.add(getVoIDFromURL(urls));
        void_.add(getVoIDFromSparql(sparqlEndPoints));
        return void_;
    }

    private static Model getVoIDFromURL(String[] urls) {
        Model void_ = ModelFactory.createDefaultModel();

        for (String url : listPotentialVoIDURLs(urls))
            try {
                if (!URLHelper.isHTML(url)) {
                    Lang[] langs = {Lang.TURTLE, Lang.RDFXML, Lang.NTRIPLES, Lang.TRIG,
                        Lang.NQUADS, Lang.JSONLD, Lang.RDFJSON, Lang.TRIX, Lang.RDFTHRIFT};
                    for (Lang lang : langs)
                        try {
                            Model tempModel = ModelFactory.createDefaultModel();
                            Thread task = new Thread() {
                                @Override
                                public void run() {
                                    RDFDataMgr.read(tempModel, url, lang);
                                }
                            };
                            task.setDaemon(true);
                            task.start();
                            task.join(DCConf.MODEL_READ_TIMEOUT);
                            if (task.isAlive()) {
                                task.stop();
                                Logger.getLogger("timeout").log(Level.WARN, "Timeout while reading " + url + ".");
                                throw new TimeoutException("Timeout while reading " + url + ".");
                            }

                            if (isVoID(tempModel)) {
                                void_.add(tempModel);
                                break;
                            }
                        } catch (Throwable e32) {
                        }
                } else
                    try {
                        Model tempModel = ModelFactory.createDefaultModel();
                        Thread task = new Thread() {
                            @Override
                            public void run() {
                                RDFDataMgr.readRDFa(tempModel, url);
                            }
                        };
                        task.setDaemon(true);
                        task.start();
                        task.join(DCConf.MODEL_READ_TIMEOUT);
                        if (task.isAlive()) {
                            task.stop();
                            Logger.getLogger("timeout").log(Level.WARN, "Timeout while reading " + url + ".");
                            throw new TimeoutException("Timeout while reading " + url + ".");
                        }

                        if (isVoID(tempModel))
                            void_.add(tempModel);
                    } catch (Throwable e33) {
                    }
            } catch (Throwable ex) {
            }

        return void_;
    }

    private static Model getVoIDFromSparql(String[] sparqlEndPoints) {
        Model void_ = ModelFactory.createDefaultModel();

        try {
            for (String sparqlEndPoint : sparqlEndPoints) {
                Model tempModel = ModelFactory.createDefaultModel();

                Thread task = new Thread() {
                    @Override
                    public void run() {
                        String from = "";
                        String queryString = "construct {?s ?p ?o}\n %1swhere {?s ?p ?o.}";
                        Model m = ModelFactory.createDefaultModel();
                        try {
                            from = listVoIDGraphNames(sparqlEndPoint)
                                    .stream()
                                    .map((n) -> String.format("from <%1s>\n", n))
                                    .reduce(from, String::concat);
                            queryString = String.format(queryString, from);

                            try (QueryExecution exec = new QueryEngineHTTP(sparqlEndPoint, queryString)) {
                                ((QueryEngineHTTP) exec).setModelContentType(WebContent.contentTypeRDFXML);
                                ((QueryEngineHTTP) exec).setTimeout(DCConf.SPARQL_TIMEOUT);
                                exec.execConstruct(m);
                                tempModel.add(m);
                            }
                        } catch (Throwable e) {
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

                if (isVoID(tempModel))
                    void_.add(tempModel);
            }
        } catch (Throwable e2) {
        }

        return void_;
    }

    private static List<String> listVoIDGraphNames(String sparqlEndPoint) {
        List<String> graphNames = new ArrayList<>();

        String name;
        String queryString = "select distinct ?g where {graph ?g {?s ?p ?o.}}";
        try (QueryExecution exec = new QueryEngineHTTP(sparqlEndPoint, queryString)) {
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
}