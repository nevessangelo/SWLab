package uff.ic.swlab.datasetcrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import uff.ic.swlab.commons.util.Conf;
import uff.ic.swlab.commons.util.adapter.FusekiServer;
import uff.ic.swlab.datasetcrawler.model.Dataset;

public class Main {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void run(String[] args) throws MalformedURLException, InterruptedException, IOException {
        Logger.getLogger("datacrawler");
        PropertyConfigurator.configure("./src/main/resources/conf/log4j.properties");
        Conf.configure("./src/main/resources/conf/datasetcrawler.properties");
        String oper = getOper(args);

        FusekiServer server = new FusekiServer(Conf.FUSEKI_DATASET);
        Integer counter = 0;

        System.out.println("Crawler started.");
        try (Crawler<Dataset> crawler = new CatalogCrawler(Conf.CKAN_CATALOG);) {

            List<String> graphNames = server.listGraphNames();
            ExecutorService pool = Executors.newWorkStealingPool(Conf.PARALLELISM);
            while (crawler.hasNext()) {
                Dataset dataset = crawler.next();

                String[] urls = dataset.getURLs(dataset);
                String[] sparqlEndPoints = dataset.getSparqlEndPoints();
                String graphURI = dataset.getNameURI();

                if (oper == null || !oper.equals("insert") || (oper.equals("insert") && !graphNames.contains(graphURI))) {
                    pool.submit(new GetVoIDTask(dataset, urls, sparqlEndPoints, graphURI, server));
                    System.out.println((++counter) + " - Submitting " + graphURI);
                } else
                    System.out.println("Skipping " + graphURI + ".");
            }
            pool.shutdown();
            System.out.println("Waiting for remaining threads...");
            pool.awaitTermination(Conf.POOL_SHUTDOWN_TIMEOUT, Conf.POOL_SHUTDOWN_TIMEOUT_UNIT);

        } catch (Throwable e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
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
}
