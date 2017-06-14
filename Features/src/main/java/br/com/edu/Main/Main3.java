/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Main;

import br.com.edu.Connection.ConnectionMySql;
import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.tasks.GetTypes;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import uff.ic.swlab.commons.util.Config;

/**
 *
 * @author angelo
 */
public class Main3 {
    public static void main(String[] args) {
        try {
            Run();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void Run() throws SQLException, Exception {
        Config.configure("./src/main/resources/conf/datasetcrawler.properties");
        ExecutorService pool = Executors.newWorkStealingPool(Config.PARALLELISM);
        ArrayList<String> datasets = new ArrayList<>();
        Connection conn = ConnectionMySql.Conectar();
        datasets = InsertFeaturesBD.GetDatasets(conn);
        for (String nome_dataset: datasets){
            pool.submit(new GetTypes(nome_dataset, conn));
            
        }
        conn.close();
        
        

    }
    
}