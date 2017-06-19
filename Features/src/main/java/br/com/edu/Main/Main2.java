/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Main;

import br.com.edu.Connection.ConnectionMySql;
import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.tasks.DownloadDump;
import br.com.edu.tasks.ReadRDF;
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
public class Main2 {
    public static void main(String[] args) {
        try{
            Run();
        }catch(Throwable e){
            e.printStackTrace();
        }
    }
    public static void Run() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        Integer counter = 0;
        Config.configure("./src/main/resources/conf/datasetcrawler.properties");
        ExecutorService pool = Executors.newWorkStealingPool(Config.PARALLELISM);
        //File file = new File(System.getProperty("user.dir") + "/Dumps/");
        File file = new File("/media/angelo/DATA/Dumps/");
        File files[];
        File files_dump[];
        File files_directory[];
        files = file.listFiles();
        Connection conn = ConnectionMySql.Conectar();
        for (int i = 0; i < files.length; i++) {
            
            String[] getNameDataset = files[i].toString().split("/");
            int size = getNameDataset.length - 1;
            String name_dataset = getNameDataset[size];
            int verifica_class = InsertFeaturesBD.VerificaClass(name_dataset, conn);
            int verifica_proprety = InsertFeaturesBD.VerificaProprety(name_dataset, conn);
            int verifica_entites = InsertFeaturesBD.VerificaEntites(name_dataset, conn);
            if (verifica_class == 0 && verifica_proprety == 0 && verifica_entites == 0) {
                pool.submit(new ReadRDF(files[i].toString(), name_dataset, conn));
                System.out.println((++counter) + ": Submitting task " + name_dataset);
            }
            
        }
        pool.shutdown();
        System.out.println("Waiting for remaining tasks...");
        pool.awaitTermination(Config.POOL_SHUTDOWN_TIMEOUT, Config.POOL_SHUTDOWN_TIMEOUT_UNIT);
        conn.close();
    }
    
}
