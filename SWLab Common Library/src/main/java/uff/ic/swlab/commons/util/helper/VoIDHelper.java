package uff.ic.swlab.commons.util.helper;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.vocabulary.RDF;
import uff.ic.swlab.commons.util.adapter.FusekiServer;
import uff.ic.swlab.commons.util.riot.RDFDataMgr;

public abstract class VoIDHelper {

    public static boolean isVoID(Model model) {
        if (model != null && model.size() > 0)
            return model.contains(null, RDF.type, model.getResource("http://rdfs.org/ns/void#Dataset"));
        else
            return false;
    }

    public static Model extractVoID(Model model) {
        if (model != null) {
            String queryString = ""
                    + "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                    + "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                    + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
                    + "prefix void: <http://rdfs.org/ns/void#>\n"
                    + "\n"
                    + "construct {?s1 rdf:type void:Dataset. ?s1 ?p1 ?o1. ?s2 ?p2 ?o2.}\n"
                    + "where {\n"
                    + "  graph cat:tip {\n"
                    + "    {{?s1 rdf:type void:Dataset.} union {?s1 rdf:type void:DatasetDescription.}}\n"
                    + "    ?s1 (!rdf:type)* ?s2. optional {?s1 ?p1 ?o1.} optional {?s2 ?p2 ?o2.}\n"
                    + "  }\n"
                    + "}";
            Query q = QueryFactory.create(queryString);
            QueryExecution exec = QueryExecutionFactory.create(q, model);
            return exec.execConstruct();
        } else
            return ModelFactory.createDefaultModel();
    }

    public static Model getContent(String[] urls, String[] sparqlEndPoints) throws InterruptedException {
        Model void_ = ModelFactory.createDefaultModel();
        void_.add(getVoIDFromURL(urls));
        void_.add(VoIDHelper.getVoIDFromSparql(sparqlEndPoints));
        return void_;
    }

    private static Model getVoIDFromURL(String[] urls) throws InterruptedException {
        Model void_ = ModelFactory.createDefaultModel();

        for (String url : listPotentialVoIDURLs(urls))
            try {
                if (!URLHelper.isHTML(url)) {
                    Lang[] langs = {Lang.TURTLE, Lang.RDFXML, Lang.NTRIPLES, Lang.TRIG,
                        Lang.NQUADS, Lang.JSONLD, Lang.RDFJSON, Lang.TRIX, Lang.RDFTHRIFT};
                    for (Lang lang : langs) {
                        Model tempModel = ModelFactory.createDefaultModel();
                        RDFDataMgr.read(tempModel, url, lang);
                        tempModel = extractVoID(tempModel);
                        void_.add(tempModel);
                        if (tempModel.size() > 0)
                            break;
                    }
                } else {
                    Model tempModel = ModelFactory.createDefaultModel();
                    RDFDataMgr.readRDFa(tempModel, url);
                    tempModel = extractVoID(tempModel);
                    void_.add(tempModel);
                }
            } catch (InterruptedException e) {
                throw new InterruptedException();
            } catch (Throwable e) {
            }

        return void_;
    }

    private static Model getVoIDFromSparql(String[] sparqlEndPoints) throws InterruptedException {
        Model void_ = ModelFactory.createDefaultModel();

        for (String sparqlEndPoint : sparqlEndPoints)
            try {
                Model tempModel = ModelFactory.createDefaultModel();
                FusekiServer.readVoIDFromSparql(tempModel, sparqlEndPoint);
                tempModel = extractVoID(tempModel);
                void_.add(tempModel);
            } catch (InterruptedException e) {
                throw new InterruptedException();
            } catch (Throwable e) {
            }

        return void_;
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
