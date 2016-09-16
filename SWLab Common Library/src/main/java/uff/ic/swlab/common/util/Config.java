package uff.ic.swlab.common.util;

public class Config {

    public static String FUSEKI_DATASET = "http://localhost:8080/fuseki/void";
    public static String CKAN_CATALOG = "http://linkeddatacatalog.dws.informatik.uni-mannheim.de";

    public static Integer TASK_INSTANCES = 50;
    public static Integer PARALLELISM = 30;
    public static Long SPARQL_TIMEOUT = 30000l;
    public static Long MODEL_READ_TIMEOUT = 10000l;
    public static Long TASK_RUNNING_TIMEOUT = 300000l;
    public static Integer CONNECTION_TIMEOUT = 10000;
    public static Integer SO_TIMEOUT = 30000;
    public static Integer POOL_SHUTDOWN_TIMEOUT = 1;

    private Config() {

    }

}
