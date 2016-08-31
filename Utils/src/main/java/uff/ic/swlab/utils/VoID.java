package uff.ic.swlab.utils;

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
import org.apache.jena.riot.WebContent;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.vocabulary.RDF;

public class VoID {

    public static Model getVoID(String[] sparqlEndPoints, String[] urls) {
        Model void_ = ModelFactory.createDefaultModel();

        void_.add(VoID.getVoidFromFile(urls));
        void_.add(VoID.getVoidFromSparql(sparqlEndPoints));

        return void_;
    }

    private static Model getVoidFromFile(String[] urls) {
        Model void_ = ModelFactory.createDefaultModel();

        try {
            String[] voidURLs = listPotentialVoIDURLs(urls);
            for (String url : voidURLs)
                try {
                    RDFDataMgr.read(void_, url);
                } catch (Exception e1) {
                    Model tempModel;
                    Lang[] langs = {Lang.TURTLE, Lang.TRIG, Lang.RDFXML, Lang.NTRIPLES,
                        Lang.NQUADS, Lang.JSONLD, Lang.RDFJSON, Lang.TRIX, Lang.RDFTHRIFT};
                    boolean read = false;
                    for (Lang l : langs)
                        try {
                            tempModel = ModelFactory.createDefaultModel();
                            RDFDataMgr.read(tempModel, url, l);
                            if (tempModel.size() > 5 && isVoID(tempModel)) {
                                void_.add(tempModel);
                                read = true;
                                break;
                            }
                        } catch (Exception e2) {
                        }
                    if (!read)
                        try {
                            tempModel = ModelFactory.createDefaultModel();
                            RDFaDataMgr.read(tempModel, url);
                            if (tempModel.size() > 5 && isVoID(tempModel))
                                void_.add(tempModel);
                        } catch (Exception e3) {
                        }
                }
        } catch (Exception e) {
        }

        return void_;
    }

    private static Model getVoidFromSparql(String[] sparqlEndPoints) {
        Model void_ = ModelFactory.createDefaultModel();

        try {
            String from = "";
            String queryString = "construct {?s ?p ?o}\n %1swhere {?s ?p ?o.}";
            for (String sparqlEndPoint : sparqlEndPoints) {
                for (String n : listVoIDGraphURIs(sparqlEndPoint))
                    from += String.format("from <%1s>\n", n);
                queryString = String.format(queryString, from);

                try (QueryExecution exec = new QueryEngineHTTP(sparqlEndPoint, queryString)) {
                    ((QueryEngineHTTP) exec).setModelContentType(WebContent.contentTypeRDFXML);
                    ((QueryEngineHTTP) exec).setTimeout(20000);
                    exec.execConstruct(void_);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {

        }

        return void_;
    }

    private static List<String> listVoIDGraphURIs(String sparqlEndPoint) {
        List<String> graphNames = new ArrayList<>();

        String name;
        String queryString = "select distinct ?g where {graph ?g {?s ?p ?o.}}";
        try (QueryExecution exec = new QueryEngineHTTP(sparqlEndPoint, queryString)) {
            ((QueryEngineHTTP) exec).setTimeout(20000);
            ResultSet rs = exec.execSelect();
            while (rs.hasNext()) {
                name = rs.next().getResource("g").getURI();
                if (name.contains("void"))
                    graphNames.add(name);
            }
        } catch (Exception e) {
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
        } catch (Exception e) {
        }

        return voidURLs.toArray(new String[0]);
    }

    private static boolean isVoID(Model model) {
        org.apache.jena.rdf.model.Resource voidDataset = model.getResource("http://rdfs.org/ns/void#Dataset");
        org.apache.jena.rdf.model.Resource voidLinkset = model.getResource("http://rdfs.org/ns/void#Linkset");
        boolean hasDataset = model.contains(null, RDF.type, voidDataset);
        boolean hasLinkset = model.contains(null, RDF.type, voidLinkset);
        return hasDataset || hasLinkset;
    }

}
