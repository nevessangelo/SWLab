/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Main;

import br.com.edu.Connection.ConnectionMySql;
import br.com.edu.Connection.InsertFeaturesBD;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
public class Notes {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, Exception {
        Integer counter = 0;
        Config.configure("./src/main/resources/conf/datasetcrawler.properties");
        Connection conn = ConnectionMySql.Conectar();
        try (Crawler<Dataset> crawler = new CatalogCrawler(Config.CKAN_CATALOG);) {
            ExecutorService pool = Executors.newWorkStealingPool(Config.PARALLELISM);
            while (crawler.hasNext()) {
                Dataset dataset = crawler.next();
                String name_dataset = dataset.getName();
                int verifica_class = InsertFeaturesBD.VerificaClass(name_dataset, conn);
                int verifica_proprety = InsertFeaturesBD.VerificaProprety(name_dataset, conn);
                int verifica_entites = InsertFeaturesBD.VerificaEntites(name_dataset, conn);
                if (verifica_class == 0 && verifica_proprety == 0 && verifica_entites == 0) {
                    String notes = dataset.getNotes();
                    pool.submit(new br.com.edu.tasks.Notes(name_dataset, notes, conn));
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
