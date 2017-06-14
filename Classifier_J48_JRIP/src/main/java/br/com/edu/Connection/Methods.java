/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Connection;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author angelo
 */
public class Methods {
    
    public static ArrayList<String> VerificaBD(int partition) throws ClassNotFoundException, SQLException{
        ArrayList<String> datasets = new ArrayList<>();
        Connection conn = br.com.edu.Connection.ConenctionBD.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT dataset FROM `Results` WHERE rotation = '" + partition + "'";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String nome_dataset = rs.getString("dataset");
                datasets.add(nome_dataset);
            }
            
        }
        return datasets;
    }
    
    public static List<Entry<String, Double>> VerificaRank(HashMap<String, Double> rank, int rotacao) throws ClassNotFoundException, SQLException {
        ArrayList<String> datasets = new ArrayList<>();
        datasets = VerificaBD(rotacao);
        for (String nome_dataset : datasets){
            if(rank.containsKey(nome_dataset)){
            }else{
                rank.put(nome_dataset, 0.0);
            }           
        }
        Set<Entry<String, Double>> set = rank.entrySet();
        List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(set);
        Collections.sort( list, new Comparator<Map.Entry<String, Double>>(){

            @Override
            public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
            
        });
        return list;
      
    }
    public static int Relevantes(String name_dataset, String ls) throws ClassNotFoundException, SQLException {
        int cont = 0;
        Connection conn = br.com.edu.Connection.ConenctionBD.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT features FROM `Features'` WHERE nome_dataset = '" + name_dataset + "' AND features = '" + ls + "' AND tipo_feature = 'Linkset' ";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                 cont++;
            }
        }
        conn.close();
        return cont;
        
        
    }
    
    public static Double GetLastRelevant(List<Map.Entry<String, Double>> rank_final, String dataset_test) throws ClassNotFoundException, SQLException{
        Double result = 0.0;
        Double total_rank = 1.0;
        int relevant = 0;
        Double posicao = 0.0;
        for(Map.Entry<String, Double> pair:rank_final){
            relevant = Relevantes(dataset_test, pair.getKey());
             if(relevant > 0){
                 posicao = total_rank;
             }
             total_rank = total_rank + 1;
            
        }
        result = posicao/total_rank;
        return result;
    }
    
    public static void InsertLastRelevant(Double result) throws ClassNotFoundException, SQLException{
        Connection conn = br.com.edu.Connection.ConenctionBD.Conectar();
        if (conn != null) {
            String query = "INSERT INTO Results_Media VALUES (?) ";
            PreparedStatement stm = conn.prepareStatement(query);
            stm.setDouble(1, result);
            stm.executeUpdate();
        }
        conn.close();
        
    }
    
    public static ArrayList<Double> GetMedia() throws ClassNotFoundException, SQLException{
        ArrayList<Double> medias = new ArrayList<>();
        Connection conn = br.com.edu.Connection.ConenctionBD.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT media FROM `Results_Media`";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Double media = rs.getDouble("media");
                medias.add(media);
                
            }
            
        }
        conn.close();
        return medias;
        
    }
    
    public static Double Avarege(ArrayList<Double> medias){
        Double sum = 0.0;
        for (Double mark : medias) {
            sum += mark;
        }
        return sum.doubleValue() / medias.size();
    }
    
    public static double sd(ArrayList<Double> table, Double mean) {
    // Step 1: 
        //double mean = mean(table);
        double temp = 0;

        for (int i = 0; i < table.size(); i++) {
            Double val = table.get(i);

            // Step 2:
            double squrDiffToMean = Math.pow(val - mean, 2);

            // Step 3:
            temp += squrDiffToMean;
        }

        // Step 4:
        double meanOfDiffs = (double) temp / (double) (table.size());

        // Step 5:
        return Math.sqrt(meanOfDiffs);
    }
    
    public static void ExportFileRelevants(int id, int rotacao, String name_dataset_test) throws IOException, ClassNotFoundException, SQLException{
        int relevants = 0;
        PrintWriter outFile = new PrintWriter(new FileWriter("/home/angelo/Área de Trabalho/Relevants"+rotacao+".txt", true));
        ArrayList<String> datasets = VerificaBD(rotacao);
        for(String dataset : datasets){
            int result = Relevantes(name_dataset_test, dataset);
            if(result > 0){
                 relevants = 1;
             }else{
                 relevants = 0;
             }
            outFile.printf("%-10s %-10s %-160s %-15s", id, "0", dataset, relevants);
            outFile.printf("\n");
        }
        outFile.close();
    }
    
    public static void ExportFileScore(int id_dataset, List<Map.Entry<String, Double>> rank_final, int rotacao) throws IOException{
        PrintWriter outFile = new PrintWriter(new FileWriter("/home/angelo/Área de Trabalho/Results"+rotacao+".txt", true)); 
        for(Map.Entry<String, Double> pair:rank_final){
            outFile.printf("%-10s %-10s %-160s %-15s %-160s %s", id_dataset, "Q0", pair.getKey(), "0", pair.getValue(), "STANDARD");
            outFile.printf("\n");
        }
        outFile.close();
        
    }

}
