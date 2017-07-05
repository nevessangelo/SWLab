package uff.ic.swlab.dataset.ertd.v3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import uff.ic.swlab.commons.util.SWLabHost;

public class Config {

    public static final SWLabHost HOST = SWLabHost.DEFAULT_HOST;

    public static final String DATA_ROOT = "./resources/data/v3/raw";
    public static final String RDF_ROOT = "./resources/data/v3/rdf";
    public static final String NAME = "EntityRelatednessTestData";
    public static final String DATASET_NAME = "EntityRelatednessTestData_v3";

    public static final String LOCAL_ONTOLOGY_HOMEPAGE = RDF_ROOT + "/ontology/" + NAME + "_v1/index.jsp";
    public static final String LOCAL_ONTOLOGY_NAME = RDF_ROOT + "/ontology/" + NAME + "_v1.rdf.gz";
    public static final String LOCAL_DATASET_HOMEPAGE = RDF_ROOT + "/dataset/" + NAME + "_v3/index.jsp";
    public static final String LOCAL_XML_DUMP_NAME = RDF_ROOT + "/dataset/" + NAME + "_v3.rdf.gz";
    public static final String LOCAL_TURTLE_DUMP_NAME = RDF_ROOT + "/dataset/" + NAME + "_v3.ttl.gz";
    public static final String LOCAL_JSON_DUMP_NAME = RDF_ROOT + "/dataset/" + NAME + "_v3.json.gz";
    public static final String LOCAL_NTRIPLES_DUMP_NAME = RDF_ROOT + "/dataset/" + NAME + "_v3.nt.gz";

    public static String USERNAME = null;
    public static String PASSWORD = null;
    public static int PORT = 0;

    public static final String REMOTE_ONTOLOGY_HOMEPAGE = "/tomcat/ontology/" + NAME + "_v1/index.jsp";
    public static final String REMOTE_ONTOLOGY_NAME = "/tomcat/ontology/" + NAME + "_v1.rdf.gz";
    public static final String REMOTE_DATASET_HOMEPAGE = "/tomcat/dataset/" + NAME + "_v3/index.jsp";
    public static final String REMOTE_XML_DUMP_NAME = "/tomcat/dataset/" + NAME + "_v3.rdf.gz";
    public static final String REMOTE_TURTLE_DUMP_NAME = "/tomcat/dataset/" + NAME + "_v3.ttl.gz";
    public static final String REMOTE_JSON_DUMP_NAME = "/tomcat/dataset/" + NAME + "_v3.json.gz";
    public static final String REMOTE_NTRIPLES_DUMP_NAME = "/tomcat/dataset/" + NAME + "_v3.nt.gz";

    public static final String FUSEKI_DATA_URL = HOST.sparqlDataUrl(NAME + "_v3");

    public static void configure(String file) throws IOException {
        try (InputStream input = new FileInputStream(file);) {
            Properties prop = new Properties();
            prop.load(input);

            PORT = Integer.parseInt(prop.getProperty("port"));
            USERNAME = prop.getProperty("username");
            PASSWORD = prop.getProperty("password");
        }
    }
}
