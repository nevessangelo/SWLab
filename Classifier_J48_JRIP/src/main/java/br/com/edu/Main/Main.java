/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Main;

import br.com.edu.Connection.Methods;
import br.com.edu.partition.Execute_Tranning;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author angelo
 */
public class Main {
    
    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, SQLException {
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 1; i <= 3; i++){
            File way_test = new File(System.getProperty("user.dir") + "/Test/"+i+"/");
            File way_tranning = new File(System.getProperty("user.dir") + "/Result/"+i+"/");
            Runnable task = new Execute_Tranning(way_test, way_tranning, i);
            Thread worker = new Thread(task);
            worker.setName(String.valueOf(i));
            worker.start();
            threads.add(worker);
            
        }
        
        
        int running = 0;
        do {
            running = 0;
            for (Thread thread : threads) {
                if (thread.isAlive()) {
                    running++;
                }
            }
            System.out.println("We have " + running + " running threads. ");
        } while (running > 0);
        ArrayList<Double> medias = Methods.GetMedia();
        Double media = Methods.Avarege(medias);
        System.out.println(media);
        Double desvio = Methods.sd(medias, media);
        System.out.println(desvio);
        
        
    }
    
}
