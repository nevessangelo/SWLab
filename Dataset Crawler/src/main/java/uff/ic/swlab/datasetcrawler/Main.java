package uff.ic.swlab.datasetcrawler;

import uff.ic.swlab.datasetcrawler.model.Dataset;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.apache.jena.rdf.model.Model;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import uff.ic.swlab.common.util.Config;
import uff.ic.swlab.common.util.Resource;
import uff.ic.swlab.common.util.SparqlServer;

public class Main {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void run(String[] args) throws MalformedURLException, InterruptedException, IOException {
        Logger.getLogger("datacrawler");
        PropertyConfigurator.configure("./src/main/resources/conf/log4j.properties");
        System.out.println("Crawler started.");

        loadProperties();
        String oper = getOper(args);

        SparqlServer server = new SparqlServer();
        server.dataURL = Config.FUSEKI_DATASET + "/data";
        server.updateURL = Config.FUSEKI_DATASET + "/update";
        server.sparqlURL = Config.FUSEKI_DATASET + "/sparql";

        int counter = 0;
        try (Crawler<Dataset> crawler = new CatalogCrawler(Config.CKAN_CATALOG);) {

            List<String> graphNames = server.listGraphNames();
            ExecutorService pool = Executors.newWorkStealingPool(Config.PARALLELISM);
            while (crawler.hasNext()) {
                Dataset dataset = crawler.next();

                Model void_ = dataset.makeVoID();
                String[] urls = dataset.getURLs(dataset);
                String[] sparqlEndPoints = dataset.getSparqlEndPoints();
                String graphURI = dataset.getNameURI();
                String authority = Resource.getAuthority(urls);

                if (oper == null || !oper.equals("insert") || (oper.equals("insert") && !graphNames.contains(graphURI)))
                    pool.submit(new RetrieveVoIDTask(void_, urls, sparqlEndPoints, graphURI, server));

                System.out.println((++counter) + "-" + graphURI);
            }
            pool.shutdown();
            pool.awaitTermination(Config.POOL_SHUTDOWN_TIMEOUT, TimeUnit.DAYS);

        } catch (Throwable e) {
        }

        System.out.println("Crawler done.");
    }

    private static String getOper(String[] args) {
        String[] opers = {"insert", "upsert", "repsert"};
        if (args == null || args.length == 0)
            return "insert";
        else
            if (args.length == 1 && args[0] != null && !args[0].equals(""))
                if (Stream.of(opers).anyMatch(x -> x.equals(args[0])))
                    return args[0];
        throw new IllegalArgumentException("Illegal arguments.");
    }

    private static void loadProperties() throws IOException {
        try (InputStream input = new FileInputStream("./src/main/resources/conf/datasetcrawler.properties");) {
            Properties prop = new Properties();
            prop.load(input);

            Config.FUSEKI_DATASET = prop.getProperty("fusekiDataset");
            Config.CKAN_CATALOG = prop.getProperty("ckanCatalog");

            Config.CONNECTION_TIMEOUT = Integer.valueOf(prop.getProperty("connectionTimeout"));
            Config.PARALLELISM = Integer.valueOf(prop.getProperty("parallelism"));
            Config.POOL_SHUTDOWN_TIMEOUT = Integer.valueOf(prop.getProperty("poolShutdownTimeout"));
            Config.SO_TIMEOUT = Integer.valueOf(prop.getProperty("soTimeout"));
            Config.TASK_INSTANCES = Integer.valueOf(prop.getProperty("taskInstances"));
            Config.TASK_RUNNING_TIMEOUT = Long.valueOf(prop.getProperty("taskRunningTimeout"));
        }
    }

}
