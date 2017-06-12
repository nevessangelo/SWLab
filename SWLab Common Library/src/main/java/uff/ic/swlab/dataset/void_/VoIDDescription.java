package uff.ic.swlab.dataset.void_;

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
import org.semarglproject.vocab.OWL;
import uff.ic.swlab.commons.util.Host;

public class VoIDDescription {

    public static final String HOST_ADDR = "swlab.ic.uff.br";
    public static final String HOST_URL = "http://" + HOST_ADDR;
    public static final String SPARQL_ENDPOINT_URL = HOST_URL + "/fuseki/%1s/sparql";

    private static String USERNAME = null;
    private static String PASSWORD = null;

    public final String DESCRIPTION_NS;
    public final String DATASET_NS = HOST_URL + "/resource/";

    private final String ROOT = "./resources/data";
    private final String LOCAL_NAME;
    private final String REMOTE_NAME;

    public final Model model = ModelFactory.createDefaultModel();

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, Exception {
        PropertyConfigurator.configure("./resources/conf/log4j.properties");
        VoIDDescription.configure("./resources/conf/common.properties");

        {
            VoIDDescription v = new VoIDDescription("void.ttl#", "void.ttl");
            (new File(v.ROOT)).mkdirs();
            try (OutputStream out = new FileOutputStream(v.LOCAL_NAME);) {
                RDFDataMgr.write(out, v.model, Lang.TURTLE);
            } finally {
            }
            try (InputStream in = new FileInputStream(v.LOCAL_NAME)) {
                Host.uploadViaFTP(v.HOST_ADDR, v.USERNAME, v.PASSWORD, v.REMOTE_NAME, in);
            } finally {
            }
        }
        {

            VoIDDescription v = new VoIDDescription("#", "_void.ttl");

            (new File(v.ROOT)).mkdirs();
            try (OutputStream out = new FileOutputStream(v.LOCAL_NAME);) {
                RDFDataMgr.write(out, v.model, Lang.TURTLE);
            } finally {
            }
            try (InputStream in = new FileInputStream(v.LOCAL_NAME)) {
                Host.uploadViaFTP(v.HOST_ADDR, v.USERNAME, v.PASSWORD, v.REMOTE_NAME, in);
            } finally {
            }
        }
    }

    public VoIDDescription(String ns, String filename) throws ParseException {
        DESCRIPTION_NS = HOST_URL + "/" + ns;
        LOCAL_NAME = ROOT + "/" + filename;
        REMOTE_NAME = "/tomcat/" + filename;

        model.setNsPrefix("void", VOID.NS);
        model.setNsPrefix("dcterms", DCTerms.NS);
        model.setNsPrefix("foaf", FOAF.NS);
        model.setNsPrefix("owl", OWL.NS);
        model.setNsPrefix("desc", DESCRIPTION_NS);
        model.setNsPrefix("swlab", DATASET_NS);

        EntityRelatednessTestData_v3 d1 = new EntityRelatednessTestData_v3(this);

        Resource dsDesc = model.createResource(DESCRIPTION_NS, VOID.DatasetDescription);
        dsDesc.addProperty(DCTerms.title, "Description of the available datasets at " + HOST_ADDR)
                .addProperty(DCTerms.creator, "http://www.ic.uff.br/~lapaesleme/foaf.rdf#me")
                .addProperty(FOAF.topic, d1.root);

        model.getNsPrefixMap().putAll(d1.description.getNsPrefixMap());
        model.add(d1.description);
    }

    public static String getSPARQLEndpoint(String datasetname) {
        return String.format(SPARQL_ENDPOINT_URL, datasetname);
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
