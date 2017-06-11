package uff.ic.swlab.dataset.entityrelatednesstestdata.v3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    public static final String DATA_ROOT = "./resources/data/v3/raw";
    public static final String RDF_ROOT = "./resources/data/v3/rdf";
    public static final String RDF_ROOT_0 = "../../EntityRelatednessTestData";
    public static final String DATASET_NAME = "EntityRelatednessTestData";

    public static final String DATA_NS = "http://swlab.ic.uff.br/resource/";

    public static final String LOCAL_ONTOLOGY_HOMEPAGE = RDF_ROOT + "/ontology/" + DATASET_NAME + "_v1/default.jsp";
    public static final String LOCAL_ONTOLOGY_NAME = RDF_ROOT + "/ontology/" + DATASET_NAME + "_v1.rdf";
    public static final String LOCAL_DATASET_HOMEPAGE = RDF_ROOT + "/dataset/" + DATASET_NAME + "_v3/index.html";
    public static final String LOCAL_XML_DUMP_NAME = RDF_ROOT + "/dataset/" + DATASET_NAME + "_v3.rdf";
    public static final String LOCAL_TURTLE_DUMP_NAME = RDF_ROOT + "/dataset/" + DATASET_NAME + "_v3.ttl";
    public static final String LOCAL_JSON_DUMP_NAME = RDF_ROOT + "/dataset/" + DATASET_NAME + "_v3.json";
    public static final String LOCAL_NTRIPLES_DUMP_NAME = RDF_ROOT + "/dataset/" + DATASET_NAME + "_v3.nt";

    public static final String HOST_ADDR = "swlab.ic.uff.br";
    public static String USERNAME = null;
    public static String PASSWORD = null;
    public static final String REMOTE_ONTOLOGY_HOMEPAGE = "/tomcat/ontology/" + DATASET_NAME + "_v1/default.jsp";
    public static final String REMOTE_ONTOLOGY_NAME = "/tomcat/ontology/" + DATASET_NAME + "_v1.rdf";
    public static final String REMOTE_DATASET_HOMEPAGE = "/tomcat/dataset/" + DATASET_NAME + "_v3";
    public static final String REMOTE_XML_DUMP_NAME = "/tomcat/dataset/" + DATASET_NAME + "_v3.rdf";
    public static final String REMOTE_TURTLE_DUMP_NAME = "/tomcat/dataset/" + DATASET_NAME + "_v3.ttl";
    public static final String REMOTE_JSON_DUMP_NAME = "/tomcat/dataset/" + DATASET_NAME + "_v3.json";
    public static final String REMOTE_NTRIPLES_DUMP_NAME = "/tomcat/dataset/" + DATASET_NAME + "_v3.nt";

    public static final String FUSEKI_URL = "http://" + HOST_ADDR + "/fuseki";
    public static final String DATASET_URL = FUSEKI_URL + "/" + DATASET_NAME + "_v3/data";

    public static void configure(String file) throws IOException {
        try (InputStream input = new FileInputStream(file);) {
            Properties prop = new Properties();
            prop.load(input);

            USERNAME = prop.getProperty("username");
            PASSWORD = prop.getProperty("password");
        }
    }
}
