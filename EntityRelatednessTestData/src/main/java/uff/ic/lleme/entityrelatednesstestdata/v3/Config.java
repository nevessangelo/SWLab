package uff.ic.lleme.entityrelatednesstestdata.v3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    public static final String DATA_ROOT = "./resources/data";
    public static final String RDF_ROOT = "../../EntityRelatednessTestData/v3";
    public static final String DATASET_NAME = "EntityRelatednessTestData";

    public static final String ALIGN_NS = "http://knowledgeweb.semanticweb.org/heterogeneity/alignment#";
    public static final String ONTOLOGY_NS = "http://swlab.ic.uff.br/ontology/EntityRelatednessTestData/v3.rdf#";
    public static final String DATA_NS = "http://swlab.ic.uff.br/resource/";

    public static final String LOCAL_VOID_NAME = DATA_ROOT + "/void.ttl";
    public static final String LOCAL_ONTOLOGY_NAME = RDF_ROOT + "/ontology/" + DATASET_NAME + ".rdf";
    public static final String XML_SERIALIZATION_NAME = RDF_ROOT + "/" + DATASET_NAME + ".rdf";
    public static final String TURTLE_SERIALIZATION_NAME = RDF_ROOT + "/" + DATASET_NAME + ".ttl";
    public static final String JSON_SERIALIZATION_NAME = RDF_ROOT + "/" + DATASET_NAME + ".json";

    public static final String HOST_ADDR = "swlab.ic.uff.br";
    public static String USERNAME = "";
    public static String PASSWORD = "";
    public static final String REMOTE_VOID_NAME = "/void.ttl";
    public static final String REMOTE_ONTOLOGY_NAME = "/ontology/" + DATASET_NAME + "/v3.rdf";
    public static final String ONTOLOGY_REMOTE_DIR = "/ontology/" + DATASET_NAME;
    public static final String DUMP_REMOTE_DIR = "/dump/" + DATASET_NAME;
    public static final String XML_SERIALIZATION_REMOTE_NAME = "/dump/" + DATASET_NAME + "/v3.rdf";
    public static final String TURTLE_SERIALIZATION_REMOTE_NAME = "/dump/" + DATASET_NAME + "/v3.ttl";
    public static final String JSON_SERIALIZATION_REMOTE_NAME = "/dump/" + DATASET_NAME + "/v3.json";

    public static final String FUSEKI_URL = "http://" + HOST_ADDR + "/fuseki";
    public static final String DATASET_URL = FUSEKI_URL + "/" + DATASET_NAME + "/data";

    public static void configure(String file) throws IOException {
        try (InputStream input = new FileInputStream(file);) {
            Properties prop = new Properties();
            prop.load(input);

            USERNAME = prop.getProperty("username");
            PASSWORD = prop.getProperty("password");
        }
    }
}
