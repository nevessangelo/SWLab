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
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.log4j.PropertyConfigurator;
import uff.ic.swlab.commons.util.Host;

public class UpdateVoid {

    private static String USERNAME = null;
    private static String PASSWORD = null;
    private static final String DATA_ROOT = "./resources/data/v3";
    private static final String RDF_ROOT = "../../EntityRelatednessTestData";
    private static final String LOCAL_VOID_NAME = DATA_ROOT + "/void.ttl";
    private static final String HOST_ADDR = "swlab.ic.uff.br";
    private static final String REMOTE_VOID_NAME = "/tomcat/void.ttl";

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, Exception {
        PropertyConfigurator.configure("./resources/conf/log4j.properties");
        configure("./resources/conf/entityrelatednesstestdata.properties");

        Model void_ = (new SWLABVoID()).getModel();

        (new File(RDF_ROOT)).mkdirs();
        try (OutputStream out = new FileOutputStream(LOCAL_VOID_NAME);) {
            RDFDataMgr.write(out, void_, Lang.TURTLE);
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
