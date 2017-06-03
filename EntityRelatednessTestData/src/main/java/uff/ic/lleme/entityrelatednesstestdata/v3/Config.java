package uff.ic.lleme.entityrelatednesstestdata.v3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    public static final String DATA_ROOT = "./resources/data/v3";
    public static final String RDF_ROOT = "../../EntityRelatednessTestData";
    public static final String DATASET_NAME = "EntityRelatednessTestData";

    public static final String ALIGN_NS = "http://knowledgeweb.semanticweb.org/heterogeneity/alignment#";
    public static final String ONTOLOGY_NS = "http://swlab.ic.uff.br/ontology/EntityRelatednessTestData_v1.rdf#";
    public static final String DATA_NS = "http://swlab.ic.uff.br/resource/";

    public static final String LOCAL_VOID_NAME = DATA_ROOT + "/void.ttl";
    public static final String LOCAL_ONTOLOGY_NAME = RDF_ROOT + "/ontology/" + DATASET_NAME + "_v1.rdf";
    public static final String XML_DUMP_NAME = RDF_ROOT + "/" + DATASET_NAME + "_v3.rdf";
    public static final String TURTLE_DUMP_NAME = RDF_ROOT + "/" + DATASET_NAME + "_v3.ttl";
    public static final String JSON_DUMP_NAME = RDF_ROOT + "/" + DATASET_NAME + "_v3.json";
    public static final String NTRIPLES_DUMP_NAME = RDF_ROOT + "/" + DATASET_NAME + "_v3.json";

    public static final String HOST_ADDR = "swlab.ic.uff.br";
    public static String USERNAME = null;
    public static String PASSWORD = null;
    public static final String REMOTE_VOID_NAME = "/tomcat/void.ttl";
    public static final String REMOTE_ONTOLOGY_NAME = "/tomcat/ontology/" + DATASET_NAME + "_v1.rdf";
    public static final String XML_REMOTE_DUMP_NAME = "/tomcat/dump/" + DATASET_NAME + "_v3.rdf";
    public static final String TURTLE_REMOTE_DUMP_NAME = "/tomcat/dump/" + DATASET_NAME + "_v3.ttl";
    public static final String JSON_REMOTE_DUMP_NAME = "/tomcat/dump/" + DATASET_NAME + "_v3.json";
    public static final String NTRIPLES_REMOTE_DUMP_NAME = "/tomcat/dump/" + DATASET_NAME + "_v3.nt";

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
