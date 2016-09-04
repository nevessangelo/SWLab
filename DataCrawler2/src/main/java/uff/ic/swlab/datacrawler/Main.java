package uff.ic.swlab.datacrawler;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.jena.rdf.model.Model;
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

        try (Crawler<Dataset> crawler = new CatalogCrawler();) {

            SparqlServer server = new SparqlServer();
            server.dataURL = "http://localhost:8080/fuseki/void/data";
            server.sparqlURL = "http://localhost:8080/fuseki/void/spqrql";
            List<String> graphNames = server.listGraphNames();

            ExecutorService pool = Executors.newFixedThreadPool(20);
            while (crawler.hasNext()) {
                Dataset dataset = crawler.next();

                Model void_ = dataset.makeVoID();
                String[] urls = dataset.getURLs(dataset);
                String[] sparqlEndPoints = dataset.getSparqlEndPoints();
                String nameURI = dataset.getNameURI();
                String authority = Resource.getAuthority(urls);

                if (!graphNames.contains(nameURI)) {
                    pool.submit(new Task(void_, nameURI, urls, sparqlEndPoints, server));
                }
            }
            pool.shutdown();
            pool.awaitTermination(7, TimeUnit.DAYS);

        } catch (Throwable t) {
        }

        System.out.println("Crawler done.");
    }

}
