package uff.ic.swlab.datacrawler;

import java.net.MalformedURLException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uff.ic.swlab.common.util.Resource;
import uff.ic.swlab.common.util.SparqlServer;
import uff.ic.swlab.common.util.VoID;

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

        while (crawler.hasNext()) {
            String uri = crawler.next();
            String[] urls = {uri};
            String authority = Resource.getAuthority(urls);

            server.putModel(authority, VoID.retrieveVoID(urls, null));
        }

        System.out.println("Done.");
    }

}
