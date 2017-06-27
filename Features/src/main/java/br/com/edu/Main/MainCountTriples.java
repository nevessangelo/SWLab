/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Main;

import br.com.edu.Connection.ConnectionMySql;
import br.com.edu.Connection.ConnectionMySql2;
import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.tasks.CountTriples;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import uff.ic.swlab.commons.util.Config;

/**
 *
 * @author angelo
 */
public class MainCountTriples {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, InterruptedException {
        Config.configure("./src/main/resources/conf/datasetcrawler.properties");
        ExecutorService pool = Executors.newWorkStealingPool(Config.PARALLELISM);
        File file = new File("/media/angelo/DATA/Dumps/");
        File files[];
        File files_dump[];
        File files_directory[];
        files = file.listFiles();
        Integer counter = 0;
        Connection conn = ConnectionMySql2.Conectar();
        for (int i = 0; i < files.length; i++) {
            String[] getNameDataset = files[i].toString().split("/");
            int size = getNameDataset.length - 1;
            String name_dataset = "http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/"+getNameDataset[size];
            
            Double verifica_count = InsertFeaturesBD.VerificaCount(name_dataset, conn);
            if(verifica_count == 0.0 && !name_dataset.equals("http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/abs-linked-data") && !name_dataset.equals("http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/bio2rdf-pathwaycommons") && !name_dataset.equals("http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/bio2rdf-wikipathways")){
                pool.submit(new CountTriples(files[i].toString(), name_dataset, conn));
                System.out.println((++counter) + ": Submitting task " + name_dataset);
            }
            
            
        }
        pool.shutdown();
        System.out.println("Waiting for remaining tasks...");
        pool.awaitTermination(Config.POOL_SHUTDOWN_TIMEOUT, Config.POOL_SHUTDOWN_TIMEOUT_UNIT);
        conn.close();
    }
    
}
