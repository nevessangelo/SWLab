package uff.ic.swlab.datacrawler;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uff.ic.swlab.utils.Resource;
import uff.ic.swlab.utils.SparqlServer;

public class Main {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run(String[] args) throws MalformedURLException, InterruptedException {
        Logger.getRootLogger().setLevel(Level.OFF);
        System.out.println("Crawler started.");

        SparqlServer server = new SparqlServer();
        server.dataURL = "http://localhost:8080/fuseki/void/data";
        server.sparqlURL = "http://localhost:8080/fuseki/void/spqrql";
        List<String> graphNames = server.listGraphNames();
        CatalogCrawler crawler = new CatalogCrawler();

        ExecutorService pool = Executors.newFixedThreadPool(100);
        while (crawler.hasNext()) {
            Dataset dataset = crawler.next();
            String[] urls = CatalogCrawler.extractURLs(dataset);
            String[] sparqlEndPoints = CatalogCrawler.extractSparqlEndPoints(dataset);
            String authority = Resource.getAuthority(urls);
            if (!graphNames.contains(authority))
                pool.submit(new Task(server, authority, sparqlEndPoints, urls));
        }
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.DAYS);

        System.out.println("Done.");
    }

}
