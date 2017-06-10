package uff.ic.swlab.datasets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Properties;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.WebContent;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.VOID;
import org.apache.log4j.PropertyConfigurator;
import uff.ic.swlab.commons.util.Host;

public class SWLABVoID {

    public static final sTRING HOST = "http://swlab.ic.uff.br";
    public static final String NAMESPACE = HOST + "/void.ttl#";
    private static final String SPARQL_ENDPOINT_URL = "http://swlab.ic.uff.br/fuseki/%1s/sparql";
    private static final Model MODEL = ModelFactory.createDefaultModel();

    private SWLABVoID() {

    }

    public static void init() throws ParseException {
        MODEL.setNsPrefix("void", VOID.NS);
        MODEL.setNsPrefix("dcterms", DCTerms.NS);
        MODEL.setNsPrefix("foaf", FOAF.NS);
        MODEL.setNsPrefix("", NAMESPACE);

        Resource dsDesc = MODEL.createResource(NAMESPACE, VOID.DatasetDescription);
        dsDesc.addProperty(DCTerms.title, "Description of the available datasets at swlab.ic.uff.br")
                .addProperty(DCTerms.creator, "http://profile.lleme.net/foaf/#me")
                .addProperty(FOAF.topic, EntityRelatednessTestData_v3.createDatasetDescription());
    }

    public static Model execConstruct(String queryString, String dataset) {
        Model result = ModelFactory.createDefaultModel();
        String endpoint = String.format(SPARQL_ENDPOINT_URL, dataset);
        try (QueryExecution exec = new QueryEngineHTTP(endpoint, queryString)) {
            ((QueryEngineHTTP) exec).setModelContentType(WebContent.contentTypeRDFXML);
            exec.execConstruct(result);
        }
        return result;
    }

    private static final String ROOT = "./resources/data";
    private static final String LOCAL_VOID_NAME = ROOT + "/void.ttl";
    private static final String HOST_ADDR = "swlab.ic.uff.br";
    private static final String REMOTE_VOID_NAME = "/tomcat/void.ttl";
    private static String USERNAME = null;
    private static String PASSWORD = null;

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, Exception {
        PropertyConfigurator.configure("./resources/conf/log4j.properties");
        configure("./resources/conf/swlab.properties");

        SWLABVoID.init();

        (new File(ROOT)).mkdirs();
        try (OutputStream out = new FileOutputStream(LOCAL_VOID_NAME);) {
            RDFDataMgr.write(out, MODEL, Lang.TURTLE);
        } finally {
        }
        try (InputStream in = new FileInputStream(LOCAL_VOID_NAME)) {
            Host.uploadViaFTP(HOST_ADDR, USERNAME, PASSWORD, REMOTE_VOID_NAME, in);
        } finally {
        }
    }

    private static void configure(String file) throws IOException {
        try (InputStream input = new FileInputStream(file);) {
            Properties prop = new Properties();
            prop.load(input);

            USERNAME = prop.getProperty("username");
            PASSWORD = prop.getProperty("password");
        }
    }
}
