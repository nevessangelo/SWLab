package uff.ic.swlab.datacrawler;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uff.ic.swlab.common.util.SparqlServer;

public class Main2 {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run(String[] args) throws MalformedURLException {
        Logger.getRootLogger().setLevel(Level.OFF);
        System.out.println("Crawler started.");

        SparqlServer server = new SparqlServer();
        server.dataURL = "http://localhost:8080/fuseki/void/data";
        LODCrawler crawler = new LODCrawler();

        ExecutorService pool = Executors.newWorkStealingPool(20);
        while (crawler.hasNext()) {
            String[] urls = {crawler.next()};
            pool.submit(new Task(null, urls, null, server, null));
        }

        System.out.println("Done.");
    }

}
