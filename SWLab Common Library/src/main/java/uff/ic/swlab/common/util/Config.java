package uff.ic.swlab.common.util;

import java.util.concurrent.TimeUnit;

public class Config {

    public static String FUSEKI_DATASET = "http://localhost:8080/fuseki/void";
    public static String CKAN_CATALOG = "http://linkeddatacatalog.dws.informatik.uni-mannheim.de";

    public static Integer TASK_INSTANCES;
    public static Integer PARALLELISM;
    public static Long TASK_RUNNING_TIMEOUT;
    public static Integer POOL_SHUTDOWN_TIMEOUT;
    public static TimeUnit POOL_SHUTDOWN_TIMEOUT_UNIT;
    public static Long SPARQL_TIMEOUT;
    public static Long MODEL_READ_TIMEOUT;

}
