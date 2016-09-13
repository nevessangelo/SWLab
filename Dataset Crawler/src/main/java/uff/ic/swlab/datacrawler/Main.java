package uff.ic.swlab.datacrawler;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.http.client.HttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.web.HttpOp;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
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

    public static void run(String[] args) throws MalformedURLException, InterruptedException {
        Logger.getLogger("datacrawler");
        PropertyConfigurator.configure("./src/main/resources/conf/log4j.properties");
        Logger.getLogger("datacrawler").log(Priority.INFO, "Crawler started.");

        final HttpClient httpclient = HttpOp.createCachingHttpClient();
        final HttpParams params = httpclient.getParams();
        params.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, Config.CONNECTION_TIMEOUT);
        params.setParameter(HttpConnectionParams.SO_TIMEOUT, Config.SO_TIMEOUT);
        HttpOp.setDefaultHttpClient(httpclient);

        String fuseki = "http://localhost:8080/fuseki/void";
        String catalog = "http://linkeddatacatalog.dws.informatik.uni-mannheim.de";

        SparqlServer server = new SparqlServer();
        server.dataURL = fuseki + "/data";
        server.updateURL = fuseki + "/update";
        server.sparqlURL = fuseki + "/sparql";

        try (Crawler<Dataset> crawler = new CatalogCrawler(catalog);) {

            //List<String> graphNames = server.listGraphNames();
            ExecutorService pool = Executors.newWorkStealingPool(Config.PARALLELISM);
            while (crawler.hasNext()) {
                Dataset dataset = crawler.next();

                Model void_ = dataset.makeVoID();
                String[] urls = dataset.getURLs(dataset);
                String[] sparqlEndPoints = dataset.getSparqlEndPoints();
                String graphURI = dataset.getNameURI();
                String authority = Resource.getAuthority(urls);

                //if (!graphNames.contains(nameURI))
                pool.submit(new RetrieveVoIDTask(void_, urls, sparqlEndPoints, graphURI, server));
            }
            pool.shutdown();
            pool.awaitTermination(Config.POOL_SHUTDOWN_TIMEOUT, TimeUnit.DAYS);

        } catch (Throwable e) {
        }

        Logger.getLogger("datacrawler").log(Priority.INFO, "Crawler done.");
    }

}
