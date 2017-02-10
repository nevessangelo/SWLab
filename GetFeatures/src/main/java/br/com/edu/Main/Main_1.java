/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Main;

import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.utils.SearchDump;
import eu.trentorise.opendata.jackan.model.CkanResource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.PropertyConfigurator;
import uff.ic.swlab.commons.util.Config;
import uff.ic.swlab.datasetcrawler.CatalogCrawler;
import uff.ic.swlab.datasetcrawler.Crawler;
import uff.ic.swlab.datasetcrawler.adapter.Dataset;

/**
 *
 * @author angelo
 */
public class Main_1 {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public static void run(String[] args) throws IOException, InterruptedException, Exception {
        int verifica_class, verifica_proprety = 0;
        String[] url_dump = null;
        PropertyConfigurator.configure("./src/main/resources/conf/log4j.properties");
        Config.configure("./src/main/resources/conf/datasetcrawler.properties");
        System.out.println("Crawler started.");
        Integer counter = 0;
        try (Crawler<Dataset> crawler = new CatalogCrawler(Config.CKAN_CATALOG);) {
            while (crawler.hasNext()) {
                Dataset dataset = crawler.next();
                String nome_dataset = dataset.getName();
                verifica_class = InsertFeaturesBD.VerificaClass(nome_dataset);
                verifica_proprety = InsertFeaturesBD.VerificaProprety(nome_dataset);
                if (verifica_class == 0 && verifica_proprety == 0) {
                    url_dump = dataset.getDumpUrls();
                    if (url_dump.length != 0) {
                       SearchDump.run(nome_dataset, url_dump);
                        
                    }
                }
            }
            
            

        }
        System.out.println("Crawler done.");

    }

}
