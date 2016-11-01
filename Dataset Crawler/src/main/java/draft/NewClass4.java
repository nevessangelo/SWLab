package draft;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.PropertyConfigurator;
import uff.ic.swlab.commons.util.Config;
import uff.ic.swlab.commons.util.adapter.FusekiServer;
import uff.ic.swlab.datasetcrawler.CatalogCrawler;
import uff.ic.swlab.datasetcrawler.GetVoIDTask;
import uff.ic.swlab.datasetcrawler.adapter.Dataset;

public class NewClass4 {

    public static void main(String[] args) throws InterruptedException, IOException {
        PropertyConfigurator.configure("./src/main/resources/conf/log4j.properties");
        Config.configure("./src/main/resources/conf/datasetcrawler.properties");
        FusekiServer server = FusekiServer.getInstance(Config.FUSEKI_SERVER);
        CatalogCrawler crawler = new CatalogCrawler(Config.CKAN_CATALOG);

        Dataset dataset = crawler.getDataset("rkb-explorer-acm");
        String graphURI = dataset.getUri();

        ExecutorService pool = Executors.newWorkStealingPool(Config.PARALLELISM);
        pool.submit(new GetVoIDTask(dataset, graphURI, server));
        pool.shutdown();
        pool.awaitTermination(Config.POOL_SHUTDOWN_TIMEOUT, Config.POOL_SHUTDOWN_TIMEOUT_UNIT);
    }
}