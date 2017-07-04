package uff.ic.swlab.w3cvoid;

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
import uff.ic.swlab.commons.util.SWLabHost;
import uff.ic.swlab.vocabulary.ertd.v1.RSC;

public class VoIDDescription {

    public static final SWLabHost HOST = SWLabHost.ALTERNATE_HOST;

    public final String MYVOID_NS;

    private final String ROOT_DIR = "../Root/WebContent";
    private final String LOCAL_NAME;
    private final String REMOTE_NAME;

    public final Model model = ModelFactory.createDefaultModel();

    public VoIDDescription(String nsPart, String filename) throws ParseException {
        MYVOID_NS = HOST.baseHttpUrl() + nsPart;

        LOCAL_NAME = ROOT_DIR + "/" + filename;
        REMOTE_NAME = "/tomcat/" + filename;

        model.setNsPrefix("void", VOID.NS);
        model.setNsPrefix("dcterms", DCTerms.NS);
        model.setNsPrefix("foaf", FOAF.NS);
        model.setNsPrefix("owl", OWL.NS);
        model.setNsPrefix(RSC.PREFIX, RSC.NS);
        model.setNsPrefix("", MYVOID_NS);

        EntityRelatednessTestData_v3 d1 = new EntityRelatednessTestData_v3(this);

        Resource dsDesc = model.createResource(MYVOID_NS, VOID.DatasetDescription);
        dsDesc.addProperty(DCTerms.title, "Description of the available datasets at " + HOST.baseHttpUrl())
                .addProperty(DCTerms.creator, "http://www.ic.uff.br/~lapaesleme/foaf.rdf#me")
                .addProperty(FOAF.topic, d1.root);

        model.getNsPrefixMap().putAll(d1.description.getNsPrefixMap());
        model.add(d1.description);
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, Exception {
        PropertyConfigurator.configure("./resources/conf/log4j.properties");

        try (InputStream input = new FileInputStream("./resources/conf/voiddescription.properties");) {
            Properties prop = new Properties();
            prop.load(input);

            int port = Integer.parseInt(prop.getProperty("port"));
            String USERNAME = prop.getProperty("username");
            String PASSWORD = prop.getProperty("password");

            VoIDDescription v = new VoIDDescription("void.ttl#", "void.ttl");
            (new File(v.ROOT_DIR)).mkdirs();
            try (OutputStream out = new FileOutputStream(v.LOCAL_NAME);) {
                RDFDataMgr.write(out, v.model, Lang.TURTLE);
            }

            try (InputStream in = new FileInputStream(v.LOCAL_NAME)) {
                HOST.uploadViaFTP(USERNAME, PASSWORD, v.REMOTE_NAME, in);
            }
        }
    }
}
