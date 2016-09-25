package uff.ic.swlab.datasetcrawler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import org.apache.log4j.PropertyConfigurator;
import uff.ic.swlab.commons.util.DCConf;
import uff.ic.swlab.commons.util.adapter.FusekiServer;
import uff.ic.swlab.datasetcrawler.model.Dataset;

public class Main {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
    }

    public static void run(String[] args) throws IOException, InterruptedException, Exception {
        PropertyConfigurator.configure("./src/main/resources/conf/log4j.properties");
        DCConf.configure("./src/main/resources/conf/datasetcrawler.properties");
        String oper = getOper(args);

        FusekiServer server = new FusekiServer(DCConf.FUSEKI_DATASET);
        Integer counter = 0;

        System.out.println("Crawler started.");
        try (Crawler<Dataset> crawler = new CatalogCrawler(DCConf.CKAN_CATALOG);) {

            List<String> graphNames = server.listGraphNames();
            ExecutorService pool = Executors.newWorkStealingPool(DCConf.PARALLELISM);
            while (crawler.hasNext()) {
                Dataset dataset = crawler.next();
                String graphURI = dataset.getNameURI();

                if (oper == null || !oper.equals("insert") || (oper.equals("insert") && !graphNames.contains(graphURI))) {
                    pool.submit(new GetVoIDTask(dataset, graphURI, server));
                    System.out.println((++counter) + ": Submitting task " + graphURI);
                } else
                    System.out.println("Skipping task " + graphURI);
            }
            pool.shutdown();
            System.out.println("Waiting for remaining tasks...");
            pool.awaitTermination(DCConf.POOL_SHUTDOWN_TIMEOUT, DCConf.POOL_SHUTDOWN_TIMEOUT_UNIT);

        }
        System.out.println("Crawler done.");
    }

    private static String getOper(String[] args) throws IllegalArgumentException {
        String[] opers = {"insert", "upsert", "repsert"};
        if (args == null || args.length == 0)
            return "insert";
        else
            if (args.length == 1 && args[0] != null && !args[0].equals(""))
                if (Stream.of(opers).anyMatch(x -> x.equals(args[0])))
                    return args[0];
        throw new IllegalArgumentException("Illegal argument list!");
    }
}
