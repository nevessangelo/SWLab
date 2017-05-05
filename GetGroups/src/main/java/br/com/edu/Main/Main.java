/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Main;
//import uff.ic.sw

import br.com.edu.Connection.MetodosMysql;
import br.com.edu.GetGroups.GetGroups;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.PropertyConfigurator;
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
        try{
            run(args);
        }catch(Throwable e){
            e.printStackTrace();
        }
    }
    
    public static void run(String[] args) throws IOException, InterruptedException, Exception{
        ArrayList<String> dataset_non_group = new ArrayList<>();
        PropertyConfigurator.configure("./src/main/resources/conf/log4j.properties");
        Config.configure("./src/main/resources/conf/datasetcrawler.properties");
        System.out.println("Crawler Started");
        try(Crawler<Dataset> crawler = new CatalogCrawler(Config.CKAN_CATALOG); ){
            while(crawler.hasNext()){
                Dataset dataset = crawler.next();
                String nome_dataset = dataset.getName();
                String link = "http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/"+nome_dataset;
                int verifica = MetodosMysql.VerificaNome(link);
                if(verifica > 0){
                    String[] tags = dataset.getTags();
                    int retorno = GetGroups.GetGroupDataset(tags, link);
                    if(retorno == 0){
                        dataset_non_group.add(link);
                    }
                }
                    
            }
            
            
        }
        String bio = "bio";
        String geo = "geo";
        ArrayList<String> dataset_sem_grops = new ArrayList<>();
        for(String dataset: dataset_non_group){
            if(dataset.contains("bio")){
                MetodosMysql.InsertGroups(dataset, "lifesciences");
            
            }else if(dataset.contains(geo)){
                MetodosMysql.InsertGroups(dataset, "geography");
            
            }
            dataset_sem_grops.add(dataset);
            
            
        }
        
        for (String nome: dataset_sem_grops){
            MetodosMysql.InsertGroups(nome, "non-group");
        }
       
            
    }
    
}
