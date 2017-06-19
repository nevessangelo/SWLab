/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Main;

import br.com.edu.Connection.ConnectionMySql;
import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.tasks.DownloadDump;
import java.io.IOException;
import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import uff.ic.swlab.commons.util.Config;
import uff.ic.swlab.datasetcrawler.CatalogCrawler;
import uff.ic.swlab.datasetcrawler.Crawler;
import uff.ic.swlab.datasetcrawler.adapter.Dataset;

/**
 *
 * @author angelo
 */
public class Main {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public static void run(String[] args) throws IOException, InterruptedException, Exception {
        String[] url_dump = null;
        Integer counter = 0;
        Config.configure("./src/main/resources/conf/datasetcrawler.properties");
        System.out.println("Crawler started.");
        Connection conn = ConnectionMySql.Conectar();
        try (Crawler<Dataset> crawler = new CatalogCrawler(Config.CKAN_CATALOG);) {
            ExecutorService pool = Executors.newWorkStealingPool(Config.PARALLELISM);
            while (crawler.hasNext()) {
                Dataset dataset = crawler.next();
                String nome_dataset = dataset.getName();
                int verifica_class = InsertFeaturesBD.VerificaClass(nome_dataset, conn);
                int verifica_proprety = InsertFeaturesBD.VerificaProprety(nome_dataset, conn);
                int verifica_entites = InsertFeaturesBD.VerificaEntites(nome_dataset, conn);
                if (verifica_class == 0 && verifica_proprety == 0 && verifica_entites == 0) {
                    System.out.println("Para o dataset: " + nome_dataset);
                    url_dump = dataset.getDumpUrls();
                    if (url_dump != null && !nome_dataset.equals("b3kat") && !nome_dataset.equals("bio2rdf-iproclass") && !nome_dataset.equals("bio2rdf-affymetrix") && !nome_dataset.equals("bio2rdf-dbsnp")) {
                        pool.submit(new DownloadDump(nome_dataset, url_dump));
                        System.out.println((++counter) + ": Submitting task " + nome_dataset);
                    }

                }

            }
            pool.shutdown();
            System.out.println("Waiting for remaining tasks...");
            pool.awaitTermination(Config.POOL_SHUTDOWN_TIMEOUT, Config.POOL_SHUTDOWN_TIMEOUT_UNIT);
        }
        conn.close();
        System.out.println("Crawler done.");

    }

}
