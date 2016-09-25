package uff.ic.swlab.commons.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public abstract class DCConf {

    public static String FUSEKI_DATASET;
    public static String CKAN_CATALOG;

    public static Integer TASK_INSTANCES;
    public static Integer PARALLELISM;
    public static Integer POOL_SHUTDOWN_TIMEOUT;
    public static TimeUnit POOL_SHUTDOWN_TIMEOUT_UNIT;

    public static Long SPARQL_TIMEOUT;
    public static Long MODEL_READ_TIMEOUT;
    public static Long MODEL_WRITE_TIMEOUT;
    public static Integer HTTP_CONNECT_TIMEOUT;
    public static Integer HTTP_READ_TIMEOUT;

    public static void configure(String file) throws IOException {
        try (InputStream input = new FileInputStream(file);) {
            Properties prop = new Properties();
            prop.load(input);

            FUSEKI_DATASET = prop.getProperty("fusekiDataset");
            CKAN_CATALOG = prop.getProperty("ckanCatalog");

            TASK_INSTANCES = Integer.valueOf(prop.getProperty("taskInstances"));
            PARALLELISM = Integer.valueOf(prop.getProperty("parallelism"));
            POOL_SHUTDOWN_TIMEOUT = Integer.valueOf(prop.getProperty("poolShutdownTimeout"));
            POOL_SHUTDOWN_TIMEOUT_UNIT = TimeUnit.valueOf(prop.getProperty("poolShutdownTimeoutUnit"));

            MODEL_READ_TIMEOUT = Long.valueOf(prop.getProperty("modelReadTimeout"));
            MODEL_WRITE_TIMEOUT = Long.valueOf(prop.getProperty("modelWriteTimeout"));
            SPARQL_TIMEOUT = Long.valueOf(prop.getProperty("sparqlTimeout"));
            HTTP_CONNECT_TIMEOUT = Integer.valueOf(prop.getProperty("httpConnectTimeout"));
            HTTP_READ_TIMEOUT = Integer.valueOf(prop.getProperty("httpReadTimeout"));
        }
    }
}
