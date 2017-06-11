package uff.ic.swlab.voiddescription;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Properties;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.VOID;
import org.apache.log4j.PropertyConfigurator;
import uff.ic.swlab.commons.util.Host;

public class VoIDDescription {

    public static final String HOST_ADDR = "swlab.ic.uff.br";
    public static final String HOST_URL = "http://" + HOST_ADDR;
    public static final String NAMESPACE = HOST_URL + "/void.ttl#";
    public static final String SPARQL_ENDPOINT_URL = HOST_URL + "/fuseki/%1s/sparql";
    public static final String DATASET_NS = "http://swlab.ic.uff.br/resource/";
    public final Model model = ModelFactory.createDefaultModel();

    public VoIDDescription() throws ParseException {
        model.setNsPrefix("void", VOID.NS);
        model.setNsPrefix("dcterms", DCTerms.NS);
        model.setNsPrefix("foaf", FOAF.NS);

        EntityRelatednessTestData_v3 d1 = new EntityRelatednessTestData_v3();

        Resource dsDesc = model.createResource(NAMESPACE, VOID.DatasetDescription);
        dsDesc.addProperty(DCTerms.title, "Description of the available datasets at swlab.ic.uff.br")
                .addProperty(DCTerms.creator, "http://www.ic.uff.br/~lapaesleme/foaf/#me")
                .addProperty(FOAF.topic, d1.root);

        model.add(d1.description);
    }

    public static String getSPARQLEndpoint(String datasetname) {
        return String.format(SPARQL_ENDPOINT_URL, datasetname);
    }

    private static final String ROOT = "./resources/data";
    private static final String LOCAL_VOID_NAME = ROOT + "/void.ttl";
    private static final String REMOTE_VOID_NAME = "/tomcat/void.ttl";
    private static String USERNAME = null;
    private static String PASSWORD = null;

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, Exception {
        PropertyConfigurator.configure("./resources/conf/log4j.properties");
        configure("./resources/conf/common.properties");

        VoIDDescription v = new VoIDDescription();
        (new File(ROOT)).mkdirs();
        try (OutputStream out = new FileOutputStream(LOCAL_VOID_NAME);) {
            RDFDataMgr.write(out, v.model, Lang.TURTLE
            );
        } finally {
        }
        try (InputStream in = new FileInputStream(LOCAL_VOID_NAME)) {
            Host.uploadViaFTP(v.HOST_ADDR, v.USERNAME, PASSWORD, REMOTE_VOID_NAME, in);
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
