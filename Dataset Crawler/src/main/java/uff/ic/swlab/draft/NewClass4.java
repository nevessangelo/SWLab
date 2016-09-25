package uff.ic.swlab.draft;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.PropertyConfigurator;
import uff.ic.swlab.commons.util.DCConf;
import uff.ic.swlab.commons.util.adapter.FusekiServer;
import uff.ic.swlab.datasetcrawler.CatalogCrawler;
import uff.ic.swlab.datasetcrawler.GetVoIDTask;
import uff.ic.swlab.datasetcrawler.model.Dataset;

public class NewClass4 {

    public static void main(String[] args) throws InterruptedException, IOException {
        PropertyConfigurator.configure("./src/main/resources/conf/log4j.properties");
        DCConf.configure("./src/main/resources/conf/datasetcrawler.properties");
        FusekiServer server = new FusekiServer(DCConf.FUSEKI_DATASET);
        CatalogCrawler crawler = new CatalogCrawler(DCConf.CKAN_CATALOG);

        Dataset dataset = crawler.getDataset("tip");
        String graphURI = dataset.getNameURI();

        ExecutorService pool = Executors.newWorkStealingPool(DCConf.PARALLELISM);
        pool.submit(new GetVoIDTask(dataset, graphURI, server));
        pool.shutdown();
        pool.awaitTermination(DCConf.POOL_SHUTDOWN_TIMEOUT, DCConf.POOL_SHUTDOWN_TIMEOUT_UNIT);

    }
}
