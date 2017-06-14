/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.partition;

import br.com.edu.Connection.Methods;
import java.io.File;
import java.io.IOException;
import static java.lang.reflect.Array.set;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author angelo
 */
public class Execute_Tranning implements Runnable {

    private final File way_test;
    private final File way_tranning;
    private final int rotacao;

    public Execute_Tranning(File way_test, File way_tranning, int rotacao) {
        this.way_test = way_test;
        this.way_tranning = way_tranning;
        this.rotacao = rotacao;
    }

    @Override
    public void run() {
         Double score = null;
         File files_teste[];
         files_teste = way_test.listFiles();
         File files_tranning[];
         files_tranning = way_tranning.listFiles();
         for (int i = 0; i < files_teste.length; i++) {
             Double last_relevant = 0.0;
             HashMap<String, Double> map_score = new HashMap<String, Double>();
             List<Map.Entry<String, Double>> rank_final = null;
             String test = files_teste[i].toString();
            // System.out.println("Para o dataset de teste: "+ test);
             String[] v_name_dataset_test = test.split("/");
             int size_v_name_dataset_test = v_name_dataset_test.length;
             String[] retira_id = v_name_dataset_test[size_v_name_dataset_test - 1].split(":");
             int size_retira_id = retira_id.length;
             String name_dataset_test = "http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/"+retira_id[size_retira_id - 2];
             int id_dataset = Integer.parseInt(retira_id[size_retira_id - 1].replace(".arff", ""));
             try {
                 Methods.ExportFileRelevants(id_dataset, rotacao, name_dataset_test);
             } catch (IOException ex) {
                 Logger.getLogger(Execute_Tranning.class.getName()).log(Level.SEVERE, null, ex);
             } catch (ClassNotFoundException ex) {
                 Logger.getLogger(Execute_Tranning.class.getName()).log(Level.SEVERE, null, ex);
             } catch (SQLException ex) {
                 Logger.getLogger(Execute_Tranning.class.getName()).log(Level.SEVERE, null, ex);
             }
             for (int j = 0; j < files_tranning.length; j++){
                 String[] v_name_dataset = files_tranning[j].toString().split("/");
                 int size_name_dataset = v_name_dataset.length;
                 String name_dataset = "http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/"+v_name_dataset[size_name_dataset - 1].replace(".arff", "");
                 String tranning = files_tranning[j].toString();
                 try {
                     score = Tranning.Tranning_JRIP(test, tranning);
                 } catch (Exception ex) {
                     Logger.getLogger(Execute_Tranning.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 map_score.put(name_dataset,score);
                 
             }
             try {
                 rank_final = Methods.VerificaRank(map_score, rotacao);
                 Methods.ExportFileScore(id_dataset,rank_final, rotacao);
             } catch (ClassNotFoundException ex) {
                 Logger.getLogger(Execute_Tranning.class.getName()).log(Level.SEVERE, null, ex);
             } catch (SQLException ex) {
                 Logger.getLogger(Execute_Tranning.class.getName()).log(Level.SEVERE, null, ex);
             } catch (IOException ex) {
                 Logger.getLogger(Execute_Tranning.class.getName()).log(Level.SEVERE, null, ex);
             }
             try {
                 last_relevant = Methods.GetLastRelevant(rank_final, name_dataset_test);
                 Methods.InsertLastRelevant(last_relevant);
             } catch (ClassNotFoundException ex) {
                 Logger.getLogger(Execute_Tranning.class.getName()).log(Level.SEVERE, null, ex);
             } catch (SQLException ex) {
                 Logger.getLogger(Execute_Tranning.class.getName()).log(Level.SEVERE, null, ex);
             }
//             for(Map.Entry<String, Double> pair:rank_final){
//                 System.out.println(pair.getKey() + " = " + pair.getValue());                 
//             }

             
         }
    }

}
